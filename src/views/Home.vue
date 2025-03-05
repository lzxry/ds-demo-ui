<template>
  <div class="home-container">
    <div class="dashboard-header">
      <h1>仪表盘</h1>
      <button @click="handleLogout" class="logout-btn">退出登录</button>
    </div>
    
    <div class="dashboard-content">
      <div class="dashboard-card">
        <h3>欢迎使用</h3>
        <p>这是您的仪表盘页面</p>
      </div>
      
      <div class="dashboard-card">
        <h3>系统状态</h3>
        <p>正常运行中</p>
      </div>
      
      <div class="dashboard-card">
        <h3>快捷操作</h3>
        <div class="quick-actions">
          <button class="action-btn">查看数据</button>
          <button class="action-btn">系统设置</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const handleLogout = () => {
  localStorage.removeItem('isLoggedIn')
  router.push('/login')
}
</script>

<style scoped>
.home-container {
  padding: 20px;
  min-height: 100vh;
  background-color: #f5f7fa;
}

.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.dashboard-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  padding: 20px;
}

.dashboard-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.dashboard-card h3 {
  margin-bottom: 15px;
  color: #333;
}

.quick-actions {
  display: flex;
  gap: 10px;
  margin-top: 15px;
}

.action-btn {
  padding: 8px 16px;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.action-btn:hover {
  background-color: #66b1ff;
}

.logout-btn {
  padding: 8px 16px;
  background-color: #f56c6c;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.logout-btn:hover {
  background-color: #f78989;
}
</style> 