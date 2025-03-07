<template>
  <div class="project-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>项目管理</span>
          <el-button type="primary" @click="handleCreateProject">
            创建项目
          </el-button>
        </div>
      </template>

      <!-- 项目列表 -->
      <el-table :data="projectList" style="width: 100%">
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
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEditProject(row)">
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
    </el-card>

    <!-- 项目表单弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="currentProject ? '编辑项目' : '创建项目'"
      width="500px"
      destroy-on-close
    >
      <el-form
        ref="formRef"
        :model="projectForm"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="项目名称" prop="name">
          <el-input v-model="projectForm.name" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="标识码" prop="code">
          <el-input
            v-model="projectForm.code"
            placeholder="请输入英文标识码"
            :disabled="!!currentProject"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="projectForm.status"
            :active-value="'active'"
            :inactive-value="'inactive'"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="projectForm.remark"
            type="textarea"
            :rows="3"
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

// 项目列表数据
const projectList = ref([
  {
    id: 1,
    name: '拼多多',
    code: 'pdd',
    status: 'active',
    remark: '拼多多项目',
    createTime: '2024-01-01 12:00:00'
  },
  {
    id: 2,
    name: '京东',
    code: 'jd',
    status: 'active',
    remark: '京东项目',
    createTime: '2024-01-02 12:00:00'
  }
])

// 表单相关
const dialogVisible = ref(false)
const formRef = ref(null)
const currentProject = ref(null)

const projectForm = reactive({
  name: '',
  code: '',
  status: 'active',
  remark: ''
})

const rules = {
  name: [
    { required: true, message: '请输入项目名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入标识码', trigger: 'blur' },
    { pattern: /^[a-z]+$/, message: '只能包含小写字母', trigger: 'blur' },
    { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
  ]
}

// 创建项目
const handleCreateProject = () => {
  currentProject.value = null
  Object.assign(projectForm, {
    name: '',
    code: '',
    status: 'active',
    remark: ''
  })
  dialogVisible.value = true
}

// 编辑项目
const handleEditProject = (project) => {
  currentProject.value = project
  Object.assign(projectForm, project)
  dialogVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate((valid) => {
    if (valid) {
      if (currentProject.value) {
        // 更新项目
        const index = projectList.value.findIndex(p => p.id === currentProject.value.id)
        if (index !== -1) {
          projectList.value[index] = {
            ...projectList.value[index],
            ...projectForm
          }
          ElMessage.success('项目更新成功')
        }
      } else {
        // 创建项目
        projectList.value.push({
          id: projectList.value.length + 1,
          ...projectForm,
          createTime: new Date().toLocaleString()
        })
        ElMessage.success('项目创建成功')
      }
      dialogVisible.value = false
    }
  })
}

// 切换项目状态
const handleToggleStatus = (project) => {
  const action = project.status === 'active' ? '禁用' : '启用'
  ElMessageBox.confirm(
    `确定要${action}项目 ${project.name} 吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    project.status = project.status === 'active' ? 'inactive' : 'active'
    ElMessage.success(`${action}成功`)
  })
}
</script>

<style scoped lang="scss">
.project-management {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
</style> 