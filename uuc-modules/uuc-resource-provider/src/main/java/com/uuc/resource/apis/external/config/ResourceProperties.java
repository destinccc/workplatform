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
        prefix = "resource.dept"
)
public class ResourceProperties {
    /**
     * 重庆卫健委对应的客户端Id
     */
    private String wClientId;



}
