package com.example.mapper;

import com.example.entity.CategoryConfig;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 分类配置表 Mapper
 */
@Mapper
public interface CategoryConfigMapper {

    /**
     * 新增分类配置
     */
    @Insert("INSERT INTO category_config (android_version, category_name, category_value, is_default, sort_order) " +
            "VALUES (#{androidVersion}, #{categoryName}, #{categoryValue}, #{isDefault}, #{sortOrder})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CategoryConfig categoryConfig);

    /**
     * 根据Android版本查询分类列表
     */
    @Select("SELECT * FROM category_config WHERE android_version = #{androidVersion} ORDER BY sort_order ASC, id ASC")
    List<CategoryConfig> selectByAndroidVersion(@Param("androidVersion") String androidVersion);

    /**
     * 检查分类是否已存在
     */
    @Select("SELECT COUNT(*) FROM category_config WHERE android_version = #{androidVersion} AND category_value = #{categoryValue}")
    int countByVersionAndValue(@Param("androidVersion") String androidVersion, @Param("categoryValue") String categoryValue);

    /**
     * 根据版本和分类值查询
     */
    @Select("SELECT * FROM category_config WHERE android_version = #{androidVersion} AND category_value = #{categoryValue}")
    CategoryConfig selectByVersionAndValue(@Param("androidVersion") String androidVersion, @Param("categoryValue") String categoryValue);

    /**
     * 删除分类配置
     */
    @Delete("DELETE FROM category_config WHERE id = #{id}")
    int deleteById(@Param("id") Integer id);

    /**
     * 获取指定版本的最大排序号
     */
    @Select("SELECT COALESCE(MAX(sort_order), 0) FROM category_config WHERE android_version = #{androidVersion}")
    int getMaxSortOrder(String androidVersion);

    /**
     * 查询所有分类配置
     */
    @Select("SELECT * FROM category_config ORDER BY android_version ASC, sort_order ASC, id ASC")
    List<CategoryConfig> selectAll();

    /**
     * 根据ID查询分类配置
     */
    @Select("SELECT * FROM category_config WHERE id = #{id}")
    CategoryConfig selectById(@Param("id") Integer id);

    /**
     * 更新分类配置
     */
    @Update("UPDATE category_config SET android_version = #{androidVersion}, category_name = #{categoryName}, category_value = #{categoryValue}, is_default = #{isDefault}, sort_order = #{sortOrder} WHERE id = #{id}")
    int updateById(CategoryConfig categoryConfig);

    /**
     * 查询指定版本和分类下的记录数量
     */
    @Select("SELECT COUNT(*) FROM customer_package_record WHERE android_version = #{androidVersion} AND category = #{category}")
    int countRecordsByVersionAndCategory(@Param("androidVersion") String androidVersion, @Param("category") String category);
} 