import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/layout/index.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/layout/index.vue'),
    redirect: '/dashboard',
    meta: { requiresAuth: true },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: {
          title: '仪表盘',
          icon: 'House'
        }
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/user/index.vue'),
        meta: {
          title: '用户管理',
          icon: 'User'
        }
      },
      {
        path: 'project',
        name: 'Project',
        component: () => import('@/views/project/index.vue'),
        meta: {
          title: '项目管理',
          icon: 'Folder'
        }
      },
      {
        path: 'statistics',
        name: 'Statistics',
        component: () => import('@/views/statistics/index.vue'),
        meta: {
          title: '数据统计',
          icon: 'TrendCharts'
        }
      },
      {
        path: 'project-access',
        name: 'ProjectAccess',
        component: () => import('@/views/project-access/index.vue'),
        meta: {
          title: '项目接入',
          icon: 'Connection'
        }
      },
      {
        path: 'recent-data',
        name: 'RecentData',
        component: () => import('@/views/recent-data/index.vue'),
        meta: {
          title: '近期数据',
          icon: 'Calendar'
        }
      },
      {
        path: 'data-validation',
        name: 'DataValidation',
        component: () => import('@/views/data-validation/index.vue'),
        meta: {
          title: '数据校验',
          icon: 'DataLine'
        }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  console.log('Route guard triggered:', { to, from })
  const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true'
  
  // 如果要访问的页面需要登录
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!isLoggedIn) {
      next({ path: '/login' })
    } else {
      next()
    }
  } else {
    // 如果已登录且尝试访问登录页
    if (to.path === '/login' && isLoggedIn) {
      next({ path: '/dashboard' })
    } else {
      next()
    }
  }
})

export default router 