package com.uuc.auth.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 获取统一登录url
 *
 * @author uuc
 */
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "sso")
public class SsoLoginProperties {
    private String loginUrl;

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }
}
