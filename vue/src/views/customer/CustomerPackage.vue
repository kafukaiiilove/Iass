<template>
  <div>
    <div class="header">
      <h1 class="page-title">{{ pageTitle }}</h1>
      <div class="role-indicator" v-if="currentRole !== 'ADMIN'">
        <el-tag 
          :type="currentRole === 'KaiFa' ? 'warning' : 'info'" 
          size="small">
          {{ currentRole === 'KaiFa' ? '开发权限' : '测试权限' }}
        </el-tag>
      </div>
    </div>
    
    <!-- 添加分类选项卡 -->
    <div class="category-tabs">
      <el-tabs v-model="activeCategory" @tab-click="handleCategoryChange">
        <el-tab-pane 
          v-for="category in availableCategories" 
          :key="category.value"
          :label="category.label" 
          :name="category.value">
        </el-tab-pane>
      </el-tabs>
    </div>
    
        <div class="search-bar">
      <el-select v-model="searchType" placeholder="选择查询类型" style="width: 120px;">
        <el-option v-if="version === 'all'" label="Android版本" value="androidVersion"></el-option>
        <el-option v-if="version === 'all'" label="分类" value="category"></el-option>
        <el-option label="时间" value="recordDate"></el-option>
        <el-option label="修复内容" value="fixContent"></el-option>
        <el-option label="开发工程师" value="developer"></el-option>
        <el-option label="测试工程师" value="tester"></el-option>
        <el-option label="测试结果" value="testResult"></el-option>
        <el-option label="镜像ID" value="imageId"></el-option>
        <el-option label="Commit ID" value="commitId"></el-option>
        <el-option label="备注" value="remarks"></el-option>
     
      </el-select>
      
      <!-- 普通文本输入 -->
      <el-input 
        v-if="!['testResult', 'androidVersion', 'category', 'recordDate'].includes(searchType)" 
        :placeholder="getSearchPlaceholder()" 
        style="width: 250px; margin-left: 10px" 
        v-model="searchKeyword">
      </el-input>
      
      <!-- 时间范围选择 -->
      <el-date-picker
        v-if="searchType === 'recordDate'"
        v-model="dateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        value-format="yyyy-MM-dd"
        style="width: 400px; margin-left: 10px;"
        clearable>
      </el-date-picker>
      
      <!-- 测试结果下拉选择 -->
      <el-select 
        v-if="searchType === 'testResult'" 
        v-model="searchKeyword" 
        placeholder="选择测试结果" 
        style="width: 250px; margin-left: 10px;"
        clearable>
        <el-option label="待测试" value="待测试"></el-option>
        <el-option label="测试中" value="测试中"></el-option>
        <el-option label="未通过" value="未通过"></el-option>
        <el-option label="已完成" value="已完成"></el-option>
        <el-option label="已取消" value="已取消"></el-option>
      </el-select>
      
      <!-- Android版本下拉选择 -->
      <el-select 
        v-if="searchType === 'androidVersion'" 
        v-model="searchKeyword" 
        placeholder="选择Android版本" 
        style="width: 250px; margin-left: 10px;"
        clearable>
        <el-option label="Android 10" value="a10"></el-option>
        <el-option label="Android 13" value="a13"></el-option>
        <el-option label="Android 14" value="a14"></el-option>
        <el-option label="Android 15" value="a15"></el-option>
      </el-select>
      
      <!-- 分类下拉选择 -->
      <el-select 
        v-if="searchType === 'category'" 
        v-model="searchKeyword" 
        placeholder="选择分类" 
        style="width: 250px; margin-left: 10px;"
        clearable>
        <el-option label="VMOS" value="vmos"></el-option>
        <el-option label="公共包" value="public"></el-option>
        <el-option label="上海通用" value="shanghai_common"></el-option>
        <el-option label="QT" value="qt"></el-option>
        <el-option label="巴德" value="bade"></el-option>
        <el-option label="毕少" value="bishao"></el-option>
        <el-option label="新硕" value="xinshuo"></el-option>
        <el-option label="GeeLark" value="geelark"></el-option>
        <el-option label="ML" value="ml"></el-option>
        <el-option label="其他" value="other"></el-option>
      </el-select>
      
      <el-button type="primary" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="info" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="operation">
      <el-button 
        v-if="canAdd" 
        type="primary" 
        plain 
        @click="handleAdd">
        新增记录
      </el-button>
      <el-button 
        v-if="canBatchDelete" 
        type="danger" 
        plain 
        @click="delBatch">
        批量删除
      </el-button>
      <el-button type="info" plain @click="testApiConnection">测试API连接</el-button>
    </div>

    <div class="table-container">
      <el-table 
        :data="tableData" 
        stripe 
        class="custom-table"
        @selection-change="handleSelectionChange"
        :header-cell-style="headerCellStyle"
        :cell-style="cellStyle">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="recordDate" label="时间" width="120" align="center" sortable>
          <template slot-scope="scope">
            {{ formatDateTimeDisplay(scope.row.recordDate) }}
          </template>
        </el-table-column>
        <!-- 所有版本时显示Android版本和分类列 -->
        <el-table-column v-if="version === 'all'" prop="androidVersion" label="Android版本" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="small" :type="getVersionTagType(scope.row.androidVersion)">
              {{ getVersionText(scope.row.androidVersion) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column v-if="version === 'all'" prop="category" label="分类" width="100" align="center">
          <template slot-scope="scope">
            <el-tag size="small" type="info">
              {{ getCategoryText(scope.row.category) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="镜像包地址" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link 
              v-if="scope.row.packageUrl" 
              type="primary" 
              :href="scope.row.packageUrl"
              target="_blank"
              :underline="false">
              {{ scope.row.packageUrl }}
            </el-link>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="fixContent" label="修复内容" show-overflow-tooltip></el-table-column>
        <el-table-column prop="developer" label="开发工程师" width="120" align="center"></el-table-column>
        <el-table-column prop="commitId" label="Commit ID" width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="tester" label="测试工程师" width="120" align="center"></el-table-column>
        <el-table-column label="测试结果" width="140" align="center">
          <template slot-scope="scope">
            <el-select 
              v-model="scope.row.testResult" 
              size="small" 
              style="width: 100px;"
              @change="handleTestResultChange(scope.row)"
              :class="'test-result-select ' + getTestResultClass(scope.row.testResult)"
              :disabled="!isFieldEditable('testResult')">
              <el-option label="待测试" value="待测试"></el-option>
              <el-option label="测试中" value="测试中"></el-option>
              <el-option label="未通过" value="未通过"></el-option>
              <el-option label="已完成" value="已完成"></el-option>
              <el-option label="已取消" value="已取消"></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="imageId" label="镜像ID" width="150" show-overflow-tooltip></el-table-column>
        <el-table-column label="备注" show-overflow-tooltip>
          <template slot-scope="scope">
            <div v-html="formatContentWithImagePreview(scope.row.remarks)" class="remarks-content"></div>
          </template>
        </el-table-column>
        <el-table-column label="操作" :width="canDelete ? '180' : '120'" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button 
              v-if="canEdit" 
              type="primary" 
              size="mini" 
              @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-button 
              v-if="canDelete" 
              type="danger" 
              size="mini" 
              @click="handleDelete(scope.row.id)">
              删除
            </el-button>
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
      :title="getDialogTitle()"
      :visible.sync="dialogFormVisible"
      width="70%"
      :close-on-click-modal="false"
      destroy-on-close
      top="3vh"
      class="customer-package-dialog">
      <el-form :model="form" label-width="120px" :rules="rules" ref="formRef" class="customer-form">
        <!-- 在All Version视图下或编辑时显示Android版本和分类选择 -->
        <el-row v-if="version === 'all' || form.id" :gutter="20">
          <el-col :span="12">
            <el-form-item label="Android版本" prop="androidVersion">
              <el-select 
                v-model="form.androidVersion" 
                placeholder="请选择Android版本" 
                style="width: 100%" 
                :disabled="version !== 'all' || !isFieldEditable('androidVersion')">
                <el-option label="Android 10" value="a10"></el-option>
                <el-option label="Android 13" value="a13"></el-option>
                <el-option label="Android 14" value="a14"></el-option>
                <el-option label="Android 15" value="a15"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分类" prop="category">
              <el-select 
                v-model="form.category" 
                placeholder="请选择分类" 
                style="width: 100%"
                :disabled="!isFieldEditable('category')">
                <el-option 
                  v-for="category in getAvailableCategoriesForVersion(form.androidVersion || version)" 
                  :key="category.value"
                  :label="category.label" 
                  :value="category.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="时间" prop="recordDate">
              <el-date-picker 
                v-model="form.recordDate" 
                type="datetime" 
                placeholder="选择日期时间" 
                value-format="yyyy-MM-dd HH:mm"
                style="width: 100%"
                :disabled="!isFieldEditable('recordDate')">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="镜像包地址" prop="packageUrl">
              <el-input 
                v-model="form.packageUrl" 
                autocomplete="off" 
                placeholder="请输入镜像包下载地址"
                @input="handlePackageUrlChange"
                :disabled="!isFieldEditable('packageUrl')">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="修复内容" prop="fixContent">
          <el-input 
            type="textarea" 
            :rows="6" 
            v-model="form.fixContent" 
            autocomplete="off"
            placeholder="请详细描述修复的内容和问题"
            :disabled="!isFieldEditable('fixContent')">
          </el-input>
        </el-form-item>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="开发工程师" prop="developer">
              <el-input 
                v-model="form.developer" 
                autocomplete="off" 
                placeholder="请输入开发工程师姓名"
                :disabled="!isFieldEditable('developer')">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Commit ID" prop="commitId">
              <el-input 
                v-model="form.commitId" 
                autocomplete="off" 
                placeholder="请输入Commit ID"
                :disabled="!isFieldEditable('commitId')">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="测试工程师" prop="tester">
              <el-input 
                v-model="form.tester" 
                autocomplete="off" 
                placeholder="请输入测试工程师姓名"
                :disabled="!isFieldEditable('tester')">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="测试结果" prop="testResult">
              <el-select 
                v-model="form.testResult" 
                placeholder="请选择测试结果" 
                style="width: 100%"
                :disabled="!isFieldEditable('testResult')">
                <el-option 
                  v-for="option in testResultOptions"
                  :key="option.value"
                  :label="option.label" 
                  :value="option.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="镜像ID" prop="imageId">
              <el-input 
                v-model="form.imageId" 
                autocomplete="off" 
                placeholder="请输入镜像ID"
                :disabled="!isFieldEditable('imageId')">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="备注" prop="remarks">
          <div class="remarks-editor-wrapper">
            <div class="editor-container">
              <Toolbar
                style="border-bottom: 1px solid #dcdfe6"
                :editor="remarksEditorRef"
                :defaultConfig="toolbarConfig"
                mode="default"
              />
              <Editor
                style="height: 200px; overflow-y: hidden;"
                v-model="form.remarks"
                :defaultConfig="editorConfig"
                mode="default"
                @onCreated="handleRemarksEditorCreated"
                @onChange="handleRemarksChange"
                :disabled="!isFieldEditable('remarks')"
              />
            </div>
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false" class="cancel-btn">取 消</el-button>
        <el-button type="primary" @click="save" class="confirm-btn">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 图片预览对话框 -->
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
      <div class="preview-controls">
        <el-button size="small" @click="zoomIn">放大</el-button>
        <el-button size="small" @click="zoomOut">缩小</el-button>
        <el-button size="small" @click="resetZoom">重置</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { 
  hasDeletePermission, 
  hasBatchDeletePermission, 
  hasAddPermission, 
  hasEditPermission,
  isFieldEditable,
  filterEditableFormData,
  getCurrentUserRole
} from '@/utils/permissions'

export default {
  name: "CustomerPackage",
  components: {
    Editor,
    Toolbar
  },
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      searchType: 'fixContent', // 默认搜索修复内容
      searchKeyword: '',
      dateRange: [], // 时间范围搜索
      dialogFormVisible: false,
      form: {},
      ids: [],
      rules: {
        recordDate: [{ required: true, message: '请选择时间', trigger: 'blur' }],
        //packageUrl: [{ required: true, message: '请输入镜像包地址', trigger: 'blur' }],
        packageUrl: [],
        androidVersion: [],
        category: [],
      },
      headerCellStyle: {
        background: '#f8f8f9',
        color: '#333',
        fontWeight: 'bold',
        textAlign: 'center'
      },
      cellStyle: {
        textAlign: 'center'
      },
      version: this.$route.params.version,
      category: this.$route.params.category || (this.$route.params.version === 'all' ? 'all' : 'vmos'),
      activeCategory: this.$route.params.category || (this.$route.params.version === 'all' ? 'all' : 'vmos'),
      saving: false,
      // 富文本编辑器相关
      remarksEditorRef: null,
      toolbarConfig: {},
      editorConfig: null,
      // 图片预览相关
      previewVisible: false,
      previewImageUrl: '',
      imageScale: 1
    };
  },
  computed: {
    pageTitle() {
      const versionMap = {
        'a10': 'Android 10',
        'a13': 'Android 13',
        'a14': 'Android 14',
        'a15': 'Android 15',
        'all': '所有版本'
      };
      const versionText = versionMap[this.version] || '';
      return `客户镜像包记录 - ${versionText}`;
    },
    availableCategories() {
      // 如果是所有版本，显示All Version选项卡
      if (this.version === 'all') {
        return [
          { label: 'All Version', value: 'all' }
        ];
      }
      
      // 根据不同的Android版本返回不同的分类选项
      // 基础分类（不包含通用类）：VMOS -> 公共包 -> 其他
      const baseCategories = [
        { label: 'VMOS', value: 'vmos' },
        { label: '公共包', value: 'public' },
        { label: '其他', value: 'other' }
      ];
      
      const categoryMap = {
        'a10': [
          ...baseCategories
        ],
        'a13': [
          { label: 'VMOS', value: 'vmos' },
          { label: '公共包', value: 'public' },
          { label: '上海通用', value: 'shanghai_common' },
          { label: 'QT', value: 'qt' },
          { label: '巴德', value: 'bade' },
          { label: '毕少', value: 'bishao' },
          { label: '其他', value: 'other' }
        ],
        'a14': [
          { label: 'VMOS', value: 'vmos' },
          { label: '公共包', value: 'public' },
          { label: 'QT', value: 'qt' },
          { label: '新硕', value: 'xinshuo' },
          { label: 'GeeLark', value: 'geelark' },
          { label: 'ML', value: 'ml' },
          { label: '毕少', value: 'bishao' },
          { label: '其他', value: 'other' }
        ],
        'a15': [
          { label: 'VMOS', value: 'vmos' },
          { label: '公共包', value: 'public' },
          { label: 'QT', value: 'qt' },
          { label: 'ML', value: 'ml' },
          { label: 'GeeLark', value: 'geelark' },
          { label: '其他', value: 'other' }
        ]
      };
      return categoryMap[this.version] || baseCategories;
    },
    // 权限相关计算属性
    canDelete() {
      return hasDeletePermission()
    },
    canBatchDelete() {
      return hasBatchDeletePermission()
    },
    canAdd() {
      return hasAddPermission()
    },
    canEdit() {
      return hasEditPermission()
    },
    currentRole() {
      return getCurrentUserRole()
    },
    // 测试结果选项（测试工程师使用）
    testResultOptions() {
      return [
        { label: '待测试', value: '待测试' },
        { label: '测试中', value: '测试中' },
        { label: '已完成', value: '已完成' },
        { label: '测试失败', value: '测试失败' }
      ]
    }
  },
  created() {
    this.initEditorConfig();
    this.load(1);
  },
  mounted() {
    // 监听图片预览事件
    document.addEventListener('preview-image', (e) => {
      this.handlePreviewImage(e.detail);
    });
  },
  beforeDestroy() {
    // 清理事件监听
    document.removeEventListener('preview-image', this.handlePreviewImage);
    // 清理富文本编辑器
    if (this.remarksEditorRef) {
      this.remarksEditorRef.destroy();
    }
  },
    watch: {
    '$route.params': {
      handler(newParams) {
        this.version = newParams.version;
        this.category = newParams.category || (newParams.version === 'all' ? 'all' : 'vmos');
        this.activeCategory = this.category;
        this.load(1);
      },
      deep: true
    },
    'form.androidVersion': {
      handler(newVersion) {
        // 当Android版本改变时，重置分类选择
        if (newVersion && this.form.category) {
          const availableCategories = this.getAvailableCategoriesForVersion(newVersion);
          const categoryExists = availableCategories.some(cat => cat.value === this.form.category);
          if (!categoryExists) {
            this.form.category = 'vmos'; // 默认选择VMOS
          }
        }
      }
    }
  },
  methods: {
    // 权限相关方法
    isFieldEditable(fieldName) {
      const isAddMode = !this.form.id; // 没有ID表示新增模式
      return isFieldEditable(fieldName, isAddMode)
    },
    
    getDialogTitle() {
      const isEdit = !!this.form.id;
      const roleText = this.currentRole === 'KaiFa' ? ' (开发)' : 
                       this.currentRole === 'CeShi' ? ' (测试)' : '';
      return (isEdit ? '编辑记录' : '新增记录') + roleText;
    },
    
    // 初始化编辑器配置
    initEditorConfig() {
      const user = JSON.parse(localStorage.getItem('xm-user') || '{}');
      this.editorConfig = {
        placeholder: '请输入备注信息...',
        MENU_CONF: {
          uploadImage: {
            server: this.$baseUrl + '/api/manager/files/upload',
            fieldName: 'file',
            maxFileSize: 5 * 1024 * 1024, // 5M
            allowedFileTypes: ['image/*'],
            headers: {
              'token': user.token || ''
            },
            customInsert: (res, insertFn) => {
              console.log('上传响应:', res);
              if (res.code === '200') {
                // 确保图片URL是完整的
                let imageUrl = res.data;
                if (!imageUrl.startsWith('http')) {
                  imageUrl = this.$baseUrl + imageUrl;
                }
                insertFn(imageUrl);
                this.$message.success('图片上传成功');
              } else {
                this.$message.error(res.msg || '图片上传失败');
              }
            },
            onError: (file, err, res) => {
              console.error('上传失败:', err, res);
              this.$message.error('图片上传失败，请重试');
            }
          }
        }
      };
    },
    // 测试API连接
    testApiConnection() {
      console.log('开始测试API连接');
      this.$request.get('/manager/customer_package/simple-test')
        .then(res => {
          console.log('API连接测试成功:', res);
          this.$message({
            message: 'API连接正常: ' + res.data,
            type: 'success',
            duration: 1500
          });
        })
        .catch(err => {
          console.error('API连接测试失败:', err);
          this.$message({
            message: 'API连接失败: ' + (err.message || err.msg || '未知错误'),
            type: 'error',
            duration: 2000
          });
        });
    },
    getTestResultType(testResult) {
      const typeMap = {
        '待测试': '',
        '测试中': 'warning',
        '未通过': 'danger',
        '已完成': 'success',
        '已取消': 'info'
      };
      return typeMap[testResult] || '';
    },
    getTestResultClass(testResult) {
      const classMap = {
        '待测试': 'pending',
        '测试中': 'testing',
        '未通过': 'failed',
        '已完成': 'completed',
        '已取消': 'cancelled'
      };
      return classMap[testResult] || 'pending';
    },
    handleTestResultChange(row) {
      // 检查权限
      if (!this.isFieldEditable('testResult')) {
        this.$message.warning('您没有权限修改测试结果');
        this.load(this.pageNum); // 恢复原值
        return;
      }
      
      // 根据权限过滤数据
      let updateData = row;
      if (this.currentRole !== 'ADMIN') {
        const isAddMode = false; // 表格中的编辑是修改现有记录
        updateData = filterEditableFormData(row, isAddMode);
      }
      
      // 直接更新测试结果
      this.$request({
        url: '/manager/customer_package/update',
        method: 'PUT',
        data: updateData
      }).then(res => {
        if (res.code === '200') {
          this.$message({
            message: '测试结果更新成功',
            type: 'success',
            duration: 1500
          });
          this.load(this.pageNum); // 刷新当前页数据
        } else {
          this.$message({
            message: res.msg || '更新失败',
            type: 'error',
            duration: 2000
          });
          // 如果更新失败，恢复原来的值
          this.load(this.pageNum);
        }
      }).catch(err => {
        this.$message({
          message: '更新失败',
          type: 'error',
          duration: 2000
        });
        // 如果更新失败，恢复原来的值
        this.load(this.pageNum);
      });
    },
    getSearchPlaceholder() {
      const placeholderMap = {
        'fixContent': '请输入修复内容关键字',
        'developer': '请输入开发工程师姓名',
        'tester': '请输入测试工程师姓名',
        'imageId': '请输入镜像ID',
        'commitId': '请输入Commit ID',
        'remarks': '请输入备注关键字'
      };
      return placeholderMap[this.searchType] || '请输入关键字';
    },
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum;
      
      // 构建搜索参数
      const params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };
      
      // 如果不是查看所有版本，添加版本和分类过滤
      if (this.version !== 'all') {
        params.androidVersion = this.version;
        params.category = this.category;
      }
      
      // 根据搜索类型添加对应的搜索参数
      if (this.searchType === 'recordDate' && this.dateRange && this.dateRange.length === 2) {
        // 时间范围搜索
        params.startDate = this.dateRange[0];
        params.endDate = this.dateRange[1];
      } else if (this.searchKeyword) {
        // 如果在All Version视图下搜索androidVersion或category，需要特殊处理
        if (this.version === 'all' && (this.searchType === 'androidVersion' || this.searchType === 'category')) {
          params[this.searchType] = this.searchKeyword;
        } else if (this.version !== 'all' || (this.searchType !== 'androidVersion' && this.searchType !== 'category')) {
          // 普通搜索或非All Version视图下的搜索
          params[this.searchType] = this.searchKeyword;
        }
      }
      
      this.$request.get('/manager/customer_package/selectPage', { params }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data?.list || [];
          this.total = res.data?.total || 0;
        } else {
          this.$message({
            message: res.msg || '获取数据失败',
            type: 'error',
            duration: 2000
          });
        }
      }).catch(err => {
        console.error('获取数据失败:', err);
        this.$message({
          message: '获取数据失败',
          type: 'error',
          duration: 2000
        });
      });
    },
    handleAdd() {
      this.form = {
        recordDate: this.formatDateTime(new Date()),
        testResult: '待测试'
      };
      
      // 如果不是All Version视图，设置默认的版本和分类
      if (this.version !== 'all') {
        this.form.androidVersion = this.version;
        this.form.category = this.category;
      }
      
      // 重新初始化编辑器配置以获取最新token
      this.initEditorConfig();
      this.dialogFormVisible = true;
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row));
      // 重新初始化编辑器配置以获取最新token
      this.initEditorConfig();
      this.dialogFormVisible = true;
    },
    formatDateTime(date) {
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}`;
    },
    save() {
      // 动态设置验证规则
      if (this.version === 'all' || this.form.id) {
        this.rules.androidVersion = [{ required: true, message: '请选择Android版本', trigger: 'change' }];
        this.rules.category = [{ required: true, message: '请选择分类', trigger: 'change' }];
      } else {
        this.rules.androidVersion = [];
        this.rules.category = [];
      }
      
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          // 防止重复提交
          if (this.saving) {
            this.$message.warning('正在保存中，请稍候...');
            return;
          }
          this.saving = true;
          
          // 显示加载提示
          const loading = this.$loading({
            lock: true,
            text: '保存中...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });

          // 将版本和分类信息附加到表单数据
          if (this.version !== 'all') {
            // 只有在新增时才设置默认版本和分类，编辑时使用表单中的值
            if (!this.form.id) {
              this.form.androidVersion = this.version;
              this.form.category = this.category;
            } else {
              // 编辑时，如果表单中没有版本信息，则使用当前页面的版本
              if (!this.form.androidVersion) {
                this.form.androidVersion = this.version;
              }
              // 编辑时保持用户在表单中选择的分类
            }
          }

          const url = this.form.id ? '/manager/customer_package/update' : '/manager/customer_package/add';
          const method = this.form.id ? 'PUT' : 'POST';
          
          // 根据权限过滤表单数据
          let formData = this.form;
          const isAddMode = !this.form.id;
          if (this.currentRole !== 'ADMIN') {
            formData = filterEditableFormData(this.form, isAddMode);
            console.log('过滤后的表单数据:', formData);
          }

          console.log('开始保存数据:', {
            url: url,
            method: method,
            data: formData,
            originalData: this.form,
            isEdit: !!this.form.id,
            currentVersion: this.version,
            currentCategory: this.category,
            userRole: this.currentRole
          });

          const startTime = Date.now();

          this.$request({
            url: url,
            method: method,
            data: formData,
            timeout: 30000 // 30秒超时
          }).then(res => {
            const endTime = Date.now();
            console.log('保存完成，耗时:', (endTime - startTime) + 'ms', res);
            
            loading.close();
            this.saving = false;
            if (res.code === '200') {
              this.$message({
                message: '保存成功',
                type: 'success',
                duration: 1500 // 1.5秒后自动关闭
              });
              this.dialogFormVisible = false;
              this.load(1);
              
              // 如果是新增记录，通知父组件更新消息计数
              if (!this.form.id) {
                this.$emit('record-added');
                // 通过事件总线通知Manager组件
                this.$root.$emit('new-record-added');
              }
            } else {
              this.$message({
                message: res.msg || '保存失败',
                type: 'error',
                duration: 2000 // 2秒后自动关闭
              });
            }
          }).catch(err => {
            const endTime = Date.now();
            console.error('保存失败，耗时:', (endTime - startTime) + 'ms', err);
            
            loading.close();
            this.saving = false;
            
            let errorMsg = '保存失败';
            if (err.code === 'ECONNABORTED' || err.message.includes('timeout')) {
              errorMsg = '请求超时，请检查网络连接或稍后重试';
            } else if (err.response) {
              errorMsg = `保存失败: ${err.response.status} ${err.response.statusText}`;
            } else if (err.message) {
              errorMsg = `保存失败: ${err.message}`;
            }
            
            this.$message({
              message: errorMsg,
              type: 'error',
              duration: 2000 // 2秒后自动关闭
            });
          });
        }
      });
    },
    handleDelete(id) {
      this.$confirm('您确定删除这条记录吗？', '确认删除', { type: "warning" }).then(() => {
        this.$request.delete('/manager/customer_package/delete/' + id).then(res => {
          if (res.code === '200') {
            this.$message({
              message: '删除成功',
              type: 'success',
              duration: 1500
            });
            this.load(1);
          } else {
            this.$message({
              message: res.msg || '删除失败',
              type: 'error',
              duration: 2000
            });
          }
        });
      }).catch(() => {});
    },
    handleSelectionChange(rows) {
      this.ids = rows.map(v => v.id);
    },
    delBatch() {
      if (!this.ids.length) {
        this.$message({
          message: '请选择数据',
          type: 'warning',
          duration: 1500
        });
        return;
      }
      this.$confirm(`您确定批量删除这 ${this.ids.length} 条数据吗？`, '确认删除', { type: "warning" }).then(() => {
        this.$request.delete('/manager/customer_package/delete/batch', { data: this.ids }).then(res => {
          if (res.code === '200') {
            this.$message({
              message: '批量删除成功',
              type: 'success',
              duration: 1500
            });
            this.load(1);
          } else {
            this.$message({
              message: res.msg || '批量删除失败',
              type: 'error',
              duration: 2000
            });
          }
        });
      }).catch(() => {});
    },
    reset() {
      this.searchType = 'fixContent';
      this.searchKeyword = '';
      this.dateRange = [];
      this.load(1);
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum);
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.load(1);
    },
    handleCategoryChange(tab) {
      this.category = tab.name;
      this.load(1);
    },
    getVersionText(version) {
      const versionMap = {
        'a10': 'Android 10',
        'a13': 'Android 13',
        'a14': 'Android 14',
        'a15': 'Android 15'
      };
      return versionMap[version] || version;
    },
    getVersionTagType(version) {
      const typeMap = {
        'a10': 'primary',
        'a13': 'success',
        'a14': 'warning',
        'a15': 'danger'
      };
      return typeMap[version] || '';
    },
    getCategoryText(category) {
      const categoryMap = {
        'shanghai_common': '上海通用',
        'public': '公共包',
        'vmos': 'VMOS',
        'qt': 'QT',
        'bade': '巴德',
        'bishao': '毕少',
        'xinshuo': '新硕',
        'geelark': 'GeeLark',
        'ml': 'ML',
        'other': '其他'
      };
      return categoryMap[category] || category;
    },
    formatDateTimeDisplay(dateTime) {
      if (!dateTime) return '';
      // 如果已经是 yyyy-MM-dd HH:mm 格式，直接返回
      if (dateTime.length === 16 && dateTime.includes(' ') && !dateTime.includes(':')) {
        return dateTime;
      }
      // 如果包含秒数，去掉秒数部分
      if (dateTime.includes(':') && dateTime.split(':').length === 3) {
        return dateTime.substring(0, 16); // 截取到分钟
      }
      return dateTime;
    },
    getAvailableCategoriesForVersion(version) {
      // 基础分类（不包含通用类）：VMOS -> 公共包 -> 其他
      const baseCategories = [
        { label: 'VMOS', value: 'vmos' },
        { label: '公共包', value: 'public' },
        { label: '其他', value: 'other' }
      ];
      
      const categoryMap = {
        'a10': [
          ...baseCategories
        ],
        'a13': [
          { label: 'VMOS', value: 'vmos' },
          { label: '公共包', value: 'public' },
          { label: '上海通用', value: 'shanghai_common' },
          { label: 'QT', value: 'qt' },
          { label: '巴德', value: 'bade' },
          { label: '毕少', value: 'bishao' },
          { label: '其他', value: 'other' }
        ],
        'a14': [
          { label: 'VMOS', value: 'vmos' },
          { label: '公共包', value: 'public' },
          { label: 'QT', value: 'qt' },
          { label: '新硕', value: 'xinshuo' },
          { label: 'GeeLark', value: 'geelark' },
          { label: 'ML', value: 'ml' },
          { label: '毕少', value: 'bishao' },
          { label: '其他', value: 'other' }
        ],
        'a15': [
          { label: 'VMOS', value: 'vmos' },
          { label: '公共包', value: 'public' },
          { label: 'QT', value: 'qt' },
          { label: 'ML', value: 'ml' },
          { label: 'GeeLark', value: 'geelark' },
          { label: '其他', value: 'other' }
        ]
      };
      return categoryMap[version] || baseCategories;
    },
    // 富文本编辑器相关方法
    handleRemarksEditorCreated(editor) {
      this.remarksEditorRef = editor;
      console.log('富文本编辑器创建成功');
    },
    handleRemarksChange(editor) {
      this.form.remarks = editor.getHtml();
    },
    // 图片预览相关方法
    formatContentWithImagePreview(content) {
      if (!content) return '';
      
      // 将图片标签转换为可点击预览的格式
      return content.replace(/<img[^>]+src="([^">]+)"[^>]*>/g, (match, src) => {
        // 处理相对路径为绝对路径
        let fullUrl = src;
        if (!src.startsWith('http')) {
          fullUrl = this.$baseUrl + src;
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
    zoomIn() {
      this.imageScale = Math.min(this.imageScale + 0.2, 5);
    },
    zoomOut() {
      this.imageScale = Math.max(this.imageScale - 0.2, 0.2);
    },
    resetZoom() {
      this.imageScale = 1;
    },
    handlePackageUrlChange(value) {
      // 当镜像包地址输入时，自动提取Commit ID
      if (value && typeof value === 'string') {
        // 多种匹配模式来提取Commit ID
        let commitIdMatch = null;
        
        // 模式1: 匹配文件名末尾的7-8位十六进制字符 (如: _4292145.tar)
        commitIdMatch = value.match(/[_-]([a-f0-9]{7,8})(?:\.[^.]*)?$/i);
        
        // 模式2: 匹配任何分支名_后面的7-8位字符 (如: master_4292145.tar, dev_abc1234.tar)
        if (!commitIdMatch) {
          commitIdMatch = value.match(/[a-zA-Z]+[_-]([a-f0-9]{7,8})(?:\.[^.]*)?$/i);
        }
        
        // 模式3: 匹配任何位置的7-8位十六进制字符（在下划线或横线后）
        if (!commitIdMatch) {
          commitIdMatch = value.match(/[_-]([a-f0-9]{7,8})(?:[_-]|\.)/i);
        }
        
        // 模式4: 匹配文件名中最后一个可能的commit ID格式
        if (!commitIdMatch) {
          const matches = value.match(/[_-]([a-f0-9]{7,8})/gi);
          if (matches && matches.length > 0) {
            // 取最后一个匹配的结果
            const lastMatch = matches[matches.length - 1];
            commitIdMatch = [lastMatch, lastMatch.substring(1)]; // 去掉前面的_或-
          }
        }
        
        if (commitIdMatch && commitIdMatch[1]) {
          // 提取到7-8位字符，设置为Commit ID
          this.form.commitId = commitIdMatch[1];
          console.log('自动提取Commit ID:', commitIdMatch[1]);
        } else {
          console.log('未能从URL中提取到Commit ID:', value);
        }
      }
    }
  }
};
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-title {
  font-size: 24px;
  color: #333;
  margin: 0;
  flex: 1;
  text-align: left;
}

.role-indicator {
  display: flex;
  align-items: center;
  margin-left: auto;
}
.category-tabs {
  margin-bottom: 20px;
}
.category-tabs :deep(.el-tabs__header) {
  margin-bottom: 0;
}
.category-tabs :deep(.el-tabs__nav-wrap::after) {
  height: 1px;
}
.search-bar {
  margin-bottom: 20px;
}
.operation {
  margin-bottom: 20px;
}
.table-container {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  overflow: hidden;
}
.custom-table {
  width: 100%;
}
.pagination {
  margin-top: 20px;
  text-align: left;
  padding: 16px 0;
}

:deep(.el-pagination) {
  justify-content: flex-start;
}

:deep(.el-pagination .btn-prev),
:deep(.el-pagination .btn-next) {
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  background: #fff;
  color: #606266;
  padding: 0 8px;
  margin: 0 2px;
}

:deep(.el-pagination .btn-prev:hover),
:deep(.el-pagination .btn-next:hover) {
  color: #409eff;
  border-color: #409eff;
}

:deep(.el-pagination .el-pager li) {
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  background: #fff;
  color: #606266;
  margin: 0 2px;
  min-width: 32px;
  height: 32px;
  line-height: 30px;
}

:deep(.el-pagination .el-pager li:hover) {
  color: #409eff;
  border-color: #409eff;
}

:deep(.el-pagination .el-pager li.active) {
  background-color: #409eff;
  border-color: #409eff;
  color: #fff;
}

:deep(.el-pagination .el-pagination__total) {
  color: #606266;
  font-weight: 500;
}

:deep(.el-pagination .el-pagination__sizes) {
  margin-left: 16px;
}

:deep(.el-pagination .el-pagination__jump) {
  margin-left: 16px;
  color: #606266;
}

/* 弹窗样式优化 */
:deep(.customer-package-dialog) {
  border-radius: 8px;
}

:deep(.customer-package-dialog .el-dialog__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 8px 8px 0 0;
  padding: 20px;
}

:deep(.customer-package-dialog .el-dialog__title) {
  color: white;
  font-size: 18px;
  font-weight: 600;
}

:deep(.customer-package-dialog .el-dialog__headerbtn .el-dialog__close) {
  color: white;
  font-size: 18px;
}

:deep(.customer-package-dialog .el-dialog__body) {
  padding: 30px;
  background-color: #fafbfc;
}

:deep(.customer-package-dialog .el-dialog__footer) {
  background-color: #f8f9fa;
  border-top: 1px solid #e9ecef;
  padding: 20px 30px;
  border-radius: 0 0 8px 8px;
}

.customer-form {
  background-color: white;
  padding: 20px;
  border-radius: 6px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.customer-form :deep(.el-form-item__label) {
  font-weight: 600;
  color: #2c3e50;
}

.customer-form :deep(.el-input__inner) {
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  transition: all 0.3s;
}

.customer-form :deep(.el-input__inner:focus) {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64,158,255,0.2);
}

.customer-form :deep(.el-textarea__inner) {
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  transition: all 0.3s;
}

.customer-form :deep(.el-textarea__inner:focus) {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64,158,255,0.2);
}

.customer-form :deep(.el-select .el-input__inner) {
  border-radius: 4px;
}

.dialog-footer {
  text-align: right;
}

.cancel-btn {
  border-radius: 4px;
  padding: 10px 20px;
  font-weight: 500;
}

.confirm-btn {
  border-radius: 4px;
  padding: 10px 20px;
  font-weight: 500;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}

.confirm-btn:hover {
  background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%);
}

/* 测试结果标签样式 */
:deep(.el-tag--warning) {
  background-color: #fdf6ec;
  border-color: #f5dab1;
  color: #e6a23c;
}

:deep(.el-tag--danger) {
  background-color: #fef0f0;
  border-color: #fbc4c4;
  color: #f56c6c;
}

:deep(.el-tag--success) {
  background-color: #f0f9ff;
  border-color: #b3d8ff;
  color: #67c23a;
}

:deep(.el-tag--info) {
  background-color: #f4f4f5;
  border-color: #d3d4d6;
  color: #909399;
}

/* 测试结果下拉选择器样式 */
.test-result-select :deep(.el-input__inner) {
  border-radius: 15px;
  font-size: 12px;
  font-weight: 500;
  text-align: center;
  border-width: 1px;
  transition: all 0.3s;
}

.test-result-select :deep(.el-input__suffix) {
  right: 8px;
}

.test-result-select :deep(.el-input__suffix-inner) {
  color: inherit;
}

/* 不同状态的颜色样式 */
.test-result-select.pending :deep(.el-input__inner) {
  background-color: #f4f4f5;
  border-color: #d3d4d6;
  color: #909399;
}

.test-result-select.testing :deep(.el-input__inner) {
  background-color: #fdf6ec;
  border-color: #f5dab1;
  color: #e6a23c;
}

.test-result-select.failed :deep(.el-input__inner) {
  background-color: #fef0f0;
  border-color: #fbc4c4;
  color: #f56c6c;
}

.test-result-select.completed :deep(.el-input__inner) {
  background-color: #f0f9ff;
  border-color: #b3d8ff;
  color: #67c23a;
}

.test-result-select.cancelled :deep(.el-input__inner) {
  background-color: #f4f4f5;
  border-color: #d3d4d6;
  color: #909399;
}

/* 悬停效果 */
.test-result-select:hover :deep(.el-input__inner) {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64,158,255,0.1);
}

/* 备注编辑器样式 */
.remarks-editor-wrapper {
  position: relative;
}

.editor-container {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}

.editor-toggle {
  text-align: right;
}

.remarks-content {
  max-height: 120px;
  overflow: hidden;
  word-break: break-all;
  line-height: 1.5;
  text-align: left;
  padding: 8px;
}

/* 图片预览容器样式 */
.image-preview-container {
  display: inline-block;
  cursor: pointer;
  margin: 4px;
  transition: all 0.3s;
  border-radius: 4px;
  overflow: hidden;
}

.image-preview-container:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 8px rgba(0,0,0,0.2);
}

.preview-image {
  max-width: 150px;
  max-height: 100px;
  display: block;
  object-fit: cover;
  border-radius: 4px;
}

.remarks-content .preview-image {
  max-width: 200px;
  max-height: 150px;
  display: inline-block;
  vertical-align: middle;
  margin: 8px auto;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  object-fit: contain;
}

/* 图片预览对话框样式 */
:deep(.image-preview-dialog) {
  background: rgba(0, 0, 0, 0.8);
}

:deep(.image-preview-dialog .el-dialog) {
  background: transparent;
  box-shadow: none;
}

:deep(.image-preview-dialog .el-dialog__header) {
  display: none;
}

:deep(.image-preview-dialog .el-dialog__body) {
  padding: 0;
  background: transparent;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.preview-content {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}

.preview-full-image {
  max-width: 90%;
  max-height: 90%;
  object-fit: contain;
  user-select: none;
  cursor: grab;
}

.preview-full-image:active {
  cursor: grabbing;
}

.preview-controls {
  position: fixed;
  bottom: 50px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 9999;
  background: rgba(0, 0, 0, 0.6);
  padding: 10px 20px;
  border-radius: 25px;
  display: flex;
  gap: 10px;
}

.preview-controls .el-button {
  background: rgba(255, 255, 255, 0.9);
  border: none;
  color: #333;
}

.preview-controls .el-button:hover {
  background: white;
}

/* 富文本编辑器样式 */
:deep(.w-e-text-container) {
  min-height: 200px !important;
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
</style> 