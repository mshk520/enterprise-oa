import request from '@/utils/request'

// 查询当前用户消息列表
export function listNoticeMsg() {
  return request({
    url: '/system/noticeMsg/list',
    method: 'get'
  })
}

// 查询当前用户未读消息数
export function getUnreadCount() {
  return request({
    url: '/system/noticeMsg/count',
    method: 'get'
  })
}

// 标记单条已读
export function readNoticeMsg(msgId) {
  return request({
    url: '/system/noticeMsg/read/' + msgId,
    method: 'put'
  })
}

// 标记全部已读
export function readAllNoticeMsg() {
  return request({
    url: '/system/noticeMsg/readAll',
    method: 'put'
  })
}
