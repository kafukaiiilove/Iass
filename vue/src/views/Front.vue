<template>
  <div class="front-container" :class="{ 'dark-mode': isDarkMode }">
    <!--  头部  -->
    <div class="front-header">
      <div class="front-header-left">
        <img src="@/assets/imgs/logo.png" />
        <div class="title">Iass组管理前台</div>
      </div>

      <div class="front-header-right">
        <el-switch
          v-model="isDarkMode"
          active-color="#409EFF"
          inactive-color="#909399"
          active-icon-class="el-icon-sunny"
          inactive-icon-class="el-icon-moon"
          @change="toggleDarkMode"
          style="margin-left: auto;">
        </el-switch>
        <el-button 
          circle 
          type="primary" 
          icon="el-icon-user" 
          @click="handleLogin"
          class="login-button">
        </el-button>
      </div>
    </div>

    <!--  主体  -->
    <div class="front-main">
      <!--  侧边栏  -->
      <div class="front-main-left">
        <el-menu 
          router 
          class="front-menu" 
          :default-active="$route.path"
          background-color="#f5f7fa"
          text-color="#303133"
          active-text-color="#409EFF">
          <el-menu-item index="/front/home">
            <i class="el-icon-s-home"></i>
            <span slot="title">Android 文档简介</span>
          </el-menu-item>
          
          <!-- Android系统版本 -->
          <el-submenu index="android-versions">
            <template slot="title">
              <i class="el-icon-mobile-phone"></i>
              <span>Android系统版本</span>
            </template>
            <el-menu-item index="/front/android/all">
              <i class="el-icon-s-grid"></i>
              <span>All Version</span>
            </el-menu-item>
            <el-menu-item index="/front/android/10">
              <i class="el-icon-arrow-right"></i>
              <span>Android 10</span>
            </el-menu-item>
            <el-menu-item index="/front/android/13">
              <i class="el-icon-arrow-right"></i>
              <span>Android 13</span>
            </el-menu-item>
            <el-menu-item index="/front/android/14">
              <i class="el-icon-arrow-right"></i>
              <span>Android 14</span>
            </el-menu-item>
            <el-menu-item index="/front/android/15">
              <i class="el-icon-arrow-right"></i>
              <span>Android 15</span>
            </el-menu-item>
          </el-submenu>

          <!-- 开发指南 -->
          <el-submenu index="development-guide">
            <template slot="title">
              <i class="el-icon-document"></i>
              <span>开发指南</span>
            </template>
            <el-menu-item index="/front/guide/getting-started">
              <i class="el-icon-arrow-right"></i>
              <span>快速开始</span>
            </el-menu-item>
            <el-menu-item index="/front/guide/best-practices">
              <i class="el-icon-arrow-right"></i>
              <span>最佳实践</span>
            </el-menu-item>
            <el-menu-item index="/front/guide/troubleshooting">
              <i class="el-icon-arrow-right"></i>
              <span>问题排查</span>
            </el-menu-item>
          </el-submenu>

          <!-- API文档 -->
          <el-submenu index="api-docs">
            <template slot="title">
              <i class="el-icon-connection"></i>
              <span>API文档</span>
            </template>
            <el-menu-item index="/front/api/authentication">
              <i class="el-icon-arrow-right"></i>
              <span>认证授权</span>
            </el-menu-item>
            <el-menu-item index="/front/api/user">
              <i class="el-icon-arrow-right"></i>
              <span>用户接口</span>
            </el-menu-item>
            <el-menu-item index="/front/api/device">
              <i class="el-icon-arrow-right"></i>
              <span>设备接口</span>
            </el-menu-item>
          </el-submenu>

          <!-- 示例代码 -->
          <el-submenu index="examples">
            <template slot="title">
              <i class="el-icon-copy-document"></i>
              <span>示例代码</span>
            </template>
            <el-menu-item index="/front/examples/basic">
              <i class="el-icon-arrow-right"></i>
              <span>基础示例</span>
            </el-menu-item>
            <el-menu-item index="/front/examples/advanced">
              <i class="el-icon-arrow-right"></i>
              <span>高级示例</span>
            </el-menu-item>
          </el-submenu>

          <!-- 更新日志 -->
          <el-menu-item index="/front/changelog">
            <i class="el-icon-notebook-2"></i>
            <span slot="title">更新日志</span>
          </el-menu-item>
        </el-menu>
      </div>

      <!--  数据表格  -->
      <div class="front-main-right">
        <router-view @update:user="updateUser" />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Front",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      isDarkMode: false
    }
  },
  methods: {
    updateUser() {
      this.user = JSON.parse(localStorage.getItem('xm-user') || '{}')
    },
    handleLogin() {
      if (this.user && this.user.id) {
        this.$router.push('/manager/home')
      } else {
        this.$router.push('/login')
      }
    },
    logout() {
      localStorage.removeItem('xm-user')
      this.$router.push('/login')
    },
    toggleDarkMode(val) {
      // 切换暗黑模式
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
        localStorage.setItem('zoom-tip-shown-front', 'true')
      }).catch(() => {
        // 即使出错也标记已显示
        localStorage.setItem('zoom-tip-shown-front', 'true')
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
        localStorage.setItem('zoom-tip-shown-front', 'true')
      }, 3000)
    }
  },
  created() {
    // 初始化暗黑模式
    const darkMode = localStorage.getItem('dark-mode')
    if (darkMode === 'true') {
      this.isDarkMode = true
      document.documentElement.classList.add('dark')
    }
    
    // 检查是否已经提示过缩放（只在前台显示一次）
    const hasShownZoomTip = localStorage.getItem('zoom-tip-shown-front')
    if (!hasShownZoomTip) {
      // 延迟显示，确保页面完全加载
      setTimeout(() => {
        this.showZoomTip()
      }, 500)
    }
  }
}
</script>

<style scoped>
@import "@/assets/css/front.css";

/* 前台菜单样式 */
.front-container :deep(.front-menu) {
  border: none !important;
  background-color: #f5f7fa !important;
}

.front-container :deep(.front-menu .el-menu-item),
.front-container :deep(.front-menu .el-submenu__title) {
  padding-left: 20px !important;
  background-color: #f5f7fa !important;
  color: #303133 !important;
}

.front-container :deep(.front-menu .el-menu-item.is-active),
.front-container :deep(.front-menu .el-submenu__title.is-active) {
  background-color: #ecf5ff !important;
  color: #409EFF !important;
}

.front-container :deep(.front-menu .el-menu-item:hover),
.front-container :deep(.front-menu .el-submenu__title:hover) {
  background-color: #ecf5ff !important;
}

/* 暗黑模式下的菜单样式 */
.dark-mode.front-container :deep(.front-menu) {
  background-color: #1f1f1f !important;
}

.dark-mode.front-container :deep(.front-menu .el-menu-item),
.dark-mode.front-container :deep(.front-menu .el-submenu__title) {
  background-color: #1f1f1f !important;
  color: #e5eaf3 !important;
}

.dark-mode.front-container :deep(.front-menu .el-menu-item.is-active),
.dark-mode.front-container :deep(.front-menu .el-submenu__title.is-active) {
  background-color: #0d0d0d !important;
  color: #409EFF !important;
}

.dark-mode.front-container :deep(.front-menu .el-menu-item:hover),
.dark-mode.front-container :deep(.front-menu .el-submenu__title:hover) {
  background-color: #0d0d0d !important;
}

/* 子菜单样式 */
.front-container :deep(.el-menu--inline) {
  background-color: #f5f7fa !important;
}

.dark-mode.front-container :deep(.el-menu--inline) {
  background-color: #1f1f1f !important;
}

/* 其他样式保持不变 */
.front-header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.login-button {
  width: 40px;
  height: 40px;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #409EFF;
  border: none;
  transition: all 0.3s ease;
}

.login-button:hover {
  background-color: #66b1ff;
  transform: scale(1.1);
}

/* 暗黑模式下的样式 */
.dark-mode .login-button {
  background-color: #2d2d2d;
  color: #fff;
}

.dark-mode .login-button:hover {
  background-color: #3d3d3d;
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
</style>