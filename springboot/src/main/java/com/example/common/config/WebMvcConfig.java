package com.example.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/manager/**")  // 需要登录的路径
                .excludePathPatterns(
                        "/api/login",     // 登录接口
                        "/api/register",  // 注册接口
                        "/api/front/**",  // 前台所有接口
                        "/api/manager/files/**",  // 文件访问接口
                        "/**/*.html",     // html静态资源
                        "/**/*.js",       // js静态资源
                        "/**/*.css",      // css静态资源
                        "/**/*.woff",
                        "/**/*.ttf"
                );
    }
} 