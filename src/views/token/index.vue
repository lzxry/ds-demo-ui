<template>
  <div class="token-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>Token管理</span>
          <el-button type="primary" @click="handleCreate">
            创建Token
          </el-button>
        </div>
      </template>

      <el-table :data="tokenList" style="width: 100%" border>
        <el-table-column prop="token" label="Token" width="200" />
        <el-table-column prop="username" label="所属用户" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="remark" label="备注" min-width="200" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'danger'">
              {{ row.status === 'active' ? '正常' : '已封禁' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button
              :type="row.status === 'active' ? 'danger' : 'success'"
              link
              @click="handleToggleStatus(row)"
            >
              {{ row.status === 'active' ? '封禁' : '启用' }}
            </el-button>
            <el-button type="danger" link @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- Token表单弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'create' ? '创建Token' : '编辑Token'"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="所属用户" prop="userId">
          <el-select v-model="form.userId" placeholder="请选择用户">
            <el-option
              v-for="user in userList"
              :key="user.id"
              :label="user.username"
              :value="user.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="Token" prop="token">
          <el-input v-model="form.token" placeholder="请输入Token" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            placeholder="请输入备注信息"
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
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// Token列表数据
const tokenList = ref([
  {
    id: 1,
    token: 'pdd1234567890',
    username: 'test_user',
    createTime: '2024-01-01 12:00:00',
    remark: '测试Token',
    status: 'active'
  }
])

// 用户列表数据
const userList = ref([
  {
    id: 1,
    username: 'test_user'
  }
])

// 弹窗控制
const dialogVisible = ref(false)
const dialogType = ref('create')
const formRef = ref(null)

// 表单数据
const form = reactive({
  userId: '',
  token: '',
  remark: ''
})

// 表单验证规则
const rules = {
  userId: [
    { required: true, message: '请选择用户', trigger: 'change' }
  ],
  token: [
    { required: true, message: '请输入Token', trigger: 'blur' }
  ]
}

// 创建Token
const handleCreate = () => {
  dialogType.value = 'create'
  Object.assign(form, {
    userId: '',
    token: '',
    remark: ''
  })
  dialogVisible.value = true
}

// 编辑Token
const handleEdit = (row) => {
  dialogType.value = 'edit'
  Object.assign(form, row)
  dialogVisible.value = true
}

// 删除Token
const handleDelete = (row) => {
  ElMessageBox.confirm(
    '确定要删除该Token吗？',
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    // TODO: 调用删除接口
    ElMessage.success('删除成功')
  })
}

// 切换Token状态
const handleToggleStatus = (row) => {
  const action = row.status === 'active' ? '封禁' : '启用'
  ElMessageBox.confirm(
    `确定要${action}Token ${row.token} 吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    // TODO: 调用状态切换接口
    row.status = row.status === 'active' ? 'banned' : 'active'
    ElMessage.success(`${action}成功`)
  })
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate((valid) => {
    if (valid) {
      // TODO: 调用创建/编辑接口
      ElMessage.success(dialogType.value === 'create' ? '创建成功' : '编辑成功')
      dialogVisible.value = false
    }
  })
}
</script>

<style scoped lang="scss">
.token-management {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
</style> 