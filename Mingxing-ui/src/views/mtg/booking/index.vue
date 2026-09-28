<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="120px">
      <el-form-item :label="$t('booking.room')" prop="roomId">
        <el-select v-model="queryParams.roomId" :placeholder="$t('booking.allRooms')" clearable size="small">
          <el-option v-for="room in roomList" :key="room.roomId" :label="$i18n.locale === 'en' && room.roomNameEn ? room.roomNameEn : (room.roomName || '')" :value="room.roomId" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('booking.booker')" prop="bookerName">
        <el-input v-model="queryParams.bookerName" :placeholder="$t('booking.searchPlaceholder')" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="$t('booking.date')">
        <el-date-picker v-model="dateRange" type="daterange" range-separator="至" start-placeholder="開始日期" end-placeholder="結束日期" value-format="yyyy-MM-dd" size="small" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('booking.search') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('booking.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="bookingList" border stripe>
      <el-table-column :label="$t('booking.dept')" min-width="120">
        <template slot-scope="scope">
          {{ $i18n.locale === 'en' && scope.row.deptNameEn ? scope.row.deptNameEn : scope.row.deptName }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('booking.booker')" prop="bookerName" min-width="100" />
      <el-table-column :label="$t('booking.applyDate')" prop="bookingDate" min-width="110">
        <template slot-scope="scope">
          {{ formatDate(scope.row.bookingDate) }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('booking.time')" min-width="140">
        <template slot-scope="scope">
          {{ formatTime(scope.row.startTime) }} - {{ formatTime(scope.row.endTime) }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('booking.room')" min-width="120">
        <template slot-scope="scope">
          {{ $i18n.locale === 'en' && scope.row.roomNameEn ? scope.row.roomNameEn : scope.row.roomName }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('booking.duration')" min-width="90" align="center">
        <template slot-scope="scope">
          {{ calcDuration(scope.row.startTime, scope.row.endTime) }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('booking.action')" width="150" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button v-if="scope.row.bookerId === currentUserId || isAdmin" type="primary" link size="small" @click="handleEdit(scope.row)">{{ $t('booking.edit') }}</el-button>
          <el-button type="info" link size="small" @click="handleDetail(scope.row)">{{ $t('booking.detail') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="$t('booking.detailTitle')" :visible.sync="detailOpen" width="600px" append-to-body :close-on-click-modal="false">
      <el-descriptions v-if="detailForm.bookingId" :column="2" border>
        <el-descriptions-item :label="$t('booking.room')">{{ $i18n.locale === 'en' && detailForm.roomNameEn ? detailForm.roomNameEn : (detailForm.roomName || '') }}</el-descriptions-item>
        <el-descriptions-item :label="$t('booking.bookingType')">{{ detailForm.bookingType === 'day' ? $t('booking.daily') : $t('booking.hourly') }}</el-descriptions-item>
        <el-descriptions-item :label="$t('booking.bookingDate')">{{ formatDate(detailForm.bookingDate) }}</el-descriptions-item>
        <el-descriptions-item :label="$t('booking.bookingPeriod')">{{ formatTime(detailForm.startTime) }} - {{ formatTime(detailForm.endTime) }}</el-descriptions-item>
        <el-descriptions-item :label="$t('booking.duration')">{{ calcDuration(detailForm.startTime, detailForm.endTime) }}</el-descriptions-item>
        <el-descriptions-item :label="$t('booking.bookerLabel')">{{ detailForm.bookerName }}</el-descriptions-item>
        <el-descriptions-item :label="$t('booking.dept')">{{ $i18n.locale === 'en' && detailForm.deptNameEn ? detailForm.deptNameEn : (detailForm.deptName || '') }}</el-descriptions-item>
        <el-descriptions-item :label="$t('booking.attendees')">{{ detailForm.attendees }}</el-descriptions-item>
        <el-descriptions-item :label="$t('booking.contactPhone')">{{ detailForm.contactPhone }}</el-descriptions-item>
        <el-descriptions-item v-if="detailForm.meetingLink" :label="$t('booking.meetingLink')">
          <a :href="detailForm.meetingLink" target="_blank" style="color: #409eff">{{ detailForm.meetingLink }}</a>
        </el-descriptions-item>
        <el-descriptions-item v-if="detailForm.meetingPassword" :label="$t('booking.meetingPassword')">{{ detailForm.meetingPassword }}</el-descriptions-item>
        <el-descriptions-item v-if="detailForm.serviceItems" :label="$t('booking.serviceItems')" :span="2">{{ translateServiceItems(detailForm.serviceItems) }}</el-descriptions-item>
        <el-descriptions-item v-if="detailForm.remark" :label="$t('booking.remark')" :span="2">{{ detailForm.remark }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button v-if="isOwnBooking || isAdmin" type="primary" @click="handleEdit(detailForm)">{{ $t('booking.edit') }}</el-button>
        <el-button v-if="isOwnBooking || isAdmin" type="danger" @click="handleDelete(detailForm)">{{ $t('booking.delete') }}</el-button>
        <el-button @click="detailOpen = false">{{ $t('booking.close') }}</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="$t('booking.editTitle')" :visible.sync="editOpen" width="550px" append-to-body :close-on-click-modal="false">
      <el-form ref="editForm" :model="editForm" :rules="editRules" label-width="150px">
        <el-form-item :label="$t('booking.room')">
          <el-input v-model="editForm.roomName" disabled />
        </el-form-item>
        <el-form-item :label="$t('booking.subject')" prop="subject">
          <el-input v-model="editForm.subject" :placeholder="$t('booking.placeholder.subject')" maxlength="50" />
        </el-form-item>
        <el-form-item :label="$t('booking.startTime')" prop="startTime">
          <el-time-select v-model="editForm.startTime" :picker-options="startTimeOptions" :placeholder="$t('booking.placeholder.startTime')" />
        </el-form-item>
        <el-form-item :label="$t('booking.endTime')" prop="endTime">
          <el-time-select v-model="editForm.endTime" :picker-options="endTimeOptions" :placeholder="$t('booking.placeholder.endTime')" />
        </el-form-item>
        <el-form-item :label="$t('booking.attendees')" prop="attendees">
          <el-input-number v-model="editForm.attendees" :min="1" :max="100" />
        </el-form-item>
        <el-form-item :label="$t('booking.contactPhone')" prop="contactPhone">
          <el-input v-model="editForm.contactPhone" :placeholder="$t('booking.placeholder.phone')" maxlength="11" />
        </el-form-item>
        <el-form-item :label="$t('booking.meetingLink')">
          <el-input v-model="editForm.meetingLink" :placeholder="$t('booking.placeholder.link')" />
        </el-form-item>
        <el-form-item :label="$t('booking.meetingPassword')">
          <el-input v-model="editForm.meetingPassword" :placeholder="$t('booking.placeholder.password')" />
        </el-form-item>
        <el-form-item :label="$t('booking.serviceItems')">
          <el-select v-model="editForm.serviceItemsList" multiple :placeholder="$t('booking.serviceSelectPlaceholder')" clearable style="width: 100%">
            <el-option v-for="item in serviceOptions.filter(d => d)" :key="item.serviceId" :label="capitalize($i18n.locale === 'en' && item.serviceNameEn ? item.serviceNameEn : (item.serviceName || ''))" :value="item.serviceName" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('booking.remark')">
          <el-input v-model="editForm.remark" type="textarea" :placeholder="$t('booking.placeholder.remark')" :rows="3" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="editOpen = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" :loading="editLoading" @click="submitEdit">{{ $t('booking.save') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listBooking, updateBooking, deleteBooking } from "@/api/mtg/booking"
import { getAvailableRooms } from "@/api/mtg/room"
import { listEnabledService } from "@/api/mtg/service"
import { checkPermi } from "@/utils/permission"

export default {
  name: "MtgBookingManage",
  data() {
    return {
      loading: false,
      bookingList: [],
      total: 0,
      roomList: [],
      serviceOptions: [],
      dateRange: [],
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        roomId: null,
        bookerName: null
      },
      detailOpen: false,
      detailForm: {},
      editOpen: false,
      editLoading: false,
      editForm: {},
      editRules: {
        subject: [{ required: true, message: this.$t('booking.placeholder.subject'), trigger: 'blur' }],
        startTime: [{ required: true, message: this.$t('booking.placeholder.startTime'), trigger: 'change' }],
        endTime: [{ required: true, message: this.$t('booking.placeholder.endTime'), trigger: 'change' }],
        attendees: [{ required: true, message: this.$t('booking.attendees'), trigger: 'blur' }],
        contactPhone: [{ required: true, message: this.$t('booking.placeholder.phone'), trigger: 'blur' }]
      },
      startTimeOptions: { start: '08:00', end: '20:00', step: '00:30' },
      endTimeOptions: { start: '08:00', end: '20:00', step: '00:30', minTime: '' }
    }
  },
  watch: {
    'editForm.startTime'(val) {
      if (val) this.endTimeOptions.minTime = val
    }
  },
  computed: {
    currentUserId() {
      return this.$store.state.user && this.$store.state.user.id
    },
    isOwnBooking() {
      if (!this.detailForm || !this.detailForm.bookerId) return false
      return this.detailForm.bookerId === this.currentUserId
    },
    isAdmin() {
      return checkPermi(['mtg:booking:edit', 'mtg:booking:remove'])
    }
  },
  created() {
    this.getRoomList()
    this.getServiceOptions()
    this.getList()
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
    getList() {
      this.loading = true
      const params = { ...this.queryParams }
      if (this.dateRange && this.dateRange.length === 2) {
        params['params[beginTime]'] = this.dateRange[0]
        params['params[endTime]'] = this.dateRange[1]
      }
      listBooking(params).then(res => {
        this.bookingList = res.rows || []
        this.total = res.total || 0
        this.loading = false
      }).catch(() => { this.loading = false })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.dateRange = []
      this.queryParams = { pageNum: 1, pageSize: 20, roomId: null, bookerName: null }
      this.handleQuery()
    },
    handleDetail(row) {
      this.detailForm = row
      this.detailOpen = true
    },
    handleEdit(row) {
      this.detailOpen = false
      this.detailForm = row
      const startTime = this.formatTime(row.startTime)
      const endTime = this.formatTime(row.endTime)
      this.editForm = {
        bookingId: row.bookingId,
        roomId: row.roomId,
        roomName: row.roomName,
        subject: row.subject,
        bookingDate: this.formatDate(row.bookingDate),
        bookingType: row.bookingType,
        startTime: startTime,
        endTime: endTime,
        attendees: row.attendees,
        contactPhone: row.contactPhone,
        meetingLink: row.meetingLink,
        meetingPassword: row.meetingPassword,
        serviceItemsList: row.serviceItems ? row.serviceItems.split(',') : [],
        remark: row.remark
      }
      this.editOpen = true
      this.$nextTick(() => { this.$refs.editForm && this.$refs.editForm.clearValidate() })
    },
    submitEdit() {
      this.$refs.editForm.validate(valid => {
        if (!valid) return
        if (this.editForm.startTime >= this.editForm.endTime) {
          this.$message.error(this.$t('booking.timeError'))
          return
        }
        const bookingDateStr = this.editForm.bookingDate
        const payload = {
          bookingId: this.editForm.bookingId,
          roomId: this.editForm.roomId,
          subject: this.editForm.subject,
          bookingDate: bookingDateStr,
          bookingType: this.editForm.bookingType,
          startTime: bookingDateStr + ' ' + this.editForm.startTime + ':00',
          endTime: bookingDateStr + ' ' + this.editForm.endTime + ':00',
          attendees: this.editForm.attendees,
          contactPhone: this.editForm.contactPhone,
          meetingLink: this.editForm.meetingLink,
          meetingPassword: this.editForm.meetingPassword,
          serviceItems: Array.isArray(this.editForm.serviceItemsList) ? this.editForm.serviceItemsList.join(',') : '',
          remark: this.editForm.remark
        }
        this.editLoading = true
        updateBooking(payload).then(() => {
          this.$message.success(this.$t('booking.edited'))
          this.editOpen = false
          this.getList()
        }).catch(() => { this.editLoading = false })
      })
    },
    handleDelete(row) {
      this.$modal.confirm(this.$t('booking.confirmDelete'), this.$t('booking.warning'), {
        confirmButtonText: this.$t('common.confirm'),
        cancelButtonText: this.$t('common.cancel'),
        type: 'warning'
      }).then(() => {
        return deleteBooking(row.bookingId)
      }).then(() => {
        this.detailOpen = false
        this.$modal.msgSuccess(this.$t('booking.deleted'))
        this.getList()
      }).catch(() => {})
    },
    calcDuration(start, end) {
      if (!start || !end) return '-'
      const diff = new Date(end) - new Date(start)
      const hours = Math.floor(diff / 3600000)
      const mins = Math.floor((diff % 3600000) / 60000)
      if (hours > 0 && mins > 0) return hours + this.$t('booking.hours') + mins + this.$t('booking.mins')
      if (hours > 0) return hours + this.$t('booking.hours')
      return mins + this.$t('booking.mins')
    },
    formatDate(dateStr) {
      if (!dateStr) return ''
      return dateStr.substring(0, 10)
    },
    formatTime(dateStr) {
      if (!dateStr) return ''
      if (typeof dateStr === 'string' && dateStr.length === 5) return dateStr
      const d = new Date(dateStr)
      return String(d.getHours()).padStart(2, '0') + ':' + String(d.getMinutes()).padStart(2, '0')
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
    }
  }
}
</script>
