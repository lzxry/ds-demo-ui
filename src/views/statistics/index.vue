<template>
  <div class="statistics">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>数据统计</span>
          <div class="header-right">
            <el-select v-model="project" placeholder="选择项目" style="width: 120px">
              <el-option label="拼多多" value="pdd" />
              <el-option label="京东" value="jd" />
              <el-option label="淘宝" value="tb" />
            </el-select>
            <el-date-picker
              v-model="date"
              type="date"
              placeholder="选择日期"
              style="width: 120px; margin-left: 10px"
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
      
      <el-table :data="statisticsList" style="width: 100%">
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="totalTasks" label="总任务数" width="100" />
        <el-table-column prop="completedTasks" label="已完成" width="100" />
        <el-table-column prop="pendingTasks" label="未完成" width="100" />
        <el-table-column prop="settledTasks" label="已核算" width="100" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleViewDetails(row)">
              查看明细
            </el-button>
            <el-button type="success" link @click="handleSettle(row)">
              确认核算
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

    <!-- 明细弹窗 -->
    <el-dialog
      v-model="detailsVisible"
      :title="`${currentUser.username} - ${currentUser.totalTasks}`"
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
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column prop="count" label="数量" width="100" />
      </el-table>
      
      <div class="details-footer">
        <div class="summary">
          项目: {{ currentUser.projectName }} ~ {{ currentUser.date }}: {{ currentUser.totalTasks }}
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { CopyDocument } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const project = ref('pdd')
const date = ref(new Date())
const status = ref('')
const user = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(100)
const detailsVisible = ref(false)
const currentUser = ref({
  username: '',
  totalTasks: 0,
  projectName: '',
  date: ''
})

// 模拟数据
const statisticsList = ref([
  {
    username: 'user001',
    totalTasks: 100,
    completedTasks: 95,
    pendingTasks: 5,
    settledTasks: 90,
    projectName: '拼多多'
  },
  {
    username: 'user002',
    totalTasks: 80,
    completedTasks: 75,
    pendingTasks: 5,
    settledTasks: 70,
    projectName: '京东'
  }
])

// 明细数据
const detailsList = ref([
  {
    token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...',
    date: '2024-01-01',
    count: 50
  },
  {
    token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...',
    date: '2024-01-02',
    count: 50
  }
])

const handleSearch = () => {
  // 查询逻辑
}

const handleViewDetails = (row: any) => {
  currentUser.value = {
    username: row.username,
    totalTasks: row.totalTasks,
    projectName: row.projectName,
    date: date.value.toISOString().split('T')[0]
  }
  detailsVisible.value = true
}

const handleCopy = () => {
  // 构建表格数据
  const tableData = detailsList.value.map(item => 
    `${item.token}\t${item.date}\t${item.count}`
  ).join('\n')
  
  // 添加汇总信息
  const summary = `\n项目: ${currentUser.value.projectName} ~ ${currentUser.value.date}: ${currentUser.value.totalTasks}`
  
  // 复制到剪贴板
  navigator.clipboard.writeText(tableData + summary)
    .then(() => {
      ElMessage.success('复制成功')
    })
    .catch(() => {
      ElMessage.error('复制失败')
    })
}

const handleSettle = (row: any) => {
  // 确认核算逻辑
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
.statistics {
  .header-right {
    display: flex;
    align-items: center;
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
}
</style> 