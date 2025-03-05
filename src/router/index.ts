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
      name: 'Dashboard',
      component: () => import('../views/Home.vue'),
      meta: { requiresAuth: true, title: '仪表盘' }
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