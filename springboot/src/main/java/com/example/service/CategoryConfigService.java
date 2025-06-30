package com.example.service;

import com.example.entity.CategoryConfig;
import com.example.mapper.CategoryConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 分类配置表 Service
 */
@Service
public class CategoryConfigService {
    
    private static final Logger logger = LoggerFactory.getLogger(CategoryConfigService.class);

    @Resource
    private CategoryConfigMapper categoryConfigMapper;

    /**
     * 添加新分类
     */
    public void addCategory(String androidVersion, String categoryName) {
        logger.info("Service层：开始添加新分类，版本：{}，分类名称：{}", androidVersion, categoryName);
        
        try {
            // 生成英文分类值，避免中文URL编码问题
            String categoryValue = generateCategoryValue(categoryName);
            
            // 检查是否已存在
            int count = categoryConfigMapper.countByVersionAndValue(androidVersion, categoryValue);
            if (count > 0) {
                throw new RuntimeException("分类已存在：" + categoryName);
            }
            
            // 获取当前版本的最大排序号
            int maxSortOrder = categoryConfigMapper.getMaxSortOrder(androidVersion);
            
            // 创建新分类配置
            CategoryConfig categoryConfig = new CategoryConfig();
            categoryConfig.setAndroidVersion(androidVersion);
            categoryConfig.setCategoryName(categoryName);
            categoryConfig.setCategoryValue(categoryValue);
            categoryConfig.setIsDefault(false); // 用户添加的分类不是默认分类
            categoryConfig.setSortOrder(maxSortOrder + 1); // 排在最后，但在"其他"之前
            
            // 如果是"其他"分类，需要特殊处理排序
            if ("other".equals(categoryValue) || "其他".equals(categoryName)) {
                categoryConfig.setSortOrder(99); // "其他"分类始终排在最后
            } else {
                // 新分类插入到"其他"分类之前
                categoryConfig.setSortOrder(Math.min(maxSortOrder + 1, 98));
            }
            
            int result = categoryConfigMapper.insert(categoryConfig);
            logger.info("Service层：添加新分类成功，影响行数：{}，分类ID：{}", result, categoryConfig.getId());
            
        } catch (Exception e) {
            logger.error("Service层：添加新分类失败，版本：{}，分类名称：{}，错误信息：{}", androidVersion, categoryName, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 根据Android版本查询分类列表
     */
    public List<CategoryConfig> getCategoriesByVersion(String androidVersion) {
        logger.info("Service层：查询版本{}的分类列表", androidVersion);
        try {
            List<CategoryConfig> categories = categoryConfigMapper.selectByAndroidVersion(androidVersion);
            logger.info("Service层：查询到{}个分类", categories.size());
            return categories;
        } catch (Exception e) {
            logger.error("Service层：查询分类列表失败，版本：{}，错误信息：{}", androidVersion, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 检查分类是否存在
     */
    public boolean categoryExists(String androidVersion, String categoryValue) {
        try {
            int count = categoryConfigMapper.countByVersionAndValue(androidVersion, categoryValue);
            return count > 0;
        } catch (Exception e) {
            logger.error("Service层：检查分类是否存在失败，版本：{}，分类值：{}，错误信息：{}", androidVersion, categoryValue, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 删除分类
     */
    public void deleteCategory(Integer id) {
        logger.info("Service层：开始删除分类，ID：{}", id);
        try {
            // 先查询分类信息
            CategoryConfig categoryConfig = categoryConfigMapper.selectById(id);
            if (categoryConfig == null) {
                throw new RuntimeException("分类不存在");
            }
            
            // 检查是否为默认分类
            if (categoryConfig.getIsDefault()) {
                throw new RuntimeException("默认分类不能删除");
            }
            
            int result = categoryConfigMapper.deleteById(id);
            logger.info("Service层：删除分类成功，影响行数：{}", result);
        } catch (Exception e) {
            logger.error("Service层：删除分类失败，ID：{}，错误信息：{}", id, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 查询所有分类配置
     */
    public List<CategoryConfig> getAllCategories() {
        logger.info("Service层：查询所有分类配置");
        try {
            List<CategoryConfig> categories = categoryConfigMapper.selectAll();
            logger.info("Service层：查询到{}个分类配置", categories.size());
            return categories;
        } catch (Exception e) {
            logger.error("Service层：查询所有分类配置失败，错误信息：{}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 新增分类配置
     */
    public boolean addCategory(CategoryConfig categoryConfig) {
        // 检查是否已存在相同的分类
        int count = categoryConfigMapper.countByVersionAndValue(
            categoryConfig.getAndroidVersion(), 
            categoryConfig.getCategoryValue()
        );
        
        if (count > 0) {
            throw new RuntimeException("该分类已存在");
        }
        
        // 设置创建时间
        categoryConfig.setCreateTime(LocalDateTime.now());
        categoryConfig.setUpdateTime(LocalDateTime.now());
        
        // 如果没有设置排序，使用最大排序号+1
        if (categoryConfig.getSortOrder() == null) {
            int maxSortOrder = categoryConfigMapper.getMaxSortOrder(categoryConfig.getAndroidVersion());
            categoryConfig.setSortOrder(maxSortOrder + 1);
        }
        
        // 如果没有设置默认值，设为false
        if (categoryConfig.getIsDefault() == null) {
            categoryConfig.setIsDefault(false);
        }
        
        return categoryConfigMapper.insert(categoryConfig) > 0;
    }

    /**
     * 根据ID查询分类配置
     */
    public CategoryConfig getCategoryById(Integer id) {
        return categoryConfigMapper.selectById(id);
    }

    /**
     * 查询指定版本和分类下的记录数量
     */
    public int getRecordCountByVersionAndCategory(String androidVersion, String category) {
        return categoryConfigMapper.countRecordsByVersionAndCategory(androidVersion, category);
    }
    
    /**
     * 生成分类值（英文或拼音，避免中文URL编码问题）
     */
    private String generateCategoryValue(String categoryName) {
        // 常见分类的映射
        switch (categoryName) {
            case "比特":
                return "bite";
            case "性能优化":
                return "performance";
            case "安全增强":
                return "security";
            case "功能扩展":
                return "feature";
            case "Bug修复":
                return "bugfix";
            case "兼容性":
                return "compatibility";
            default:
                // 对于其他分类，使用拼音或简化的英文标识
                // 这里简单处理：去除空格，转小写，如果包含中文则用时间戳
                String value = categoryName.toLowerCase().replaceAll("\\s+", "_");
                if (value.matches(".*[\\u4e00-\\u9fa5].*")) {
                    // 包含中文，使用简化标识
                    return "custom_" + System.currentTimeMillis() % 10000;
                }
                return value;
        }
    }
} 