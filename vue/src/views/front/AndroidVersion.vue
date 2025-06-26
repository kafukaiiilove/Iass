<template>
  <div class="android-version">
    <div class="search">
      <el-input placeholder="请输入标题查询" style="width: 200px" v-model="title"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
      <el-button type="primary" style="margin-left: 10px" @click="showEditorDialog">新增</el-button>
    </div>

    <div class="table">
      <el-table 
        :data="tableData" 
        stripe 
        class="github-style-table"
        :header-cell-style="{ 
          background: '#f6f8fa',
          borderBottom: '1px solid #d0d7de',
          padding: '8px 16px',
          fontSize: '14px',
          fontWeight: '600',
          color: '#24292f',
          textAlign: 'center'
        }"
        :cell-style="{ 
          padding: '8px 16px',
          fontSize: '14px',
          color: '#24292f',
          borderBottom: '1px solid #d0d7de',
          textAlign: 'center'
        }"
        :default-sort="{ prop: 'id', order: 'ascending' }">
        <el-table-column prop="id" label="序号" width="80" sortable align="center" header-align="center"></el-table-column>
        <el-table-column prop="title" label="功能" show-overflow-tooltip align="center" header-align="center"></el-table-column>
        <el-table-column label="方法" align="center" header-align="center">
          <template slot-scope="scope">
            <div v-html="scope.row.content" class="github-content"></div>
          </template>
        </el-table-column>
        <el-table-column label="参数" align="center" header-align="center">
          <template slot-scope="scope">
            <div v-html="scope.row.parameter" class="github-content"></div>
          </template>
        </el-table-column>
        <el-table-column label="描述" align="center" header-align="center">
          <template slot-scope="scope">
            <div v-html="scope.row.description" class="github-content"></div>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="创建时间" width="180" align="center" header-align="center"></el-table-column>
        <el-table-column prop="user" label="创建人" width="120" align="center" header-align="center"></el-table-column>
      </el-table>
    </div>

    <div class="pagination">
      <el-pagination
          background
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, prev, pager, next"
          :total="total">
      </el-pagination>
    </div>

    <!-- 编辑器弹窗 -->
    <el-dialog
      :title="'新增 Android ' + version + ' 文档'"
      :visible.sync="dialogVisible"
      width="80%"
      :before-close="handleClose"
      custom-class="editor-dialog"
    >
      <div class="editor-container" style="display: flex; gap: 20px;">
        <div style="flex: 1;">
          <Editor
            :value="value"
            :plugins="plugins"
            :locale="zhHans"
            @change="handleChange"
            style="height:100%; min-height:400px;"
          />
        </div>
      </div>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveContent">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { Editor, Viewer } from '@bytemd/vue'
import gfm from '@bytemd/plugin-gfm'
import highlight from '@bytemd/plugin-highlight'
import math from '@bytemd/plugin-math'
import mermaid from '@bytemd/plugin-mermaid'
import 'bytemd/dist/index.css'
import 'highlight.js/styles/vs.css'
import 'github-markdown-css/github-markdown.css'

// 中文语言包
const zhHans = {
  write: '编辑',
  preview: '预览',
  uploadingImage: '正在上传图片...',
  pasteDropSelect: '拖放、粘贴或选择文件',
}

export default {
  name: "AndroidVersion",
  components: {
    Editor
  },
  props: {
    version: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      title: null,
      dialogVisible: false,
      value: '',
      plugins: [
        gfm(),
        highlight(),
        math(),
        mermaid()
      ],
      zhHans
    }
  },
  methods: {
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      let searchTitle = this.title
      if (searchTitle) {
        searchTitle = searchTitle.replace(/\*/g, '%')
      }
      console.log('请求参数:', {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        title: searchTitle || '',
        version: this.version
      })
      this.$request.get('/manager/notice/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          title: searchTitle || '',
          version: this.version
        }
      }).then(res => {
        console.log('公告数据响应:', res)
        if (res.code === '200') {
          this.tableData = (res.data?.records || []).map(item => {
            if (item.content) {
              item.content = this.processImageUrls(item.content);
            }
            if (item.parameter) {
              item.parameter = this.processImageUrls(item.parameter);
            }
            if (item.description) {
              item.description = this.processImageUrls(item.description);
            }
            return item;
          });
          this.total = res.data?.total || 0
        } else {
          this.$message.error(res.msg || '获取公告数据失败')
        }
      }).catch(err => {
        console.error('获取公告数据失败:', err)
        this.$message.error('获取公告数据失败')
      })
    },
    reset() {
      this.title = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    processImageUrls(content) {
      if (!content) return content;
      const imgRegex = /<img[^>]+src="([^">]+)"/g;
      let match;
      let updatedContent = content;
      
      while ((match = imgRegex.exec(content)) !== null) {
        const imgUrl = match[1];
        if (!imgUrl.startsWith('http')) {
          const fullUrl = this.$baseUrl + imgUrl;
          updatedContent = updatedContent.replace(imgUrl, fullUrl);
        }
      }
      
      return updatedContent;
    },
    showEditorDialog() {
      this.dialogVisible = true
      this.value = ''
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done()
        })
        .catch(_ => {})
    },
    handleChange(v) {
      this.value = v
    },
    saveContent() {
      const content = this.value
      localStorage.setItem(`android_${this.version}_content`, content)
      this.$message.success('保存成功')
      this.dialogVisible = false
      this.load(1)
    },
    handleUploadImage(files) {
      return new Promise((resolve, reject) => {
        const file = files[0]
        const reader = new FileReader()
        
        reader.onload = (e) => {
          const base64 = e.target.result
          resolve([{
            url: base64,
            alt: file.name
          }])
        }
        
        reader.onerror = (error) => {
          reject(error)
        }
        
        reader.readAsDataURL(file)
      })
    }
  },
  created() {
    this.load(1)
  }
}
</script>

<style scoped>
.android-version {
  padding: 20px;
}

.search {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.table {
  margin-top: 20px;
  border: 1px solid #d0d7de;
  border-radius: 6px;
  overflow: hidden;
}

.github-style-table {
  width: 100%;
  border-collapse: separate !important;
  border-spacing: 0 !important;
}

:deep(.el-table td), :deep(.el-table th) {
  border-right: 2px solid #b1b1b1 !important;
  border-bottom: 1px solid #d0d7de !important;
  text-align: center !important;
  vertical-align: middle;
}
:deep(.el-table td:last-child), :deep(.el-table th:last-child) {
  border-right: none !important;
}
:deep(.el-table__row:hover) td {
  background: #e6f7ff !important;
}

.github-content {
  line-height: 1.6;
  color: #24292f;
  word-break: break-all;
  max-height: 120px;
  overflow: hidden;
}

.github-content img {
  max-width: 70%;
  height: auto;
  border-radius: 6px;
  border: 1px solid #d0d7de;
  margin: 8px 0;
  display: block;
}

.github-content p {
  margin: 4px 0;
  line-height: 1.6;
  text-align: left;
}

.pagination {
  margin-top: 20px;
  text-align: left;
  padding: 16px 0;
}

:deep(.editor-dialog) {
  .el-dialog__body {
    padding: 0;
  }
  
  .editor-container {
    height: 60vh;
    min-height: 400px;
    overflow: hidden;
  }
  
  .bytemd {
    height: 100% !important;
  }
  
  .markdown-body {
    padding: 20px;
  }
}
</style> 