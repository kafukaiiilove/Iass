package com.example.service;

import com.example.entity.CustomerPackageRecord;
import com.example.mapper.CustomerPackageRecordMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * 客户镜像包记录表 Service
 **/
@Service
public class CustomerPackageRecordService {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomerPackageRecordService.class);

    @Resource
    private CustomerPackageRecordMapper customerPackageRecordMapper;

    /**
     * 新增
     */
    public void add(CustomerPackageRecord record) {
        logger.info("Service层：开始新增客户镜像包记录");
        try {
            int result = customerPackageRecordMapper.insert(record);
            logger.info("Service层：新增客户镜像包记录成功，影响行数：{}", result);
        } catch (Exception e) {
            logger.error("Service层：新增客户镜像包记录失败，错误信息：{}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 根据ID删除
     */
    public void deleteById(Integer id) {
        logger.info("Service层：开始删除客户镜像包记录，ID：{}", id);
        try {
            int result = customerPackageRecordMapper.deleteById(id);
            logger.info("Service层：删除客户镜像包记录成功，影响行数：{}", result);
        } catch (Exception e) {
            logger.error("Service层：删除客户镜像包记录失败，ID：{}，错误信息：{}", id, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        logger.info("Service层：开始批量删除客户镜像包记录，IDs：{}", ids);
        try {
            customerPackageRecordMapper.deleteBatch(ids);
            logger.info("Service层：批量删除客户镜像包记录成功");
        } catch (Exception e) {
            logger.error("Service层：批量删除客户镜像包记录失败，IDs：{}，错误信息：{}", ids, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 根据ID修改
     */
    public void updateById(CustomerPackageRecord record) {
        logger.info("Service层：开始更新客户镜像包记录，ID：{}", record.getId());
        try {
            int result = customerPackageRecordMapper.updateById(record);
            logger.info("Service层：更新客户镜像包记录成功，影响行数：{}", result);
        } catch (Exception e) {
            logger.error("Service层：更新客户镜像包记录失败，ID：{}，错误信息：{}", record.getId(), e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 根据ID查询
     */
    public CustomerPackageRecord selectById(Integer id) {
        logger.info("Service层：开始查询客户镜像包记录，ID：{}", id);
        try {
            CustomerPackageRecord record = customerPackageRecordMapper.selectById(id);
            logger.info("Service层：查询客户镜像包记录成功，ID：{}", id);
            return record;
        } catch (Exception e) {
            logger.error("Service层：查询客户镜像包记录失败，ID：{}，错误信息：{}", id, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 条件查询所有
     */
    public List<CustomerPackageRecord> selectAll(CustomerPackageRecord record) {
        logger.info("Service层：开始条件查询客户镜像包记录");
        try {
            List<CustomerPackageRecord> list = customerPackageRecordMapper.selectAll(record);
            logger.info("Service层：条件查询客户镜像包记录成功，查询到{}条记录", list.size());
            return list;
        } catch (Exception e) {
            logger.error("Service层：条件查询客户镜像包记录失败，错误信息：{}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 分页条件查询
     */
    public PageInfo<CustomerPackageRecord> selectPage(CustomerPackageRecord record, Integer pageNum, Integer pageSize) {
        logger.info("Service层：开始分页查询客户镜像包记录，页码：{}，每页大小：{}", pageNum, pageSize);
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<CustomerPackageRecord> list = customerPackageRecordMapper.selectAll(record);
            PageInfo<CustomerPackageRecord> pageInfo = PageInfo.of(list);
            logger.info("Service层：分页查询客户镜像包记录成功，总记录数：{}，当前页记录数：{}", 
                       pageInfo.getTotal(), pageInfo.getList().size());
            return pageInfo;
        } catch (Exception e) {
            logger.error("Service层：分页查询客户镜像包记录失败，错误信息：{}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 分页条件查询（支持时间范围）
     */
    public PageInfo<CustomerPackageRecord> selectPageWithDateRange(CustomerPackageRecord record, Integer pageNum, Integer pageSize, String startDate, String endDate) {
        logger.info("Service层：开始分页查询客户镜像包记录（支持时间范围），页码：{}，每页大小：{}，开始日期：{}，结束日期：{}", 
                   pageNum, pageSize, startDate, endDate);
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<CustomerPackageRecord> list = customerPackageRecordMapper.selectPage(record, startDate, endDate);
            PageInfo<CustomerPackageRecord> pageInfo = PageInfo.of(list);
            logger.info("Service层：分页查询客户镜像包记录成功，总记录数：{}，当前页记录数：{}", 
                       pageInfo.getTotal(), pageInfo.getList().size());
            return pageInfo;
        } catch (Exception e) {
            logger.error("Service层：分页查询客户镜像包记录失败，错误信息：{}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 将指定版本和分类的记录移动到"其他"分类
     */
    public void moveCategoryRecordsToOther(String androidVersion, String fromCategory) {
        logger.info("Service层：开始将记录从分类{}移动到其他分类，版本：{}", fromCategory, androidVersion);
        try {
            int result = customerPackageRecordMapper.updateCategoryToOther(androidVersion, fromCategory);
            logger.info("Service层：移动记录到其他分类成功，影响行数：{}", result);
        } catch (Exception e) {
            logger.error("Service层：移动记录到其他分类失败，版本：{}，原分类：{}，错误信息：{}", 
                        androidVersion, fromCategory, e.getMessage(), e);
            throw e;
        }
    }
} 