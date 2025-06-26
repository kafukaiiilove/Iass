package com.example.service;

import com.example.entity.Account;
import com.example.entity.A14;
import com.example.mapper.A14Mapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Android 14系统信息表处理
 **/
@Service
public class A14Service {

    @Resource
    private A14Mapper a14Mapper;

    /**
     * 新增
     */
    public void add(A14 a14) {
        // 设置时间和用户
        a14.setTime(LocalDateTime.now());
        Account currentUser = TokenUtils.getCurrentUser();
        a14.setUser(currentUser.getUsername());
        
        // 获取当前最大ID
        Integer maxId = a14Mapper.getMaxId();
        if (maxId == null) {
            // 如果表中没有数据，设置ID为1
            a14.setId(1);
        } else {
            // 设置ID为最大ID+1
            a14.setId(maxId + 1);
        }
        
        // 插入数据
        a14Mapper.insert(a14);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        a14Mapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            a14Mapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(A14 a14) {
        a14Mapper.updateById(a14);
    }

    /**
     * 根据ID查询
     */
    public A14 selectById(Integer id) {
        return a14Mapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<A14> selectAll(A14 a14) {
        return a14Mapper.selectAll(a14);
    }

    /**
     * 分页查询
     */
    public PageInfo<A14> selectPage(A14 a14, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<A14> list = a14Mapper.selectAll(a14);
        return PageInfo.of(list);
    }

    public void insert(A14 a14) {
        add(a14);
    }

    public void update(A14 a14) {
        updateById(a14);
    }

    public List<A14> findAll() {
        return selectAll(null);
    }
} 