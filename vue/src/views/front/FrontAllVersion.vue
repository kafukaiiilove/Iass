<template>
  <div>
    <div class="header">
      <div class="page-title">客户镜像包记录 - All Version</div>
      
      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-select v-model="searchType" placeholder="选择查询类型" style="width: 120px;">
          <el-option label="Android版本" value="androidVersion"></el-option>
          <el-option label="分类" value="category"></el-option>
          <el-option label="时间" value="recordDate"></el-option>
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
          v-if="!['testResult', 'androidVersion', 'category', 'recordDate'].includes(searchType)" 
          :placeholder="getSearchPlaceholder()" 
          style="width: 250px; margin-left: 10px" 
          v-model="searchKeyword">
        </el-input>
        
        <!-- 时间范围选择 -->
        <el-date-picker
          v-if="searchType === 'recordDate'"
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
          style="width: 400px; margin-left: 10px;"
          clearable>
        </el-date-picker>
        
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
        
        <!-- Android版本下拉选择 -->
        <el-select 
          v-if="searchType === 'androidVersion'" 
          v-model="searchKeyword" 
          placeholder="选择Android版本" 
          style="width: 250px; margin-left: 10px;"
          clearable>
          <el-option label="Android 10" value="a10"></el-option>
          <el-option label="Android 13" value="a13"></el-option>
          <el-option label="Android 14" value="a14"></el-option>
          <el-option label="Android 15" value="a15"></el-option>
        </el-select>
        
        <!-- 分类下拉选择 -->
        <el-select 
          v-if="searchType === 'category'" 
          v-model="searchKeyword" 
          placeholder="选择分类" 
          style="width: 250px; margin-left: 10px;"
          clearable>
          <el-option label="VMOS" value="vmos"></el-option>
          <el-option label="公共包" value="public"></el-option>
          <el-option label="上海通用" value="shanghai_common"></el-option>
          <el-option label="QT" value="qt"></el-option>
          <el-option label="巴德" value="bade"></el-option>
          <el-option label="毕少" value="bishao"></el-option>
          <el-option label="新硕" value="xinshuo"></el-option>
          <el-option label="GeeLark" value="geelark"></el-option>
          <el-option label="ML" value="ml"></el-option>
          <el-option label="其他" value="other"></el-option>
        </el-select>
        
        <el-button type="primary" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
        <el-button type="info" plain style="margin-left: 10px" @click="reset">重置</el-button>
      </div>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table :data="tableData" stripe class="custom-table">
        <el-table-column prop="androidVersion" label="Android版本" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getVersionTagType(scope.row.androidVersion)" size="small">
              {{ getVersionText(scope.row.androidVersion) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="分类" width="100" align="center">
          <template slot-scope="scope">
            <el-tag size="small" type="info">{{ getCategoryText(scope.row.category) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="recordDate" label="时间" width="130" align="center">
          <template slot-scope="scope">
            {{ formatDateTimeDisplay(scope.row.recordDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="packageUrl" label="镜像包地址" min-width="200">
          <template slot-scope="scope">
            <div v-html="formatContentWithImagePreview(scope.row.packageUrl)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="fixContent" label="修复内容" min-width="200">
          <template slot-scope="scope">
            <div v-html="formatContentWithImagePreview(scope.row.fixContent)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="imageId" label="镜像ID" width="120" align="center">
          <template slot-scope="scope">
            {{ scope.row.imageId || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="developer" label="开发工程师" width="100" align="center"></el-table-column>
        <el-table-column prop="commitId" label="Commit ID" width="100" align="center"></el-table-column>
        <el-table-column prop="tester" label="测试工程师" width="100" align="center"></el-table-column>
        <el-table-column prop="testResult" label="测试结果" width="100" align="center">
          <template slot-scope="scope">
            <el-tag 
              :type="scope.row.testResult === '已完成' ? 'success' : 
                     scope.row.testResult === '测试中' ? 'warning' : 
                     scope.row.testResult === '未通过' ? 'danger' : 
                     scope.row.testResult === '已取消' ? 'info' : ''"
              size="small">
              {{ scope.row.testResult }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" label="备注" min-width="150">
          <template slot-scope="scope">
            <div v-html="formatContentWithImagePreview(scope.row.remarks)"></div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[5, 10, 20, 50]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "FrontAllVersion",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      searchType: 'fixContent',
      searchKeyword: '',
      dateRange: [],
    }
  },
  created() {
    this.load(1);
  },
  methods: {
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum;
      
      const params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };
      
      if (this.searchType === 'recordDate' && this.dateRange && this.dateRange.length === 2) {
        params.startDate = this.dateRange[0];
        params.endDate = this.dateRange[1];
      } else if (this.searchKeyword) {
        params[this.searchType] = this.searchKeyword;
      }
      
      this.$request.get('/manager/customer_package/selectPage', { params }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data?.list || [];
          this.total = res.data?.total || 0;
        } else {
          this.$message({
            message: res.msg || '获取数据失败',
            type: 'error',
            duration: 2000
          });
        }
      }).catch(err => {
        console.error('获取数据失败:', err);
        this.$message({
          message: '获取数据失败',
          type: 'error',
          duration: 2000
        });
      });
    },
    reset() {
      this.searchType = 'fixContent';
      this.searchKeyword = '';
      this.dateRange = [];
      this.load(1);
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum);
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.load(1);
    },
    getVersionText(version) {
      const versionMap = {
        'a10': 'Android 10',
        'a13': 'Android 13',
        'a14': 'Android 14',
        'a15': 'Android 15'
      };
      return versionMap[version] || version;
    },
    getVersionTagType(version) {
      const typeMap = {
        'a10': 'primary',
        'a13': 'success',
        'a14': 'warning',
        'a15': 'danger'
      };
      return typeMap[version] || '';
    },
    getCategoryText(category) {
      const categoryMap = {
        'shanghai_common': '上海通用',
        'public': '公共包',
        'vmos': 'VMOS',
        'qt': 'QT',
        'bade': '巴德',
        'bishao': '毕少',
        'xinshuo': '新硕',
        'geelark': 'GeeLark',
        'ml': 'ML',
        'other': '其他'
      };
      return categoryMap[category] || category;
    },
    formatDateTimeDisplay(dateTime) {
      if (!dateTime) return '';
      if (dateTime.length === 16 && dateTime.includes(' ') && !dateTime.includes(':')) {
        return dateTime;
      }
      if (dateTime.includes(':') && dateTime.split(':').length === 3) {
        return dateTime.substring(0, 16);
      }
      return dateTime;
    },
    formatContentWithImagePreview(content) {
      if (!content) return '';
      
      let formattedContent = content.replace(/localhost/g, '192.168.120.104');
      
      const imagePattern = /(https?:\/\/[^\s<>"]+?\.(jpg|jpeg|png|gif|bmp|webp))/gi;
      
      formattedContent = formattedContent.replace(imagePattern, (match) => {
        return `<a href="${match}" target="_blank" style="color: #409EFF; text-decoration: none;">
                  <img src="${match}" style="max-width: 100px; max-height: 60px; margin: 2px; border-radius: 4px; cursor: pointer;" 
                       onmouseover="this.style.transform='scale(1.1)'" 
                       onmouseout="this.style.transform='scale(1)'"
                       onerror="this.style.display='none'; this.nextSibling.style.display='inline';">
                  <span style="display: none; color: #409EFF;">[图片预览]</span>
                </a>`;
      });
      
      const urlPattern = /(https?:\/\/[^\s<>"]+)/gi;
      formattedContent = formattedContent.replace(urlPattern, (match) => {
        if (match.match(/\.(jpg|jpeg|png|gif|bmp|webp)$/i)) {
          return match;
        }
        return `<a href="${match}" target="_blank" style="color: #409EFF; text-decoration: underline;">${match}</a>`;
      });
      
      return formattedContent;
    },
    getSearchPlaceholder() {
      const placeholderMap = {
        'fixContent': '请输入修复内容',
        'developer': '请输入开发工程师',
        'tester': '请输入测试工程师',
        'imageId': '请输入镜像ID',
        'commitId': '请输入Commit ID',
        'remarks': '请输入备注'
      };
      return placeholderMap[this.searchType] || '请输入搜索内容';
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
</style> 