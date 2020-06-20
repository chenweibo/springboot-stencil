package com.chen.stencil.config;

import com.chen.stencil.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.beans.factory.annotation.Value;


@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Value("${file.uploadFolder}")
    private String uploadFolder;

    @Value("${file.clientUrl}")
    private String clientUrl;

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截路径可自行配置多个 可用 ，分隔开
        registry.addInterceptor(jwtInterceptor()).addPathPatterns("/api/**");
    }


    /**
     * 处理在拦截器加载前注入bean解决redis工具类空指针
     */
    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }


    /**
     * 访问外部静态资源配置
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler((clientUrl))
                .addResourceLocations("file:" + uploadFolder);
    }

    /**
     * 跨域支持
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS", "HEAD")
                .maxAge(3600 * 24);
    }


}
