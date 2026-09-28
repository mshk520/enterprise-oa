<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item :label="$t('roomManage.roomName')" prop="roomName">
        <el-input v-model="queryParams.roomName" :placeholder="$t('roomManage.placeholder.roomName')" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="$t('roomManage.floor')" prop="floor">
        <el-input v-model="queryParams.floor" :placeholder="$t('roomManage.placeholder.floor')" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="$t('roomManage.status')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('roomManage.placeholder.selectStatus')" clearable>
          <el-option :label="$t('roomManage.enabledStatus')" value="0" />
          <el-option :label="$t('roomManage.disabledStatus')" value="1" />
          <el-option :label="$t('roomManage.maintaining')" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('roomManage.search') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('roomManage.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['mtg:room:add']">{{ $t('roomManage.add') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" @click="handleUpdate" :disabled="single" v-hasPermi="['mtg:room:edit']">{{ $t('roomManage.edit') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete" :disabled="multiple" v-hasPermi="['mtg:room:remove']">{{ $t('roomManage.delete') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['mtg:room:export']">{{ $t('roomManage.export') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="roomList" @selection-change="handleSelectionChange" border>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('roomManage.roomName')" align="center" prop="roomName" min-width="120">
        <template slot-scope="scope">
          {{ $i18n.locale === 'en' && scope.row.roomNameEn ? scope.row.roomNameEn : scope.row.roomName }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('roomManage.floor')" align="center" prop="floor" width="80">
        <template slot-scope="scope">
          {{ $i18n.locale === 'en' && scope.row.floorEn ? scope.row.floorEn : scope.row.floor }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('roomManage.capacity')" align="center" prop="capacity" width="80">
        <template slot-scope="scope">
          <span class="capacity-tag">{{ scope.row.capacity }}{{ $t('roomManage.people') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('roomManage.visibility')" align="center" prop="visibilityScopeText" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.visibilityScope === '0' ? 'info' : (scope.row.visibilityScope === '1' ? 'warning' : 'success')" size="small">
            {{ scope.row.visibilityScope === '0' ? $t('roomManage.allVisible') : (scope.row.visibilityScope === '1' ? $t('roomManage.deptOnly') : $t('roomManage.companyOnly')) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('roomManage.devices')" align="center" prop="deviceList" min-width="120">
        <template slot-scope="scope">
          <template v-if="scope.row.deviceList && scope.row.deviceList.length > 0">
            <el-tag v-for="device in scope.row.deviceList" :key="device.deviceId || device.id || device.deviceName" size="small" style="margin: 2px;">
              {{ $i18n.locale === 'en' && device.deviceNameEn ? device.deviceNameEn : (device.deviceName || device.name || '') }}
            </el-tag>
          </template>
          <span v-else style="color: #c0c4cc;">-</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('roomManage.approval')" align="center" prop="needApprovalText" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.needApproval === '1' ? 'danger' : 'success'" size="small">
            {{ scope.row.needApproval === '1' ? $t('roomManage.needApproval') : $t('roomManage.noApproval') }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('roomManage.status')" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '0' ? 'success' : (scope.row.status === '1' ? 'danger' : 'warning')">
            {{ scope.row.status === '0' ? $t('roomManage.enabledStatus') : (scope.row.status === '1' ? $t('roomManage.disable') : $t('roomManage.maintain')) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('roomManage.createTime')" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('roomManage.action')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['mtg:room:edit']">{{ $t('roomManage.edit') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['mtg:room:remove']">{{ $t('roomManage.delete') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-switch" @click="handleChangeStatus(scope.row)" v-hasPermi="['mtg:room:status']">
            {{ scope.row.status === '0' ? $t('roomManage.disable') : $t('roomManage.enable') }}
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-setting" @click="handleMaintain(scope.row)" v-hasPermi="['mtg:room:maintain']">
            {{ scope.row.status === '2' ? $t('roomManage.cancelMaintain') : $t('roomManage.maintain') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-form-item :label="$t('roomManage.roomName')" prop="roomName">
          <el-input v-model="form.roomName" :placeholder="$t('roomManage.placeholder.roomName')" />
        </el-form-item>
        <el-form-item :label="$t('roomManage.roomNameEn')">
          <el-input v-model="form.roomNameEn" placeholder="Enter room name in English" />
        </el-form-item>
        <el-form-item :label="$t('roomManage.floor')" prop="floor">
          <el-input v-model="form.floor" :placeholder="$t('roomManage.placeholder.floor')" />
        </el-form-item>
        <el-form-item :label="$t('roomManage.floorEn')">
          <el-input v-model="form.floorEn" placeholder="Enter floor in English" />
        </el-form-item>
        <el-form-item :label="$t('roomManage.capacity')" prop="capacity">
          <el-input-number v-model="form.capacity" :min="1" :max="999" />
          <span class="unit">{{ $t('roomManage.people') }}</span>
        </el-form-item>
        <el-form-item :label="$t('roomManage.bufferTime')" prop="bufferTime">
          <el-input-number v-model="form.bufferTime" :min="0" :max="60" :placeholder="$t('roomManage.unitMin')" />
          <span class="unit">{{ $t('roomManage.unitMin') }}</span>
        </el-form-item>
        <el-form-item :label="$t('roomManage.visibility')" prop="visibilityScope">
          <el-radio-group v-model="form.visibilityScope">
            <el-radio label="0">{{ $t('roomManage.allVisible') }}</el-radio>
            <el-radio label="1">{{ $t('roomManage.deptOnly') }}</el-radio>
            <el-radio label="2">{{ $t('roomManage.companyOnly') }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t('roomManage.approval')" prop="needApproval">
          <el-switch v-model="form.needApproval" active-value="1" inactive-value="0" />
        </el-form-item>
        <el-form-item :label="$t('roomManage.status')" prop="status">
          <el-switch v-model="form.status" active-value="0" inactive-value="1" />
        </el-form-item>
        <el-form-item :label="$t('roomManage.deviceConfig')" prop="deviceIds">
          <el-checkbox-group v-model="form.deviceIds">
            <el-checkbox
              v-for="device in deviceOptions.filter(d => d)"
              :key="device.deviceId || device.id || device.deviceName"
              :label="device.deviceId || device.id || device.deviceName || ''"
              class="device-item"
            >{{ $i18n.locale === 'en' && device.deviceNameEn ? device.deviceNameEn : (device.deviceName || device.name || '') }}</el-checkbox>
          </el-checkbox-group>
          <div v-if="deviceOptions.length === 0" class="empty-tip">
            {{ $t('roomManage.noDevice') }}
          </div>
        </el-form-item>
        <el-form-item :label="$t('roomManage.remark')" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" :placeholder="$t('roomManage.placeholder.remark')" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('roomManage.confirm') }}</el-button>
        <el-button @click="open = false">{{ $t('roomManage.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRoom, getRoom, addRoom, updateRoom, delRoom, changeRoomStatus, maintainRoom } from "@/api/mtg/room";
import { listEnabledDevice } from "@/api/mtg/device";

export default {
  name: "Room",
  data() {
    return {
      loading: false,
      showSearch: true,
      ids: [],
      single: true,
      multiple: true,
      total: 0,
      roomList: [],
      title: "",
      open: false,
      form: {
        deviceIds: []
      },
      deviceOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        roomName: undefined,
        floor: undefined,
        status: undefined
      },
      rules: {
        roomName: [
          { required: true, message: this.$t('roomManage.validation.roomName'), trigger: "blur" }
        ],
        capacity: [
          { required: true, message: this.$t('roomManage.validation.capacity'), trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getDeviceOptions();
  },
  methods: {
    getList() {
      this.loading = true;
      listRoom(this.queryParams).then(response => {
        this.roomList = (response.rows || []).filter(d => d && (d.roomId || d.roomName));
        this.total = response.total;
        this.loading = false;
      });
    },
    getDeviceOptions() {
      // 從設備表動態載入所有啟用狀態的設備（無需權限）
      listEnabledDevice().then(response => {
        console.log('設備接口返回:', response);
        // 過濾掉 null/undefined 項，確保每個設備都有必要屬性
        this.deviceOptions = (response.data || []).filter(d => d && (d.deviceId || d.id || d.deviceName || d.name));
        console.log('設備列表載入完成:', this.deviceOptions);
      }).catch(error => {
        console.error('載入設備列表失敗:', error);
        this.deviceOptions = [];
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
      this.open = true;
      this.title = this.$t('roomManage.addTitle');
      this.form = {
        roomId: null,
        roomName: "",
        floor: "",
        capacity: 10,
        bufferTime: 0,
        visibilityScope: "0",
        needApproval: "0",
        deviceIds: [],
        status: "0",
        remark: ""
      };
      this.getDeviceOptions();
    },
    handleUpdate(row) {
      this.open = true;
      this.title = this.$t('roomManage.editTitle');
      const roomId = row.roomId || this.ids[0];
      getRoom(roomId).then(response => {
        this.form = Object.assign({ deviceIds: [] }, response.data);
        if (!Array.isArray(this.form.deviceIds)) {
          this.form.deviceIds = [];
        }
      });
      this.getDeviceOptions();
    },
    handleDelete(row) {
      const roomIds = row.roomId || this.ids;
      this.$modal.confirm(this.$t('roomManage.confirmDelete', { id: roomIds })).then(function() {
        return delRoom(roomIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('roomManage.deleted'));
      }).catch(() => {});
    },
    handleChangeStatus(row) {
      const text = row.status === "0" ? this.$t('roomManage.disable') : this.$t('roomManage.enable');
      this.$modal.confirm(this.$t('roomManage.confirmStatus', { text })).then(function() {
        return changeRoomStatus({ roomId: row.roomId, status: row.status === "0" ? "1" : "0" });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('roomManage.edited'));
      }).catch(() => {});
    },
    handleMaintain(row) {
      const text = row.status === "2" ? this.$t('roomManage.cancelMaintain') : this.$t('roomManage.maintain');
      this.$modal.confirm(this.$t('roomManage.confirmMaintain', { text })).then(function() {
        return maintainRoom({ roomId: row.roomId, status: row.status === "2" ? "0" : "2" });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('roomManage.edited'));
      }).catch(() => {});
    },
    handleExport() {
      this.download('mtg/room/export', {
        ...this.queryParams
      }, `room_${new Date().getTime()}.xlsx`)
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.roomId != null) {
            updateRoom(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess(this.$t('roomManage.edited'));
                this.open = false;
                this.getList();
              }
            });
          } else {
            addRoom(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess(this.$t('roomManage.added'));
                this.open = false;
                this.getList();
              }
            });
          }
        }
      });
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.roomId);
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
  font-size: 14px;
}

.field-tip {
  color: #718096;
  font-size: 12px;
  line-height: 1.4;
  margin-top: 4px;
}

.device-item {
  margin-right: 24px;
  margin-bottom: 8px;
}

.empty-tip {
  color: #718096;
  font-size: 12px;
  margin-top: 4px;
}

.capacity-tag {
  display: inline-block;
  padding: 2px 8px;
  background: #edf2f7;
  color: #2c5282;
  border-radius: 3px;
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
