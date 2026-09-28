import request from '@/utils/request'

export function listExpense(query) {
  return request({
    url: '/car/expense/list',
    method: 'get',
    params: query
  })
}

export function getExpense(expenseId) {
  return request({
    url: '/car/expense/' + expenseId,
    method: 'get'
  })
}

export function addExpense(data) {
  return request({
    url: '/car/expense',
    method: 'post',
    data: data
  })
}

export function updateExpense(data) {
  return request({
    url: '/car/expense',
    method: 'put',
    data: data
  })
}

export function delExpense(expenseIds) {
  return request({
    url: '/car/expense/' + expenseIds,
    method: 'delete'
  })
}