import request from '@/utils/request'

export function getRoomCalendar(date) {
  return request({
    url: '/meeting/booking/calendar',
    method: 'get',
    params: { date }
  })
}

export function getRoomCalendarRange(startDate, endDate) {
  return request({
    url: '/meeting/booking/calendar',
    method: 'get',
    params: { date: startDate, endDate }
  })
}
