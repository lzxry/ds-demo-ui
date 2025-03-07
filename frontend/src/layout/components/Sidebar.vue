<template>
  <el-menu
    :default-active="activeMenu"
    class="sidebar-menu"
    :collapse="isCollapse"
    background-color="#304156"
    text-color="#bfcbd9"
    active-text-color="#409EFF"
  >
    <el-menu-item
      v-for="route in routes"
      :key="route.path"
      :index="route.path"
      @click="handleMenuClick(route)"
    >
      <el-icon><component :is="route.meta.icon" /></el-icon>
      <template #title>{{ route.meta.title }}</template>
    </el-menu-item>
  </el-menu>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  House,
  User,
  Folder,
  TrendCharts,
  Connection,
  Calendar,
  DataLine
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

// 获取路由配置
const routes = computed(() => {
  return router.options.routes[0].children
})

// 当前激活的菜单
const activeMenu = computed(() => {
  return route.path
})

// 菜单是否折叠
const isCollapse = computed(() => {
  return false // 可以根据需要添加折叠功能
})

// 菜单点击处理
const handleMenuClick = (route) => {
  router.push(route.path)
}
</script>

<style scoped lang="scss">
.sidebar-menu {
  height: 100%;
  border-right: none;
  
  &:not(.el-menu--collapse) {
    width: 200px;
  }
  
  .el-menu-item {
    &:hover {
      background-color: #263445 !important;
    }
    
    &.is-active {
      background-color: #263445 !important;
    }
  }
}
</style> 