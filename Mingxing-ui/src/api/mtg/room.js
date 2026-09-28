import request from '@/utils/request'

export function listRoom(query) {
  return request({
    url: '/mtg/room/list',
    method: 'get',
    params: query
  })
}

export function getRoom(roomId) {
  return request({
    url: '/mtg/room/' + roomId,
    method: 'get'
  })
}

export function getAvailableRooms(query) {
  return request({
    url: '/mtg/room/available',
    method: 'get',
    params: query
  })
}

export function addRoom(data) {
  return request({
    url: '/mtg/room',
    method: 'post',
    data: data
  })
}

export function updateRoom(data) {
  return request({
    url: '/mtg/room',
    method: 'put',
    data: data
  })
}

export function delRoom(roomId) {
  return request({
    url: '/mtg/room/' + roomId,
    method: 'delete'
  })
}

export function changeRoomStatus(data) {
  return request({
    url: '/mtg/room/changeStatus',
    method: 'put',
    data: data
  })
}

export function maintainRoom(data) {
  return request({
    url: '/mtg/room/maintain',
    method: 'put',
    data: data
  })
}