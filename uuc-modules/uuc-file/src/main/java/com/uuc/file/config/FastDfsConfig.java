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
@ConditionalOnProperty(prefix = "upload", name = "type", havingValue = "fdfs")
@ConfigurationProperties(prefix = "fdfs")
public class FastDfsConfig {

    private String domain;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
