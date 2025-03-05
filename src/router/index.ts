import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      component: () => import('@/layouts/DefaultLayout.vue'),
      children: [
        {
          path: '',
          name: 'dashboard',
          component: () => import('@/views/dashboard/index.vue'),
          meta: { title: '仪表盘' }
        },
        {
          path: 'users',
          name: 'users',
          component: () => import('@/views/users/index.vue'),
          meta: { title: '用户管理' }
        },
        {
          path: 'projects',
          name: 'projects',
          component: () => import('@/views/projects/index.vue'),
          meta: { title: '项目管理' }
        },
        {
          path: 'statistics',
          name: 'statistics',
          component: () => import('@/views/statistics/index.vue'),
          meta: { title: '数据统计' }
        },
        {
          path: 'integration',
          name: 'integration',
          component: () => import('@/views/integration/index.vue'),
          meta: { title: '项目接入' }
        },
        {
          path: 'recent',
          name: 'recent',
          component: () => import('@/views/recent/index.vue'),
          meta: { title: '近期数据' }
        }
      ]
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/login/index.vue'),
      meta: { title: '登录' }
    }
  ]
})

export default router 