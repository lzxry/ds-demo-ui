<template>
  <div class="statistics">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>数据核算</span>
          <div class="header-right">
            <el-select v-model="project" placeholder="选择项目" style="width: 120px">
              <el-option label="拼多多" value="pdd" />
              <el-option label="京东" value="jd" />
              <el-option label="淘宝" value="tb" />
            </el-select>
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :default-value="[new Date(), new Date()]"
              style="margin-left: 10px"
            />
            <el-select v-model="status" placeholder="核算状态" style="width: 120px; margin-left: 10px">
              <el-option label="全部" value="" />
              <el-option label="未核算" value="pending" />
              <el-option label="已核算" value="settled" />
            </el-select>
            <el-select v-model="user" placeholder="选择用户" style="width: 120px; margin-left: 10px">
              <el-option label="全部" value="" />
              <el-option label="user001" value="1" />
              <el-option label="user002" value="2" />
            </el-select>
            <el-button type="primary" style="margin-left: 10px" @click="handleSearch">
              查询
            </el-button>
          </div>
        </div>
      </template>
      
      <el-table 
        :data="tableData" 
        style="width: 100%"
        :row-class-name="tableRowClassName"
      >
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="totalTasks" label="下发数" width="100" />
        <el-table-column prop="completedTasks" label="已回传" width="100">
          <template #default="{ row }">
            <span class="blue-text">{{ row.completedTasks }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="pendingTasks" label="未完成" width="100" />
        <el-table-column prop="unsettledTasks" label="未核算" width="100" />
        <el-table-column prop="settledTasks" label="已核算" width="100" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <template v-if="row.username !== '总计'">
              <el-button type="primary" link @click="handleViewDetails(row)">
                查看明细
              </el-button>
              <el-button type="success" link @click="handleSettle(row)">
                确认核算
              </el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 明细弹窗 -->
    <el-dialog
      v-model="detailsVisible"
      :title="currentUser.username"
      width="800px"
      destroy-on-close
    >
      <div class="details-header">
        <el-button type="primary" @click="handleCopy">
          <el-icon><CopyDocument /></el-icon>复制数据
        </el-button>
      </div>
      
      <el-table :data="detailsList" style="width: 100%">
        <el-table-column prop="token" label="Token" />
        <el-table-column prop="remark" label="Token备注" width="150" />
        <el-table-column prop="count" label="未核对数量" width="100" />
      </el-table>
      
      <div class="details-footer">
        <div class="summary">
          {{ currentUser.projectName }}【{{ currentUser.date }}】未核算：{{ currentUser.pendingTasks }}
        </div>
      </div>
    </el-dialog>

    <!-- 确认核算弹窗 -->
    <el-dialog
      v-model="settleConfirmVisible"
      title="确认核算"
      width="500px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :show-close="false"
    >
      <div class="settle-confirm-content">
        <el-table :data="[settleUser]" border style="width: 100%">
          <el-table-column prop="username" label="用户" width="120" />
          <el-table-column prop="date" label="日期" />
          <el-table-column prop="completedTasks" label="已回传数" width="100">
            <template #default="{ row }">
              <span class="red-text">{{ row.completedTasks }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="pendingTasks" label="待核算" width="100">
            <template #default="{ row }">
              <span class="red-text">{{ row.pendingTasks }}</span>
            </template>
          </el-table-column>
        </el-table>
        <p class="confirm-question">是否确认核算？</p>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="success" @click="confirmSettle">确认核算</el-button>
          <el-button @click="settleConfirmVisible = false">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { CopyDocument } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const project = ref('pdd')
const dateRange = ref([new Date(), new Date()])
const status = ref('')
const user = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(100)
const detailsVisible = ref(false)
const settleConfirmVisible = ref(false)
const currentUser = ref({
  username: '',
  totalTasks: 0,
  projectName: '',
  date: ''
})
const settleUser = ref({
  username: '',
  totalTasks: 0,
  pendingTasks: 0,
  date: ''
})

// 模拟数据
const statisticsList = ref([
  {
    username: 'user001',
    totalTasks: 100,
    completedTasks: 95,
    pendingTasks: 5,
    unsettledTasks: 5,
    settledTasks: 90,
    projectName: '拼多多'
  },
  {
    username: 'user002',
    totalTasks: 80,
    completedTasks: 75,
    pendingTasks: 5,
    unsettledTasks: 5,
    settledTasks: 70,
    projectName: '京东'
  }
])

// 明细数据
const detailsList = ref([
  {
    token: 'pdd' + Math.random().toString(36).substring(2, 12),
    remark: '多多买菜-北京',
    count: 50
  },
  {
    token: 'jd' + Math.random().toString(36).substring(2, 12),
    remark: '京东到家-上海',
    count: 50
  }
])

// 计算汇总数据
const summaryData = computed(() => {
  const total = statisticsList.value.reduce((acc, curr) => {
    acc.totalTasks += curr.totalTasks
    acc.completedTasks += curr.completedTasks
    acc.pendingTasks += curr.pendingTasks
    acc.unsettledTasks += curr.unsettledTasks
    acc.settledTasks += curr.settledTasks
    return acc
  }, { totalTasks: 0, completedTasks: 0, pendingTasks: 0, unsettledTasks: 0, settledTasks: 0 })

  return {
    username: '总计',
    ...total,
    projectName: project.value === 'pdd' ? '拼多多' : project.value === 'jd' ? '京东' : '淘宝'
  }
})

// 合并汇总行和数据列表
const tableData = computed(() => {
  return [summaryData.value, ...statisticsList.value]
})

// 设置行的类名
const tableRowClassName = ({ row }) => {
  if (row.username === '总计') {
    return 'summary-row'
  }
  return ''
}

const handleSearch = () => {
  // 查询逻辑
}

const handleViewDetails = (row) => {
  currentUser.value = {
    username: row.username,
    totalTasks: row.totalTasks,
    pendingTasks: row.pendingTasks,
    projectName: row.projectName,
    date: dateRange.value[0].toISOString().split('T')[0] + ' 至 ' + dateRange.value[1].toISOString().split('T')[0]
  }
  detailsVisible.value = true
}

const handleCopy = () => {
  // 构建表格数据
  const tableData = detailsList.value.map(item => 
    `${item.token}\t${item.remark}\t${item.count}`
  ).join('\n')
  
  // 添加汇总信息
  const summary = `\n{{ currentUser.projectName }}【{{ currentUser.date }}】未核算：{{ currentUser.pendingTasks }}`
  
  // 复制到剪贴板
  navigator.clipboard.writeText(tableData + summary)
    .then(() => {
      ElMessage.success('复制成功')
    })
    .catch(() => {
      ElMessage.error('复制失败')
    })
}

const handleSettle = (row) => {
  settleUser.value = {
    username: row.username,
    completedTasks: row.completedTasks,
    pendingTasks: row.pendingTasks,
    date: dateRange.value[0].toISOString().split('T')[0] + ' 至 ' + dateRange.value[1].toISOString().split('T')[0]
  }
  settleConfirmVisible.value = true
}

const confirmSettle = () => {
  // 这里添加确认核算的具体逻辑
  ElMessage.success('核算成功')
  settleConfirmVisible.value = false
}

const handleSizeChange = (val) => {
  pageSize.value = val
  // 重新加载数据
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  // 重新加载数据
}
</script>

<style scoped lang="scss">
.statistics {
  .header-right {
    display: flex;
    align-items: center;
  }
  
  :deep(.el-date-editor.el-input__wrapper) {
    width: 140px !important;
  }
  
  :deep(.el-date-editor--daterange) {
    width: 140px !important;
  }
  
  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
  
  .details-header {
    margin-bottom: 20px;
    display: flex;
    justify-content: flex-end;
  }
  
  .details-footer {
    margin-top: 20px;
    padding-top: 20px;
    border-top: 1px solid #ebeef5;
    
    .summary {
      text-align: right;
      color: #606266;
      font-weight: bold;
    }
  }

  :deep(.summary-row) {
    background-color: #f5f7fa;
    font-weight: bold;
    
    td {
      background-color: #f5f7fa !important;
    }
  }

  .blue-text {
    color: #409EFF;
  }

  .settle-confirm-content {
    text-align: center;
    font-size: 16px;
    line-height: 1.8;
    padding: 20px 0;

    .confirm-question {
      margin-top: 20px;
      font-weight: bold;
      color: #303133;
    }
  }

  .red-text {
    color: #F56C6C;
    font-weight: bold;
  }

  .dialog-footer {
    display: flex;
    justify-content: center;
    gap: 20px;
  }
}
</style> 