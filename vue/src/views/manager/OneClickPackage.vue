<template>
  <div class="package-container">
    <div class="header">
      <h2>一键打包</h2>
      <p>快速触发GitLab CI/CD流水线，实现自动化打包</p>
    </div>

    <div class="package-section">
      <el-row :gutter="20">
        <el-col :span="12">
          <div class="command-display">
            <h3>构建命令</h3>
            <div class="command-list">
              <div class="command-item" v-for="(cmd, key) in commands" :key="key">
                <div class="command-header">
                  <el-tag :type="getTagType(key)" size="medium">{{ key.toUpperCase() }}</el-tag>
                  <span class="project-id">项目ID: {{ getProjectId(cmd) }}</span>
                </div>
                <div class="command-content">
                  <el-input
                    type="textarea"
                    :value="cmd"
                    readonly
                    :rows="3"
                    class="command-textarea"
                  />
                  <el-button 
                    type="text" 
                    icon="el-icon-document-copy" 
                    @click="copyCommand(cmd)"
                    class="copy-btn"
                  >
                    复制
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </el-col>

        <el-col :span="12">
          <div class="package-control">
            <h3>打包控制</h3>
            
            <div class="control-form">
              <el-form :model="packageForm" label-width="100px">
                <el-form-item label="选择版本">
                  <el-select 
                    v-model="packageForm.version" 
                    placeholder="请选择Android版本"
                    style="width: 100%"
                  >
                    <el-option label="Android 10" value="a10"></el-option>
                    <el-option label="Android 13" value="a13"></el-option>
                    <el-option label="Android 14" value="a14"></el-option>
                    <el-option label="Android 15" value="a15"></el-option>
                  </el-select>
                </el-form-item>

                <el-form-item label="选择分支">
                  <el-select 
                    v-model="packageForm.branch" 
                    placeholder="请选择代码分支"
                    style="width: 100%"
                  >
                    <el-option label="master" value="master"></el-option>
                    <el-option label="develop" value="develop"></el-option>
                    <el-option label="release" value="release"></el-option>
                    <el-option label="hotfix" value="hotfix"></el-option>
                  </el-select>
                </el-form-item>

                <el-form-item>
                  <el-button 
                    type="primary" 
                    size="large" 
                    @click="triggerPackage"
                    :disabled="!packageForm.version || !packageForm.branch || isPackaging"
                    :loading="isPackaging"
                    class="package-btn"
                  >
                    <i class="el-icon-upload2"></i>
                    {{ isPackaging ? '打包中...' : '一键打包' }}
                  </el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 功能说明 -->
            <div class="feature-notice">
              <el-alert
                title="功能暂未开发"
                type="warning"
                description="一键打包功能正在开发中，敬请期待。目前可以复制命令手动执行。"
                show-icon
                :closable="false"
              />
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 打包历史 -->
    <div class="history-section">
      <h3>打包历史</h3>
      <el-table :data="packageHistory" style="width: 100%" max-height="300">
        <el-table-column prop="version" label="版本" width="100"></el-table-column>
        <el-table-column prop="branch" label="分支" width="120"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="触发时间" width="180"></el-table-column>
        <el-table-column prop="duration" label="耗时" width="100"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="viewLog(scope.row)">查看日志</el-button>
            <el-button type="text" size="small" @click="retrigger(scope.row)">重新触发</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 项目信息 -->
    <div class="project-info">
      <h3>项目信息</h3>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="GitLab服务器">http://192.168.230.230</el-descriptions-item>
        <el-descriptions-item label="API版本">v4</el-descriptions-item>
        <el-descriptions-item label="A10项目ID">79</el-descriptions-item>
        <el-descriptions-item label="A13项目ID">137</el-descriptions-item>
        <el-descriptions-item label="A14项目ID">108</el-descriptions-item>
        <el-descriptions-item label="A15项目ID">122</el-descriptions-item>
      </el-descriptions>
    </div>
  </div>
</template>

<script>
export default {
  name: 'OneClickPackage',
  data() {
    return {
      packageForm: {
        version: '',
        branch: 'master'
      },
      isPackaging: false,
      commands: {
        a10: 'curl -X POST --fail -F token=glptt-e0a6cf96bd15352f6a4eee661bebca6221132bbe -F ref=master http://192.168.230.230/api/v4/projects/79/trigger/pipeline',
        a13: 'curl -X POST --fail -F token=glptt-e9aad6368e29e1248c270bf0cd378d413082cb1b -F ref=master http://192.168.230.230/api/v4/projects/137/trigger/pipeline',
        a14: 'curl -X POST --fail -F token=glptt-a2e7b5209bb9dd96f5c6687b9743eba6bb3dbd1e -F ref=master http://192.168.230.230/api/v4/projects/108/trigger/pipeline',
        a15: 'curl -X POST --fail -F token=glptt-ad4ea92c652c47f93c00477509a9dc15117d474a -F ref=master http://192.168.230.230/api/v4/projects/122/trigger/pipeline'
      },
      packageHistory: [
        {
          id: 1,
          version: 'A13',
          branch: 'master',
          status: '成功',
          time: '2024-01-15 14:30:25',
          duration: '15分钟'
        },
        {
          id: 2,
          version: 'A14',
          branch: 'develop',
          status: '失败',
          time: '2024-01-15 13:45:12',
          duration: '8分钟'
        },
        {
          id: 3,
          version: 'A10',
          branch: 'master',
          status: '进行中',
          time: '2024-01-15 12:20:33',
          duration: '12分钟'
        }
      ]
    }
  },
  methods: {
    getTagType(version) {
      const types = {
        a10: 'primary',
        a13: 'success',
        a14: 'warning',
        a15: 'danger'
      }
      return types[version] || 'info'
    },
    getProjectId(command) {
      const match = command.match(/projects\/(\d+)\//)
      return match ? match[1] : 'N/A'
    },
    copyCommand(command) {
      navigator.clipboard.writeText(command).then(() => {
        this.$message.success('命令已复制到剪贴板')
      }).catch(() => {
        this.$message.error('复制失败，请手动复制')
      })
    },
    triggerPackage() {
      this.$message.warning('功能暂未开发，请手动复制命令执行')
      
      // 模拟打包过程（实际功能待开发）
      this.isPackaging = true
      setTimeout(() => {
        this.isPackaging = false
        this.$message.info('这是一个演示，实际功能正在开发中')
      }, 2000)
    },
    getStatusType(status) {
      const types = {
        '成功': 'success',
        '失败': 'danger',
        '进行中': 'warning'
      }
      return types[status] || 'info'
    },
    viewLog(row) {
      this.$message.info(`查看 ${row.version} ${row.branch} 分支的构建日志`)
    },
    retrigger(row) {
      this.$confirm(`确认重新触发 ${row.version} ${row.branch} 分支的打包吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('重新触发成功（演示模式）')
      }).catch(() => {
        this.$message.info('已取消重新触发')
      })
    }
  }
}
</script>

<style scoped>
.package-container {
  padding: 20px;
  background: linear-gradient(135deg, #74b9ff 0%, #0984e3 100%);
  min-height: calc(100vh - 140px);
}

.header {
  text-align: center;
  margin-bottom: 30px;
  color: white;
}

.header h2 {
  font-size: 2.5em;
  margin-bottom: 10px;
  text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
}

.header p {
  font-size: 1.2em;
  opacity: 0.9;
}

.package-section,
.history-section,
.project-info {
  background: white;
  border-radius: 15px;
  padding: 25px;
  margin-bottom: 25px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.2);
}

.command-display h3,
.package-control h3,
.history-section h3,
.project-info h3 {
  margin-top: 0;
  color: #333;
  border-bottom: 2px solid #74b9ff;
  padding-bottom: 10px;
}

.command-item {
  margin-bottom: 20px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 15px;
  background: #f8f9fa;
}

.command-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.project-id {
  font-size: 12px;
  color: #909399;
}

.command-content {
  position: relative;
}

.command-textarea :deep(.el-textarea__inner) {
  font-family: 'Courier New', monospace;
  font-size: 12px;
  line-height: 1.4;
  background-color: #2d3748;
  color: #e2e8f0;
  border: none;
}

.copy-btn {
  position: absolute;
  top: 5px;
  right: 5px;
  background: rgba(255,255,255,0.9);
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 4px 8px;
  font-size: 12px;
}

.control-form {
  margin-bottom: 20px;
}

.package-btn {
  width: 100%;
  height: 50px;
  font-size: 16px;
  border-radius: 25px;
  background: linear-gradient(135deg, #00cec9 0%, #55a3ff 100%);
  border: none;
  transition: all 0.3s ease;
}

.package-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0,206,201,0.4);
}

.feature-notice {
  margin-top: 20px;
}

/* 暗黑模式适配 */
.dark-mode .package-section,
.dark-mode .history-section,
.dark-mode .project-info {
  background: #2d2d2d;
  color: #e5eaf3;
}

.dark-mode .command-display h3,
.dark-mode .package-control h3,
.dark-mode .history-section h3,
.dark-mode .project-info h3 {
  color: #e5eaf3;
  border-bottom-color: #74b9ff;
}

.dark-mode .command-item {
  background: #363636;
  border-color: #444;
}

.dark-mode .project-id {
  color: #b0b0b0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .package-container {
    padding: 10px;
  }
  
  .package-section {
    padding: 15px;
  }
  
  .command-textarea :deep(.el-textarea__inner) {
    font-size: 10px;
  }
}
</style> 