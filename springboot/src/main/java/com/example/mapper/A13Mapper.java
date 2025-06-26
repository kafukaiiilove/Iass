package com.example.mapper;

import com.example.entity.A13;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 操作a13相关数据接口
*/
public interface A13Mapper {

    /**
      * 新增
    */
    int insert(A13 a13);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(A13 a13);

    /**
      * 根据ID查询
    */
    A13 selectById(Integer id);

    /**
      * 查询所有
    */
    List<A13> selectAll(A13 a13);
    /**
     * 获取主键最大值
     */
    Integer getMaxId();
    /**
     * 更新主键设置值方法
     */
    void updateAutoIncrement(@Param("value") int value);


}