<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item :label="$t('reportManage.yearFrom')" prop="yMFrom">
        <el-select v-model="queryParams.yMFrom" :placeholder="$t('reportManage.selectYear')" clearable style="width: 150px;">
          <el-option v-for="year in yearOptions" :key="year" :label="year" :value="year" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('reportManage.yearTo')" prop="yMTo">
        <el-select v-model="queryParams.yMTo" :placeholder="$t('reportManage.selectYear')" clearable style="width: 150px;">
          <el-option v-for="year in yearOptions" :key="year" :label="year" :value="year" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery" v-hasPermi="['mtg:report:list']">{{ $t('reportManage.search') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('reportManage.reset') }}</el-button>
      </el-form-item>
      <el-form-item style="float: right;">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['mtg:report:export']">{{ $t('reportManage.export') }}</el-button>
      </el-form-item>
    </el-form>

    <div v-if="summaryList.length > 0" class="summary-bar">
      <span class="summary-title">{{ $t('reportManage.summary') }}</span>
      <span class="summary-content">
        <span v-for="item in summaryList" :key="item.year" class="summary-item">
          <span class="summary-year">{{ item.year }}:</span>
          <span class="summary-qty">{{ Number(item.totalQty).toLocaleString() }}</span>
        </span>
      </span>
    </div>

    <el-table v-loading="loading" :data="reportList" border :header-cell-style="{ background: '#f5f7fa', color: '#1a2744', fontWeight: '600' }">
      <el-table-column type="index" :label="$t('reportManage.index')" width="80" align="center" />
      <el-table-column :label="$t('reportManage.year')" align="center" prop="year" width="80">
        <template slot-scope="scope">
          <span class="year-tag">{{ scope.row.year }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('reportManage.month')" align="center" prop="month" width="80" />
      <el-table-column :label="$t('reportManage.customerName')" align="left" prop="customerId" min-width="160">
        <template slot-scope="scope">
          <span class="customer-name">{{ scope.row.customerId }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('reportManage.gauge')" align="center" prop="gauge" width="100" />
      <el-table-column :label="$t('reportManage.orderQty')" align="center" prop="orderQty" width="100">
        <template slot-scope="scope">
          <span class="order-qty">{{ scope.row.orderQty }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('reportManage.producingCompany')" align="center" prop="producingCompany" width="120">
        <template slot-scope="scope">
          <el-tag size="small" :type="scope.row.producingCompany === 'CN' ? 'success' : 'info'">
            {{ scope.row.producingCompany }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import { listChinaOrder, summaryChinaOrder } from "@/api/mtg/report"

export default {
  name: "RptChinaOrder",
  data() {
    return {
      loading: false,
      showSearch: true,
      total: 0,
      reportList: [],
      summaryList: [],
      yearOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        yMFrom: '',
        yMTo: ''
      }
    }
  },
  created() {
    this.initYearOptions()
    this.getList()
  },
  methods: {
    initYearOptions() {
      const currentYear = new Date().getFullYear()
      this.yearOptions = []
      for (let y = currentYear - 10; y <= currentYear + 3; y++) {
        this.yearOptions.push(String(y))
      }
      this.queryParams.yMFrom = String(currentYear)
      this.queryParams.yMTo = String(currentYear)
    },
    getList() {
      if (!this.queryParams.yMFrom || !this.queryParams.yMTo) {
        this.$modal.msgWarning(this.$t('reportManage.selectYear'))
        return
      }
      this.loading = true
      listChinaOrder(this.queryParams).then(response => {
        this.reportList = response.rows
        this.total = response.total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
      this.getSummary()
    },
    getSummary() {
      summaryChinaOrder(this.queryParams.yMFrom, this.queryParams.yMTo).then(response => {
        this.summaryList = response.data
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.initYearOptions()
      this.handleQuery()
    },
    handleExport() {
      if (!this.queryParams.yMFrom || !this.queryParams.yMTo) {
        this.$modal.msgWarning(this.$t('reportManage.selectYear'))
        return
      }
      this.download('mtg/report/chinaOrder/export', { yMFrom: this.queryParams.yMFrom, yMTo: this.queryParams.yMTo }, `${this.$t('reportManage.chinaOrder')}_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped>
.summary-bar {
  display: flex;
  align-items: flex-start;
  padding: 6px 16px;
  margin-bottom: 12px;
  background: #f5f7fa;
  border: 2px solid #c0c4cc;
  border-radius: 4px;
  font-size: 13px;
}
.summary-title {
  font-weight: 600;
  font-size: 14px;
  color: #1a2744;
  white-space: nowrap;
  margin-right: 16px;
  line-height: 28px;
}
.summary-content {
  display: flex;
  flex-wrap: wrap;
  gap: 8px 10px;
  align-items: center;
}
.summary-item {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 12px;
  border-radius: 3px;
  font-size: 13px;
}
.summary-year {
  color: #2c5282;
  font-weight: 500;
}
.summary-qty {
  color: #1a2744;
  font-weight: 700;
}
.year-tag {
  display: inline-block;
  padding: 2px 8px;
  background: #edf2f7;
  color: #2c5282;
  border-radius: 3px;
  font-size: 12px;
  font-weight: 500;
}
.customer-name {
  color: #1a2744;
  font-weight: 500;
}
.order-qty {
  display: inline-block;
  padding: 2px 8px;
  background: #ebf8ff;
  color: #2b6cb0;
  border-radius: 3px;
  font-size: 13px;
}
</style>
