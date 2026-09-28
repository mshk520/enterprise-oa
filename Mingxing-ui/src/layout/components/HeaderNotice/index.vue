<template>
  <div>
    <el-popover ref="noticePopover" placement="bottom-end" width="360" trigger="manual" :value="noticeVisible" popper-class="notice-popover">
      <div class="notice-header">
        <span class="notice-title">消息中心</span>
        <span class="notice-mark-all" @click="markAllRead">全部已读</span>
      </div>
      <div v-if="noticeLoading" class="notice-loading"><i class="el-icon-loading"></i> 加载中...</div>
      <div v-else-if="noticeList.length === 0" class="notice-empty"><i class="el-icon-inbox"></i><br>暂无消息</div>
      <div v-else class="notice-list">
        <div v-for="item in noticeList" :key="item.msgId" class="notice-item" :class="{ 'is-read': item.isRead === '1' }" @click="handleClick(item)">
          <div class="notice-item-icon">
            <i :class="getIconClass(item)"></i>
          </div>
          <div class="notice-item-body">
            <div class="notice-item-title">{{ item.msgTitle }}</div>
            <div class="notice-item-content">{{ item.msgContent }}</div>
            <div class="notice-item-date">{{ item.createTime }}</div>
          </div>
          <div v-if="item.isRead === '0'" class="notice-dot"></div>
        </div>
      </div>
    </el-popover>

    <div v-popover:noticePopover class="right-menu-item hover-effect notice-trigger" @mouseenter="onNoticeEnter" @mouseleave="onNoticeLeave">
      <svg-icon icon-class="bell" />
      <span v-if="unreadCount > 0" class="notice-badge">{{ unreadCount > 99 ? '99+' : unreadCount }}</span>
    </div>
  </div>
</template>

<script>
import { listNoticeMsg, readNoticeMsg, readAllNoticeMsg } from '@/api/system/noticeMsg'

export default {
  name: 'HeaderNotice',
  data() {
    return {
      noticeList: [],
      unreadCount: 0,
      noticeLoading: false,
      noticeVisible: false,
      noticeLeaveTimer: null
    }
  },
  mounted() {
    this.loadMessages()
  },
  methods: {
    onNoticeEnter() {
      clearTimeout(this.noticeLeaveTimer)
      this.noticeVisible = true
      this.loadMessages()
      this.$nextTick(() => {
        const popper = this.$refs.noticePopover.$refs.popper
        if (popper && !popper._noticeBound) {
          popper._noticeBound = true
          popper.addEventListener('mouseenter', () => clearTimeout(this.noticeLeaveTimer))
          popper.addEventListener('mouseleave', () => {
            this.noticeLeaveTimer = setTimeout(() => { this.noticeVisible = false }, 100)
          })
        }
      })
    },
    onNoticeLeave() {
      this.noticeLeaveTimer = setTimeout(() => { this.noticeVisible = false }, 150)
    },
    loadMessages() {
      this.noticeLoading = true
      listNoticeMsg().then(res => {
        this.noticeList = res.data || []
        this.unreadCount = this.noticeList.filter(n => n.isRead === '0').length
      }).finally(() => {
        this.noticeLoading = false
      })
    },
    getIconClass(item) {
      if (item.businessType === 'car:apply') return 'el-icon-s-promotion'
      if (item.businessType === 'car:duty') return 'el-icon-s-claim'
      return 'el-icon-bell'
    },
    handleClick(item) {
      if (item.isRead === '0') {
        readNoticeMsg(item.msgId).catch(() => {})
        item.isRead = '1'
        this.unreadCount = Math.max(0, this.unreadCount - 1)
      }
    },
    markAllRead() {
      if (this.unreadCount === 0) return
      readAllNoticeMsg().then(() => {
        this.noticeList = this.noticeList.map(n => ({ ...n, isRead: '1' }))
        this.unreadCount = 0
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.notice-trigger {
  position: relative;
  transform: translateX(-6px);
  .svg-icon { width: 1.2em; height: 1.2em; vertical-align: -0.2em; }
  .notice-badge {
    position: absolute;
    top: 7px;
    right: -3px;
    background: #f56c6c;
    color: #fff;
    border-radius: 10px;
    font-size: 10px;
    height: 16px;
    line-height: 16px;
    padding: 0 4px;
    min-width: 16px;
    text-align: center;
    white-space: nowrap;
    pointer-events: none;
  }
}
.notice-popover {
  padding: 0 !important;
}
.notice-popover .notice-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 14px;
  background: #f7f9fb;
  border-bottom: 1px solid #eee;
  font-size: 13px;
  font-weight: 600;
  color: #333;
}
.notice-popover .notice-mark-all {
  font-size: 12px;
  color: #409EFF;
  font-weight: normal;
  cursor: pointer;
}
.notice-popover .notice-mark-all:hover { color: #2b7cc1; }
.notice-popover .notice-loading,
.notice-popover .notice-empty {
  padding: 24px;
  text-align: center;
  color: #bbb;
  font-size: 12px;
  line-height: 1.8;
}
.notice-list {
  max-height: 400px;
  overflow-y: auto;
}
.notice-item {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  padding: 10px 14px;
  border-bottom: 1px solid #f5f5f5;
  cursor: pointer;
  transition: background 0.15s;
  position: relative;
}
.notice-item:last-child { border-bottom: none; }
.notice-item:hover { background: #f7f9fb; }
.notice-item.is-read { opacity: 0.55; }
.notice-item-icon {
  flex-shrink: 0;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #ecf5ff;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 2px;
  i { color: #409EFF; font-size: 16px; }
}
.notice-item-body {
  flex: 1;
  min-width: 0;
}
.notice-item-title {
  font-size: 13px;
  color: #333;
  font-weight: 500;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.notice-item-content {
  font-size: 12px;
  color: #999;
  margin-top: 2px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.notice-item-date {
  font-size: 11px;
  color: #bbb;
  margin-top: 4px;
}
.notice-dot {
  flex-shrink: 0;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #f56c6c;
  margin-top: 6px;
}
</style>
