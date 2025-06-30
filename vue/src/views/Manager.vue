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
            <div class="user-info">
              <div class="user-name">{{ user?.name }}</div>
              <div class="user-role">{{ roleDisplayName }}</div>
            </div>
            <i class="el-icon-arrow-down el-icon--right"></i>
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
      <div class="manager-main-left" :class="{ 'collapsed': isCollapsed }" @mouseenter="handleSidebarHover" @mouseleave="handleSidebarLeave">
        <!-- 收缩/展开按钮 -->
        <div class="sidebar-toggle" @click="toggleSidebar">
          <div class="toggle-icon">
            <div class="arrow-wrapper" :class="{ 'collapsed': isCollapsed }">
              <div class="arrow arrow-left"></div>
              <div class="arrow arrow-right"></div>
            </div>
          </div>
        </div>
        
        <el-menu 
          router 
          class="manager-menu" 
          :key="`menu-${menuKey}-${isCollapsed}`"
          :default-active="$route.path"
          :collapse="isCollapsed"
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
            <!-- 管理员信息：仅管理员可见 -->
            <el-menu-item index="/manager/admin" v-if="isAdminUser">
              <i class="el-icon-user"></i>
              <span>管理员信息</span>
            </el-menu-item>
            <!-- 个人信息和修改密码：所有用户可见 -->
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
import Vue from 'vue'
import { hasSystemPermission, getCurrentUserRole, getRoleDisplayName } from '@/utils/permissions'

export default {
  name: "Manager",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      isDarkMode: false,
      newRecordCount: 0, // 新记录计数
      lastRecordCount: 0, // 上次记录总数
      isCollapsed: false, // 侧边栏收缩状态
      recordCheckInterval: null, // 定时器引用
      menuKey: Date.now() // 菜单组件的唯一key
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
    },
    // 权限相关计算属性
    hasSystemPermission() {
      return hasSystemPermission();
    },
    // 检查是否为管理员（用于管理员信息菜单项）
    isAdminUser() {
      return this.user.role === 'ADMIN';
    },
    currentUserRole() {
      return this.user.role || getCurrentUserRole()
    },
    roleDisplayName() {
      return getRoleDisplayName(this.currentUserRole)
    }
  },
  created() {
    // 先设置全局错误处理，在其他逻辑之前
    this.setupGlobalErrorHandler()
    
    // 设置Vue错误处理器
    Vue.config.errorHandler = (err, vm, info) => {
      if (err.message && (
          err.message.includes('setAttribute') ||
          err.message.includes('Cannot read properties of null') ||
          err.message.includes('removeTabindex') ||
          err.message.includes('clickoutside')
      )) {
        // 静默处理Element UI的相关错误
        console.log('Caught and ignored Element UI error:', err.message);
        return;
      }
      console.error('Vue Error:', err, info);
    };
    
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
    
    // 恢复侧边栏收缩状态
    const sidebarCollapsed = localStorage.getItem('sidebar-collapsed')
    if (sidebarCollapsed === 'true') {
      this.isCollapsed = true
    }
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
      this.user = { ...newUser };  // 使用对象展开运算符创建新对象，确保触发响应式更新
      
      // 强制更新菜单组件，确保权限变化能立即生效
      this.menuKey = Date.now();
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
      this.recordCheckInterval = setInterval(async () => {
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
      }, 900000); // 15分钟检查一次
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
    },
    // 切换侧边栏收缩状态
    toggleSidebar() {
      // 先更新状态
      this.isCollapsed = !this.isCollapsed;
      
      // 保存状态到localStorage
      localStorage.setItem('sidebar-collapsed', this.isCollapsed.toString());
      
      // 立即更新菜单key并强制重新渲染，避免Element UI内部状态问题
      this.menuKey = Date.now();
      
      // 同步更新，减少视觉延迟
      this.$forceUpdate();
    },
    // 鼠标悬停在收缩的侧边栏上
    handleSidebarHover() {
      if (this.isCollapsed) {
        // 可以在这里添加悬停时的额外效果
        this.showExpandHint = true;
      }
    },
    // 鼠标离开收缩的侧边栏
    handleSidebarLeave() {
      if (this.isCollapsed) {
        this.showExpandHint = false;
      }
    },
    // 设置全局错误处理
    setupGlobalErrorHandler() {
      // 1. 重写console.error过滤Element UI错误
      const originalConsoleError = console.error;
      console.error = (...args) => {
        const errorMessage = args[0] && args[0].toString ? args[0].toString() : '';
        
        // 过滤Element UI相关错误
        if (errorMessage.includes('setAttribute') ||
            errorMessage.includes('removeTabindex') ||
            errorMessage.includes('Cannot read properties of null') ||
            errorMessage.includes('clickoutside') ||
            args.some(arg => arg && arg.message && (
              arg.message.includes('setAttribute') ||
              arg.message.includes('removeTabindex') ||
              arg.message.includes('Cannot read properties of null')
            ))) {
          // 静默处理这些错误
          return;
        }
        
        // 其他错误正常输出
        originalConsoleError.apply(console, args);
      };
      
      // 2. 全局错误事件监听
      window.addEventListener('error', (event) => {
        if (event.error && event.error.message) {
          const message = event.error.message;
          if (message.includes('setAttribute') ||
              message.includes('removeTabindex') ||
              message.includes('Cannot read properties of null') ||
              message.includes('clickoutside')) {
            event.preventDefault();
            event.stopPropagation();
            return false;
          }
        }
      }, true);
      
      // 3. 未处理的Promise拒绝
      window.addEventListener('unhandledrejection', (event) => {
        if (event.reason && event.reason.message) {
          const message = event.reason.message;
          if (message.includes('setAttribute') ||
              message.includes('removeTabindex') ||
              message.includes('Cannot read properties of null')) {
            event.preventDefault();
            return false;
          }
        }
      });
      
      // 4. 重写Element UI的问题方法
      this.$nextTick(() => {
        // 确保Element UI组件已经加载
        try {
          // 找到所有Element UI组件并添加错误保护
          const elComponents = document.querySelectorAll('[class*="el-"]');
          elComponents.forEach(el => {
            // 保护setAttribute方法
            if (el && typeof el.setAttribute === 'function') {
              const originalSetAttribute = el.setAttribute;
              el.setAttribute = function(name, value) {
                try {
                  if (this && this.nodeType === Node.ELEMENT_NODE) {
                    return originalSetAttribute.call(this, name, value);
                  }
                } catch (error) {
                  // 静默处理setAttribute错误
                  console.log('Protected setAttribute error:', error.message);
                }
              };
            }
          });
        } catch (error) {
          // 保护代码本身的错误
          console.log('Error protection setup failed:', error.message);
        }
      });
    },
    // 安全的文档点击处理
    handleDocumentClick(event) {
      // 这个方法用于安全地处理文档点击事件
      // 确保不会操作已销毁的DOM元素
      try {
        if (event.target && event.target.nodeType === Node.ELEMENT_NODE) {
          // 只有当目标是有效的DOM元素时才处理
        }
      } catch (error) {
        // 忽略DOM操作错误
        console.log('Safely ignored DOM operation error');
      }
    },
    // 安全的菜单重置方法
    safeMenuReset() {
      try {
        // 重置菜单key
        this.menuKey = Date.now();
        
        // 清理可能的Element UI内部引用
        this.$nextTick(() => {
          const menuElement = this.$el.querySelector('.manager-menu');
          if (menuElement && menuElement.__vue__) {
            // 清理Vue实例引用
            try {
              if (menuElement.__vue__.$el) {
                menuElement.__vue__.$el.removeAttribute('tabindex');
              }
            } catch (error) {
              // 静默处理清理错误
            }
          }
        });
      } catch (error) {
        console.log('Safe menu reset error:', error.message);
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
    
    // 清理可能的定时器
    if (this.recordCheckInterval) {
      clearInterval(this.recordCheckInterval);
      this.recordCheckInterval = null;
    }
    
    // 移除全局点击事件监听器
    document.removeEventListener('click', this.handleDocumentClick);
    
    // 清理Element UI组件
    try {
      // 安全地清理菜单组件
      this.safeMenuReset();
    } catch (error) {
      // 静默处理清理错误
    }
    
    // 清理可能的DOM引用
    this.$nextTick(() => {
      try {
        const menuElement = this.$el && this.$el.querySelector('.manager-menu');
        if (menuElement) {
          // 移除所有事件监听器
          menuElement.removeEventListener('click', this.handleDocumentClick);
        }
      } catch (error) {
        // 静默处理清理错误
      }
    });
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

.user-info {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-right: 8px;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
  line-height: 1.2;
}

.user-role {
  font-size: 12px;
  color: #909399;
  line-height: 1.2;
  margin-top: 2px;
}

.dark-mode .user-role {
  color: #b0b0b0;
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
  transition: width 0.15s ease, box-shadow 0.15s ease;
  position: relative;
}

.manager-main-left.collapsed {
  width: 64px;
}

.sidebar-toggle {
  position: absolute;
  right: -14px;
  top: 20px;
  width: 28px;
  height: 28px;
  background: #ffffff;
  border: 1px solid #e1e4e8;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 1000;
  box-shadow: 0 2px 8px rgba(0,0,0,0.15);
  transition: all 0.2s ease;
}

.sidebar-toggle:hover {
  background: #f8f9fa;
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(0,0,0,0.2);
}

.toggle-icon {
  width: 14px;
  height: 14px;
  position: relative;
}

.arrow-wrapper {
  width: 100%;
  height: 100%;
  position: relative;
  transition: transform 0.2s ease;
}

.arrow-wrapper.collapsed {
  transform: rotate(180deg);
}

.arrow {
  position: absolute;
  width: 6px;
  height: 6px;
  border-left: 1.5px solid #8c8c8c;
  border-bottom: 1.5px solid #8c8c8c;
  transition: all 0.2s ease;
}

.arrow-left {
  left: 2px;
  top: 2px;
  transform: rotate(45deg);
}

.arrow-right {
  right: 2px;
  top: 2px;
  transform: rotate(225deg);
}

.sidebar-toggle:hover .arrow {
  border-color: #666666;
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
  transition: all 0.15s ease !important;
}

.manager-container :deep(.manager-menu .el-menu-item),
.manager-container :deep(.manager-menu .el-submenu__title) {
  padding-left: 20px !important;
  background-color: #f5f7fa !important;
  color: #303133 !important;
  transition: all 0.15s ease !important;
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

.dark-mode .sidebar-toggle {
  background: #2d2d2d;
  border-color: #444444;
  box-shadow: 0 2px 8px rgba(0,0,0,0.4);
}

.dark-mode .sidebar-toggle:hover {
  background: #363636;
  box-shadow: 0 4px 12px rgba(0,0,0,0.5);
}

.dark-mode .arrow {
  border-color: #bbb;
}

.dark-mode .sidebar-toggle:hover .arrow {
  border-color: #fff;
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

/* 收缩状态下的菜单样式 */
.manager-main-left.collapsed :deep(.el-menu--collapse) {
  width: 64px;
}

.manager-main-left.collapsed :deep(.el-submenu__title),
.manager-main-left.collapsed :deep(.el-menu-item) {
  padding: 0 20px !important;
  text-align: center;
}

.manager-main-left.collapsed :deep(.el-submenu__title .el-submenu__icon-arrow),
.manager-main-left.collapsed :deep(.el-menu-item .record-badge) {
  display: none;
}

/* 收缩状态下的悬停效果 */
.manager-main-left.collapsed:hover {
  box-shadow: 2px 0 8px rgba(0,0,0,0.15);
}

.dark-mode .manager-main-left.collapsed:hover {
  box-shadow: 2px 0 8px rgba(0,0,0,0.3);
}

/* 收缩状态下的图标居中 */
.manager-main-left.collapsed :deep(.el-menu-item i),
.manager-main-left.collapsed :deep(.el-submenu__title i) {
  margin-right: 0 !important;
}

/* 收缩状态下的tooltip */
.manager-main-left.collapsed :deep(.el-tooltip) {
  display: block;
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