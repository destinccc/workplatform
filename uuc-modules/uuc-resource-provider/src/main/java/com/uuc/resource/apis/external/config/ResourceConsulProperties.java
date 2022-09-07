package com.uuc.resource.apis.external.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author: fxm
 * @date: 2022-06-20
 * @description:
 **/
@Data
@Configuration
@ConfigurationProperties(
        prefix = "resource.consul"
)
public class ResourceConsulProperties {
    /**
     * consul的地址
     */
    private String url;
    /**
     * consul注册的service
     */
    private String serviceName;



}
