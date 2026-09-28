<template>
  <div :class="{'hidden':hidden}" class="pagination-container">
    <el-pagination
      :current-page.sync="currentPage"
      :page-size.sync="pageSize"
      :layout="layout"
      :page-sizes="pageSizes"
      :pager-count="pagerCount"
      :total="total"
      v-bind="$attrs"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
import { scrollTo } from '@/utils/scroll-to'

export default {
  name: 'Pagination',
  props: {
    total: {
      required: true,
      type: Number
    },
    page: {
      type: Number,
      default: 1
    },
    limit: {
      type: Number,
      default: 20
    },
    pageSizes: {
      type: Array,
      default() {
        return [10, 20, 30, 50]
      }
    },
    // 移动端页码按钮的数量端默认值5
    pagerCount: {
      type: Number,
      default: document.body.clientWidth < 992 ? 5 : 7
    },
    layout: {
      type: String,
      default: 'total, sizes, prev, pager, next, jumper'
    },
    background: {
      type: Boolean,
      default: true
    },
    autoScroll: {
      type: Boolean,
      default: true
    },
    hidden: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
    }
  },
  computed: {
    currentPage: {
      get() {
        return this.page
      },
      set(val) {
        this.$emit('update:page', val)
      }
    },
    pageSize: {
      get() {
        return this.limit
      },
      set(val) {
        this.$emit('update:limit', val)
      }
    }
  },
  methods: {
    handleSizeChange(val) {
      if (this.currentPage * val > this.total) {
        this.currentPage = 1
      }
      this.$emit('pagination', { page: this.currentPage, limit: val })
      if (this.autoScroll) {
        scrollTo(0, 800)
      }
    },
    handleCurrentChange(val) {
      this.$emit('pagination', { page: val, limit: this.pageSize })
      if (this.autoScroll) {
        scrollTo(0, 800)
      }
    }
  }
}
</script>

<style scoped>
.pagination-container {
  background: transparent;
  padding: 16px 0;
}
.pagination-container.hidden {
  display: none;
}
::v-deep .el-pagination {
  font-size: 13px;
  color: #4a5568;
}
::v-deep .el-pagination .btn-prev,
::v-deep .el-pagination .btn-next {
  border-radius: 4px;
  border: 1px solid #e8ecef;
  background: #fff;
  color: #4a5568;
  &:hover {
    color: #1a2744;
    border-color: #d1d5db;
  }
}
::v-deep .el-pagination .el-pager li {
  border-radius: 4px;
  margin: 0 2px;
  border: 1px solid #e8ecef;
  background: #fff;
  color: #4a5568;
  &:hover {
    color: #1a2744;
    border-color: #d1d5db;
  }
  &.active {
    background: #409eff;
    border-color: #409eff;
    color: #fff;
  }
}
::v-deep .el-pagination .el-pagination__total {
  color: #718096;
}
::v-deep .el-pagination .el-pagination__sizes .el-input__inner {
  border-radius: 4px;
  border: 1px solid #e8ecef;
  color: #4a5568;
}
::v-deep .el-pagination .el-pagination__jump {
  color: #718096;
  .el-input__inner {
    border-radius: 4px;
    border: 1px solid #e8ecef;
    color: #4a5568;
  }
}
</style>
