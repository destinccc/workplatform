package com.uuc.auth.service;

import com.uuc.common.core.constant.CacheConstants;
import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.utils.JwtUtils;
import com.uuc.common.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class ServerTokenService {
    protected static final long HOURS = 3600;
    private final static long expireTime = CacheConstants.EXPIRATION * HOURS;
    private final static String SERVER_TOKEN = CacheConstants.CLIENT_TOKEN;
    @Autowired
    private RedisService redisService;

    public Map<String, Object> createServerToken(String clientId){
        Map<String, Object> claimsMap = new HashMap<String, Object>();
        claimsMap.put(SecurityConstants.CLIENT_ID, clientId);
        claimsMap.put(SecurityConstants.SERVER_MARK, SecurityConstants.SERVER_MARK_KEY);
        String serverToken = JwtUtils.createToken(claimsMap);
        redisService.setCacheObject(getServerTokenKey(serverToken), clientId, expireTime, TimeUnit.SECONDS);
        long expireTimeValue = System.currentTimeMillis()/1000+expireTime;
        Map<String, Object> rspMap = new HashMap<String, Object>();
        rspMap.put("access_token", serverToken);
        rspMap.put("expires_in", expireTime);
        rspMap.put("expires_time",expireTimeValue);
        return rspMap;
    }
    public Map<String, Object> createToken(String clientId){
        String serverToken=UUID.randomUUID().toString().replaceAll("-", "");
        redisService.setCacheObject(getServerTokenKey(serverToken), clientId, expireTime, TimeUnit.SECONDS);
        long expireTimeValue = System.currentTimeMillis()/1000+expireTime;
        Map<String, Object> rspMap = new HashMap<String, Object>();
        rspMap.put("access_token", serverToken);
        rspMap.put("expires_in", expireTime);
        rspMap.put("expires_time",expireTimeValue);
        return rspMap;
    }



    private String getServerTokenKey(String token) {
        return SERVER_TOKEN + token;
    }
}
