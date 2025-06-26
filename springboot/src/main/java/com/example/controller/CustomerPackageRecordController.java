package com.example.controller;

import com.example.common.Result;
import com.example.entity.CustomerPackageRecord;
import com.example.service.CustomerPackageRecordService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * 客户镜像包记录表前端操作接口
 **/
@RestController
@RequestMapping("/api/manager/customer_package")
public class CustomerPackageRecordController {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomerPackageRecordController.class);

    @Resource
    private CustomerPackageRecordService customerPackageRecordService;

    /**
     * 测试接口 - 验证数据库连接
     */
    @GetMapping("/test")
    public Result test() {
        logger.info("开始测试数据库连接");
        try {
            // 尝试查询所有记录
            List<CustomerPackageRecord> list = customerPackageRecordService.selectAll(new CustomerPackageRecord());
            logger.info("数据库连接测试成功，查询到{}条记录", list.size());
            return Result.success("数据库连接正常，表存在，当前记录数：" + list.size());
        } catch (Exception e) {
            logger.error("数据库连接测试失败，错误信息：{}", e.getMessage(), e);
            return Result.error("500", "数据库连接失败：" + e.getMessage());
        }
    }

    /**
     * 简单测试接口 - 只测试基本连接
     */
    @GetMapping("/simple-test")
    public Result simpleTest() {
        logger.info("开始简单测试");
        try {
            // 创建一个简单的测试对象
            CustomerPackageRecord testRecord = new CustomerPackageRecord();
            testRecord.setAndroidVersion("a10");
            testRecord.setCategory("vmos");
            
            // 尝试查询
            List<CustomerPackageRecord> list = customerPackageRecordService.selectAll(testRecord);
            logger.info("简单测试成功，查询到{}条记录", list.size());
            return Result.success("测试成功，查询到" + list.size() + "条记录");
        } catch (Exception e) {
            logger.error("简单测试失败，错误信息：{}", e.getMessage(), e);
            return Result.error("500", "测试失败：" + e.getMessage());
        }
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody CustomerPackageRecord record) {
        logger.info("开始新增客户镜像包记录，版本：{}，分类：{}", record.getAndroidVersion(), record.getCategory());
        try {
            customerPackageRecordService.add(record);
            logger.info("新增客户镜像包记录成功，ID：{}", record.getId());
            return Result.success();
        } catch (Exception e) {
            logger.error("新增客户镜像包记录失败，错误信息：{}", e.getMessage(), e);
            return Result.error("500", "新增失败：" + e.getMessage());
        }
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        logger.info("开始删除客户镜像包记录，ID：{}", id);
        try {
            customerPackageRecordService.deleteById(id);
            logger.info("删除客户镜像包记录成功，ID：{}", id);
            return Result.success();
        } catch (Exception e) {
            logger.error("删除客户镜像包记录失败，ID：{}，错误信息：{}", id, e.getMessage(), e);
            return Result.error("500", "删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        logger.info("开始批量删除客户镜像包记录，IDs：{}", ids);
        try {
            customerPackageRecordService.deleteBatch(ids);
            logger.info("批量删除客户镜像包记录成功，IDs：{}", ids);
            return Result.success();
        } catch (Exception e) {
            logger.error("批量删除客户镜像包记录失败，IDs：{}，错误信息：{}", ids, e.getMessage(), e);
            return Result.error("500", "批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody CustomerPackageRecord record) {
        logger.info("开始更新客户镜像包记录，ID：{}", record.getId());
        try {
            customerPackageRecordService.updateById(record);
            logger.info("更新客户镜像包记录成功，ID：{}", record.getId());
            return Result.success();
        } catch (Exception e) {
            logger.error("更新客户镜像包记录失败，ID：{}，错误信息：{}", record.getId(), e.getMessage(), e);
            return Result.error("500", "更新失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        logger.info("开始查询客户镜像包记录，ID：{}", id);
        try {
            CustomerPackageRecord record = customerPackageRecordService.selectById(id);
            if (record == null) {
                logger.warn("未找到客户镜像包记录，ID：{}", id);
                return Result.error("404", "未找到记录");
            }
            logger.info("查询客户镜像包记录成功，ID：{}", id);
            return Result.success(record);
        } catch (Exception e) {
            logger.error("查询客户镜像包记录失败，ID：{}，错误信息：{}", id, e.getMessage(), e);
            return Result.error("500", "查询失败：" + e.getMessage());
        }
    }

    /**
     * 分页条件查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            CustomerPackageRecord record) {
        logger.info("开始分页查询客户镜像包记录，页码：{}，每页大小：{}，版本：{}，分类：{}，开始日期：{}，结束日期：{}", 
                   pageNum, pageSize, record.getAndroidVersion(), record.getCategory(), startDate, endDate);
        try {
            PageInfo<CustomerPackageRecord> pageInfo;
            
            // 如果有时间范围参数，使用支持时间范围的查询方法
            if (startDate != null || endDate != null) {
                pageInfo = customerPackageRecordService.selectPageWithDateRange(record, pageNum, pageSize, startDate, endDate);
            } else {
                pageInfo = customerPackageRecordService.selectPage(record, pageNum, pageSize);
            }
            
            logger.info("分页查询客户镜像包记录成功，总记录数：{}，当前页记录数：{}", 
                       pageInfo.getTotal(), pageInfo.getList().size());
            return Result.success(pageInfo);
        } catch (Exception e) {
            logger.error("分页查询客户镜像包记录失败，错误信息：{}", e.getMessage(), e);
            return Result.error("500", "查询失败：" + e.getMessage());
        }
    }
} 