<template>
  <div class="layout-container">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside width="200px">
        <el-menu
          :default-active="route.path"
          class="el-menu-vertical"
          :router="true"
          @select="handleSelect"
        >
          <el-menu-item index="/dashboard">
            <el-icon><House /></el-icon>
            <span>仪表盘</span>
          </el-menu-item>
          <el-menu-item index="/user">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/project">
            <el-icon><Folder /></el-icon>
            <span>项目管理</span>
          </el-menu-item>
          <el-menu-item index="/statistics">
            <el-icon><TrendCharts /></el-icon>
            <span>数据统计</span>
          </el-menu-item>
          <el-menu-item index="/project-access">
            <el-icon><Connection /></el-icon>
            <span>项目接入</span>
          </el-menu-item>
          <el-menu-item index="/recent-data">
            <el-icon><Calendar /></el-icon>
            <span>近期数据</span>
          </el-menu-item>
          <el-menu-item index="/data-validation">
            <el-icon><DataLine /></el-icon>
            <span>数据校验</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 主要内容区 -->
      <el-container>
        <el-header>
          <div class="header-right">
            <el-button type="danger" @click="handleLogout">退出登录</el-button>
          </div>
        </el-header>
        <el-main>
          <router-view v-slot="{ Component }">
            <component :is="Component" />
          </router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import {
  House,
  User,
  Folder,
  TrendCharts,
  Connection,
  Calendar,
  DataLine
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

console.log('Layout mounted, current route:', route.path)

const handleSelect = (index) => {
  console.log('Menu selected:', index)
  router.push(index)
}

const handleLogout = () => {
  localStorage.removeItem('isLoggedIn')
  router.push('/login')
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
  width: 100vw;
}

.el-container {
  height: 100%;
}

.el-aside {
  background-color: #304156;
  color: #fff;
}

.el-menu {
  border-right: none;
}

.el-menu-vertical {
  height: 100%;
}

.el-header {
  background-color: #fff;
  border-bottom: 1px solid #dcdfe6;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding: 0 20px;
}

.el-main {
  background-color: #f0f2f5;
  padding: 20px;
}

.header-right {
  display: flex;
  align-items: center;
}
</style> 