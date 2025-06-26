package com.example.mapper;

import com.example.entity.A12;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 操作a12相关数据接口
*/
public interface A12Mapper {

    /**
      * 新增
    */
    int insert(A12 a12);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(A12 a12);

    /**
      * 根据ID查询
    */
    A12 selectById(Integer id);

    /**
      * 查询所有
    */
    List<A12> selectAll(A12 a12);

    /**
     * 获取主键最大值
     */
    Integer getMaxId();

    /**
     * 更新主键设置值方法
     */
    void updateAutoIncrement(@Param("value") int value);
} 