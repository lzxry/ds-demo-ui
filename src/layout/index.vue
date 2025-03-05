<template>
  <div class="app-wrapper">
    <!-- 侧边栏 -->
    <div class="sidebar-container">
      <Sidebar />
    </div>
    
    <!-- 主内容区 -->
    <div class="main-container">
      <router-view />
    </div>
  </div>
</template>

<script setup>
import Sidebar from './components/Sidebar.vue'
</script>

<style scoped lang="scss">
.app-wrapper {
  display: flex;
  height: 100vh;
  
  .sidebar-container {
    width: 200px;
    height: 100%;
    background-color: #304156;
  }
  
  .main-container {
    flex: 1;
    height: 100%;
    padding: 20px;
    background-color: #f0f2f5;
    overflow-y: auto;
  }
}
</style> 