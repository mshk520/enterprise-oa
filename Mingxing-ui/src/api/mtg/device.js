import request from '@/utils/request'

export function listDevice(query) {
  return request({
    url: '/mtg/device/list',
    method: 'get',
    params: query
  })
}

// 查询启用的设备列表（无需权限）
export function listEnabledDevice() {
  return request({
    url: '/mtg/device/listEnabled',
    method: 'get'
  })
}

export function getDevice(deviceId) {
  return request({
    url: '/mtg/device/' + deviceId,
    method: 'get'
  })
}

export function addDevice(data) {
  return request({
    url: '/mtg/device',
    method: 'post',
    data: data
  })
}

export function updateDevice(data) {
  return request({
    url: '/mtg/device',
    method: 'put',
    data: data
  })
}

export function delDevice(deviceId) {
  return request({
    url: '/mtg/device/' + deviceId,
    method: 'delete'
  })
}

export function changeDeviceStatus(data) {
  return request({
    url: '/mtg/device/changeStatus',
    method: 'put',
    data: data
  })
}