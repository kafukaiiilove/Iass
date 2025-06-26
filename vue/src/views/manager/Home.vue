<template>
  <div class="home-container">
    <!-- <div class="welcome-card">
       您好，{{ user?.name }}！欢迎使用本系统 -->
    <!-- </div>  -->

    <div class="content-wrapper">
      <div class="update-card">
        <div class="card-title">客户镜像包记录</div>
        <div class="timeline-container">
          <el-timeline>
            <el-timeline-item v-for="item in packageRecords" :key="item.id" :timestamp="formatDateTime(item.recordDate)">
              <div class="timeline-item">
                <div class="item-title">
                  <span class="emoji">{{ getAndroidEmoji(getVersionText(item.androidVersion)) }}</span>
                  {{ getVersionText(item.androidVersion) }} - {{ getCategoryText(item.category) }}
                </div>
                <div class="item-content">
                  <div class="record-info">
                    <div class="info-row">
                      <span class="label">开发工程师：</span>
                      <span class="value">{{ item.developer }}</span>
                    </div>
                    <div class="info-row" v-if="item.tester">
                      <span class="label">测试工程师：</span>
                      <span class="value">{{ item.tester }}</span>
                    </div>
                    <div class="info-row" v-if="item.testResult">
                      <span class="label">测试结果：</span>
                      <el-tag 
                        :type="getTestResultType(item.testResult)"
                        size="mini">
                        {{ item.testResult }}
                      </el-tag>
                    </div>
                    <div class="info-row" v-if="item.fixContent">
                      <span class="label">修复内容：</span>
                      <span class="value">{{ item.fixContent }}</span>
                    </div>
                    <div class="info-row" v-if="item.packageUrl">
                      <span class="label">镜像包地址：</span>
                      <a :href="item.packageUrl" target="_blank" class="package-link">{{ item.packageUrl }}</a>
                    </div>
                  </div>
                </div>
              </div>
            </el-timeline-item>
          </el-timeline>
          
          <!-- 分页组件 -->
          <div class="pagination-container" v-if="total > 0">
            <el-pagination
              background
              @current-change="handleCurrentChange"
              @size-change="handleSizeChange"
              :current-page="currentPage"
              :page-sizes="[5, 10, 20, 30]"
              :page-size="pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total">
            </el-pagination>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      packageRecords: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      androidEmojis: {
        'Android 10': '🔟',
        'Android 11': '🔥',
        'Android 12': '⭐',
        'Android 13': '🚀',
        'Android 14': '💎',
        'Android 15': '🌟'
      },
      // Android版本映射
      versionMap: {
        'a10': 'Android 10',
        'a11': 'Android 11', 
        'a12': 'Android 12',
        'a13': 'Android 13',
        'a14': 'Android 14',
        'a15': 'Android 15'
      },
      // 分类映射
      categoryMap: {
        'public': '公共包',
        'vmos': 'VMOS',
        'qt': 'QT',
        'bade': '巴德',
        'bishao': '毕少',
        'xinshuo': '新硕',
        'geelark': 'GeeLark',
        'ml': 'ML',
        'other': '其他'
      }
    }
  },
  created() {
    console.log('【Home组件】组件创建，开始加载数据');
    this.load()
  },
  methods: {
    getAndroidEmoji(androidVersion) {
      return this.androidEmojis[androidVersion] || '📱';
    },
    getVersionText(version) {
      return this.versionMap[version] || version;
    },
    getCategoryText(category) {
      return this.categoryMap[category] || category;
    },
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
    load() {
      console.log('【Home组件】开始请求客户镜像包记录数据，页码：', this.currentPage);
      
      const params = {
        pageNum: this.currentPage,
        pageSize: this.pageSize
      };
      
      this.$request.get('/manager/customer_package/selectPage', { params }).then(res => {
        console.log('【Home组件】获取客户镜像包记录数据成功:', res);
        if (res.code === '200') {
          this.packageRecords = res.data.list || [];
          this.total = res.data.total || 0;
          console.log('【Home组件】获取到记录数：', this.packageRecords.length, '总数：', this.total);
        } else {
          console.error('【Home组件】获取客户镜像包记录数据失败:', res.msg);
          this.$message.error(res.msg || '获取客户镜像包记录数据失败')
        }
      }).catch(err => {
        console.error('【Home组件】获取客户镜像包记录数据异常:', err);
        this.$message.error('获取客户镜像包记录数据失败')
      })
    },
    handleCurrentChange(page) {
      console.log('【Home组件】切换页码：', page);
      this.currentPage = page;
      this.load();
    },
    handleSizeChange(pageSize) {
      console.log('【Home组件】切换页面大小：', pageSize);
      this.pageSize = pageSize;
      this.currentPage = 1; // 重置到第一页
      this.load();
    },
    formatDateTime(dateTime) {
      if (!dateTime) return '';
      const date = new Date(dateTime);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}`;
    }
  }
}
</script>

<style scoped>
.home-container {
  min-height: calc(100vh - 100px);
  background: linear-gradient(135deg, #fff9e6 0%, #fff5d6 100%);
  padding: 20px;
}

.welcome-card {
  background: rgba(255, 255, 255, 0.9);
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  margin-bottom: 20px;
  font-size: 18px;
  color: #333;
}

.content-wrapper {
  display: flex;
  gap: 20px;
  background: linear-gradient(135deg, #fff9e6 0%, #fff5d6 100%);
  padding: 20px;
  border-radius: 12px;
}

.update-card {
  background: rgba(255, 255, 255, 0.9);
  padding: 25px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  width: 100%;
}

.card-title {
  font-size: 22px;
  font-weight: bold;
  color: #333;
  margin-bottom: 30px;
  padding-bottom: 15px;
  border-bottom: 2px solid #f0f0f0;
}

.timeline-container {
  padding: 10px;
}

.timeline-item {
  cursor: default;
  padding: 8px;
  border-radius: 8px;
  transition: all 0.3s;
}

.timeline-item:hover {
  background: rgba(255, 255, 255, 0.8);
}

.item-title {
  font-weight: bold;
  font-size: 18px;
  color: #333;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
}

.item-content {
  font-size: 16px;
  color: #666;
  line-height: 1.6;
  padding: 8px 0;
  text-align: left;
}

.record-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.info-row {
  display: flex;
  align-items: flex-start;
  gap: 8px;
}

.label {
  font-weight: bold;
  color: #333;
  min-width: 100px;
  flex-shrink: 0;
}

.value {
  color: #666;
  word-break: break-all;
}

.package-link {
  color: #409EFF;
  text-decoration: none;
  word-break: break-all;
}

.package-link:hover {
  text-decoration: underline;
}

.pagination-container {
  margin-top: 30px;
  text-align: left;
  padding: 20px 0;
  border-top: 1px solid #f0f0f0;
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
  background: #409eff;
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

:deep(.el-timeline-item__node) {
  background-color: #e6a23c;
}

:deep(.el-timeline-item__tail) {
  border-left: 2px solid #e6a23c;
}

:deep(.el-timeline-item__timestamp) {
  color: #909399;
  font-size: 13px;
}

/* 暗黑模式适配 */
:deep(.dark-mode) .home-container {
  background: linear-gradient(135deg, #2d2d2d 0%, #1f1f1f 100%);
}

:deep(.dark-mode) .welcome-card,
:deep(.dark-mode) .update-card {
  background: rgba(45, 45, 45, 0.9);
  color: #e5eaf3;
}

:deep(.dark-mode) .card-title {
  color: #e5eaf3;
  border-bottom-color: #3d3d3d;
}

:deep(.dark-mode) .item-title {
  color: #e5eaf3;
}

:deep(.dark-mode) .label {
  color: #e5eaf3;
}

:deep(.dark-mode) .value {
  color: #a8abb2;
}

:deep(.dark-mode) .timeline-item:hover {
  background: rgba(45, 45, 45, 0.8);
}

:deep(.dark-mode) .pagination-container {
  border-top-color: #3d3d3d;
}

.emoji {
  margin-right: 8px;
  font-size: 16px;
}
</style>
