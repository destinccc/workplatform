package com.uuc.auth.api.service;

import com.uuc.auth.api.model.RefreshTokenContent;
import com.uuc.auth.api.model.RequestToken;
import com.uuc.auth.api.model.TokenContent;
import com.uuc.auth.api.model.TokenResponse;

/**
 * @author deng
 * @date 2022/7/13 0013
 * @description token操作相关接口类
 */
public interface RefreshTokenManager {

    TokenResponse generateRefreshToken(String token);



    void remove(String refreshToken);

    RefreshTokenContent validate(String refreshToken);
}
