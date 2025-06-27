/**
 * 权限管理工具
 */

// 角色枚举
export const ROLES = {
  ADMIN: 'ADMIN',      // 管理员
  KAIFA: 'KaiFa',      // 开发工程师
  CESHI: 'CeShi'       // 测试工程师
}

// 获取当前用户信息
export function getCurrentUser() {
  const user = JSON.parse(localStorage.getItem('xm-user') || '{}')
  return user
}

// 获取当前用户角色
export function getCurrentUserRole() {
  const user = getCurrentUser()
  return user.role || ''
}

// 检查是否为管理员
export function isAdmin() {
  return getCurrentUserRole() === ROLES.ADMIN
}

// 检查是否为开发工程师
export function isKaiFa() {
  return getCurrentUserRole() === ROLES.KAIFA
}

// 检查是否为测试工程师
export function isCeshi() {
  return getCurrentUserRole() === ROLES.CESHI
}

// 检查是否有系统管理权限
export function hasSystemPermission() {
  return isAdmin()
}

// 检查是否有删除权限
export function hasDeletePermission() {
  return isAdmin()
}

// 检查是否有批量删除权限
export function hasBatchDeletePermission() {
  return isAdmin()
}

// 检查是否有新增记录权限
export function hasAddPermission() {
  return isAdmin() || isKaiFa() || isCeshi() // 管理员、开发工程师和测试工程师可以新增
}

// 检查是否有编辑权限
export function hasEditPermission() {
  return isAdmin() || isKaiFa() || isCeshi() // 所有角色都可以编辑
}

// 开发工程师可编辑的字段
export const KAIFA_EDITABLE_FIELDS = [
  'recordDate',     // 时间年月日时分
  'packageUrl',     // 镜像包地址
  'fixContent',     // 修复内容描述
  'imageId',        // 镜像ID
  'developer',      // 开发工程师
  'commitId',       // Commit id
  'androidVersion', // Android版本
  'category',       // 分类
  'remarks'         // 备注
]

// 测试工程师可编辑的字段
export const CESHI_EDITABLE_FIELDS = [
  'tester',         // 测试工程师
  'testResult',     // 测试结果
  'remarks'         // 备注
]

// 检查字段是否可编辑
export function isFieldEditable(fieldName, isAddMode = false) {
  const role = getCurrentUserRole()
  
  if (role === ROLES.ADMIN) {
    // 管理员可以编辑所有字段
    return true
  } else if (role === ROLES.KAIFA) {
    // 开发工程师只能编辑指定字段
    return KAIFA_EDITABLE_FIELDS.includes(fieldName)
  } else if (role === ROLES.CESHI) {
    // 测试工程师：新增时可以编辑所有字段，编辑时只能编辑指定字段
    if (isAddMode) {
      return true
    } else {
      return CESHI_EDITABLE_FIELDS.includes(fieldName)
    }
  }
  
  return false
}

// 获取角色显示名称
export function getRoleDisplayName(role) {
  const roleMap = {
    [ROLES.ADMIN]: '管理员',
    [ROLES.KAIFA]: '开发工程师',
    [ROLES.CESHI]: '测试工程师'
  }
  return roleMap[role] || role
}

// 检查字段是否只读
export function isFieldReadOnly(fieldName) {
  return !isFieldEditable(fieldName)
}

// 获取当前用户可编辑的字段列表
export function getEditableFields() {
  const role = getCurrentUserRole()
  
  if (role === ROLES.ADMIN) {
    return [...KAIFA_EDITABLE_FIELDS, ...CESHI_EDITABLE_FIELDS]
  } else if (role === ROLES.KAIFA) {
    return KAIFA_EDITABLE_FIELDS
  } else if (role === ROLES.CESHI) {
    return CESHI_EDITABLE_FIELDS
  }
  
  return []
}

// 过滤表单数据，只保留当前用户可编辑的字段
export function filterEditableFormData(formData, isAddMode = false) {
  const role = getCurrentUserRole()
  
  // 管理员或者测试工程师新增时，返回所有数据
  if (role === ROLES.ADMIN || (role === ROLES.CESHI && isAddMode)) {
    return formData
  }
  
  const editableFields = getEditableFields()
  const filteredData = {}
  
  // 保留ID字段
  if (formData.id) {
    filteredData.id = formData.id
  }
  
  // 只保留可编辑的字段
  editableFields.forEach(field => {
    if (formData.hasOwnProperty(field)) {
      filteredData[field] = formData[field]
    }
  })
  
  return filteredData
} 