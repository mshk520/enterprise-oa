import request from '@/utils/request'

// 查询在外车辆
export function getOutVehicles() {
  return request({
    url: '/car/duty/out',
    method: 'get'
  })
}

// 查询可出厂车辆
export function getAvailableVehicles() {
  return request({
    url: '/car/duty/available',
    method: 'get'
  })
}

// 出厂操作
export function depart(data) {
  return request({
    url: '/car/duty/depart',
    method: 'post',
    data: data
  })
}

// 回场操作
export function returnVehicle(data) {
  return request({
    url: '/car/duty/return',
    method: 'post',
    data: data
  })
}

// 紧急出厂
export function emergencyDepart(data) {
  return request({
    url: '/car/duty/emergencyDepart',
    method: 'post',
    data: data
  })
}

// 查询司机列表
export function getDrivers() {
  return request({
    url: '/car/duty/drivers',
    method: 'get'
  })
}

// 查询紧急出厂车辆
export function getEmergencyVehicles() {
  return request({
    url: '/car/duty/emergencyVehicles',
    method: 'get'
  })
}
