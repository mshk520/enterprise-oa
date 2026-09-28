<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" class="query-form" size="small" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item :label="$t('vehicle.plateNumber')" prop="plateNumber">
        <el-input v-model="queryParams.plateNumber" :placeholder="$t('vehicle.input') + $t('vehicle.plateNumber')" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="$t('vehicle.brand')" prop="brand">
        <el-input v-model="queryParams.brand" :placeholder="$t('vehicle.input') + $t('vehicle.brand')" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="$t('vehicle.status')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('vehicle.select')" clearable>
          <el-option v-for="dict in statusOptions" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery" v-hasPermi="['car:vehicle:list']">{{ $t('vehicle.search') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('vehicle.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['car:vehicle:add']">{{ $t('vehicle.add') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate()" v-hasPermi="['car:vehicle:edit']">{{ $t('vehicle.edit') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete()" v-hasPermi="['car:vehicle:remove']">{{ $t('vehicle.delete') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['car:vehicle:export']">{{ $t('vehicle.export') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="vehicleList" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="index" :label="$t('vehicle.index')" width="80" align="center" />
      <el-table-column :label="$t('vehicle.plateNumber')" align="center" prop="plateNumber" min-width="120">
        <template slot-scope="scope">
          <span class="plate-tag">{{ scope.row.plateNumber }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('vehicle.brand')" align="center" prop="brand" min-width="100" />
      <el-table-column :label="$t('vehicle.color')" align="center" prop="color" min-width="100" />
      <el-table-column :label="$t('vehicle.seatCount')" align="center" prop="seatCount" width="80" />
      <el-table-column :label="$t('vehicle.deptName')" align="center" prop="deptName" min-width="120" />
      <el-table-column :label="$t('vehicle.status')" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag size="small" :type="statusTagType(scope.row.status)">
            {{ statusLabel(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('vehicle.totalMileage')" align="center" prop="totalMileage" width="100" />
      <el-table-column :label="$t('vehicle.operation')" align="center" width="180" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['car:vehicle:edit']">{{ $t('vehicle.edit') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['car:vehicle:remove']">{{ $t('vehicle.delete') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item :label="$t('vehicle.plateNumber')" prop="plateNumber">
          <el-input v-model="form.plateNumber" :placeholder="$t('vehicle.input') + $t('vehicle.plateNumber')" maxlength="20" />
        </el-form-item>
        <el-form-item :label="$t('vehicle.brand')" prop="brand">
          <el-input v-model="form.brand" :placeholder="$t('vehicle.input') + $t('vehicle.brand')" maxlength="50" />
        </el-form-item>
        <el-form-item :label="$t('vehicle.color')" prop="color">
          <el-input v-model="form.color" :placeholder="$t('vehicle.input') + $t('vehicle.color')" maxlength="50" />
        </el-form-item>
        <el-form-item :label="$t('vehicle.seatCount')" prop="seatCount">
          <el-input-number v-model="form.seatCount" :min="1" :max="50" />
        </el-form-item>
        <el-form-item :label="$t('vehicle.deptName')" prop="deptId">
          <treeselect v-model="form.deptId" :options="deptOptions" :placeholder="$t('vehicle.select') + $t('vehicle.deptName')" />
        </el-form-item>
        <el-form-item :label="$t('vehicle.status')" prop="status">
          <el-select v-model="form.status" :placeholder="$t('vehicle.select') + $t('vehicle.status')">
            <el-option v-for="dict in statusOptions" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('vehicle.totalMileage')" prop="totalMileage">
          <el-input-number v-model="form.totalMileage" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item :label="$t('vehicle.remark')">
          <el-input v-model="form.remark" type="textarea" :placeholder="$t('vehicle.input') + $t('vehicle.remark')" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('vehicle.confirm') }}</el-button>
        <el-button @click="cancel">{{ $t('vehicle.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listVehicle, getVehicle, addVehicle, updateVehicle, delVehicle } from "@/api/car/vehicle"
import { deptTreeSelect } from "@/api/system/user"
import Treeselect from "@riophae/vue-treeselect"
import "@riophae/vue-treeselect/dist/vue-treeselect.css"

export default {
  name: "CarVehicle",
  components: { Treeselect },
  data() {
    return {
      loading: false,
      showSearch: true,
      total: 0,
      vehicleList: [],
      ids: [],
      single: true,
      multiple: true,
      dialogVisible: false,
      dialogTitle: '',
      deptOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        plateNumber: null,
        brand: null,
        status: null
      },
      statusOptions: [
        { value: '0', label: '空闲' },
        { value: '1', label: '已派出' },
        { value: '2', label: '维修中' },
        { value: '3', label: '已报废' }
      ],
      form: {},
      rules: {
        plateNumber: [{ required: true, message: '车牌号不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listVehicle(this.queryParams).then(response => {
        this.vehicleList = response.rows
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
      this.ids = selection.map(item => item.vehicleId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    statusTagType(status) {
      const map = { '0': 'success', '1': 'warning', '2': 'info', '3': 'danger' }
      return map[status] || 'info'
    },
    statusLabel(status) {
      const map = { '0': '空闲', '1': '已派出', '2': '维修中', '3': '已报废' }
      return map[status] || status
    },
    handleAdd() {
      this.reset()
      this.getDeptTree()
      this.dialogTitle = this.$t('vehicle.add') + this.$t('vehicle.vehicle')
      this.dialogVisible = true
    },
    handleUpdate(row) {
      this.reset()
      this.getDeptTree()
      const vehicleId = row ? row.vehicleId : this.ids[0]
      getVehicle(vehicleId).then(response => {
        this.form = response.data
        this.dialogTitle = this.$t('vehicle.edit') + this.$t('vehicle.vehicle')
        this.dialogVisible = true
      })
    },
    getDeptTree() {
      deptTreeSelect().then(response => {
        this.deptOptions = response.data
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.vehicleId != null) {
            updateVehicle(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('vehicle.success'))
              this.dialogVisible = false
              this.getList()
            })
          } else {
            addVehicle(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('vehicle.success'))
              this.dialogVisible = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const vehicleIds = row ? [row.vehicleId] : this.ids
      this.$modal.confirm(this.$t('vehicle.confirmDelete')).then(() => {
        return delVehicle(vehicleIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('vehicle.success'))
      }).catch(() => {})
    },
    handleExport() {
      this.download('car/vehicle/export', { ...this.queryParams }, `车辆数据_${new Date().getTime()}.xlsx`)
    },
    reset() {
      this.form = {
        vehicleId: null,
        plateNumber: null,
        brand: null,
        color: null,
        seatCount: 5,
        deptId: null,
        status: '0',
        totalMileage: 0,
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
</style>
