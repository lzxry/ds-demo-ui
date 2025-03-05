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
          <el-radio-button label="">全部</el-radio-button>
          <el-radio-button label="PULL_PUSH">拉取提交</el-radio-button>
          <el-radio-button label="PUSH_PULL">推送拉取</el-radio-button>
          <el-radio-button label="OFFLINE">离线</el-radio-button>
        </el-radio-group>
      </div>

      <!-- 接入列表 -->
      <el-table :data="accessList" style="width: 100%; margin-top: 20px">
        <el-table-column prop="name" label="接入名称" width="150" />
        <el-table-column prop="project" label="所属项目" width="120" />
        <el-table-column prop="type" label="接入方式" width="180">
          <template #default="{ row }">
            <el-tag :type="getAccessTypeTag(row.type)">
              {{ getAccessTypeText(row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="下发策略" min-width="300">
          <template #default="{ row }">
            <div class="dispatch-strategy">
              <div class="priority-info">
                <span class="label">优先级：</span>
                <el-tag :type="getPriorityTag(row.priority)" size="small">
                  {{ row.priority }}
                </el-tag>
              </div>
              <div class="time-info">
                <span class="label">时间段：</span>
                <template v-if="row.timeRanges">
                  <el-tag 
                    v-for="time in row.timeRanges.split('\n')" 
                    :key="time"
                    size="small"
                    class="time-tag"
                    :type="time === '全天' ? 'success' : 'info'"
                  >
                    {{ time }}
                  </el-tag>
                </template>
                <span v-else class="no-data">未配置</span>
              </div>
            </div>
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
            <el-button type="success" link @click="handleViewData(row)">
              查看数据
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
            <el-radio label="PULL_PUSH">拉取任务API，提交数据给甲方</el-radio>
            <el-radio label="PUSH_PULL">甲方推送任务，拉取用户数据</el-radio>
            <el-radio label="OFFLINE">离线交付</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- PULL_PUSH 配置 -->
        <template v-if="accessForm.type === 'PULL_PUSH'">
          <el-form-item label="任务API" prop="taskApi">
            <el-input v-model="accessForm.taskApi" placeholder="请输入任务API地址" />
          </el-form-item>
          <el-form-item label="任务请求方法" prop="taskMethod">
            <el-select v-model="accessForm.taskMethod" placeholder="请选择请求方法">
              <el-option label="GET" value="GET" />
              <el-option label="POST" value="POST" />
            </el-select>
          </el-form-item>
          <el-form-item v-if="accessForm.taskMethod === 'POST'" label="任务请求体" prop="taskBody">
            <el-input
              v-model="accessForm.taskBody"
              type="textarea"
              :rows="3"
              placeholder="请输入JSON格式的请求体"
            />
          </el-form-item>
          <el-form-item label="任务请求头" prop="taskHeaders">
            <el-input
              v-model="accessForm.taskHeaders"
              type="textarea"
              :rows="3"
              placeholder="请输入请求头配置，格式：key1=value1&#10;key2=value2"
            />
          </el-form-item>
          
          <!-- 数据映射配置 -->
          <el-divider>数据映射配置</el-divider>
          <el-form-item label="任务ID字段" prop="taskIdField">
            <el-input v-model="accessForm.taskIdField" placeholder="请输入任务ID在返回数据中的字段路径，如：data.taskId" />
          </el-form-item>
          <el-form-item label="任务名称字段" prop="taskNameField">
            <el-input v-model="accessForm.taskNameField" placeholder="请输入任务名称在返回数据中的字段路径" />
          </el-form-item>
          <el-form-item label="任务描述字段" prop="taskDescField">
            <el-input v-model="accessForm.taskDescField" placeholder="请输入任务描述在返回数据中的字段路径" />
          </el-form-item>
          <el-form-item label="任务金额字段" prop="taskAmountField">
            <el-input v-model="accessForm.taskAmountField" placeholder="请输入任务金额在返回数据中的字段路径" />
          </el-form-item>
          <el-form-item label="任务截止时间字段" prop="taskDeadlineField">
            <el-input v-model="accessForm.taskDeadlineField" placeholder="请输入任务截止时间在返回数据中的字段路径" />
          </el-form-item>
          <el-form-item label="自定义字段映射" prop="customFields">
            <el-input
              v-model="accessForm.customFields"
              type="textarea"
              :rows="3"
              placeholder="请输入自定义字段映射，格式：&#10;目标字段名=源字段路径&#10;例如：&#10;category=data.type&#10;level=data.difficulty"
            />
          </el-form-item>
          <el-form-item label="字段转换规则" prop="fieldTransforms">
            <el-input
              v-model="accessForm.fieldTransforms"
              type="textarea"
              :rows="3"
              placeholder="请输入字段转换规则，格式：&#10;字段路径=转换函数&#10;例如：&#10;data.amount=parseFloat&#10;data.status=status => status === 1 ? 'active' : 'inactive'"
            />
          </el-form-item>

          <el-form-item label="提交API" prop="submitApi">
            <el-input v-model="accessForm.submitApi" placeholder="请输入提交数据API地址" />
          </el-form-item>
          <el-form-item label="提交请求方法" prop="submitMethod">
            <el-select v-model="accessForm.submitMethod" placeholder="请选择请求方法">
              <el-option label="GET" value="GET" />
              <el-option label="POST" value="POST" />
            </el-select>
          </el-form-item>
          <el-form-item v-if="accessForm.submitMethod === 'POST'" label="提交请求体" prop="submitBody">
            <el-input
              v-model="accessForm.submitBody"
              type="textarea"
              :rows="3"
              placeholder="请输入JSON格式的请求体"
            />
          </el-form-item>
          <el-form-item label="提交请求头" prop="submitHeaders">
            <el-input
              v-model="accessForm.submitHeaders"
              type="textarea"
              :rows="3"
              placeholder="请输入请求头配置，格式：key1=value1&#10;key2=value2"
            />
          </el-form-item>
        </template>

        <!-- PUSH_PULL 配置 -->
        <template v-if="accessForm.type === 'PUSH_PULL'">
          <el-divider>推送任务配置</el-divider>
          <el-form-item label="推送API" prop="pushApi">
            <el-input v-model="accessForm.pushApi" placeholder="请输入推送任务API地址" />
            <div class="form-tip">甲方通过此接口向我们推送任务</div>
          </el-form-item>
          <el-form-item label="推送请求方法" prop="pushMethod">
            <el-select v-model="accessForm.pushMethod" placeholder="请选择请求方法">
              <el-option label="POST" value="POST" />
            </el-select>
          </el-form-item>
          <el-form-item label="推送请求体" prop="pushBody">
            <el-input
              v-model="accessForm.pushBody"
              type="textarea"
              :rows="3"
              placeholder="请输入JSON格式的请求体"
            />
            <div class="form-tip">示例：&#10;{&#10;  "projectCode": "项目唯一码",&#10;  "tasks": [{&#10;    "taskId": "任务ID",&#10;    "title": "任务标题",&#10;    "description": "任务描述",&#10;    "amount": "任务金额",&#10;    "deadline": "截止时间"&#10;  }]&#10;}</div>
          </el-form-item>
          <el-form-item label="推送请求头" prop="pushHeaders">
            <el-input
              v-model="accessForm.pushHeaders"
              type="textarea"
              :rows="3"
              placeholder="请输入请求头配置，格式：key1=value1&#10;key2=value2"
            />
          </el-form-item>

          <el-divider>拉取数据配置</el-divider>
          <el-form-item label="拉取API" prop="pullApi">
            <el-input v-model="accessForm.pullApi" placeholder="请输入拉取数据API地址" />
            <div class="form-tip">甲方通过此接口拉取用户提交的任务数据</div>
          </el-form-item>
          <el-form-item label="拉取请求方法" prop="pullMethod">
            <el-select v-model="accessForm.pullMethod" placeholder="请选择请求方法">
              <el-option label="GET" value="GET" />
            </el-select>
          </el-form-item>
          <el-form-item label="拉取请求头" prop="pullHeaders">
            <el-input
              v-model="accessForm.pullHeaders"
              type="textarea"
              :rows="3"
              placeholder="请输入请求头配置，格式：key1=value1&#10;key2=value2"
            />
          </el-form-item>

          <el-divider>分页配置</el-divider>
          <el-form-item label="分页参数名" prop="paginationConfig">
            <el-row :gutter="20">
              <el-col :span="8">
                <el-input v-model="accessForm.pageParam" placeholder="页码参数名" />
              </el-col>
              <el-col :span="8">
                <el-input v-model="accessForm.pageSizeParam" placeholder="每页条数参数名" />
              </el-col>
              <el-col :span="8">
                <el-input v-model="accessForm.totalParam" placeholder="总条数参数名" />
              </el-col>
            </el-row>
            <div class="form-tip">示例：page=1&pageSize=20&total=100</div>
          </el-form-item>

          <el-divider>数据映射配置</el-divider>
          <el-form-item label="任务ID字段" prop="taskIdField">
            <el-input v-model="accessForm.taskIdField" placeholder="请输入任务ID在返回数据中的字段路径" />
          </el-form-item>
          <el-form-item label="任务状态字段" prop="taskStatusField">
            <el-input v-model="accessForm.taskStatusField" placeholder="请输入任务状态在返回数据中的字段路径" />
          </el-form-item>
          <el-form-item label="任务结果字段" prop="taskResultField">
            <el-input v-model="accessForm.taskResultField" placeholder="请输入任务结果在返回数据中的字段路径" />
          </el-form-item>
          <el-form-item label="提交时间字段" prop="submitTimeField">
            <el-input v-model="accessForm.submitTimeField" placeholder="请输入提交时间在返回数据中的字段路径" />
          </el-form-item>
          <el-form-item label="自定义字段映射" prop="customFields">
            <el-input
              v-model="accessForm.customFields"
              type="textarea"
              :rows="3"
              placeholder="请输入自定义字段映射，格式：&#10;目标字段名=源字段路径&#10;例如：&#10;category=data.type&#10;level=data.difficulty"
            />
          </el-form-item>
        </template>

        <!-- OFFLINE 配置 -->
        <template v-if="accessForm.type === 'OFFLINE'">
          <el-form-item label="任务模板" prop="taskTemplate">
            <el-upload
              class="upload-demo"
              action="/api/upload"
              :on-success="handleTaskTemplateSuccess"
              :before-upload="beforeTaskTemplateUpload"
            >
              <el-button type="primary">上传任务模板</el-button>
            </el-upload>
          </el-form-item>
          <el-form-item label="数据模板" prop="dataTemplate">
            <el-upload
              class="upload-demo"
              action="/api/upload"
              :on-success="handleDataTemplateSuccess"
              :before-upload="beforeDataTemplateUpload"
            >
              <el-button type="primary">上传数据模板</el-button>
            </el-upload>
          </el-form-item>
          <el-form-item label="同步周期" prop="syncInterval">
            <el-input-number v-model="accessForm.syncInterval" :min="1" :max="24" />
            <span class="unit">小时</span>
          </el-form-item>
        </template>

        <!-- 任务下发策略配置 -->
        <el-divider>任务下发策略配置</el-divider>
        
        <!-- 优先级配置 -->
        <el-form-item label="任务优先级" prop="priority">
          <el-input-number 
            v-model="accessForm.priority" 
            :min="1" 
            :max="10" 
            :step="1"
            placeholder="数字越大优先级越高"
          />
          <div class="form-tip">设置任务优先级，数字越大优先级越高，相同时间段内优先下发高优先级任务</div>
        </el-form-item>

        <!-- 时间段配置 -->
        <el-form-item label="下发时间段" prop="timeRanges">
          <div class="day-settings">
            <el-radio-group v-model="dayType">
              <el-radio label="all">全天</el-radio>
              <el-radio label="custom">自定义时间段</el-radio>
            </el-radio-group>
            <div v-if="dayType === 'custom'" class="time-settings">
              <el-select
                v-model="selectedHours"
                multiple
                collapse-tags
                collapse-tags-tooltip
                placeholder="请选择时间点"
                style="width: 100%"
                :multiple-limit="6"
              >
                <el-option
                  v-for="hour in hours"
                  :key="hour.value"
                  :label="hour.label"
                  :value="hour.value"
                />
              </el-select>
              <div class="form-tip">最多可选择6个时间点</div>
            </div>
          </div>
          <div class="form-tip">选择全天或设置具体的时间点</div>
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

    <!-- 数据统计弹窗 -->
    <el-dialog
      v-model="dataDialogVisible"
      title="数据统计"
      width="80%"
      destroy-on-close
    >
      <div class="accounting-stats">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card class="accounting-card">
              <template #header>
                <div class="card-header">
                  <span>用户核算</span>
                </div>
              </template>
              <div class="stats-content">
                <div class="stats-item">
                  <span class="label">今日完成数：</span>
                  <span class="value">{{ dataStats.userAccounting.todayCompleted }}</span>
                </div>
                <div class="stats-item">
                  <span class="label">今日金额：</span>
                  <span class="value">¥{{ dataStats.userAccounting.todayAmount }}</span>
                </div>
                <div class="stats-item">
                  <span class="label">累计完成数：</span>
                  <span class="value">{{ dataStats.userAccounting.totalCompleted }}</span>
                </div>
                <div class="stats-item">
                  <span class="label">累计金额：</span>
                  <span class="value">¥{{ dataStats.userAccounting.totalAmount }}</span>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card class="accounting-card">
              <template #header>
                <div class="card-header">
                  <span>客户核算</span>
                </div>
              </template>
              <div class="stats-content">
                <div class="stats-item">
                  <span class="label">今日完成数：</span>
                  <span class="value">{{ dataStats.clientAccounting.todayCompleted }}</span>
                </div>
                <div class="stats-item">
                  <span class="label">今日金额：</span>
                  <span class="value">¥{{ dataStats.clientAccounting.todayAmount }}</span>
                </div>
                <div class="stats-item">
                  <span class="label">累计完成数：</span>
                  <span class="value">{{ dataStats.clientAccounting.totalCompleted }}</span>
                </div>
                <div class="stats-item">
                  <span class="label">累计金额：</span>
                  <span class="value">¥{{ dataStats.clientAccounting.totalAmount }}</span>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 接入类型
const accessType = ref('')

// 原始接入列表数据
const originalAccessList = ref([
  {
    id: 1,
    name: '拼多多API接入',
    project: '拼多多',
    type: 'PULL_PUSH',
    status: 'active',
    lastSync: '2024-01-20 10:00:00',
    priority: 8,
    timeRanges: '全天'
  },
  {
    id: 2,
    name: '京东推送接入',
    project: '京东',
    type: 'PUSH_PULL',
    status: 'active',
    lastSync: '2024-01-20 09:30:00',
    priority: 6,
    timeRanges: '1点\n5点\n13点\n22点'
  },
  {
    id: 3,
    name: '淘宝离线接入',
    project: '淘宝',
    type: 'OFFLINE',
    status: 'active',
    lastSync: '2024-01-20 09:00:00',
    priority: 4,
    timeRanges: '全天'
  }
])

// 当前显示的接入列表数据
const accessList = ref([...originalAccessList.value])

// 表单相关
const dialogVisible = ref(false)
const formRef = ref(null)
const currentAccess = ref(null)

const accessForm = reactive({
  name: '',
  project: '',
  type: 'PULL_PUSH',
  taskApi: '',
  taskMethod: 'GET',
  taskBody: '',
  taskHeaders: '',
  taskIdField: '',
  taskNameField: '',
  taskDescField: '',
  taskAmountField: '',
  taskDeadlineField: '',
  customFields: '',
  fieldTransforms: '',
  submitApi: '',
  submitMethod: 'POST',
  submitBody: '',
  submitHeaders: '',
  pushApi: '',
  pushMethod: 'POST',
  pushBody: '',
  pushHeaders: '',
  pullApi: '',
  pullMethod: 'GET',
  pullBody: '',
  pullHeaders: '',
  apiKey: '',
  authInfo: '',
  taskTemplate: '',
  dataTemplate: '',
  syncInterval: 24,
  pageParam: 'page',
  pageSizeParam: 'pageSize',
  totalParam: 'total',
  taskStatusField: '',
  taskResultField: '',
  submitTimeField: '',
  priority: 5,
  timeRanges: '',
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
  ]
}

// 数据统计相关
const dataDialogVisible = ref(false)
const dataStats = ref({
  userAccounting: {
    todayCompleted: 0,
    todayAmount: 0,
    totalCompleted: 0,
    totalAmount: 0
  },
  clientAccounting: {
    todayCompleted: 0,
    todayAmount: 0,
    totalCompleted: 0,
    totalAmount: 0
  }
})

// 获取接入类型标签样式
const getAccessTypeTag = (type) => {
  const types = {
    PULL_PUSH: 'success',
    PUSH_PULL: 'warning',
    OFFLINE: 'info'
  }
  return types[type] || 'info'
}

// 获取接入类型显示文本
const getAccessTypeText = (type) => {
  const texts = {
    PULL_PUSH: '拉取提交',
    PUSH_PULL: '推送拉取',
    OFFLINE: '离线'
  }
  return texts[type] || type
}

// 处理接入类型变更
const handleAccessTypeChange = (type) => {
  // 如果选择全部，显示所有数据
  if (!type) {
    accessList.value = [...originalAccessList.value]
    return
  }
  // 根据接入类型从原始数据中筛选
  accessList.value = originalAccessList.value.filter(item => item.type === type)
}

// 创建接入
const handleCreateAccess = () => {
  currentAccess.value = null
  Object.assign(accessForm, {
    name: '',
    project: '',
    type: 'PULL_PUSH',
    taskApi: '',
    taskMethod: 'GET',
    taskBody: '',
    taskHeaders: '',
    taskIdField: '',
    taskNameField: '',
    taskDescField: '',
    taskAmountField: '',
    taskDeadlineField: '',
    customFields: '',
    fieldTransforms: '',
    submitApi: '',
    submitMethod: 'POST',
    submitBody: '',
    submitHeaders: '',
    pushApi: '',
    pushMethod: 'POST',
    pushBody: '',
    pushHeaders: '',
    pullApi: '',
    pullMethod: 'GET',
    pullBody: '',
    pullHeaders: '',
    apiKey: '',
    authInfo: '',
    taskTemplate: '',
    dataTemplate: '',
    syncInterval: 24,
    pageParam: 'page',
    pageSizeParam: 'pageSize',
    totalParam: 'total',
    taskStatusField: '',
    taskResultField: '',
    submitTimeField: '',
    priority: 5,
    timeRanges: '',
  })
  dialogVisible.value = true
  dayType.value = 'all'
  selectedHours.value = []
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
  
  // 处理时间段配置
  let timeRanges = []
  if (dayType.value === 'all') {
    timeRanges = ['全天']
  } else if (dayType.value === 'custom' && selectedHours.value.length > 0) {
    timeRanges = selectedHours.value
      .sort((a, b) => a - b)
      .map(hour => `${hour}点`)
  }
  accessForm.timeRanges = timeRanges.join('\n')
  
  await formRef.value.validate((valid) => {
    if (valid) {
      if (currentAccess.value) {
        // 更新接入
        const index = originalAccessList.value.findIndex(a => a.id === currentAccess.value.id)
        if (index !== -1) {
          originalAccessList.value[index] = {
            ...originalAccessList.value[index],
            ...accessForm
          }
          // 更新当前显示的列表
          accessList.value = originalAccessList.value.filter(item => item.type === accessType.value)
          ElMessage.success('接入更新成功')
        }
      } else {
        // 创建接入
        const newAccess = {
          id: originalAccessList.value.length + 1,
          ...accessForm,
          status: 'active',
          lastSync: '-'
        }
        originalAccessList.value.push(newAccess)
        // 更新当前显示的列表
        accessList.value = originalAccessList.value.filter(item => item.type === accessType.value)
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

// 文件上传相关方法
const handleTaskTemplateSuccess = (response) => {
  accessForm.taskTemplate = response.url
  ElMessage.success('任务模板上传成功')
}

const handleDataTemplateSuccess = (response) => {
  accessForm.dataTemplate = response.url
  ElMessage.success('数据模板上传成功')
}

const beforeTaskTemplateUpload = (file) => {
  const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
  if (!isExcel) {
    ElMessage.error('只能上传 Excel 文件！')
    return false
  }
  return true
}

const beforeDataTemplateUpload = (file) => {
  const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
  if (!isExcel) {
    ElMessage.error('只能上传 Excel 文件！')
    return false
  }
  return true
}

// 在 script setup 部分添加新的响应式变量
const dayType = ref('all')
const selectedHours = ref([])

// 生成24小时选项
const hours = Array.from({ length: 24 }, (_, i) => ({
  value: i,
  label: `${i}点`
}))

// 在 script setup 部分添加优先级标签方法
const getPriorityTag = (priority) => {
  if (priority >= 8) return 'danger'
  if (priority >= 5) return 'warning'
  return 'info'
}

// 获取任务类型标签样式
const getTaskTypeTag = (type) => {
  const types = {
    PULL: 'success',
    PUSH: 'warning',
    SYNC: 'info'
  }
  return types[type] || 'info'
}

// 获取任务类型显示文本
const getTaskTypeText = (type) => {
  const texts = {
    PULL: '拉取',
    PUSH: '推送',
    SYNC: '同步'
  }
  return texts[type] || type
}

// 获取任务状态标签样式
const getTaskStatusTag = (status) => {
  const types = {
    pending: 'info',
    processing: 'warning',
    completed: 'success',
    failed: 'danger'
  }
  return types[status] || 'info'
}

// 获取任务状态显示文本
const getTaskStatusText = (status) => {
  const texts = {
    pending: '待处理',
    processing: '处理中',
    completed: '已完成',
    failed: '失败'
  }
  return texts[status] || status
}

// 查看数据
const handleViewData = (row) => {
  dataDialogVisible.value = true
  // 模拟获取数据
  dataStats.value = {
    userAccounting: {
      todayCompleted: 150,
      todayAmount: 1500,
      totalCompleted: 1500,
      totalAmount: 15000
    },
    clientAccounting: {
      todayCompleted: 120,
      todayAmount: 1200,
      totalCompleted: 1200,
      totalAmount: 12000
    }
  }
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
    padding: 20px;

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
  }

  .upload-demo {
    margin-top: 10px;
  }

  .form-tip {
    font-size: 12px;
    color: #909399;
    margin-top: 4px;
    line-height: 1.5;
  }

  .day-settings {
    display: flex;
    flex-direction: column;
    gap: 10px;
    margin-top: 10px;
  }

  .time-settings {
    margin-top: 10px;
  }

  .time-tag {
    margin-right: 8px;
    margin-bottom: 4px;
  }

  .time-settings {
    margin-top: 10px;
    
    .form-tip {
      margin-top: 8px;
    }
  }

  .dispatch-strategy {
    display: flex;
    flex-direction: column;
    gap: 8px;

    .priority-info,
    .time-info {
      display: flex;
      align-items: center;
      gap: 8px;

      .label {
        color: #606266;
        font-size: 13px;
      }
    }

    .time-info {
      flex-wrap: wrap;
      gap: 4px;

      .time-tag {
        margin-right: 4px;
        margin-bottom: 4px;
      }

      .no-data {
        color: #909399;
        font-size: 13px;
      }
    }
  }
}
</style> 