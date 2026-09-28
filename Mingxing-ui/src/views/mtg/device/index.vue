<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item :label="$t('deviceManage.deviceName')" prop="deviceName">
        <el-input v-model="queryParams.deviceName" :placeholder="$t('deviceManage.placeholder.deviceName')" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="$t('deviceManage.status')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('deviceManage.placeholder.selectStatus')" clearable>
          <el-option :label="$t('deviceManage.normal')" value="0" />
          <el-option :label="$t('deviceManage.disabled')" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('deviceManage.search') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('deviceManage.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['mtg:device:add']">{{ $t('deviceManage.add') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" @click="handleUpdate" :disabled="single" v-hasPermi="['mtg:device:edit']">{{ $t('deviceManage.edit') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete" :disabled="multiple" v-hasPermi="['mtg:device:remove']">{{ $t('deviceManage.delete') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['mtg:device:export']">{{ $t('deviceManage.export') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="filteredDeviceList" @selection-change="handleSelectionChange" border>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('deviceManage.deviceId')" align="center" prop="deviceId" width="80" />
      <el-table-column :label="$t('deviceManage.deviceName')" align="center" min-width="120">
          <template slot-scope="scope">
            {{ $i18n.locale === 'en' && scope.row.deviceNameEn ? scope.row.deviceNameEn : (scope.row.deviceName || '') }}
          </template>
        </el-table-column>
      <el-table-column :label="$t('deviceManage.status')" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'">
            {{ scope.row.status === '0' ? $t('deviceManage.normal') : $t('deviceManage.disabled') }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('deviceManage.remark')" align="center" prop="remark" width="200" show-overflow-tooltip />
      <el-table-column :label="$t('deviceManage.createTime')" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('deviceManage.action')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['mtg:device:edit']">{{ $t('deviceManage.edit') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['mtg:device:remove']">{{ $t('deviceManage.delete') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-switch" @click="handleChangeStatus(scope.row)" v-hasPermi="['mtg:device:status']">
            {{ scope.row.status === '0' ? $t('deviceManage.disable') : $t('deviceManage.enable') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-form-item :label="$t('deviceManage.deviceName')" prop="deviceName">
          <el-input v-model="form.deviceName" :placeholder="$t('deviceManage.placeholder.deviceName')" />
        </el-form-item>
        <el-form-item :label="$t('deviceManage.deviceNameEn')">
          <el-input v-model="form.deviceNameEn" placeholder="Enter device name in English" />
        </el-form-item>
        <el-form-item :label="$t('deviceManage.status')" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">{{ $t('deviceManage.normal') }}</el-radio>
            <el-radio label="1">{{ $t('deviceManage.disabled') }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t('deviceManage.remark')" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" :placeholder="$t('deviceManage.placeholder.remark')" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('deviceManage.confirm') }}</el-button>
        <el-button @click="open = false">{{ $t('deviceManage.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDevice, getDevice, addDevice, updateDevice, delDevice, changeDeviceStatus } from "@/api/mtg/device";

export default {
  name: "Device",
  data() {
    return {
      loading: false,
      showSearch: true,
      ids: [],
      single: true,
      multiple: true,
      total: 0,
      deviceList: [],
      title: "",
      open: false,
      form: {},
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deviceName: undefined,
        status: undefined
      },
      rules: {
        deviceName: [
          { required: true, message: this.$t('deviceManage.validation.deviceName'), trigger: "blur" }
        ]
      }
    };
  },
  computed: {
    filteredDeviceList() {
      return (this.deviceList || []).filter(d => d && (d.deviceId || d.deviceName));
    }
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listDevice(this.queryParams).then(response => {
        this.deviceList = response.rows || [];
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
      this.title = this.$t('deviceManage.addTitle');
    },
    handleUpdate(row) {
      this.reset();
      const deviceId = row.deviceId || this.ids[0];
      getDevice(deviceId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = this.$t('deviceManage.editTitle');
      });
    },
    reset() {
      this.form = {
        deviceId: null,
        deviceName: "",
        deviceNameEn: "",
        status: "0",
        remark: ""
      };
    },
    handleDelete(row) {
      const deviceIds = row.deviceId || this.ids;
      this.$modal.confirm(this.$t('deviceManage.confirmDelete', { id: deviceIds })).then(function() {
        return delDevice(deviceIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('deviceManage.deleted'));
      }).catch(() => {});
    },
    handleChangeStatus(row) {
      const text = row.status === "0" ? this.$t('deviceManage.disable') : this.$t('deviceManage.enable');
      this.$modal.confirm(this.$t('deviceManage.confirmStatus', { text: text })).then(function() {
        return changeDeviceStatus({ deviceId: row.deviceId, status: row.status === "0" ? "1" : "0" });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('deviceManage.edited'));
      }).catch(() => {});
    },
    handleExport() {
      this.download('mtg/device/export', {
        ...this.queryParams
      }, `device_${new Date().getTime()}.xlsx`)
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.deviceId != null) {
            updateDevice(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess(this.$t('deviceManage.edited'));
                this.open = false;
                this.getList();
              }
            });
          } else {
            addDevice(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess(this.$t('deviceManage.added'));
                this.open = false;
                this.getList();
              }
            });
          }
        }
      });
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.deviceId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    }
  }
};
</script>

<style scoped>
.unit {
  margin-left: 10px;
  color: #718096;
  font-size: 12px;
}

.el-table {
  border-radius: 4px;
  border: 1px solid #e8ecef;
}

.el-dialog__body {
  padding: 24px;
}

.el-form-item {
  margin-bottom: 20px;
}

.el-form-item__label {
  font-weight: 500;
  color: #1a2744;
}
</style>