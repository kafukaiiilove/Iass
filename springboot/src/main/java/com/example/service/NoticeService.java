package com.example.service;

import com.example.entity.Account;
import com.example.entity.Notice;
import com.example.mapper.NoticeMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

/**
 * 公告信息表业务处理
 **/
@Service
public class NoticeService {

    @Resource
    private NoticeMapper noticeMapper;

    /**
     * 新增
     */
    public void add(Notice notice) {
        // 设置时间和用户
        notice.setTime(LocalDateTime.now());
        Account currentUser = TokenUtils.getCurrentUser();
        notice.setUser(currentUser.getUsername());
        
        // 获取当前最大ID
        Integer maxId = noticeMapper.getMaxId();
        if (maxId == null) {
            // 如果表中没有数据，设置ID为1
            notice.setId(1);
        } else {
            // 设置ID为最大ID+1
            notice.setId(maxId + 1);
        }
        
        // 插入数据
        noticeMapper.insert(notice);
        

    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        noticeMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            noticeMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Notice notice) {
        noticeMapper.updateById(notice);
    }

    /**
     * 根据ID查询
     */
    public Notice selectById(Integer id) {
        return noticeMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Notice> selectAll(Notice notice) {
        return noticeMapper.selectAll(notice);
    }

    /**
     * 分页查询
     */
    public PageInfo<Notice> selectPage(Notice notice, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Notice> list = noticeMapper.selectAll(notice);
        return PageInfo.of(list);
    }

    public void insert(Notice notice) {
        add(notice);
    }

    public void update(Notice notice) {
        updateById(notice);
    }

    public List<Notice> findAll() {
        return selectAll(null);
    }
}