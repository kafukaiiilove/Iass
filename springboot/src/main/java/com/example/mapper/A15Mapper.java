package com.example.mapper;

import com.example.entity.A15;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 操作a15相关数据接口
*/
public interface A15Mapper {

    /**
      * 新增
    */
    int insert(A15 a15);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(A15 a15);

    /**
      * 根据ID查询
    */
    A15 selectById(Integer id);

    /**
      * 查询所有
    */
    List<A15> selectAll(A15 a15);

    /**
     * 获取主键最大值
     */
    Integer getMaxId();

    /**
     * 更新主键设置值方法
     */
    void updateAutoIncrement(@Param("value") int value);
} 