package com.uuc.auth.api.service;

import com.alibaba.fastjson.JSONObject;
import com.uuc.auth.api.constants.TokenConstants;
import com.uuc.auth.api.model.RequestToken;
import com.uuc.auth.api.model.TokenContent;
import com.uuc.auth.api.model.TokenResponse;
import com.uuc.common.core.constant.CacheConstants;
import com.uuc.common.redis.service.RedisService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author deng
 * @date 2022/7/13 0013
 * @description
 */
@Service
public class RedisTokenManager implements AccessTokenManager{

    @Autowired
    private RedisService redisService;
    @Value("${ump.token.timeout:7200}")
    private long timeout;

    @Override
    public String generate(RequestToken requestToken) {
        String accessToken =  UUID.randomUUID().toString().replaceAll("-", "");
        TokenContent tokenContent=new TokenContent();
        tokenContent.setAppId(requestToken.getClientId());
        tokenContent.setCreateTime(System.currentTimeMillis()/1000);
        tokenContent.setExpireIn(timeout);
        tokenContent.setExpireTime(System.currentTimeMillis()/1000+timeout);
        redisService.setCacheObject(CacheConstants.CLIENT_TOKEN +accessToken,tokenContent,timeout, TimeUnit.SECONDS);
        return accessToken;
    }

    @Override
    public TokenContent get(String token) {
        Object obj=redisService.getCacheObject(TokenConstants.CLIENT_TOKEN +token);
        if(obj!=null){
            return (TokenContent) obj;
        }
        return null;
    }

    @Override
    public void remove(String token) {
        redisService.deleteObject(token);
    }

    @Override
    public boolean refresh(String token) {
        if(redisService.getCacheObject(CacheConstants.CLIENT_TOKEN +token)==null){
            return false;
        }
        TokenContent tokenContent = get(token);
        tokenContent.setExpireTime(System.currentTimeMillis()/1000+timeout);
        redisService.setCacheObject(CacheConstants.CLIENT_TOKEN +token,tokenContent,timeout,TimeUnit.SECONDS);
        return true;
    }
    @Override
    public long getTimeout(){
        return timeout;
    }
}
