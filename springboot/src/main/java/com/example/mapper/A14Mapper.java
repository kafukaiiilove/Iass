package com.example.mapper;

import com.example.entity.A14;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 操作a14相关数据接口
*/
public interface A14Mapper {

    /**
      * 新增
    */
    int insert(A14 a14);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(A14 a14);

    /**
      * 根据ID查询
    */
    A14 selectById(Integer id);

    /**
      * 查询所有
    */
    List<A14> selectAll(A14 a14);

    /**
     * 获取主键最大值
     */
    Integer getMaxId();

    /**
     * 更新主键设置值方法
     */
    void updateAutoIncrement(@Param("value") int value);
} 