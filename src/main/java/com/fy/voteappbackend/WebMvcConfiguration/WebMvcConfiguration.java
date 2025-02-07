package com.fy.voteappbackend.WebMvcConfiguration;

import com.fy.voteappbackend.interceptor.JwtTokenAdminInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configurable
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("拦截器启用");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/**") // 拦截所有路径
                .excludePathPatterns("/admins/login")   //排除/admins/login
                .excludePathPatterns("/user/**")
                .excludePathPatterns("/admins/test");          //排除/user
    }

}
