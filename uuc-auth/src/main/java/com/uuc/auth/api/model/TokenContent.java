package com.uuc.auth.api.model;

import lombok.Data;

/**
 * @author deng
 * @date 2022/7/13 0013
 * @description
 */
@Data
public class TokenContent {

    private String appId;

    private Long createTime;

    private Long expireTime;

    private Long expireIn;
}
