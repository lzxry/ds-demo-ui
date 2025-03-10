<template>
  <div class="security-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>安全管理</span>
        </div>
      </template>

      <el-tabs v-model="activeTab">
        <!-- 访问控制 -->
        <el-tab-pane label="访问控制" name="access">
          <div class="tab-content">
            <div class="section-header">
              <h3>IP白名单</h3>
              <el-button type="primary" size="small" @click="handleAddIp">添加IP</el-button>
            </div>
            <el-table :data="ipWhitelist" style="width: 100%">
              <el-table-column prop="ip" label="IP地址" />
              <el-table-column prop="description" label="描述" />
              <el-table-column prop="createdAt" label="添加时间" />
              <el-table-column label="操作" width="120">
                <template #default="{ row }">
                  <el-button type="danger" link @click="handleDeleteIp(row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>

            <div class="section-header" style="margin-top: 30px">
              <h3>API访问限制</h3>
              <el-button type="primary" size="small" @click="handleEditApiLimit">编辑限制</el-button>
            </div>
            <el-descriptions :column="2" border>
              <el-descriptions-item label="单IP请求频率限制">{{ apiLimits.rateLimit }} 次/分钟</el-descriptions-item>
              <el-descriptions-item label="单Token请求频率限制">{{ apiLimits.tokenLimit }} 次/分钟</el-descriptions-item>
              <el-descriptions-item label="单IP并发请求数">{{ apiLimits.concurrentLimit }} 个</el-descriptions-item>
              <el-descriptions-item label="异常IP自动封禁">{{ apiLimits.autoBan ? '启用' : '禁用' }}</el-descriptions-item>
            </el-descriptions>
          </div>
        </el-tab-pane>

        <!-- 认证授权 -->
        <el-tab-pane label="认证授权" name="auth">
          <div class="tab-content">
            <div class="section-header">
              <h3>认证设置</h3>
              <el-button type="primary" size="small" @click="handleEditAuthSettings">编辑设置</el-button>
            </div>
            <el-descriptions :column="2" border>
              <el-descriptions-item label="密码策略">{{ authSettings.passwordPolicy }}</el-descriptions-item>
              <el-descriptions-item label="密码过期时间">{{ authSettings.passwordExpiry }} 天</el-descriptions-item>
              <el-descriptions-item label="登录失败锁定">{{ authSettings.loginLockThreshold }} 次后锁定</el-descriptions-item>
              <el-descriptions-item label="锁定时间">{{ authSettings.lockDuration }} 分钟</el-descriptions-item>
              <el-descriptions-item label="会话超时时间">{{ authSettings.sessionTimeout }} 分钟</el-descriptions-item>
              <el-descriptions-item label="双因素认证">{{ authSettings.twoFactorAuth ? '启用' : '禁用' }}</el-descriptions-item>
            </el-descriptions>

            <div class="section-header" style="margin-top: 30px">
              <h3>角色权限管理</h3>
              <el-button type="primary" size="small" @click="handleAddRole">添加角色</el-button>
            </div>
            <el-table :data="roles" style="width: 100%">
              <el-table-column prop="name" label="角色名称" />
              <el-table-column prop="description" label="描述" />
              <el-table-column prop="userCount" label="用户数量" />
              <el-table-column label="操作" width="200">
                <template #default="{ row }">
                  <el-button type="primary" link @click="handleEditRole(row)">编辑</el-button>
                  <el-button type="success" link @click="handleAssignPermissions(row)">分配权限</el-button>
                  <el-button type="danger" link @click="handleDeleteRole(row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>

        <!-- 日志管理 -->
        <el-tab-pane label="日志管理" name="logs">
          <div class="tab-content">
            <div class="section-header">
              <h3>操作日志</h3>
              <div class="filter-container">
                <el-date-picker
                  v-model="logDateRange"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  format="YYYY-MM-DD"
                  value-format="YYYY-MM-DD"
                  style="width: 260px"
                />
                <el-select v-model="logType" placeholder="日志类型" style="width: 120px; margin-left: 10px">
                  <el-option label="全部" value="" />
                  <el-option label="登录" value="login" />
                  <el-option label="操作" value="operation" />
                  <el-option label="系统" value="system" />
                </el-select>
                <el-button type="primary" @click="handleSearchLogs" style="margin-left: 10px">查询</el-button>
                <el-button type="success" @click="handleExportLogs">导出</el-button>
              </div>
            </div>
            <el-table :data="logs" style="width: 100%">
              <el-table-column prop="timestamp" label="时间" width="180" />
              <el-table-column prop="username" label="用户" width="120" />
              <el-table-column prop="ip" label="IP地址" width="140" />
              <el-table-column prop="type" label="类型" width="100">
                <template #default="{ row }">
                  <el-tag :type="getLogTypeTag(row.type)">{{ row.type }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="action" label="操作" />
              <el-table-column prop="status" label="状态" width="100">
                <template #default="{ row }">
                  <el-tag :type="row.status === 'success' ? 'success' : 'danger'">
                    {{ row.status === 'success' ? '成功' : '失败' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="详情" width="80" fixed="right">
                <template #default="{ row }">
                  <el-button type="primary" link @click="handleViewLogDetail(row)">查看</el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="pagination-container">
              <el-pagination
                v-model:current-page="currentPage"
                v-model:page-size="pageSize"
                :page-sizes="[10, 20, 50, 100]"
                layout="total, sizes, prev, pager, next, jumper"
                :total="totalLogs"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
              />
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- IP白名单添加弹窗 -->
    <el-dialog v-model="ipDialogVisible" title="添加IP白名单" width="500px">
      <el-form :model="ipForm" label-width="100px">
        <el-form-item label="IP地址" prop="ip">
          <el-input v-model="ipForm.ip" placeholder="请输入IP地址" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="ipForm.description" placeholder="请输入描述信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="ipDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitIpForm">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- API限制编辑弹窗 -->
    <el-dialog v-model="apiLimitDialogVisible" title="编辑API访问限制" width="500px">
      <el-form :model="apiLimitForm" label-width="180px">
        <el-form-item label="单IP请求频率限制" prop="rateLimit">
          <el-input-number v-model="apiLimitForm.rateLimit" :min="1" :max="1000" /> 次/分钟
        </el-form-item>
        <el-form-item label="单Token请求频率限制" prop="tokenLimit">
          <el-input-number v-model="apiLimitForm.tokenLimit" :min="1" :max="1000" /> 次/分钟
        </el-form-item>
        <el-form-item label="单IP并发请求数" prop="concurrentLimit">
          <el-input-number v-model="apiLimitForm.concurrentLimit" :min="1" :max="100" /> 个
        </el-form-item>
        <el-form-item label="异常IP自动封禁" prop="autoBan">
          <el-switch v-model="apiLimitForm.autoBan" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="apiLimitDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitApiLimitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 认证设置编辑弹窗 -->
    <el-dialog v-model="authSettingsDialogVisible" title="编辑认证设置" width="500px">
      <el-form :model="authSettingsForm" label-width="140px">
        <el-form-item label="密码策略" prop="passwordPolicy">
          <el-select v-model="authSettingsForm.passwordPolicy" placeholder="请选择密码策略">
            <el-option label="简单" value="simple" />
            <el-option label="中等" value="medium" />
            <el-option label="复杂" value="complex" />
          </el-select>
        </el-form-item>
        <el-form-item label="密码过期时间" prop="passwordExpiry">
          <el-input-number v-model="authSettingsForm.passwordExpiry" :min="0" :max="365" /> 天
        </el-form-item>
        <el-form-item label="登录失败锁定" prop="loginLockThreshold">
          <el-input-number v-model="authSettingsForm.loginLockThreshold" :min="0" :max="10" /> 次后锁定
        </el-form-item>
        <el-form-item label="锁定时间" prop="lockDuration">
          <el-input-number v-model="authSettingsForm.lockDuration" :min="1" :max="1440" /> 分钟
        </el-form-item>
        <el-form-item label="会话超时时间" prop="sessionTimeout">
          <el-input-number v-model="authSettingsForm.sessionTimeout" :min="1" :max="1440" /> 分钟
        </el-form-item>
        <el-form-item label="双因素认证" prop="twoFactorAuth">
          <el-switch v-model="authSettingsForm.twoFactorAuth" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="authSettingsDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitAuthSettingsForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 当前激活的标签页
const activeTab = ref('access')

// IP白名单数据
const ipWhitelist = ref([
  { ip: '192.168.1.100', description: '开发环境', createdAt: '2023-05-15 10:30:00' },
  { ip: '10.0.0.1', description: '办公网络', createdAt: '2023-05-16 14:20:00' },
  { ip: '203.0.113.1', description: '测试服务器', createdAt: '2023-05-17 09:15:00' }
])

// API限制数据
const apiLimits = ref({
  rateLimit: 100,
  tokenLimit: 200,
  concurrentLimit: 10,
  autoBan: true
})

// 认证设置数据
const authSettings = ref({
  passwordPolicy: '复杂',
  passwordExpiry: 90,
  loginLockThreshold: 5,
  lockDuration: 30,
  sessionTimeout: 120,
  twoFactorAuth: true
})

// 角色数据
const roles = ref([
  { id: 1, name: '管理员', description: '系统管理员', userCount: 3 },
  { id: 2, name: '操作员', description: '系统操作员', userCount: 8 },
  { id: 3, name: '审计员', description: '系统审计员', userCount: 2 },
  { id: 4, name: '访客', description: '只读访问', userCount: 12 }
])

// 日志数据
const logs = ref([])
const logDateRange = ref([])
const logType = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const totalLogs = ref(0)

// 弹窗控制
const ipDialogVisible = ref(false)
const apiLimitDialogVisible = ref(false)
const authSettingsDialogVisible = ref(false)
const roleDialogVisible = ref(false)
const permissionDialogVisible = ref(false)

// 表单数据
const ipForm = ref({ ip: '', description: '' })
const apiLimitForm = ref({ ...apiLimits.value })
const authSettingsForm = ref({ ...authSettings.value })
const roleForm = ref({ name: '', description: '' })
const currentRole = ref(null)

// 权限树数据
const permissionTree = ref([
  {
    id: 1,
    name: '系统管理',
    children: [
      { id: 11, name: '用户管理' },
      { id: 12, name: '角色管理' },
      { id: 13, name: '权限管理' }
    ]
  },
  {
    id: 2,
    name: '项目管理',
    children: [
      { id: 21, name: '项目列表' },
      { id: 22, name: '项目创建' },
      { id: 23, name: '项目编辑' },
      { id: 24, name: '项目删除' }
    ]
  },
  {
    id: 3,
    name: '数据管理',
    children: [
      { id: 31, name: '数据查看' },
      { id: 32, name: '数据导出' },
      { id: 33, name: '数据分析' }
    ]
  }
])
const checkedPermissions = ref([11, 21, 31])

// 初始化
onMounted(() => {
  // 这里可以调用API获取实际数据
  fetchLogs()
})

// 获取日志数据
const fetchLogs = () => {
  // 模拟API调用
  setTimeout(() => {
    logs.value = [
      { id: 1, timestamp: '2023-05-20 10:30:45', username: 'admin', ip: '192.168.1.100', type: 'login', action: '用户登录', status: 'success' },
      { id: 2, timestamp: '2023-05-20 11:15:22', username: 'admin', ip: '192.168.1.100', type: 'operation', action: '创建项目', status: 'success' },
      { id: 3, timestamp: '2023-05-20 13:45:10', username: 'operator', ip: '10.0.0.5', type: 'operation', action: '修改配置', status: 'success' },
      { id: 4, timestamp: '2023-05-20 14:20:33', username: 'guest', ip: '203.0.113.5', type: 'login', action: '用户登录', status: 'failed' },
      { id: 5, timestamp: '2023-05-20 15:10:18', username: 'admin', ip: '192.168.1.100', type: 'system', action: '系统备份', status: 'success' }
    ]
    totalLogs.value = 125 // 模拟总记录数
  }, 500)
}

// 处理IP白名单添加
const handleAddIp = () => {
  ipForm.value = { ip: '', description: '' }
  ipDialogVisible.value = true
}

// 提交IP表单
const submitIpForm = () => {
  // 这里应该调用API保存数据
  ipWhitelist.value.push({
    ip: ipForm.value.ip,
    description: ipForm.value.description,
    createdAt: new Date().toLocaleString()
  })
  ipDialogVisible.value = false
  ElMessage.success('IP白名单添加成功')
}

// 处理IP删除
const handleDeleteIp = (row) => {
  ElMessageBox.confirm('确定要删除此IP白名单吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 这里应该调用API删除数据
    const index = ipWhitelist.value.findIndex(item => item.ip === row.ip)
    if (index !== -1) {
      ipWhitelist.value.splice(index, 1)
      ElMessage.success('删除成功')
    }
  }).catch(() => {})
}

// 处理API限制编辑
const handleEditApiLimit = () => {
  apiLimitForm.value = { ...apiLimits.value }
  apiLimitDialogVisible.value = true
}

// 提交API限制表单
const submitApiLimitForm = () => {
  // 这里应该调用API保存数据
  Object.assign(apiLimits.value, apiLimitForm.value)
  apiLimitDialogVisible.value = false
  ElMessage.success('API访问限制更新成功')
}

// 处理认证设置编辑
const handleEditAuthSettings = () => {
  authSettingsForm.value = { ...authSettings.value }
  authSettingsDialogVisible.value = true
}

// 提交认证设置表单
const submitAuthSettingsForm = () => {
  // 这里应该调用API保存数据
  Object.assign(authSettings.value, authSettingsForm.value)
  authSettingsDialogVisible.value = false
  ElMessage.success('认证设置更新成功')
}

// 处理角色添加
const handleAddRole = () => {
  currentRole.value = null
  roleForm.value = { name: '', description: '' }
  roleDialogVisible.value = true
}

// 处理角色编辑
const handleEditRole = (row) => {
  currentRole.value = row
  roleForm.value = { name: row.name, description: row.description }
  roleDialogVisible.value = true
}

// 提交角色表单
const submitRoleForm = () => {
  // 这里应该调用API保存数据
  if (currentRole.value) {
    // 编辑现有角色
    const index = roles.value.findIndex(item => item.id === currentRole.value.id)
    if (index !== -1) {
      roles.value[index].name = roleForm.value.name
      roles.value[index].description = roleForm.value.description
    }
  } else {
    // 添加新角色
    const newId = Math.max(...roles.value.map(item => item.id)) + 1
    roles.value.push({
      id: newId,
      name: roleForm.value.name,
      description: roleForm.value.description,
      userCount: 0
    })
  }
  roleDialogVisible.value = false
  ElMessage.success(currentRole.value ? '角色更新成功' : '角色添加成功')
}

// 处理角色删除
const handleDeleteRole = (row) => {
  ElMessageBox.confirm('确定要删除此角色吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 这里应该调用API删除数据
    const index = roles.value.findIndex(item => item.id === row.id)
    if (index !== -1) {
      roles.value.splice(index, 1)
      ElMessage.success('删除成功')
    }
  }).catch(() => {})
}

// 处理权限分配
const handleAssignPermissions = (row) => {
  currentRole.value = row
  // 这里应该调用API获取当前角色的权限
  permissionDialogVisible.value = true
}

// 处理日志搜索
const handleSearchLogs = () => {
  fetchLogs()
}

// 处理日志导出
const handleExportLogs = () => {
  ElMessage.success('日志导出成功')
}

// 处理日志详情查看
const handleViewLogDetail = (row) => {
  ElMessageBox.alert(`操作详情：${row.action}\n操作结果：${row.status === 'success' ? '成功' : '失败'}`, '日志详情', {
    confirmButtonText: '确定'
  })
}

// 分页处理
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchLogs()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchLogs()
}

// 获取日志类型标签样式
const getLogTypeTag = (type) => {
  const map = {
    login: 'info',
    operation: 'success',
    system: 'warning'
  }
  return map[type] || 'info'
}