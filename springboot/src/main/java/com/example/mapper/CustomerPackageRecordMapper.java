package com.example.mapper;

import com.example.entity.CustomerPackageRecord;
import java.util.List;

/**
 * 操作客户镜像包记录相关数据接口
*/
public interface CustomerPackageRecordMapper {

    /**
     * 新增
     */
    int insert(CustomerPackageRecord record);

    /**
     * 根据ID删除
     */
    int deleteById(Integer id);

    /**
     * 批量删除
     */
    void deleteBatch(List<Integer> ids);

    /**
     * 根据ID修改
     */
    int updateById(CustomerPackageRecord record);

    /**
     * 根据ID查询
     */
    CustomerPackageRecord selectById(Integer id);

    /**
     * 条件查询所有
     */
    List<CustomerPackageRecord> selectAll(CustomerPackageRecord record);

    /**
     * 分页条件查询（支持时间范围）
     */
    List<CustomerPackageRecord> selectPage(CustomerPackageRecord record, String startDate, String endDate);

    /**
     * 条件查询总数（支持时间范围）
     */
    int selectCount(CustomerPackageRecord record, String startDate, String endDate);

    /**
     * 将指定版本和分类的记录移动到"其他"分类
     */
    int updateCategoryToOther(String androidVersion, String fromCategory);

} 