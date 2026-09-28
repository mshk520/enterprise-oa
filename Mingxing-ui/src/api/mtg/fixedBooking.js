import request from '@/utils/request'

export function listFixedBooking(query) {
  return request({
    url: '/mtg/fixedBooking/list',
    method: 'get',
    params: query
  })
}

export function getFixedBooking(fixedId) {
  return request({
    url: '/mtg/fixedBooking/' + fixedId,
    method: 'get'
  })
}

export function addFixedBooking(data) {
  return request({
    url: '/mtg/fixedBooking',
    method: 'post',
    data: data
  })
}

export function updateFixedBooking(data) {
  return request({
    url: '/mtg/fixedBooking',
    method: 'put',
    data: data
  })
}

export function delFixedBooking(fixedIds) {
  return request({
    url: '/mtg/fixedBooking/' + fixedIds,
    method: 'delete'
  })
}
