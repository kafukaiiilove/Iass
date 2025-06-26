package com.example.mapper;

import com.example.entity.A10;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 操作a10相关数据接口
*/
public interface A10Mapper {

    /**
      * 新增
    */
    int insert(A10 a10);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(A10 a10);

    /**
      * 根据ID查询
    */
    A10 selectById(Integer id);

    /**
      * 查询所有
    */
    List<A10> selectAll(A10 a10);

    /**
     * 获取主键最大值
     */
    Integer getMaxId();

    /**
     * 更新主键设置值方法
     */
    void updateAutoIncrement(@Param("value") int value);
} 