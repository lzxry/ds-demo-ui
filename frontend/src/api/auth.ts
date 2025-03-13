import request from './index'

// 登录
export function login(data: { username: string, password: string }) {
  return request({
    url: '/auth/login',
    method: 'post',
    params: data
  })
}

// 登出
export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
} 