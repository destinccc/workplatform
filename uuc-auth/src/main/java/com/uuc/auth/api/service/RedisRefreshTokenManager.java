package com.uuc.auth.api.service;

import com.uuc.auth.api.constants.TokenConstants;
import com.uuc.auth.api.model.RefreshTokenContent;
import com.uuc.auth.api.model.RequestToken;
import com.uuc.auth.api.model.TokenContent;
import com.uuc.auth.api.model.TokenResponse;
import com.uuc.common.core.constant.CacheConstants;
import com.uuc.common.redis.service.RedisService;
import org.apache.poi.ss.formula.functions.T;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author deng
 * @date 2022/7/13 0013
 * @description
 */
@Component
public class RedisRefreshTokenManager implements RefreshTokenManager{

    @Autowired
    private RedisService redisService;
    @Autowired
    private AccessTokenManager accessTokenManager;
    @Value("${ump.refreshToken.timeout:21600}")
    private long timeout;

    @Override
    public TokenResponse generateRefreshToken(String token) {
        String refreshToken = UUID.randomUUID().toString().replaceAll("-", "");
        RefreshTokenContent refreshTokenContent=new RefreshTokenContent();
        refreshTokenContent.setAccessToken(token);
        refreshTokenContent.setCreateTime(System.currentTimeMillis()/1000);
        refreshTokenContent.setExpireIn(timeout);
        refreshTokenContent.setExpireTime(System.currentTimeMillis()/1000+timeout);
        redisService.setCacheObject(CacheConstants.CLIENT_REFRESH_TOKEN + refreshToken,refreshTokenContent,timeout, TimeUnit.SECONDS);
        TokenResponse tokenResponse=new TokenResponse();
        tokenResponse.setAccess_token(token);
        tokenResponse.setRefresh_token(token);
        tokenResponse.setRefresh_token(refreshToken);
        tokenResponse.setExpires_in(accessTokenManager.getTimeout());
        tokenResponse.setExpires_time(System.currentTimeMillis()/1000+accessTokenManager.getTimeout());
        return tokenResponse;
    }


    @Override
    public void remove(String refreshToken) {
        redisService.deleteObject(refreshToken);
    }

    @Override
    public RefreshTokenContent validate(String refreshToken) {
        Object cacheObject = redisService.getCacheObject(CacheConstants.CLIENT_REFRESH_TOKEN + refreshToken);
        RefreshTokenContent refreshTokenContent=null;
        if(cacheObject!=null){
            redisService.deleteObject(CacheConstants.CLIENT_REFRESH_TOKEN + refreshToken);
            refreshTokenContent=(RefreshTokenContent) cacheObject;
        }
        return refreshTokenContent;
    }
}
