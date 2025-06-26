package com.example.common.config;

import cn.hutool.core.util.ObjectUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.exception.CustomException;
import com.example.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt拦截器
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);

    @Resource
    private AdminService adminService;

    // 白名单路径
    private static final String[] WHITE_LIST = {
        "/api/login",
        "/api/register",
        "/api/front/**"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取当前请求路径
        String requestURI = request.getRequestURI();
        
        // 检查是否是白名单路径
        for (String path : WHITE_LIST) {
            if (path.endsWith("/**")) {
                String basePath = path.substring(0, path.length() - 3);
                if (requestURI.startsWith(basePath)) {
                    return true;
                }
            } else if (requestURI.equals(path)) {
                return true;
            }
        }

        // 1. 从http请求的header中获取token
        String token = request.getHeader(Constants.TOKEN);
        if (ObjectUtil.isEmpty(token)) {
            // 如果没拿到，从参数里再拿一次
            token = request.getParameter(Constants.TOKEN);
        }

        // 2. 判断token是否为空
        if (ObjectUtil.isEmpty(token)) {
            log.warn("请求未携带token");
            throw new CustomException(ResultCodeEnum.TOKEN_INVALID_ERROR);
        }

        Account account = null;
        try {
            // 3. 解析token获取存储的数据
            String userRole = JWT.decode(token).getAudience().get(0);
            String userId = userRole.split("-")[0];
            String role = userRole.split("-")[1];

            // 根据userId查询数据库，获取用户信息
            if (RoleEnum.ADMIN.name().equals(role)) {
                account = adminService.selectById(Integer.valueOf(userId));
            }
        } catch (Exception e) {
            log.error("解析token时出错", e);
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }

        // 4. 用户不存在，返回错误
        if (ObjectUtil.isNull(account)) {
            log.warn("用户不存在，userId: {}", request.getHeader(Constants.TOKEN));
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }

        try {
            // 5. 用户密码加签验证 token
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(account.getPassword())).build();
            jwtVerifier.verify(token); // 验证token
        } catch (JWTVerificationException e) {
            log.error("token验证失败, token: {}", token, e);
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }

        // 6. 所有验证通过，返回true，放行请求
        log.info("token验证通过，用户: {}", account.getUsername());
        return true;
    }
}
