<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="120px">
      <el-form-item :label="$t('fixedBooking.subject')" prop="bookingTitle">
        <el-input v-model="queryParams.bookingTitle" :placeholder="$t('fixedBooking.placeholder.subject')" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="$t('fixedBooking.recurrenceType')" prop="recurrenceType">
        <el-select v-model="queryParams.recurrenceType" :placeholder="$t('fixedBooking.all')" clearable size="small">
          <el-option :label="$t('fixedBooking.daily')" value="daily" />
          <el-option :label="$t('fixedBooking.weekly')" value="weekly" />
          <el-option :label="$t('fixedBooking.monthly')" value="monthly" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('fixedBooking.status')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('fixedBooking.all')" clearable size="small">
          <el-option :label="$t('fixedBooking.enabled')" value="0" />
          <el-option :label="$t('fixedBooking.disabled')" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{$t('fixedBooking.search')}}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('fixedBooking.reset')}}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['mtg:fixedBooking:add']">{{$t('fixedBooking.add')}}</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="fixedList" border stripe>
      <el-table-column :label="$t('fixedBooking.subject')" prop="bookingTitle" min-width="120" show-overflow-tooltip />
      <el-table-column :label="$t('fixedBooking.timeRange')" min-width="120">
        <template slot-scope="scope">
          {{ scope.row.startTime }} - {{ scope.row.endTime }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('fixedBooking.recurrenceType')" min-width="80" align="center">
        <template slot-scope="scope">
          {{ recurrenceText(scope.row.recurrenceType) }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('fixedBooking.recurrenceDay')" min-width="100" align="center">
        <template slot-scope="scope">
          {{ recurrenceDayText(scope.row) }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('fixedBooking.effectPeriod')" min-width="180">
        <template slot-scope="scope">
          {{ scope.row.startDate && scope.row.startDate.substring(0, 10) }} ~ {{ scope.row.endDate && scope.row.endDate.substring(0, 10) }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('fixedBooking.room')" min-width="120">
        <template slot-scope="scope">
          <span v-for="(name, idx) in ($i18n.locale === 'en' && scope.row.roomNamesEn && scope.row.roomNamesEn.length ? scope.row.roomNamesEn : scope.row.roomNames)" :key="idx">
            {{ idx > 0 ? '、' : '' }}{{ name }}
          </span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('fixedBooking.booker')" prop="bookerName" min-width="80" />
      <el-table-column :label="$t('fixedBooking.status')" width="80" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'" size="small">
            {{ scope.row.status === '0' ? $t('fixedBooking.enabled') : $t('fixedBooking.disabled') }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('fixedBooking.action')" width="180" align="center" fixed="right">
        <template slot-scope="scope">
          <div style="display: flex; justify-content: center; gap: 4px;">
            <el-button type="primary" link size="small" @click="handleEdit(scope.row)" v-hasPermi="['mtg:fixedBooking:edit']">{{$t('fixedBooking.edit')}}</el-button>
            <el-button :type="scope.row.status === '0' ? 'warning' : 'success'" link size="small" @click="handleToggleStatus(scope.row)" v-hasPermi="['mtg:fixedBooking:edit']">
              {{ scope.row.status === '0' ? $t('fixedBooking.disable') : $t('fixedBooking.enable') }}
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="dialogTitle" :visible.sync="dialogOpen" width="650px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-form-item :label="$t('fixedBooking.subject')" prop="bookingTitle">
          <el-input v-model="form.bookingTitle" :placeholder="$t('fixedBooking.placeholder.subject')" maxlength="50" />
        </el-form-item>
        <el-form-item :label="$t('fixedBooking.startTime')" prop="startTime">
          <el-time-select v-model="form.startTime" :picker-options="startTimeOptions" :placeholder="$t('fixedBooking.placeholder.startTime')" />
        </el-form-item>
        <el-form-item :label="$t('fixedBooking.endTime')" prop="endTime">
          <el-time-select v-model="form.endTime" :picker-options="endTimeOptions" :placeholder="$t('fixedBooking.placeholder.endTime')" />
        </el-form-item>
        <el-form-item :label="$t('fixedBooking.recurrenceType')" prop="recurrenceType">
          <el-radio-group v-model="form.recurrenceType" @change="handleTypeChange">
            <el-radio label="daily">{{ $t('fixedBooking.daily') }}</el-radio>
            <el-radio label="weekly">{{ $t('fixedBooking.weekly') }}</el-radio>
            <el-radio label="monthly">{{ $t('fixedBooking.monthly') }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="form.recurrenceType === 'weekly'" :label="$t('fixedBooking.weeklyDay')" prop="recurrenceDay">
          <el-select v-model="form.recurrenceDay" :placeholder="$t('fixedBooking.placeholder.select')">
            <el-option :value="1" :label="$t('fixedBooking.mon')" />
            <el-option :value="2" :label="$t('fixedBooking.tue')" />
            <el-option :value="3" :label="$t('fixedBooking.wed')" />
            <el-option :value="4" :label="$t('fixedBooking.thu')" />
            <el-option :value="5" :label="$t('fixedBooking.fri')" />
            <el-option :value="6" :label="$t('fixedBooking.sat')" />
            <el-option :value="7" :label="$t('fixedBooking.sun')" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="form.recurrenceType === 'monthly'" :label="$t('fixedBooking.monthlyDay')" prop="recurrenceDay">
          <el-input-number v-model="form.recurrenceDay" :min="1" :max="31" />
        </el-form-item>
        <el-form-item :label="$t('fixedBooking.effectDate')" prop="dateRange">
          <el-date-picker v-model="form.dateRange" type="daterange" range-separator="至" start-placeholder="開始日期" end-placeholder="結束日期" value-format="yyyy-MM-dd" :picker-options="dateRangeOptions" style="width: 100%" />
        </el-form-item>
        <el-form-item :label="$t('fixedBooking.room')" prop="roomIds">
          <el-select v-model="form.roomIds" multiple :placeholder="$t('fixedBooking.placeholder.room')" style="width: 100%">
            <el-option v-for="room in roomList" :key="room.roomId" :label="room.roomName" :value="room.roomId" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('fixedBooking.attendees')" prop="attendees">
          <el-input-number v-model="form.attendees" :min="1" :max="100" />
        </el-form-item>
        <el-form-item :label="$t('fixedBooking.contactPhone')" prop="contactPhone">
          <el-input v-model="form.contactPhone" :placeholder="$t('fixedBooking.placeholder.phone')" maxlength="11" />
        </el-form-item>
        <el-form-item :label="$t('fixedBooking.meetingLink')">
          <el-input v-model="form.meetingLink" :placeholder="$t('fixedBooking.placeholder.link')" />
        </el-form-item>
        <el-form-item :label="$t('fixedBooking.meetingPassword')">
          <el-input v-model="form.meetingPassword" :placeholder="$t('fixedBooking.placeholder.password')" />
        </el-form-item>
        <el-form-item :label="$t('fixedBooking.serviceItems')">
<el-select v-model="form.serviceItemsList" multiple :placeholder="$t('fixedBooking.serviceSelectPlaceholder')" clearable style="width: 100%">
              <el-option v-for="item in serviceOptions.filter(d => d)" :key="item.serviceId" :label="capitalize($i18n.locale === 'en' && item.serviceNameEn ? item.serviceNameEn : (item.serviceName || ''))" :value="item.serviceName" />
            </el-select>
        </el-form-item>
        <el-form-item :label="$t('fixedBooking.remark')">
          <el-input v-model="form.remark" type="textarea" :placeholder="$t('fixedBooking.placeholder.remark')" :rows="3" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogOpen = false">{{ $t('fixedBooking.cancel') }}</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitForm">{{ $t('fixedBooking.confirm') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listFixedBooking, getFixedBooking, addFixedBooking, updateFixedBooking, delFixedBooking } from "@/api/mtg/fixedBooking"
import { getAvailableRooms } from "@/api/mtg/room"
import { listEnabledService } from "@/api/mtg/service"

export default {
  name: "MtgFixedBooking",
  data() {
    return {
      loading: false,
      submitLoading: false,
      fixedList: [],
      total: 0,
      roomList: [],
      serviceOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        bookingTitle: null,
        recurrenceType: null,
        status: null
      },
      dialogOpen: false,
      dialogTitle: '',
      form: {},
      rules: {
        bookingTitle: [{ required: true, message: this.$t('fixedBooking.validation.subject'), trigger: 'blur' }],
        startTime: [{ required: true, message: this.$t('fixedBooking.validation.startTime'), trigger: 'change' }],
        endTime: [{ required: true, message: this.$t('fixedBooking.validation.endTime'), trigger: 'change' }],
        recurrenceType: [{ required: true, message: this.$t('fixedBooking.validation.recurrence'), trigger: 'change' }],
        dateRange: [{ required: true, message: this.$t('fixedBooking.validation.dateRange'), trigger: 'change' }],
        roomIds: [{ required: true, message: this.$t('fixedBooking.validation.room'), trigger: 'change' }],
        attendees: [{ required: true, message: this.$t('fixedBooking.validation.attendees'), trigger: 'blur' }],
        contactPhone: [{ required: true, message: this.$t('fixedBooking.validation.phone'), trigger: 'blur' }]
      },
      startTimeOptions: { start: '08:00', end: '22:00', step: '00:30' },
      endTimeOptions: { start: '08:00', end: '22:00', step: '00:30', minTime: '' },
      dateRangeOptions: {
        disabledDate(date) {
          return date && date < new Date(new Date().setHours(0, 0, 0, 0))
        }
      }
    }
  },
  watch: {
    'form.startTime'(val) {
      if (val) this.endTimeOptions.minTime = val
    }
  },
  created() {
    this.getRoomList()
    this.getServiceOptions()
    this.getList()
  },
  methods: {
    capitalize(str) {
      return str ? str.replace(/\b\w/g, c => c.toUpperCase()) : ''
    },
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
      listFixedBooking(this.queryParams).then(res => {
        this.fixedList = res.rows || []
        this.total = res.total || 0
        this.loading = false
      }).catch(() => { this.loading = false })
    },
    loadRoomNames() {
      const roomMap = {}
      const roomMapEn = {}
      this.roomList.forEach(r => {
        roomMap[r.roomId] = r.roomName
        roomMapEn[r.roomId] = r.roomNameEn
      })
      this.fixedList.forEach(f => {
        f.roomNames = (f.roomIds || []).map(id => roomMap[id] || '').filter(Boolean)
        f.roomNamesEn = (f.roomIds || []).map(id => roomMapEn[id] || '').filter(Boolean)
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.queryParams = { pageNum: 1, pageSize: 20, bookingTitle: null, recurrenceType: null, status: null }
      this.handleQuery()
    },
    handleAdd() {
      this.dialogTitle = this.$t('fixedBooking.addTitle')
      this.form = {
        bookingTitle: '',
        startTime: '',
        endTime: '',
        recurrenceType: 'daily',
        recurrenceDay: null,
        dateRange: null,
        roomIds: [],
        attendees: 1,
        contactPhone: '',
        meetingLink: '',
        meetingPassword: '',
        serviceItemsList: [],
        remark: ''
      }
      this.dialogOpen = true
      this.$nextTick(() => { this.$refs.form && this.$refs.form.clearValidate() })
    },
    handleEdit(row) {
      this.dialogTitle = this.$t('fixedBooking.editTitle')
      getFixedBooking(row.fixedId).then(res => {
        const data = res.data
        this.form = {
          fixedId: data.fixedId,
          bookingTitle: data.bookingTitle,
          startTime: data.startTime,
          endTime: data.endTime,
          recurrenceType: data.recurrenceType,
          recurrenceDay: data.recurrenceDay,
          dateRange: [data.startDate ? data.startDate.substring(0, 10) : '', data.endDate ? data.endDate.substring(0, 10) : ''],
          roomIds: data.roomIds || [],
          attendees: data.attendees,
          contactPhone: data.contactPhone,
          meetingLink: data.meetingLink,
          meetingPassword: data.meetingPassword,
          serviceItemsList: data.serviceItems ? data.serviceItems.split(',') : [],
          remark: data.remark
        }
        this.dialogOpen = true
        this.$nextTick(() => { this.$refs.form && this.$refs.form.clearValidate() })
      })
    },
    handleTypeChange() {
      this.form.recurrenceDay = null
    },
    handleToggleStatus(row) {
      const newStatus = row.status === '0' ? '1' : '0'
      const confirmMsg = newStatus === '1' ? this.$t('fixedBooking.confirmDisable') : this.$t('fixedBooking.confirmEnable')
      const statusText = newStatus === '1' ? this.$t('fixedBooking.disabled') : this.$t('fixedBooking.enabled')
      this.$modal.confirm(confirmMsg).then(() => {
        return updateFixedBooking({ fixedId: row.fixedId, status: newStatus })
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(statusText + '成功')
      }).catch(() => {})
    },
    handleDelete(row) {
      this.$modal.confirm(this.$t('fixedBooking.confirmDelete'), this.$t('fixedBooking.warning'), {
        confirmButtonText: this.$t('fixedBooking.confirm'),
        cancelButtonText: this.$t('fixedBooking.cancel'),
        type: 'warning'
      }).then(() => {
        return delFixedBooking(row.fixedId)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('fixedBooking.deleted'))
      }).catch(() => {})
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        if (this.form.startTime >= this.form.endTime) {
          this.$message.error(this.$t('fixedBooking.timeError'))
          return
        }
        if (!this.form.dateRange || this.form.dateRange.length < 2) {
          this.$message.error(this.$t('fixedBooking.dateRangeError'))
          return
        }
        const payload = {
          fixedId: this.form.fixedId || undefined,
          bookingTitle: this.form.bookingTitle,
          startTime: this.form.startTime,
          endTime: this.form.endTime,
          recurrenceType: this.form.recurrenceType,
          recurrenceDay: this.form.recurrenceDay,
          startDate: this.form.dateRange[0],
          endDate: this.form.dateRange[1],
          roomIds: this.form.roomIds,
          attendees: this.form.attendees,
          contactPhone: this.form.contactPhone,
          meetingLink: this.form.meetingLink,
          meetingPassword: this.form.meetingPassword,
          serviceItems: Array.isArray(this.form.serviceItemsList) ? this.form.serviceItemsList.join(',') : '',
          status: '0',
          remark: this.form.remark
        }
        this.submitLoading = true
        const request = payload.fixedId ? updateFixedBooking : addFixedBooking
        request(payload).then(() => {
          this.$message.success(payload.fixedId ? this.$t('fixedBooking.edited') : this.$t('fixedBooking.added'))
          this.dialogOpen = false
          this.getList()
        }).catch(() => { this.submitLoading = false })
      })
    },
    recurrenceText(type) {
      return { 'daily': this.$t('fixedBooking.daily'), 'weekly': this.$t('fixedBooking.weekly'), 'monthly': this.$t('fixedBooking.monthly') }[type] || type
    },
    recurrenceDayText(row) {
      if (row.recurrenceType === 'daily') return this.$t('fixedBooking.noRepeat')
      if (row.recurrenceType === 'weekly') {
        return { 1: this.$t('fixedBooking.mon'), 2: this.$t('fixedBooking.tue'), 3: this.$t('fixedBooking.wed'), 4: this.$t('fixedBooking.thu'), 5: this.$t('fixedBooking.fri'), 6: this.$t('fixedBooking.sat'), 7: this.$t('fixedBooking.sun') }[row.recurrenceDay] || this.$t('fixedBooking.noRepeat')
      }
      if (row.recurrenceType === 'monthly') {
        return row.recurrenceDay ? this.$t('fixedBooking.dayOfMonth', { day: row.recurrenceDay }) : this.$t('fixedBooking.noRepeat')
      }
      return this.$t('fixedBooking.noRepeat')
    }
  }
}
</script>
