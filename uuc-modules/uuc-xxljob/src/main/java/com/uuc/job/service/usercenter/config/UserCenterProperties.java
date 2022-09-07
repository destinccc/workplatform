package com.uuc.job.service.usercenter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: fxm
 * @date: 2022-08-10
 * @description: 用户中心权限检查属性配置
 **/
@Data
@Component
@ConfigurationProperties(prefix = "gdmars.scusercenter.service.chkperm")
public class UserCenterProperties {

    /**
     * 统一管理平台在统一用户中心申请的应用密钥
     */
    private String appSecret;
    /**
     * 统一管理平台在统一用户中心申请的应用appId
     */
    private String appId;
}
