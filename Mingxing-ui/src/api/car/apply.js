import request from '@/utils/request'

// 查询用车申请列表
export function listApply(query) {
  return request({
    url: '/car/apply/list',
    method: 'get',
    params: query
  })
}

// 查询用车申请详细
export function getApply(applyId) {
  return request({
    url: '/car/apply/' + applyId,
    method: 'get'
  })
}

// 新增用车申请
export function addApply(data) {
  return request({
    url: '/car/apply',
    method: 'post',
    data: data
  })
}

// 修改用车申请
export function updateApply(data) {
  return request({
    url: '/car/apply',
    method: 'put',
    data: data
  })
}

// 审批用车申请
export function auditApply(data) {
  return request({
    url: '/car/apply/audit',
    method: 'put',
    data: data
  })
}

// 补签（完善紧急申请信息）
export function supplementApply(data) {
  return request({
    url: '/car/apply/supplement',
    method: 'put',
    data: data
  })
}

// 删除用车申请
export function delApply(applyId) {
  return request({
    url: '/car/apply/' + applyId,
    method: 'delete'
  })
}