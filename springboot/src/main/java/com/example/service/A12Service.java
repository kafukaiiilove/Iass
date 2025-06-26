package com.example.service;

import com.example.entity.Account;
import com.example.entity.A12;
import com.example.mapper.A12Mapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Android 12系统信息表处理
 **/
@Service
public class A12Service {

    @Resource
    private A12Mapper a12Mapper;

    /**
     * 新增
     */
    public void add(A12 a12) {
        // 设置时间和用户
        a12.setTime(LocalDateTime.now());
        Account currentUser = TokenUtils.getCurrentUser();
        a12.setUser(currentUser.getUsername());
        
        // 获取当前最大ID
        Integer maxId = a12Mapper.getMaxId();
        if (maxId == null) {
            // 如果表中没有数据，设置ID为1
            a12.setId(1);
        } else {
            // 设置ID为最大ID+1
            a12.setId(maxId + 1);
        }
        
        // 插入数据
        a12Mapper.insert(a12);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        a12Mapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            a12Mapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(A12 a12) {
        a12Mapper.updateById(a12);
    }

    /**
     * 根据ID查询
     */
    public A12 selectById(Integer id) {
        return a12Mapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<A12> selectAll(A12 a12) {
        return a12Mapper.selectAll(a12);
    }

    /**
     * 分页查询
     */
    public PageInfo<A12> selectPage(A12 a12, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<A12> list = a12Mapper.selectAll(a12);
        return PageInfo.of(list);
    }

    public void insert(A12 a12) {
        add(a12);
    }

    public void update(A12 a12) {
        updateById(a12);
    }

    public List<A12> findAll() {
        return selectAll(null);
    }
} 