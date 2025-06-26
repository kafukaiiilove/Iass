package com.example.service;

import com.example.entity.NoticeMarkdown;
import java.util.List;

/**
 * 公告信息表业务处理
 **/
public interface NoticeMarkdownService {
    /**
     * 查询所有
     */
    List<NoticeMarkdown> findAll();

    /**
     * 根据ID查询
     */
    NoticeMarkdown findById(Integer id);

    /**
     * 新增
     */
    void insert(NoticeMarkdown notice);

    /**
     * 修改
     */
    void update(NoticeMarkdown notice);

    /**
     * 删除
     */
    void deleteById(Integer id);

    /**
     * 批量删除
     */
    void deleteBatch(List<Integer> ids);
} 