<template>
  <div class="app-container">
    <!-- 可出厂车辆 -->
    <el-card class="box-card" shadow="hover">
      <div slot="header" class="card-header">
        <span class="section-title">
          <i class="el-icon-s-order"></i>
          {{ $t('carDuty.availableTitle') }}
          <el-badge :value="availableList.length" :max="99" class="badge-item" />
        </span>
      </div>
      <div v-if="availableList.length === 0" class="empty-tip">{{ $t('carDuty.noAvailable') }}</div>
      <div v-else class="vehicle-grid">
        <div v-for="item in availableList" :key="item.applyId" class="vehicle-card available-card">
          <div class="vehicle-info">
            <div class="plate-tag">{{ item.plateNumber }}</div>
            <div class="info-row">
              <span class="label">{{ $t('carDuty.brand') }}:</span>
              <span class="value">{{ item.brand }} {{ item.color }}</span>
            </div>
            <div class="info-row">
              <span class="label">{{ $t('carDuty.applyNo') }}:</span>
              <span class="value tag-apply">{{ item.applyNo }}</span>
            </div>
            <div class="info-row" v-if="item.driverName">
              <span class="label">{{ $t('carDuty.driver') }}:</span>
              <span class="value">{{ item.driverName }}</span>
            </div>
            <div class="info-row" v-if="item.applicantName">
              <span class="label">{{ $t('carDuty.applicant') }}:</span>
              <span class="value">{{ item.applicantName }}</span>
            </div>
            <div class="info-row" v-if="item.auditBy">
              <span class="label">{{ $t('carDuty.auditor') }}:</span>
              <span class="value">{{ item.auditBy }}</span>
            </div>
          </div>
          <div class="vehicle-action">
            <el-button type="primary" size="small" icon="el-icon-s-promotion" @click="handleDepart(item)" v-hasPermi="['car:duty:operate']">
              {{ $t('carDuty.departBtn') }}
            </el-button>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 在外车辆 -->
    <el-card class="box-card" style="margin-top: 16px;" shadow="hover">
      <div slot="header" class="card-header">
        <span class="section-title">
          <i class="el-icon-truck"></i>
          {{ $t('carDuty.outTitle') }}
          <el-badge :value="outList.length" :max="99" class="badge-item" />
        </span>
      </div>
      <div v-if="outList.length === 0" class="empty-tip">{{ $t('carDuty.noOutVehicle') }}</div>
      <div v-else class="vehicle-grid">
        <div v-for="item in outList" :key="item.accessId" class="vehicle-card out-card">
          <div class="vehicle-info">
            <div class="plate-tag">{{ item.plateNumber }}</div>
            <div class="info-row">
              <span class="label">{{ $t('carDuty.driver') }}:</span>
              <span class="value">{{ item.driverName || '-' }}</span>
            </div>
            <div class="info-row" v-if="item.applyNo">
              <span class="label">{{ $t('carDuty.applyNo') }}:</span>
              <span class="value">{{ item.applyNo }}</span>
            </div>
            <div class="info-row" v-else>
              <span class="label">{{ $t('carDuty.applyNo') }}:</span>
              <span class="value tag-temp">{{ $t('carDuty.temporary') }}</span>
            </div>
            <div class="info-row" v-if="item.applicantName">
              <span class="label">{{ $t('carDuty.applicant') }}:</span>
              <span class="value">{{ item.applicantName }}</span>
            </div>
            <div class="info-row" v-if="item.auditBy">
              <span class="label">{{ $t('carDuty.auditor') }}:</span>
              <span class="value">{{ item.auditBy }}</span>
            </div>
            <div class="info-row">
              <span class="label">{{ $t('carDuty.outTime') }}:</span>
              <span class="value">{{ parseTime(item.outTime, '{y}-{m}-{d} {h}:{i}') }}</span>
            </div>
          </div>
          <div class="vehicle-action">
            <el-button type="success" size="small" icon="el-icon-check" @click="handleReturn(item)" v-hasPermi="['car:duty:operate']">
              {{ $t('carDuty.returnBtn') }}
            </el-button>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 出厂确认弹窗 -->
    <el-dialog :title="$t('carDuty.departConfirm')" :visible.sync="departDialogVisible" width="420px" append-to-body :close-on-click-modal="false">
      <div class="confirm-info">
        <p><b>{{ $t('carDuty.plateNumber') }}:</b> {{ departTarget.plateNumber }}</p>
        <p><b>{{ $t('carDuty.applyNo') }}:</b> {{ departTarget.applyNo }}</p>
        <p v-if="departTarget.driverName"><b>{{ $t('carDuty.driver') }}:</b> {{ departTarget.driverName }}</p>
      </div>
      <div slot="footer">
        <el-button @click="departDialogVisible = false">{{ $t('carDuty.cancel') }}</el-button>
        <el-button type="primary" @click="confirmDepart" :loading="submitLoading">{{ $t('carDuty.confirmDepart') }}</el-button>
      </div>
    </el-dialog>

    <!-- 回场确认弹窗 -->
    <el-dialog :title="$t('carDuty.returnConfirm')" :visible.sync="returnDialogVisible" width="420px" append-to-body :close-on-click-modal="false">
      <div class="confirm-info">
        <p><b>{{ $t('carDuty.plateNumber') }}:</b> {{ returnTarget.plateNumber }}</p>
        <p v-if="returnTarget.applyNo"><b>{{ $t('carDuty.applyNo') }}:</b> {{ returnTarget.applyNo }}</p>
        <p><b>{{ $t('carDuty.outTime') }}:</b> {{ parseTime(returnTarget.outTime, '{y}-{m}-{d} {h}:{i}') }}</p>
      </div>
      <div slot="footer">
        <el-button @click="returnDialogVisible = false">{{ $t('carDuty.cancel') }}</el-button>
        <el-button type="success" @click="confirmReturn" :loading="submitLoading">{{ $t('carDuty.confirmReturn') }}</el-button>
      </div>
    </el-dialog>

    <!-- 紧急出厂弹窗 -->
    <el-dialog :title="$t('carDuty.emergencyDepart')" :visible.sync="emergencyDialogVisible" width="420px" append-to-body :close-on-click-modal="false">
      <el-form :model="emergencyForm" label-width="120px">
        <el-form-item :label="$t('carDuty.selectVehicle')">
          <el-select v-model="emergencyForm.vehicleId" :placeholder="$t('carDuty.selectVehicle')" filterable style="width:100%">
            <el-option v-for="item in emergencyVehicleList" :key="item.vehicleId" :label="item.plateNumber" :value="item.vehicleId" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('carDuty.driverName')">
          <el-input v-model="emergencyForm.driverName" :placeholder="$t('carDuty.inputDriverName')" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="emergencyDialogVisible = false">{{ $t('carDuty.cancel') }}</el-button>
        <el-button type="danger" @click="confirmEmergency" :loading="submitLoading">{{ $t('carDuty.confirmEmergency') }}</el-button>
      </div>
    </el-dialog>

    <!-- 浮动紧急出厂按钮 -->
    <div class="fab-container" v-hasPermi="['car:duty:operate']">
      <div class="fab-btn" @click="handleEmergency">
        <i class="el-icon-warning"></i>
        <span>{{ $t('carDuty.emergencyDepart') }}</span>
      </div>
    </div>
  </div>
</template>

<script>
import { getOutVehicles, getAvailableVehicles, depart, returnVehicle, emergencyDepart, getDrivers, getEmergencyVehicles } from "@/api/car/duty"
import noticeSocket from "@/utils/noticeSocket"

export default {
  name: "CarDuty",
  data() {
    return {
      loading: false,
      submitLoading: false,
      outList: [],
      availableList: [],
      departDialogVisible: false,
      returnDialogVisible: false,
      departTarget: {},
      returnTarget: {},
      emergencyDialogVisible: false,
      emergencyForm: {
        vehicleId: null,
        driverName: ''
      },
      driverList: [],
      emergencyVehicleList: []
    }
  },
  created() {
    this.loadAll()
    // 每3分钟自动刷新
    this.timer = setInterval(() => {
      this.loadAll()
    }, 180000)
    // 页面切到后台时停止轮询，切回来时恢复并立即刷新
    this.handleVisibility = () => {
      if (document.hidden) {
        if (this.timer) {
          clearInterval(this.timer)
          this.timer = null
        }
      } else {
        if (!this.timer) {
          this.loadAll()
          this.timer = setInterval(() => {
            this.loadAll()
          }, 180000)
        }
      }
    }
    document.addEventListener('visibilitychange', this.handleVisibility)
    // 监听审批通过推送 → 自动刷新可出厂列表
    this.handleApprovedPush = (msg) => {
      this.loadAll()
      if (msg) {
        this.$notify({
          title: '有新的可出厂车辆',
          message: msg.content || '审批通过的车辆已可出厂',
          type: 'success',
          duration: 5000
        })
      }
    }
    noticeSocket.on('carApplyApproved', this.handleApprovedPush)
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
    }
    document.removeEventListener('visibilitychange', this.handleVisibility)
    noticeSocket.off('carApplyApproved', this.handleApprovedPush)
  },
  methods: {
    loadAll() {
      this.loading = true
      Promise.all([
        getOutVehicles(),
        getAvailableVehicles()
      ]).then(([outRes, availRes]) => {
        this.outList = outRes.data || []
        this.availableList = availRes.data || []
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleDepart(item) {
      this.departTarget = item
      this.departDialogVisible = true
    },
    handleReturn(item) {
      this.returnTarget = item
      this.returnDialogVisible = true
    },
    confirmDepart() {
      this.submitLoading = true
      const data = {
        vehicleId: this.departTarget.vehicleId,
        applyId: this.departTarget.applyId
      }
      depart(data).then(res => {
        if (res.code === 200) {
          this.$modal.msgSuccess(res.msg || this.$t('carDuty.departSuccess'))
          this.departDialogVisible = false
          this.loadAll()
        }
        this.submitLoading = false
      }).catch(() => {
        this.submitLoading = false
      })
    },
    confirmReturn() {
      this.submitLoading = true
      returnVehicle({ accessId: this.returnTarget.accessId }).then(res => {
        if (res.code === 200) {
          this.$modal.msgSuccess(res.msg || this.$t('carDuty.returnSuccess'))
          this.returnDialogVisible = false
          this.loadAll()
        }
        this.submitLoading = false
      }).catch(() => {
        this.submitLoading = false
      })
    },
    handleEmergency() {
      this.emergencyForm = { vehicleId: null, driverName: '' }
      getDrivers().then(res => {
        this.driverList = res.data || []
      })
      getEmergencyVehicles().then(res => {
        this.emergencyVehicleList = res.data || []
      })
      this.emergencyDialogVisible = true
    },
    confirmEmergency() {
      if (!this.emergencyForm.vehicleId) {
        this.$modal.msgWarning(this.$t('carDuty.selectVehicleWarn'))
        return
      }
      if (!this.emergencyForm.driverName) {
        this.$modal.msgWarning(this.$t('carDuty.selectDriverWarn'))
        return
      }
      this.submitLoading = true
      emergencyDepart(this.emergencyForm).then(res => {
        if (res.code === 200) {
          this.$modal.msgSuccess(this.$t('carDuty.emergencySuccess'))
          this.emergencyDialogVisible = false
          this.loadAll()
        }
        this.submitLoading = false
      }).catch(() => {
        this.submitLoading = false
      })
    }
  }
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}
.badge-item {
  margin-left: 8px;
}
.vehicle-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
}
.vehicle-card {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 16px 18px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  transition: box-shadow 0.2s;
}
.vehicle-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}
.out-card {
  border-left: 4px solid #e6a23c;
  background: #fdf6ec;
}
.available-card {
  border-left: 4px solid #67c23a;
  background: #f0f9eb;
}
.plate-tag {
  display: inline-block;
  padding: 4px 10px;
  background: #2c5282;
  color: #fff;
  border-radius: 4px;
  font-size: 16px;
  font-weight: 700;
  letter-spacing: 1px;
  margin-bottom: 10px;
}
.info-row {
  margin-bottom: 5px;
  font-size: 14px;
  color: #606266;
}
.info-row .label {
  color: #909399;
  margin-right: 4px;
}
.tag-temp {
  color: #e6a23c;
  font-style: italic;
}
.tag-apply {
  color: #409eff;
  font-weight: 500;
}
.vehicle-action {
  margin-top: 12px;
  text-align: right;
}
.empty-tip {
  text-align: center;
  color: #909399;
  padding: 32px 0;
  font-size: 14px;
}
.confirm-info p {
  margin: 8px 0;
  font-size: 14px;
}
.fab-container {
  position: fixed;
  right: 32px;
  bottom: 80px;
  z-index: 999;
}
.fab-btn {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background: linear-gradient(135deg, #f56c6c, #e64340);
  color: #fff;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 16px rgba(245, 108, 108, 0.5);
  transition: all 0.3s;
  user-select: none;
}
.fab-btn:hover {
  transform: scale(1.1);
  box-shadow: 0 6px 24px rgba(245, 108, 108, 0.7);
}
.fab-btn i {
  font-size: 22px;
  line-height: 1;
}
.fab-btn span {
  font-size: 11px;
  margin-top: 2px;
  white-space: nowrap;
}
</style>
