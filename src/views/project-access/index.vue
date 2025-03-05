<template>
  <div class="project-access">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>项目接入</span>
          <el-button type="primary" @click="handleCreateAccess">
            创建接入
          </el-button>
        </div>
      </template>

      <!-- 接入方式选择 -->
      <div class="access-types">
        <el-radio-group v-model="accessType" @change="handleAccessTypeChange">
          <el-radio-button label="api">公网API接入</el-radio-button>
          <el-radio-button label="internal">内网API接入</el-radio-button>
          <el-radio-button label="file">离线文件接入</el-radio-button>
        </el-radio-group>
      </div>

      <!-- 接入列表 -->
      <el-table :data="accessList" style="width: 100%; margin-top: 20px">
        <el-table-column prop="name" label="接入名称" width="150" />
        <el-table-column prop="project" label="所属项目" width="120" />
        <el-table-column prop="type" label="接入方式" width="120">
          <template #default="{ row }">
            <el-tag :type="getAccessTypeTag(row.type)">
              {{ getAccessTypeText(row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'info'">
              {{ row.status === 'active' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastSync" label="最后同步" width="180" />
        <el-table-column label="操作" width="300" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEditAccess(row)">
              编辑
            </el-button>
            <el-button type="success" link @click="handleViewTasks(row)">
              任务管理
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
    </el-card>

    <!-- 接入配置弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="currentAccess ? '编辑接入' : '创建接入'"
      width="600px"
      destroy-on-close
    >
      <el-form
        ref="formRef"
        :model="accessForm"
        :rules="rules"
        label-width="120px"
      >
        <el-form-item label="接入名称" prop="name">
          <el-input v-model="accessForm.name" placeholder="请输入接入名称" />
        </el-form-item>
        <el-form-item label="所属项目" prop="project">
          <el-select v-model="accessForm.project" placeholder="请选择项目">
            <el-option label="拼多多" value="pdd" />
            <el-option label="京东" value="jd" />
            <el-option label="淘宝" value="tb" />
          </el-select>
        </el-form-item>
        <el-form-item label="接入方式" prop="type">
          <el-radio-group v-model="accessForm.type">
            <el-radio label="api">公网API接入</el-radio>
            <el-radio label="internal">内网API接入</el-radio>
            <el-radio label="file">离线文件接入</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 公网API配置 -->
        <template v-if="accessForm.type === 'api'">
          <el-form-item label="API地址" prop="apiUrl">
            <el-input v-model="accessForm.apiUrl" placeholder="请输入API地址" />
          </el-form-item>
          <el-form-item label="API密钥" prop="apiKey">
            <el-input v-model="accessForm.apiKey" placeholder="请输入API密钥" />
          </el-form-item>
        </template>

        <!-- 内网API配置 -->
        <template v-if="accessForm.type === 'internal'">
          <el-form-item label="API地址" prop="internalUrl">
            <el-input v-model="accessForm.internalUrl" placeholder="请输入内网API地址" />
          </el-form-item>
          <el-form-item label="认证信息" prop="authInfo">
            <el-input v-model="accessForm.authInfo" placeholder="请输入认证信息" />
          </el-form-item>
        </template>

        <!-- 离线文件配置 -->
        <template v-if="accessForm.type === 'file'">
          <el-form-item label="文件路径" prop="filePath">
            <el-input v-model="accessForm.filePath" placeholder="请输入文件路径" />
          </el-form-item>
          <el-form-item label="同步周期" prop="syncInterval">
            <el-input-number v-model="accessForm.syncInterval" :min="1" :max="24" />
            <span class="unit">小时</span>
          </el-form-item>
        </template>

        <!-- 通用配置 -->
        <el-form-item label="数据校验规则" prop="validationRules">
          <el-input
            v-model="accessForm.validationRules"
            type="textarea"
            :rows="3"
            placeholder="请输入数据校验规则"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="accessForm.status"
            :active-value="'active'"
            :inactive-value="'inactive'"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 任务管理弹窗 -->
    <el-dialog
      v-model="taskDialogVisible"
      title="任务管理"
      width="1000px"
      destroy-on-close
    >
      <div class="task-management">
        <!-- 任务统计 -->
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card shadow="hover">
              <template #header>
                <div class="stat-header">待处理任务</div>
              </template>
              <div class="stat-value">{{ taskStats.pending }}</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover">
              <template #header>
                <div class="stat-header">处理中任务</div>
              </template>
              <div class="stat-value">{{ taskStats.processing }}</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover">
              <template #header>
                <div class="stat-header">已完成任务</div>
              </template>
              <div class="stat-value">{{ taskStats.completed }}</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover">
              <template #header>
                <div class="stat-header">失败任务</div>
              </template>
              <div class="stat-value">{{ taskStats.failed }}</div>
            </el-card>
          </el-col>
        </el-row>

        <!-- 任务列表 -->
        <div class="task-list">
          <el-table :data="taskList" style="width: 100%; margin-top: 20px">
            <el-table-column prop="id" label="任务ID" width="120" />
            <el-table-column prop="type" label="任务类型" width="120">
              <template #default="{ row }">
                <el-tag :type="getTaskTypeTag(row.type)">
                  {{ getTaskTypeText(row.type) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getTaskStatusTag(row.status)">
                  {{ getTaskStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180" />
            <el-table-column prop="updateTime" label="更新时间" width="180" />
            <el-table-column label="操作" width="150" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" link @click="handleViewTaskDetail(row)">
                  查看详情
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 接入方式
const accessType = ref('api')

// 接入列表数据
const accessList = ref([
  {
    id: 1,
    name: '拼多多API接入',
    project: '拼多多',
    type: 'api',
    status: 'active',
    lastSync: '2024-01-01 12:00:00'
  },
  {
    id: 2,
    name: '京东内网接入',
    project: '京东',
    type: 'internal',
    status: 'active',
    lastSync: '2024-01-02 12:00:00'
  }
])

// 表单相关
const dialogVisible = ref(false)
const formRef = ref(null)
const currentAccess = ref(null)

const accessForm = reactive({
  name: '',
  project: '',
  type: 'api',
  apiUrl: '',
  apiKey: '',
  internalUrl: '',
  authInfo: '',
  filePath: '',
  syncInterval: 1,
  validationRules: '',
  status: 'active'
})

const rules = {
  name: [
    { required: true, message: '请输入接入名称', trigger: 'blur' }
  ],
  project: [
    { required: true, message: '请选择所属项目', trigger: 'change' }
  ],
  type: [
    { required: true, message: '请选择接入方式', trigger: 'change' }
  ],
  apiUrl: [
    { required: true, message: '请输入API地址', trigger: 'blur' }
  ],
  apiKey: [
    { required: true, message: '请输入API密钥', trigger: 'blur' }
  ],
  internalUrl: [
    { required: true, message: '请输入内网API地址', trigger: 'blur' }
  ],
  authInfo: [
    { required: true, message: '请输入认证信息', trigger: 'blur' }
  ],
  filePath: [
    { required: true, message: '请输入文件路径', trigger: 'blur' }
  ]
}

// 任务相关
const taskDialogVisible = ref(false)
const taskStats = ref({
  pending: 10,
  processing: 5,
  completed: 100,
  failed: 2
})

const taskList = ref([
  {
    id: 'T001',
    type: 'pull',
    status: 'pending',
    createTime: '2024-01-01 12:00:00',
    updateTime: '2024-01-01 12:00:00'
  },
  {
    id: 'T002',
    type: 'push',
    status: 'processing',
    createTime: '2024-01-01 12:01:00',
    updateTime: '2024-01-01 12:02:00'
  }
])

// 获取接入方式标签类型
const getAccessTypeTag = (type) => {
  const types = {
    api: 'success',
    internal: 'warning',
    file: 'info'
  }
  return types[type] || 'info'
}

// 获取接入方式文本
const getAccessTypeText = (type) => {
  const texts = {
    api: '公网API',
    internal: '内网API',
    file: '离线文件'
  }
  return texts[type] || type
}

// 获取任务类型标签类型
const getTaskTypeTag = (type) => {
  const types = {
    pull: 'primary',
    push: 'success'
  }
  return types[type] || 'info'
}

// 获取任务类型文本
const getTaskTypeText = (type) => {
  const texts = {
    pull: '拉取数据',
    push: '推送数据'
  }
  return texts[type] || type
}

// 获取任务状态标签类型
const getTaskStatusTag = (status) => {
  const types = {
    pending: 'info',
    processing: 'warning',
    completed: 'success',
    failed: 'danger'
  }
  return types[status] || 'info'
}

// 获取任务状态文本
const getTaskStatusText = (status) => {
  const texts = {
    pending: '待处理',
    processing: '处理中',
    completed: '已完成',
    failed: '失败'
  }
  return texts[status] || status
}

// 切换接入方式
const handleAccessTypeChange = (type) => {
  // 根据接入方式筛选列表
  accessList.value = accessList.value.filter(item => item.type === type)
}

// 创建接入
const handleCreateAccess = () => {
  currentAccess.value = null
  Object.assign(accessForm, {
    name: '',
    project: '',
    type: accessType.value,
    apiUrl: '',
    apiKey: '',
    internalUrl: '',
    authInfo: '',
    filePath: '',
    syncInterval: 1,
    validationRules: '',
    status: 'active'
  })
  dialogVisible.value = true
}

// 编辑接入
const handleEditAccess = (access) => {
  currentAccess.value = access
  Object.assign(accessForm, access)
  dialogVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate((valid) => {
    if (valid) {
      if (currentAccess.value) {
        // 更新接入
        const index = accessList.value.findIndex(a => a.id === currentAccess.value.id)
        if (index !== -1) {
          accessList.value[index] = {
            ...accessList.value[index],
            ...accessForm
          }
          ElMessage.success('接入更新成功')
        }
      } else {
        // 创建接入
        accessList.value.push({
          id: accessList.value.length + 1,
          ...accessForm,
          lastSync: '-'
        })
        ElMessage.success('接入创建成功')
      }
      dialogVisible.value = false
    }
  })
}

// 切换接入状态
const handleToggleStatus = (access) => {
  const action = access.status === 'active' ? '禁用' : '启用'
  ElMessageBox.confirm(
    `确定要${action}接入 ${access.name} 吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    access.status = access.status === 'active' ? 'inactive' : 'active'
    ElMessage.success(`${action}成功`)
  })
}

// 查看任务
const handleViewTasks = (access) => {
  currentAccess.value = access
  taskDialogVisible.value = true
}

// 查看任务详情
const handleViewTaskDetail = (task) => {
  // TODO: 实现任务详情查看
  ElMessage.info('任务详情功能开发中')
}
</script>

<style scoped lang="scss">
.project-access {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .access-types {
    margin: 20px 0;
  }

  .task-management {
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

    .unit {
      margin-left: 10px;
      color: #606266;
    }
  }
}
</style> 