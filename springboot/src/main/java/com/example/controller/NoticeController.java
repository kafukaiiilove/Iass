package com.example.controller;

import com.example.common.Result;
import com.example.entity.Notice;
import com.example.service.NoticeService;
import com.example.service.NoticeMarkdownService;
import com.example.entity.NoticeMarkdown;
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

/**
 * 公告信息表前端操作接口
 **/
@RestController
@RequestMapping("/api/manager/notice")
public class NoticeController {
    private static final Logger logger = LoggerFactory.getLogger(NoticeController.class);

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private NoticeMarkdownService noticeMarkdownService;

    @Autowired
    private FileController fileController;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Notice notice) {
        logger.info("开始新增公告，标题：{}", notice.getTitle());
        try {
            // 检查必要字段是否为空
            if (notice.getTitle() == null || notice.getTitle().trim().isEmpty()) {
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
            notice.setTime(LocalDateTime.now());
            notice.setUser(currentUser.getUsername());
            
            // 保存到数据库
            try {
                noticeService.add(notice);
                logger.info("新增公告成功，ID：{}", notice.getId());
                return Result.success(notice);
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
            noticeService.deleteById(id);
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
            noticeService.deleteBatch(ids);
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
    public Result update(@RequestBody Notice notice) {
        logger.info("开始更新公告，ID：{}", notice.getId());
        try {
            // 检查必要字段是否为空
            if (notice.getTitle() == null || notice.getTitle().trim().isEmpty()) {
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
            notice.setTime(LocalDateTime.now());
            
            noticeService.update(notice);
            logger.info("更新公告成功，ID：{}", notice.getId());
            return Result.success();
        } catch (Exception e) {
            logger.error("更新公告失败，ID：{}，错误信息：{}", notice.getId(), e.getMessage(), e);
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
            Notice notice = noticeService.selectById(id);
            if (notice == null) {
                logger.warn("未找到公告，ID：{}", id);
                return Result.error(ResultCodeEnum.PARAM_ERROR.toString(), "未找到公告");
            }
            logger.info("查询公告成功，ID：{}", id);
            return Result.success(notice);
        } catch (Exception e) {
            logger.error("查询公告失败，ID：{}，错误信息：{}", id, e.getMessage(), e);
            return Result.error(ResultCodeEnum.SYSTEM_ERROR.toString(), "查询公告失败：" + e.getMessage());
        }
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Notice notice) {

        try {
            List<Notice> list = noticeService.selectAll(notice);
          
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
                           @RequestParam(required = false) String title) {
        logger.info("开始分页查询公告，页码：{}，每页大小：{}，标题：{}", pageNum, pageSize, title);
        try {
            // 构建查询条件
            Notice notice = new Notice();
            notice.setTitle(title);
            
            // 获取数据
            List<Notice> list = noticeService.selectAll(notice);
            
            // 按时间倒序排序
            list.sort((a, b) -> b.getTime().compareTo(a.getTime()));
            
            // 分页处理
            int start = (pageNum - 1) * pageSize;
            int end = Math.min(start + pageSize, list.size());
            List<Notice> pageList = list.subList(start, end);
            
            Map<String, Object> result = new HashMap<>();
            result.put("records", pageList);
            result.put("total", list.size());
            
            logger.info("分页查询公告成功，总记录数：{}，当前页记录数：{}", list.size(), pageList.size());
            return Result.success(result);
        } catch (Exception e) {
            logger.error("分页查询公告失败，错误信息：{}", e.getMessage(), e);
            return Result.error(ResultCodeEnum.SYSTEM_ERROR.toString(), "分页查询公告失败：" + e.getMessage());
        }
    }

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
            NoticeMarkdown notice = new NoticeMarkdown();
            notice.setTime(LocalDateTime.now().toString());
            String username = TokenUtils.getCurrentUser().getUsername();
            notice.setUser(username);

            for (String section : sections) {
                if (section.trim().isEmpty()) continue;
                String[] lines = section.split("\\r?\\n", 2); // 只分割成两部分
                if (lines.length < 2) continue;
                String key = lines[0].trim();
                String value = lines[1].trim();
                if (key.equals("功能描述") || key.equals("标题")) {
                    notice.setTitle(value);
                } else if (key.equals("属性键名") || key.equals("内容")) {
                    notice.setContent(value);
                } else if (key.equals("实例参数") || key.equals("参数")) {
                    notice.setParameter(value);
                } else if (key.equals("参数描述") || key.equals("描述")) {
                    notice.setDescription(value);
                }
            }

            // 检查必要字段是否为空
            if (notice.getTitle() == null || notice.getTitle().trim().isEmpty()) {
                logger.error("功能名称为空");
                return Result.error(ResultCodeEnum.PARAM_ERROR.toString(), "功能名称不能为空");
            }
            
            return Result.success(notice);
        } catch (IOException e) {
            logger.error("文件上传失败", e);
            return Result.error(ResultCodeEnum.SYSTEM_ERROR.toString(), "文件上传失败");
        } catch (Exception e) {
            logger.error("处理markdown文件失败", e);
            return Result.error(ResultCodeEnum.SYSTEM_ERROR.toString(), "处理文件失败");
        }
    }

    @PostMapping("/markdown/save")
    public Result saveMarkdown(@RequestBody NoticeMarkdown noticeMarkdown) {
        logger.info("开始保存markdown，标题：{}", noticeMarkdown.getTitle());
        try {
            // 检查必要字段是否为空
            if (noticeMarkdown.getTitle() == null || noticeMarkdown.getTitle().trim().isEmpty()) {
                logger.error("功能名称为空");
                return Result.error(ResultCodeEnum.PARAM_ERROR.toString(), "功能名称不能为空");
            }
            
            // 设置时间和用户
            noticeMarkdown.setTime(LocalDateTime.now().toString());
            noticeMarkdown.setUser(TokenUtils.getCurrentUser().getUsername());
            
            // 构建markdown内容
            StringBuilder markdownContent = new StringBuilder();
            markdownContent.append("## 标题\n").append(noticeMarkdown.getTitle()).append("\n\n");
            if (noticeMarkdown.getContent() != null) {
                markdownContent.append("## 内容\n").append(noticeMarkdown.getContent()).append("\n\n");
            }
            if (noticeMarkdown.getParameter() != null) {
                markdownContent.append("## 参数\n").append(noticeMarkdown.getParameter()).append("\n\n");
            }
            if (noticeMarkdown.getDescription() != null) {
                markdownContent.append("## 描述\n").append(noticeMarkdown.getDescription()).append("\n\n");
            }
            
            // 保存文件
            String username = TokenUtils.getCurrentUser().getUsername();
            String filePath = fileController.saveMarkdownFile(username, noticeMarkdown.getTitle(), markdownContent.toString());
            logger.info("文件保存成功，路径：{}", filePath);
            
            // 更新文件路径
            noticeMarkdown.setFilePath(filePath);
            
            // 保存到数据库
            noticeMarkdownService.insert(noticeMarkdown);
            logger.info("保存markdown成功，ID：{}，文件路径：{}", noticeMarkdown.getId(), noticeMarkdown.getFilePath());
            return Result.success(noticeMarkdown);
        } catch (Exception e) {
            logger.error("保存markdown失败，错误信息：{}", e.getMessage(), e);
            return Result.error(ResultCodeEnum.SYSTEM_ERROR.toString());
        }
    }
}