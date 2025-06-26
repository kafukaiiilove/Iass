package com.example.service;

import com.example.entity.Account;
import com.example.entity.A15;
import com.example.mapper.A15Mapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Android 15系统信息表处理
 **/
@Service
public class A15Service {

    @Resource
    private A15Mapper a15Mapper;

    /**
     * 新增
     */
    public void add(A15 a15) {
        // 设置时间和用户
        a15.setTime(LocalDateTime.now());
        Account currentUser = TokenUtils.getCurrentUser();
        a15.setUser(currentUser.getUsername());
        
        // 获取当前最大ID
        Integer maxId = a15Mapper.getMaxId();
        if (maxId == null) {
            // 如果表中没有数据，设置ID为1
            a15.setId(1);
        } else {
            // 设置ID为最大ID+1
            a15.setId(maxId + 1);
        }
        
        // 插入数据
        a15Mapper.insert(a15);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        a15Mapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            a15Mapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(A15 a15) {
        a15Mapper.updateById(a15);
    }

    /**
     * 根据ID查询
     */
    public A15 selectById(Integer id) {
        return a15Mapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<A15> selectAll(A15 a15) {
        return a15Mapper.selectAll(a15);
    }

    /**
     * 分页查询
     */
    public PageInfo<A15> selectPage(A15 a15, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<A15> list = a15Mapper.selectAll(a15);
        return PageInfo.of(list);
    }

    public void insert(A15 a15) {
        add(a15);
    }

    public void update(A15 a15) {
        updateById(a15);
    }

    public List<A15> findAll() {
        return selectAll(null);
    }
} 