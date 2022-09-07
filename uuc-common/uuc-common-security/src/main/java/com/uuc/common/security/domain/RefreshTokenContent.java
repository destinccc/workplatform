package com.uuc.common.security.domain;

import com.uuc.system.api.model.LoginUser;

import java.io.Serializable;

public class RefreshTokenContent implements Serializable {
    private String accessToken;
    private LoginUser loginUser;
    private String code;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public LoginUser getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(LoginUser loginUser) {
        this.loginUser = loginUser;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
