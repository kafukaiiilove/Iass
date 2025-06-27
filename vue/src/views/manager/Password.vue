<template>
  <div>
    <el-card style="width: 50%">
      <el-form ref="formRef" :model="user" :rules="rules" label-width="100px" style="padding-right: 50px">
        <el-form-item label="原始密码" prop="password">
          <el-input show-password v-model="user.password" placeholder="原始密码"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input show-password v-model="user.newPassword" placeholder="新密码"></el-input>
        </el-form-item>
        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input show-password v-model="user.confirmPassword" placeholder="确认密码"></el-input>
        </el-form-item>
        <div style="text-align: center; margin-bottom: 20px">
          <el-button type="primary" @click="update">确认修改</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "Password",
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请确认密码'))
      } else if (value !== this.user.newPassword) {
        callback(new Error('确认密码错误'))
      } else {
        callback()
      }
    }

    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        password: [
          { required: true, message: '请输入原始密码', trigger: 'blur' },
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
        ],
        confirmPassword: [
          { validator: validatePassword, required: true, trigger: 'blur' },
        ],
      }
    }
  },
  created() {
    // 如果没有用户信息，跳转到登录页
    if (!this.user.id) {
      this.$message.warning('请先登录')
      this.$router.push('/login')
    }
    
    // 调试：打印用户信息
    console.log('Password组件 - 当前用户信息:', this.user)
    console.log('用户名字段:', this.user.username)
    console.log('角色字段:', this.user.role)
  },
  methods: {
    // 根据用户角色获取对应的API路径
    getPasswordUpdatePath() {
      const userRole = this.user.role
      console.log('当前用户角色:', userRole)
      
      switch (userRole) {
        case 'ADMIN':
          return 'manager/admin/updatePassword'
        case 'KaiFa':
          return 'manager/kaifa/updatePassword'
        case 'CeShi':
          return 'manager/ceshi/updatePassword'
        default:
          console.warn('未知角色:', userRole, '使用默认管理员路径')
          return 'manager/admin/updatePassword'
      }
    },
    
    update() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          // 根据用户角色动态获取API路径
          const apiPath = this.getPasswordUpdatePath()
          console.log('使用API路径:', apiPath)
          console.log('发送的用户数据:', this.user)
          
          // 确保数据结构正确
          const requestData = {
            username: this.user.username,
            password: this.user.password,
            newPassword: this.user.newPassword,
            role: this.user.role
          }
          console.log('实际发送的请求数据:', requestData)
          
          this.$request.put(apiPath, requestData).then(res => {
            if (res.code === '200') {
              // 成功更新
              localStorage.removeItem('xm-user')   // 清除缓存的用户信息
              this.$message.success('修改密码成功')
              this.$router.push('/login')
            } else {
              this.$message.error(res.msg || '修改密码失败')
            }
          }).catch(err => {
            console.error('修改密码失败:', err)
            this.$message.error('修改密码失败')
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.el-form-item__label {
  font-weight: bold;
}
</style>