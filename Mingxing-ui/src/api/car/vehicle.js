import request from '@/utils/request'

export function listVehicle(query) {
  return request({
    url: '/car/vehicle/list',
    method: 'get',
    params: query
  })
}

export function getVehicle(vehicleId) {
  return request({
    url: '/car/vehicle/' + vehicleId,
    method: 'get'
  })
}

export function addVehicle(data) {
  return request({
    url: '/car/vehicle',
    method: 'post',
    data: data
  })
}

export function updateVehicle(data) {
  return request({
    url: '/car/vehicle',
    method: 'put',
    data: data
  })
}

export function delVehicle(vehicleId) {
  return request({
    url: '/car/vehicle/' + vehicleId,
    method: 'delete'
  })
}
