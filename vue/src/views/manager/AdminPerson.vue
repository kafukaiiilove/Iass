<template>
  <div>
    <el-card style="width: 50%">
      <el-form :model="user" label-width="100px" style="padding-right: 50px">
        <div style="margin: 15px; text-align: center">
          <el-upload
              class="avatar-uploader"
              :action="$baseUrl + '/api/manager/files/upload'"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :headers="headers"
          >
            <img v-if="displayAvatarUrl" :src="displayAvatarUrl" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </div>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="user.username" placeholder="用户名" disabled></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="user.name" placeholder="姓名"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="user.phone" placeholder="电话"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="user.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <div style="text-align: center; margin-bottom: 20px">
          <el-button type="primary" @click="update">保 存</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "AdminPerson",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      headers: {
        token: JSON.parse(localStorage.getItem('xm-user') || '{}').token || ''
      }
    }
  },
  created() {
    // 如果没有用户信息，跳转到登录页
    if (!this.user.id) {
      this.$message.warning('请先登录')
      this.$router.push('/login')
    }
  },
  methods: {
    update() {
      // 保存当前的用户信息到数据库
      this.$request.put('/manager/admin/update', this.user).then(res => {
        if (res.code === '200') {
          // 成功更新
          this.$message.success('保存成功')

          // 确保user对象包含完整的头像URL
          if (this.user.avatar) {
            // 更新浏览器缓存里的用户信息
            localStorage.setItem('xm-user', JSON.stringify(this.user))
            // 触发父级的数据更新
            this.$emit('update:user')
          } else {
            this.$message.warning('头像信息未保存，请重新上传头像')
          }
        } else {
          this.$message.error(res.msg || '保存失败')
        }
      }).catch(err => {
        console.error('保存失败:', err)
        this.$message.error('保存失败')
      })
    },
    handleAvatarSuccess(response, file, fileList) {
      if (response.code === '200') {
        // 把user的头像属性换成上传的图片的链接
        this.$set(this.user, 'avatar', response.data)
        // 立即更新localStorage中的用户信息
        localStorage.setItem('xm-user', JSON.stringify(this.user))
        // 触发父级的数据更新
        this.$emit('update:user')
        this.$message.success('头像上传成功')
      } else {
        this.$message.error(response.msg || '头像上传失败')
      }
    }
  },
  computed: {
    displayAvatarUrl() {
      if (!this.user.avatar) return '';
      
      // 将localhost替换为实际IP地址
      if (this.user.avatar.includes('localhost')) {
        return this.user.avatar.replace('localhost', '192.168.120.104');
      }
      
      return this.user.avatar;
    }
  }
}
</script>

<style scoped>
/deep/.el-form-item__label {
  font-weight: bold;
}
/deep/.el-upload {
  border-radius: 50%;
}
/deep/.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  border-radius: 50%;
}
/deep/.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
  border-radius: 50%;
}
.avatar {
  width: 120px;
  height: 120px;
  display: block;
  border-radius: 50%;
}
</style>