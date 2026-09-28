import request from '@/utils/request'

export function listService(query) {
  return request({
    url: '/mtg/service/list',
    method: 'get',
    params: query
  })
}

export function listEnabledService() {
  return request({
    url: '/mtg/service/listEnabled',
    method: 'get'
  })
}

export function getService(serviceId) {
  return request({
    url: '/mtg/service/' + serviceId,
    method: 'get'
  })
}

export function addService(data) {
  return request({
    url: '/mtg/service',
    method: 'post',
    data: data
  })
}

export function updateService(data) {
  return request({
    url: '/mtg/service',
    method: 'put',
    data: data
  })
}

export function delService(serviceId) {
  return request({
    url: '/mtg/service/' + serviceId,
    method: 'delete'
  })
}

export function changeServiceStatus(data) {
  return request({
    url: '/mtg/service/changeStatus',
    method: 'put',
    data: data
  })
}