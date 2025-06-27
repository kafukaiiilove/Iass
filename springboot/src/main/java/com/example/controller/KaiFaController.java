package com.example.controller;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Result;
import com.example.common.enums.RoleEnum;
import com.example.entity.Admin;
import com.example.entity.Account;
import com.example.service.AdminService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 开发工程师角色专用接口
 **/
@RestController
@RequestMapping("/api/manager/kaifa")
public class KaiFaController {

    @Resource
    private AdminService adminService;

    /**
     * 查询所有开发工程师
     */
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Admin> list = adminService.selectByRole(RoleEnum.KaiFa.name());
        return Result.success(list);
    }

    /**
     * 分页查询开发工程师
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Admin> page = adminService.selectPageByRole(RoleEnum.KaiFa.name(), pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 根据ID查询开发工程师
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Admin admin = adminService.selectById(id);
        if (admin != null && RoleEnum.KaiFa.name().equals(admin.getRole())) {
            return Result.success(admin);
        }
        return Result.error("404", "开发工程师不存在");
    }

    /**
     * 修改开发工程师信息
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Admin admin) {
        // 确保角色不被修改
        admin.setRole(RoleEnum.KaiFa.name());
        adminService.updateById(admin);
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        try {
            // 确保用户名不为空
            if (ObjectUtil.isEmpty(account.getUsername())) {
                return Result.error("400", "用户名不能为空");
            }
            
            // 验证用户角色
            Admin dbUser = adminService.selectByUsername(account.getUsername());
            if (dbUser == null || !RoleEnum.KaiFa.name().equals(dbUser.getRole())) {
                return Result.error("403", "无权限修改此用户密码");
            }
            
            adminService.updatePassword(account);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("500", "修改密码失败：" + e.getMessage());
        }
    }
} 