package com.uuc.file.config;

import java.io.File;

import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 通用映射配置
 * 
 * @author uuc
 */
@Configuration
@ConditionalOnProperty(prefix = "upload", name = "type", havingValue = "file")
public class ResourcesConfig implements WebMvcConfigurer
{
    @Autowired(required = false)
    private LocalConfig localConfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        /** 本地文件上传路径 */
        registry.addResourceHandler(localConfig.getPrefix() + "/**")
                .addResourceLocations("file:" + localConfig.getPath() + File.separator);
    }
    
    /**
     * 开启跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路由
        registry.addMapping(localConfig.getPrefix()  + "/**")
                // 设置允许跨域请求的域名
                .allowedOrigins("*")
                // 设置允许的方法
                .allowedMethods("GET");
    }
}