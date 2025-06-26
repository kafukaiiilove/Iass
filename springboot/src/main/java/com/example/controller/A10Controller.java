package com.example.controller;

import com.example.common.Result;
import com.example.entity.A10;
import com.example.service.A10Service;
import com.example.common.enums.ResultCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.time.LocalDateTime;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import com.example.utils.TokenUtils;
import javax.servlet.http.HttpServletRequest;
import com.example.entity.Account;
import com.github.pagehelper.PageInfo;

/**
 * Android 10系统信息表接口
 **/
@RestController
@RequestMapping("/api/manager/a10")
public class A10Controller {
    private static final Logger logger = LoggerFactory.getLogger(A10Controller.class);

    @Autowired
    private A10Service a10Service;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody A10 a10) {
        logger.info("开始新增A10，标题：{}", a10.getTitle());
        try {
            // 检查必要字段是否为空
            if (a10.getTitle() == null || a10.getTitle().trim().isEmpty()) {
                logger.error("功能名称为空");
                return Result.error(ResultCodeEnum.PARAM_ERROR.toString(), "功能名称不能为空");
            }
            
            // 检查用户是否登录
            Account currentUser = TokenUtils.getCurrentUser();
            if (currentUser == null) {
                logger.error("用户未登录");
                return Result.error(ResultCodeEnum.NO_AUTH.toString(), "用户未登录");
            }
            
            // 设置时间和用户
            a10.setTime(LocalDateTime.now());
            a10.setUser(currentUser.getUsername());
            
            // 保存到数据库
            try {
                a10Service.add(a10);
                logger.info("新增A10成功，ID：{}", a10.getId());
                return Result.success(a10);
            } catch (Exception e) {
                logger.error("保存到数据库失败，错误信息：{}", e.getMessage(), e);
                return Result.error(ResultCodeEnum.SYSTEM_ERROR.toString(), "保存到数据库失败：" + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("新增A10失败，错误信息：{}", e.getMessage(), e);
            return Result.error(ResultCodeEnum.SYSTEM_ERROR.toString(), "新增A10失败：" + e.getMessage());
        }
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        logger.info("开始删除A10，ID：{}", id);
        try {
            a10Service.deleteById(id);
            logger.info("删除A10成功，ID：{}", id);
            return Result.success();
        } catch (Exception e) {
            logger.error("删除A10失败，ID：{}，错误信息：{}", id, e.getMessage(), e);
            return Result.error(ResultCodeEnum.SYSTEM_ERROR.toString(), "删除A10失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        logger.info("开始批量删除A10，IDs：{}", ids);
        try {
            a10Service.deleteBatch(ids);
            logger.info("批量删除A10成功，IDs：{}", ids);
            return Result.success();
        } catch (Exception e) {
            logger.error("批量删除A10失败，IDs：{}，错误信息：{}", ids, e.getMessage(), e);
            return Result.error(ResultCodeEnum.SYSTEM_ERROR.toString(), "批量删除A10失败：" + e.getMessage());
        }
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody A10 a10) {
        logger.info("开始更新A10，ID：{}", a10.getId());
        try {
            // 检查必要字段是否为空
            if (a10.getTitle() == null || a10.getTitle().trim().isEmpty()) {
                logger.error("功能名称为空");
                return Result.error(ResultCodeEnum.PARAM_ERROR.toString(), "功能名称不能为空");
            }
            
            // 检查用户是否登录
            Account currentUser = TokenUtils.getCurrentUser();
            if (currentUser == null) {
                logger.error("用户未登录");
                return Result.error(ResultCodeEnum.NO_AUTH.toString(), "用户未登录");
            }
            
            // 设置更新时间
            a10.setTime(LocalDateTime.now());
            
            a10Service.updateById(a10);
            logger.info("更新A10成功，ID：{}", a10.getId());
            return Result.success();
        } catch (Exception e) {
            logger.error("更新A10失败，ID：{}，错误信息：{}", a10.getId(), e.getMessage(), e);
            return Result.error(ResultCodeEnum.SYSTEM_ERROR.toString(), "更新A10失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        logger.info("开始查询A10，ID：{}", id);
        try {
            A10 a10 = a10Service.selectById(id);
            if (a10 == null) {
                logger.warn("未找到A10，ID：{}", id);
                return Result.error(ResultCodeEnum.PARAM_ERROR.toString(), "未找到A10");
            }
            logger.info("查询A10成功，ID：{}", id);
            return Result.success(a10);
        } catch (Exception e) {
            logger.error("查询A10失败，ID：{}，错误信息：{}", id, e.getMessage(), e);
            return Result.error(ResultCodeEnum.SYSTEM_ERROR.toString(), "查询A10失败：" + e.getMessage());
        }
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(A10 a10) {
        logger.info("开始查询所有A10");
        try {
            List<A10> list = a10Service.selectAll(a10);
            logger.info("查询所有A10成功，数量：{}", list.size());
            return Result.success(list);
        } catch (Exception e) {
            logger.error("查询所有A10失败，错误信息：{}", e.getMessage(), e);
            return Result.error(ResultCodeEnum.SYSTEM_ERROR.toString(), "查询所有A10失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(A10 a10,
                           @RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "10") Integer pageSize) {
        logger.info("开始分页查询A10，页码：{}，每页大小：{}", pageNum, pageSize);
        try {
            PageInfo<A10> pageInfo = a10Service.selectPage(a10, pageNum, pageSize);
            logger.info("分页查询A10成功，总记录数：{}，当前页记录数：{}", pageInfo.getTotal(), pageInfo.getList().size());
            return Result.success(pageInfo);
        } catch (Exception e) {
            logger.error("分页查询A10失败，错误信息：{}", e.getMessage(), e);
            return Result.error(ResultCodeEnum.SYSTEM_ERROR.toString(), "分页查询A10失败：" + e.getMessage());
        }
    }

    /**
     * 上传并解析Markdown文件
     */
    @PostMapping("/markdown/upload")
    public Result uploadMarkdown(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        logger.info("开始上传markdown文件，文件名：{}，大小：{}字节", file.getOriginalFilename(), file.getSize());
        try {
            if (file.isEmpty()) {
                logger.error("文件为空");
                return Result.error(ResultCodeEnum.PARAM_ERROR.toString(), "文件为空");
            }

            String originalFilename = file.getOriginalFilename();
            if (!originalFilename.endsWith(".md")) {
                logger.error("文件类型错误：{}", originalFilename);
                return Result.error(ResultCodeEnum.PARAM_ERROR.toString(), "只能上传markdown文件");
            }

            // 读取文件内容
            String contentText = new String(file.getBytes(), StandardCharsets.UTF_8);
            logger.info("成功读取文件内容，长度：{}字符", contentText.length());
            
            // 解析markdown内容
            String[] sections = contentText.split("## ");
            A10 a10 = new A10();
            a10.setTime(LocalDateTime.now());
            String username = TokenUtils.getCurrentUser().getUsername();
            a10.setUser(username);

            for (String section : sections) {
                if (section.trim().isEmpty()) continue;
                String[] lines = section.split("\\r?\\n", 2); // 只分割成两部分
                if (lines.length < 2) continue;
                String key = lines[0].trim();
                String value = lines[1].trim();
                if (key.equals("功能描述") || key.equals("标题")) {
                    a10.setTitle(value);
                } else if (key.equals("属性键名") || key.equals("内容")) {
                    a10.setContent(value);
                } else if (key.equals("实例参数") || key.equals("参数")) {
                    a10.setParameter(value);
                } else if (key.equals("参数描述") || key.equals("描述")) {
                    a10.setDescription(value);
                }
            }

            // 检查必要字段是否为空
            if (a10.getTitle() == null || a10.getTitle().trim().isEmpty()) {
                logger.error("功能名称为空");
                return Result.error(ResultCodeEnum.PARAM_ERROR.toString(), "功能名称不能为空");
            }
            
            return Result.success(a10);
        } catch (IOException e) {
            logger.error("文件上传失败", e);
            return Result.error(ResultCodeEnum.SYSTEM_ERROR.toString(), "文件上传失败");
        } catch (Exception e) {
            logger.error("处理markdown文件失败", e);
            return Result.error(ResultCodeEnum.SYSTEM_ERROR.toString(), "处理文件失败");
        }
    }
} 