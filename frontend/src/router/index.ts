import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('../views/login/index.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/',
      component: () => import('../layout/index.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          name: 'Dashboard',
          component: () => import('../views/Home.vue'),
          meta: { title: '仪表盘', icon: 'dashboard' }
        },
        // 用户管理模块
        {
          path: 'users',
          name: 'Users',
          component: () => import('../views/users/index.vue'),
          meta: { title: '用户管理', icon: 'user' }
        },
        {
          path: 'token',
          name: 'Token',
          component: () => import('../views/token/index.vue'),
          meta: { title: 'Token管理', icon: 'key' }
        },
        // 项目管理模块
        {
          path: 'projects',
          name: 'Projects',
          component: () => import('../views/projects/index.vue'),
          meta: { title: '项目管理', icon: 'folder' }
        },
        // 数据统计模块
        {
          path: 'statistics',
          name: 'Statistics',
          component: () => import('../views/statistics/index.vue'),
          meta: { title: '数据统计', icon: 'chart' }
        },
        // 项目接入模块
        {
          path: 'project-access',
          name: 'ProjectAccess',
          component: () => import('../views/project-access/index.vue'),
          meta: { title: '项目接入', icon: 'connection' }
        },
        // 近期数据模块
        {
          path: 'recent-data',
          name: 'RecentData',
          component: () => import('../views/recent-data/index.vue'),
          meta: { title: '近期数据', icon: 'data' }
        },
        // 安全管理
        {
          path: 'security',
          name: 'Security',
          component: () => import('../views/security/index.vue'),
          meta: { title: '安全管理', icon: 'lock' }
        }
      ]
    },
    // 添加一个通配符路由，确保所有未匹配的路径都重定向到首页
    {
      path: '/:pathMatch(.*)*',
      redirect: '/'
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  console.log('路由守卫触发:', {
    to: to.path,
    from: from.path,
    isLoggedIn: localStorage.getItem('isLoggedIn') === 'true'
  })
  
  // 获取登录状态
  const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true'
  
  // 如果访问需要认证的页面且未登录
  if (to.meta.requiresAuth && !isLoggedIn) {
    console.log('需要认证但未登录，重定向到登录页')
    next('/login')
    return
  }
  
  // 如果已登录且访问登录页
  if (to.path === '/login' && isLoggedIn) {
    console.log('已登录，重定向到首页')
    next('/')
    return
  }
  
  // 其他情况正常放行
  console.log('正常放行')
  next()
})

export default router