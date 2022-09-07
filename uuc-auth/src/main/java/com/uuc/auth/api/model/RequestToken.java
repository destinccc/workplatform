package com.uuc.auth.api.model;

import lombok.Data;

/**
 * @author deng
 * @date 2022/7/13
 * @description 请求token的实体对象
 */
@Data
public class RequestToken {
    private String grantType;
    private String clientId;
    private String clientSecret;
    private String code;
    private String userName;
    private String password;
    private String refreshToken;
}
