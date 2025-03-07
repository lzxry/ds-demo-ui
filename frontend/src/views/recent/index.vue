<template>
  <div class="recent">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>近期数据</span>
          <div class="header-right">
            <el-select v-model="project" placeholder="选择项目" style="width: 120px">
              <el-option label="拼多多" value="pdd" />
              <el-option label="京东" value="jd" />
              <el-option label="淘宝" value="tb" />
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
      
      <el-table :data="recentList" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="projectName" label="项目" width="120" />
        <el-table-column prop="submitTime" label="提交时间" width="180" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'success' ? 'success' : 'danger'">
              {{ row.status === 'success' ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">
              查看
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
    
    <!-- 数据详情弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      title="数据详情"
      width="60%"
      destroy-on-close
    >
      <pre class="data-detail">{{ JSON.stringify(currentData, null, 2) }}</pre>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const project = ref('pdd')
const user = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(100)
const dialogVisible = ref(false)
const currentData = ref({})

// 模拟数据
const recentList = ref([
  {
    id: 1,
    username: 'user001',
    projectName: '拼多多',
    submitTime: '2024-01-01 12:00:00',
    status: 'success',
    data: {
      id: '123456',
      title: '商品标题',
      price: '99.00',
      description: '商品描述',
      images: ['http://example.com/image1.jpg']
    }
  },
  {
    id: 2,
    username: 'user002',
    projectName: '京东',
    submitTime: '2024-01-02 12:00:00',
    status: 'success',
    data: {
      id: '789012',
      title: '商品标题',
      price: '199.00',
      description: '商品描述',
      images: ['http://example.com/image2.jpg']
    }
  }
])

const handleSearch = () => {
  // 查询逻辑
}

const handleView = (row: any) => {
  currentData.value = row.data
  dialogVisible.value = true
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
.recent {
  .header-right {
    display: flex;
    align-items: center;
  }
  
  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
  
  .data-detail {
    background-color: #f5f7fa;
    padding: 15px;
    border-radius: 4px;
    max-height: 500px;
    overflow: auto;
    font-family: monospace;
    white-space: pre-wrap;
    word-break: break-all;
  }
}
</style> 