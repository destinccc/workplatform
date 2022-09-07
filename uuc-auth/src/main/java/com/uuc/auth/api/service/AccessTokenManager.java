package com.uuc.auth.api.service;

import com.uuc.auth.api.model.RequestToken;
import com.uuc.auth.api.model.TokenContent;
import com.uuc.auth.api.model.TokenResponse;

/**
 * @author deng
 * @date 2022/7/13 0013
 * @description token操作相关接口类
 */
public interface AccessTokenManager {

    String generate(RequestToken requestToken);


    TokenContent get(String token);

    void remove(String token);

    boolean refresh(String accessToken);

    long getTimeout();
}
