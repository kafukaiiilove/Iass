<template>
  <div class="lucky-wheel-container">
    <div class="header">
      <h2>🎰 幸运大转盘</h2>
      <p>哈哈哈哈哈哈哈哈哈哈哈哈！</p>
    </div>

    <div class="wheel-section">
      <div class="main-content">
        <el-tabs v-model="activeTab" type="border-card" @tab-click="handleTabClick">
          <el-tab-pane label="今晚吃什么" name="food">
            <div class="wheel-content">
              <!-- 获取位置按钮 -->
              <div class="location-section">
                <el-button 
                  @click="getCurrentLocation" 
                  :loading="isGettingLocation" 
                  type="info" 
                  class="location-btn"
                  :disabled="isSpinning">
                  <i class="el-icon-location"></i>
                  {{ isGettingLocation ? '定位中...' : '获取当前位置推荐' }}
                </el-button>
                
                <div v-if="currentLocation" class="location-info">
                  <span>📍 {{ currentLocation.address }}</span>
                  <span class="location-coords">({{ currentLocation.lat }}, {{ currentLocation.lng }})</span>
                </div>
              </div>
              
              <div class="wheel-container">
                <svg class="wheel-svg" :class="{ spinning: isSpinning }" :style="{ transform: `rotate(${rotation}deg)` }" viewBox="0 0 400 400">
                  <g v-for="(item, index) in currentOptions" :key="index">
                    <path :d="getSegmentPath(index)" :fill="segmentColors[index % segmentColors.length]" stroke="#333" stroke-width="2"/>
                    <text :x="getTextX(index)" :y="getTextY(index)" :transform="getTextTransform(index)" 
                          text-anchor="middle" dominant-baseline="middle" fill="white" font-weight="bold" font-size="14">
                      {{ item }}
                    </text>
                  </g>
                </svg>
                <div class="wheel-pointer"></div>
              </div>
              
              <div class="result-display" v-if="result">
                <h3>🎉 转盘结果：{{ result }}</h3>
                <div v-if="currentLocation && currentLocation.restaurants" class="restaurant-recommendations">
                  <p>📍 {{ currentLocation.address }} 附近推荐：</p>
                  <div class="restaurant-list">
                    <div v-for="restaurant in currentLocation.restaurants" :key="restaurant.name" class="restaurant-item">
                      <span class="restaurant-name">{{ restaurant.name }}</span>
                      <span class="restaurant-type">{{ restaurant.type }}</span>
                    </div>
                  </div>
                </div>
              </div>
              <el-button type="primary" size="large" @click="spin" :disabled="isSpinning" class="spin-button">
                {{ isSpinning ? '转盘中...' : '开始转盘' }}
              </el-button>
            </div>
          </el-tab-pane>

          <el-tab-pane label="这个Bug谁来改" name="bug">
            <div class="wheel-content">
              <div class="management-layout">
                <!-- 左侧：组别选择和转盘 -->
                <div class="wheel-section-left">
                  <!-- 组别选择 -->
                  <div class="group-selector">
                    <el-select v-model="selectedBugGroups" multiple placeholder="请选择开发组（支持多选）" style="width: 300px;">
                      <el-option label="架构组" value="architecture"></el-option>
                      <el-option label="系统研发专题组" value="system"></el-option>
                      <el-option label="逆向组" value="reverse"></el-option>
                    </el-select>
                  </div>
              
                  <div v-if="selectedBugGroups.length > 0" class="wheel-container">
                    <svg class="wheel-svg" :class="{ spinning: isSpinning }" :style="{ transform: `rotate(${rotation}deg)` }" viewBox="0 0 400 400">
                      <g v-for="(item, index) in currentOptions" :key="index">
                        <path :d="getSegmentPath(index)" :fill="segmentColors[index % segmentColors.length]" stroke="#333" stroke-width="2"/>
                        <text :x="getTextX(index)" :y="getTextY(index)" :transform="getTextTransform(index)" 
                              text-anchor="middle" dominant-baseline="middle" fill="white" font-weight="bold" font-size="14">
                          {{ item }}
                        </text>
                      </g>
                    </svg>
                    <div class="wheel-pointer"></div>
                  </div>
                  
                  <div v-if="selectedBugGroups.length === 0" class="no-group-selected">
                    <i class="el-icon-info"></i>
                    <p>请先选择开发组进行转盘</p>
                  </div>
                  
                  <div class="result-display" v-if="result && selectedBugGroups.length > 0">
                    <h3>🐛 这个Bug交给：{{ result }}</h3>
                    <p class="group-info">来自：{{ getSelectedBugGroupNames() }}</p>
                  </div>
                  <el-button type="danger" size="large" @click="spin" :disabled="isSpinning || selectedBugGroups.length === 0" class="spin-button">
                    {{ isSpinning ? '转盘中...' : '开始转盘' }}
                  </el-button>
                </div>

                <!-- 右侧：人员管理 -->
                <div class="group-management">
                  <div class="group-management-header">
                    <h3>👥 开发组人员管理</h3>
                  </div>
                  
                  <div class="groups-list">
                    <div v-for="(groupName, groupKey) in groupDisplayNames.bug" :key="groupKey" class="group-item">
                      <div class="group-header">
                        <h4>{{ groupName }}</h4>
                        <el-button type="primary" size="mini" @click="showAddMemberDialog(groupKey, 'bug')">
                          <i class="el-icon-plus"></i> 添加成员
                        </el-button>
                      </div>
                      
                      <div class="members-list">
                        <el-tag 
                          v-for="(member, index) in bugGroups[groupKey]" 
                          :key="index"
                          closable
                          @close="removeMember(groupKey, index, 'bug')"
                          @dblclick="editMember(groupKey, index, 'bug')"
                          class="member-tag">
                          {{ member }}
                        </el-tag>
                        <div v-if="bugGroups[groupKey].length === 0" class="empty-group">
                          暂无成员
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="这个包谁来测" name="test">
            <div class="wheel-content">
              <div class="management-layout">
                <!-- 左侧：组别选择和转盘 -->
                <div class="wheel-section-left">
                  <!-- 组别选择 -->
                  <div class="group-selector">
                    <el-select v-model="selectedTestGroups" multiple placeholder="请选择测试组（支持多选）" style="width: 300px;">
                      <el-option label="测试1组" value="test1"></el-option>
                      <el-option label="测试2组" value="test2"></el-option>
                    </el-select>
                  </div>
                  
                  <div v-if="selectedTestGroups.length > 0" class="wheel-container">
                    <svg class="wheel-svg" :class="{ spinning: isSpinning }" :style="{ transform: `rotate(${rotation}deg)` }" viewBox="0 0 400 400">
                      <g v-for="(item, index) in currentOptions" :key="index">
                        <path :d="getSegmentPath(index)" :fill="segmentColors[index % segmentColors.length]" stroke="#333" stroke-width="2"/>
                        <text :x="getTextX(index)" :y="getTextY(index)" :transform="getTextTransform(index)" 
                              text-anchor="middle" dominant-baseline="middle" fill="white" font-weight="bold" font-size="14">
                          {{ item }}
                        </text>
                      </g>
                    </svg>
                    <div class="wheel-pointer"></div>
                  </div>
                  
                  <div v-if="selectedTestGroups.length === 0" class="no-group-selected">
                    <i class="el-icon-info"></i>
                    <p>请先选择测试组进行转盘</p>
                  </div>
                  
                  <div class="result-display" v-if="result && selectedTestGroups.length > 0">
                    <h3>📱 测试任务分配给：{{ result }}</h3>
                    <p class="group-info">来自：{{ getSelectedTestGroupNames() }}</p>
                  </div>
                  <el-button type="success" size="large" @click="spin" :disabled="isSpinning || selectedTestGroups.length === 0" class="spin-button">
                    {{ isSpinning ? '转盘中...' : '开始转盘' }}
                  </el-button>
                </div>

                <!-- 右侧：人员管理 -->
                <div class="group-management">
                  <div class="group-management-header">
                    <h3>🧪 测试组人员管理</h3>
                  </div>
                  
                  <div class="groups-list">
                    <div v-for="(groupName, groupKey) in groupDisplayNames.test" :key="groupKey" class="group-item">
                      <div class="group-header">
                        <h4>{{ groupName }}</h4>
                        <el-button type="primary" size="mini" @click="showAddMemberDialog(groupKey, 'test')">
                          <i class="el-icon-plus"></i> 添加成员
                        </el-button>
                      </div>
                      
                      <div class="members-list">
                        <el-tag 
                          v-for="(member, index) in testGroups[groupKey]" 
                          :key="index"
                          closable
                          @close="removeMember(groupKey, index, 'test')"
                          @dblclick="editMember(groupKey, index, 'test')"
                          class="member-tag">
                          {{ member }}
                        </el-tag>
                        <div v-if="testGroups[groupKey].length === 0" class="empty-group">
                          暂无成员
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <div class="history-section">
      <h3>历史记录</h3>
      <el-table :data="history" style="width: 100%" max-height="250">
        <el-table-column prop="type" label="类型" width="120"></el-table-column>
        <el-table-column prop="result" label="结果" width="200"></el-table-column>
        <el-table-column prop="time" label="时间"></el-table-column>
      </el-table>
    </div>

    <!-- 成员编辑对话框 -->
    <el-dialog 
      :title="memberDialogTitle" 
      :visible.sync="memberDialogVisible" 
      width="400px"
      @close="closeMemberDialog">
      <el-form ref="memberForm" :model="memberForm" :rules="memberRules" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="memberForm.name" placeholder="请输入成员姓名" maxlength="20"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeMemberDialog">取 消</el-button>
        <el-button type="primary" @click="confirmMember">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'LuckyWheel',
  data() {
    return {
      activeTab: 'food',
      isSpinning: false,
      rotation: 0,
      result: '',
      history: [],
      selectedBugGroups: [], // 多选Bug组
      selectedTestGroups: [], // 多选测试组
      currentLocation: null, // 当前位置信息
      isGettingLocation: false,
      // 成员管理对话框
      memberDialogVisible: false,
      memberDialogTitle: '',
      memberDialogMode: 'add', // 'add' 或 'edit'
      memberForm: {
        name: ''
      },
      memberRules: {
        name: [
          { required: true, message: '请输入成员姓名', trigger: 'blur' },
          { min: 2, max: 20, message: '姓名长度在 2 到 20 个字符', trigger: 'blur' }
        ]
      },
             currentGroupKey: '',
       currentGroupType: '',
       currentMemberIndex: -1,
       options: {
        food: [
          '长沙味道', '湘外婆', '麦当劳', '烧腊饭', '猪脚饭', '南昌拌粉', 
          '兰州拉面', '卤咖', '盖码饭', '下馆子', '不吃了'
        ]
      },
      bugGroups: {
        architecture: ['大林', '吉军'],
        system: ['岳彪', '柴祥', '龚海杰', '杨霆', '何光明'],
        reverse: ['曾知宇', '颜文', '唐辉辉', '钟基洪']
      },
      testGroups: {
        test1: ['张文奕', '李松', '汤欢欢', '陈斯佳'],
        test2: ['曹媛', '李露霜', '刘东杨']
      },
      segmentColors: [
        '#ff6b6b', '#4ecdc4', '#45b7d1', '#96ceb4', '#feca57', 
        '#ff9ff3', '#54a0ff', '#5f27cd', '#00d2d3', '#ff9f43', 
        '#ee5a24', '#0abde3', '#2ed573', '#ffa502', '#ff6348',
        '#a55eea', '#26de81', '#fd79a8', '#6c5ce7', '#fdcb6e'
      ]
    }
  },
  computed: {
    currentOptions() {
      if (this.activeTab === 'food') {
        return this.options.food || []
      } else if (this.activeTab === 'bug') {
        // 支持多选组
        if (this.selectedBugGroups.length > 0) {
          let allMembers = []
          this.selectedBugGroups.forEach(groupKey => {
            if (this.bugGroups[groupKey]) {
              allMembers = allMembers.concat(this.bugGroups[groupKey])
            }
          })
          return allMembers
        }
        return []
      } else if (this.activeTab === 'test') {
        // 支持多选组
        if (this.selectedTestGroups.length > 0) {
          let allMembers = []
          this.selectedTestGroups.forEach(groupKey => {
            if (this.testGroups[groupKey]) {
              allMembers = allMembers.concat(this.testGroups[groupKey])
            }
          })
          return allMembers
        }
        return []
      }
      return []
    },
    // 组别显示名称映射
    groupDisplayNames() {
      return {
        bug: {
          architecture: '架构组',
          system: '系统研发专题组', 
          reverse: '逆向组'
        },
        test: {
          test1: '测试1组',
          test2: '测试2组'
        }
      }
    },
    // 动态生成转盘颜色
    segmentColors() {
      const colors = [
        '#ff6b6b', '#4ecdc4', '#45b7d1', '#96ceb4', '#feca57', 
        '#ff9ff3', '#54a0ff', '#5f27cd', '#00d2d3', '#ff9f43', 
        '#ee5a24', '#0abde3', '#2ed573', '#ffa502', '#ff6348'
      ]
      const count = this.currentOptions.length
      if (count === 0) return []
      
      let segmentColors = []
      for (let i = 0; i < count; i++) {
        const startAngle = (i * 360) / count
        const endAngle = ((i + 1) * 360) / count
        const color = colors[i % colors.length]
        segmentColors.push(`${color} ${startAngle}deg ${endAngle}deg`)
      }
      
      return segmentColors
    }
  },
  
  mounted() {
    this.initializeWheel()
  },
  
  methods: {
    // 统一角度计算 - 确保所有方法使用相同逻辑
    getSegmentAngle(index) {
      const count = this.currentOptions.length
      const segmentAngle = 360 / count
      
      // 扇形角度：从-90度开始（指针位置），顺时针分布
      const startAngle = -90 + index * segmentAngle
      const endAngle = -90 + (index + 1) * segmentAngle
      const centerAngle = -90 + index * segmentAngle + segmentAngle / 2
      
      return { startAngle, endAngle, centerAngle, segmentAngle }
    },
    
    // SVG扇形路径计算
    getSegmentPath(index) {
      const { startAngle, endAngle, segmentAngle } = this.getSegmentAngle(index)
      
      const centerX = 200
      const centerY = 200  
      const radius = 190
      
      const startRad = (startAngle * Math.PI) / 180
      const endRad = (endAngle * Math.PI) / 180
      
      const x1 = centerX + radius * Math.cos(startRad)
      const y1 = centerY + radius * Math.sin(startRad)
      const x2 = centerX + radius * Math.cos(endRad)
      const y2 = centerY + radius * Math.sin(endRad)
      
      const largeArcFlag = segmentAngle > 180 ? 1 : 0
      
      return `M ${centerX} ${centerY} L ${x1} ${y1} A ${radius} ${radius} 0 ${largeArcFlag} 1 ${x2} ${y2} Z`
    },
    
    // 文字X坐标
    getTextX(index) {
      const { centerAngle } = this.getSegmentAngle(index)
      const textRadius = 130
      
      return 200 + textRadius * Math.cos((centerAngle * Math.PI) / 180)
    },
    
    // 文字Y坐标
    getTextY(index) {
      const { centerAngle } = this.getSegmentAngle(index)
      const textRadius = 130
      
      return 200 + textRadius * Math.sin((centerAngle * Math.PI) / 180)
    },
    
    // 文字旋转角度
    getTextTransform(index) {
      const { centerAngle } = this.getSegmentAngle(index)
      const textX = this.getTextX(index)
      const textY = this.getTextY(index)
      
      // 文字旋转角度，保持可读性
      let rotationAngle = centerAngle
      
      // 如果文字在下半圆（90度到270度），旋转180度保持可读性
      const normalizedAngle = (centerAngle + 360) % 360
      if (normalizedAngle > 90 && normalizedAngle < 270) {
        rotationAngle = centerAngle + 180
      }
      
      return `rotate(${rotationAngle}, ${textX}, ${textY})`
    },
    
    // 初始化转盘状态
    initializeWheel() {
      console.log('🎡 初始化转盘...')
      this.rotation = 0
      this.result = ''
    },
    

    
    handleTabClick() {
      // 切换标签页时完全重置转盘状态
      this.result = ''
      this.rotation = 0
      this.isSpinning = false
      
      // 重置各标签页的选择状态
      if (this.activeTab === 'bug') {
        this.selectedBugGroups = []
      } else if (this.activeTab === 'test') {
        this.selectedTestGroups = []
      }
      
      // 确保在下一个事件循环中重置状态
      this.$nextTick(() => {
        this.result = ''
        this.rotation = 0
      })
    },
    // 验证指针实际指向哪个扇形
    verifyPointerPosition() {
      // 转盘最终旋转角度
      const finalRotationAngle = this.rotation % 360
      const normalizedRotation = finalRotationAngle >= 0 ? finalRotationAngle : finalRotationAngle + 360
      
      // 指针在固定位置（顶部），相当于角度-90°
      // 当转盘旋转normalizedRotation度后，指针相对于转盘的角度为：
      let pointerOnWheel = -90 - normalizedRotation
      
      // 规范化到0-360度范围
      while (pointerOnWheel < 0) {
        pointerOnWheel += 360
      }
      while (pointerOnWheel >= 360) {
        pointerOnWheel -= 360
      }
      
      // 计算指针指向哪个扇形
      // 第0个扇形从-90°开始，但我们需要转换到0-360度系统
      const segmentAngle = 360 / this.currentOptions.length
      let pointerAngleFrom0 = pointerOnWheel + 90 // 转换为从0°开始的角度系统
      if (pointerAngleFrom0 >= 360) {
        pointerAngleFrom0 -= 360
      }
      
      const actualIndex = Math.floor(pointerAngleFrom0 / segmentAngle)
      const clampedIndex = actualIndex >= this.currentOptions.length ? 0 : actualIndex
      const actualOption = this.currentOptions[clampedIndex]
      
      console.log('🔍 指针位置验证：')
      console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━')
      console.log('📐 角度计算：')
      console.log('  - 转盘旋转角度:', normalizedRotation.toFixed(2) + '°')
      console.log('  - 指针在转盘上的位置:', pointerOnWheel.toFixed(2) + '°')
      console.log('  - 转换为0°起始:', pointerAngleFrom0.toFixed(2) + '°')
      console.log('  - 每扇形角度:', segmentAngle.toFixed(2) + '°')
      console.log('')
      console.log('📍 实际指向：')
      console.log('  - 计算索引:', actualIndex)
      console.log('  - 最终索引:', clampedIndex)
      console.log('  - 实际选项:', actualOption)
      console.log('')
      
      // 显示所有扇形的角度范围用于调试
      console.log('📋 扇形分布：')
      this.currentOptions.forEach((option, index) => {
        const { startAngle, endAngle, centerAngle } = this.getSegmentAngle(index)
        console.log(`  ${index}: "${option}" [${startAngle.toFixed(1)}° - ${endAngle.toFixed(1)}°, 中心: ${centerAngle.toFixed(1)}°]`)
      })
      console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━')
      
      return { actualIndex: clampedIndex, actualOption }
    },

    spin() {
      if (this.isSpinning || this.currentOptions.length === 0) return
      
      this.isSpinning = true
      this.result = ''
      
      // 1. 随机选择一个选项
      const randomIndex = Math.floor(Math.random() * this.currentOptions.length)
      const selectedOption = this.currentOptions[randomIndex]
      
      // 2. 使用统一的角度计算方法
      const { centerAngle, segmentAngle } = this.getSegmentAngle(randomIndex)
      
      // 3. 计算旋转角度 - 让选中扇形的中心对准指针(-90度位置)
      // 指针在-90度，需要让扇form中心旋转到-90度位置
      const targetAngle = -90 - centerAngle
      
      // 4. 添加多圈旋转效果
      const spinRounds = 4 + Math.random() * 3 // 4-7圈
      const finalRotation = spinRounds * 360 + targetAngle
      
      this.rotation = finalRotation
      
      console.log('🎰 统一角度系统 - 精确旋转：')
      console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━')
      console.log('📊 转盘信息：')
      console.log('  - 选项总数:', this.currentOptions.length)
      console.log('  - 每扇形角度:', segmentAngle.toFixed(2) + '°')
      console.log('  - 选项列表:', this.currentOptions.map((item, i) => `${i}:${item}`))
      console.log('')
      console.log('🎯 选中信息：')
      console.log('  - 选中索引:', randomIndex)
      console.log('  - 选中选项:', selectedOption)
      console.log('  - 扇形中心角度:', centerAngle.toFixed(2) + '°')
      console.log('')
      console.log('🌀 旋转计算：')
      console.log('  - 指针位置: -90° (顶部)')
      console.log('  - 目标对齐角度:', targetAngle.toFixed(2) + '°')
      console.log('  - 旋转圈数:', spinRounds.toFixed(1) + '圈')
      console.log('  - 最终旋转角度:', finalRotation.toFixed(2) + '°')
      console.log('')
      console.log('✅ 预期结果: "' + selectedOption + '"')
      console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━')
      
      // 延迟显示结果并验证
      setTimeout(() => {
        // 验证指针实际指向
        const verification = this.verifyPointerPosition()
        
        // 如果验证结果与预期不符，使用实际指向的结果
        if (verification.actualIndex !== randomIndex) {
          console.log('⚠️ 检测到偏差，使用实际指向结果')
          this.result = verification.actualOption
        } else {
          this.result = selectedOption
        }
        
        this.isSpinning = false
        this.addToHistory()
      }, 3000)
    },
    

    
    addToHistory() {
      const typeMap = {
        food: '今晚吃什么',
        bug: 'Bug修复',
        test: '测试任务'
      }
      
      let displayResult = this.result
      if (this.activeTab === 'bug') {
        const groupNames = this.getSelectedBugGroupNames()
        if (groupNames) {
          displayResult = `【${groupNames}】${this.result}`
        }
      } else if (this.activeTab === 'test') {
        const groupNames = this.getSelectedTestGroupNames()
        if (groupNames) {
          displayResult = `【${groupNames}】${this.result}`
        }
      }
      
      this.history.unshift({
        type: typeMap[this.activeTab],
        result: displayResult,
        time: new Date().toLocaleString()
      })
      
      // 只保留最近20条记录
      if (this.history.length > 20) {
        this.history.pop()
      }
    },
    getBugGroupName(groupKey) {
      const groupNames = {
        architecture: '架构组',
        system: '系统研发专题组',
        reverse: '逆向组'
      }
      return groupNames[groupKey] || groupKey
    },
    getTestGroupName(groupKey) {
      const groupNames = {
        test1: '测试1组',
        test2: '测试2组'
      }
      return groupNames[groupKey] || groupKey
    },
    // 获取选中的Bug组名称
    getSelectedBugGroupNames() {
      return this.selectedBugGroups.map(key => this.getBugGroupName(key)).join('、')
    },
    // 获取选中的测试组名称
    getSelectedTestGroupNames() {
      return this.selectedTestGroups.map(key => this.getTestGroupName(key)).join('、')
    },
    
    // 显示添加成员对话框
    showAddMemberDialog(groupKey, groupType) {
      this.memberDialogMode = 'add'
      this.memberDialogTitle = `添加成员到${this.groupDisplayNames[groupType][groupKey]}`
      this.currentGroupKey = groupKey
      this.currentGroupType = groupType
      this.currentMemberIndex = -1
      this.memberForm.name = ''
      this.memberDialogVisible = true
    },
    
    // 编辑成员（双击触发）
    editMember(groupKey, memberIndex, groupType) {
      this.memberDialogMode = 'edit'
      this.memberDialogTitle = `编辑${this.groupDisplayNames[groupType][groupKey]}成员`
      this.currentGroupKey = groupKey
      this.currentGroupType = groupType
      this.currentMemberIndex = memberIndex
      
      const groups = groupType === 'bug' ? this.bugGroups : this.testGroups
      this.memberForm.name = groups[groupKey][memberIndex]
      this.memberDialogVisible = true
    },
    
    // 删除成员
    removeMember(groupKey, memberIndex, groupType) {
      this.$confirm('确定要删除该成员吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const groups = groupType === 'bug' ? this.bugGroups : this.testGroups
        groups[groupKey].splice(memberIndex, 1)
        this.$message.success('删除成功')
      }).catch(() => {
        // 用户取消删除
      })
    },
    
    // 确认添加/编辑成员
    confirmMember() {
      this.$refs.memberForm.validate((valid) => {
        if (valid) {
          const groups = this.currentGroupType === 'bug' ? this.bugGroups : this.testGroups
          const memberName = this.memberForm.name.trim()
          
          if (this.memberDialogMode === 'add') {
            // 检查是否重复
            if (groups[this.currentGroupKey].includes(memberName)) {
              this.$message.warning('该成员已存在')
              return
            }
            groups[this.currentGroupKey].push(memberName)
            this.$message.success('添加成功')
          } else {
            // 编辑模式
            groups[this.currentGroupKey][this.currentMemberIndex] = memberName
            this.$message.success('修改成功')
          }
          
          this.closeMemberDialog()
        }
      })
    },
    
    // 关闭成员对话框
    closeMemberDialog() {
      this.memberDialogVisible = false
      this.memberForm.name = ''
      this.currentGroupKey = ''
      this.currentGroupType = ''
      this.currentMemberIndex = -1
      if (this.$refs.memberForm) {
        this.$refs.memberForm.resetFields()
      }
    },
    // 获取当前位置
    getCurrentLocation() {
      this.isGettingLocation = true
      
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
          (position) => {
            const { latitude, longitude, accuracy } = position.coords
            console.log('🌍 获取到位置信息：', {
              纬度: latitude,
              经度: longitude,
              精度: accuracy + '米'
            })
            this.processLocation(latitude, longitude)
          },
          (error) => {
            console.error('获取位置失败:', error)
            let errorMsg = '位置获取失败'
            switch(error.code) {
              case error.PERMISSION_DENIED:
                errorMsg = '用户拒绝了位置共享请求，请在浏览器设置中允许位置访问'
                break
              case error.POSITION_UNAVAILABLE:
                errorMsg = '位置信息不可用，请检查GPS或网络连接'
                break
              case error.TIMEOUT:
                errorMsg = '获取位置超时，请重试'
                break
            }
            this.$message.error(errorMsg)
            this.locationFallback()
          },
          {
            enableHighAccuracy: true,    // 启用高精度定位
            timeout: 20000,             // 增加超时时间
            maximumAge: 60000           // 减少缓存时间，获取更新的位置
          }
        )
      } else {
        this.$message.error('您的浏览器不支持地理位置功能')
        this.locationFallback()
      }
    },
    // 处理位置信息
    async processLocation(lat, lng) {
      try {
        // 使用真实API获取地址和餐厅推荐
        this.$message.info('正在获取附近餐厅推荐...')
        
        const addressData = await this.getAddressByCoordinates(lat, lng)
        const restaurants = await this.getNearbyRestaurants(lat, lng)
        
        this.currentLocation = {
          lat,
          lng,
          address: addressData.address,
          city: addressData.city,
          restaurants: restaurants
        }
        this.isGettingLocation = false
        this.$message.success(`获取成功！找到${restaurants.length}家附近餐厅`)
      } catch (error) {
        console.error('获取位置信息失败:', error)
        this.$message.error('无法获取附近餐厅信息')
        this.locationFallback()
      }
    },
    // 位置获取失败处理
    locationFallback() {
      this.isGettingLocation = false
      this.$message.error('位置获取失败，请检查浏览器位置权限设置')
    },
    // 根据位置推荐餐厅
    getRestaurantsByLocation(lat, lng) {
      // 根据地理位置智能推荐餐厅
      const locationBasedRestaurants = this.getLocationBasedRecommendations(lat, lng)
      
      // 从转盘选项中筛选出当地特色
      const wheelOptions = this.currentOptions || this.options.food
      const localSpecialties = wheelOptions.filter(food => 
        locationBasedRestaurants.includes(food)
      )
      
      // 如果有匹配的本地特色，优先推荐
      if (localSpecialties.length > 0) {
        return localSpecialties.slice(0, 5)
      }
      
      // 否则返回地理位置相关推荐
      return locationBasedRestaurants.slice(0, 5)
    },
    
    // 获取逆地理编码地址（使用JSONP方式）
    async getAddressByCoordinates(lat, lng) {
      return new Promise((resolve) => {
        const aMapKey = 'YOUR_AMAP_KEY' // 替换为您的高德地图API Key
        
        if (aMapKey === 'YOUR_AMAP_KEY') {
          // 如果没有配置API Key，使用兜底方案
          resolve(this.getAddressFallback(lat, lng))
          return
        }
        
        // 使用JSONP方式调用高德地图逆地理编码API
        const script = document.createElement('script')
        const callbackName = 'regeoCallback' + Date.now()
        
        window[callbackName] = (data) => {
          document.head.removeChild(script)
          delete window[callbackName]
          
          if (data.status === '1' && data.regeocode) {
            const address = data.regeocode.formatted_address
            const city = data.regeocode.addressComponent.city || data.regeocode.addressComponent.province
            resolve({ address, city })
          } else {
            resolve(this.getAddressFallback(lat, lng))
          }
        }
        
        script.src = `https://restapi.amap.com/v3/geocode/regeo?key=${aMapKey}&location=${lng},${lat}&radius=1000&extensions=all&callback=${callbackName}`
        script.onerror = () => {
          document.head.removeChild(script)
          delete window[callbackName]
          resolve(this.getAddressFallback(lat, lng))
        }
        
        document.head.appendChild(script)
        
        // 设置超时
        setTimeout(() => {
          if (window[callbackName]) {
            document.head.removeChild(script)
            delete window[callbackName]
            resolve(this.getAddressFallback(lat, lng))
          }
        }, 5000)
      })
    },
    
    // 地址解析兜底方案
    getAddressFallback(lat, lng) {
      const cityMaps = {
        39: '北京市', 31: '上海市', 23: '广州市', 22: '深圳市',
        30: '杭州市', 28: '成都市', 34: '西安市', 32: '南京市'
      }
      
      const latKey = Math.floor(lat)
      const city = cityMaps[latKey] || '未知城市'
      return { 
        address: `${city} (${lat.toFixed(4)}, ${lng.toFixed(4)})`,
        city: city
      }
    },
    
    // 获取附近餐厅（多API融合）
    async getNearbyRestaurants(lat, lng) {
      try {
        // 尝试多个API源
        const restaurants = await Promise.race([
          this.getRestaurantsFromAMap(lat, lng),
          this.getRestaurantsFromDianping(lat, lng),
          this.getRestaurantsFromEleme(lat, lng)
        ])
        
        return restaurants.slice(0, 10) // 最多返回10个
      } catch (error) {
        console.error('获取餐厅推荐失败:', error)
        return this.getRestaurantsFallback(lat, lng)
      }
    },
    
    // 高德地图POI搜索（使用JSONP方式解决跨域）
    async getRestaurantsFromAMap(lat, lng) {
      return new Promise((resolve, reject) => {
        // 注意：这需要申请高德地图API Key
        const aMapKey = 'YOUR_AMAP_KEY' // 替换为您的高德地图API Key
        
        if (aMapKey === 'YOUR_AMAP_KEY') {
          // 如果没有配置API Key，基于位置生成模拟数据
          setTimeout(() => {
            const mockRestaurants = this.generateLocationBasedRestaurants(lat, lng)
            resolve(mockRestaurants)
          }, 500)
          return
        }
        
        // 使用JSONP方式调用高德地图API
        const script = document.createElement('script')
        const callbackName = 'amapCallback' + Date.now()
        
        window[callbackName] = (data) => {
          document.head.removeChild(script)
          delete window[callbackName]
          
          if (data.status === '1' && data.pois) {
            const restaurants = data.pois.map(poi => ({
              name: poi.name,
              type: poi.type || '餐厅',
              address: poi.address,
              distance: poi.distance ? poi.distance + 'm' : '未知'
            }))
            resolve(restaurants)
          } else {
            reject(new Error('高德地图API返回错误'))
          }
        }
        
        script.src = `https://restapi.amap.com/v3/place/around?key=${aMapKey}&location=${lng},${lat}&keywords=餐厅|美食&types=050000&radius=1000&offset=8&page=1&callback=${callbackName}`
        script.onerror = () => {
          document.head.removeChild(script)
          delete window[callbackName]
          reject(new Error('高德地图API调用失败'))
        }
        
        document.head.appendChild(script)
        
        // 设置超时
        setTimeout(() => {
          if (window[callbackName]) {
            document.head.removeChild(script)
            delete window[callbackName]
            reject(new Error('高德地图API调用超时'))
          }
        }, 5000)
      })
    },
    
    // 大众点评API（需要企业认证）
    async getRestaurantsFromDianping(lat, lng) {
      // 注意：大众点评API需要企业认证，这里提供模拟实现
      const mockResponse = await new Promise(resolve => {
        setTimeout(() => {
          const restaurants = [
            { name: '海底捞火锅', type: '火锅' },
            { name: '麦当劳', type: '快餐' },
            { name: '星巴克', type: '咖啡' },
            { name: '肯德基', type: '快餐' },
            { name: '必胜客', type: '西餐' }
          ]
          
          resolve(restaurants)
        }, 1000)
      })
      return mockResponse
    },
    
    // 饿了么API（需要商务合作）
    async getRestaurantsFromEleme(lat, lng) {
      // 注意：饿了么API需要商务合作，这里提供模拟实现
      const mockResponse = await new Promise(resolve => {
        setTimeout(() => {
          const restaurants = [
            { name: '张亮麻辣烫', type: '麻辣烫' },
            { name: '兰州拉面', type: '面食' },
            { name: '沙县小吃', type: '小吃' },
            { name: '黄焖鸡米饭', type: '快餐' },
            { name: '重庆小面', type: '面食' }
          ]
          
          resolve(restaurants)
        }, 800)
      })
      return mockResponse
    },
    

    
    // 餐厅推荐兜底方案
    getRestaurantsFallback(lat, lng) {
      return this.generateLocationBasedRestaurants(lat, lng)
    },
    
    // 基于位置生成餐厅数据
    generateLocationBasedRestaurants(lat, lng) {
      // 根据经纬度判断大致区域
      let regionRestaurants = []
      
      // 成都地区 (lat: 30.x, lng: 104.x)
      if (lat >= 30.0 && lat <= 31.0 && lng >= 103.0 && lng <= 105.0) {
        regionRestaurants = [
          { name: '川味观', type: '川菜' },
          { name: '蜀九香火锅', type: '火锅' },
          { name: '陈麻婆豆腐', type: '川菜' },
          { name: '夫妻肺片', type: '小吃' },
          { name: '钟水饺', type: '小吃' },
          { name: '龙抄手', type: '面食' },
          { name: '担担面', type: '面食' },
          { name: '麻辣串串', type: '小吃' }
        ]
      }
      // 长沙地区 (lat: 28.x, lng: 112-113.x) - 用户实际位置
      else if (lat >= 27.5 && lat <= 29.0 && lng >= 112.0 && lng <= 114.0) {
        regionRestaurants = [
          { name: '文和友老长沙', type: '湘菜' },
          { name: '火宫殿', type: '湘菜' },
          { name: '茶颜悦色', type: '奶茶' },
          { name: '超级文和友', type: '小龙虾' },
          { name: '湘菜馆', type: '湘菜' },
          { name: '臭豆腐摊', type: '小吃' },
          { name: '糖油粑粑', type: '小吃' },
          { name: '口味虾', type: '小吃' }
        ]
      }
      // 北京地区
      else if (lat >= 39.0 && lat <= 41.0 && lng >= 115.0 && lng <= 118.0) {
        regionRestaurants = [
          { name: '老北京炸酱面', type: '面食' },
          { name: '全聚德烤鸭', type: '烤鸭' },
          { name: '东来顺涮羊肉', type: '火锅' },
          { name: '庆丰包子', type: '包子' },
          { name: '豆汁焦圈', type: '小吃' },
          { name: '卤煮火烧', type: '小吃' },
          { name: '艾窝窝', type: '点心' },
          { name: '炒肝', type: '小吃' }
        ]
      }
      // 上海地区
      else if (lat >= 30.0 && lat <= 32.0 && lng >= 120.0 && lng <= 122.0) {
        regionRestaurants = [
          { name: '南翔小笼包', type: '小笼包' },
          { name: '本帮菜馆', type: '本帮菜' },
          { name: '生煎包', type: '包子' },
          { name: '红烧肉', type: '本帮菜' },
          { name: '糖醋排骨', type: '本帮菜' },
          { name: '白切鸡', type: '本帮菜' },
          { name: '油条豆浆', type: '早餐' },
          { name: '咸菜肉丝面', type: '面食' }
        ]
      }
      // 默认通用餐厅
      else {
        regionRestaurants = [
          { name: '麦当劳', type: '快餐' },
          { name: '肯德基', type: '快餐' },
          { name: '星巴克', type: '咖啡' },
          { name: '兰州拉面', type: '面食' },
          { name: '沙县小吃', type: '小吃' },
          { name: '黄焖鸡米饭', type: '快餐' },
          { name: '麻辣烫', type: '小吃' },
          { name: '重庆小面', type: '面食' }
        ]
      }
      
      return regionRestaurants.slice(0, 8) // 返回8个餐厅
    },
    
    // 基于地理位置的智能推荐
    getLocationBasedRecommendations(lat, lng) {
      // 根据经纬度判断地区并推荐当地特色
      if (lat >= 39 && lat <= 41 && lng >= 115 && lng <= 118) {
        // 北京地区
        return ['炸酱面', '烤鸭', '豆汁焦圈', '卤煮火烧', '东北菜']
      } else if (lat >= 30 && lat <= 32 && lng >= 120 && lng <= 122) {
        // 上海/杭州地区
        return ['小笼包', '本帮菜', '杭帮菜', '西湖醋鱼', '粤菜']
      } else if (lat >= 22 && lat <= 24 && lng >= 113 && lng <= 115) {
        // 广深地区
        return ['粤菜', '潮汕牛肉火锅', '茶餐厅', '港式茶点', '日料']
      } else if (lat >= 30 && lat <= 31 && lng >= 103 && lng <= 105) {
        // 成都地区
        return ['川菜', '火锅', '麻辣烫', '串串香', '冒菜']
      } else if (lat >= 34 && lat <= 36 && lng >= 108 && lng <= 110) {
        // 西安地区
        return ['肉夹馍', '凉皮', '羊肉泡馍', '西北菜', '兰州拉面']
      } else {
        // 其他地区默认推荐
        return ['兰州拉面', '沙县小吃', '麻辣烫', '黄焖鸡米饭', '火锅']
      }
    },

  }
}
</script>

<style scoped>
.lucky-wheel-container {
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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

.wheel-section {
  background: white;
  border-radius: 15px;
  padding: 30px;
  margin-bottom: 30px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.3);
}

.wheel-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 30px;
}

.wheel-container {
  position: relative;
  width: 300px;
  height: 300px;
}

.wheel-svg {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  border: 6px solid #333;
  transition: transform 3s cubic-bezier(0.23, 1, 0.32, 1);
  box-shadow: 0 4px 20px rgba(0,0,0,0.3);
}

.wheel-svg.spinning {
  transition: transform 3s cubic-bezier(0.23, 1, 0.32, 1);
}

.wheel-pointer {
  position: absolute;
  top: -8px;
  left: 50%;
  transform: translateX(-50%);
  width: 0;
  height: 0;
  border-left: 12px solid transparent;
  border-right: 12px solid transparent;
  border-top: 25px solid #ff4757;
  z-index: 10;
  filter: drop-shadow(2px 2px 4px rgba(0,0,0,0.3));
}

.result-display {
  text-align: center;
  padding: 20px;
  background: linear-gradient(135deg, #ffd89b 0%, #19547b 100%);
  border-radius: 10px;
  color: white;
  min-width: 300px;
}

.result-display h3 {
  margin: 0;
  font-size: 1.5em;
  text-shadow: 1px 1px 2px rgba(0,0,0,0.3);
}

.spin-button {
  padding: 15px 40px;
  font-size: 1.2em;
  border-radius: 25px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.2);
  transition: all 0.3s ease;
}

.spin-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0,0,0,0.3);
}

.history-section {
  background: white;
  border-radius: 15px;
  padding: 20px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.3);
}

.history-section h3 {
  margin-top: 0;
  color: #333;
  text-align: center;
}

/* 暗黑模式适配 */
.dark-mode .wheel-section,
.dark-mode .history-section {
  background: #2d2d2d;
  color: #e5eaf3;
}

.dark-mode .history-section h3 {
  color: #e5eaf3;
}

/* 组别选择器样式 */
.group-selector {
  text-align: center;
  margin-bottom: 20px;
}

.group-selector :deep(.el-select) {
  font-size: 16px;
}

.group-selector :deep(.el-input__inner) {
  border-radius: 20px;
  border: 2px solid #667eea;
  font-size: 16px;
  text-align: center;
}

.group-selector :deep(.el-input__inner:focus) {
  border-color: #409EFF;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

/* 未选择组时的提示样式 */
.no-group-selected {
  text-align: center;
  padding: 40px 20px;
  color: #909399;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  border-radius: 15px;
  margin: 20px 0;
}

.no-group-selected i {
  font-size: 48px;
  margin-bottom: 15px;
  color: #409EFF;
}

.no-group-selected p {
  font-size: 16px;
  margin: 0;
  line-height: 1.5;
}

/* 选择行样式 */
.select-row {
  margin-bottom: 10px;
}

.select-row:last-child {
  margin-bottom: 0;
}

/* 组信息样式 */
.group-info {
  font-size: 14px;
  color: #666;
  margin-top: 5px;
}

.dark-mode .group-info {
  color: #bbb;
}

/* 定位功能样式 */
.location-section {
  text-align: center;
  margin-bottom: 20px;
}

.location-btn {
  border-radius: 20px;
  padding: 10px 20px;
  font-size: 14px;
}

.location-info {
  margin-top: 15px;
  padding: 10px 20px;
  background: #f0f9ff;
  border-radius: 20px;
  color: #0369a1;
  font-size: 14px;
  display: inline-block;
  border: 1px solid #e0f2fe;
}

.location-coords {
  margin-left: 8px;
  font-size: 12px;
  color: #64748b;
  font-family: monospace;
}

.dark-mode .location-info {
  background: #1e3a8a;
  color: #93c5fd;
  border-color: #1e40af;
}

.dark-mode .location-coords {
  color: #94a3b8;
}

/* 餐厅推荐样式 */
.restaurant-recommendations {
  margin-top: 15px;
  padding: 15px;
  background: #f8fafc;
  border-radius: 10px;
  border-left: 4px solid #06b6d4;
  max-height: 200px;
  overflow-y: auto;
}

.restaurant-recommendations p {
  margin: 0 0 10px 0;
  color: #0891b2;
  font-weight: 500;
}

.restaurant-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.restaurant-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 15px;
  background: white;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
  transition: all 0.2s ease;
}

.restaurant-item:hover {
  border-color: #06b6d4;
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(6, 182, 212, 0.1);
}

.restaurant-name {
  font-weight: 500;
  color: #1e293b;
  font-size: 15px;
  flex: 1;
}

.restaurant-type {
  font-size: 12px;
  color: #64748b;
  background: #f1f5f9;
  padding: 4px 10px;
  border-radius: 12px;
  font-weight: 500;
}

.restaurant-tag {
  display: inline-block;
  padding: 4px 12px;
  background: #06b6d4;
  color: white;
  border-radius: 15px;
  font-size: 12px;
  font-weight: 500;
}

.dark-mode .restaurant-recommendations {
  background: #1e293b;
  border-left-color: #06b6d4;
}

.dark-mode .restaurant-recommendations p {
  color: #67e8f9;
}

/* 暗黑模式下的组别选择器 */
.dark-mode .group-selector :deep(.el-input__inner) {
  background-color: #363636;
  border-color: #667eea;
  color: #e5eaf3;
}

.dark-mode .no-group-selected {
  background: linear-gradient(135deg, #2d2d2d 0%, #404040 100%);
  color: #b0b0b0;
}

/* 布局样式 */
.management-layout {
  display: flex;
  gap: 30px;
  align-items: flex-start;
}

.wheel-section-left {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

/* 组员管理区域样式 */
.group-management {
  width: 350px;
  background: #f8f9fa;
  border-radius: 10px;
  padding: 20px;
  border: 2px solid #e9ecef;
  max-height: 600px;
  overflow-y: auto;
}

.group-management-header {
  text-align: center;
  margin-bottom: 20px;
  border-bottom: 2px solid #dee2e6;
  padding-bottom: 10px;
}

.group-management-header h3 {
  margin: 0;
  color: #495057;
  font-size: 18px;
}

.groups-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.group-item {
  background: white;
  border-radius: 8px;
  padding: 15px;
  border: 1px solid #dee2e6;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.group-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid #e9ecef;
}

.group-header h4 {
  margin: 0;
  color: #343a40;
  font-size: 16px;
  font-weight: 600;
}

.members-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  min-height: 30px;
  align-items: flex-start;
}

.member-tag {
  cursor: pointer;
  transition: all 0.2s ease;
  user-select: none;
}

.member-tag:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 6px rgba(0,0,0,0.15);
}

.empty-group {
  color: #868e96;
  font-style: italic;
  font-size: 14px;
  padding: 5px 0;
  text-align: center;
  width: 100%;
  background: #f8f9fa;
  border-radius: 4px;
  border: 1px dashed #dee2e6;
}

/* 暗黑模式下的组员管理样式 */
.dark-mode .group-management {
  background: #343a40;
  border-color: #495057;
}

.dark-mode .group-management-header h3 {
  color: #e9ecef;
}

.dark-mode .group-item {
  background: #495057;
  border-color: #6c757d;
}

.dark-mode .group-header {
  border-bottom-color: #6c757d;
}

.dark-mode .group-header h4 {
  color: #e9ecef;
}

.dark-mode .empty-group {
  color: #adb5bd;
  background: #6c757d;
  border-color: #868e96;
}

/* 对话框样式 */
.dialog-footer {
  text-align: right;
}

/* Element UI 标签页样式覆盖 */
:deep(.el-tabs__header) {
  margin: 0 0 30px 0;
}

:deep(.el-tabs__item) {
  font-size: 16px;
  padding: 0 30px;
}

:deep(.el-tabs__item.is-active) {
  color: #667eea;
  font-weight: bold;
}

:deep(.el-tabs__active-bar) {
  background-color: #667eea;
}
</style> 