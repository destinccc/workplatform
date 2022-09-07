package com.uuc.auth.api.service;

import com.uuc.auth.api.enums.GrantTypeEnum;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author deng
 * @date 2022/7/13 0013
 * @description
 */

public class TokenFactory {
    private static TokenFactory tokenFactory=new TokenFactory();

    private static final Map<String,TokenBuilder> tokenMap=new HashMap<>();

    //初始化注入不同授权类型token处理类
    /*static{
        tokenMap.put(GrantTypeEnum.CLIENT_CREDENTIAL.getValue(), new ClientTokenService());
    }*/
    public static TokenFactory getInstance(){
        return tokenFactory;
    }

    public TokenBuilder getTokenBuild(String grantType){
        return tokenMap.get(grantType);
    }
}
