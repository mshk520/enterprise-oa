<template>
  <div class="app-container home">
    <div class="welcome-section">
      <h2 class="welcome-title">{{ greeting }}，{{ $store.state.user.nickName || $store.state.user.name }}</h2>
      <p class="welcome-date">{{ currentDate }}</p>
    </div>

    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="9">
        <div class="module-grid">
          <div class="module-card" @click="$router.push('/mtg/calendar')">
            <div class="module-icon meeting-icon">
              <i class="el-icon-date"></i>
            </div>
            <div class="module-info">
              <div class="module-name">{{ $t('index.meeting') }}</div>
              <div class="module-desc">{{ $t('index.meetingDesc') }}</div>
            </div>
            <el-button type="primary" size="small" round class="module-btn">{{ $t('index.enter') }}</el-button>
          </div>
          <div class="module-card" @click="$router.push('/car/car/apply/index')">
            <div class="module-icon car-icon">
              <i class="el-icon-truck"></i>
            </div>
            <div class="module-info">
              <div class="module-name">{{ $t('index.car') }}</div>
              <div class="module-desc">{{ $t('index.carDesc') }}</div>
            </div>
            <el-button type="primary" size="small" round class="module-btn">{{ $t('index.enter') }}</el-button>
          </div>
        </div>
      </el-col>

      <el-col :xs="24" :sm="24" :md="15">
        <el-card class="info-card" shadow="never">
          <div slot="header" class="card-header">
            <i class="el-icon-bell"></i>
            <span>{{ $t('index.notices') }}</span>
            <el-button
              v-if="unreadCount > 0"
              type="text"
              size="mini"
              class="mark-read-btn"
              @click="handleMarkAllRead"
            >{{ $t('index.markAllRead') }}</el-button>
          </div>
          <div class="notice-body" v-loading="noticeLoading">
            <div v-if="notices.length === 0" class="list-empty">
              <i class="el-icon-info"></i>
              <p>{{ $t('index.noNotice') }}</p>
            </div>
            <div
              v-for="item in notices"
              :key="item.noticeId"
              class="notice-item"
              @click="handleReadNotice(item)"
            >
              <span class="notice-dot" :class="{ unread: !item.isRead }"></span>
              <span class="notice-title">{{ $i18n.locale === 'en' && item.noticeTitleEn ? item.noticeTitleEn : item.noticeTitle }}</span>
              <span class="notice-time">{{ formatTimeStr(item.createTime) }}</span>
            </div>
          </div>
        </el-card>

        <el-card class="info-card" shadow="never">
          <div slot="header" class="card-header">
            <i class="el-icon-document-checked"></i>
            <span>{{ $t('index.myBookings') }}</span>
            <el-button type="text" size="mini" class="mark-read-btn" @click="$router.push('/mtg/booking')">{{ $t('index.viewAll') }}</el-button>
          </div>
          <div class="booking-body" v-loading="bookingLoading">
            <div v-if="myBookings.length === 0" class="list-empty">
              <i class="el-icon-s-order"></i>
              <p>{{ $t('index.noMyBooking') }}</p>
            </div>
            <div
              v-for="item in myBookings"
              :key="item.bookingId"
              class="booking-item"
              @click="$router.push('/mtg/booking')"
            >
              <div class="booking-date">
                <span class="date-day">{{ formatDay(item.bookingDate) }}</span>
                <span class="date-month">{{ formatMonth(item.bookingDate) }}</span>
              </div>
              <div class="booking-info">
                <div class="booking-subject">{{ item.subject }}</div>
                <div class="booking-meta">
                  <i class="el-icon-time"></i>
                  {{ formatTime(item.startTime) }} - {{ formatTime(item.endTime) }}
                  <i class="el-icon-office-building" style="margin-left: 12px;"></i>
                  {{ $i18n.locale === 'en' && item.roomNameEn ? item.roomNameEn : item.roomName }}
                </div>
              </div>
              <el-tag :type="bookingStatusType(item.bookingStatus)" size="small" effect="plain">
                {{ bookingStatusText(item.bookingStatus) }}
              </el-tag>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listBooking } from "@/api/mtg/booking"
import { listNoticeTop, markNoticeRead, markNoticeReadAll } from "@/api/system/notice"

export default {
  name: "Index",
  data() {
    return {
      notices: [],
      unreadCount: 0,
      noticeLoading: false,
      myBookings: [],
      bookingLoading: false,
      weekDays: ['index.sun', 'index.mon', 'index.tue', 'index.wed', 'index.thu', 'index.fri', 'index.sat'],
      greetings: {
        night: 'index.night',
        morning: 'index.morning',
        noon: 'index.noon',
        afternoon: 'index.afternoon',
        evening: 'index.evening'
      }
    }
  },
  computed: {
    currentDate() {
      const d = new Date()
      if (this.$i18n.locale === 'zh-TW') {
        return d.getFullYear() + '年' + (d.getMonth() + 1) + '月' + d.getDate() + '日 ' + this.$t(this.weekDays[d.getDay()])
      } else {
        // English format: "Month DD, YYYY, Day"
        return d.toLocaleDateString('en-US', {
          year: 'numeric',
          month: 'long',
          day: 'numeric',
          weekday: 'long'
        })
      }
    },
    greeting() {
      const h = new Date().getHours()
      if (h < 6) return this.$t(this.greetings.night)
      if (h < 12) return this.$t(this.greetings.morning)
      if (h < 14) return this.$t(this.greetings.noon)
      if (h < 18) return this.$t(this.greetings.afternoon)
      return this.$t(this.greetings.evening)
    }
  },
  created() {
    this.getNotices()
    this.getMyBookings()
  },
  methods: {
    getNotices() {
      this.noticeLoading = true
      listNoticeTop().then(res => {
        this.notices = res.data || []
        this.unreadCount = res.unreadCount || 0
        this.noticeLoading = false
      }).catch(() => { this.noticeLoading = false })
    },
    handleReadNotice(item) {
      if (!item.isRead) {
        markNoticeRead(item.noticeId).then(() => {
          item.isRead = true
          this.unreadCount = Math.max(0, this.unreadCount - 1)
        })
      }
    },
    handleMarkAllRead() {
      const ids = this.notices.filter(n => !n.isRead).map(n => n.noticeId)
      if (ids.length === 0) return
      markNoticeReadAll(ids.join(',')).then(() => {
        this.notices.forEach(n => { n.isRead = true })
        this.unreadCount = 0
      })
    },
    getMyBookings() {
      this.bookingLoading = true
      listBooking({ pageNum: 1, pageSize: 5 }).then(res => {
        this.myBookings = res.rows || []
        this.bookingLoading = false
      }).catch(() => { this.bookingLoading = false })
    },
    formatDay(dateStr) {
      if (!dateStr) return ''
      return String(new Date(dateStr).getDate()).padStart(2, '0')
    },
    formatMonth(dateStr) {
      if (!dateStr) return ''
      return new Date(dateStr).getMonth() + 1
    },
    formatTime(dateStr) {
      if (!dateStr) return ''
      if (typeof dateStr === 'string' && dateStr.length === 5) return dateStr
      const d = new Date(dateStr)
      return String(d.getHours()).padStart(2, '0') + ':' + String(d.getMinutes()).padStart(2, '0')
    },
    formatTimeStr(dateStr) {
      if (!dateStr) return ''
      const d = new Date(dateStr)
      return String(d.getMonth() + 1).padStart(2, '0') + '-' + String(d.getDate()).padStart(2, '0')
    },
    bookingStatusType(status) {
      const s = status || '0'
      return { '0': 'warning', '1': 'success', '2': 'danger', '3': 'info', '5': 'primary' }[s] || 'info'
    },
    bookingStatusText(status) {
      const s = status || '0'
      const map = {
        '0': this.$t('calendar.bookedStatus'),
        '1': this.$t('calendar.confirmed'),
        '2': this.$t('calendar.rejected'),
        '3': this.$t('calendar.cancelled'),
        '5': this.$t('calendar.fixed')
      }
      return map[s] || this.$t('calendar.bookedStatus')
    }
  }
}
</script>

<style lang="scss" scoped>
.home {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Microsoft YaHei', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
.welcome-section {
  margin-bottom: 24px;
}
.welcome-title {
  font-size: 20px;
  font-weight: 600;
  color: #1a2744;
  margin-bottom: 6px;
}
.welcome-date {
  font-size: 13px;
  color: #718096;
}
.module-grid {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.module-card {
  display: flex;
  align-items: center;
  padding: 20px;
  background: #fff;
  border: 1px solid #e8ecef;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
  gap: 16px;
  &:hover {
    border-color: #5777ba;
    box-shadow: 0 2px 12px rgba(87, 119, 186, 0.12);
    transform: translateY(-2px);
  }
}
.module-icon {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  i {
    font-size: 28px;
    color: #fff;
  }
}
.meeting-icon {
  background: linear-gradient(135deg, #5777ba, #7d9bd6);
}
.car-icon {
  background: linear-gradient(135deg, #38a169, #6bc98f);
}
.module-info {
  flex: 1;
  min-width: 0;
}
.module-name {
  font-size: 16px;
  font-weight: 700;
  color: #1a2744;
  margin-bottom: 6px;
}
.module-desc {
  font-size: 12px;
  color: #718096;
  line-height: 1.5;
}
.module-btn {
  flex-shrink: 0;
}
.info-card {
  border: 1px solid #e8ecef;
  border-radius: 8px;
  margin-bottom: 16px;
  border-top: 1px solid #e8ecef;
}
.card-header {
  font-size: 14px;
  font-weight: 600;
  color: #1a2744;
  display: flex;
  align-items: center;
  gap: 6px;
  i { color: #5777ba; }
}
.mark-read-btn {
  margin-left: auto;
  padding: 0;
}
.notice-body {
  min-height: 120px;
}
.notice-item {
  display: flex;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #f0f2f5;
  cursor: pointer;
  gap: 10px;
  &:last-child { border-bottom: none; }
  &:hover .notice-title { color: #2c5282; }
}
.notice-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #cbd5e0;
  flex-shrink: 0;
  &.unread { background: #e53e3e; }
}
.notice-title {
  flex: 1;
  min-width: 0;
  font-size: 13px;
  color: #4a5568;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.notice-time {
  font-size: 12px;
  color: #a0aec0;
  flex-shrink: 0;
}
.booking-body {
  min-height: 140px;
}
.booking-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f2f5;
  cursor: pointer;
  gap: 16px;
  &:last-child { border-bottom: none; }
  &:hover {
    background: #f8fafc;
  }
}
.booking-date {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  border-radius: 8px;
  background: #edf2f7;
  flex-shrink: 0;
}
.date-day {
  font-size: 18px;
  font-weight: 700;
  color: #2c5282;
  line-height: 1.2;
}
.date-month {
  font-size: 12px;
  color: #718096;
}
.booking-info {
  flex: 1;
  min-width: 0;
}
.booking-subject {
  font-size: 14px;
  color: #1a2744;
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-bottom: 4px;
}
.booking-meta {
  font-size: 12px;
  color: #718096;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}
.booking-meta i {
  margin-right: 4px;
}
.list-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 120px;
  color: #a0aec0;
  i { font-size: 32px; color: #d1d5db; }
  p {
    font-size: 13px;
    margin-top: 8px;
  }
}
</style>