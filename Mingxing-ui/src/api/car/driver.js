import request from '@/utils/request'

// 查询司机信息列表
export function listDriver(query) {
  return request({
    url: '/car/driver/list',
    method: 'get',
    params: query
  })
}

// 查询司机信息详细
export function getDriver(driverId) {
  return request({
    url: '/car/driver/' + driverId,
    method: 'get'
  })
}

// 新增司机信息
export function addDriver(data) {
  return request({
    url: '/car/driver',
    method: 'post',
    data: data
  })
}

// 修改司机信息
export function updateDriver(data) {
  return request({
    url: '/car/driver',
    method: 'put',
    data: data
  })
}

// 删除司机信息
export function delDriver(driverId) {
  return request({
    url: '/car/driver/' + driverId,
    method: 'delete'
  })
}

// 导出司机信息
export function exportDriver(query) {
  return request({
    url: '/car/driver/export',
    method: 'get',
    params: query
  })
}
