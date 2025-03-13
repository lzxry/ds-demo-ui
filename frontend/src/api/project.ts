import request from './index'

// 获取项目列表
export function getProjectList(params: any) {
  return request({
    url: '/project/list',
    method: 'get',
    params
  })
}

// 创建项目
export function createProject(data: any) {
  return request({
    url: '/project',
    method: 'post',
    data
  })
}

// 编辑项目
export function updateProject(id: number, data: any) {
  return request({
    url: `/project/${id}`,
    method: 'put',
    data
  })
}

// 其他项目相关API... 