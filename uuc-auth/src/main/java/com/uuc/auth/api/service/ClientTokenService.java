package com.uuc.auth.api.service;
import com.uuc.auth.api.enums.GrantTypeEnum;
import com.uuc.auth.api.model.RequestToken;
import com.uuc.auth.api.model.TokenResponse;
import com.uuc.common.core.constant.HttpStatus;
import com.uuc.common.core.exception.ServiceException;
import com.uuc.common.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author deng
 * @date 2022/7/13 0013
 * @description 客户端模式实现类
 */
@Service("client_credential")
public class ClientTokenService implements TokenBuilder{
    @Autowired
    private AccessTokenManager accessTokenManager;
    @Autowired
    private RefreshTokenManager refreshTokenManager;

    @Override
    public TokenResponse createToken(RequestToken requestToken) {
        checkParameter(requestToken);
        String accessToken = accessTokenManager.generate(requestToken);
        //生成refreshToken
        TokenResponse tokenResponse = refreshTokenManager.generateRefreshToken(accessToken);
        return tokenResponse;
    }


    @Override
    public void checkParameter(RequestToken requestToken) {
        System.out.println("requestToken的值:"+requestToken.getGrantType()+"==========grantType的值:"+GrantTypeEnum.CLIENT_CREDENTIAL.getValue());
        if(!requestToken.getGrantType().equals(GrantTypeEnum.CLIENT_CREDENTIAL.getValue())){
            throw new ServiceException("授权类型错误",HttpStatus.BAD_REQUEST);
        }
        String clientId=requestToken.getClientId();
        String clientSecret=requestToken.getClientSecret();
        if(StringUtils.isEmpty(clientId)||StringUtils.isEmpty(clientSecret)){
            throw new ServiceException("缺少客户端参数",HttpStatus.BAD_REQUEST);
        }
    }

}
