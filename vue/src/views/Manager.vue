<template>
  <div class="manager-container" :class="{ 'dark-mode': isDarkMode }">
    <!--  头部  -->
    <div class="manager-header">
      <div class="manager-header-left">
        <img src="@/assets/imgs/logo.png" />
        <div class="title">Iass组管理后台</div>
      </div>

      <div class="manager-header-center">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{ path: '/' }">前台</el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: $route.path }">{{ $route.meta.name }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <div class="manager-header-right">
        <el-switch
          v-model="isDarkMode"
          active-color="#409EFF"
          inactive-color="#909399"
          active-icon-class="el-icon-sunny"
          inactive-icon-class="el-icon-moon"
          @change="toggleDarkMode"
          style="margin-left: auto;">
        </el-switch>
        <el-dropdown @command="handleCommand">
          <span class="el-dropdown-link">
            <el-avatar :size="32" :src="avatarUrl" style="margin-right: 8px;"></el-avatar>
            {{ user?.name }}<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="adminPerson">个人信息</el-dropdown-item>
            <el-dropdown-item command="password">修改密码</el-dropdown-item>
            <el-dropdown-item command="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>

    <!--  主体  -->
    <div class="manager-main">
      <!--  侧边栏  -->
      <div class="manager-main-left">
        <el-menu 
          router 
          class="manager-menu" 
          :default-active="$route.path"
          :collapse="false"
          :unique-opened="false"
          background-color="#f5f7fa"
          text-color="#303133"
          active-text-color="#409EFF">
          <el-menu-item index="/manager/home" class="home-menu-item">
            <i class="el-icon-s-home"></i>
            <span slot="title">系统首页</span>
            <el-badge :value="newRecordCount" :hidden="newRecordCount === 0" class="record-badge">
            </el-badge>
          </el-menu-item>
          <!-- <el-menu-item index="/manager/notice">
            <i class="el-icon-document"></i>
            <span slot="title">镜像包更新记录</span>
          </el-menu-item> -->

          <!-- 客户镜像包记录 -->
          <el-submenu index="customer-packages">
            <template slot="title">
              <i class="el-icon-box"></i>
              <span>客户镜像包记录</span>
            </template>

            <!-- All Version -->
            <el-menu-item index="/manager/customer/all">
              <i class="el-icon-s-grid"></i>
              <span>All Version</span>
            </el-menu-item>

            <!-- Android 10 -->
            <el-menu-item index="/manager/customer/a10">
              <i class="el-icon-cpu"></i>
              <span>Android 10</span>
            </el-menu-item>

            <!-- Android 13 -->
            <el-menu-item index="/manager/customer/a13">
              <i class="el-icon-monitor"></i>
              <span>Android 13</span>
            </el-menu-item>

            <!-- Android 14 -->
            <el-menu-item index="/manager/customer/a14">
              <i class="el-icon-connection"></i>
              <span>Android 14</span>
            </el-menu-item>

            <!-- Android 15 -->
            <el-menu-item index="/manager/customer/a15">
              <i class="el-icon-data-analysis"></i>
              <span>Android 15</span>
            </el-menu-item>
          </el-submenu>

          <!-- Android系统版本 -->
          <el-submenu index="android-versions">
            <template slot="title">
              <i class="el-icon-mobile-phone"></i>
              <span>Android系统属性</span>
            </template>
            <el-menu-item index="/manager/a10">
              <i class="el-icon-arrow-right"></i>
              <span>Android 10</span>
            </el-menu-item>
            <el-menu-item index="/manager/a13">
              <i class="el-icon-arrow-right"></i>
              <span>Android 13</span>
            </el-menu-item>
            <el-menu-item index="/manager/a14">
              <i class="el-icon-arrow-right"></i>
              <span>Android 14</span>
            </el-menu-item>
            <el-menu-item index="/manager/a15">
              <i class="el-icon-arrow-right"></i>
              <span>Android 15</span>
            </el-menu-item>
          </el-submenu>

          <el-submenu index="system">
            <template slot="title">
              <i class="el-icon-setting"></i>
              <span>系统管理</span>
            </template>
            <el-menu-item index="/manager/admin">
              <i class="el-icon-user"></i>
              <span>管理员信息</span>
            </el-menu-item>
            <el-menu-item index="/manager/adminPerson">
              <i class="el-icon-user-solid"></i>
              <span>个人信息</span>
            </el-menu-item>
            <el-menu-item index="/manager/password">
              <i class="el-icon-lock"></i>
              <span>修改密码</span>
            </el-menu-item>
          </el-submenu>

          
        </el-menu>
      </div>

      <!--  数据表格  -->
      <div class="manager-main-right">
        <router-view @update:user="updateUser" />
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "Manager",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      isDarkMode: false,
      newRecordCount: 0, // 新记录计数
      lastRecordCount: 0 // 上次记录总数
    }
  },
  computed: {
    avatarUrl() {
      console.log('Current user avatar:', this.user.avatar);
      let url = this.user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png';
      
      // 将localhost替换为实际IP地址
      if (url.includes('localhost')) {
        url = url.replace('localhost', '192.168.120.104');
      }
      
      console.log('Using avatar URL:', url);
      return url;
    }
  },
  created() {
    // 获取当前管理员信息
    this.loadCurrentUser()
    const darkMode = localStorage.getItem('dark-mode')
    if (darkMode === 'true') {
      this.isDarkMode = true
      document.documentElement.classList.add('dark')
    }
    
    // 检查是否已经提示过缩放（只在后台显示一次）
    const hasShownZoomTip = localStorage.getItem('zoom-tip-shown-manager')
    if (!hasShownZoomTip) {
      // 延迟显示，确保页面完全加载
      setTimeout(() => {
        this.showZoomTip()
      }, 500)
    }
    
    // 初始化记录计数
    this.initRecordCount()
    // 监听新记录添加事件（实时更新）
    this.listenForNewRecords()
    // 定期检查新记录（仅用于多用户环境和容错）
    this.startRecordCountCheck()
  },
  methods: {
    loadCurrentUser() {
      // 从localStorage中获取当前用户信息
      const user = JSON.parse(localStorage.getItem('xm-user') || '{}')
      if (user.id) {
        this.user = user
      } else {
        this.$message.warning('请先登录')
        this.$router.push('/login')
      }
    },
    updateUser() {
      // 重新获取用户信息并强制更新视图
      const newUser = JSON.parse(localStorage.getItem('xm-user') || '{}');
      console.log('Updating user with:', newUser);
      this.user = { ...newUser };  // 使用对象展开运算符创建新对象，确保触发响应式更新
    },
    handleCommand(command) {
      if (command === 'logout') {
        this.$confirm('确认退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          localStorage.removeItem('xm-user')
          localStorage.removeItem('xm-token')
          this.$router.push('/login')
          this.$message.success('退出成功')
        }).catch(() => {
          // 用户取消退出登录，不做任何操作，也不显示错误
          console.log('用户取消退出登录')
        })
      } else {
        this.$router.push('/manager/' + command)
      }
    },
    toggleDarkMode(val) {
      if (val) {
        document.documentElement.classList.add('dark')
        localStorage.setItem('dark-mode', 'true')
      } else {
        document.documentElement.classList.remove('dark')
        localStorage.setItem('dark-mode', 'false')
      }
    },
    showZoomTip() {
      this.$alert('为了更好的浏览体验，建议将浏览器缩放设置为75%（按Ctrl和-键）', '页面显示提示', {
        confirmButtonText: '我知道了',
        type: 'info',
        center: true,
        showClose: false,
        closeOnClickModal: false,
        closeOnPressEscape: false,
        customClass: 'zoom-tip-dialog'
      }).then(() => {
        // 用户点击确定后标记已显示
        localStorage.setItem('zoom-tip-shown-manager', 'true')
      }).catch(() => {
        // 即使出错也标记已显示
        localStorage.setItem('zoom-tip-shown-manager', 'true')
      })
      
      // 3秒后自动关闭
      setTimeout(() => {
        // 查找并关闭MessageBox
        const messageBox = document.querySelector('.el-message-box__wrapper')
        if (messageBox) {
          const closeBtn = messageBox.querySelector('.el-message-box__btns .el-button')
          if (closeBtn) {
            closeBtn.click()
          }
        }
        localStorage.setItem('zoom-tip-shown-manager', 'true')
      }, 3000)
    },
    // 初始化记录计数
    async initRecordCount() {
      try {
        const res = await this.$request.get('/manager/customer_package/selectPage', {
          params: { pageNum: 1, pageSize: 1 }
        });
        if (res.code === '200') {
          this.lastRecordCount = res.data?.total || 0;
          // 从localStorage获取上次保存的计数
          const savedCount = localStorage.getItem('last-record-count');
          if (savedCount) {
            const saved = parseInt(savedCount);
            if (this.lastRecordCount > saved) {
              this.newRecordCount = this.lastRecordCount - saved;
            }
          }
          // 保存当前计数
          localStorage.setItem('last-record-count', this.lastRecordCount.toString());
        }
      } catch (error) {
        console.error('初始化记录计数失败:', error);
      }
    },
    // 开始定期检查新记录（仅用于多用户环境和容错）
    startRecordCountCheck() {
      // 每5分钟检查一次新记录（大幅降低频率，仅作为兜底机制）
      setInterval(async () => {
        try {
          const res = await this.$request.get('/manager/customer_package/selectPage', {
            params: { pageNum: 1, pageSize: 1 }
          });
          if (res.code === '200') {
            const currentTotal = res.data?.total || 0;
            if (currentTotal > this.lastRecordCount) {
              // 有新记录（可能是其他用户添加的或事件丢失）
              this.newRecordCount += (currentTotal - this.lastRecordCount);
              this.lastRecordCount = currentTotal;
              localStorage.setItem('last-record-count', this.lastRecordCount.toString());
              console.log('定期检查发现新记录（可能来自其他用户）:', this.newRecordCount);
            }
          }
        } catch (error) {
          console.error('检查新记录失败:', error);
        }
      }, 300000); // 5分钟检查一次
    },
    // 重置新记录计数（当用户点击系统首页时调用）
    resetRecordCount() {
      this.newRecordCount = 0;
      localStorage.setItem('last-record-count', this.lastRecordCount.toString());
    },
    // 监听新记录添加事件
    listenForNewRecords() {
      // 监听全局事件
      this.$root.$on('new-record-added', () => {
        console.log('收到新记录添加事件');
        this.handleNewRecordAdded();
      });
    },
    // 处理新记录添加
    async handleNewRecordAdded() {
      try {
        // 立即检查最新的记录总数
        const res = await this.$request.get('/manager/customer_package/selectPage', {
          params: { pageNum: 1, pageSize: 1 }
        });
        if (res.code === '200') {
          const currentTotal = res.data?.total || 0;
          if (currentTotal > this.lastRecordCount) {
            // 有新记录，立即更新计数
            this.newRecordCount += (currentTotal - this.lastRecordCount);
            this.lastRecordCount = currentTotal;
            localStorage.setItem('last-record-count', this.lastRecordCount.toString());
            console.log('立即更新消息计数:', this.newRecordCount);
          }
        }
      } catch (error) {
        console.error('处理新记录添加失败:', error);
      }
    }
  },
  // 监听路由变化
  watch: {
    '$route'(to, from) {
      // 当用户访问系统首页时，重置消息计数
      if (to.path === '/manager/home') {
        this.resetRecordCount();
             }
     }
   },
   beforeDestroy() {
     // 清理事件监听器
     this.$root.$off('new-record-added');
   }
 }
</script>

<style scoped>
.manager-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.manager-header {
  height: 60px;
  line-height: 60px;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  justify-content: space-between;
  padding: 0 20px;
  background-color: #fff;
}

.manager-header-left {
  display: flex;
  align-items: center;
}

.manager-header-left img {
  height: 40px;
  margin-right: 10px;
}

.manager-header-left .title {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
}

.manager-header-center {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.manager-header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.el-dropdown-link {
  cursor: pointer;
  color: #409EFF;
  display: flex;
  align-items: center;
  gap: 8px;
}

.manager-main {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.manager-main-left {
  width: 200px;
  background-color: #f5f7fa;
  border-right: 1px solid #e6e6e6;
  overflow-y: auto;
}

.manager-main-right {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: #fff;
}

/* 菜单样式 */
.manager-container :deep(.manager-menu) {
  border: none !important;
  background-color: #f5f7fa !important;
}

.manager-container :deep(.manager-menu .el-menu-item),
.manager-container :deep(.manager-menu .el-submenu__title) {
  padding-left: 20px !important;
  background-color: #f5f7fa !important;
  color: #303133 !important;
}

.manager-container :deep(.manager-menu .el-menu-item.is-active),
.manager-container :deep(.manager-menu .el-submenu__title.is-active) {
  background-color: #ecf5ff !important;
  color: #409EFF !important;
}

.manager-container :deep(.manager-menu .el-menu-item:hover),
.manager-container :deep(.manager-menu .el-submenu__title:hover) {
  background-color: #ecf5ff !important;
}

/* 暗黑模式样式 */
.dark-mode.manager-container {
  background-color: #1f1f1f;
  color: #e5eaf3;
}

.dark-mode .manager-header {
  background-color: #2d2d2d;
  border-bottom-color: #3d3d3d;
}

.dark-mode .manager-header-left .title {
  color: #e5eaf3;
}

.dark-mode .manager-main-left {
  background-color: #2d2d2d;
  border-right-color: #3d3d3d;
}

.dark-mode .manager-main-right {
  background-color: #1f1f1f;
}

/* 暗黑模式下的菜单样式 */
.dark-mode.manager-container :deep(.manager-menu) {
  background-color: #2d2d2d !important;
}

.dark-mode.manager-container :deep(.manager-menu .el-menu-item),
.dark-mode.manager-container :deep(.manager-menu .el-submenu__title) {
  background-color: #2d2d2d !important;
  color: #e5eaf3 !important;
}

.dark-mode.manager-container :deep(.manager-menu .el-menu-item.is-active),
.dark-mode.manager-container :deep(.manager-menu .el-submenu__title.is-active) {
  background-color: #0d0d0d !important;
  color: #409EFF !important;
}

.dark-mode.manager-container :deep(.manager-menu .el-menu-item:hover),
.dark-mode.manager-container :deep(.manager-menu .el-submenu__title:hover) {
  background-color: #0d0d0d !important;
}

/* 子菜单样式 */
.manager-container :deep(.el-menu--inline) {
  background-color: #f5f7fa !important;
}

.dark-mode.manager-container :deep(.el-menu--inline) {
  background-color: #2d2d2d !important;
}

/* 下拉菜单样式 */
.el-dropdown-link {
  font-size: 14px;
  padding: 0 10px;
  height: 40px;
  line-height: 40px;
  border-radius: 4px;
  transition: all 0.3s;
}

.el-dropdown-link:hover {
  background-color: #f5f7fa;
}

.dark-mode .el-dropdown-link {
  color: #e5eaf3;
}

.dark-mode .el-dropdown-link:hover {
  background-color: #3d3d3d;
}

.el-avatar {
  border: 2px solid #fff;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.dark-mode .el-avatar {
  border-color: #2d2d2d;
}

/* 缩放提示弹窗样式 */
:deep(.zoom-tip-dialog) {
  top: 20% !important;
  transform: translateY(0) !important;
}

:deep(.zoom-tip-dialog .el-message-box) {
  margin-top: 0 !important;
  padding: 30px 40px;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

:deep(.zoom-tip-dialog .el-message-box__header) {
  padding-bottom: 20px;
}

:deep(.zoom-tip-dialog .el-message-box__title) {
  font-size: 18px;
  font-weight: 600;
  color: #409EFF;
}

:deep(.zoom-tip-dialog .el-message-box__content) {
  padding-bottom: 20px;
}

:deep(.zoom-tip-dialog .el-message-box__message) {
  font-size: 16px;
  line-height: 1.5;
  color: #606266;
}

:deep(.zoom-tip-dialog .el-message-box__btns) {
  text-align: center;
}

:deep(.zoom-tip-dialog .el-button--primary) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  padding: 12px 30px;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
}

:deep(.zoom-tip-dialog .el-button--primary:hover) {
  background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%);
}

/* 消息徽章样式 */
.home-menu-item {
  position: relative;
}

.record-badge {
  position: absolute;
  right: 15px;
  top: 50%;
  transform: translateY(-50%);
  z-index: 10;
}

.record-badge :deep(.el-badge__content) {
  background-color: #f56c6c;
  border: 1px solid #fff;
  font-size: 10px;
  height: 16px;
  line-height: 14px;
  padding: 0 4px;
  min-width: 16px;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.3);
}

/* 暗黑模式下的徽章样式 */
.dark-mode .record-badge :deep(.el-badge__content) {
  border-color: #2d2d2d;
}
</style>