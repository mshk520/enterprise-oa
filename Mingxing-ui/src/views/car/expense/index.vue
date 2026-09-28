<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item :label="$t('carExpense.plateNumber')" prop="plateNumber">
        <el-select v-model="queryParams.plateNumber" :placeholder="$t('carExpense.select') + $t('carExpense.plateNumber')" clearable>
          <el-option v-for="v in vehicleOptions" :key="v.vehicleId" :label="v.plateNumber" :value="v.plateNumber" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('carExpense.useType')" prop="useType">
        <el-select v-model="queryParams.useType" :placeholder="$t('carExpense.select')" clearable>
          <el-option v-for="dict in useTypeOptions" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('carApply.search') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('carApply.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['car:expense:add']">{{ $t('carApply.add') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate()" v-hasPermi="['car:expense:edit']">{{ $t('carApply.edit') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete()" v-hasPermi="['car:expense:remove']">{{ $t('carApply.delete') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['car:expense:export']">{{ $t('carApply.export') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="expenseList" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('carExpense.applyNo')" align="center" prop="applyNo" min-width="130" />
      <el-table-column :label="$t('carExpense.plateNumber')" align="center" prop="plateNumber" min-width="100" />
      <el-table-column :label="$t('carExpense.useType')" align="center" min-width="90">
        <template slot-scope="scope">
          <el-tag size="small" :type="scope.row.useType === '1' ? '' : 'warning'">{{ useTypeLabel(scope.row.useType) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('carExpense.fuelAmount')" align="center" prop="fuelAmount" min-width="90" />
      <el-table-column :label="$t('carExpense.tollFee')" align="center" prop="tollFee" min-width="100" />
      <el-table-column :label="$t('carExpense.maintenanceFee')" align="center" prop="maintenanceFee" min-width="90" />
      <el-table-column :label="$t('carExpense.oilChange')" align="center" prop="oilChange" min-width="80" />
      <el-table-column :label="$t('carExpense.tireChange')" align="center" prop="tireChange" min-width="80" />
      <el-table-column :label="$t('carExpense.brakeChange')" align="center" prop="brakeChange" min-width="90" />
      <el-table-column :label="$t('carExpense.insuranceTax')" align="center" prop="insuranceTax" min-width="110" />
      <el-table-column :label="$t('carExpense.phoneFee')" align="center" prop="phoneFee" min-width="80" />
      <el-table-column :label="$t('carExpense.parkingFee')" align="center" prop="parkingFee" min-width="80" />
      <el-table-column :label="$t('carExpense.hotelFee')" align="center" prop="hotelFee" min-width="80" />
      <el-table-column :label="$t('carExpense.mealFee')" align="center" prop="mealFee" min-width="80" />
      <el-table-column :label="$t('carExpense.annualInspection')" align="center" prop="annualInspection" min-width="80" />
      <el-table-column :label="$t('carExpense.mileage')" align="center" prop="mileage" min-width="80" />
      <el-table-column :label="$t('carExpense.fuelVolume')" align="center" prop="fuelVolume" min-width="90" />
      <el-table-column :label="$t('carExpense.fuelPerKm')" align="center" prop="fuelPerKm" min-width="100" />
      <el-table-column :label="$t('carExpense.fuelPer100km')" align="center" prop="fuelPer100km" min-width="100" />
      <el-table-column :label="$t('carExpense.otherFee')" align="center" prop="otherFee" min-width="80" />
      <el-table-column :label="$t('carApply.operation')" align="center" width="150" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['car:expense:edit']">{{ $t('carApply.edit') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['car:expense:remove']">{{ $t('carApply.delete') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="$t('carExpense.title')" :visible.sync="dialogVisible" width="750px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item :label="$t('carExpense.applyId')" prop="applyId">
              <el-select v-model="form.applyId" filterable clearable :placeholder="$t('carExpense.selectApply')" style="width:100%" @change="onApplyChange">
                <el-option v-for="a in applyOptions" :key="a.applyId" :label="a.applyNo + ' - ' + a.applicantName" :value="a.applyId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('carExpense.accessId')" prop="accessId">
              <el-select v-model="form.accessId" filterable clearable :placeholder="$t('carExpense.selectAccess')" style="width:100%">
                <el-option v-for="r in accessOptions" :key="r.accessId" :label="r.plateNumber + ' (' + r.outTimeStr + ')'" :value="r.accessId" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item :label="$t('carExpense.plateNumber')" prop="plateNumber">
              <el-select v-model="form.plateNumber" :placeholder="$t('carExpense.select') + $t('carExpense.plateNumber')" style="width:100%">
                <el-option v-for="v in vehicleOptions" :key="v.vehicleId" :label="v.plateNumber + ' - ' + v.brand + ' ' + v.color" :value="v.plateNumber" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('carExpense.useType')" prop="useType">
              <el-select v-model="form.useType" :placeholder="$t('carExpense.select') + $t('carExpense.useType')" style="width:100%">
                <el-option v-for="dict in useTypeOptions" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item :label="$t('carExpense.fuelAmount')">
              <el-input-number v-model="form.fuelAmount" :precision="2" :min="0" controls-position="right" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('carExpense.tollFee')">
              <el-input-number v-model="form.tollFee" :precision="2" :min="0" controls-position="right" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item :label="$t('carExpense.maintenanceFee')">
              <el-input-number v-model="form.maintenanceFee" :precision="2" :min="0" controls-position="right" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('carExpense.oilChange')">
              <el-input-number v-model="form.oilChange" :precision="2" :min="0" controls-position="right" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item :label="$t('carExpense.tireChange')">
              <el-input-number v-model="form.tireChange" :precision="2" :min="0" controls-position="right" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('carExpense.brakeChange')">
              <el-input-number v-model="form.brakeChange" :precision="2" :min="0" controls-position="right" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item :label="$t('carExpense.insuranceTax')">
              <el-input-number v-model="form.insuranceTax" :precision="2" :min="0" controls-position="right" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('carExpense.phoneFee')">
              <el-input-number v-model="form.phoneFee" :precision="2" :min="0" controls-position="right" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item :label="$t('carExpense.parkingFee')">
              <el-input-number v-model="form.parkingFee" :precision="2" :min="0" controls-position="right" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('carExpense.hotelFee')">
              <el-input-number v-model="form.hotelFee" :precision="2" :min="0" controls-position="right" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item :label="$t('carExpense.mealFee')">
              <el-input-number v-model="form.mealFee" :precision="2" :min="0" controls-position="right" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('carExpense.annualInspection')">
              <el-input-number v-model="form.annualInspection" :precision="2" :min="0" controls-position="right" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item :label="$t('carExpense.mileage')">
              <el-input-number v-model="form.mileage" :precision="2" :min="0" controls-position="right" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('carExpense.fuelVolume')">
              <el-input-number v-model="form.fuelVolume" :precision="2" :min="0" controls-position="right" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item :label="$t('carExpense.fuelPerKm')">
              <el-input-number v-model="form.fuelPerKm" :precision="2" :min="0" controls-position="right" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('carExpense.fuelPer100km')">
              <el-input-number v-model="form.fuelPer100km" :precision="2" :min="0" controls-position="right" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="24">
            <el-form-item :label="$t('carExpense.otherFee')">
              <el-input-number v-model="form.otherFee" :precision="2" :min="0" controls-position="right" style="width:50%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="24">
            <el-form-item :label="$t('carApply.remark')">
              <el-input v-model="form.remark" type="textarea" :rows="3" :placeholder="$t('carExpense.input') + $t('carApply.remark')" maxlength="500" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('carApply.confirm') }}</el-button>
        <el-button @click="cancel">{{ $t('carApply.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listExpense, getExpense, addExpense, updateExpense, delExpense } from "@/api/car/expense"
import { listVehicle } from "@/api/car/vehicle"
import { listApply } from "@/api/car/apply"
import { listAccessRecord } from "@/api/car/accessRecord"

export default {
  name: "CarExpense",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      expenseList: [],
      vehicleOptions: [],
      applyOptions: [],
      accessOptions: [],
      dialogVisible: false,
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        plateNumber: undefined,
        useType: undefined
      },
      form: {},
      rules: {
        plateNumber: [{ required: true, message: this.$t('carExpense.select') + this.$t('carExpense.plateNumber'), trigger: 'change' }],
        useType: [{ required: true, message: this.$t('carExpense.select') + this.$t('carExpense.useType'), trigger: 'change' }]
      },
      useTypeOptions: [
        { value: '1', label: this.$t('carApply.officialUse') },
        { value: '2', label: this.$t('carApply.privateUse') }
      ]
    }
  },
  created() {
    this.getList()
    this.getVehicleList()
  },
  methods: {
    getList() {
      this.loading = true
      listExpense(this.queryParams).then(response => {
        this.expenseList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getVehicleList() {
      listVehicle({}).then(response => {
        this.vehicleOptions = response.rows
      })
    },
    useTypeLabel(val) {
      const opt = this.useTypeOptions.find(d => d.value === val)
      return opt ? opt.label : val
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.expenseId)
      this.single = selection.length !== 1
      this.multiple = selection.length === 0
    },
    handleAdd() {
      this.form = { useType: '1' }
      this.loadApplyOptions()
      this.dialogVisible = true
    },
    handleUpdate(row) {
      const id = row ? row.expenseId : this.ids[0]
      this.loadApplyOptions()
      getExpense(id).then(response => {
        this.form = response.data
        this.loadAccessOptions(this.form.applyId)
        this.dialogVisible = true
      })
    },
    loadApplyOptions() {
      listApply({ status: '1', pageNum: 1, pageSize: 200 }).then(res => {
        this.applyOptions = res.rows
      })
    },
    loadAccessOptions(applyId) {
      if (!applyId) {
        this.accessOptions = []
        return
      }
      listAccessRecord({ pageNum: 1, pageSize: 200 }).then(res => {
        this.accessOptions = res.rows.map(r => {
          return Object.assign({}, r, {
            outTimeStr: r.outTime ? r.outTime.substring(0, 16) : ''
          })
        })
      })
    },
    onApplyChange(applyId) {
      if (!applyId) {
        this.form.plateNumber = ''
        this.form.useType = '1'
        this.accessOptions = []
        this.form.accessId = null
        return
      }
      const apply = this.applyOptions.find(a => a.applyId === applyId)
      if (apply) {
        this.form.plateNumber = apply.plateNumber || apply.externalPlateNumber || ''
        this.form.useType = apply.useType || '1'
      }
      this.loadAccessOptions(applyId)
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.form.expenseId) {
            updateExpense(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess(this.$t('carApply.success'))
                this.dialogVisible = false
                this.getList()
              }
            })
          } else {
            addExpense(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess(this.$t('carApply.success'))
                this.dialogVisible = false
                this.getList()
              }
            })
          }
        }
      })
    },
    handleDelete(row) {
      const ids = row ? [row.expenseId] : this.ids
      this.$modal.confirm(this.$t('carApply.confirmDelete')).then(() => {
        return delExpense(ids.join(','))
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('carApply.success'))
      }).catch(() => {})
    },
    handleExport() {
      this.download('car/expense/export', { ...this.queryParams }, `expense_${new Date().getTime()}.xlsx`)
    },
    cancel() {
      this.dialogVisible = false
      this.reset()
    },
    reset() {
      this.form = {}
      this.$refs.form.resetFields()
    }
  }
}
</script>