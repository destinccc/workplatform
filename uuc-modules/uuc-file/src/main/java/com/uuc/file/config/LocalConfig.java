package com.uuc.file.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author: fxm
 * @date: 2022-05-31
 * @description:
 **/
@Configuration
@ConditionalOnProperty(prefix = "upload", name = "type", havingValue = "file")
@ConfigurationProperties(prefix = "file")
public class LocalConfig {
    /**
     * 上传文件存储在本地的根路径
     */
    private String path;

    /**
     * 资源映射路径 前缀
     */
    public String prefix;

    public String domain;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
