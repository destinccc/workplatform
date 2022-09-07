package com.uuc.auth.api.model;

import lombok.Data;

/**
 * @author deng
 * @date 2022/7/13 0013
 * @description
 */
@Data
public class TokenResponse {
    private String access_token;
    private Long expires_in;
    private Long expires_time;
    private String refresh_token;
}
