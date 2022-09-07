package com.uuc.auth.api.controller;

import com.alibaba.fastjson.JSON;
import com.uuc.auth.api.enums.GrantTypeEnum;
import com.uuc.auth.api.model.RefreshTokenContent;
import com.uuc.auth.api.model.RequestToken;
import com.uuc.auth.api.model.TokenResponse;
import com.uuc.auth.api.service.*;
import com.uuc.auth.form.RequestEntity;
import com.uuc.auth.utils.GrantTypeUtil;
import com.uuc.auth.utils.RSAEncrypt;
import com.uuc.common.core.constant.HttpStatus;
import com.uuc.common.core.domain.R;
import com.uuc.common.core.exception.ServiceException;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.core.utils.ip.IpUtils;
import com.uuc.system.api.domain.UucModelInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author deng
 * @date 2022/7/13
 * @description 用于对外提供token
 */
@RestController
@RequestMapping("/v1")
public class TokenServerController {

    private static final Logger log = LoggerFactory.getLogger(TokenServerController.class);

    @Autowired
    private TokenApiService tokenApiService;
    @Autowired
    private RefreshTokenManager refreshTokenManager;
    @Autowired
    private AccessTokenManager accessTokenManager;
    @Resource
    private Map<String,TokenBuilder> tokenBuilderMap;


    /**
     * 获取token接口，客户端参数通过RSA公钥加密传入，需要解密，返回数据公钥加密
     * @param requestEntity
     * @param request
     * @return
     */
    @PostMapping("/token")
    public R getToken(@RequestBody RequestEntity requestEntity, HttpServletRequest request){

        try {
            log.info("进入获取token流程,请求Ip为{}==============================", IpUtils.getIpAddr(request));
            verifyRequest(requestEntity,request);//校验请求参数是否正确
            UucModelInfo uucModelInfo = checkClient(tokenApiService.getClientId(request));//远程调用查询客户端数据
            RequestToken requestToken = decryPrivate(uucModelInfo, requestEntity);//解密请求参数
            boolean checkTypeFlag = GrantTypeUtil.checkType(requestToken.getGrantType()); //校验授权类型字段是否正确
            if (!checkTypeFlag){
                return R.fail("不支持的授权类型");
            }
            //根据不同grant_type生成token
            TokenResponse tokenResponse = tokenBuilderMap.get(requestToken.getGrantType()).createToken(requestToken);
            log.info("完成获取token流程==============================");
            String encryptPublic =encryptPublic(uucModelInfo,tokenResponse);//RSA公钥加密返回
            return R.ok(encryptPublic);
        }catch (ServiceException e){
            log.error("执行获取token出错，错误信息为:"+e.getMessage());
            return R.fail(e.getCode(),e.getMessage());
        }
        catch (Exception e){
            log.error("获取token失败============================,错误信息:{}",e.getMessage());
            return R.fail("获取token失败");
        }
    }

    /**
     * 刷新token接口，客户端参数通过RSA公钥加密传入，需要解密，返回数据公钥加密
     * @param requestEntity
     * @param request
     * @return
     */
    @PostMapping("/refreshToken")
    public R refreshToken(@RequestBody RequestEntity requestEntity, HttpServletRequest request){
        try {
            log.info("进入refreshToken流程,请求Ip为{}==============================", IpUtils.getIpAddr(request));
            verifyRequest(requestEntity,request);
            UucModelInfo uucModelInfo = checkClient(tokenApiService.getClientId(request));
            RequestToken requestToken = decryPrivate(uucModelInfo, requestEntity);
            String refreshToken=requestToken.getRefreshToken();
            if (StringUtils.isEmpty(refreshToken)){
                return R.fail(HttpStatus.BAD_REQUEST,"请求参数异常");
            }
            RefreshTokenContent refreshTokenContent = refreshTokenManager.validate(requestToken.getRefreshToken());//验证refreshToken
            if(refreshTokenContent==null||StringUtils.isEmpty(refreshTokenContent.getAccessToken())){
                return R.fail(HttpStatus.BAD_REQUEST,"参数异常");
            }
            accessTokenManager.refresh(refreshTokenContent.getAccessToken());//刷新token时间
            TokenResponse tokenResponse = refreshTokenManager.generateRefreshToken(refreshTokenContent.getAccessToken());//根据token重新生成refreshToken
            String encryptPublic =encryptPublic(uucModelInfo,tokenResponse);
            log.info("完成refreshToken流程==============================");
            return R.ok(encryptPublic);
        }catch (ServiceException e){
            log.error("执行refreshToken出错，错误信息为:"+e.getMessage());
            return R.fail(e.getCode(),e.getMessage());
        }
        catch (Exception e){
            log.error("执行refreshToken出错============================,错误信息:{}",e.getMessage());
            return R.fail("获取token失败");
        }
    }

    /**
     * 校验请求参数
     * @param requestEntity
     * @param request
     */
    private void verifyRequest(RequestEntity requestEntity,HttpServletRequest request){
        String requestData=requestEntity.getData();
        String clientId=tokenApiService.getClientId(request);
        if(StringUtils.isEmpty(requestData)||StringUtils.isEmpty(clientId)){
            log.error("请求参数异常，requestData:{}，clientId:{}",requestData,clientId);
            throw new ServiceException("缺少参数",HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 远程查询客户端数据
     * @param clientId
     * @return
     */
    private UucModelInfo checkClient(String clientId){
        UucModelInfo uucModelInfo = tokenApiService.checkClient(clientId);
        if(uucModelInfo==null||StringUtils.isEmpty(uucModelInfo.getClientRsaPrivate())||StringUtils.isEmpty(uucModelInfo.getClientRsaPublic())){
            log.error("客户端参数:{}",uucModelInfo);
            throw new ServiceException("客户端参数异常");
        }
        return uucModelInfo;

    }

    /**
     * 私钥解密请求参数
     * @param uucModelInfo
     * @param requestEntity
     * @return
     */
    private RequestToken decryPrivate(UucModelInfo uucModelInfo,RequestEntity requestEntity){
        String privateSecret=uucModelInfo.getClientRsaPrivate();
        //解密参数
        String decryBody=null;
        try{
            decryBody = RSAEncrypt.decryptPrivate(requestEntity.getData(), privateSecret);
        }catch (Exception e){
            log.error("解密失败，请求的数据为：{}",requestEntity.getData());
            throw new ServiceException("解密失败");
        }
        RequestToken requestToken = JSON.parseObject(decryBody, RequestToken.class);
        return requestToken;
    }

    /**
     * 公钥加密返回参数
     * @param uucModelInfo
     * @param tokenResponse
     * @return
     */
    private String encryptPublic(UucModelInfo uucModelInfo,TokenResponse tokenResponse){
        try {
            String jsonString = JSON.toJSONString(tokenResponse);
            String encryptPublic = RSAEncrypt.encryptPublic(jsonString, uucModelInfo.getClientRsaPublic());
            return encryptPublic;
        }catch (Exception e){
            log.error("返回参数加密失败,错误:{}",e.getMessage());
            throw new ServiceException("返回参数加密失败");
        }

    }
}
