<template>
  <div>
    <div class="search">
      <el-select v-model="searchType" placeholder="选择查询类型" style="width: 120px;">
        <el-option label="标题" value="title"></el-option>
        <el-option label="属性键名" value="content"></el-option>
        <el-option label="示例参数" value="parameter"></el-option>
        <el-option label="参数描述" value="description"></el-option>
        <el-option label="创建人" value="user"></el-option>
      </el-select>
      <el-input 
        :placeholder="getSearchPlaceholder()" 
        style="width: 250px; margin-left: 10px" 
        v-model="searchKeyword">
      </el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="operation">
      <el-button type="primary" plain @click="handleAdd">新增</el-button>
      <el-button type="success" plain @click="handleUpload">上传</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="table">
      <el-table 
        :data="tableData" 
        stripe 
        class="github-style-table"
        row-key="id"
        @selection-change="handleSelectionChange"
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
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="title" label="功能描述" show-overflow-tooltip align="left" header-align="center"></el-table-column>
        <el-table-column label="属性键名" align="center" header-align="center">
          <template slot-scope="scope">
            <div v-html="formatContentWithImagePreview(scope.row.content)" class="github-content"></div>
          </template>
        </el-table-column>
        <el-table-column label="示例参数" align="center" header-align="center">
          <template slot-scope="scope">
            <div v-html="formatContentWithImagePreview(scope.row.parameter)" class="github-content"></div>
          </template>
        </el-table-column>
        <el-table-column label="参数描述" align="center" header-align="center">
          <template slot-scope="scope">
            <div v-html="formatContentWithImagePreview(scope.row.description)" class="github-content"></div>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="创建时间" width="180" align="center" header-align="center"></el-table-column>
        <el-table-column prop="user" label="创建人" width="120" align="center" header-align="center"></el-table-column>
        <el-table-column label="操作" width="250" align="center" header-align="center">
          <template slot-scope="scope">
            <el-button type="success" @click="handleViewMarkdown(scope.row)">查看</el-button>
            <el-button type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
          background
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
          :current-page="pageNum"
          :page-sizes="[5, 10, 20, 50]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </div>

    <el-dialog
      v-if="dialogFormVisible"
      :key="dialogFormVisible ? 'dialog-open' : 'dialog-close'"
      title="信息"
      :visible.sync="dialogFormVisible"
      width="1800px"
      :close-on-click-modal="false"
      destroy-on-close
      class="custom-dialog it-style-dialog"
      :modal-append-to-body="true"
      :append-to-body="true"
      :show-close="true"
      center
      @close="handleDialogClose">
      <div class="dialog-flex-body">
        <!-- 侧边栏 -->
        <div class="dialog-sidebar">
          <div class="sidebar-title">操作说明</div>
          <ul class="sidebar-list">
            <li>支持图片粘贴/上传</li>
            <li>内容实时预览</li>
            <li>支持撤销操作</li>
            <li>分类、标签可选</li>
          </ul>
        </div>
        <!-- 表单内容 -->
        <div class="dialog-main">
          <el-form 
            :model="form" 
            label-width="100px" 
            :rules="rules" 
            ref="formRef"
            class="dialog-form it-form">
            <el-form-item label="功能" prop="title">
              <el-input v-model="form.title" autocomplete="off" style="width: 100%"></el-input>
            </el-form-item>
            <el-form-item label="方法" prop="content">
              <div class="editor-wrapper">
                <Toolbar
                  style="border-bottom: 1px solid #dcdfe6"
                  :editor="editorRef"
                  :defaultConfig="toolbarConfig"
                  mode="default"
                />
                <Editor
                  style="height: 300px; overflow-y: hidden;"
                  v-model="form.content"
                  :defaultConfig="editorConfig"
                  mode="default"
                  @onCreated="handleCreated"
                  @onChange="handleContentChange"
                />
              </div>
            </el-form-item>
            <el-form-item label="参数" prop="parameter">
              <div class="editor-wrapper">
                <Toolbar
                  style="border-bottom: 1px solid #dcdfe6"
                  :editor="editorRef2"
                  :defaultConfig="toolbarConfig"
                  mode="default"
                />
                <Editor
                  style="height: 300px; overflow-y: hidden;"
                  v-model="form.parameter"
                  :defaultConfig="editorConfig"
                  mode="default"
                  @onCreated="handleCreated2"
                  @onChange="handleParameterChange"
                />
              </div>
            </el-form-item>
            <el-form-item label="描述" prop="description">
              <div class="editor-wrapper">
                <Toolbar
                  style="border-bottom: 1px solid #dcdfe6"
                  :editor="editorRef3"
                  :defaultConfig="toolbarConfig"
                  mode="default"
                />
                <Editor
                  style="height: 300px; overflow-y: hidden;"
                  v-model="form.description"
                  :defaultConfig="editorConfig"
                  mode="default"
                  @onCreated="handleCreated3"
                  @onChange="handleDescriptionChange"
                />
              </div>
            </el-form-item>
          </el-form>
          <div class="dialog-footer it-footer">
            <el-button @click="dialogFormVisible = false" class="it-btn">取 消</el-button>
            <el-button type="primary" @click="save" class="it-btn">确 定</el-button>
          </div>
        </div>
      </div>
    </el-dialog>

    <!-- 添加图片预览对话框 -->
    <el-dialog
      :visible.sync="previewVisible"
      :append-to-body="true"
      :modal-append-to-body="true"
      :close-on-click-modal="true"
      :close-on-press-escape="true"
      custom-class="image-preview-dialog"
      width="90%"
      :fullscreen="true"
      center
      @close="previewVisible = false">
      <div class="preview-content">
        <img
          :src="previewImageUrl"
          class="preview-full-image"
          alt="预览图片"
          :style="{ transform: `scale(${imageScale})`, transition: 'transform 0.2s' }"
          @wheel="handleImageWheel"
        >
      </div>
    </el-dialog>

    <!-- 上传对话框 -->
    <el-dialog 
      title="上传Markdown文件" 
      :visible.sync="uploadDialogVisible" 
      width="40%" 
      :close-on-click-modal="false" 
      destroy-on-close 
      class="custom-dialog"
      :modal-append-to-body="true"
      :append-to-body="true"
      :show-close="true"
      center>
      <el-upload
        class="upload-demo"
        drag
        :action="$baseUrl + '/manager/a13/markdown/upload'"
        :on-success="handleUploadSuccess"
        :on-error="handleUploadError"
        :before-upload="beforeUpload"
        :headers="uploadHeaders"
        :show-file-list="false"
        :auto-upload="true"
        :http-request="customUpload">
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传markdown文件，且不超过10MB</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="uploadDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="uploadDialogVisible = false">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 预览对话框 -->
    <el-dialog 
      title="Markdown预览" 
      :visible.sync="previewDialogVisible" 
      width="60%" 
      :close-on-click-modal="false" 
      destroy-on-close 
      class="custom-dialog"
      :modal-append-to-body="true"
      :append-to-body="true"
      :show-close="true"
      center>
      <div class="markdown-preview" v-html="previewContent"></div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="previewDialogVisible = false">关 闭</el-button>
        <el-button type="primary" @click="savePreview">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import axios from 'axios'
import MarkdownIt from 'markdown-it'
import hljs from 'highlight.js'
import 'highlight.js/styles/atom-one-dark.css'

export default {
  name: "A13",
  components: { Editor, Toolbar },
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      title: null,
      searchType: 'title',
      searchKeyword: null,
      dialogFormVisible: false,
      form: {
        id: null,
        title: '',
        content: '',
        parameter: '',
        description: '',
        time: null,
        user: ''
      },
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        title: [
          {required: true, message: '请输入标题', trigger: 'blur'},
        ],
        content: [
          {required: true, message: '请输入内容', trigger: 'blur'},
        ]
      },
      ids: [],
      currentEditor: null,
      baseUrl: process.env.VUE_APP_BASE_API || '',
      uploadHeaders: {
        token: JSON.parse(localStorage.getItem('xm-user') || '{}').token || ''
      },
      currentField: null,
      axiosInstance: null,
      undoStack: {
        content: [''],
        parameter: [''],
        description: ['']
      },
      isUndoing: false,
      md: new MarkdownIt({
        html: true,
        linkify: true,
        typographer: true,
        highlight: function (str, lang) {
          if (lang && hljs.getLanguage(lang)) {
            try {
              return `<pre class="hljs"><code>${hljs.highlight(lang, str, true).value}</code></pre>`;
            } catch (error) {
              console.log(error)
            }
          }
          return `<pre class="hljs"><code>${this.md.utils.escapeHtml(str)}</code></pre>`;
        },
      }),
      editorRef: null,
      editorRef2: null,
      editorRef3: null,
      toolbarConfig: {
        excludeKeys: [
          'uploadVideo',
          'insertTable',
          'codeBlock',
          'todo'
        ]
      },
      editorConfig: {
        placeholder: '请输入内容...',
        MENU_CONF: {
          uploadImage: {
            server: this.$baseUrl + '/api/manager/files/upload',
            fieldName: 'file',
            maxFileSize: 5 * 1024 * 1024,  // 最大文件大小 5MB
            maxNumberOfFiles: 10,  // 最多上传10张图片
            allowedFileTypes: ['image/*'],
            meta: {
              token: JSON.parse(localStorage.getItem('xm-user') || '{}').token || ''
            },
            customInsert(res, insertFn) {
              if (res.code === '200') {
                insertFn(res.data)
              } else {
                this.$message.error(res.msg || '图片上传失败')
              }
            }
          }
        }
      },
      previewImageUrl: '',
      previewVisible: false,
      imageScale: 1,
      uploadDialogVisible: false,
      previewDialogVisible: false,
      previewContent: '',
      previewNotice: null,
    }
  },
  created() {
    this.load(1)
    const user = JSON.parse(localStorage.getItem('xm-user') || '{}');
    this.axiosInstance = axios.create({
      baseURL: this.$baseUrl,
      timeout: 30000,
      headers: {
        token: user.token
      }
    })
  },
  beforeDestroy() {
    // 清理数据
    this.dialogFormVisible = false;
    this.form = {
      id: null,
      title: '',
      content: '',
      parameter: '',
      description: '',
      time: null,
      user: ''
    };
    this.tableData = [];
    if (this.editorRef) this.editorRef.destroy()
    if (this.editorRef2) this.editorRef2.destroy()
    if (this.editorRef3) this.editorRef3.destroy()
    // 清理全局方法
    delete window.previewImage;
    // 移除事件监听
    document.removeEventListener('preview-image', this.handlePreviewImage);
  },
  methods: {
    load(pageNum) {
      console.log('开始加载数据，页码：', pageNum);
      if (pageNum) this.pageNum = pageNum
      
      // 构建搜索参数
      const params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };
      
      // 根据搜索类型添加对应的搜索参数
      if (this.searchKeyword && this.searchType) {
        let searchValue = this.searchKeyword.replace(/\*/g, '%');
        params[this.searchType] = searchValue;
      }
      
      this.$request.get('/manager/a13/selectPage', { params }).then(res => {
        console.log('获取数据响应：', res);
        if (res.code === '200') {
          this.tableData = res.data?.list || res.data?.records || []
          this.total = res.data?.total || 0
        } else {
          this.$message.error(res.msg || '获取数据失败')
        }
      }).catch(err => {
        console.error('获取数据失败:', err)
        this.$message.error('获取数据失败')
      })
    },
    handleAdd() {
      console.log('点击新增按钮');
      this.form = {
        id: null,
        title: '',
        content: '',
        parameter: '',
        description: '',
        time: null,
        user: ''
      }
      this.dialogFormVisible = true // 打开弹窗
    },
    handleUpload() {
      console.log('点击上传按钮');
      this.uploadDialogVisible = true;
    },
    handleEdit(row) {
      console.log('点击编辑按钮，行数据：', row);
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    save() {
      console.log('开始保存数据：', this.form);
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          // 确保内容不为空
          if (!this.form.content) {
            console.warn('内容为空');
            this.$message.warning('请输入内容');
            return;
          }

          // 显示保存中提示
          const loading = this.$loading({
            lock: true,
            text: '保存中...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });

          try {
            // 处理图片URL
            const formData = JSON.parse(JSON.stringify(this.form));
            console.log('处理前的数据：', formData);
            
            if (formData.content) {
              formData.content = this.processImageUrls(formData.content);
            }
            if (formData.parameter) {
              formData.parameter = this.processImageUrls(formData.parameter);
            }
            if (formData.description) {
              formData.description = this.processImageUrls(formData.description);
            }
            
            console.log('处理后的数据：', formData);

            // 根据是否有ID判断是新增还是修改
            const url = this.form.id ? '/manager/a13/update' : '/manager/a13/add';
            const method = this.form.id ? 'put' : 'post';
            console.log('请求URL：', url, '方法：', method);

            this.$request({
              method: method,
              url: url,
              data: formData,
              headers: {
                'Content-Type': 'application/json'
              }
            }).then(res => {
              loading.close();
              console.log('保存响应：', res);
              if (res.code === '200') {
                this.$message.success('保存成功');
                this.dialogFormVisible = false;
                this.load(1);
              } else {
                console.error('保存失败：', res.msg);
                this.$message.error(res.msg || '保存失败');
              }
            }).catch(err => {
              loading.close();
              console.error('保存请求失败：', err);
              this.$message.error('保存失败，请重试');
            });
          } catch (error) {
            loading.close();
            console.error('数据处理失败：', error);
            this.$message.error('数据处理失败，请重试');
          }
        } else {
          console.warn('表单验证失败');
          return false;
        }
      });
    },
    handleDelete(id) {
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/manager/a13/delete/' + id).then(res => {
          if (res.code === '200') {
            this.$message.success('删除成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {
      })
    },
    handleSelectionChange(rows) {
      this.ids = rows.map(v => v.id)
    },
    delBatch() {
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(() => {
        this.$request.delete('/manager/a13/delete/batch', {data: this.ids}).then(res => {
          if (res.code === '200') {
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {
        this.$message.info('已取消批量删除');
      });
    },
    reset() {
      this.searchType = 'title'
      this.searchKeyword = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.load(1);
    },
    handleCreated(editor) {
      this.editorRef = editor
    },
    handleCreated2(editor) {
      this.editorRef2 = editor
    },
    handleCreated3(editor) {
      this.editorRef3 = editor
    },
    handleContentChange(editor) {
      this.form.content = editor.getHtml()
    },
    handleParameterChange(editor) {
      this.form.parameter = editor.getHtml()
    },
    handleDescriptionChange(editor) {
      this.form.description = editor.getHtml()
    },
    handleDialogClose() {
      this.dialogFormVisible = false
      this.form = {
        id: null,
        title: '',
        content: '',
        parameter: '',
        description: '',
        time: null,
        user: ''
      }
      if (this.editorRef) this.editorRef.destroy()
      if (this.editorRef2) this.editorRef2.destroy()
      if (this.editorRef3) this.editorRef3.destroy()
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
    formatContentWithImagePreview(content) {
      if (!content) return '';
      
      // 将图片标签转换为可点击预览的格式
      return content.replace(/<img[^>]+src="([^">]+)"[^>]*>/g, (match, src) => {
        // 将localhost替换为实际IP地址
        let fullUrl = src;
        if (src.includes('localhost')) {
          fullUrl = src.replace('localhost', '192.168.120.104');
        } else if (!src.startsWith('http')) {
          // 如果不是完整URL，添加基础URL
          fullUrl = 'http://192.168.120.104:9090/api' + src;
        }
        
        return `<div class="image-preview-container" onclick="document.dispatchEvent(new CustomEvent('preview-image', {detail: '${fullUrl}'}))">
          <img src="${fullUrl}" class="preview-image" alt="预览图片">
        </div>`;
      });
    },
    handlePreviewImage(url) {
      this.previewImageUrl = url;
      this.previewVisible = true;
      this.imageScale = 1; // 打开时重置缩放
    },
    handleImageWheel(e) {
      e.preventDefault();
      if (e.deltaY < 0) {
        // 放大
        this.imageScale = Math.min(this.imageScale + 0.1, 5);
      } else {
        // 缩小
        this.imageScale = Math.max(this.imageScale - 0.1, 0.2);
      }
    },
    handleUploadSuccess(res) {
      if (res.code === '200') {
        this.$message.success('上传成功');
        const notice = res.data;
        if (!notice) {
          this.$message.error('解析markdown内容失败');
          return;
        }
        // 直接填充到form表单
        this.form = {
          title: notice.title || '',
          content: notice.content || '',
          parameter: notice.parameter || '',
          description: notice.description || '',
          time: new Date().toISOString(),
          user: JSON.parse(localStorage.getItem('xm-user') || '{}').username
        };
        this.uploadDialogVisible = false;
        this.dialogFormVisible = true; // 打开新增弹窗
      } else {
        this.$message.error(res.msg || '上传失败');
      }
    },
    handleUploadError(err) {
      let errorMsg = '上传失败';
      if (err.response) {
        const status = err.response.status;
        if (status === 500) {
          errorMsg = '服务器内部错误，请稍后重试';
        } else if (status === 413) {
          errorMsg = '文件大小超过限制';
        } else if (status === 415) {
          errorMsg = '不支持的文件类型';
        }
      } else if (err.message) {
        errorMsg = err.message;
      }
      this.$message.error(errorMsg);
    },
    beforeUpload(file) {
      const user = JSON.parse(localStorage.getItem('xm-user') || '{}');
      if (!user.token) {
        this.$message.error('请先登录');
        return false;
      }
      const isMarkdown = file.type === 'text/markdown' || file.name.endsWith('.md');
      const isLt10M = file.size / 1024 / 1024 < 10;
      if (!isMarkdown) {
        this.$message.error('只能上传markdown文件！');
        return false;
      }
      if (!isLt10M) {
        this.$message.error('文件大小不能超过10MB！');
        return false;
      }
      return true;
    },
    customUpload(options) {
      const formData = new FormData();
      formData.append('file', options.file);
      const user = JSON.parse(localStorage.getItem('xm-user') || '{}');
      const token = user.token;
      this.$request({
        url: '/manager/a13/markdown/upload',
        method: 'post',
        data: formData,
        headers: {
          'Content-Type': 'multipart/form-data',
          'token': token
        }
      }).then(res => {
        options.onSuccess(res);
      }).catch(err => {
        options.onError(err);
      });
    },
    savePreview() {
      if (!this.previewNotice) {
        this.$message.error('预览内容为空');
        return;
      }
      const user = JSON.parse(localStorage.getItem('xm-user') || '{}');
      if (!user.token) {
        this.$message.error('请先登录');
        return;
      }
      const loading = this.$loading({
        lock: true,
        text: '保存中...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      this.$request({
        method: 'post',
        url: '/manager/a13/markdown/save',
        data: {
          ...this.previewNotice,
          time: new Date().toISOString(),
          user: user.username
        },
        headers: {
          token: user.token,
          'Content-Type': 'application/json'
        }
      }).then(res => {
        loading.close();
        if (res.code === '200') {
          this.$message.success('保存成功');
          this.previewDialogVisible = false;
          this.load(1);
        } else {
          this.$message.error(res.msg || '保存失败');
        }
      }).catch(err => {
        loading.close();
        this.$message.error(err.response?.data?.msg || '保存失败，请重试');
      });
    },
    handleViewMarkdown(row) {
      this.$request.get('/manager/a13/selectById/' + row.id).then(res => {
        if (res.code === '200') {
          const data = res.data;
          let md = '';
          if (data.title) md += `## 功能描述\n${data.title}\n\n`;
          if (data.content) md += `## 属性键名\n${data.content}\n\n`;
          if (data.parameter) md += `## 示例参数\n${data.parameter}\n\n`;
          if (data.description) md += `## 参数描述\n${data.description}\n\n`;
          this.previewContent = this.md.render(md);
          this.previewDialogVisible = true;
        } else {
          this.$message.error(res.msg || '获取详情失败');
        }
      });
    },
    getSearchPlaceholder() {
      switch (this.searchType) {
        case 'title':
          return '请输入标题查询';
        case 'content':
          return '请输入属性键名查询';
        case 'parameter':
          return '请输入示例参数查询';
        case 'description':
          return '请输入参数描述查询';
        case 'user':
          return '请输入创建人查询';
        default:
          return '请输入查询关键词';
      }
    },
  },
  mounted() {
    // 添加全局事件监听
    document.addEventListener('preview-image', (e) => {
      this.handlePreviewImage(e.detail);
    });
  }
}
</script>

<style scoped>
/* 表格内容样式 */
.table-content {
  max-height: 120px;
  overflow: hidden;
  word-break: break-all;
  line-height: 1.5;
  text-align: left;
  padding: 8px;
}

.table-content img {
  max-width: 200px;
  max-height: 150px;
  display: inline-block;
  vertical-align: middle;
  margin: 8px 0;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  object-fit: contain;
}

/* 表格单元格样式 */
.el-table .cell {
  text-align: left !important;
  padding: 8px !important;
  line-height: 1.5;
  font-size: 14px;
}

/* 表格行样式 */
.el-table__row td {
  vertical-align: middle;
  padding: 8px !important;
}

/* 编辑器样式 */
.editor-wrapper {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}

:deep(.w-e-text-container) {
  min-height: 300px !important;
}

:deep(.w-e-toolbar) {
  border-top: none !important;
  border-left: none !important;
  border-right: none !important;
}

:deep(.w-e-bar-item) {
  padding: 0 8px !important;
}

:deep(.w-e-bar-divider) {
  margin: 0 8px !important;
}

:deep(.w-e-text-container .w-e-text) {
  padding: 12px !important;
}

/* GitHub风格内容样式 */
.github-content {
  text-align: center;
  line-height: 1.5;
  color: #24292f;
  word-break: break-all;
  max-height: 120px;
  overflow: hidden;
  padding: 8px;
}

.github-content img {
  max-width: 200px;
  max-height: 150px;
  display: inline-block;
  vertical-align: middle;
  margin: 8px auto;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  object-fit: contain;
}

.github-content p {
  margin: 4px 0;
  line-height: 1.5;
  text-align: center;
}

/* 表格整体样式 */
.el-table {
  th {
    text-align: center !important;
    padding: 8px !important;
  }
  td {
    padding: 8px !important;
  }
}

/* 确保下拉框可以正常显示 */
.el-select-dropdown {
  z-index: 9999 !important;
}

.el-popper {
  z-index: 9999 !important;
}

/* 添加新的样式 */
.search {
  margin-bottom: 20px;
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

/* 确保内容左对齐的列 */
:deep(.el-table td:nth-child(n+3):nth-child(-n+6)) {
  text-align: left;
}

:deep(.el-pagination) {
  justify-content: flex-start;
}

/* 弹窗样式 */
.custom-dialog {
  border-radius: 25px;
  overflow: hidden;
}

.custom-dialog .el-dialog {
  margin-top: 8vh !important;
  max-height: 84vh;
  display: flex;
  flex-direction: column;
  border-radius: 25px;
  overflow: hidden;
}

.custom-dialog .el-dialog__header {
  padding: 15px 20px;
  border-bottom: 1px solid #e68a3f;
}

.custom-dialog .el-dialog__body {
  flex: 1 1 auto;
  overflow-y: auto;
  max-height: calc(84vh - 120px); /* 120px 预留header+footer */
  padding: 20px;
  background: #fff;
}

.custom-dialog .el-dialog__footer {
  flex-shrink: 0;
  padding: 10px 20px;
  text-align: center;
  border-top: 1px solid #ebeef5;
  background: #fff;
  z-index: 1;
  position: sticky;
  bottom: 0;
}

.dialog-flex-body {
  display: flex;
  min-height: 420px;
  max-height: 60vh;
  overflow: hidden;
}
.dialog-sidebar {
  width: 180px;
  background: #f0f4f8;
  border-right: 1px solid #e0e6ed;
  padding: 32px 16px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  border-radius: 12px 0 0 12px;
  font-size: 14px;
  color: #6b7b8d;
  overflow-y: auto;
  max-height: 60vh;
}
.sidebar-title {
  font-weight: bold;
  font-size: 16px;
  margin-bottom: 18px;
  color: #2d3a4b;
}
.sidebar-list {
  list-style: none;
  padding: 0;
  margin: 0;
}
.sidebar-list li {
  margin-bottom: 10px;
  padding-left: 8px;
  position: relative;
}
.sidebar-list li:before {
  content: '';
  display: inline-block;
  width: 6px;
  height: 6px;
  background: #409eff;
  border-radius: 50%;
  margin-right: 8px;
  position: absolute;
  left: 0;
  top: 7px;
}
.dialog-main {
  flex: 1;
  padding: 32px 32px 16px 32px;
  background: #fff;
  border-radius: 0 12px 12px 0;
  min-width: 0;
  overflow-y: auto;
  max-height: 60vh;
  display: flex;
  flex-direction: column;
}
.it-form .el-form-item {
  margin-bottom: 22px;
}
.it-form .el-input,
.it-form .el-select,
.it-form .el-textarea {
  border-radius: 8px;
  background: #f6f8fa;
  border: 1px solid #e0e6ed;
}
.it-footer {
  text-align: right;
  margin-top: 18px;
}
.it-btn {
  border-radius: 8px !important;
  padding: 8px 28px !important;
  font-size: 15px !important;
  font-weight: 500;
}
.it-btn[type='primary'] {
  background: linear-gradient(90deg, #409eff 0%, #66b1ff 100%);
  border: none;
}
.custom-dialog .el-dialog {
  margin: 0 auto !important;
  top: 50% !important;
  transform: translateY(-50%) !important;
  border-radius: 18px;
  background: hsl(15, 40%, 98%);
  box-shadow: 0 8px 32px 0 rgba(60, 60, 60, 0.12);
  overflow: hidden;
}
.custom-dialog .el-dialog__body {
  padding: 0;
  background: transparent;
}
.custom-dialog .el-dialog__footer {
  flex-shrink: 0;
  padding: 10px 20px;
  text-align: center;
  border-top: 1px solid #ebeef5;
  background: #fff;
  z-index: 1;
  position: sticky;
  bottom: 0;
}

/* 修复选择框样式 */
:deep(.el-table .el-table__fixed) {
  height: 100% !important;
  overflow: hidden;
}

:deep(.el-table__fixed-right) {
  height: 100% !important;
  overflow: hidden;
}

:deep(.el-table__fixed) {
  height: 100% !important;
  overflow: hidden;
}

:deep(.el-table__fixed-right-patch) {
  background-color: #f6f8fa;
}

:deep(.el-table__fixed-header-wrapper) {
  background-color: #f6f8fa;
}

:deep(.el-table__fixed-body-wrapper) {
  background-color: #fff;
}

/* 确保选择框固定 */
:deep(.el-table__fixed-right .el-table__fixed-header-wrapper) {
  background-color: #f6f8fa;
}

:deep(.el-table__fixed-right .el-table__fixed-body-wrapper) {
  background-color: #fff;
}

:deep(.el-table__fixed-right .el-table__fixed-footer-wrapper) {
  background-color: #f6f8fa;
}

/* 修复选择框位置 */
:deep(.el-table__fixed-right .el-table__fixed-header-wrapper .el-table__header) {
  background-color: #f6f8fa;
}

:deep(.el-table__fixed-right .el-table__fixed-body-wrapper .el-table__body) {
  background-color: #fff;
}

:deep(.el-table__fixed-right .el-table__fixed-footer-wrapper .el-table__footer) {
  background-color: #f6f8fa;
}

/* 编辑器图片样式 */
:deep(.w-e-text-container .w-e-text img) {
  max-width: 100% !important;
  max-height: 300px !important;
  object-fit: contain !important;
  margin: 8px 0 !important;
  border-radius: 4px !important;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1) !important;
}

/* 确保图片容器样式 */
:deep(.w-e-text-container) {
  min-height: 300px !important;
  padding: 12px !important;
}

:deep(.w-e-text-container .w-e-text) {
  padding: 12px !important;
  font-size: 14px !important;
  line-height: 1.6 !important;
}

/* 图片预览相关样式 */
.image-preview-container {
  display: inline-block;
  margin: 8px;
  transition: transform 0.3s ease;
}

.image-preview-container:hover {
  transform: scale(1.05);
}

.preview-image {
  max-width: 200px;
  max-height: 150px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  cursor: pointer;
  object-fit: contain;
}

.preview-content {
  text-align: center;
  padding: 0;
  height: calc(100vh - 120px);
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(0, 0, 0, 0.9);
}

.preview-full-image {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
  cursor: zoom-in;
}

:deep(.image-preview-dialog) {
  margin: 0 !important;
  padding: 0;
  background: transparent;
}

:deep(.image-preview-dialog .el-dialog__header) {
  padding: 10px;
  background: rgba(0, 0, 0, 0.8);
  margin: 0;
}

:deep(.image-preview-dialog .el-dialog__headerbtn) {
  top: 10px;
  right: 10px;
}

:deep(.image-preview-dialog .el-dialog__headerbtn .el-dialog__close) {
  font-size: 20px;
  color: #fff;
}

:deep(.image-preview-dialog .el-dialog__headerbtn:hover .el-dialog__close) {
  color: #409EFF;
}

:deep(.image-preview-dialog .el-dialog__body) {
  padding: 0;
  margin: 0;
  height: calc(100vh - 55px);
  overflow: hidden;
}

:deep(.image-preview-dialog .el-dialog__header .el-dialog__title) {
  color: #fff;
  font-size: 16px;
}

/* 以下为Notice.vue上传弹窗相关样式同步 */
.custom-dialog {
  border-radius: 25px;
  overflow: hidden;
}
.custom-dialog .el-dialog {
  margin-top: 8vh !important;
  max-height: 84vh;
  display: flex;
  flex-direction: column;
  border-radius: 25px;
  overflow: hidden;
}
.custom-dialog .el-dialog__header {
  padding: 15px 20px;
  border-bottom: 1px solid #e68a3f;
}
.custom-dialog .el-dialog__body {
  flex: 1 1 auto;
  overflow-y: auto;
  max-height: calc(84vh - 120px);
  padding: 20px;
  background: #fff;
}
.custom-dialog .el-dialog__footer {
  flex-shrink: 0;
  padding: 10px 20px;
  text-align: center;
  border-top: 1px solid #ebeef5;
  background: #fff;
  z-index: 1;
  position: sticky;
  bottom: 0;
}

/* 上传区域样式 */
.upload-demo {
  width: 100%;
  padding: 20px 0;
  background: #f6f8fa;
  border-radius: 12px;
  border: 1px dashed #d0d7de;
  text-align: center;
}
.upload-demo .el-upload__text {
  font-size: 16px;
  color: #409eff;
}
.upload-demo .el-upload__tip {
  color: #888;
  font-size: 13px;
  margin-top: 8px;
}

/* Markdown预览样式 */
.markdown-preview {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  max-height: 70vh;
  overflow-y: auto;
}

.markdown-preview :deep(h1) {
  font-size: 24px;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.markdown-preview :deep(h2) {
  font-size: 20px;
  margin: 20px 0 15px;
}

.markdown-preview :deep(h3) {
  font-size: 18px;
  margin: 15px 0 10px;
}

.markdown-preview :deep(p) {
  margin: 10px 0;
  line-height: 1.6;
}

.markdown-preview :deep(ul), 
.markdown-preview :deep(ol) {
  padding-left: 20px;
  margin: 10px 0;
}

.markdown-preview :deep(li) {
  margin: 5px 0;
}

.markdown-preview :deep(code) {
  background-color: #f5f5f5;
  padding: 2px 4px;
  border-radius: 3px;
  font-family: Consolas, Monaco, 'Andale Mono', monospace;
}

.markdown-preview :deep(pre) {
  background-color: #f5f5f5;
  padding: 15px;
  border-radius: 4px;
  overflow-x: auto;
  margin: 10px 0;
}

.markdown-preview :deep(pre code) {
  background-color: transparent;
  padding: 0;
}

.markdown-preview :deep(blockquote) {
  border-left: 4px solid #ddd;
  padding-left: 15px;
  color: #666;
  margin: 10px 0;
}

.markdown-preview :deep(table) {
  width: 100%;
  border-collapse: collapse;
  margin: 10px 0;
}

.markdown-preview :deep(th),
.markdown-preview :deep(td) {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

.markdown-preview :deep(th) {
  background-color: #f5f5f5;
}

.markdown-preview :deep(img) {
  max-width: 100%;
  height: auto;
  margin: 10px 0;
  border-radius: 4px;
}

/* 上传对话框样式 */
.custom-dialog {
  border-radius: 25px;
  overflow: hidden;
}

.custom-dialog .el-dialog {
  margin-top: 8vh !important;
  max-height: 84vh;
  display: flex;
  flex-direction: column;
  border-radius: 25px;
  overflow: hidden;
}

.custom-dialog .el-dialog__header {
  padding: 15px 20px;
  border-bottom: 1px solid #e68a3f;
}

.custom-dialog .el-dialog__body {
  flex: 1 1 auto;
  overflow-y: auto;
  max-height: calc(84vh - 120px);
  padding: 20px;
  background: #fff;
}

.custom-dialog .el-dialog__footer {
  flex-shrink: 0;
  padding: 10px 20px;
  text-align: center;
  border-top: 1px solid #ebeef5;
  background: #fff;
  z-index: 1;
  position: sticky;
  bottom: 0;
}

/* 上传组件样式 */
.upload-demo {
  text-align: center;
}

.el-upload {
  width: 100%;
}

.el-upload-dragger {
  width: 100%;
  height: 200px;
}

.el-upload__tip {
  color: #909399;
  font-size: 12px;
  margin-top: 10px;
}

/* 强制选择列居中 */
:deep(.el-table-column--selection .cell) {
  display: flex;
  justify-content: center;
  align-items: center;
}
</style> 