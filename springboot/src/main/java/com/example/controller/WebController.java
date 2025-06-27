package com.example.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.service.AdminService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 基础前端接口
 */
@RestController
@RequestMapping("/api")
public class WebController {

    @Resource
    private AdminService adminService;

    @GetMapping("/")
    public Result hello() {
        return Result.success("访问成功");
    }

    /**
     * 登录（根据用户名自动识别角色）
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        if (ObjectUtil.isEmpty(account.getUsername()) || ObjectUtil.isEmpty(account.getPassword())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        
        // 根据用户名查找用户，自动识别角色
        Admin dbUser = adminService.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            return Result.error(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        
        // 设置角色并进行登录验证
        account.setRole(dbUser.getRole());
        account = adminService.login(account);
        
        return Result.success(account);
    }

    /**
     * 注册（支持多角色）
     */
    @PostMapping("/register")
    public Result register(@RequestBody Account account) {
        if (StrUtil.isBlank(account.getUsername()) || StrUtil.isBlank(account.getPassword())
                || ObjectUtil.isEmpty(account.getRole())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        
        // 支持所有角色注册
        if (RoleEnum.ADMIN.name().equals(account.getRole()) || 
            RoleEnum.KaiFa.name().equals(account.getRole()) || 
            RoleEnum.CeShi.name().equals(account.getRole())) {
            adminService.register(account);
        } else {
            return Result.error(ResultCodeEnum.PARAM_ERROR.code, "不支持的角色类型");
        }
        
        return Result.success();
    }

}
