<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item :label="$t('driverManage.driverName')" prop="driverName">
        <el-input v-model="queryParams.driverName" :placeholder="$t('driverManage.placeholder.driverName')" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="$t('driverManage.phone')" prop="phone">
        <el-input v-model="queryParams.phone" :placeholder="$t('driverManage.placeholder.phone')" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="$t('driverManage.status')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('driverManage.placeholder.selectStatus')" clearable>
          <el-option :label="$t('driverManage.onDuty')" value="0" />
          <el-option :label="$t('driverManage.onLeave')" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('driverManage.search') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('driverManage.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['car:driver:add']">{{ $t('driverManage.add') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" @click="handleUpdate" :disabled="single" v-hasPermi="['car:driver:edit']">{{ $t('driverManage.edit') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete" :disabled="multiple" v-hasPermi="['car:driver:remove']">{{ $t('driverManage.delete') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['car:driver:export']">{{ $t('driverManage.export') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="driverList" @selection-change="handleSelectionChange" border>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('driverManage.driverId')" align="center" prop="driverId" width="80" />
      <el-table-column :label="$t('driverManage.driverName')" align="center" prop="driverName" min-width="120" />
      <el-table-column :label="$t('driverManage.phone')" align="center" prop="phone" width="140" />
      <el-table-column :label="$t('driverManage.status')" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '0' ? 'success' : 'info'">
            {{ scope.row.status === '0' ? $t('driverManage.onDuty') : $t('driverManage.onLeave') }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('driverManage.remark')" align="center" prop="remark" width="200" show-overflow-tooltip />
      <el-table-column :label="$t('driverManage.createTime')" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('driverManage.action')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['car:driver:edit']">{{ $t('driverManage.edit') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['car:driver:remove']">{{ $t('driverManage.delete') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item :label="$t('driverManage.driverName')" prop="driverName">
          <el-input v-model="form.driverName" :placeholder="$t('driverManage.placeholder.driverName')" />
        </el-form-item>
        <el-form-item :label="$t('driverManage.phone')" prop="phone">
          <el-input v-model="form.phone" :placeholder="$t('driverManage.placeholder.phone')" maxlength="20" />
        </el-form-item>
        <el-form-item :label="$t('driverManage.status')" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">{{ $t('driverManage.onDuty') }}</el-radio>
            <el-radio label="1">{{ $t('driverManage.onLeave') }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t('driverManage.remark')" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" :placeholder="$t('driverManage.placeholder.remark')" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('driverManage.confirm') }}</el-button>
        <el-button @click="open = false">{{ $t('driverManage.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDriver, getDriver, addDriver, updateDriver, delDriver, exportDriver } from "@/api/car/driver";

export default {
  name: "Driver",
  data() {
    return {
      loading: false,
      showSearch: true,
      ids: [],
      single: true,
      multiple: true,
      total: 0,
      driverList: [],
      title: "",
      open: false,
      form: {},
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        driverName: undefined,
        phone: undefined,
        status: undefined
      },
      rules: {
        driverName: [
          { required: true, message: this.$t('driverManage.validation.driverName'), trigger: "blur" }
        ],
        phone: [
          { required: true, message: this.$t('driverManage.validation.phone'), trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listDriver(this.queryParams).then(response => {
        this.driverList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = this.$t('driverManage.addTitle');
    },
    handleUpdate(row) {
      this.reset();
      const driverId = row.driverId || this.ids[0];
      getDriver(driverId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = this.$t('driverManage.editTitle');
      });
    },
    reset() {
      this.form = {
        driverId: null,
        driverName: "",
        phone: "",
        status: "0",
        remark: ""
      };
    },
    handleDelete(row) {
      const driverIds = row.driverId || this.ids;
      this.$modal.confirm(this.$t('driverManage.confirmDelete', { id: driverIds })).then(function() {
        return delDriver(driverIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('driverManage.deleted'));
      }).catch(() => {});
    },
    handleExport() {
      this.download('car/driver/export', {
        ...this.queryParams
      }, `driver_${new Date().getTime()}.xlsx`)
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.driverId != null) {
            updateDriver(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess(this.$t('driverManage.edited'));
                this.open = false;
                this.getList();
              }
            });
          } else {
            addDriver(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess(this.$t('driverManage.added'));
                this.open = false;
                this.getList();
              }
            });
          }
        }
      });
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.driverId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    }
  }
};
</script>
