import request from '@/utils/request'

export function listChinaOrder(query) {
  return request({
    url: '/mtg/report/chinaOrder',
    method: 'get',
    params: query
  })
}

export function summaryChinaOrder(yMFrom, yMTo) {
  return request({
    url: '/mtg/report/summary',
    method: 'get',
    params: { yMFrom, yMTo }
  })
}

export function exportChinaOrder(yMFrom, yMTo) {
  return request({
    url: '/mtg/report/chinaOrder/export',
    method: 'post',
    params: { yMFrom, yMTo },
    responseType: 'blob'
  })
}
