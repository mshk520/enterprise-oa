import request from '@/utils/request'

export function listAccessRecord(query) {
  return request({
    url: '/car/accessRecord/list',
    method: 'get',
    params: query
  })
}

export function getAccessRecord(accessId) {
  return request({
    url: '/car/accessRecord/' + accessId,
    method: 'get'
  })
}

export function addAccessRecord(data) {
  return request({
    url: '/car/accessRecord',
    method: 'post',
    data: data
  })
}

export function updateAccessRecord(data) {
  return request({
    url: '/car/accessRecord',
    method: 'put',
    data: data
  })
}

export function delAccessRecord(accessId) {
  return request({
    url: '/car/accessRecord/' + accessId,
    method: 'delete'
  })
}