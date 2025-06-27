<template>
  <div class="container">
    <!-- 浮动装饰元素 -->
    <div class="floating-bubbles">
      <div class="bubble bubble-1"></div>
      <div class="bubble bubble-2"></div>
      <div class="bubble bubble-3"></div>
      <div class="bubble bubble-4"></div>
      <div class="bubble bubble-5"></div>
    </div>
    
    <!-- 登录卡片 -->
    <div class="login-card" :class="{ 'card-shake': isShaking }">
      <div class="card-content">
        <!-- 左侧装饰区域 -->
        <div class="card-left">
          <div class="logo-container">
            <div class="logo-gif">
              <img src="@/assets/imgs/company-logo.gif" alt="公司Logo" class="animated-logo">
            </div>
            <h2 class="welcome-title">欢迎回来</h2>
            <p class="welcome-subtitle">登录您的Iass管理账户</p>
          </div>
          
          <!-- 左侧装饰波浪 -->
          <div class="left-decoration">
            <div class="ocean-wave">
              <div class="wave wave1"></div>
              <div class="wave wave2"></div>
              <div class="wave wave3"></div>
            </div>
          </div>
        </div>
        
        <!-- 右侧表单区域 -->
        <div class="card-right">
          <div class="form-container">
            <el-form :model="form" :rules="rules" ref="formRef" class="login-form">
              <el-form-item prop="username" class="form-item">
                <div class="input-wrapper">
                  <i class="input-icon el-icon-user"></i>
                  <el-input 
                    v-model="form.username" 
                    placeholder="请输入账号"
                    class="custom-input"
                    @focus="handleInputFocus"
                    @blur="handleInputBlur">
                  </el-input>
                </div>
              </el-form-item>
              
              <el-form-item prop="password" class="form-item">
                <div class="input-wrapper">
                  <i class="input-icon el-icon-lock"></i>
                  <el-input 
                    v-model="form.password" 
                    type="password"
                    placeholder="请输入密码"
                    class="custom-input"
                    show-password
                    @focus="handleInputFocus"
                    @blur="handleInputBlur">
                  </el-input>
                </div>
              </el-form-item>
              
              <el-form-item class="form-item login-btn-item">
                <el-button 
                  class="login-btn" 
                  :loading="isLoading"
                  @click="login">
                  <span v-if="!isLoading">
                    <i class="el-icon-right"></i>
                    登 录
                  </span>
                  <span v-else>登录中...</span>
                </el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      form: { 
        username: '',
        password: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ]
      },
      isLoading: false,
      isShaking: false
    }
  },
  created() {

  },
  methods: {
    login() {
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          this.isLoading = true;
          // 验证通过
          this.$request.post('/login', this.form).then(res => {
            if (res.code === '200') {
              localStorage.setItem("xm-user", JSON.stringify(res.data))  // 存储用户数据
              this.$router.push('/manager/home')  // 跳转到后台首页
              this.$message.success('登录成功')
            } else {
              this.$message.error(res.msg)
              this.triggerShake()
            }
          }).catch(err => {
            this.$message.error('登录失败，请重试')
            this.triggerShake()
          }).finally(() => {
            this.isLoading = false
          })
        } else {
          this.triggerShake()
        }
      })
    },
    triggerShake() {
      this.isShaking = true
      setTimeout(() => {
        this.isShaking = false
      }, 600)
    },
    handleInputFocus() {
      // 输入框聚焦时的处理
    },
    handleInputBlur() {
      // 输入框失焦时的处理
    }
  }
}
</script>

<style scoped>
.container {
  height: 100vh;
  overflow: hidden;
  background-image: url("@/assets/imgs/Sea.png");
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

/* 浮动气泡装饰 */
.floating-bubbles {
  position: absolute;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}

.bubble {
  position: absolute;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  animation: float 6s ease-in-out infinite;
}

.bubble-1 {
  width: 40px;
  height: 40px;
  left: 10%;
  top: 20%;
  animation-delay: 0s;
}

.bubble-2 {
  width: 60px;
  height: 60px;
  left: 20%;
  top: 80%;
  animation-delay: 2s;
}

.bubble-3 {
  width: 30px;
  height: 30px;
  left: 80%;
  top: 10%;
  animation-delay: 4s;
}

.bubble-4 {
  width: 50px;
  height: 50px;
  left: 90%;
  top: 70%;
  animation-delay: 1s;
}

.bubble-5 {
  width: 35px;
  height: 35px;
  left: 70%;
  top: 40%;
  animation-delay: 3s;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
    opacity: 0.8;
  }
  50% {
    transform: translateY(-20px) rotate(180deg);
    opacity: 0.4;
  }
}

/* 登录卡片 */
.login-card {
  width: 800px;
  height: 450px;
  background: rgba(255, 255, 255, 0.075);
  backdrop-filter: blur(25px);
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-radius: 24px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  position: relative;
  z-index: 2;
  overflow: hidden;
  animation: cardEntrance 0.8s ease-out;
  transition: all 0.3s ease;
}

.login-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.15);
}

.card-content {
  display: flex;
  height: 100%;
  width: 100%;
}

.card-shake {
  animation: shake 0.6s ease-in-out;
}

@keyframes cardEntrance {
  0% {
    opacity: 0;
    transform: translateY(50px) scale(0.9);
  }
  100% {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

@keyframes shake {
  0%, 100% { transform: translateX(0); }
  10%, 30%, 50%, 70%, 90% { transform: translateX(-8px); }
  20%, 40%, 60%, 80% { transform: translateX(8px); }
}

/* 左侧装饰区域 */
.card-left {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 40px;
  position: relative;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
  border-radius: 24px 0 0 24px;
}

.logo-container {
  position: relative;
  text-align: center;
  z-index: 2;
}

.logo-gif {
  width: 120px;
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border: 2px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 15px 35px rgba(102, 126, 234, 0.2);
  animation: logoFloat 3s ease-in-out infinite;
  position: relative;
  overflow: hidden;
}

.animated-logo {
  width: 90px;
  height: 90px;
  object-fit: contain;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.logo-gif:hover .animated-logo {
  transform: scale(1.1);
}

.logo-gif::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.1), transparent);
  transform: rotate(45deg);
  animation: shine 3s ease-in-out infinite;
  pointer-events: none;
}

@keyframes logoFloat {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
    box-shadow: 0 15px 35px rgba(102, 126, 234, 0.2);
  }
  50% {
    transform: translateY(-8px) rotate(1deg);
    box-shadow: 0 20px 40px rgba(102, 126, 234, 0.3);
  }
}

@keyframes shine {
  0% {
    left: -100%;
  }
  100% {
    left: 100%;
  }
}

.welcome-title {
  color: #2c3e50;
  font-size: 28px;
  font-weight: 600;
  margin: 0 0 8px 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.welcome-subtitle {
  color: #5a6c7d;
  font-size: 16px;
  margin: 0;
  opacity: 0.8;
}

/* 右侧表单区域 */
.card-right {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  border-radius: 0 24px 24px 0;
}

.form-container {
  width: 100%;
  max-width: 320px;
}

.login-form {
  width: 100%;
}

.form-item {
  margin-bottom: 25px;
}

.form-item:deep(.el-form-item__error) {
  color: #ff6b6b;
  font-size: 12px;
  margin-top: 5px;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: 16px;
  color: #667eea;
  font-size: 18px;
  z-index: 3;
  transition: all 0.3s ease;
}

.custom-input {
  width: 100%;
}

.custom-input:deep(.el-input__inner) {
  height: 52px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.9);
  padding-left: 50px;
  font-size: 15px;
  color: #2c3e50;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.custom-input:deep(.el-input__inner):focus {
  border-color: #667eea;
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
  transform: translateY(-2px);
}

.custom-input:deep(.el-input__inner)::placeholder {
  color: #a0a8b0;
  font-size: 14px;
}

.custom-input:deep(.el-input__suffix) {
  right: 16px;
}

.custom-input:deep(.el-input__suffix-inner) {
  color: #667eea;
}

/* 登录按钮 */
.login-btn-item {
  margin-bottom: 0;
  margin-top: 10px;
}

.login-btn {
  width: 100%;
  height: 52px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 16px;
  color: white;
  font-size: 16px;
  font-weight: 600;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.login-btn:hover {
  background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%);
  transform: translateY(-2px);
  box-shadow: 0 10px 25px rgba(102, 126, 234, 0.4);
}

.login-btn:active {
  transform: translateY(0);
}

.login-btn i {
  margin-right: 8px;
  font-size: 16px;
}

/* 左侧装饰波浪 */
.left-decoration {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 80px;
  overflow: hidden;
  z-index: 1;
}

.ocean-wave {
  position: relative;
  width: 100%;
  height: 100%;
}

.wave {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 200%;
  height: 100%;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.3) 0%, rgba(118, 75, 162, 0.3) 100%);
  border-radius: 50%;
  animation: wave 4s ease-in-out infinite;
}

.wave1 {
  animation-delay: 0s;
}

.wave2 {
  animation-delay: 1s;
  opacity: 0.7;
}

.wave3 {
  animation-delay: 2s;
  opacity: 0.5;
}

@keyframes wave {
  0%, 100% {
    transform: translateX(-50%) translateY(0px) rotate(0deg);
  }
  50% {
    transform: translateX(-50%) translateY(-10px) rotate(180deg);
  }
}

/* 响应式设计 */
@media (max-width: 900px) {
  .login-card {
    width: 90%;
    height: auto;
    min-height: 500px;
  }
  
  .card-content {
    flex-direction: column;
  }
  
  .card-left {
    border-radius: 24px 24px 0 0;
    padding: 30px;
  }
  
  .card-right {
    border-radius: 0 0 24px 24px;
    padding: 20px 30px 30px;
  }
  
  .form-container {
    max-width: 100%;
  }
}

@media (max-width: 600px) {
  .login-card {
    width: 95%;
    margin: 0 10px;
  }
  
  .card-left, .card-right {
    padding: 20px;
  }
  
  .welcome-title {
    font-size: 24px;
  }
  
  .welcome-subtitle {
    font-size: 14px;
  }
  
  .logo-gif {
    width: 100px;
    height: 100px;
  }
  
  .animated-logo {
    width: 75px;
    height: 75px;
  }
}

/* 加载状态 */
.login-btn.is-loading {
  pointer-events: none;
}

.login-btn.is-loading:deep(.el-loading-spinner) {
  margin-top: -10px;
}

/* 美化滚动条（如果需要） */
::-webkit-scrollbar {
  width: 8px;
}

::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: rgba(102, 126, 234, 0.3);
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: rgba(102, 126, 234, 0.5);
}
</style>