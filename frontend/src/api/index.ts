import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'
import { mockData } from '../utils/mockData'

// 是否使用模拟数据
const useMockData = true

// 创建axios实例
const service = axios.create({
  baseURL: '/api',
  timeout: 15000
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 从localStorage获取token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    
    // 如果返回的状态码是200，说明接口请求成功，可以正常拿到数据
    if (res.code === 200) {
      return res.data
    }
    
    // 否则的话抛出错误
    ElMessage.error(res.message || '请求失败')
    
    // 401: 未登录
    if (res.code === 401) {
      ElMessage.error('请先登录')
      localStorage.removeItem('token')
      localStorage.removeItem('isLoggedIn')
      router.push('/login')
    }
    
    return Promise.reject(new Error(res.message || '请求失败'))
  },
  error => {
    console.error('响应错误:', error)
    
    // 开发环境下使用模拟数据
    if (useMockData && import.meta.env.DEV) {
      const url = error.config.url
      
      // 模拟登录接口
      if (url.includes('/auth/login')) {
        const params = new URLSearchParams(error.config.data)
        const username = params.get('username')
        const password = params.get('password')
        
        if (username === 'admin' && password === 'admin') {
          return Promise.resolve({ data: mockData.loginResponse.data })
        }
      }
      
      // 模拟用户信息接口
      if (url.includes('/user/info')) {
        return Promise.resolve({ data: mockData.userInfo })
      }
      
      // 模拟项目列表接口
      if (url.includes('/project/list')) {
        return Promise.resolve({ data: mockData.projectList })
      }
      
      // 模拟统计数据接口
      if (url.includes('/statistics/overview')) {
        return Promise.resolve({ data: mockData.statisticsData })
      }
    }
    
    // 处理服务器返回的错误
    if (error.response) {
      ElMessage.error(error.response.data?.message || '请求失败')
      
      if (error.response.status === 401) {
        localStorage.removeItem('token')
        localStorage.removeItem('isLoggedIn')
        router.push('/login')
      }
    } else {
      ElMessage.error('网络错误，请稍后再试')
    }
    
    return Promise.reject(error)
  }
)

export default service 