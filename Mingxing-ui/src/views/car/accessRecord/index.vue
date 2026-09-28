<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" class="query-form" size="small" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item :label="$t('accessRecord.plateNumber')" prop="plateNumber">
        <el-input v-model="queryParams.plateNumber" :placeholder="$t('accessRecord.input') + $t('accessRecord.plateNumber')" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="$t('accessRecord.driverName')" prop="driverName">
        <el-input v-model="queryParams.driverName" :placeholder="$t('accessRecord.input') + $t('accessRecord.driverName')" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="$t('accessRecord.outTime')" prop="outTime">
        <el-date-picker v-model="outTimeRange" type="datetimerange" value-format="yyyy-MM-dd HH:mm:ss" range-separator="-" :start-placeholder="$t('accessRecord.startTime')" :end-placeholder="$t('accessRecord.endTime')" style="width: 320px" @change="onOutTimeChange" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('accessRecord.search') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('accessRecord.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['car:accessRecord:add']">{{ $t('accessRecord.add') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate()" v-hasPermi="['car:accessRecord:edit']">{{ $t('accessRecord.edit') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete()" v-hasPermi="['car:accessRecord:remove']">{{ $t('accessRecord.delete') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['car:accessRecord:export']">{{ $t('accessRecord.export') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="recordList" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="index" :label="$t('accessRecord.index')" width="80" align="center" />
      <el-table-column :label="$t('accessRecord.applyNo')" align="center" prop="applyNo" min-width="130">
        <template slot-scope="scope">
          <span>{{ scope.row.applyNo }}</span>
          <el-tag v-if="scope.row.isUrgent === '1'" type="danger" size="mini" style="margin-left:4px">紧急</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('accessRecord.plateNumber')" align="center" prop="plateNumber" min-width="120">
        <template slot-scope="scope">
          <span class="plate-tag">{{ scope.row.plateNumber }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('accessRecord.driverName')" align="center" prop="driverName" min-width="100" />
      <el-table-column :label="$t('accessRecord.outTime')" align="center" prop="outTime" width="160">
        <template slot-scope="scope">{{ parseTime(scope.row.outTime, '{y}-{m}-{d} {h}:{i}') }}</template>
      </el-table-column>
      <el-table-column :label="$t('accessRecord.inTime')" align="center" prop="inTime" width="160">
        <template slot-scope="scope">{{ scope.row.inTime ? parseTime(scope.row.inTime, '{y}-{m}-{d} {h}:{i}') : '' }}</template>
      </el-table-column>
      <el-table-column :label="$t('accessRecord.operatorName')" align="center" prop="operatorName" min-width="100" />
      <el-table-column :label="$t('accessRecord.remark')" align="center" prop="remark" min-width="150" show-overflow-tooltip />
      <el-table-column :label="$t('accessRecord.createTime')" align="center" prop="createTime" width="160">
        <template slot-scope="scope">{{ parseTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column :label="$t('accessRecord.operation')" align="center" width="160" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['car:accessRecord:edit']">{{ $t('accessRecord.edit') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['car:accessRecord:remove']">{{ $t('accessRecord.delete') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="620px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="130px">
        <el-form-item :label="$t('accessRecord.applyId')" prop="applyId">
          <el-select v-model="form.applyId" filterable clearable :placeholder="$t('accessRecord.selectApply')" style="width:100%" @change="onApplyChange">
            <el-option v-for="a in applyOptions" :key="a.applyId" :label="a.applyNo + ' - ' + a.applicantName + ' (' + a.plateNumber + ')'" :value="a.applyId" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('accessRecord.plateNumber')" prop="plateNumber">
          <el-select v-model="form.plateNumber" filterable allow-create default-first-option :placeholder="$t('accessRecord.select') + $t('accessRecord.plateNumber')" style="width:100%">
            <el-option v-for="v in vehicleOptions" :key="v.plateNumber" :label="v.plateNumber + ' - ' + v.brand + ' ' + v.color" :value="v.plateNumber" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('accessRecord.driverName')" prop="driverName">
          <el-input v-model="form.driverName" :placeholder="$t('accessRecord.input') + $t('accessRecord.driverName')" maxlength="50" />
        </el-form-item>
        <el-form-item :label="$t('accessRecord.outTime')" prop="outTime">
          <el-date-picker v-model="form.outTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" :placeholder="$t('accessRecord.select') + $t('accessRecord.outTime')" style="width:100%" />
        </el-form-item>
        <el-form-item :label="$t('accessRecord.inTime')" prop="inTime">
          <el-date-picker v-model="form.inTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" :placeholder="$t('accessRecord.select') + $t('accessRecord.inTime')" style="width:100%" />
        </el-form-item>
        <el-form-item :label="$t('accessRecord.operatorName')" prop="operatorName">
          <el-input v-model="form.operatorName" :placeholder="$t('accessRecord.input') + $t('accessRecord.operatorName')" maxlength="50" />
        </el-form-item>
        <el-form-item :label="$t('accessRecord.remark')">
          <el-input v-model="form.remark" type="textarea" :placeholder="$t('accessRecord.input') + $t('accessRecord.remark')" maxlength="200" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('accessRecord.confirm') }}</el-button>
        <el-button @click="cancel">{{ $t('accessRecord.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAccessRecord, getAccessRecord, addAccessRecord, updateAccessRecord, delAccessRecord } from "@/api/car/accessRecord"
import { listVehicle } from "@/api/car/vehicle"
import { listApply } from "@/api/car/apply"

export default {
  name: "CarAccessRecord",
  data() {
    return {
      loading: false,
      showSearch: true,
      total: 0,
      recordList: [],
      ids: [],
      single: true,
      multiple: true,
      dialogVisible: false,
      dialogTitle: '',
      vehicleOptions: [],
      applyOptions: [],
      outTimeRange: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        plateNumber: null,
        driverName: null,
        params: {}
      },
      form: {},
      rules: {
        plateNumber: [{ required: true, message: this.$t('accessRecord.rulePlate'), trigger: 'change' }],
        outTime: [{ required: true, message: this.$t('accessRecord.ruleOutTime'), trigger: 'change' }]
      }
    }
  },
  created() {
    this.getVehicleList()
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listAccessRecord(this.queryParams).then(response => {
        this.recordList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getVehicleList() {
      listVehicle({ pageNum: 1, pageSize: 100, status: '0' }).then(response => {
        this.vehicleOptions = response.rows
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.outTimeRange = []
      this.resetForm("queryForm")
      this.handleQuery()
    },
    onOutTimeChange(val) {
      if (val && val.length === 2) {
        this.queryParams.params.beginOutTime = val[0]
        this.queryParams.params.endOutTime = val[1]
      } else {
        this.queryParams.params.beginOutTime = undefined
        this.queryParams.params.endOutTime = undefined
      }
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.accessId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.loadApplyOptions()
      this.dialogTitle = this.$t('accessRecord.addTitle')
      this.dialogVisible = true
    },
    handleUpdate(row) {
      this.reset()
      this.loadApplyOptions()
      const accessId = row ? row.accessId : this.ids[0]
      getAccessRecord(accessId).then(response => {
        this.form = response.data
        this.dialogTitle = this.$t('accessRecord.editTitle')
        this.dialogVisible = true
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.accessId != null) {
            updateAccessRecord(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('accessRecord.success'))
              this.dialogVisible = false
              this.getList()
            })
          } else {
            addAccessRecord(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('accessRecord.success'))
              this.dialogVisible = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const accessIds = row ? [row.accessId] : this.ids
      this.$modal.confirm(this.$t('accessRecord.confirmDelete')).then(() => {
        return delAccessRecord(accessIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('accessRecord.success'))
      }).catch(() => {})
    },
    handleExport() {
      this.download('car/accessRecord/export', { ...this.queryParams }, `车辆出入记录_${new Date().getTime()}.xlsx`)
    },
    loadApplyOptions() {
      listApply({ status: '1', pageNum: 1, pageSize: 200 }).then(res => {
        this.applyOptions = res.rows
      })
    },
    onApplyChange(applyId) {
      if (!applyId) return
      const apply = this.applyOptions.find(a => a.applyId === applyId)
      if (apply) {
        this.form.plateNumber = apply.plateNumber || apply.externalPlateNumber || ''
        this.form.driverName = apply.driverName || apply.externalDriverPhone || ''
      }
    },
    reset() {
      this.form = {
        accessId: null,
        applyId: null,
        plateNumber: null,
        driverName: null,
        outTime: null,
        inTime: null,
        operatorName: null,
        remark: null
      }
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
.query-form .el-form-item__label {
  white-space: nowrap;
}
.plate-tag {
  display: inline-block;
  padding: 2px 8px;
  background: #edf2f7;
  color: #2c5282;
  border-radius: 3px;
  font-size: 12px;
  font-weight: 500;
}
.dialog-footer {
  text-align: right;
}
</style>