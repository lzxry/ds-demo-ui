<template>
  <div class="integration">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>项目接入</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>添加接入
          </el-button>
        </div>
      </template>
      
      <el-table :data="integrationList" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="projectName" label="项目名称" width="150" />
        <el-table-column prop="type" label="接入方式" width="120">
          <template #default="{ row }">
            <el-tag :type="getTypeTag(row.type)">
              {{ getTypeLabel(row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'info'">
              {{ row.status === 'active' ? '运行中' : '已停止' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastSyncTime" label="最后同步时间" width="180" />
        <el-table-column prop="remark" label="备注" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="success" link @click="handleTest(row)">
              测试连接
            </el-button>
            <el-button 
              :type="row.status === 'active' ? 'warning' : 'success'" 
              link 
              @click="handleToggleStatus(row)"
            >
              {{ row.status === 'active' ? '停止' : '启动' }}
            </el-button>
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
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { Plus } from '@element-plus/icons-vue'

// 模拟数据
const integrationList = ref([
  {
    id: 1,
    projectName: '拼多多项目',
    type: 'api',
    status: 'active',
    lastSyncTime: '2024-01-01 12:00:00',
    remark: '通过公网API接入'
  },
  {
    id: 2,
    projectName: '京东项目',
    type: 'file',
    status: 'inactive',
    lastSyncTime: '2024-01-02 12:00:00',
    remark: '通过文件导入导出'
  }
])

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(100)

const getTypeTag = (type: string) => {
  const map: Record<string, string> = {
    api: 'primary',
    file: 'success',
    internal: 'warning'
  }
  return map[type] || 'info'
}

const getTypeLabel = (type: string) => {
  const map: Record<string, string> = {
    api: '公网API',
    file: '文件导入',
    internal: '内网API'
  }
  return map[type] || '未知'
}

const handleAdd = () => {
  // 添加接入逻辑
}

const handleEdit = (row: any) => {
  // 编辑接入逻辑
}

const handleTest = (row: any) => {
  // 测试连接逻辑
}

const handleToggleStatus = (row: any) => {
  // 切换状态逻辑
}

const handleSizeChange = (val: number) => {
  pageSize.value = val
  // 重新加载数据
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  // 重新加载数据
}
</script>

<style scoped lang="scss">
.integration {
  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style> 