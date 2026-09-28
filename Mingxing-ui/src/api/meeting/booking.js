import request from '@/utils/request'

// 查询预约列表
export function listBooking(query) {
  return request({
    url: '/meeting/booking/list',
    method: 'get',
    params: query
  })
}

// 查询我的预约列表
export function listMyBooking(query) {
  return request({
    url: '/meeting/booking/my',
    method: 'get',
    params: query
  })
}

// 查询预约详细
export function getBooking(bookingId) {
  return request({
    url: '/meeting/booking/' + bookingId,
    method: 'get'
  })
}

// 新增预约
export function addBooking(data) {
  return request({
    url: '/meeting/booking',
    method: 'post',
    data: data
  })
}

// 修改预约
export function updateBooking(data) {
  return request({
    url: '/meeting/booking',
    method: 'put',
    data: data
  })
}

// 审核预约
export function auditBooking(data) {
  return request({
    url: '/meeting/booking/audit',
    method: 'put',
    data: data
  })
}

// 取消预约
export function cancelBooking(data) {
  return request({
    url: '/meeting/booking/cancel',
    method: 'put',
    data: data
  })
}

// 删除预约
export function delBooking(bookingId) {
  return request({
    url: '/meeting/booking/' + bookingId,
    method: 'delete'
  })
}
