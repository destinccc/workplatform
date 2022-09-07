package com.uuc.auth.api.model;

import lombok.Data;

/**
 * @author deng
 * @date 2022/7/13 0013
 * @description
 */
@Data
public class RefreshTokenContent {

    private String accessToken;

    private Long createTime;

    private Long expireIn;

    private Long expireTime;
}
