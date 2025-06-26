<template>
  <!-- No changes to template section -->
</template>

<script>
export default {
  data() {
    return {
      // No changes to data section
    }
  },
  methods: {
    load() {
      this.$request.get('/api/manager/category/selectAll').then(res => {
        this.tableData = res.data || []
      })
    },
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            method: this.form.id ? 'put' : 'post',
            url: '/api/manager/category',
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
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
    },
    del(id) {
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/api/manager/category/' + id).then(res => {
          if (res.code === '200') {
            this.$message.success('删除成功')
            this.load()
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {
      })
    },
  }
}
</script>

<style>
  /* No changes to style section */
</style> 