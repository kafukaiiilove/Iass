<template>
  <div class="admin-container">
    <div class="search-bar">
      <el-input placeholder="请输入用户名查询" style="width: 200px" v-model="username"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
      <el-button type="primary" style="margin-left: 10px" @click="add">新增</el-button>
      <el-button type="danger" style="margin-left: 10px" @click="delBatch">批量删除</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="username" label="用户名"></el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column prop="phone" label="电话"></el-table-column>
        <el-table-column prop="email" label="邮箱"></el-table-column>
        <el-table-column prop="role" label="角色">
          <template slot-scope="scope">
            {{ getRoleText(scope.row.role) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template slot-scope="scope">
            <el-button type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="pagination">
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-size="pageSize"
        layout="total, prev, pager, next"
        :total="total">
      </el-pagination>
    </div>

    <!-- 表单弹窗 -->
    <el-dialog title="管理员信息" :visible.sync="dialogVisible" width="50%">
      <el-form :model="form" label-width="100px" ref="formRef" :rules="rules">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="form.role" placeholder="请选择角色">
            <el-option label="管理员" value="ADMIN"></el-option>
            <el-option label="开发工程师" value="KaiFa"></el-option>
            <el-option label="测试工程师" value="CeShi"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Admin',
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      username: '',
      dialogVisible: false,
      form: {},
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        role: [{ required: true, message: '请选择角色', trigger: 'change' }]
      },
      multipleSelection: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.$request.get('/manager/admin/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.username
        }
      }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data?.list || []
          this.total = res.data?.total || 0
        }
      })
    },
    reset() {
      this.username = ''
      this.load()
    },
    add() {
      this.dialogVisible = true
      this.form = {}
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
    },
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/manager/admin/update' : '/manager/admin/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {
              this.$message.success('保存成功')
              this.dialogVisible = false
              this.load()
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    del(id) {
      this.$confirm('确认删除？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$request.delete('/manager/admin/delete/' + id).then(res => {
          if (res.code === '200') {
            this.$message.success('删除成功')
            this.load()
          } else {
            this.$message.error(res.msg)
          }
        })
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    delBatch() {
      if (!this.multipleSelection.length) {
        this.$message.warning('请选择要删除的数据')
        return
      }
      this.$confirm('确认批量删除？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const ids = this.multipleSelection.map(v => v.id)
        this.$request.post('/manager/admin/delete/batch', ids).then(res => {
          if (res.code === '200') {
            this.$message.success('批量删除成功')
            this.load()
          } else {
            this.$message.error(res.msg)
          }
        })
      })
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    getRoleText(role) {
      const roleMap = {
        'ADMIN': '管理员',
        'KaiFa': '开发工程师',
        'CeShi': '测试工程师'
      }
      return roleMap[role] || role
    }
  }
}
</script>

<style scoped>
.admin-container {
  padding: 20px;
}

.search-bar {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.table {
  margin-top: 20px;
  border: 1px solid #e6e6e6;
  border-radius: 6px;
  overflow: hidden;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>