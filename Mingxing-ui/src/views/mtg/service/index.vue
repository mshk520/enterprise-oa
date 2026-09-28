<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item :label="$t('serviceManage.serviceName')" prop="serviceName">
        <el-input v-model="queryParams.serviceName" :placeholder="$t('serviceManage.placeholder.serviceName')" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="$t('serviceManage.status')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('serviceManage.placeholder.selectStatus')" clearable>
          <el-option :label="$t('serviceManage.normal')" value="0" />
          <el-option :label="$t('serviceManage.disabled')" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('serviceManage.search') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('serviceManage.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['mtg:service:add']">{{ $t('serviceManage.add') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" @click="handleUpdate" :disabled="single" v-hasPermi="['mtg:service:edit']">{{ $t('serviceManage.edit') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete" :disabled="multiple" v-hasPermi="['mtg:service:remove']">{{ $t('serviceManage.delete') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['mtg:service:export']">{{ $t('serviceManage.export') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="serviceList" @selection-change="handleSelectionChange" border>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('serviceManage.serviceName')" align="center" prop="serviceName" min-width="120">
        <template slot-scope="scope">
          {{ $i18n.locale === 'en' && scope.row.serviceNameEn ? scope.row.serviceNameEn : scope.row.serviceName }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('serviceManage.status')" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'">
            {{ scope.row.status === '0' ? $t('serviceManage.normal') : $t('serviceManage.disabled') }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('serviceManage.emailNotify')" align="center" prop="emailNotify" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.emailNotify === '1' ? 'warning' : 'info'" size="small">
            {{ scope.row.emailNotify === '1' ? $t('serviceManage.emailNotifyYes') : $t('serviceManage.emailNotifyNo') }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('serviceManage.remark')" align="center" prop="remark" width="200" show-overflow-tooltip />
      <el-table-column :label="$t('serviceManage.createTime')" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('serviceManage.action')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['mtg:service:edit']">{{ $t('serviceManage.edit') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['mtg:service:remove']">{{ $t('serviceManage.delete') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-switch" @click="handleChangeStatus(scope.row)" v-hasPermi="['mtg:service:edit']">
            {{ scope.row.status === '0' ? $t('serviceManage.disable') : $t('serviceManage.enable') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

      <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-form-item :label="$t('serviceManage.serviceName')" prop="serviceName">
          <el-input v-model="form.serviceName" :placeholder="$t('serviceManage.placeholder.serviceName')" />
        </el-form-item>
        <el-form-item :label="$t('serviceManage.serviceNameEn')">
          <el-input v-model="form.serviceNameEn" placeholder="Enter service name in English" />
        </el-form-item>
        <el-form-item :label="$t('serviceManage.status')" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">{{ $t('serviceManage.normal') }}</el-radio>
            <el-radio label="1">{{ $t('serviceManage.disabled') }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t('serviceManage.emailNotify')" prop="emailNotify">
          <el-radio-group v-model="form.emailNotify">
            <el-radio label="1">{{ $t('serviceManage.emailNotifyYes') }}</el-radio>
            <el-radio label="0">{{ $t('serviceManage.emailNotifyNo') }}</el-radio>
          </el-radio-group>
          <div class="tip">{{ $t('serviceManage.emailNotifyTip') }}</div>
        </el-form-item>
        <el-form-item :label="$t('serviceManage.remark')" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" :placeholder="$t('serviceManage.placeholder.remark')" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('serviceManage.confirm') }}</el-button>
        <el-button @click="open = false">{{ $t('serviceManage.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listService, getService, addService, updateService, delService, changeServiceStatus } from "@/api/mtg/service";

export default {
  name: "Service",
  data() {
    return {
      loading: false,
      showSearch: true,
      ids: [],
      single: true,
      multiple: true,
      total: 0,
      serviceList: [],
      title: "",
      open: false,
      form: {},
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        serviceName: undefined,
        status: undefined
      },
      rules: {
        serviceName: [
          { required: true, message: this.$t('serviceManage.validation.serviceName'), trigger: "blur" }
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
      listService(this.queryParams).then(response => {
        this.serviceList = (response.rows || []).filter(d => d && (d.serviceId || d.serviceName));
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
      this.title = this.$t('serviceManage.addTitle');
    },
    handleUpdate(row) {
      this.reset();
      const serviceId = row.serviceId || this.ids[0];
      getService(serviceId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = this.$t('serviceManage.editTitle');
      });
    },
    reset() {
      this.form = {
        serviceId: null,
        serviceName: "",
        serviceNameEn: "",
        status: "0",
        emailNotify: "0",
        remark: ""
      };
    },
    handleDelete(row) {
      const serviceIds = row.serviceId || this.ids;
      this.$modal.confirm(this.$t('serviceManage.confirmDelete')).then(function() {
        return delService(serviceIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('serviceManage.deleted'));
      }).catch(() => {});
    },
    handleChangeStatus(row) {
      const text = row.status === "0" ? this.$t('serviceManage.disable') : this.$t('serviceManage.enable');
      this.$modal.confirm(this.$t('serviceManage.confirmStatus', { text: text })).then(function() {
        return changeServiceStatus({ serviceId: row.serviceId, status: row.status === "0" ? "1" : "0" });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('serviceManage.edited'));
      }).catch(() => {});
    },
    handleExport() {
      this.download('mtg/service/export', {
        ...this.queryParams
      }, `service_${new Date().getTime()}.xlsx`)
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.serviceId != null) {
            updateService(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess(this.$t('serviceManage.edited'));
                this.open = false;
                this.getList();
              }
            });
          } else {
            addService(this.form).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess(this.$t('serviceManage.added'));
                this.open = false;
                this.getList();
              }
            });
          }
        }
      });
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.serviceId);
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
.tip {
  color: #718096;
  font-size: 12px;
  line-height: 1.5;
  margin-top: 4px;
}
.el-table {
  border-radius: 4px;
  border: 1px solid #e8ecef;
}
</style>