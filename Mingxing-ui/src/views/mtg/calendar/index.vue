<template>
  <div class="app-container">
    <div class="calendar-layout">
      <div class="calendar-left">
        <div class="calendar-header">
          <i class="el-icon-date"></i>
          <span class="title">{{ $t('calendar.title') }}</span>
        </div>
        <el-calendar v-model="calendarDate">
          <template slot="dateCell" slot-scope="{date, data}">
            <div class="calendar-cell" :class="{'has-booking': hasBookingOnDate(data.day)}" @click="handleDateClick(data.day)">
              <div class="cell-day">{{ data.day.split('-').slice(2).join('') }}</div>
              <div v-if="getBookingCount(data.day) > 0" class="booking-badge">
                {{ getBookingCount(data.day) }}{{ $t('calendar.bookingCount') }}
              </div>
            </div>
          </template>
        </el-calendar>
      </div>
      <div class="calendar-right">
        <div class="detail-header">
          <div class="detail-title">
            <i class="el-icon-time"></i>
            <span>{{ selectedDate }} {{ $t('calendar.detailTitle') }}</span>
          </div>
          <div class="detail-summary">
            <el-tag effect="plain" type="info" size="medium">{{ $t('calendar.totalBookings', { count: totalBookings }) }}</el-tag>
          </div>
        </div>
        <div v-loading="loading" class="room-list">
          <el-empty v-if="!loading && roomGroups.length === 0" :description="$t('calendar.empty')" :image-size="120"></el-empty>
          <div v-for="group in roomGroups" :key="group.roomId" class="room-card">
            <div class="room-info">
              <div class="room-header">
                <div class="room-name">
                  <i class="el-icon-office-building"></i>
                  <span>{{ $i18n.locale === 'en' && group.roomNameEn ? group.roomNameEn : (group.roomName || '') }}</span>
                </div>
                <el-tag v-if="group.bookings.length > 0" type="success" effect="dark" size="small">
                  {{ group.bookings.length }} {{ $t('calendar.meetingCount') }}
                </el-tag>
                <el-tag v-else type="info" effect="plain" size="small">{{ $t('calendar.free') }}</el-tag>
                <el-button type="primary" size="small" icon="el-icon-plus" @click="handleBookRoom(group)" v-hasPermi="['mtg:booking:add']">{{ $t('calendar.book') }}</el-button>
              </div>
              <div class="room-meta" v-if="group.capacity || (group.deviceList && group.deviceList.length > 0)">
                <span v-if="group.capacity" class="meta-item">
                  <i class="el-icon-user"></i> {{ $t('calendar.capacity', { count: group.capacity }) }}
                </span>
                <span v-for="device in group.deviceList" :key="device.deviceId || device.id || device.deviceName" class="meta-item">
                  <i :class="device.icon || 'el-icon-set-up'"></i> {{ $i18n.locale === 'en' && device.deviceNameEn ? device.deviceNameEn : (device.deviceName || device.name || '') }}
                </span>
              </div>
            </div>
            <div class="booking-list">
              <el-empty v-if="group.bookings.length === 0" :description="$t('calendar.roomEmpty')" :image-size="80"></el-empty>
              <div
                v-for="booking in group.bookings"
                :key="booking.bookingId"
                class="booking-item"
                :class="[
                  'status-' + (booking.bookingStatus || '0'),
                  (booking.bookerId !== currentUserId && !isAdmin) ? 'no-permission' : ''
                ]"
                @click="handleViewDetail(booking)"
              >
                <div class="booking-time">
                  <i class="el-icon-clock"></i>
                  <span class="time-range">{{ formatTime(booking.startTime) }} - {{ formatTime(booking.endTime) }}</span>
                </div>
                <div class="booking-content">
                  <div class="booking-title">
                    {{ $t('calendar.booker') }}：{{ booking.bookerName }}
                  </div>
                  <div class="booking-user">
                    <i class="el-icon-user-solid"></i>
                    <span>{{ $i18n.locale === 'en' && booking.deptNameEn ? booking.deptNameEn : (booking.deptName || '') }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <el-dialog :title="$t('calendar.detail')" :visible.sync="detailOpen" width="600px" append-to-body :close-on-click-modal="false">
      <el-descriptions v-if="detailForm.bookingId" :column="2" border>
        <el-descriptions-item :label="$t('calendar.subject')">{{ detailForm.subject }}</el-descriptions-item>
        <el-descriptions-item :label="$t('calendar.room')">{{ $i18n.locale === 'en' && detailForm.roomNameEn ? detailForm.roomNameEn : (detailForm.roomName || '') }}</el-descriptions-item>
        <el-descriptions-item :label="$t('calendar.bookingType')">{{ detailForm.bookingType === 'day' ? $t('calendar.daily') : $t('calendar.hourly') }}</el-descriptions-item>
        <el-descriptions-item :label="$t('calendar.bookingDate')">{{ formatDate(detailForm.bookingDate) }}</el-descriptions-item>
        <el-descriptions-item :label="$t('calendar.bookingPeriod')">{{ formatTime(detailForm.startTime) }} - {{ formatTime(detailForm.endTime) }}</el-descriptions-item>
        <el-descriptions-item :label="$t('calendar.attendees')">{{ detailForm.attendees }}</el-descriptions-item>
        <el-descriptions-item :label="$t('calendar.bookerLabel')">{{ detailForm.bookerName }}</el-descriptions-item>
        <el-descriptions-item :label="$t('calendar.dept')">{{ $i18n.locale === 'en' && detailForm.deptNameEn ? detailForm.deptNameEn : (detailForm.deptName || '') }}</el-descriptions-item>
        <el-descriptions-item :label="$t('calendar.contactPhone')">{{ detailForm.contactPhone }}</el-descriptions-item>
        <el-descriptions-item v-if="detailForm.meetingLink" :label="$t('calendar.meetingLink')">
          <a :href="detailForm.meetingLink" target="_blank" style="color: #409eff">{{ detailForm.meetingLink }}</a>
        </el-descriptions-item>
        <el-descriptions-item v-if="detailForm.meetingPassword" :label="$t('calendar.meetingPassword')">{{ detailForm.meetingPassword }}</el-descriptions-item>
        <el-descriptions-item v-if="detailForm.serviceItems" :label="$t('calendar.serviceItems')" :span="2">{{ translateServiceItems(detailForm.serviceItems) }}</el-descriptions-item>
        <el-descriptions-item :label="$t('calendar.status')" :span="2">
          <el-tag :type="statusType(detailForm.bookingStatus)" effect="dark">
            {{ statusText(detailForm.bookingStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item v-if="detailForm.cancelReason" :label="$t('calendar.cancelReason')" :span="2">
          {{ detailForm.cancelReason }}
        </el-descriptions-item>
        <el-descriptions-item v-if="detailForm.remark" :label="$t('calendar.remark')" :span="2">
          {{ detailForm.remark }}
        </el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="isOwnBooking || isAdmin" type="primary" icon="el-icon-edit" @click="handleEditFromDetail">{{ $t('calendar.edit') }}</el-button>
        <el-button v-if="isOwnBooking || isAdmin" type="danger" icon="el-icon-delete" @click="handleCancelFromDetail">{{ $t('calendar.delete') }}</el-button>
        <el-button @click="detailOpen = false">{{ $t('calendar.close') }}</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="$t('calendar.bookRoom')" :visible.sync="bookOpen" width="550px" append-to-body :close-on-click-modal="false">
      <el-form ref="bookForm" :model="bookForm" :rules="bookRules" label-width="150px">
        <el-form-item :label="$t('calendar.room')" prop="roomId">
          <el-input v-model="bookForm.roomName" disabled />
        </el-form-item>
        <el-form-item :label="$t('calendar.bookingType')" prop="bookingType">
          <el-radio-group v-model="bookForm.bookingType" @change="handleBookingTypeChange">
            <el-radio label="hour">{{ $t('calendar.hourly') }}</el-radio>
            <el-radio label="day">{{ $t('calendar.daily') }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="bookForm.bookingType === 'hour'" :label="$t('calendar.bookingDate')" prop="bookingDate">
          <span style="line-height: 32px; color: #303133; font-size: 14px;">{{ bookForm.bookingDate }}</span>
        </el-form-item>
        <el-form-item v-if="bookForm.bookingType === 'day'" :label="$t('calendar.bookingDate')" prop="dateRange">
          <el-date-picker v-model="bookForm.dateRange" type="daterange" :range-separator="$i18n.locale === 'zh-TW' ? '至' : 'to'" :start-placeholder="$t('calendar.startDate')" :end-placeholder="$t('calendar.endDate')" value-format="yyyy-MM-dd" :picker-options="dateRangeOptions" style="width: 100%" />
        </el-form-item>
        <el-form-item :label="$t('calendar.subject')" prop="subject">
          <el-input v-model="bookForm.subject" :placeholder="$t('calendar.placeholder.subject')" maxlength="50" />
        </el-form-item>
        <el-form-item :label="$t('calendar.startTime')" prop="startTime">
          <el-time-select v-model="bookForm.startTime" :picker-options="startTimeOptions" :placeholder="$t('calendar.placeholder.startTime')" />
        </el-form-item>
        <el-form-item :label="$t('calendar.endTime')" prop="endTime">
          <el-time-select v-model="bookForm.endTime" :picker-options="endTimeOptions" :placeholder="$t('calendar.placeholder.endTime')" />
        </el-form-item>
        <el-form-item :label="$t('calendar.attendees')" prop="attendees">
          <el-input-number v-model="bookForm.attendees" :min="1" :max="100" />
        </el-form-item>
        <el-form-item :label="$t('calendar.contactPhone')" prop="contactPhone">
          <el-input v-model="bookForm.contactPhone" :placeholder="$t('calendar.placeholder.phone')" maxlength="11" />
        </el-form-item>
        <el-form-item :label="$t('calendar.serviceItems')">
          <el-select v-model="bookForm.serviceItems" multiple :placeholder="$t('calendar.serviceSelectPlaceholder')" clearable style="width: 100%">
            <el-option v-for="item in serviceOptions.filter(d => d)" :key="item.serviceId" :label="capitalize($i18n.locale === 'en' && item.serviceNameEn ? item.serviceNameEn : (item.serviceName || ''))" :value="item.serviceName" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('calendar.meetingLink')">
          <el-input v-model="bookForm.meetingLink" :placeholder="$t('calendar.placeholder.link')" />
        </el-form-item>
        <el-form-item :label="$t('calendar.meetingPassword')">
          <el-input v-model="bookForm.meetingPassword" :placeholder="$t('calendar.placeholder.password')" />
        </el-form-item>
        <el-form-item :label="$t('calendar.remark')">
          <el-input v-model="bookForm.remark" type="textarea" :placeholder="$t('calendar.placeholder.remark')" :rows="3" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="bookOpen = false">{{ $t('calendar.cancel') }}</el-button>
        <el-button type="primary" :loading="bookLoading" @click="submitBook">{{ $t('calendar.submit') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getBookingCalendar, getBookingCalendarRange, addBooking, updateBooking, cancelBooking, deleteBooking } from "@/api/mtg/booking"
import { getAvailableRooms } from "@/api/mtg/room"
import { listEnabledService } from "@/api/mtg/service"

export default {
  name: "MtgCalendar",
  data() {
    return {
      calendarDate: new Date(),
      selectedDate: this.formatDateStr(new Date()),
      loading: false,
      allBookings: [],
      monthBookings: [],
      dayBookings: [],
      roomList: [],
      serviceOptions: [],
      detailOpen: false,
      detailForm: {},
      bookOpen: false,
      bookLoading: false,
      bookForm: {
        roomId: null,
        roomName: '',
        bookingDate: '',
        bookingType: 'hour',
        dateRange: null,
        subject: '',
        startTime: '',
        endTime: '',
        attendees: 1,
        contactPhone: '',
        meetingLink: '',
        meetingPassword: '',
        serviceItems: [],
        remark: ''
      },
      bookRules: {
        subject: [{ required: true, message: this.$t('calendar.validation.subject'), trigger: 'blur' }],
        startTime: [{ required: true, message: this.$t('calendar.validation.startTime'), trigger: 'change' }],
        endTime: [{ required: true, message: this.$t('calendar.validation.endTime'), trigger: 'change' }],
        attendees: [{ required: true, message: this.$t('calendar.validation.attendees'), trigger: 'blur' }],
        contactPhone: [{ required: true, message: this.$t('calendar.validation.phone'), trigger: 'blur' }],
        dateRange: [{ required: true, message: this.$t('calendar.dateRangeError'), trigger: 'change' }]
      },
      startTimeOptions: {
        start: '08:00',
        end: '20:00',
        step: '00:30'
      },
      endTimeOptions: {
        start: '08:00',
        end: '20:00',
        step: '00:30',
        minTime: ''
      },
      dateRangeOptions: {
        disabledDate(date) {
          return date && date < new Date(new Date().setHours(0, 0, 0, 0))
        }
      }
    }
  },
  computed: {
    roomGroups() {
      return this.roomList.map(room => {
        const bookings = this.dayBookings
          .filter(b => b.roomId === room.roomId)
          .sort((a, b) => this.parseTime(a.startTime) - this.parseTime(b.startTime))
        return {
          roomId: room.roomId,
          roomName: room.roomName,
          roomNameEn: room.roomNameEn,
          location: room.location,
          capacity: room.capacity,
          deviceList: room.deviceList || [],
          bookings: bookings
        }
      })
    },
    totalBookings() { return this.dayBookings.length },
    currentUserId() {
      return this.$store.state.user && this.$store.state.user.id
    },
    isOwnBooking() {
      if (!this.detailForm || !this.detailForm.bookerId) return false
      return this.detailForm.bookerId === this.currentUserId
    },
    isAdmin() {
      return this.$store.state.user && this.$store.state.user.name === 'admin'
    }
  },
  watch: {
    calendarDate(newVal) {
      const dateStr = this.formatDateStr(newVal)
      if (dateStr !== this.selectedDate) {
        this.selectedDate = dateStr
        this.loadDayBookings()
      }
      this.loadMonthBookings()
    },
    'bookForm.startTime'(val) {
      if (val) this.endTimeOptions.minTime = val
    }
  },
  created() {
    this.getRoomList()
    this.getServiceOptions()
    this.loadDayBookings()
    this.loadMonthBookings()
  },
  methods: {
    getRoomList() {
      getAvailableRooms().then(res => {
        this.roomList = res.data || []
      })
    },
    getServiceOptions() {
      listEnabledService().then(res => {
        this.serviceOptions = (res.data || []).filter(d => d && (d.serviceId || d.serviceName))
      })
    },
    loadDayBookings() {
      this.loading = true
      getBookingCalendar(this.selectedDate).then(res => {
        this.allBookings = res.data || []
        this.dayBookings = this.allBookings.filter(b => this.formatDateStr(b.bookingDate) === this.selectedDate)
        this.loading = false
      }).catch(() => { this.loading = false })
    },
    loadMonthBookings() {
      const d = new Date(this.calendarDate)
      const firstDay = new Date(d.getFullYear(), d.getMonth(), 1)
      const lastDay = new Date(d.getFullYear(), d.getMonth() + 1, 0)
      getBookingCalendarRange(this.formatDateStr(firstDay), this.formatDateStr(lastDay)).then(res => {
        this.monthBookings = res.data || []
      })
    },
    hasBookingOnDate(day) {
      return this.monthBookings.some(b => this.formatDateStr(b.bookingDate) === day)
    },
    getBookingCount(day) {
      return this.monthBookings.filter(b => this.formatDateStr(b.bookingDate) === day).length
    },
    handleDateClick(day) {
      this.selectedDate = day
      this.loadDayBookings()
    },
    handleViewDetail(booking) {
      if (booking.bookerId !== this.currentUserId && !this.isAdmin) {
        this.$message.warning(this.$t('calendar.noPermission'))
        return
      }
      this.detailForm = booking
      this.detailOpen = true
    },
    handleEditFromDetail() {
      this.detailOpen = false
      this.bookForm = {
        roomId: this.detailForm.roomId,
        roomName: this.detailForm.roomName,
        bookingDate: this.formatDateStr(this.detailForm.bookingDate),
        bookingType: this.detailForm.bookingType || 'hour',
        dateRange: null,
        subject: this.detailForm.subject,
        startTime: this.formatTime(this.detailForm.startTime),
        endTime: this.formatTime(this.detailForm.endTime),
        attendees: this.detailForm.attendees,
        contactPhone: this.detailForm.contactPhone,
        meetingLink: this.detailForm.meetingLink,
        meetingPassword: this.detailForm.meetingPassword,
        serviceItems: this.detailForm.serviceItems ? this.detailForm.serviceItems.split(',') : [],
        remark: this.detailForm.remark,
        bookingId: this.detailForm.bookingId
      }
      this.bookOpen = true
      this.$nextTick(() => { this.$refs.bookForm && this.$refs.bookForm.clearValidate() })
    },
    handleCancelFromDetail() {
      this.$modal.confirm(this.$t('calendar.confirmDelete'), this.$t('calendar.warning'), {
        confirmButtonText: this.$t('nav.confirm'),
        cancelButtonText: this.$t('nav.cancel'),
        type: 'warning'
      }).then(() => {
        return deleteBooking(this.detailForm.bookingId)
      }).then(() => {
        this.detailOpen = false
        this.loadDayBookings()
        this.$modal.msgSuccess(this.$t('calendar.deleted'))
      }).catch(() => {})
    },
    handleBookRoom(group) {
      const last = this.getSavedForm()
      this.bookForm = {
        roomId: group.roomId,
        roomName: group.roomName,
        bookingDate: this.selectedDate,
        bookingType: 'hour',
        dateRange: null,
        subject: last.subject || '',
        startTime: '',
        endTime: '',
        attendees: last.attendees || 1,
        contactPhone: last.contactPhone || '',
        meetingLink: last.meetingLink || '',
        meetingPassword: last.meetingPassword || '',
        serviceItems: [],
        remark: last.remark || ''
      }
      this.bookOpen = true
      this.$nextTick(() => { this.$refs.bookForm && this.$refs.bookForm.clearValidate() })
    },
    handleBookingTypeChange(val) {
      if (val === 'hour') {
        this.bookForm.dateRange = null
        this.bookForm.bookingDate = this.selectedDate
      } else {
        this.bookForm.bookingDate = ''
        this.bookForm.startTime = '08:00'
        this.bookForm.endTime = '18:00'
      }
    },
    submitBook() {
      this.$refs.bookForm.validate(valid => {
        if (!valid) return
        if (this.bookForm.startTime >= this.bookForm.endTime) {
          this.$message.error(this.$t('calendar.timeError'))
          return
        }
        let bookingDateStr = this.bookForm.bookingDate
        let endDateStr = null
        if (this.bookForm.bookingType === 'day') {
          if (!this.bookForm.dateRange || this.bookForm.dateRange.length < 2) {
            this.$message.error(this.$t('calendar.dateRangeError'))
            return
          }
          bookingDateStr = this.bookForm.dateRange[0]
          endDateStr = this.bookForm.dateRange[1]
        }
        const payload = {
          roomId: this.bookForm.roomId,
          subject: this.bookForm.subject,
          bookingDate: bookingDateStr,
          bookingType: this.bookForm.bookingType,
          startTime: bookingDateStr + ' ' + this.bookForm.startTime + ':00',
          endTime: bookingDateStr + ' ' + this.bookForm.endTime + ':00',
          attendees: this.bookForm.attendees,
          contactPhone: this.bookForm.contactPhone,
          meetingLink: this.bookForm.meetingLink,
          meetingPassword: this.bookForm.meetingPassword,
          serviceItems: Array.isArray(this.bookForm.serviceItems) ? this.bookForm.serviceItems.join(',') : this.bookForm.serviceItems,
          remark: this.bookForm.remark
        }
        if (this.bookForm.bookingId) {
          payload.bookingId = this.bookForm.bookingId
        }
        this.bookLoading = true
        const request = this.bookForm.bookingId ? updateBooking : addBooking
request(payload).then(res => {
  this.bookLoading = false
  this.saveForm({
    attendees: this.bookForm.attendees,
    contactPhone: this.bookForm.contactPhone,
    meetingLink: this.bookForm.meetingLink,
    meetingPassword: this.bookForm.meetingPassword,
    subject: this.bookForm.subject,
    remark: this.bookForm.remark
  })
  this.$message.success(this.bookForm.bookingId ? this.$t('calendar.edited') : this.$t('calendar.booked'))
  this.bookOpen = false
  this.loadDayBookings()
}).catch(() => { this.bookLoading = false })
      })
    },
    saveForm(fields) {
      try {
        localStorage.setItem('mtg_booking_form', JSON.stringify(fields))
      } catch (e) {}
    },
    getSavedForm() {
      try {
        const raw = localStorage.getItem('mtg_booking_form')
        if (raw) return JSON.parse(raw)
      } catch (e) {}
      return {}
    },
    statusType(status) {
      const s = status || '0'
      return { '0': 'warning', '1': 'success', '2': 'danger', '3': 'info', '5': 'primary' }[s] || 'info'
    },
    statusText(status) {
      const s = status || '0'
      const map = {
        '0': this.$t('calendar.bookedStatus'),
        '1': this.$t('calendar.confirmed'),
        '2': this.$t('calendar.rejected'),
        '3': this.$t('calendar.cancelled'),
        '5': this.$t('calendar.fixed')
      }
      return map[s] || this.$t('calendar.bookedStatus')
    },
    translateServiceItems(items) {
      if (!items) return ''
      const names = items.split(',')
      if (this.$i18n.locale !== 'en') return items
      return names.map(name => {
        const found = this.serviceOptions.find(s => s.serviceName === name.trim())
        return found && found.serviceNameEn ? this.capitalize(found.serviceNameEn) : (name.trim() || '')
      }).join(', ')
    },
    capitalize(str) {
      return str ? str.replace(/\b\w/g, c => c.toUpperCase()) : ''
    },
    formatTime(dateStr) {
      if (!dateStr) return ''
      if (typeof dateStr === 'string' && dateStr.length === 5) return dateStr
      const d = new Date(dateStr)
      return String(d.getHours()).padStart(2, '0') + ':' + String(d.getMinutes()).padStart(2, '0')
    },
    parseTime(dateStr) {
      if (!dateStr) return 0
      if (typeof dateStr === 'string' && dateStr.includes(':')) {
        const parts = dateStr.split(':')
        return parseInt(parts[0]) * 60 + parseInt(parts[1])
      }
      const d = new Date(dateStr)
      return d.getHours() * 60 + d.getMinutes()
    },
    formatDate(dateStr) {
      if (!dateStr) return ''
      return this.formatDateStr(dateStr)
    },
    formatDateStr(date) {
      if (!date) return ''
      const d = typeof date === 'string' ? new Date(date) : date
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return year + '-' + month + '-' + day
    }
  }
}
</script>

<style scoped>
.calendar-layout {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Microsoft YaHei', Arial, sans-serif;
  display: flex;
  gap: 24px;
  min-height: calc(100vh - 140px);
}
.calendar-left {
  flex: 0 0 33.3333%;
  max-width: 33.3333%;
  background: #fff;
  border-radius: 4px;
  padding: 24px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);
  border: 1px solid #e8ecef;
}
.calendar-header {
  display: flex;
  align-items: center;
  font-size: 16px;
  font-weight: 700;
  color: #1a2744;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e8ecef;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}
.calendar-header .title { margin-left: 8px; }
.calendar-header i { color: #1a2744; font-size: 18px; }
.calendar-right {
  flex: 1;
  background: #fff;
  border-radius: 4px;
  padding: 24px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);
  border: 1px solid #e8ecef;
  overflow-y: auto;
}
.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e8ecef;
  flex-wrap: wrap;
  gap: 12px;
}
.detail-title {
  display: flex;
  align-items: center;
  font-size: 16px;
  font-weight: 700;
  color: #1a2744;
  text-transform: uppercase;
  letter-spacing: 0.3px;
}
.detail-title i { color: #1a2744; margin-right: 8px; font-size: 18px; }
.detail-summary { display: flex; gap: 8px; }
::v-deep .el-calendar__body { padding: 0; }
::v-deep .el-calendar-day { height: 80px; padding: 0; }
.calendar-cell {
  width: 100%; height: 100%; display: flex; flex-direction: column;
  align-items: center; justify-content: center; cursor: pointer;
  transition: background-color 0.15s ease; padding: 4px;
}
.calendar-cell:hover { background-color: #f5f7fa; }
.cell-day { font-size: 15px; color: #1a2744; font-weight: 500; }
.calendar-cell.has-booking .cell-day { color: #2c5282; font-weight: 700; }
.booking-badge {
  margin-top: 4px; padding: 2px 8px; font-size: 10px;
  background: #2c5282; color: #fff;
  border-radius: 3px; line-height: 1.4; font-weight: 500;
  letter-spacing: 0.3px;
}
::v-deep .el-calendar-table td.is-selected .calendar-cell { background-color: #edf2f7; }
::v-deep .el-calendar-table td.is-today .cell-day {
  color: #fff; font-weight: 700;
  background: #2c5282;
  border-radius: 50%; width: 28px; height: 28px;
  display: flex; align-items: center; justify-content: center;
}
.room-list { display: flex; flex-direction: column; gap: 16px; }
.room-card {
  border: 1px solid #e8ecef; border-radius: 4px; overflow: hidden;
  transition: all 0.2s ease;
}
.room-card:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  border-color: #d0d7de;
}
.room-info {
  padding: 16px 20px;
  background: #f8fafc;
  border-bottom: 1px solid #e8ecef;
}
.room-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 8px; gap: 8px; }
.room-name { display: flex; align-items: center; font-size: 15px; font-weight: 700; color: #1a2744; }
.room-name i { color: #1a2744; margin-right: 8px; font-size: 16px; }
.room-meta { display: flex; flex-wrap: wrap; gap: 16px; font-size: 13px; color: #5a6a7a; }
.meta-item { display: flex; align-items: center; }
.meta-item i { margin-right: 4px; color: #8896a6; }
.booking-list { padding: 12px 20px; min-height: 60px; }
.booking-item {
  display: flex; align-items: center; gap: 16px; padding: 12px 16px;
  margin-bottom: 8px; border-radius: 4px; background: #f8fafc;
  border-left: 3px solid #8896a6; cursor: pointer; transition: all 0.15s ease;
}
.booking-item:last-child { margin-bottom: 0; }
.booking-item:hover {
  background: #edf2f7;
  transform: translateX(2px);
}
.booking-item.status-0 { border-left-color: #d69e2e; background: #fefcf3; }
.booking-item.status-1 { border-left-color: #38a169; background: #f0fff4; }
.booking-item.status-2 { border-left-color: #e53e3e; background: #fff5f5; }
.booking-item.status-3 { border-left-color: #8896a6; background: #f7fafc; }
.booking-item.no-permission { opacity: 0.4; cursor: not-allowed; }
.booking-item.no-permission:hover { transform: none; }
.booking-time { display: flex; align-items: center; min-width: 140px; font-weight: 600; color: #1a2744; font-size: 13px; }
.booking-time i { margin-right: 6px; color: #2c5282; }
.booking-content { flex: 1; min-width: 0; }
.booking-title { font-size: 14px; color: #1a2744; font-weight: 500; margin-bottom: 4px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.booking-user { display: flex; align-items: center; font-size: 12px; color: #718096; }
.booking-user i { margin-right: 4px; }
.booking-dept { color: #a0aec0; }
@media (max-width: 1100px) {
  .calendar-layout { flex-direction: column; }
  .calendar-left { flex: 1; max-width: 100%; }
}
</style>
