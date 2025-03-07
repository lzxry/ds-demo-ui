<template>
  <div class="projects">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>项目列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>添加项目
          </el-button>
        </div>
      </template>
      
      <el-table :data="projectList" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="项目名称" width="150" />
        <el-table-column prop="code" label="标识码" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'info'">
              {{ row.status === 'active' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button 
              :type="row.status === 'active' ? 'warning' : 'success'" 
              link 
              @click="handleToggleStatus(row)"
            >
              {{ row.status === 'active' ? '禁用' : '启用' }}
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
const projectList = ref([
  {
    id: 1,
    name: '拼多多项目',
    code: 'PDD',
    status: 'active',
    remark: '拼多多数据采集项目',
    createTime: '2024-01-01 12:00:00'
  },
  {
    id: 2,
    name: '京东项目',
    code: 'JD',
    status: 'active',
    remark: '京东数据采集项目',
    createTime: '2024-01-02 12:00:00'
  }
])

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(100)

const handleAdd = () => {
  // 添加项目逻辑
}

const handleEdit = (row: any) => {
  // 编辑项目逻辑
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
.projects {
  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style> 