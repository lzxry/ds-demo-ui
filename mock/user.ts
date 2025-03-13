export default [
  {
    url: '/api/auth/login',
    method: 'post',
    response: ({ body }) => {
      const { username, password } = body
      if (username === 'admin' && password === 'admin') {
        return {
          code: 200,
          message: '登录成功',
          data: 'mock-token-admin-2024'
        }
      }
      return {
        code: 401,
        message: '用户名或密码错误'
      }
    }
  },
  // 其他模拟接口...
] 