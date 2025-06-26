package com.example.service;

import com.example.entity.Account;
import com.example.entity.A13;
import com.example.mapper.A13Mapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Android 13系统信息表处理
 **/
@Service
public class A13Service {

    @Resource
    private A13Mapper a13Mapper;

    /**
     * 新增
     */
    public void add(A13 a13) {
        // 设置时间和用户
        a13.setTime(LocalDateTime.now());
        Account currentUser = TokenUtils.getCurrentUser();
        a13.setUser(currentUser.getUsername());
        
        // 获取当前最大ID
        Integer maxId = a13Mapper.getMaxId();
        if (maxId == null) {
            // 如果表中没有数据，设置ID为1
            a13.setId(1);
        } else {
            // 设置ID为最大ID+1
            a13.setId(maxId + 1);
        }
        
        // 插入数据
        a13Mapper.insert(a13);
        

    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        a13Mapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            a13Mapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(A13 a13) {
        a13Mapper.updateById(a13);
    }

    /**
     * 根据ID查询
     */
    public A13 selectById(Integer id) {
        return a13Mapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<A13> selectAll(A13 a13) {
        return a13Mapper.selectAll(a13);
    }

    /**
     * 分页查询
     */
    public PageInfo<A13> selectPage(A13 a13, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<A13> list = a13Mapper.selectAll(a13);
        return PageInfo.of(list);
    }

    public void insert(A13 a13) {
        add(a13);
    }

    public void update(A13 a13) {
        updateById(a13);
    }

    public List<A13> findAll() {
        return selectAll(null);
    }
}