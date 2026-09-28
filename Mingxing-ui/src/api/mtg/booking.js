import request from '@/utils/request'

export function getBookingCalendar(date) {
  return request({
    url: '/mtg/booking/calendar',
    method: 'get',
    params: { date }
  })
}

export function getBookingCalendarRange(startDate, endDate) {
  return request({
    url: '/mtg/booking/calendar',
    method: 'get',
    params: { date: startDate, endDate }
  })
}

export function listBooking(query) {
  return request({
    url: '/mtg/booking/list',
    method: 'get',
    params: query
  })
}

export function addBooking(data) {
  return request({
    url: '/mtg/booking',
    method: 'post',
    data: data
  })
}

export function updateBooking(data) {
  return request({
    url: '/mtg/booking',
    method: 'put',
    data: data
  })
}

export function cancelBooking(data) {
  return request({
    url: '/mtg/booking/cancel',
    method: 'put',
    data: data
  })
}

export function auditBooking(data) {
  return request({
    url: '/mtg/booking/audit',
    method: 'put',
    data: data
  })
}

export function deleteBooking(bookingId) {
  return request({
    url: '/mtg/booking/' + bookingId,
    method: 'delete'
  })
}
