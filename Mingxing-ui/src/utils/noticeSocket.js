import { Notification, MessageBox } from 'element-ui'
import store from '@/store'
import router from '@/router'
import { getUnreadCount } from '@/api/system/noticeMsg'
import i18n from '@/lang'

class NoticeSocket {
  constructor() {
    this.ws = null
    this.lockReconnect = false
    this.heartbeatTimer = null
    this.listeners = {}
    this.intentionalClose = false
    this.reconnectCount = 0
    this.maxReconnectCount = 5
  }

  on(type, handler) {
    if (!this.listeners[type]) this.listeners[type] = []
    this.listeners[type].push(handler)
  }

  off(type, handler) {
    const list = this.listeners[type]
    if (!list) return
    const idx = list.indexOf(handler)
    if (idx > -1) list.splice(idx, 1)
  }

  emit(type, payload) {
    const list = this.listeners[type] || []
    list.forEach(handler => {
      try {
        handler(payload)
      } catch (e) {
        console.error('[noticeSocket] listener error', e)
      }
    })
  }

  connect() {
    const token = store.getters.token
    if (!token) return
    // 已连接或正在连接时不重复建连（App 和 Layout 都会调用）
    if (this.ws && (this.ws.readyState === 0 || this.ws.readyState === 1)) return
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:'
    let host
    const wsUrl = process.env.VUE_APP_WS_URL
    const baseUrl = process.env.VUE_APP_BASE_API || ''
    if (wsUrl) {
      // 开发环境：直连后端 WebSocket 端口，不走 devServer 代理
      host = wsUrl
    } else if (baseUrl.startsWith('http')) {
      // 生产：绝对地址的 API 前缀，替换为 ws
      host = baseUrl.replace(/^http/, protocol)
    } else {
      // 生产：同域反代（nginx 需把 /ws 代理到后端）
      host = window.location.host
    }
    const url = `${host}/ws?token=${encodeURIComponent(token)}&clientType=web`
    try {
      this.ws = new WebSocket(url)
    } catch (e) {
      console.error('WebSocket init error', e)
      this.reconnect()
      return
    }
    this.ws.onopen = () => {
      console.log('WebSocket connected')
      this.reconnectCount = 0
      this.startHeartbeat()
    }
    this.ws.onmessage = (event) => {
      this.handleMessage(event.data)
    }
    this.ws.onclose = (event) => {
      console.log('WebSocket closed, code=', event.code, 'reason=', event.reason)
      this.stopHeartbeat()
      // 主动关闭，不重连
      if (this.intentionalClose) {
        this.intentionalClose = false
        return
      }
      // 被服务器拒绝（重复登录/策略违反），不重连
      if (event.code === 1008) {
        console.log('WebSocket rejected by server (duplicate login), stop reconnecting')
        return
      }
      this.reconnect()
    }
    this.ws.onerror = (err) => {
      console.error('WebSocket error', err)
      this.ws.close()
    }
  }

  startHeartbeat() {
    this.stopHeartbeat()
    this.heartbeatTimer = setInterval(() => {
      if (this.ws && this.ws.readyState === 1) {
        this.ws.send('ping')
      }
    }, 30000)
  }

  stopHeartbeat() {
    if (this.heartbeatTimer) clearInterval(this.heartbeatTimer)
  }

  reconnect() {
    if (this.lockReconnect) return
    // 没有 token（未登录/已退出），不重连
    const token = store.getters.token
    if (!token) {
      console.log('WebSocket reconnect skipped: no token (not logged in)')
      return
    }
    // 超过最大重试次数，停止重连
    if (this.reconnectCount >= this.maxReconnectCount) {
      console.log('WebSocket max reconnect attempts reached, stop reconnecting')
      return
    }
    this.lockReconnect = true
    this.reconnectCount++
    // 指数退避：1s, 2s, 4s, 8s, 16s
    const delay = Math.min(1000 * Math.pow(2, this.reconnectCount - 1), 30000)
    console.log(`WebSocket reconnecting in ${delay}ms (attempt ${this.reconnectCount}/${this.maxReconnectCount})`)
    setTimeout(() => {
      this.lockReconnect = false
      this.connect()
    }, delay)
  }

  handleMessage(data) {
    if (data === 'pong') return
    try {
      const msg = JSON.parse(data)
      if (msg.type === 'carApply') {
        const userName = store.getters.nickName || store.getters.name || '当前用户'
        if (msg.applicantName === userName) return
        this._refreshBadge()
        // 先触发列表刷新（页面在后台就能看到新数据），再显示弹窗
        this.emit('carApply', msg)
        Notification({
          title: msg.title || '新的用车申请',
          message: msg.content,
          type: 'warning',
          duration: 6000,
          onClick: () => {
            this._jumpToList()
          }
        })
        MessageBox.confirm(msg.content || i18n.t('booking.pendingAudit'), i18n.t('booking.pendingAudit'), {
          confirmButtonText: i18n.t('modal.confirm'),
          cancelButtonText: i18n.t('modal.cancel'),
          type: 'warning'
        }).then(() => {
          this._jumpToList()
        }).catch(() => {})
      } else if (msg.type === 'carApplyAudit') {
        const loginName = store.getters.name || ''
        if (msg.createBy && msg.createBy !== loginName) return
        const approved = msg.status === '1'
        this._refreshBadge()
        this.emit('carApplyAudit', msg)
        Notification({
          title: msg.title || (approved ? i18n.t('booking.approved') : i18n.t('booking.rejected')),
          message: msg.content,
          type: approved ? 'success' : 'error',
          duration: 6000,
          onClick: () => {
            this._jumpToList()
          }
        })
        MessageBox.confirm(msg.content, msg.title || (approved ? i18n.t('booking.approved') : i18n.t('booking.rejected')), {
          confirmButtonText: i18n.t('modal.confirm'),
          cancelButtonText: i18n.t('modal.cancel'),
          type: approved ? 'success' : 'error'
        }).then(() => {
          this._jumpToList()
        }).catch(() => {})
      } else if (msg.type === 'carApplyApproved') {
        this._refreshBadge()
        this.emit('carApplyApproved', msg)
        MessageBox.confirm(msg.content, msg.title || i18n.t('booking.approved'), {
          confirmButtonText: i18n.t('modal.confirm'),
          cancelButtonText: i18n.t('modal.cancel'),
          type: 'success'
        }).then(() => {
          this._jumpToDuty()
        }).catch(() => {})
      }
    } catch (e) {
      console.error('Parse WebSocket message error', e)
    }
  }

  // 从已注册的动态路由中查找门卫出入登记（保安值班）页面的真实路径，避免写死路径导致 404
  _findDutyPath(routes) {
    for (const r of routes || []) {
      if (r.children && r.children.length) {
        const found = this._findDutyPath(r.children)
        if (found) return found
      }
      const title = (r.meta && r.meta.title) || ''
      const path = r.path || ''
      if (/(^|\/)duty/.test(path) || /门卫|門衛|保安值班/.test(title)) {
        return path
      }
    }
    return null
  }

  _jumpToDuty() {
    // 动态查找路由路径，找不到时回退默认路径
    const target = this._findDutyPath(store.getters.permission_routes) || '/car/duty'
    if (router.currentRoute.path === target) {
      this.emit('carApplyApproved', null)
    } else {
      router.push(target).catch(() => {})
    }
  }

  _jumpToList() {
    const target = '/car/car/apply/index'
    if (router.currentRoute.path === target) {
      this.emit('carApply', null)
    } else {
      router.push(target)
    }
  }

  _refreshBadge() {
    getUnreadCount().then(res => {
      if (store.commit) {
        store.commit('SET_NOTICE_BADGE', res.data || 0)
      }
    }).catch(() => {})
  }

  close() {
    this.intentionalClose = true
    this.reconnectCount = 0
    this.stopHeartbeat()
    if (this.ws) this.ws.close()
  }
}

const noticeSocket = new NoticeSocket()

// 热更新（HMR）时旧模块实例的连接不会自动关闭，会占着服务端 connKey 名额，
// 导致新实例连接被拒、反复重连刷日志。这里在模块被替换前主动断开旧连接。
if (module.hot) {
  module.hot.dispose(() => {
    try { noticeSocket.close() } catch (e) { /* ignore */ }
  })
}

export default noticeSocket