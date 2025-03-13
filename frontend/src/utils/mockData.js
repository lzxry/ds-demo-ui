// 简单的模拟数据，不依赖mockjs库
export const mockData = {
  // 登录响应
  loginResponse: {
    code: 200,
    message: '登录成功',
    data: 'mock-token-2024'
  },
  
  // 用户信息
  userInfo: {
    id: 1,
    username: 'admin',
    nickname: '管理员',
    avatar: 'https://via.placeholder.com/50',
    roles: ['admin']
  },
  
  // 项目列表
  projectList: {
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
  },
  
  // 统计数据
  statisticsData: {
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