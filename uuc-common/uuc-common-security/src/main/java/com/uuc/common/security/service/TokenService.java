package com.uuc.common.security.service;

import com.uuc.common.core.constant.CacheConstants;
import com.uuc.common.core.constant.HttpStatus;
import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.exception.ServiceException;
import com.uuc.common.core.utils.JwtUtils;
import com.uuc.common.core.utils.ServletUtils;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.core.utils.ip.IpUtils;
import com.uuc.common.core.utils.uuid.IdUtils;
import com.uuc.common.redis.service.RedisService;
import com.uuc.common.security.domain.RefreshTokenContent;
import com.uuc.common.security.utils.SecurityUtils;
import com.uuc.system.api.RemoteUaaService;
import com.uuc.system.api.model.LoginUser;
import com.uuc.system.api.model.UucUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 *
 * @author uuc
 */
@Component
public class TokenService {
    @Autowired
    private RedisService redisService;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long HOURS = 3600;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private final static long expireTime = CacheConstants.EXPIRATION * HOURS;

    private final static long refreshExpireTime = CacheConstants.REFRESH_EXPIRATION * HOURS;

    private final static String ACCESS_TOKEN = CacheConstants.LOGIN_TOKEN_KEY;

    private final static String TOKEN_CODE = CacheConstants.TOKEN_CODE_KEY;

    private final static String CODE_TOKEN = CacheConstants.CODE_TOKEN_KEY;

    private final static String REFRESH_TOKEN = CacheConstants.REFRESH_TOKEN;

    private final static String TOKEN_REFRESH= CacheConstants.TOKEN_REFRESH;

    private final static String REFRESH_TOKEN_CONTENT= CacheConstants.REFRESH_TOKEN_CONTENT;


    private final static Long MILLIS_MINUTE_TEN = CacheConstants.REFRESH_TIME * MILLIS_MINUTE;

    private static final Logger log = LoggerFactory.getLogger(TokenService.class);



    /**
     * 创建令牌
     */
    public Map<String, Object> createToken(LoginUser loginUser) {
        String token = IdUtils.fastUUID();
        loginUser.setToken(token);
        loginUser.setIpaddr(IpUtils.getIpAddr(ServletUtils.getRequest()));


        // Jwt存储信息
        Map<String, Object> claimsMap = new HashMap<String, Object>();
        claimsMap.put(SecurityConstants.USER_KEY, token);
        claimsMap.put(SecurityConstants.DETAILS_USER_ID, loginUser.getUserid());
        claimsMap.put(SecurityConstants.DETAILS_USERNAME, loginUser.getUsername());
        claimsMap.put(SecurityConstants.DETAILS_USERNAME_ZH, loginUser.getUsernameZh());
        // 接口返回信息
        //Map<String, Object> rspMap = new HashMap<String, Object>();
        String accessToken = JwtUtils.createToken(claimsMap);
        //rspMap.put("access_token", JwtUtils.createToken(claimsMap));
        //rspMap.put("expires_in", expireTime);
        loginUser.setToken(accessToken);
        refreshToken(loginUser);
        // 接口返回信息
        Map<String, Object> rspMap = new HashMap<String, Object>();
        rspMap.put("access_token", accessToken);
        rspMap.put("expires_in", expireTime);
        rspMap.put("expires_time",loginUser.getExpireTime());
        rspMap.put("userJobNumber",loginUser.getUucUserInfo()!=null?loginUser.getUucUserInfo().getUserJobNumber():null);
        return rspMap;
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser() {
        return getLoginUser(ServletUtils.getRequest());
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = SecurityUtils.getToken(request);
        return getLoginUser(token);
    }

    @Autowired
    private RemoteUaaService remoteUaaService;

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(String token) {
        LoginUser user = null;
        try {
            if (StringUtils.isNotEmpty(token)) {
//                user = redisService.getCacheObject(getTokenKey(token));
                return remoteUaaService.getUserInfo(SecurityConstants.INNER).getData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * 获取用户信息
     */
    public LoginUser getLoginUserByToken(String token) {
        LoginUser user = null;
        user = redisService.getCacheObject(getTokenKey(token));
        if (Objects.isNull(user)){
            return user;
        }
//        user.setToken(null);
        return user;
    }
    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser) {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())) {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户缓存信息
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userkey = JwtUtils.getUserKey(token);
            redisService.deleteObject(getTokenKey(userkey));
        }
    }

    /**
     * 验证令牌有效期，相差不足120分钟，自动刷新缓存
     *
     * @param loginUser
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        // todo 失效时间3600s 后续做成nacos配置
        if (expireTime - currentTime/1000 <= 3600) {
            //refreshToken(getTokenRefreshKey(loginUser.getToken()));
           /* if(loginUser==null||StringUtils.isEmpty(loginUser.getToken())){
                throw new ServiceException("登录信息已过期", HttpStatus.UNAUTHORIZED);
            }*/
            refreshTokenForInner(loginUser.getToken());
        }
    }

    /**
     * 验证token是否有效
     */
    public boolean checkToken(String token){
        String tokenKey=getTokenKey(token);
        Object cacheObject = redisService.getCacheObject(tokenKey);
        if(cacheObject==null){
            return false;
        }
        return true;
    }
    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime()/1000 + expireTime);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        // token不返回密码
        UucUserInfo uucUserInfo=loginUser.getUucUserInfo();
        uucUserInfo.getAccountList().get(0).setLoginPwd("");
        loginUser.setUucUserInfo(uucUserInfo);
        redisService.setCacheObject(userKey, loginUser, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 根据token刷新token关联的redis保存时间
     */
    public Map<String, Object> refreshToken(String refreshToken){

        //String refreshTokenKey=getRefreshTokenKey(refreshToken);
        //redisService.deleteObject(refreshTokenKey);
        if(StringUtils.isNotEmpty(refreshToken)){
            //Object cacheObject = redisService.getCacheObject(refreshTokenKey);
            Object refreshTokenObj = redisService.getCacheObject(getRefreshContentKey(refreshToken));
            if(refreshTokenObj==null){
                throw new ServiceException("refreshToken已失效",HttpStatus.UNAUTHORIZED);
            }
            //
            //redisService.deleteObject(refreshTokenKey);
            //redisService.deleteObject(refreshToken);
            //String token=(String)cacheObject;
            RefreshTokenContent refreshTokenContent=(RefreshTokenContent)refreshTokenObj;
            LoginUser loginUser=refreshTokenContent.getLoginUser();
            String code=refreshTokenContent.getCode();
            String accessToken=refreshTokenContent.getAccessToken();
            redisService.deleteObject(getTokenRefreshKey(accessToken));
            String tokenKey=getTokenKey(accessToken);
            String codeKey=getCodeKey(accessToken);
            if(StringUtils.isEmpty(code)||loginUser==null||StringUtils.isEmpty(accessToken)){
                throw new ServiceException("登录信息已失效", HttpStatus.UNAUTHORIZED);
            }else {
                String codeTokenKey=getCodeTokenKey(code);
                redisService.expire(codeTokenKey,expireTime,TimeUnit.SECONDS);
                redisService.expire(codeKey,expireTime,TimeUnit.SECONDS);
                //重新保存loginUser
                loginUser.setExpireTime(System.currentTimeMillis()/1000+expireTime);
                log.info("刷新token后的到期时间为:{}------------------------------------",loginUser.getExpireTime());
                redisService.setCacheObject(tokenKey,loginUser,expireTime,TimeUnit.SECONDS);
                //重新生成一个refreshToken
                //String refreshToken1 = createRefreshToken(token);
                String tokenRefreshKey = getTokenRefreshKey(accessToken);
                redisService.setCacheObject(tokenRefreshKey,refreshToken,refreshExpireTime,TimeUnit.SECONDS);
                redisService.setCacheObject(refreshToken,refreshTokenContent,refreshExpireTime,TimeUnit.SECONDS);
                Map<String, Object> rspMap = new HashMap<String, Object>();
                rspMap.put("accessToken", accessToken);
                rspMap.put("expiresIn", expireTime);
                rspMap.put("expiresTime",System.currentTimeMillis()/1000+expireTime);
                rspMap.put("userCode",loginUser.getUserid());
                rspMap.put("refreshToken",refreshToken);
                return rspMap;
            }

        }
        throw new ServiceException("已失效", HttpStatus.UNAUTHORIZED);
    }
    /**
     * 统一门户刷新token时可能没有refreshToken的场景，此方法专门用于统一门户后端刷新token
     *
     */
    @Autowired
    private HttpServletRequest request;

    public Map<String, Object> refreshTokenForInner(String token){
        log.info("进入内部token续期流程----------------------");
        if(StringUtils.isEmpty(token)){
            throw new ServiceException(StringUtils.getSlfStr(request.getRequestURI())+"缺少token参数",HttpStatus.UNAUTHORIZED);
        }
        String tokenKey=getTokenKey(token);
        String codeKey=getCodeKey(token);
        Object cacheObject = redisService.getCacheObject(codeKey);
        LoginUser loginUser=getLoginUserByToken(token);
        if(loginUser==null){
            throw new ServiceException("token已过期", HttpStatus.UNAUTHORIZED);
        }
        if(cacheObject!=null){
            String code=String.valueOf(cacheObject);
            String codeTokenKey=getCodeTokenKey(code);
            redisService.expire(codeTokenKey,expireTime,TimeUnit.SECONDS);
            redisService.expire(codeKey,expireTime,TimeUnit.SECONDS);
        }
        //重新保存loginUser
        loginUser.setExpireTime(System.currentTimeMillis()/1000+expireTime);
        redisService.setCacheObject(tokenKey,loginUser,expireTime,TimeUnit.SECONDS);
        Map<String, Object> rspMap = new HashMap<String, Object>();
        rspMap.put("accessToken", token);
        rspMap.put("expiresIn", expireTime);
        rspMap.put("expiresTime",System.currentTimeMillis()/1000+expireTime);
        rspMap.put("userCode",loginUser.getUserid());
        log.info("续期后数据:{}----------------------",rspMap);
        return rspMap;

    }
    /**
     * 根据token清楚缓存中的token及对应的code及refreshToken
     */
    public void inValidToken(String token){
        String tokenKey=getTokenKey(token);
        String codeKey=getCodeKey(token);
        String tokenRefreshKey=getTokenRefreshKey(token);
        String code=redisService.getCacheObject(codeKey);
        String refreshToken=redisService.getCacheObject(tokenRefreshKey);
        if(StringUtils.isNotEmpty(tokenKey)){
            redisService.deleteObject(tokenKey);
        }
        if(StringUtils.isNotEmpty(codeKey)){
            redisService.deleteObject(codeKey);
            if(StringUtils.isNotEmpty(code)){
                redisService.deleteObject(getCodeTokenKey(code));
            }
        }
        if(StringUtils.isNotEmpty(tokenRefreshKey)){
            redisService.deleteObject(tokenRefreshKey);
            if(StringUtils.isNotEmpty(refreshToken)){
                redisService.deleteObject(getRefreshContentKey(refreshToken));
            }
        }
    }
    /**
     *
     * @param token
     * @return
     */
    public Object verifyTokenValid(String token){
        Object cacheObject = redisService.getCacheObject(getCodeKey(token));
        if(cacheObject==null){
            return null;
        }
        return cacheObject;
    }

    /**
     * 根据token生成code
     * @param token
     * @return
     */
    public String createCode(String token){
        Object cacheCode =redisService.getCacheObject(getCodeKey(token));
        if(cacheCode!=null){
             return String.valueOf(cacheCode);
        }
        String code = IdUtils.fastUUID();
        redisService.setCacheObject(getCodeKey(token),code,expireTime,TimeUnit.SECONDS);
        redisService.setCacheObject(getCodeTokenKey(code),token,expireTime,TimeUnit.SECONDS);
        return code;
    }
    /**
     * 根据token生成refreshToken,存在则不生生成
     */
    public String  createRefreshToken(String token){
        String refreshToken =null;
        Object cacheObject = redisService.getCacheObject(getTokenRefreshKey(token));
        if(cacheObject!=null){
             refreshToken=String.valueOf(cacheObject);
        }else{
            refreshToken=IdUtils.fastUUID();
            //redisService.setCacheObject(getRefreshTokenKey(refreshToken),token,refreshExpireTime,TimeUnit.SECONDS);
            redisService.setCacheObject(getTokenRefreshKey(token),refreshToken,refreshExpireTime,TimeUnit.SECONDS);
            //refreshTokenContent中保存token、code及loginUser信息
            RefreshTokenContent refreshTokenContent=new RefreshTokenContent();
            refreshTokenContent.setAccessToken(token);
            LoginUser loginUser = getLoginUser(token);
            String code=redisService.getCacheObject(getCodeKey(token));
            if(loginUser!=null){
                refreshTokenContent.setLoginUser(loginUser);
            }
            if(StringUtils.isNotEmpty(code)){
                refreshTokenContent.setCode(code);
            }
            redisService.setCacheObject(getRefreshContentKey(refreshToken),refreshTokenContent,refreshExpireTime,TimeUnit.SECONDS);
        }
        return refreshToken;
    }

    /**
     * 根据之前的refreshToken获取登录用户信息以及code信息
     */
    /**
     * 根据code获取token
     * @param code
     * @return
     *
     */
    public String getAccessTokenByCode(String code){
        Object cacheObject = redisService.getCacheObject(getCodeTokenKey(code));
        if(cacheObject==null){
            return null;
        }
        String accessToken=String.valueOf(cacheObject);
        String tokenCodeKey=getCodeKey(accessToken);
        redisService.expire(getCodeTokenKey(code),expireTime,TimeUnit.SECONDS);
        redisService.expire(tokenCodeKey,expireTime,TimeUnit.SECONDS);
        redisService.expire(getTokenKey(accessToken),expireTime,TimeUnit.SECONDS);
        //更新用户的过期时间
        LoginUser loginUser = getLoginUserByToken(accessToken);
        loginUser.setExpireTime(System.currentTimeMillis()/1000+expireTime);
        redisService.setCacheObject(getTokenKey(accessToken), loginUser, expireTime, TimeUnit.SECONDS);
        return accessToken;
    }
    private String getTokenKey(String token) {
        return ACCESS_TOKEN + token;
    }
    private String getCodeKey(String token){
        return TOKEN_CODE+token;
    }
    private String getCodeTokenKey(String code){return CODE_TOKEN+code;}

    private String getRefreshTokenKey(String refreshToken){
        return REFRESH_TOKEN+refreshToken;
    }
    private String getTokenRefreshKey(String token){
        return TOKEN_REFRESH+token;
    }

    private String getRefreshContentKey(String refreshToken){
        return REFRESH_TOKEN_CONTENT+refreshToken;
    }
}
