package com.example.service;

import com.example.entity.Account;
import com.example.entity.A10;
import com.example.mapper.A10Mapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Android 10系统信息表处理
 **/
@Service
public class A10Service {

    @Resource
    private A10Mapper a10Mapper;

    /**
     * 新增
     */
    public void add(A10 a10) {
        // 设置时间和用户
        a10.setTime(LocalDateTime.now());
        Account currentUser = TokenUtils.getCurrentUser();
        a10.setUser(currentUser.getUsername());
        
        // 获取当前最大ID
        Integer maxId = a10Mapper.getMaxId();
        if (maxId == null) {
            // 如果表中没有数据，设置ID为1
            a10.setId(1);
        } else {
            // 设置ID为最大ID+1
            a10.setId(maxId + 1);
        }
        
        // 插入数据
        a10Mapper.insert(a10);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        a10Mapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            a10Mapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(A10 a10) {
        a10Mapper.updateById(a10);
    }

    /**
     * 根据ID查询
     */
    public A10 selectById(Integer id) {
        return a10Mapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<A10> selectAll(A10 a10) {
        return a10Mapper.selectAll(a10);
    }

    /**
     * 分页查询
     */
    public PageInfo<A10> selectPage(A10 a10, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<A10> list = a10Mapper.selectAll(a10);
        return PageInfo.of(list);
    }

    public void insert(A10 a10) {
        add(a10);
    }

    public void update(A10 a10) {
        updateById(a10);
    }

    public List<A10> findAll() {
        return selectAll(null);
    }
} 