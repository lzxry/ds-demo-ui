<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <h2 class="login-title">DS 管理端系统</h2>
      </template>
      
      <el-form
        ref="formRef"
        :model="loginForm"
        :rules="rules"
        label-width="0"
        size="large"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="用户名"
            prefix-icon="User"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="密码"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            class="login-button"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { useUserStore } from '../store'

const router = useRouter()
const formRef = ref()
const loading = ref(false)
const userStore = useUserStore()

const loginForm = reactive({
  username: '',
  password: ''
})

// 添加自动填充
onMounted(() => {
  loginForm.username = 'admin'
  loginForm.password = 'admin'
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    loading.value = true
    
    try {
      // 直接使用模拟登录，确保可以登录
      console.log('尝试登录:', loginForm)
      
      if (loginForm.username === 'admin' && loginForm.password === 'admin') {
        localStorage.setItem('isLoggedIn', 'true')
        localStorage.setItem('token', 'dev-mock-token')
        ElMessage.success('登录成功')
        router.push('/dashboard')
      } else {
        ElMessage.error('用户名或密码错误')
      }
    } catch (error) {
      console.error('登录失败:', error)
      ElMessage.error('登录失败，请稍后再试')
    } finally {
      loading.value = false
    }
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}
</script>

<style scoped lang="scss">
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f0f2f5;
  
  .login-card {
    width: 400px;
    
    .login-title {
      text-align: center;
      margin: 0;
      color: #303133;
      font-size: 24px;
    }
    
    .login-button {
      width: 100%;
    }
  }
}
</style> 