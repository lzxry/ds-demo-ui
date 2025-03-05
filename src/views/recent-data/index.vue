<template>
  <div class="recent-data">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>近期数据查看</span>
          <div class="header-actions">
            <el-button type="primary" link @click="handleRefresh">
              <el-icon><Refresh /></el-icon>
              刷新
            </el-button>
            <el-button type="primary" link @click="handleColumnSettings">
              <el-icon><Setting /></el-icon>
              列设置
            </el-button>
          </div>
        </div>
      </template>

      <!-- 筛选条件 -->
      <el-form :model="filterForm" label-width="100px" class="filter-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="项目">
              <el-select 
                v-model="filterForm.project" 
                placeholder="请选择项目" 
                clearable
                @change="handleProjectChange"
              >
                <el-option
                  v-for="project in projectList"
                  :key="project.id"
                  :label="project.name"
                  :value="project.code"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="用户">
              <el-select 
                v-model="filterForm.user" 
                placeholder="请选择用户" 
                clearable
                :disabled="!filterForm.project"
                @change="handleUserChange"
              >
                <el-option
                  v-for="user in userList"
                  :key="user.id"
                  :label="user.username"
                  :value="user.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="Token">
              <el-select 
                v-model="filterForm.token" 
                placeholder="请选择Token" 
                clearable
                :disabled="!filterForm.user"
              >
                <el-option
                  v-for="token in tokenList"
                  :key="token.token"
                  :label="`${token.remark}|${token.token}`"
                  :value="token.token"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="时间范围">
              <el-date-picker
                v-model="filterForm.dateRange"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                :shortcuts="dateShortcuts"
                value-format="YYYY-MM-DD HH:mm:ss"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item>
              <el-button type="primary" @click="handleSearch" :loading="loading">
                查询
              </el-button>
              <el-button @click="handleReset">重置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <!-- 最新数据列表 -->
      <div class="latest-data">
        <div class="list-header">
          <span class="title">最新数据（最近20条）</span>
        </div>
        <el-table :data="latestData" style="width: 100%; margin-bottom: 20px">
          <el-table-column type="index" label="序号" width="80" />
          <el-table-column prop="body" label="回传内容" min-width="400">
            <template #default="{ row }">
              <el-tooltip
                :content="row.body"
                placement="top"
                :show-after="500"
                :hide-after="0"
              >
                <div class="body-content">{{ row.body }}</div>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column prop="submitTime" label="回传时间" width="180" />
        </el-table>
      </div>

      <!-- 数据列表 -->
      <div v-if="showDataList" class="data-list">
        <div class="list-header">
          <span class="title">最新提交数据（最近20条）</span>
          <div class="header-actions">
            <el-button type="primary" link @click="handleCopyData">
              <el-icon><CopyDocument /></el-icon>
              复制数据
            </el-button>
            <el-button type="primary" link @click="handleExport">
              <el-icon><Download /></el-icon>
              导出数据
            </el-button>
          </div>
        </div>

        <el-table 
          :data="dataList" 
          style="width: 100%" 
          border
          v-loading="loading"
          @sort-change="handleSortChange"
        >
          <el-table-column type="expand">
            <template #default="props">
              <el-form label-position="left" inline class="table-expand">
                <el-form-item label="数据内容">
                  <pre class="json-content">{{ formatJson(props.row.content) }}</pre>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
          <el-table-column 
            v-for="col in visibleColumns" 
            :key="col.prop"
            :prop="col.prop"
            :label="col.label"
            :width="col.width"
            :sortable="col.sortable"
          >
            <template #default="{ row }" v-if="col.prop === 'token'">
              {{ row.tokenRemark }}|{{ row.token }}
            </template>
            <template #default="{ row }" v-if="col.prop === 'content'">
              <el-tooltip
                :content="formatJson(row.content)"
                placement="top"
                :show-after="500"
                :hide-after="0"
              >
                <div class="content-preview">{{ formatJson(row.content) }}</div>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" link @click="handleViewDetail(row)">
                查看详情
              </el-button>
              <el-button type="success" link @click="handleCopyRow(row)">
                复制
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </el-card>

    <!-- 数据详情弹窗 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="数据详情"
      width="800px"
      destroy-on-close
    >
      <div v-if="currentDetail" class="data-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="ID">{{ currentDetail.id }}</el-descriptions-item>
          <el-descriptions-item label="提交时间">{{ currentDetail.submitTime }}</el-descriptions-item>
          <el-descriptions-item label="用户">{{ currentDetail.username }}</el-descriptions-item>
          <el-descriptions-item label="Token">{{ currentDetail.tokenRemark }}|{{ currentDetail.token }}</el-descriptions-item>
          <el-descriptions-item label="项目">{{ currentDetail.project }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getProcessStatusTag(currentDetail.status)">
              {{ getProcessStatusText(currentDetail.status) }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>

        <div class="detail-content">
          <div class="content-header">
            <h4>数据内容</h4>
            <el-button type="primary" link @click="handleCopyDetail">
              <el-icon><CopyDocument /></el-icon>
              复制
            </el-button>
          </div>
          <pre class="json-content">{{ formatJson(currentDetail.content) }}</pre>
        </div>
      </div>
    </el-dialog>

    <!-- 列设置弹窗 -->
    <el-dialog
      v-model="columnSettingsVisible"
      title="列设置"
      width="500px"
    >
      <el-checkbox-group v-model="selectedColumns">
        <el-checkbox 
          v-for="col in allColumns" 
          :key="col.prop"
          :label="col.prop"
        >
          {{ col.label }}
        </el-checkbox>
      </el-checkbox-group>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh, Setting, CopyDocument, Download } from '@element-plus/icons-vue'

// 筛选表单
const filterForm = reactive({
  project: '',
  user: '',
  token: '',
  dateRange: []
})

// 日期快捷选项
const dateShortcuts = [
  {
    text: '最近一小时',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000)
      return [start, end]
    }
  },
  {
    text: '最近一天',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24)
      return [start, end]
    }
  },
  {
    text: '最近一周',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
      return [start, end]
    }
  }
]

// 表格列配置
const allColumns = [
  { prop: 'id', label: 'ID', width: 80, sortable: true },
  { prop: 'submitTime', label: '提交时间', width: 180, sortable: true },
  { prop: 'username', label: '用户', width: 120 },
  { prop: 'token', label: 'Token', width: 200 },
  { prop: 'project', label: '项目', width: 120 },
  { prop: 'content', label: '数据内容', minWidth: 200 },
  { prop: 'status', label: '状态', width: 100 }
]

const selectedColumns = ref(allColumns.map(col => col.prop))
const visibleColumns = computed(() => 
  allColumns.filter(col => selectedColumns.value.includes(col.prop))
)

// 分页相关
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(100)

// 加载状态
const loading = ref(false)

// 弹窗控制
const detailDialogVisible = ref(false)
const columnSettingsVisible = ref(false)
const currentDetail = ref(null)

// 项目变化时清空用户和Token
const handleProjectChange = () => {
  filterForm.user = ''
  filterForm.token = ''
}

// 用户变化时清空Token
const handleUserChange = () => {
  filterForm.token = ''
}

// 查询数据
const handleSearch = async () => {
  if (!filterForm.project && !filterForm.user && !filterForm.token && !filterForm.dateRange?.length) {
    ElMessage.warning('请至少选择一个筛选条件')
    return
  }
  loading.value = true
  showDataList.value = true
  try {
    // TODO: 调用接口获取数据
    await new Promise(resolve => setTimeout(resolve, 1000))
  } finally {
    loading.value = false
  }
}

// 重置筛选条件
const handleReset = () => {
  Object.assign(filterForm, {
    project: '',
    user: '',
    token: '',
    dateRange: []
  })
  showDataList.value = false
  dataList.value = []
}

// 刷新数据
const handleRefresh = () => {
  if (showDataList.value) {
    handleSearch()
  }
}

// 复制行数据
const handleCopyRow = (row) => {
  const text = formatJson(row.content)
  navigator.clipboard.writeText(text).then(() => {
    ElMessage.success('复制成功')
  })
}

// 复制详情数据
const handleCopyDetail = () => {
  if (currentDetail.value) {
    const text = formatJson(currentDetail.value.content)
    navigator.clipboard.writeText(text).then(() => {
      ElMessage.success('复制成功')
    })
  }
}

// 复制所有数据
const handleCopyData = () => {
  const text = dataList.value.map(row => formatJson(row.content)).join('\n')
  navigator.clipboard.writeText(text).then(() => {
    ElMessage.success('复制成功')
  })
}

// 导出数据
const handleExport = () => {
  // TODO: 实现数据导出功能
  ElMessage.success('数据导出成功')
}

// 查看详情
const handleViewDetail = (row) => {
  currentDetail.value = row
  detailDialogVisible.value = true
}

// 列设置
const handleColumnSettings = () => {
  columnSettingsVisible.value = true
}

// 排序变化
const handleSortChange = ({ prop, order }) => {
  // TODO: 实现排序逻辑
  console.log('排序变化:', prop, order)
}

// 分页大小变化
const handleSizeChange = (val) => {
  pageSize.value = val
  handleSearch()
}

// 页码变化
const handleCurrentChange = (val) => {
  currentPage.value = val
  handleSearch()
}

// 获取数据类型标签类型
const getDataTypeTag = (type) => {
  const types = {
    order: 'success',
    product: 'warning',
    user: 'info'
  }
  return types[type] || 'info'
}

// 获取数据类型文本
const getDataTypeText = (type) => {
  const texts = {
    order: '订单数据',
    product: '商品数据',
    user: '用户数据'
  }
  return texts[type] || type
}

// 获取数据质量标签类型
const getQualityTag = (quality) => {
  const types = {
    good: 'success',
    normal: 'warning',
    poor: 'danger'
  }
  return types[quality] || 'info'
}

// 获取数据质量文本
const getQualityText = (quality) => {
  const texts = {
    good: '优质',
    normal: '一般',
    poor: '较差'
  }
  return texts[quality] || quality
}

// 获取处理状态标签类型
const getProcessStatusTag = (status) => {
  const types = {
    pending: 'info',
    processing: 'warning',
    completed: 'success',
    failed: 'danger'
  }
  return types[status] || 'info'
}

// 获取处理状态文本
const getProcessStatusText = (status) => {
  const texts = {
    pending: '待处理',
    processing: '处理中',
    completed: '已完成',
    failed: '失败'
  }
  return texts[status] || status
}

// 格式化JSON
const formatJson = (content) => {
  if (typeof content === 'string') {
    try {
      return JSON.stringify(JSON.parse(content), null, 2)
    } catch (e) {
      return content
    }
  }
  return JSON.stringify(content, null, 2)
}

// 最新数据列表
const latestData = ref([
  {
    body: '{"taskId": "PDD_2024012001", "result": "success", "data": {"score": 95, "comment": "质量良好"}}',
    submitTime: '2024-01-20 15:30:22'
  },
  {
    body: '{"taskId": "JD_2024012002", "result": "success", "data": {"score": 88, "comment": "符合要求"}}',
    submitTime: '2024-01-20 15:28:45'
  },
  {
    body: '{"taskId": "TB_2024012003", "result": "success", "data": {"score": 92, "comment": "完成度好"}}',
    submitTime: '2024-01-20 15:25:18'
  },
  {
    body: '{"taskId": "PDD_2024012004", "result": "success", "data": {"score": 85, "comment": "基本达标"}}',
    submitTime: '2024-01-20 15:20:33'
  },
  {
    body: '{"taskId": "JD_2024012005", "result": "success", "data": {"score": 90, "comment": "质量优秀"}}',
    submitTime: '2024-01-20 15:15:56'
  },
  {
    body: '{"taskId": "TB_2024012006", "result": "success", "data": {"score": 87, "comment": "符合标准"}}',
    submitTime: '2024-01-20 15:10:42'
  },
  {
    body: '{"taskId": "PDD_2024012007", "result": "success", "data": {"score": 93, "comment": "完成出色"}}',
    submitTime: '2024-01-20 15:05:27'
  },
  {
    body: '{"taskId": "JD_2024012008", "result": "success", "data": {"score": 89, "comment": "质量良好"}}',
    submitTime: '2024-01-20 15:00:15'
  },
  {
    body: '{"taskId": "TB_2024012009", "result": "success", "data": {"score": 91, "comment": "符合要求"}}',
    submitTime: '2024-01-20 14:55:38'
  },
  {
    body: '{"taskId": "PDD_2024012010", "result": "success", "data": {"score": 86, "comment": "基本达标"}}',
    submitTime: '2024-01-20 14:50:24'
  },
  {
    body: '{"taskId": "JD_2024012011", "result": "success", "data": {"score": 94, "comment": "质量优秀"}}',
    submitTime: '2024-01-20 14:45:19'
  },
  {
    body: '{"taskId": "TB_2024012012", "result": "success", "data": {"score": 88, "comment": "符合标准"}}',
    submitTime: '2024-01-20 14:40:33'
  },
  {
    body: '{"taskId": "PDD_2024012013", "result": "success", "data": {"score": 92, "comment": "完成出色"}}',
    submitTime: '2024-01-20 14:35:47'
  },
  {
    body: '{"taskId": "JD_2024012014", "result": "success", "data": {"score": 87, "comment": "质量良好"}}',
    submitTime: '2024-01-20 14:30:22'
  },
  {
    body: '{"taskId": "TB_2024012015", "result": "success", "data": {"score": 90, "comment": "符合要求"}}',
    submitTime: '2024-01-20 14:25:15'
  },
  {
    body: '{"taskId": "PDD_2024012016", "result": "success", "data": {"score": 85, "comment": "基本达标"}}',
    submitTime: '2024-01-20 14:20:38'
  },
  {
    body: '{"taskId": "JD_2024012017", "result": "success", "data": {"score": 93, "comment": "质量优秀"}}',
    submitTime: '2024-01-20 14:15:42'
  },
  {
    body: '{"taskId": "TB_2024012018", "result": "success", "data": {"score": 89, "comment": "符合标准"}}',
    submitTime: '2024-01-20 14:10:27'
  },
  {
    body: '{"taskId": "PDD_2024012019", "result": "success", "data": {"score": 91, "comment": "完成出色"}}',
    submitTime: '2024-01-20 14:05:33'
  },
  {
    body: '{"taskId": "JD_2024012020", "result": "success", "data": {"score": 86, "comment": "质量良好"}}',
    submitTime: '2024-01-20 14:00:19'
  }
])
</script>

<style scoped lang="scss">
.recent-data {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .header-actions {
      display: flex;
      gap: 10px;
    }
  }

  .filter-form {
    margin-bottom: 20px;
    width: 70%;
    margin-left: auto;
    margin-right: auto;
  }

  .latest-data {
    margin-bottom: 30px;
    width: 70%;
    margin-left: auto;
    margin-right: auto;
    
    .list-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 15px;

      .title {
        font-size: 16px;
        font-weight: bold;
        color: #303133;
      }
    }

    .body-content {
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      max-width: 500px;
    }
  }

  .data-list {
    width: 70%;
    margin-left: auto;
    margin-right: auto;
    
    .list-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;

      .title {
        font-size: 16px;
        font-weight: bold;
        color: #303133;
      }

      .header-actions {
        display: flex;
        gap: 10px;
      }
    }

    .content-preview {
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .pagination-container {
      margin-top: 20px;
      display: flex;
      justify-content: flex-end;
    }
  }

  .data-detail {
    .detail-content {
      margin-top: 20px;

      .content-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 10px;

        h4 {
          margin: 0;
          color: #606266;
        }
      }

      .json-content {
        background-color: #f5f7fa;
        padding: 15px;
        border-radius: 4px;
        font-family: monospace;
        white-space: pre-wrap;
        word-break: break-all;
        max-height: 400px;
        overflow-y: auto;
        color: #333;
        line-height: 1.5;
      }
    }
  }

  .table-expand {
    padding: 20px;
    background-color: #f5f7fa;
    border-radius: 4px;
  }
}
</style> 