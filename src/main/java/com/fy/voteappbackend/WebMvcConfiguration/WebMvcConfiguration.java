package com.fy.voteappbackend.WebMvcConfiguration;

import com.fy.voteappbackend.Tools.PictureTools;
import com.fy.voteappbackend.interceptor.JwtTokenAdminInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configurable
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;


    //TODO 项目完成后删除test路径
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("JWT拦截器启用");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/**") // 拦截所有路径
                .excludePathPatterns("/admins/**")          //排除/admins/**
                .excludePathPatterns("/user/login")        //排除/login
                .excludePathPatterns("/user/register")     //排除/register
                .excludePathPatterns("/admins/test");

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("图片拦截器启动");
        registry.addResourceHandler("/**")
                .addResourceLocations("file:./PhotoUpload/");
    }

}
