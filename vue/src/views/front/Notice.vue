<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入标题查询" style="width: 200px" v-model="title"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
      <el-button type="primary" style="margin-left: 10px" @click="showEditorDialog">新增</el-button>
    </div>

    <!-- 添加编辑器组件 -->
    <div v-if="showEditor" class="editor-container">
      <bytemd-editor
        v-model="editorContent"
        :plugins="plugins"
        :uploadImages="handleUploadImage"
        :locale="zhHans"
      />
      <div class="editor-buttons">
        <el-button @click="cancelEdit">取消</el-button>
        <el-button type="primary" @click="saveContent">保存</el-button>
      </div>
    </div>

    <!-- 添加查看器组件 -->
    <div v-if="showViewer" class="viewer-container">
      <bytemd-viewer
        :value="viewerContent"
        :plugins="plugins"
      />
      <div class="viewer-buttons">
        <el-button @click="closeViewer">关闭</el-button>
      </div>
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
          borderBottom: '1px solid #d0d7de'
        }"
        :default-sort="{ prop: 'id', order: 'ascending' }">
        <el-table-column prop="id" label="序号" width="80" sortable></el-table-column>
        <el-table-column prop="title" label="功能" show-overflow-tooltip align="left" header-align="center"></el-table-column>
        <el-table-column label="方法" align="left" header-align="center">
          <template slot-scope="scope">
            <div v-html="scope.row.content" class="github-content"></div>
          </template>
        </el-table-column>
        <el-table-column label="参数" align="left" header-align="center">
          <template slot-scope="scope">
            <div v-html="scope.row.parameter" class="github-content"></div>
          </template>
        </el-table-column>
        <el-table-column label="描述" align="left" header-align="center">
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
      title="新增公告"
      :visible.sync="dialogVisible"
      width="80%"
      :before-close="handleClose"
      custom-class="editor-dialog"
    >
      <div class="editor-container">
        <bytemd-editor
          v-model="editorContent"
          :plugins="plugins"
          :uploadImages="handleUploadImage"
          :locale="zhHans"
        />
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
  name: "FrontNotice",
  components: {
    'bytemd-editor': Editor,
    'bytemd-viewer': Viewer
  },
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      title: null,
      showEditor: false,
      showViewer: false,
      editorContent: '',
      viewerContent: '',
      plugins: [
        gfm(),
        highlight(),
        math(),
        mermaid()
      ],
      zhHans,
      dialogVisible: false
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      let searchTitle = this.title
      if (searchTitle) {
        searchTitle = searchTitle.replace(/\*/g, '%')
      }
      console.log('请求参数:', {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        title: searchTitle || '',
      })
      this.$request.get('/manager/notice/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          title: searchTitle || '',
        }
      }).ten(res => {
        console.log('公告数据响应:', res)
        if (res.code === '200') {
          // 处理返回数据中的图片URL
          this.tableData = (res.data?.records || []).map(item => {
            // 确保content中的图片URL是完整的
            if (item.content) {
              item.content = this.processImageUrls(item.content);
            }
            // 确保parameter中的图片URL是完整的
            if (item.parameter) {
              item.parameter = this.processImageUrls(item.parameter);
            }
            // 确保description中的图片URL是完整的
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
    // 处理图片URL
    processImageUrls(content) {
      if (!content) return content;
      const imgRegex = /<img[^>]+src="([^">]+)"/g;
      let match;
      let updatedContent = content;
      
      while ((match = imgRegex.exec(content)) !== null) {
        const imgUrl = match[1];
        // 如果URL不是以http开头，添加baseUrl
        if (!imgUrl.startsWith('http')) {
          const fullUrl = this.$baseUrl + imgUrl;
          updatedContent = updatedContent.replace(imgUrl, fullUrl);
        }
      }
      
      return updatedContent;
    },
    showEditorDialog() {
      this.dialogVisible = true
      this.editorContent = ''
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done()
        })
        .catch(_ => {})
    },
    saveContent() {
      // 保存到本地
      const content = this.editorContent
      localStorage.setItem('notice_content', content)
      this.$message.success('保存成功')
      this.dialogVisible = false
      this.load(1)
    },
    handleUploadImage(files) {
      return new Promise((resolve, reject) => {
        const file = files[0]
        const reader = new FileReader()
        
        reader.onload = (e) => {
          // 将图片转换为base64
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
    },
    // 查看内容
    viewContent(content) {
      this.viewerContent = content
      this.showViewer = true
    },
    // 关闭查看器
    closeViewer() {
      this.showViewer = false
      this.viewerContent = ''
    }
  }
}
</script>

<style scoped>
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
  border-collapse: collapse;
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

/* Element UI 样式覆盖 */
:deep(.el-table) {
  border: none;
}

:deep(.el-table::before) {
  display: none;
}

:deep(.el-table--striped .el-table__row--striped td) {
  background: #f6f8fa;
}

:deep(.el-table td, .el-table th) {
  border-right: 1px solid #d0d7de;
}

:deep(.el-table td:last-child, .el-table th:last-child) {
  border-right: none;
}

/* 移除之前的右对齐样式 */
:deep(.el-table td:nth-last-child(-n+2)) {
  text-align: center;
}

/* 确保内容左对齐的列 */
:deep(.el-table td:nth-child(n+2):nth-child(-n+5)) {
  text-align: left;
}

:deep(.el-pagination) {
  justify-content: flex-start;
}

.editor-container, .viewer-container {
  margin: 20px 0;
  border: 1px solid #d0d7de;
  border-radius: 6px;
  overflow: hidden;
}

.editor-buttons, .viewer-buttons {
  padding: 10px;
  text-align: right;
  background: #f6f8fa;
  border-top: 1px solid #d0d7de;
}

:deep(.bytemd) {
  height: 500px;
}

:deep(.bytemd-viewer) {
  padding: 20px;
  background: #fff;
}

:deep(.editor-dialog) {
  .el-dialog__body {
    padding: 0;
  }
  
  .editor-container {
    height: calc(100vh - 300px);
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