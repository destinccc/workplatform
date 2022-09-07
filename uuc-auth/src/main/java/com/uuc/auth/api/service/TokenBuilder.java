package com.uuc.auth.api.service;

import com.uuc.auth.api.model.RequestToken;
import com.uuc.auth.api.model.TokenResponse;

/**
 * @author deng
 * @date 2022/7/13 0013
 * @description
 */
public interface TokenBuilder {


    TokenResponse createToken(RequestToken requestToken);

    void checkParameter(RequestToken requestToken);
}
