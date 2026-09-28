<template>
  <div class="app-container">
    <div class="calendar-layout">
      <div class="calendar-left">
        <div class="calendar-header">
          <i class="el-icon-date"></i>
          <span class="title">会议室使用情况</span>
        </div>
        <el-calendar v-model="calendarDate">
          <template slot="dateCell" slot-scope="{date, data}">
            <div class="calendar-cell" :class="{'has-booking': hasBookingOnDate(data.day)}" @click="handleDateClick(data.day)">
              <div class="cell-day">{{ data.day.split('-').slice(2).join('') }}</div>
              <div v-if="getBookingCount(data.day) > 0" class="booking-badge">
                {{ getBookingCount(data.day) }}个预约
              </div>
            </div>
          </template>
        </el-calendar>
      </div>
      <div class="calendar-right">
        <div class="detail-header">
          <div class="detail-title">
            <i class="el-icon-time"></i>
            <span>{{ selectedDate }} 会议室预约详情</span>
          </div>
          <div class="detail-summary">
            <el-tag effect="plain" type="info" size="medium">共 {{ totalBookings }} 个预约</el-tag>
            <el-tag effect="plain" type="success" size="medium">已通过 {{ approvedCount }}</el-tag>
            <el-tag effect="plain" type="warning" size="medium">待审核 {{ pendingCount }}</el-tag>
          </div>
        </div>
        <div v-loading="loading" class="room-list">
          <el-empty v-if="!loading && roomGroups.length === 0" description="该日暂无预约数据" :image-size="120"></el-empty>
          <div v-for="group in roomGroups" :key="group.roomId" class="room-card">
            <div class="room-info">
              <div class="room-header">
                <div class="room-name">
                  <i class="el-icon-office-building"></i>
                  <span>{{ group.roomName }}</span>
                </div>
                <el-tag v-if="group.bookings.length > 0" type="success" effect="dark" size="small">
                  {{ group.bookings.length }} 个会议
                </el-tag>
                <el-tag v-else type="info" effect="plain" size="small">空闲</el-tag>
                <el-button type="primary" size="small" icon="el-icon-plus" @click="handleBookRoom(group)">预约</el-button>
              </div>
              <div class="room-meta" v-if="group.location || group.capacity || group.equipment">
                <span v-if="group.location" class="meta-item">
                  <i class="el-icon-location-information"></i> {{ group.location }}
                </span>
                <span v-if="group.capacity" class="meta-item">
                  <i class="el-icon-user"></i> 容纳 {{ group.capacity }} 人
                </span>
                <span v-if="group.equipment" class="meta-item">
                  <i class="el-icon-set-up"></i> {{ group.equipment }}
                </span>
              </div>
            </div>
            <div class="booking-list">
              <el-empty v-if="group.bookings.length === 0" description="该会议室今日空闲" :image-size="80"></el-empty>
              <div
                v-for="booking in group.bookings"
                :key="booking.bookingId"
                class="booking-item"
                :class="['status-' + booking.bookingStatus, { 'is-fixed': booking.fixedId }]"
                @click="handleViewDetail(booking)"
              >
                <div class="booking-time">
                  <i class="el-icon-clock"></i>
                  <span class="time-range">{{ formatTime(booking.startTime) }} - {{ formatTime(booking.endTime) }}</span>
                </div>
              <div class="booking-content">
                  <div class="booking-title">
                    <el-tag v-if="booking.fixedId" size="mini" type="danger" effect="dark" style="margin-right:4px">固定</el-tag>
                    {{ booking.bookingTitle }}
                  </div>
                  <div class="booking-user">
                    <i class="el-icon-user-solid"></i>
                    <span>{{ booking.bookingUser }}</span>
                    <span v-if="booking.bookingDept" class="booking-dept">（{{ booking.bookingDept }}）</span>
                  </div>
                </div>
                <div class="booking-status">
                  <el-tag :type="statusType(booking.bookingStatus)" effect="dark" size="small">
                    {{ statusText(booking.bookingStatus) }}
                  </el-tag>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <el-dialog title="预约详情" :visible.sync="detailOpen" width="600px" append-to-body :close-on-click-modal="false">
      <el-descriptions v-if="detailForm.bookingId" :column="2" border>
        <el-descriptions-item label="会议主题">
          <el-tag v-if="detailForm.fixedId" size="mini" type="danger" effect="dark" style="margin-right:4px">固定预约</el-tag>
          {{ detailForm.bookingTitle }}
        </el-descriptions-item>
        <el-descriptions-item label="会议室">{{ detailForm.roomName }}</el-descriptions-item>
        <el-descriptions-item label="预约日期">{{ formatDate(detailForm.bookingDate) }}</el-descriptions-item>
        <el-descriptions-item label="预约时段">{{ formatTime(detailForm.startTime) }} - {{ formatTime(detailForm.endTime) }}</el-descriptions-item>
        <el-descriptions-item label="预约人">{{ detailForm.bookingUser }}</el-descriptions-item>
        <el-descriptions-item label="预约部门">{{ detailForm.bookingDept }}</el-descriptions-item>
        <el-descriptions-item label="参会人数">{{ detailForm.attendees }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detailForm.contactPhone }}</el-descriptions-item>
        <el-descriptions-item v-if="detailForm.meetingLink" label="会议链接">
          <a :href="detailForm.meetingLink" target="_blank" style="color: #409eff">{{ detailForm.meetingLink }}</a>
        </el-descriptions-item>
        <el-descriptions-item v-if="detailForm.meetingPassword" label="会议密码">{{ detailForm.meetingPassword }}</el-descriptions-item>
        <el-descriptions-item label="预约状态" :span="2">
          <el-tag :type="statusType(detailForm.bookingStatus)" effect="dark">
            {{ statusText(detailForm.bookingStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item v-if="detailForm.rejectReason" label="拒绝原因" :span="2">
          {{ detailForm.rejectReason }}
        </el-descriptions-item>
        <el-descriptions-item v-if="detailForm.remark" label="备注" :span="2">
          {{ detailForm.remark }}
        </el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="(detailForm.bookingStatus === '0' || detailForm.bookingStatus === '1') && (isAdmin || !detailForm.fixedId)" type="primary" icon="el-icon-edit" @click="handleEditFromDetail">修 改</el-button>
        <el-button v-if="(detailForm.bookingStatus === '0' || detailForm.bookingStatus === '1') && (isAdmin || !detailForm.fixedId)" type="danger" icon="el-icon-delete" @click="handleCancelFromDetail">取 消</el-button>
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
    <el-dialog title="预约会议室" :visible.sync="bookOpen" width="550px" append-to-body :close-on-click-modal="false">
      <el-form ref="bookForm" :model="bookForm" :rules="bookRules" label-width="100px">
        <el-form-item label="会议室" prop="roomId">
          <el-input v-model="bookForm.roomName" disabled />
        </el-form-item>
        <el-form-item label="预约类型" prop="bookingType">
          <el-radio-group v-model="bookForm.bookingType" @change="handleBookingTypeChange">
            <el-radio label="hour">按小时</el-radio>
            <el-radio label="day">按天</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="bookForm.bookingType === 'hour'" label="预约日期" prop="bookingDate">
          <span style="line-height: 32px; color: #303133; font-size: 14px;">{{ bookForm.bookingDate }}</span>
        </el-form-item>
        <el-form-item v-if="bookForm.bookingType === 'day'" label="预约日期" prop="dateRange">
          <el-date-picker v-model="bookForm.dateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd" :picker-options="dateRangeOptions" style="width: 100%" />
        </el-form-item>
        <el-form-item label="会议主题" prop="bookingTitle">
          <el-input v-model="bookForm.bookingTitle" placeholder="请输入会议主题" maxlength="50" />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-time-select v-model="bookForm.startTime" :picker-options="startTimeOptions" placeholder="选择开始时间" />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-time-select v-model="bookForm.endTime" :picker-options="endTimeOptions" placeholder="选择结束时间" />
        </el-form-item>
        <el-form-item label="参会人数" prop="attendees">
          <el-input-number v-model="bookForm.attendees" :min="1" :max="100" />
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="bookForm.contactPhone" placeholder="请输入联系电话" maxlength="11" />
        </el-form-item>
        <el-form-item label="会议链接">
          <el-input v-model="bookForm.meetingLink" placeholder="请输入会议链接（如腾讯会议、Zoom链接）" />
        </el-form-item>
        <el-form-item label="会议密码">
          <el-input v-model="bookForm.meetingPassword" placeholder="请输入会议密码" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="bookForm.remark" type="textarea" placeholder="请输入备注" :rows="3" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="bookOpen = false">取 消</el-button>
        <el-button type="primary" :loading="bookLoading" @click="submitBook">提 交</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getRoomCalendar, getRoomCalendarRange } from "@/api/meeting/calendar"
import { listAvailableRoom } from "@/api/meeting/room"
import { addBooking, updateBooking, cancelBooking } from "@/api/meeting/booking"

export default {
  name: "MeetingCalendar",
  data() {
    return {
      calendarDate: new Date(),
      selectedDate: this.formatDateString(new Date()),
      loading: false,
      allBookings: [],
      monthBookings: [],
      dayBookings: [],
      roomList: [],
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
        bookingTitle: '',
        startTime: '',
        endTime: '',
        attendees: 1,
        contactPhone: '',
        meetingLink: '',
        meetingPassword: '',
        remark: ''
      },
      bookRules: {
        bookingTitle: [{ required: true, message: '请输入会议主题', trigger: 'blur' }],
        startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
        endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
        attendees: [{ required: true, message: '请输入参会人数', trigger: 'blur' }],
        contactPhone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
        dateRange: [{ required: true, message: '请选择预约日期范围', trigger: 'change' }]
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
    isAdmin() {
      return this.$store.state.user && this.$store.state.user.name === 'admin'
    },
    roomGroups() {
      const groups = this.roomList.map(room => {
        const bookings = this.dayBookings
          .filter(b => b.roomId === room.roomId)
          .sort((a, b) => {
            const aStart = this.parseTime(a.startTime)
            const bStart = this.parseTime(b.startTime)
            return aStart - bStart
          })
        return {
          roomId: room.roomId,
          roomName: room.roomName,
          location: room.location,
          capacity: room.capacity,
          equipment: room.equipment,
          bookings: bookings
        }
      })
      return groups
    },
    totalBookings() {
      return this.dayBookings.length
    },
    pendingCount() {
      return this.dayBookings.filter(b => b.bookingStatus === '0').length
    },
    approvedCount() {
      return this.dayBookings.filter(b => b.bookingStatus === '1').length
    }
  },
  watch: {
    calendarDate(newVal) {
      const dateStr = this.formatDateString(newVal)
      if (dateStr !== this.selectedDate) {
        this.selectedDate = dateStr
        this.loadDayBookings()
      } else {
        this.loadMonthBookings()
      }
    },
    'bookForm.startTime'(val) {
      if (val) {
        this.endTimeOptions.minTime = val
      }
    }
  },
  created() {
    this.getRoomList()
    this.loadDayBookings()
  },
  methods: {
    getRoomList() {
      listAvailableRoom().then(response => {
        this.roomList = response.data || []
      })
    },
    loadDayBookings() {
      this.loading = true
      getRoomCalendar(this.selectedDate).then(response => {
        this.allBookings = response.data || []
        this.dayBookings = this.allBookings
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
      this.loadMonthBookings()
    },
    loadMonthBookings() {
      const d = new Date(this.calendarDate)
      const year = d.getFullYear()
      const month = d.getMonth()
      const firstDay = new Date(year, month, 1)
      const lastDay = new Date(year, month + 1, 0)
      const startDate = this.formatDateString(firstDay)
      const endDate = this.formatDateString(lastDay)
      getRoomCalendarRange(startDate, endDate).then(response => {
        this.monthBookings = response.data || []
      })
    },
    hasBookingOnDate(day) {
      return this.monthBookings.filter(b => this.formatDateString(b.bookingDate) === day).length > 0
    },
    getBookingCount(day) {
      return this.monthBookings.filter(b => {
        return this.formatDateString(b.bookingDate) === day
      }).length
    },
    handleDateClick(day) {
      this.selectedDate = day
      this.loadDayBookings()
    },
    handleViewDetail(booking) {
      this.detailForm = booking
      this.detailOpen = true
    },
    handleEditFromDetail() {
      this.detailOpen = false
      this.bookForm = {
        roomId: this.detailForm.roomId,
        roomName: this.detailForm.roomName,
        bookingDate: this.formatDateString(this.detailForm.bookingDate),
        bookingType: this.detailForm.bookingType || 'hour',
        dateRange: null,
        bookingTitle: this.detailForm.bookingTitle,
        startTime: this.formatTime(this.detailForm.startTime),
        endTime: this.formatTime(this.detailForm.endTime),
        attendees: this.detailForm.attendees,
        contactPhone: this.detailForm.contactPhone,
        meetingLink: this.detailForm.meetingLink,
        meetingPassword: this.detailForm.meetingPassword,
        remark: this.detailForm.remark,
        bookingId: this.detailForm.bookingId
      }
      this.bookOpen = true
      this.$nextTick(() => {
        this.$refs.bookForm && this.$refs.bookForm.clearValidate()
      })
    },
    handleCancelFromDetail() {
      this.$modal.confirm('是否确认取消该预约？').then(() => {
        return cancelBooking({ bookingId: this.detailForm.bookingId })
      }).then(() => {
        this.detailOpen = false
        this.loadDayBookings()
        this.$modal.msgSuccess("取消成功")
      }).catch(() => {})
    },
    statusType(status) {
      const map = { '0': 'warning', '1': 'success', '2': 'danger', '3': 'info' }
      return map[status] || 'info'
    },
    statusText(status) {
      const map = { '0': '待审核', '1': '已通过', '2': '已拒绝', '3': '已取消' }
      return map[status] || '未知'
    },
    formatDateString(date) {
      if (!date) return ''
      const d = typeof date === 'string' ? new Date(date) : date
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    formatDate(date) {
      if (!date) return ''
      return this.formatDateString(date)
    },
    formatTime(time) {
      if (!time) return ''
      if (typeof time === 'string') {
        return time.substring(0, 5)
      }
      const d = new Date(time)
      const h = String(d.getHours()).padStart(2, '0')
      const m = String(d.getMinutes()).padStart(2, '0')
      return `${h}:${m}`
    },
    parseTime(time) {
      if (!time) return 0
      if (typeof time === 'string') {
        const parts = time.split(':')
        return parseInt(parts[0]) * 60 + parseInt(parts[1])
      }
      const d = new Date(time)
      return d.getHours() * 60 + d.getMinutes()
    },
    handleBookRoom(group) {
      this.bookForm = {
        roomId: group.roomId,
        roomName: group.roomName,
        bookingDate: this.selectedDate,
        bookingType: 'hour',
        dateRange: null,
        bookingTitle: '',
        startTime: '',
        endTime: '',
        attendees: 1,
        contactPhone: '',
        remark: ''
      }
      this.bookOpen = true
      this.$nextTick(() => {
        this.$refs.bookForm && this.$refs.bookForm.clearValidate()
      })
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
          this.$message.error('结束时间必须大于开始时间')
          return
        }
        if (this.bookForm.bookingType === 'day') {
          if (!this.bookForm.dateRange || this.bookForm.dateRange.length < 2) {
            this.$message.error('请选择预约日期范围')
            return
          }
          this.bookForm.bookingDate = this.bookForm.dateRange[0]
          this.bookForm.endDate = this.bookForm.dateRange[1]
        }
        this.bookLoading = true
        const request = this.bookForm.bookingId ? updateBooking : addBooking
        request(this.bookForm).then(response => {
          this.$message.success(this.bookForm.bookingId ? '修改成功' : '预约成功')
          this.bookOpen = false
          this.loadDayBookings()
        }).catch(() => {
          this.bookLoading = false
        })
      })
    }
  }
}
</script>

<style scoped>
.calendar-layout {
  display: flex;
  gap: 16px;
  min-height: calc(100vh - 140px);
}

.calendar-left {
  flex: 0 0 33.3333%;
  max-width: 33.3333%;
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.calendar-header {
  display: flex;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #ebeef5;
}

.calendar-header .title {
  margin-left: 8px;
}

.calendar-header i {
  color: #409eff;
  font-size: 20px;
}

.calendar-right {
  flex: 1;
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  overflow-y: auto;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 1px solid #ebeef5;
  flex-wrap: wrap;
  gap: 12px;
}

.detail-title {
  display: flex;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.detail-title i {
  color: #409eff;
  margin-right: 8px;
  font-size: 20px;
}

.detail-summary {
  display: flex;
  gap: 8px;
}

::v-deep .el-calendar__body {
  padding: 0;
}

::v-deep .el-calendar-day {
  height: 80px;
  padding: 0;
}

.calendar-cell {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background-color 0.2s;
  padding: 4px;
}

.calendar-cell:hover {
  background-color: #f5f7fa;
}

.cell-day {
  font-size: 16px;
  color: #303133;
  font-weight: 500;
}

.calendar-cell.has-booking .cell-day {
  color: #409eff;
  font-weight: 600;
}

.booking-badge {
  margin-top: 4px;
  padding: 1px 6px;
  font-size: 11px;
  background: #409eff;
  color: #fff;
  border-radius: 10px;
  line-height: 1.4;
}

::v-deep .el-calendar-table td.is-selected .calendar-cell {
  background-color: #ecf5ff;
}

::v-deep .el-calendar-table td.is-today .cell-day {
  color: #409eff;
  font-weight: 700;
}

.room-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.room-card {
  border: 1px solid #ebeef5;
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s;
}

.room-card:hover {
  box-shadow: 0 4px 12px 0 rgba(0, 0, 0, 0.08);
  border-color: #c6e2ff;
}

.room-info {
  padding: 16px 20px;
  background: linear-gradient(135deg, #f8fbff 0%, #f0f7ff 100%);
  border-bottom: 1px solid #ebeef5;
}

.room-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
  gap: 8px;
}

.room-name {
  display: flex;
  align-items: center;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.room-name i {
  color: #409eff;
  margin-right: 8px;
  font-size: 18px;
}

.room-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  font-size: 13px;
  color: #606266;
}

.meta-item {
  display: flex;
  align-items: center;
}

.meta-item i {
  margin-right: 4px;
  color: #909399;
}

.booking-list {
  padding: 12px 20px;
  min-height: 60px;
}

.booking-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 12px 16px;
  margin-bottom: 8px;
  border-radius: 6px;
  background: #fafafa;
  border-left: 3px solid #909399;
  cursor: pointer;
  transition: all 0.2s;
}

.booking-item:last-child {
  margin-bottom: 0;
}

.booking-item:hover {
  background: #f0f7ff;
  transform: translateX(2px);
}

.booking-item.status-0 {
  border-left-color: #e6a23c;
  background: #fdf6ec;
}

.booking-item.status-1 {
  border-left-color: #67c23a;
  background: #f0f9eb;
}

.booking-item.status-2 {
  border-left-color: #f56c6c;
  background: #fef0f0;
}

.booking-item.status-3 {
  border-left-color: #909399;
  background: #f4f4f5;
}

.booking-item.is-fixed {
  border-left-color: #f56c6c;
  background: #fef0f0;
}

.booking-time {
  display: flex;
  align-items: center;
  min-width: 140px;
  font-weight: 600;
  color: #303133;
}

.booking-time i {
  margin-right: 6px;
  color: #409eff;
}

.booking-content {
  flex: 1;
  min-width: 0;
}

.booking-title {
  font-size: 14px;
  color: #303133;
  font-weight: 500;
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.booking-user {
  display: flex;
  align-items: center;
  font-size: 12px;
  color: #909399;
}

.booking-user i {
  margin-right: 4px;
}

.booking-dept {
  color: #c0c4cc;
}

@media (max-width: 1100px) {
  .calendar-layout {
    flex-direction: column;
  }
  .calendar-left {
    flex: 1;
    max-width: 100%;
  }
}
</style>
