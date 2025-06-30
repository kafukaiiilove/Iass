package com.example.controller;

import com.example.common.Result;
import com.example.entity.A13;
import com.example.service.A13Service;
// import com.example.service.A13MarkdownService;
// import com.example.entity.A13Markdown;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.example.utils.TokenUtils;
import javax.servlet.http.HttpServletRequest;
import com.example.entity.Account;
import com.github.pagehelper.PageInfo;

/**
 * 公告信息表前端操作接口
 **/
@RestController
@RequestMapping("/api/manager/a13")
public class A13Controller {
    private static final Logger logger = LoggerFactory.getLogger(A13Controller.class);

    @Autowired
    private A13Service a13Service;

    

    @Autowired
    private FileController fileController;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody A13 a13) {
        logger.info("开始新增公告，标题：{}", a13.getTitle());
        try {
            // 检查必要字段是否为空
            if (a13.getTitle() == null || a13.getTitle().trim().isEmpty()) {
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
            a13.setTime(LocalDateTime.now().toString());
            a13.setUser(currentUser.getUsername());
            
            // 保存到数据库
            try {
                a13Service.add(a13);
                logger.info("新增公告成功，ID：{}", a13.getId());
                return Result.success(a13);
            } catch (Exception e) {
                logger.error("保存到数据库失败，错误信息：{}", e.getMessage(), e);
                return Result.error(ResultCodeEnum.SYSTEM_ERROR.toString(), "保存到数据库失败：" + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("新增公告失败，错误信息：{}", e.getMessage(), e);
            return Result.error(ResultCodeEnum.SYSTEM_ERROR.toString(), "新增公告失败：" + e.getMessage());
        }
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        logger.info("开始删除公告，ID：{}", id);
        try {
            a13Service.deleteById(id);
            logger.info("删除公告成功，ID：{}", id);
            return Result.success();
        } catch (Exception e) {
            logger.error("删除公告失败，ID：{}，错误信息：{}", id, e.getMessage(), e);
            return Result.error(ResultCodeEnum.SYSTEM_ERROR.toString(), "删除公告失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        logger.info("开始批量删除公告，IDs：{}", ids);
        try {
            a13Service.deleteBatch(ids);
            logger.info("批量删除公告成功，IDs：{}", ids);
            return Result.success();
        } catch (Exception e) {
            logger.error("批量删除公告失败，IDs：{}，错误信息：{}", ids, e.getMessage(), e);
            return Result.error(ResultCodeEnum.SYSTEM_ERROR.toString(), "批量删除公告失败：" + e.getMessage());
        }
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody A13 a13) {
        logger.info("开始更新公告，ID：{}", a13.getId());
        try {
            // 检查必要字段是否为空
            if (a13.getTitle() == null || a13.getTitle().trim().isEmpty()) {
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
            a13.setTime(LocalDateTime.now().toString());
            
            a13Service.update(a13);
            logger.info("更新公告成功，ID：{}", a13.getId());
            return Result.success();
        } catch (Exception e) {
            logger.error("更新公告失败，ID：{}，错误信息：{}", a13.getId(), e.getMessage(), e);
            return Result.error(ResultCodeEnum.SYSTEM_ERROR.toString(), "更新公告失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        logger.info("开始查询公告，ID：{}", id);
        try {
            A13 a13 = a13Service.selectById(id);
            if (a13 == null) {
                logger.warn("未找到公告，ID：{}", id);
                return Result.error(ResultCodeEnum.PARAM_ERROR.toString(), "未找到公告");
            }
            logger.info("查询公告成功，ID：{}", id);
            return Result.success(a13);
        } catch (Exception e) {
            logger.error("查询公告失败，ID：{}，错误信息：{}", id, e.getMessage(), e);
            return Result.error(ResultCodeEnum.SYSTEM_ERROR.toString(), "查询公告失败：" + e.getMessage());
        }
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(A13 a13) {
        logger.info("开始查询所有公告");
        try {
            List<A13> list = a13Service.selectAll(a13);
            logger.info("查询所有公告成功，数量：{}", list.size());
            return Result.success(list);
        } catch (Exception e) {
            logger.error("查询所有公告失败，错误信息：{}", e.getMessage(), e);
            return Result.error(ResultCodeEnum.SYSTEM_ERROR.toString(), "查询所有公告失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "10") Integer pageSize,
                           @RequestParam(required = false) String title,
                           @RequestParam(required = false) String content,
                           @RequestParam(required = false) String parameter,
                           @RequestParam(required = false) String description,
                           @RequestParam(required = false) String user) {
        logger.info("开始分页查询公告，页码：{}，每页大小：{}，标题：{}，内容：{}，参数：{}，描述：{}，用户：{}", 
                   pageNum, pageSize, title, content, parameter, description, user);
        try {
            // 构建查询条件
            A13 a13 = new A13();
            a13.setTitle(title);
            a13.setContent(content);
            a13.setParameter(parameter);
            a13.setDescription(description);
            a13.setUser(user);
            
            // 使用PageHelper进行分页
            PageInfo<A13> pageInfo = a13Service.selectPage(a13, pageNum, pageSize);
            
            Map<String, Object> result = new HashMap<>();
            result.put("list", pageInfo.getList());  // 改为list以保持与前端一致
            result.put("records", pageInfo.getList());  // 保留records作为兼容
            result.put("total", pageInfo.getTotal());
            
            logger.info("分页查询公告成功，总记录数：{}，当前页记录数：{}", pageInfo.getTotal(), pageInfo.getList().size());
            return Result.success(result);
        } catch (Exception e) {
            logger.error("分页查询公告失败，错误信息：{}", e.getMessage(), e);
            return Result.error(ResultCodeEnum.SYSTEM_ERROR.toString(), "分页查询公告失败：" + e.getMessage());
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
            A13 a13 = new A13();
            a13.setTime(LocalDateTime.now().toString());
            String username = TokenUtils.getCurrentUser().getUsername();
            a13.setUser(username);

            for (String section : sections) {
                if (section.trim().isEmpty()) continue;
                String[] lines = section.split("\\r?\\n", 2); // 只分割成两部分
                if (lines.length < 2) continue;
                String key = lines[0].trim();
                String value = lines[1].trim();
                if (key.equals("功能描述") || key.equals("标题")) {
                    a13.setTitle(value);
                } else if (key.equals("属性键名") || key.equals("内容")) {
                    a13.setContent(value);
                } else if (key.equals("实例参数") || key.equals("参数")) {
                    a13.setParameter(value);
                } else if (key.equals("参数描述") || key.equals("描述")) {
                    a13.setDescription(value);
                }
            }

            // 检查必要字段是否为空
            if (a13.getTitle() == null || a13.getTitle().trim().isEmpty()) {
                logger.error("功能名称为空");
                return Result.error(ResultCodeEnum.PARAM_ERROR.toString(), "功能名称不能为空");
            }
            
            return Result.success(a13);
        } catch (IOException e) {
            logger.error("文件上传失败", e);
            return Result.error(ResultCodeEnum.SYSTEM_ERROR.toString(), "文件上传失败");
        } catch (Exception e) {
            logger.error("处理markdown文件失败", e);
            return Result.error(ResultCodeEnum.SYSTEM_ERROR.toString(), "处理文件失败");
        }
    }

}