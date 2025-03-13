// 简单的本地存储数据库
export const localDB = {
  // 初始化数据
  init() {
    if (!localStorage.getItem('db_initialized')) {
      // 项目数据
      localStorage.setItem('projects', JSON.stringify([
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
      ]))
      
      // 用户数据
      localStorage.setItem('users', JSON.stringify([
        {
          id: 1,
          username: 'admin',
          password: 'admin',
          nickname: '管理员',
          avatar: 'https://via.placeholder.com/50',
          roles: ['admin']
        }
      ]))
      
      localStorage.setItem('db_initialized', 'true')
    }
  },
  
  // 获取数据
  get(key) {
    const data = localStorage.getItem(key)
    return data ? JSON.parse(data) : null
  },
  
  // 保存数据
  set(key, value) {
    localStorage.setItem(key, JSON.stringify(value))
  }
} 