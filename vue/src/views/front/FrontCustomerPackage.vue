<template>
  <div>
    <div class="header">
      <h1 class="page-title">{{ pageTitle }}</h1>
    </div>
    
    <!-- 添加分类选项卡 -->
    <div class="category-tabs">
      <el-tabs v-model="activeCategory" @tab-click="handleCategoryChange">
        <el-tab-pane 
          v-for="category in availableCategories" 
          :key="category.value"
          :label="category.label" 
          :name="category.value">
        </el-tab-pane>
      </el-tabs>
    </div>
    
    <div class="search-bar">
      <el-select v-model="searchType" placeholder="选择查询类型" style="width: 120px;">
        <el-option label="修复内容" value="fixContent"></el-option>
        <el-option label="开发工程师" value="developer"></el-option>
        <el-option label="测试工程师" value="tester"></el-option>
        <el-option label="测试结果" value="testResult"></el-option>
        <el-option label="镜像ID" value="imageId"></el-option>
        <el-option label="Commit ID" value="commitId"></el-option>
        <el-option label="备注" value="remarks"></el-option>
      </el-select>
      
      <!-- 普通文本输入 -->
      <el-input 
        v-if="searchType !== 'testResult'" 
        :placeholder="getSearchPlaceholder()" 
        style="width: 250px; margin-left: 10px" 
        v-model="searchKeyword">
      </el-input>
      
      <!-- 测试结果下拉选择 -->
      <el-select 
        v-if="searchType === 'testResult'" 
        v-model="searchKeyword" 
        placeholder="选择测试结果" 
        style="width: 250px; margin-left: 10px;"
        clearable>
        <el-option label="待测试" value="待测试"></el-option>
        <el-option label="测试中" value="测试中"></el-option>
        <el-option label="未通过" value="未通过"></el-option>
        <el-option label="已完成" value="已完成"></el-option>
        <el-option label="已取消" value="已取消"></el-option>
      </el-select>
      
      <el-button type="primary" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="info" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="table-container">
      <el-table 
        :data="tableData" 
        stripe 
        class="custom-table"
        :header-cell-style="headerCellStyle"
        :cell-style="cellStyle">
        <el-table-column prop="recordDate" label="时间" width="120" align="center" sortable></el-table-column>
        <el-table-column label="镜像包地址" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link 
              v-if="scope.row.packageUrl" 
              type="primary" 
              :href="scope.row.packageUrl"
              target="_blank"
              :underline="false">
              {{ scope.row.packageUrl }}
            </el-link>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="fixContent" label="修复内容" show-overflow-tooltip></el-table-column>
        <el-table-column prop="developer" label="开发工程师" width="120" align="center"></el-table-column>
        <el-table-column prop="commitId" label="Commit ID" width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="tester" label="测试工程师" width="120" align="center"></el-table-column>
        <el-table-column label="测试结果" width="120" align="center">
          <template slot-scope="scope">
            <el-tag 
              :type="getTestResultType(scope.row.testResult)"
              size="small">
              {{ scope.row.testResult || '待测试' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="imageId" label="镜像ID" width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="remarks" label="备注" show-overflow-tooltip></el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          background
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
          :current-page="pageNum"
          :page-sizes="[5, 10, 20, 50]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "FrontCustomerPackage",
  props: {
    version: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      searchType: 'fixContent', // 默认搜索修复内容
      searchKeyword: '',
      headerCellStyle: {
        background: '#f8f8f9',
        color: '#333',
        fontWeight: 'bold',
        textAlign: 'center'
      },
      cellStyle: {
        textAlign: 'center'
      },
      activeCategory: 'vmos'
    };
  },
  computed: {
    pageTitle() {
      const versionMap = {
        'a10': 'Android 10',
        'a12': 'Android 12',
        'a13': 'Android 13',
        'a14': 'Android 14',
        'a15': 'Android 15'
      };
      const versionText = versionMap[this.version] || '';
      return `客户镜像包记录 - ${versionText}`;
    },
    availableCategories() {
      // 根据不同的Android版本返回不同的分类选项，VMOS放在第一位
      const categoryMap = {
        'a10': [
          { label: 'VMOS', value: 'vmos' },
          { label: '公共包', value: 'public' },
          { label: '其他', value: 'other' }
        ],
        'a13': [
          { label: 'VMOS', value: 'vmos' },
          { label: '公共包', value: 'public' },
          { label: '上海通用', value: 'shanghai_common' },
          { label: 'QT', value: 'qt' },
          { label: '巴德', value: 'bade' },
          { label: '毕少', value: 'bishao' },
          { label: '其他', value: 'other' }
        ],
        'a14': [
          { label: 'VMOS', value: 'vmos' },
          { label: '公共包', value: 'public' },
          { label: 'QT', value: 'qt' },
          { label: '新硕', value: 'xinshuo' },
          { label: 'GeeLark', value: 'geelark' },
          { label: 'ML', value: 'ml' },
          { label: '毕少', value: 'bishao' },
          { label: '其他', value: 'other' }
        ],
        'a15': [
          { label: 'VMOS', value: 'vmos' },
          { label: '公共包', value: 'public' },
          { label: 'QT', value: 'qt' },
          { label: 'ML', value: 'ml' },
          { label: 'GeeLark', value: 'geelark' },
          { label: '其他', value: 'other' }
        ]
      };
      return categoryMap[this.version] || [];
    }
  },
  created() {
    this.load(1);
  },
  watch: {
    // 监听路由参数变化，当切换Android版本时重新加载数据
    '$route'(to, from) {
      if (to.params.version !== from.params.version) {
        // 版本变化时重置分类为默认值并重新加载数据
        this.activeCategory = 'vmos';
        this.searchType = 'fixContent';
        this.searchKeyword = '';
        this.load(1);
      }
    },
    // 监听version属性变化
    version(newVersion, oldVersion) {
      if (newVersion !== oldVersion) {
        this.activeCategory = 'vmos';
        this.searchType = 'fixContent';
        this.searchKeyword = '';
        this.load(1);
      }
    }
  },
  methods: {
    getTestResultType(testResult) {
      const typeMap = {
        '待测试': '',
        '测试中': 'warning',
        '未通过': 'danger',
        '已完成': 'success',
        '已取消': 'info'
      };
      return typeMap[testResult] || '';
    },
    getSearchPlaceholder() {
      const placeholderMap = {
        'fixContent': '请输入修复内容关键字',
        'developer': '请输入开发工程师姓名',
        'tester': '请输入测试工程师姓名',
        'imageId': '请输入镜像ID',
        'commitId': '请输入Commit ID',
        'remarks': '请输入备注关键字'
      };
      return placeholderMap[this.searchType] || '请输入关键字';
    },
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum;
      
      // 构建搜索参数
      const params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        androidVersion: this.version,
        category: this.activeCategory
      };
      
      // 根据搜索类型添加对应的搜索参数
      if (this.searchKeyword) {
        params[this.searchType] = this.searchKeyword;
      }
      
      this.$request.get('/manager/customer_package/selectPage', { params }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data?.list || [];
          this.total = res.data?.total || 0;
        } else {
          this.$message.error(res.msg || '获取数据失败');
        }
      }).catch(err => {
        console.error('获取数据失败:', err);
        this.$message.error('获取数据失败');
      });
    },
    reset() {
      this.searchType = 'fixContent';
      this.searchKeyword = '';
      this.load(1);
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum);
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.load(1);
    },
    handleCategoryChange(tab) {
      this.activeCategory = tab.name;
      this.load(1);
    }
  }
};
</script>

<style scoped>
.header {
  margin-bottom: 20px;
}
.page-title {
  font-size: 24px;
  color: #333;
  margin-bottom: 15px;
}
.category-tabs {
  margin-bottom: 20px;
}
.category-tabs :deep(.el-tabs__header) {
  margin-bottom: 0;
}
.category-tabs :deep(.el-tabs__nav-wrap::after) {
  height: 1px;
}
.search-bar {
  margin-bottom: 20px;
}
.table-container {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  overflow: hidden;
}
.custom-table {
  width: 100%;
}
.pagination {
  margin-top: 20px;
  text-align: left;
  padding: 16px 0;
}

:deep(.el-pagination) {
  justify-content: flex-start;
}

:deep(.el-pagination .btn-prev),
:deep(.el-pagination .btn-next) {
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  background: #fff;
  color: #606266;
  padding: 0 8px;
  margin: 0 2px;
}

:deep(.el-pagination .btn-prev:hover),
:deep(.el-pagination .btn-next:hover) {
  color: #409eff;
  border-color: #409eff;
}

:deep(.el-pagination .el-pager li) {
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  background: #fff;
  color: #606266;
  margin: 0 2px;
  min-width: 32px;
  height: 32px;
  line-height: 30px;
}

:deep(.el-pagination .el-pager li:hover) {
  color: #409eff;
  border-color: #409eff;
}

:deep(.el-pagination .el-pager li.active) {
  background-color: #409eff;
  border-color: #409eff;
  color: #fff;
}

:deep(.el-pagination .el-pagination__total) {
  color: #606266;
  font-weight: 500;
}

:deep(.el-pagination .el-pagination__sizes) {
  margin-left: 16px;
}

:deep(.el-pagination .el-pagination__jump) {
  margin-left: 16px;
  color: #606266;
}

/* 测试结果标签样式 */
:deep(.el-tag--warning) {
  background-color: #fdf6ec;
  border-color: #f5dab1;
  color: #e6a23c;
}

:deep(.el-tag--danger) {
  background-color: #fef0f0;
  border-color: #fbc4c4;
  color: #f56c6c;
}

:deep(.el-tag--success) {
  background-color: #f0f9ff;
  border-color: #b3d8ff;
  color: #67c23a;
}

:deep(.el-tag--info) {
  background-color: #f4f4f5;
  border-color: #d3d4d6;
  color: #909399;
}
</style> 