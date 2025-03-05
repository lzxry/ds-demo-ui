import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/layout/index.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: {
          title: '首页',
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

export default router 