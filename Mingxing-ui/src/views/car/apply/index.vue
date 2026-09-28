<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item :label="$t('carApply.applyNo')" prop="applyNo">
        <el-input v-model="queryParams.applyNo" :placeholder="$t('carApply.input') + $t('carApply.applyNo')" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="$t('carApply.applicantName')" prop="applicantName">
        <el-input v-model="queryParams.applicantName" :placeholder="$t('carApply.input') + $t('carApply.applicantName')" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="$t('carApply.status')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('carApply.select')" clearable>
          <el-option v-for="dict in statusOptions" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('carApply.isUrgent')" prop="isUrgent">
        <el-select v-model="queryParams.isUrgent" :placeholder="$t('carApply.select')" clearable>
          <el-option :label="$t('carApply.yes')" value="1" />
          <el-option :label="$t('carApply.no')" value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('carApply.search') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('carApply.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['car:apply:add']">{{ $t('carApply.add') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate()" v-hasPermi="['car:apply:edit']">{{ $t('carApply.edit') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete()" v-hasPermi="['car:apply:remove']">{{ $t('carApply.delete') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['car:apply:export']">{{ $t('carApply.export') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="applyList" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('carApply.applyNo')" align="center" prop="applyNo" width="140" />
      <el-table-column :label="$t('carApply.applicantName')" align="center" prop="applicantName" min-width="90" />
      <el-table-column :label="$t('carApply.tripRange')" align="center" min-width="200">
        <template slot-scope="scope">
          <span>{{ scope.row.startPlace }}</span>
          <i class="el-icon-right" style="margin: 0 4px; color: #999;"></i>
          <span>{{ scope.row.endPlace }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('carApply.vehicle')" align="center" min-width="110">
        <template slot-scope="scope">
          <span v-if="scope.row.status === '0'">{{ $t('carApply.pending') }}</span>
          <span v-else-if="scope.row.vehicleType === '2'">{{ scope.row.externalPlateNumber }}</span>
          <span v-else>{{ scope.row.plateNumber }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('carApply.driver')" align="center" min-width="110">
        <template slot-scope="scope">
          <span v-if="scope.row.status === '0'">{{ $t('carApply.pending') }}</span>
          <span v-else-if="scope.row.vehicleType === '2'">{{ scope.row.externalDriverPhone }}</span>
          <span v-else>{{ scope.row.driverName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('carApply.departureTime')" align="center" prop="departureTime" width="150">
        <template slot-scope="scope">{{ parseTime(scope.row.departureTime, '{y}-{m}-{d} {h}:{i}') }}</template>
      </el-table-column>
      <el-table-column :label="$t('carApply.useType')" align="center" prop="useType" width="90">
        <template slot-scope="scope">
          <el-tag size="small" :type="scope.row.useType === '1' ? '' : 'warning'">{{ useTypeLabel(scope.row.useType) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('carApply.purposeType')" align="center" prop="purposeType" min-width="120">
        <template slot-scope="scope">{{ purposeTypeLabel(scope.row.purposeType) }}</template>
      </el-table-column>
      <el-table-column :label="$t('carApply.status')" align="center" prop="status" width="90">
        <template slot-scope="scope">
          <el-tag size="small" :type="statusTagType(scope.row.status)">{{ statusLabel(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('carApply.urgent')" align="center" prop="isUrgent" width="80">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isUrgent === '1'" type="danger" size="small">{{ $t('carApply.pendingSupplement') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('carApply.operation')" align="center" width="220" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['car:apply:edit']">{{ $t('carApply.edit') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-check" @click="handleAudit(scope.row)" v-if="scope.row.status === '0'" v-hasPermi="['car:apply:audit']">{{ $t('carApply.audit') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-document-checked" @click="handleSupplement(scope.row)" v-if="scope.row.isUrgent === '1'" v-hasPermi="['car:apply:supplement']">{{ $t('carApply.supplement') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['car:apply:remove']">{{ $t('carApply.delete') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 申请用车弹窗 -->
    <el-dialog :title="$t('carApply.title')" :visible.sync="dialogVisible" width="780px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="160px">
        <el-form-item :label="$t('carApply.startPlace')" prop="startPlace">
          <el-cascader v-model="form.startPlace" :options="regions" clearable :placeholder="$t('carApply.select') + $t('carApply.startPlace')" style="width:100%" @change="onStartPlaceChange" />
          <el-input v-if="hasStartPlace" v-model="startDetailAddress" :placeholder="$t('carApply.startPlaceDetail')" maxlength="200" style="width:100%; margin-top:8px;" />
        </el-form-item>
        <el-form-item :label="$t('carApply.endPlace')" prop="endPlace">
          <el-cascader v-model="form.endPlace" :options="regions" clearable :placeholder="$t('carApply.select') + $t('carApply.endPlace')" style="width:100%" @change="onEndPlaceChange" />
          <el-input v-if="hasEndPlace" v-model="endDetailAddress" :placeholder="$t('carApply.endPlaceDetail')" maxlength="200" style="width:100%; margin-top:8px;" />
        </el-form-item>

        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item :label="$t('carApply.departureTime')" prop="departureTime">
              <el-date-picker v-model="form.departureTime" type="datetime" value-format="yyyy-MM-dd HH:mm" :placeholder="$t('carApply.select') + $t('carApply.departureTime')" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item :label="$t('carApply.tripType')">
          <el-radio-group v-model="form.tripType" size="small">
            <el-radio-button label="1">{{ $t('carApply.singleTrip') }}</el-radio-button>
            <el-radio-button label="2">{{ $t('carApply.roundTrip') }}</el-radio-button>
          </el-radio-group>
        </el-form-item>

        <el-row :gutter="16" v-if="form.tripType === '2'">
          <el-col :span="16">
            <el-form-item :label="$t('carApply.returnTime')">
              <el-date-picker v-model="form.returnTime" type="datetime" value-format="yyyy-MM-dd HH:mm" :placeholder="$t('carApply.select') + $t('carApply.returnTime')" style="width:100%" :disabled="form.returnTimeTbd === 'Y'" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label-width="80px">
              <el-checkbox v-model="form.returnTimeTbd" true-label="Y" false-label="N">{{ $t('carApply.returnTimeTbd') }}</el-checkbox>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item :label="$t('carApply.useType')" prop="useType">
          <el-radio-group v-model="form.useType">
            <el-radio label="1">{{ $t('carApply.officialUse') }}</el-radio>
            <el-radio label="2">{{ $t('carApply.privateUse') }}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item :label="$t('carApply.purposeType')" prop="purposeType">
          <el-checkbox-group v-model="purposeTypeArr" @change="onPurposeTypeChange">
            <el-checkbox label="goods">{{ $t('carApply.deliverGoods') }}</el-checkbox>
            <el-checkbox label="people">{{ $t('carApply.sendPeople') }}</el-checkbox>
            <el-checkbox label="pickup">{{ $t('carApply.pickupPeople') }}</el-checkbox>
            <el-checkbox label="business">{{ $t('carApply.businessTrip') }}</el-checkbox>
            <el-checkbox label="meal">{{ $t('carApply.meal') }}</el-checkbox>
            <el-checkbox label="other">{{ $t('carApply.other') }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <el-row :gutter="16" v-if="purposeTypeArr.includes('meal')">
          <el-col :span="8">
            <el-form-item :label="$t('carApply.driverAccompany')">
              <el-radio-group v-model="form.driverAccompany" size="small" style="display:inline-flex; white-space:nowrap;">
                <el-radio label="Y">{{ $t('common.yes') }}</el-radio>
                <el-radio label="N">{{ $t('common.no') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item v-if="purposeTypeArr.includes('other')" :label="$t('carApply.otherPurpose')">
          <el-input v-model="form.otherPurpose" :placeholder="$t('carApply.input') + $t('carApply.otherPurpose')" maxlength="200" />
        </el-form-item>

        <el-form-item :label="$t('carApply.purposeDetail')" prop="purpose">
          <el-input v-model="form.purpose" type="textarea" :rows="3" :placeholder="$t('carApply.purposePlaceholder')" maxlength="200" />
        </el-form-item>

        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item :label="$t('carApply.contactPhone')" prop="contactPhone">
              <el-input v-model="form.contactPhone" :placeholder="$t('carApply.input') + $t('carApply.contactPhone')" maxlength="20" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item :label="$t('carApply.remark')">
          <el-input v-model="form.remark" type="textarea" :rows="2" :placeholder="$t('carApply.input') + $t('carApply.remark')" maxlength="200" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('carApply.submit') }}</el-button>
        <el-button @click="cancel">{{ $t('carApply.cancel') }}</el-button>
      </div>
    </el-dialog>

    <!-- 审批弹窗 -->
    <el-dialog :title="$t('carApply.auditTitle')" :visible.sync="auditDialogVisible" width="450px" append-to-body :close-on-click-modal="false">
      <el-form ref="auditForm" :model="auditForm" :rules="auditRules" label-width="90px">
        <el-form-item :label="$t('carApply.applyNo')">
          <el-input :value="auditForm.applyNo" disabled />
        </el-form-item>
        <el-form-item :label="$t('carApply.auditResult')" prop="status">
          <el-radio-group v-model="auditForm.status">
            <el-radio label="1">{{ $t('carApply.approve') }}</el-radio>
            <el-radio label="2">{{ $t('carApply.reject') }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t('carApply.vehicleType')" v-if="auditForm.status === '1'">
          <el-radio-group v-model="auditForm.vehicleType">
            <el-radio label="1">{{ $t('carApply.companyVehicle') }}</el-radio>
            <el-radio label="2">{{ $t('carApply.externalVehicle') }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <template v-if="auditForm.status === '1' && auditForm.vehicleType === '1'">
          <el-form-item :label="$t('carApply.vehicle')">
            <el-select v-model="auditForm.vehicleId" :placeholder="$t('carApply.selectVehicle')" clearable>
              <el-option v-for="v in vehicleOptions" :key="v.vehicleId" :label="v.plateNumber + ' - ' + v.brand + ' ' + v.color" :value="v.vehicleId" />
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('carApply.driver')">
            <el-select v-model="auditForm.driverId" :placeholder="$t('carApply.selectDriver')" clearable>
              <el-option v-for="d in driverOptions" :key="d.driverId" :label="d.driverName + ' - ' + d.phone" :value="d.driverId" />
            </el-select>
          </el-form-item>
        </template>
        <template v-if="auditForm.status === '1' && auditForm.vehicleType === '2'">
          <el-form-item :label="$t('carApply.externalPlateNumber')">
            <el-input v-model="auditForm.externalPlateNumber" :placeholder="$t('carApply.input') + $t('carApply.externalPlateNumber')" maxlength="50" />
          </el-form-item>
          <el-form-item :label="$t('carApply.externalDriverPhone')">
            <el-input v-model="auditForm.externalDriverPhone" :placeholder="$t('carApply.input') + $t('carApply.externalDriverPhone')" maxlength="20" />
          </el-form-item>
        </template>
        <el-form-item :label="$t('carApply.auditRemark')">
          <el-input v-model="auditForm.auditRemark" type="textarea" :rows="3" :placeholder="$t('carApply.input') + $t('carApply.auditRemark')" maxlength="200" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAudit">{{ $t('carApply.confirm') }}</el-button>
        <el-button @click="auditDialogVisible = false">{{ $t('carApply.cancel') }}</el-button>
      </div>
    </el-dialog>

    <!-- 补签弹窗 -->
    <el-dialog :title="$t('carApply.supplementTitle')" :visible.sync="supplementDialogVisible" width="800px" append-to-body :close-on-click-modal="false" top="8vh">
      <el-form ref="supplementForm" :model="supplementForm" label-width="160px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item :label="$t('carApply.startPlace')" prop="startPlace">
              <el-cascader v-model="supplementForm.startPlace" :options="regions" clearable :placeholder="$t('carApply.select') + $t('carApply.startPlace')" style="width:100%" @change="onSupplementStartChange" />
              <el-input v-if="supplementForm.startPlace && supplementForm.startPlace.length > 0" v-model="supplementStartDetail" :placeholder="$t('carApply.startPlaceDetail')" maxlength="200" style="width:100%; margin-top:8px;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('carApply.endPlace')" prop="endPlace">
              <el-cascader v-model="supplementForm.endPlace" :options="regions" clearable :placeholder="$t('carApply.select') + $t('carApply.endPlace')" style="width:100%" @change="onSupplementEndChange" />
              <el-input v-if="supplementForm.endPlace && supplementForm.endPlace.length > 0" v-model="supplementEndDetail" :placeholder="$t('carApply.endPlaceDetail')" maxlength="200" style="width:100%; margin-top:8px;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item :label="$t('carApply.departureTime')" prop="departureTime">
              <el-date-picker v-model="supplementForm.departureTime" type="datetime" :placeholder="$t('carApply.select') + $t('carApply.departureTime')" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('carApply.purposeType')" prop="purposeType">
              <el-select v-model="supplementForm.purposeType" :placeholder="$t('carApply.select') + $t('carApply.purposeType')" clearable style="width:100%">
                <el-option v-for="dict in purposeTypeOptions" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item :label="$t('carApply.purposeDetail')" prop="purpose">
          <el-input v-model="supplementForm.purpose" type="textarea" :rows="3" :placeholder="$t('carApply.purposePlaceholder')" maxlength="200" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitSupplement">{{ $t('carApply.confirmSupplement') }}</el-button>
        <el-button @click="supplementDialogVisible = false">{{ $t('carApply.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listApply, getApply, addApply, updateApply, auditApply, delApply, supplementApply } from "@/api/car/apply"
import { listVehicle } from "@/api/car/vehicle"
import { listDriver } from "@/api/car/driver"
import { regions } from "@/assets/js/region.js"
import noticeSocket from "@/utils/noticeSocket"

export default {
  name: "CarApply",
  data() {
    return {
      loading: false,
      showSearch: true,
      total: 0,
      applyList: [],
      ids: [],
      single: true,
      multiple: true,
      dialogVisible: false,
      auditDialogVisible: false,
      auditForm: {},
      supplementDialogVisible: false,
      supplementForm: {},
      supplementStartDetail: '',
      supplementEndDetail: '',
      purposeTypeOptions: [
        { value: 'goods', label: this.$t('carApply.deliverGoods') },
        { value: 'people', label: this.$t('carApply.sendPeople') },
        { value: 'pickup', label: this.$t('carApply.pickupPeople') },
        { value: 'business', label: this.$t('carApply.businessTrip') },
        { value: 'meal', label: this.$t('carApply.meal') },
        { value: 'other', label: this.$t('carApply.other') }
      ],
      vehicleOptions: [],
      driverOptions: [],
      purposeTypeArr: [],
      startDetailAddress: '',
      endDetailAddress: '',
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        applyNo: null,
        applicantName: null,
        status: null,
        isUrgent: null
      },
      statusOptions: [
        { value: '0', label: this.$t('carApply.pending') },
        { value: '1', label: this.$t('carApply.approved') },
        { value: '2', label: this.$t('carApply.rejected') },
        { value: '3', label: this.$t('carApply.finished') }
      ],
      form: {},
      regions: regions,
      rules: {
        startPlace: [{ required: true, message: this.$t('carApply.requiredStartPlace'), trigger: 'blur' }],
        endPlace: [{ required: true, message: this.$t('carApply.requiredEndPlace'), trigger: 'blur' }],
        departureTime: [{ required: true, message: this.$t('carApply.requiredDepartureTime'), trigger: 'change' }],
        useType: [{ required: true, message: this.$t('carApply.requiredUseType'), trigger: 'change' }],
        purposeType: [{ required: true, message: this.$t('carApply.requiredPurposeType'), trigger: 'blur' }],
        contactPhone: [{ required: true, message: this.$t('carApply.requiredContactPhone'), trigger: 'blur' }]
      },
      auditRules: {
      }
    }
  },
  computed: {
    hasStartPlace() {
      return Array.isArray(this.form.startPlace) && this.form.startPlace.length > 0
    },
    hasEndPlace() {
      return Array.isArray(this.form.endPlace) && this.form.endPlace.length > 0
    }
  },
  created() {
    this.handleApplyPush = (msg) => {
      this.$nextTick(() => {
        this.getList()
        this.$message && this.$message.success(
          msg && msg.applicantName
            ? `收到新的用车申请（${msg.applicantName}），列表已刷新`
            : '收到新的用车申请，列表已刷新'
        )
      })
    }
    this.handleAuditPush = () => {
      this.$nextTick(() => {
        this.getList()
      })
    }
    noticeSocket.on('carApply', this.handleApplyPush)
    noticeSocket.on('carApplyAudit', this.handleAuditPush)
    this.getList()
  },
  activated() {
    this.getList()
  },
  mounted() {
  },
  beforeDestroy() {
    noticeSocket.off('carApply', this.handleApplyPush)
    noticeSocket.off('carApplyAudit', this.handleAuditPush)
  },
  methods: {
    getList() {
      this.loading = true
      listApply(this.queryParams).then(response => {
        this.applyList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.applyId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    statusTagType(status) {
      const map = { '0': 'warning', '1': 'success', '2': 'danger', '3': 'info' }
      return map[status] || 'info'
    },
    statusLabel(status) {
      const map = { '0': this.$t('carApply.pending'), '1': this.$t('carApply.approved'), '2': this.$t('carApply.rejected'), '3': this.$t('carApply.finished') }
      return map[status] || status
    },
    useTypeLabel(type) {
      const map = { '1': this.$t('carApply.officialUse'), '2': this.$t('carApply.privateUse') }
      return map[type] || type
    },
    purposeTypeLabel(types) {
      if (!types) return ''
      const map = {
        goods: this.$t('carApply.deliverGoods'),
        people: this.$t('carApply.sendPeople'),
        pickup: this.$t('carApply.pickupPeople'),
        business: this.$t('carApply.businessTrip'),
        meal: this.$t('carApply.meal'),
        other: this.$t('carApply.other')
      }
      return types.split(',').map(t => map[t] || t).join(', ')
    },
    onPurposeTypeChange(val) {
      this.form.purposeType = val.join(',')
    },
    onStartPlaceChange(val) {
      if (!Array.isArray(val) || val.length === 0) {
        this.startDetailAddress = ''
      }
    },
    onEndPlaceChange(val) {
      if (!Array.isArray(val) || val.length === 0) {
        this.endDetailAddress = ''
      }
    },
    handleAdd() {
      this.reset()
      this.form.applicantName = this.$store.getters.nickName || this.$store.getters.name || ''
      this.form.deptName = (this.$store.getters.nickName ? this.$store.getters.nickName : '') || ''
      this.dialogVisible = true
    },
    handleUpdate(row) {
      this.reset()
      const applyId = row ? row.applyId : this.ids[0]
      getApply(applyId).then(response => {
        this.form = response.data
        this.purposeTypeArr = this.form.purposeType ? this.form.purposeType.split(',') : []
        if (this.form.startPlace && typeof this.form.startPlace === 'string') {
          const parts = this.form.startPlace.split('/')
          if (parts[0] === '广东省') {
            this.form.startPlace = parts.slice(0, 3)
            this.startDetailAddress = parts.slice(3).join('/')
          } else {
            this.form.startPlace = parts.slice(0, 2)
            this.startDetailAddress = parts.slice(2).join('/')
          }
        }
        if (this.form.endPlace && typeof this.form.endPlace === 'string') {
          const parts = this.form.endPlace.split('/')
          if (parts[0] === '广东省') {
            this.form.endPlace = parts.slice(0, 3)
            this.endDetailAddress = parts.slice(3).join('/')
          } else {
            this.form.endPlace = parts.slice(0, 2)
            this.endDetailAddress = parts.slice(2).join('/')
          }
        }
        this.dialogVisible = true
      })
    },
    handleAudit(row) {
      this.auditForm = {
        applyId: row.applyId,
        applyNo: row.applyNo,
        status: '1',
        auditRemark: '',
        vehicleType: row.vehicleType || '1',
        externalPlateNumber: row.externalPlateNumber || '',
        externalDriverPhone: row.externalDriverPhone || '',
        vehicleId: row.vehicleId || null,
        driverId: row.driverId || null
      }
      // 加载车辆和司机列表
      listVehicle({ status: '0' }).then(res => {
        this.vehicleOptions = res.rows
      })
      listDriver({ status: '0' }).then(res => {
        this.driverOptions = res.rows
      })
      this.auditDialogVisible = true
    },
    submitAudit() {
      this.$refs["auditForm"].validate(valid => {
        if (valid) {
          if (this.auditForm.status === '1') {
            if (this.auditForm.vehicleType === '1' && (this.auditForm.vehicleId === null || this.auditForm.vehicleId === '')) {
              this.$modal.msgWarning(this.$t('carApply.selectVehicle'))
              return
            }
            if (this.auditForm.vehicleType === '2' && !this.auditForm.externalPlateNumber) {
              this.$modal.msgWarning(this.$t('carApply.input') + this.$t('carApply.externalPlateNumber'))
              return
            }
            if (this.auditForm.vehicleType === '2' && !this.auditForm.externalDriverPhone) {
              this.$modal.msgWarning(this.$t('carApply.input') + this.$t('carApply.externalDriverPhone'))
              return
            }
          }
          auditApply(this.auditForm).then(response => {
            if (response.code === 200) {
              this.$modal.msgSuccess(this.$t('carApply.success'))
              this.auditDialogVisible = false
              this.getList()
            }
          })
        }
      })
    },
    handleSupplement(row) {
      this.supplementStartDetail = ''
      this.supplementEndDetail = ''
      this.supplementForm = {
        applyId: row.applyId,
        startPlace: row.startPlace || '',
        endPlace: row.endPlace || '',
        departureTime: row.departureTime || '',
        purposeType: row.purposeType || '',
        purpose: row.purpose || ''
      }
      if (this.supplementForm.startPlace && typeof this.supplementForm.startPlace === 'string' && this.supplementForm.startPlace.indexOf('/') !== -1) {
        const parts = this.supplementForm.startPlace.split('/')
        if (parts[0] === '广东省') {
          this.supplementForm.startPlace = parts.slice(0, 3)
          this.supplementStartDetail = parts.slice(3).join('/')
        } else {
          this.supplementForm.startPlace = parts.slice(0, 2)
          this.supplementStartDetail = parts.slice(2).join('/')
        }
      }
      if (this.supplementForm.endPlace && typeof this.supplementForm.endPlace === 'string' && this.supplementForm.endPlace.indexOf('/') !== -1) {
        const parts = this.supplementForm.endPlace.split('/')
        if (parts[0] === '广东省') {
          this.supplementForm.endPlace = parts.slice(0, 3)
          this.supplementEndDetail = parts.slice(3).join('/')
        } else {
          this.supplementForm.endPlace = parts.slice(0, 2)
          this.supplementEndDetail = parts.slice(2).join('/')
        }
      }
      this.supplementDialogVisible = true
    },
    onSupplementStartChange(val) {
      if (!Array.isArray(val) || val.length === 0) {
        this.supplementStartDetail = ''
      }
    },
    onSupplementEndChange(val) {
      if (!Array.isArray(val) || val.length === 0) {
        this.supplementEndDetail = ''
      }
    },
    submitSupplement() {
      if (Array.isArray(this.supplementForm.startPlace)) {
        let placeStr = this.supplementForm.startPlace.join('/')
        if (this.supplementStartDetail) {
          placeStr += '/' + this.supplementStartDetail
        }
        this.supplementForm.startPlace = placeStr
      }
      if (Array.isArray(this.supplementForm.endPlace)) {
        let placeStr = this.supplementForm.endPlace.join('/')
        if (this.supplementEndDetail) {
          placeStr += '/' + this.supplementEndDetail
        }
        this.supplementForm.endPlace = placeStr
      }
      if (this.supplementForm.startPlace && !Array.isArray(this.supplementForm.startPlace) && this.supplementStartDetail) {
        this.supplementForm.startPlace += '/' + this.supplementStartDetail
      }
      if (this.supplementForm.endPlace && !Array.isArray(this.supplementForm.endPlace) && this.supplementEndDetail) {
        this.supplementForm.endPlace += '/' + this.supplementEndDetail
      }
      supplementApply(this.supplementForm).then(response => {
        if (response.code === 200) {
          this.$modal.msgSuccess(this.$t('carApply.supplementSuccess'))
          this.supplementDialogVisible = false
          this.getList()
        }
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 起始地
          if (Array.isArray(this.form.startPlace)) {
            if (this.startDetailAddress) {
              this.form.startPlace = this.form.startPlace.join('/') + '/' + this.startDetailAddress
            } else {
              this.form.startPlace = this.form.startPlace.join('/')
            }
          }
          // 目的地
          if (Array.isArray(this.form.endPlace)) {
            if (this.endDetailAddress) {
              this.form.endPlace = this.form.endPlace.join('/') + '/' + this.endDetailAddress
            } else {
              this.form.endPlace = this.form.endPlace.join('/')
            }
          }
          if (this.form.applyId != null) {
            updateApply(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('carApply.success'))
              this.dialogVisible = false
              this.getList()
            })
          } else {
            addApply(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('carApply.success'))
              this.dialogVisible = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const applyIds = row ? [row.applyId] : this.ids
      this.$modal.confirm(this.$t('carApply.confirmDelete', { id: applyIds.join(',') })).then(() => {
        return delApply(applyIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('carApply.success'))
      }).catch(() => {})
    },
    handleExport() {
      this.download('car/apply/export', { ...this.queryParams }, `用车申请_${new Date().getTime()}.xlsx`)
    },
    reset() {
      this.form = {
        applyId: null,
        applicantName: '',
        deptName: '',
        startPlace: '',
        endPlace: '',
        departureTime: '',
        returnTime: '',
        returnTimeTbd: 'N',
        tripType: '1',
        useType: '1',
        purposeType: '',
        driverAccompany: 'N',
        otherPurpose: '',
        contactPhone: '',
        purpose: '',
        vehicleId: null,
        driverId: null,
        remark: ''
      }
      this.purposeTypeArr = []
      this.startDetailAddress = ''
      this.endDetailAddress = ''
      this.resetForm("form")
    },
    cancel() {
      this.dialogVisible = false
      this.reset()
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>