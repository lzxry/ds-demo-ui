import Mock from 'mockjs'

// 模拟延迟
Mock.setup({
  timeout: '300-600'
})

// 登录接口
Mock.mock(/\/api\/auth\/login/, 'post', (options) => {
  const urlSearchParams = new URLSearchParams(options.body)
  const params = Object.fromEntries(urlSearchParams.entries())
  const { username, password } = params
  
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
})

// 用户信息接口
Mock.mock(/\/api\/user\/info/, 'get', () => {
  return {
    code: 200,
    message: '获取成功',
    data: {
      id: 1,
      username: 'admin',
      nickname: '管理员',
      avatar: 'https://via.placeholder.com/50',
      roles: ['admin']
    }
  }
})

// 项目列表接口
Mock.mock(/\/api\/project\/list/, 'get', () => {
  return {
    code: 200,
    message: '获取成功',
    data: {
      list: [
        {
          id: 1,
          name: '拼多多项目',
          code: 'pdd',
          status: 'active',
          remark: '拼多多数据采集项目',
          createTime: '2024-01-01 12:00:00'
        },
        {
          id: 2,
          name: '京东项目',
          code: 'jd',
          status: 'active',
          remark: '京东数据采集项目',
          createTime: '2024-01-02 12:00:00'
        }
      ],
      total: 2
    }
  }
})

// 统计数据接口
Mock.mock(/\/api\/statistics\/overview/, 'get', () => {
  return {
    code: 200,
    message: '获取成功',
    data: {
      todayTasks: 150,
      todayCompleted: 120,
      todayAmount: 1500.00,
      totalTasks: 1500,
      trend: [
        { date: '2024-03-10', count: 56 },
        { date: '2024-03-11', count: 68 },
        { date: '2024-03-12', count: 52 },
        { date: '2024-03-13', count: 75 },
        { date: '2024-03-14', count: 82 },
        { date: '2024-03-15', count: 68 },
        { date: '2024-03-16', count: 42 }
      ]
    }
  }
})

// 其他模拟接口...

export default Mock 