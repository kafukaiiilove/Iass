package com.example.service.impl;

import com.example.entity.NoticeMarkdown;
import com.example.mapper.NoticeMarkdownMapper;
import com.example.service.NoticeMarkdownService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoticeMarkdownServiceImpl implements NoticeMarkdownService {
    private static final Logger logger = LoggerFactory.getLogger(NoticeMarkdownServiceImpl.class);

    @Autowired
    private NoticeMarkdownMapper noticeMarkdownMapper;

    @Override
    @Cacheable(value = "notices", key = "'all'")
    public List<NoticeMarkdown> findAll() {
        logger.info("开始查询所有公告");
        try {
            List<NoticeMarkdown> notices = noticeMarkdownMapper.findAll();
            logger.info("查询到{}条公告数据", notices.size());
            return notices;
        } catch (Exception e) {
            logger.error("查询所有公告失败", e);
            throw new RuntimeException("查询公告失败", e);
        }
    }

    @Override
    @Cacheable(value = "notice", key = "#id")
    public NoticeMarkdown findById(Integer id) {
        logger.info("开始查询公告，ID：{}", id);
        try {
            NoticeMarkdown notice = noticeMarkdownMapper.findById(id);
            if (notice == null) {
                logger.warn("未找到ID为{}的公告", id);
                throw new RuntimeException("公告不存在");
            }
            logger.info("成功查询到公告，ID：{}", id);
            return notice;
        } catch (Exception e) {
            logger.error("查询公告失败，ID：{}", id, e);
            throw new RuntimeException("查询公告失败", e);
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"notices", "notice"}, allEntries = true)
    public void insert(NoticeMarkdown notice) {
        logger.info("开始新增公告，标题：{}", notice.getTitle());
        try {
            // 检查必要字段
            if (notice.getTitle() == null || notice.getTitle().trim().isEmpty()) {
                throw new IllegalArgumentException("公告标题不能为空");
            }
            if (notice.getFilePath() == null || notice.getFilePath().trim().isEmpty()) {
                throw new IllegalArgumentException("文件路径不能为空");
            }
            
            // 插入数据
            noticeMarkdownMapper.insert(notice);
            logger.info("新增公告成功，ID：{}", notice.getId());
        } catch (Exception e) {
            logger.error("新增公告失败，错误信息：{}", e.getMessage(), e);
            throw new RuntimeException("新增公告失败：" + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"notices", "notice"}, allEntries = true)
    public void update(NoticeMarkdown notice) {
        logger.info("开始更新公告，ID：{}", notice.getId());
        try {
            NoticeMarkdown existingNotice = noticeMarkdownMapper.findById(notice.getId());
            if (existingNotice == null) {
                logger.warn("更新失败，未找到ID为{}的公告", notice.getId());
                throw new RuntimeException("公告不存在");
            }
            if (notice.getTitle() == null || notice.getTitle().trim().isEmpty()) {
                throw new IllegalArgumentException("公告标题不能为空");
            }
            noticeMarkdownMapper.update(notice);
            logger.info("更新公告成功，ID：{}", notice.getId());
        } catch (Exception e) {
            logger.error("更新公告失败，ID：{}", notice.getId(), e);
            throw new RuntimeException("更新公告失败", e);
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"notices", "notice"}, allEntries = true)
    public void deleteById(Integer id) {
        logger.info("开始删除公告，ID：{}", id);
        try {
            NoticeMarkdown existingNotice = noticeMarkdownMapper.findById(id);
            if (existingNotice == null) {
                logger.warn("删除失败，未找到ID为{}的公告", id);
                throw new RuntimeException("公告不存在");
            }
            noticeMarkdownMapper.deleteById(id);
            logger.info("删除公告成功，ID：{}", id);
        } catch (Exception e) {
            logger.error("删除公告失败，ID：{}", id, e);
            throw new RuntimeException("删除公告失败", e);
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"notices", "notice"}, allEntries = true)
    public void deleteBatch(List<Integer> ids) {
        logger.info("开始批量删除公告，IDs：{}", ids);
        try {
            for (Integer id : ids) {
                deleteById(id);
            }
            logger.info("批量删除公告成功，IDs：{}", ids);
        } catch (Exception e) {
            logger.error("批量删除公告失败，IDs：{}", ids, e);
            throw new RuntimeException("批量删除公告失败", e);
        }
    }
} 