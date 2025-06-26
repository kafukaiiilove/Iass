package com.example.mapper;

import com.example.entity.NoticeMarkdown;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 公告信息表数据库操作
 **/
@Mapper
public interface NoticeMarkdownMapper {

    /**
     * 新增
     */
    void insert(NoticeMarkdown notice);

    /**
     * 删除
     */
    void deleteById(Integer id);

    /**
     * 批量删除
     */
    void deleteBatch(List<Integer> ids);

    /**
     * 修改
     */
    void update(NoticeMarkdown notice);

    /**
     * 根据ID查询
     */
    NoticeMarkdown findById(Integer id);

    /**
     * 查询所有
     */
    List<NoticeMarkdown> findAll();
} 