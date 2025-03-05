<template>
  <div class="login-container">
    <div class="login-box">
      <h2>系统登录</h2>
      <div class="form-item">
        <input 
          type="text" 
          v-model="username" 
          placeholder="用户名" 
          @keyup.enter="handleLogin"
          ref="usernameInput"
        />
      </div>
      <div class="form-item">
        <input 
          type="password" 
          v-model="password" 
          placeholder="密码" 
          @keyup.enter="handleLogin"
        />
      </div>
      <div class="form-item">
        <button 
          @click="handleLogin" 
          :disabled="loading"
          class="login-btn"
        >
          {{ loading ? '登录中...' : '登录' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const username = ref('')
const password = ref('')
const loading = ref(false)
const usernameInput = ref<HTMLInputElement | null>(null)

// 自动填充
onMounted(() => {
  username.value = 'admin'
  password.value = 'admin'
  usernameInput.value?.focus()
})

const handleLogin = async () => {
  if (loading.value) return
  
  loading.value = true
  try {
    if (username.value === 'admin' && password.value === 'admin') {
      localStorage.setItem('isLoggedIn', 'true')
      router.push('/')
    } else {
      alert('用户名或密码错误')
    }
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f5f5;
}

.login-box {
  width: 350px;
  padding: 30px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.form-item {
  margin-bottom: 20px;
}

input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  outline: none;
  transition: border-color 0.3s;
}

input:focus {
  border-color: #409eff;
}

.login-btn {
  width: 100%;
  padding: 12px;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
}

.login-btn:hover:not(:disabled) {
  background-color: #66b1ff;
}

.login-btn:disabled {
  background-color: #a0cfff;
  cursor: not-allowed;
}
</style> 