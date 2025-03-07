<template>
  <div class="user-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>接单者管理</span>
          <el-button type="primary" @click="handleCreateUser">
            创建接单者账号
          </el-button>
        </div>
      </template>

      <!-- 用户列表 -->
      <el-table :data="userList" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="contact" label="联系方式" width="150">
          <template #default="{ row }">
            <el-tag size="small" :type="getContactTypeTag(row.contactType)">
              {{ getContactTypeText(row.contactType) }}
            </el-tag>
            {{ row.contact }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="Token数量" width="100">
          <template #default="{ row }">
            <el-tag type="info">{{ row.tokenCount || 0 }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="success" link @click="handleViewStats(row)">
              查看统计
            </el-button>
            <el-button type="info" link @click="handleManageTokens(row)">
              Token管理
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- Token管理弹窗 -->
    <el-dialog
      v-model="tokenDialogVisible"
      :title="currentUser ? `为 ${currentUser.username} 生成Token` : '生成Token'"
      width="600px"
      destroy-on-close
    >
      <el-form :model="tokenForm" label-width="100px">
        <el-form-item label="生成数量">
          <el-input-number v-model="tokenForm.count" :min="1" :max="100" />
        </el-form-item>
        <el-form-item label="备注信息">
          <el-input
            v-model="tokenForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入Token备注信息"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="tokenDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleGenerateTokenSubmit">
            生成
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- Token列表弹窗 -->
    <el-dialog
      v-model="tokenListVisible"
      :title="currentUser ? `${currentUser.username} 的Token管理` : 'Token管理'"
      width="800px"
      destroy-on-close
    >
      <div class="token-header">
        <el-select v-model="tokenStatusFilter" placeholder="Token状态" style="width: 120px; margin-right: 16px">
          <el-option label="全部" value="" />
          <el-option label="正常" value="active" />
          <el-option label="已封禁" value="banned" />
        </el-select>
        <el-button type="primary" @click="handleGenerateToken(currentUser)">
          生成新Token
        </el-button>
      </div>
      <el-table :data="filteredTokenList" style="width: 100%">
        <el-table-column prop="token" label="Token" width="200" />
        <el-table-column prop="remark" label="备注" width="200" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'danger'">
              {{ row.status === 'active' ? '正常' : '已封禁' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button
              :type="row.status === 'active' ? 'danger' : 'success'"
              link
              @click="handleToggleTokenStatus(row)"
            >
              {{ row.status === 'active' ? '封禁' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 统计信息弹窗 -->
    <el-dialog
      v-model="statsDialogVisible"
      :title="`${currentUser?.username} 的任务统计`"
      width="800px"
      destroy-on-close
    >
      <div class="stats-content">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-card shadow="hover">
              <template #header>
                <div class="stat-header">今日获取任务</div>
              </template>
              <div class="stat-value">{{ todayStats.received }}</div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover">
              <template #header>
                <div class="stat-header">今日完成任务</div>
              </template>
              <div class="stat-value">{{ todayStats.completed }}</div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover">
              <template #header>
                <div class="stat-header">完成率</div>
              </template>
              <div class="stat-value">{{ todayStats.completionRate }}%</div>
            </el-card>
          </el-col>
        </el-row>

        <!-- Token任务统计表格 -->
        <div class="token-stats">
          <h4>Token任务统计</h4>
          <el-table :data="tokenStats" style="width: 100%; margin-top: 20px">
            <el-table-column prop="token" label="Token" width="200" />
            <el-table-column prop="remark" label="备注" width="200" />
            <el-table-column prop="received" label="获取任务" width="100" />
            <el-table-column prop="completed" label="完成任务" width="100" />
            <el-table-column prop="completionRate" label="完成率" width="100">
              <template #default="{ row }">
                {{ row.completionRate }}%
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-dialog>

    <!-- 用户表单弹窗 -->
    <el-dialog
      v-model="userDialogVisible"
      :title="userDialogType === 'create' ? '创建用户' : '编辑用户'"
      width="500px"
    >
      <el-form
        ref="userFormRef"
        :model="userForm"
        :rules="userRules"
        label-width="100px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="联系方式" prop="contact">
          <el-input v-model="userForm.contact" placeholder="请输入联系方式" />
        </el-form-item>
        <el-form-item label="联系方式类型" prop="contactType">
          <el-select v-model="userForm.contactType" placeholder="请选择联系方式类型">
            <el-option label="QQ" value="qq" />
            <el-option label="微信" value="wx" />
            <el-option label="Telegram" value="tg" />
            <el-option label="X(Twitter)" value="x" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="userForm.remark"
            type="textarea"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="userDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleUserSubmit">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 用户列表数据
const userList = ref([
  {
    id: 1,
    username: 'user001',
    contact: '123456789',
    contactType: 'qq',
    createTime: '2024-01-01 12:00:00',
    tokenCount: 3
  },
  {
    id: 2,
    username: 'user002',
    contact: '987654321',
    contactType: 'wx',
    createTime: '2024-01-02 12:00:00',
    tokenCount: 1
  }
])

// Token相关
const tokenDialogVisible = ref(false)
const tokenListVisible = ref(false)
const currentUser = ref(null)
const tokenStatusFilter = ref('')
const tokenForm = ref({
  count: 1,
  remark: ''
})

// Token列表数据
const tokenList = ref([
  {
    token: 'pdd1234567890',
    createTime: '2024-01-01 12:00:00',
    remark: '测试Token',
    status: 'active'
  }
])

// 计算筛选后的Token列表
const filteredTokenList = computed(() => {
  if (!tokenStatusFilter.value) {
    return tokenList.value
  }
  return tokenList.value.filter(token => token.status === tokenStatusFilter.value)
})

// 统计相关
const statsDialogVisible = ref(false)
const todayStats = ref({
  received: 100,
  completed: 95,
  completionRate: 95
})

const tokenStats = ref([
  {
    token: 'pdd1234567890',
    remark: '测试Token',
    received: 50,
    completed: 48,
    completionRate: 96
  }
])

// 用户表单相关
const userDialogVisible = ref(false)
const userDialogType = ref('create')
const userFormRef = ref(null)
const userForm = reactive({
  username: '',
  contact: '',
  contactType: '',
  remark: ''
})

const userRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  contact: [
    { required: true, message: '请输入联系方式', trigger: 'blur' }
  ],
  contactType: [
    { required: true, message: '请选择联系方式类型', trigger: 'change' }
  ]
}

// 获取联系方式类型标签
const getContactTypeTag = (type) => {
  const types = {
    qq: 'success',
    wx: 'warning',
    tg: 'info',
    x: 'danger',
    other: ''
  }
  return types[type] || ''
}

// 获取联系方式类型文本
const getContactTypeText = (type) => {
  const texts = {
    qq: 'QQ',
    wx: '微信',
    tg: 'Telegram',
    x: 'X(Twitter)',
    other: '其他'
  }
  return texts[type] || type
}

// 创建接单者账号
const handleCreateUser = () => {
  userDialogType.value = 'create'
  Object.assign(userForm, {
    username: '',
    contact: '',
    contactType: '',
    remark: ''
  })
  userDialogVisible.value = true
}

// 管理用户Token
const handleManageTokens = (user) => {
  currentUser.value = user
  // TODO: 调用获取用户Token列表接口
  tokenListVisible.value = true
}

// 生成Token
const handleGenerateToken = (user) => {
  currentUser.value = user
  tokenDialogVisible.value = true
}

// 提交Token生成
const handleGenerateTokenSubmit = () => {
  // TODO: 实现Token生成逻辑
  ElMessage.success('Token生成成功')
  tokenDialogVisible.value = false
  // 更新用户Token数量
  if (currentUser.value) {
    currentUser.value.tokenCount = (currentUser.value.tokenCount || 0) + tokenForm.value.count
  }
  // 刷新Token列表
  handleManageTokens(currentUser.value)
}

// 查看统计
const handleViewStats = (user) => {
  currentUser.value = user
  statsDialogVisible.value = true
}

// 切换Token状态
const handleToggleTokenStatus = (token) => {
  const action = token.status === 'active' ? '封禁' : '启用'
  ElMessageBox.confirm(
    `确定要${action}Token ${token.remark} 吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    token.status = token.status === 'active' ? 'banned' : 'active'
    ElMessage.success(`${action}成功`)
  })
}

// 提交用户表单
const handleUserSubmit = async () => {
  if (!userFormRef.value) return
  
  await userFormRef.value.validate((valid) => {
    if (valid) {
      // TODO: 调用创建/编辑接口
      ElMessage.success(userDialogType.value === 'create' ? '创建成功' : '编辑成功')
      userDialogVisible.value = false
    }
  })
}
</script>

<style scoped lang="scss">
.user-management {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .stats-content {
    .stat-header {
      font-size: 14px;
      color: #606266;
    }

    .stat-value {
      font-size: 24px;
      font-weight: bold;
      text-align: center;
      padding: 10px 0;
      color: #303133;
    }

    .token-stats {
      margin-top: 30px;

      h4 {
        margin-bottom: 15px;
        color: #606266;
      }
    }
  }

  .token-header {
    margin-bottom: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style> 