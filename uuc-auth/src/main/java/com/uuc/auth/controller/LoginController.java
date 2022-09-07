package com.uuc.auth.controller;

import com.alibaba.fastjson.JSON;
import com.uuc.auth.config.properties.SsoLoginProperties;
import com.uuc.auth.form.LoginBody;
import com.uuc.auth.form.RequestEntity;
import com.uuc.auth.form.SecretBody;
import com.uuc.auth.service.SysLoginService;
import com.uuc.auth.utils.RSAEncrypt;
import com.uuc.common.core.constant.*;
import com.uuc.common.core.domain.R;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.core.utils.ip.IpUtils;
import com.uuc.common.redis.service.RedisService;
import com.uuc.common.security.service.TokenService;
import com.uuc.system.api.RemoteClientService;
import com.uuc.system.api.domain.UucModelInfo;
import com.uuc.system.api.model.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * token 控制
 *
 * @author uuc
 */
@RestController
public class LoginController
{
    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SysLoginService sysLoginService;

    @Autowired
    private RemoteClientService remoteClientService;

    protected static final long EXPIRE_SECOND = 7200;

    @Autowired
    private SsoLoginProperties ssoLoginProperties;


    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    /**
     * 登录，用于统一门户登录页面调用
     * @param form
     * @return
     */
    @PostMapping("login")
    public R<?> login(@RequestBody LoginBody form,HttpServletRequest request)

    {
        log.info("进入登录流程,请求IP为{}",IpUtils.getIpAddr(request));
        //校验验证码
      if(StringUtils.isEmpty(form.getCode())||StringUtils.isEmpty(form.getUuid())){
          log.error("验证码参数错误");
          return R.fail("验证码参数错误");
        }
        String verifyKey = Constants.CAPTCHA_CODE_KEY + form.getUuid();
        String captcha = redisService.getCacheObject(verifyKey);
        redisService.deleteObject(verifyKey);
        if (captcha == null)
        {
            log.error("验证码超时");
            return R.fail("验证码超时");
        }
        if (!form.getCode().equalsIgnoreCase(captcha))
        {
            log.error("验证码不匹配");
            return R.fail("验证码不匹配");
        }
        String password = descPassword(form.getPassword());
        if (StringUtils.isEmpty(password)) {
            log.error("解密后密钥为空");
            return R.fail("密码错误");
        }
        // 用户登录
        log.info("开始调用远程登录方法......................");
        LoginUser userInfo = sysLoginService.login(form.getUsername(), password);
        log.info("调用返回成功");
        String active = userInfo.getUucUserInfo().getAccountList().get(0).getActive();
        Map<String, Object> resultMap = tokenService.createToken(userInfo);
        log.info("创建token成功.......................");
        resultMap.put("active", active);
        // 获取登录token
        return R.ok(resultMap,"操作成功");
    }

    /**
     * 密码解密
     * @param str
     * @return
     */
    private String descPassword(String str) {
        String result = "";
        // 密码解密
        log.info("远程调用获取模块信息");
        R<UucModelInfo> uucModelInfoR = remoteClientService.checkClient(ClientConstants.MODEL_CODE_UUC, SecurityConstants.INNER);
        UucModelInfo uucModelInfo = uucModelInfoR.getData();
        if(uucModelInfo!=null){
            String privateKey=uucModelInfo.getClientRsaPrivate();
            try {
                result = RSAEncrypt.decryptPrivate(str, privateKey);
            } catch (Exception e) {
                log.error("解密字符串失败");
                e.printStackTrace();
            }
        }
        return result;
    }
    /**
     * 登录之后，登录页面根据token生成code，如果token与code的关系存在则直接返回
     * @param request
     * @return
     */
    @GetMapping("/getCode")
    public R<?> getCode(HttpServletRequest request){
        log.info("进入获取code流程,请求IP为：{}",IpUtils.getIpAddr(request));
        String token = getToken(request);
        if(StringUtils.isEmpty(token)){
            log.error("缺少token");
            return R.fail(HttpStatus.UNAUTHORIZED,"认证失败");
        }
        //Object tokenValid = tokenService.verifyTokenValid(token);
        /*if(tokenValid==null){
            return R.fail(HttpStatus.FORBIDDEN,"授权过期");
        }*/
        String code = tokenService.createCode(token);
        log.info("生成code成功");
        return R.ok(code,"操作成功");
    }

    /**
     * 根据code获取accessToken，用于各系统调用
     * @param requestEntity
     * @param request
     * @return
     */
    @PostMapping("/getAccessToken")
    public R<?> getAccessToken(@RequestBody RequestEntity requestEntity, HttpServletRequest request){
        log.info("进入获取token流程,请求Ip为{}",IpUtils.getIpAddr(request));
        String clientId = getClientId(request);
        if(StringUtils.isEmpty(clientId)){
            log.error("缺少客户端参数");
            return R.fail("客户端信息错误");
        }
        //调用远程方法查询客户端与客户端密钥
        R<UucModelInfo> uucModelInfoR = remoteClientService.checkClient(clientId, SecurityConstants.INNER);
        log.info("远程调用获取模块信息成功返回");
        UucModelInfo uucModelInfo = uucModelInfoR.getData();
        if(uucModelInfo==null){
            log.error("返回客户端参数有误");
            return R.fail("客户端信息错误");

        }
        String publicKey=uucModelInfo.getClientRsaPublic();
        //String secretKey=uucModelInfo.getClientRsaPrivate();
        String decrypt="";
        try {
            decrypt= RSAEncrypt.decryptPublic(requestEntity.getData(), publicKey);

        if(StringUtils.isEmpty(decrypt)){
            log.error("解密字符串为空");
            return R.fail("解密失败");
        }
        SecretBody secretBody = JSON.parseObject(decrypt, SecretBody.class);
        if(StringUtils.isEmpty(secretBody.getCode())){
            log.error("code解密为空");
            return R.fail("code解密失败");
        }
        String accessToken = tokenService.getAccessTokenByCode(secretBody.getCode());
        log.info("执行获取accessToken完成");
        if(StringUtils.isEmpty(accessToken)){
            log.error("code失效...........");
            return R.fail(HttpStatus.UNAUTHORIZED,"code失效");
        }
        LoginUser loginUser = tokenService.getLoginUserByToken(accessToken);
        log.info("执行获取accessToken完成");
        if(loginUser==null){
            log.error("根据token获取用户信息失败");
            return R.fail(HttpStatus.UNAUTHORIZED,"认证信息已失效");
        }
        //生成refreshToken
        String refreshToken = tokenService.createRefreshToken(accessToken);
        log.info("执行创建refreshToken完成");
        Map<String, Object> rspMap = new HashMap<String, Object>();
        rspMap.put("accessToken", accessToken);
        rspMap.put("expiresTime",loginUser.getExpireTime());
        rspMap.put("expiresIn",EXPIRE_SECOND);
        rspMap.put("userCode", loginUser.getUserid());
        rspMap.put("refreshToken", refreshToken);
        rspMap.put("userJobNumber",loginUser.getUucUserInfo()!=null?loginUser.getUucUserInfo().getUserJobNumber():null);
        String s = JSON.toJSONString(rspMap);
        String res=RSAEncrypt.encryptPublic(s,publicKey);
        log.info("accessToken加密准备返回....................");
        return R.ok(res,"操作成功");
        }catch (Exception e){
        return R.fail("获取token失败");
        }
    }


    /**
     * 登出
     * @param request
     * @return
     */
    @RequestMapping (value = "logout",method = {RequestMethod.GET,RequestMethod.DELETE})
    public R<?> logout(HttpServletRequest request)
    {
        log.info("进入登出程序，请求ip为{}，请求的url：{}",IpUtils.getIpAddr(request),request.getRequestURI());
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token))
        {
            //删除redis中的token及对应的code
            log.info("获取token成功");
            tokenService.inValidToken(token);
            log.info("执行token失效完成");
            // 记录用户退出日志
            //sysLoginService.logout(username);
        }
        return R.ok("操作成功");
    }

    /**
     * 刷新token
     * @param requestEntity
     * @param request
     * @return
     */
    @PostMapping(value = "refreshToken")
    public R<?> refresh(@RequestBody RequestEntity requestEntity,HttpServletRequest request)
    {
        log.info("进入refreshToken流程，请求的ip为{}",IpUtils.getIpAddr(request));
        String clientId = getClientId(request);
        if(StringUtils.isEmpty(clientId)){
            log.error("缺少客户端参数");
            return R.fail("缺少客户端参数");
        }
        //调用远程方法查询客户端与客户端密钥
        R<UucModelInfo> uucModelInfoR = remoteClientService.checkClient(clientId, SecurityConstants.INNER);
        log.info("远程调用获取模块方法返回...........");
        UucModelInfo uucModelInfo = uucModelInfoR.getData();
        if(uucModelInfo==null){
            log.error("客户端参数有误");
            return R.fail("客户端信息错误");
        }
        String publicKey=uucModelInfo.getClientRsaPublic();
        try{
        String decrypt = RSAEncrypt.decryptPublic(requestEntity.getData(), publicKey);
        if(StringUtils.isEmpty(decrypt)){
            log.error("解密refreshToken字符串为空");
            return R.fail("解密失败");
        }
        SecretBody secretBody = JSON.parseObject(decrypt, SecretBody.class);
        if(StringUtils.isEmpty(secretBody.getRefreshToken())){
            log.error("解密后没有获取到refreshToken");
            return R.fail("refreshToken解密失败");
        }
        // 刷新令牌有效期
        Map<String, Object> stringObjectMap = tokenService.refreshToken(secretBody.getRefreshToken());
        log.info("执行刷新令牌成功返回");
        String s = JSON.toJSONString(stringObjectMap);
        String res=RSAEncrypt.encryptPublic(s,publicKey);
        log.info("刷新token流程成功，准备返回");
        return R.ok(res,"操作成功");
        }catch (Exception e){
            return R.fail(e.getMessage());
        }
    }

    /**
     * 校验token是否有效
     * @param request
     * @return
     */
    @RequestMapping(value = "checkToken",method = {RequestMethod.GET,RequestMethod.POST})
    public R<?> checkToken(HttpServletRequest request)
    {
        log.info("进入checkToken流程,请求的ip为{}", IpUtils.getIpAddr(request));
        String clientId = getClientId(request);
        if(StringUtils.isEmpty(clientId)){
            log.error("获取客户端参数为空");
            return R.fail("缺少客户端参数");
        }
        //调用远程方法查询客户端与客户端密钥
        R<UucModelInfo> uucModelInfoR = remoteClientService.checkClient(clientId, SecurityConstants.INNER);
        log.info("远程调用获取模块返回");
        UucModelInfo uucModelInfo = uucModelInfoR.getData();
        if(uucModelInfo==null){
            log.error("模块参数有误");
            return R.fail("模块参数错误");
        }
        String publicKey=uucModelInfo.getClientRsaPublic();
        String token = getToken(request);
        if(StringUtils.isEmpty(token)){
            log.info("token为空");
            return R.fail("缺少认证参数");
        }
        // 刷新令牌有效期
        boolean b = tokenService.checkToken(token);
        log.info("checkToken的执行结果为{}",b+"");
        if(b){
            LoginUser loginUser = tokenService.getLoginUser(token);
            String refreshToken = redisService.getCacheObject(CacheConstants.TOKEN_REFRESH + token);
            if(loginUser==null){
                log.error("redis取缓存用户或refreshToken失败");
                return R.fail(HttpStatus.UNAUTHORIZED, "redis取缓存用户失败");
            }
            Map<String, Object> rspMap = new HashMap<String, Object>();
            rspMap.put("accessToken",token);
            rspMap.put("expiresTime",loginUser.getExpireTime());
            System.out.println("过期时间为:"+loginUser.getExpireTime());
            rspMap.put("expiresIn",EXPIRE_SECOND);
            rspMap.put("userCode", loginUser.getUserid());
            rspMap.put("refreshToken", refreshToken);
            String s = JSON.toJSONString(rspMap);
            try {
               String res=RSAEncrypt.encryptPublic(s,publicKey);
                log.info("checkToken加密成功，准备返回");
                return R.ok(res,"操作成功");
            }catch (Exception e){
                return R.fail();
            }
        }
        return R.fail(HttpStatus.UNAUTHORIZED, "checkToken失败");
    }

    /**
     * 根据token获取用户信息
     * @param request
     * @return
     */
    @PostMapping("getUserByToken")
    public R<?> getUserByToken(HttpServletRequest request){
        log.info("进去根据token获取用户信息，请求ip为{}",IpUtils.getIpAddr(request));
        String token = getToken(request);
        if(StringUtils.isEmpty(token)){
            log.error("缺少认证参数");
            return R.fail("缺少认证参数");
        }
        LoginUser loginUser = tokenService.getLoginUser(token);
        LoginUser loginUser1=new LoginUser();
        if(loginUser==null){
            log.error("redis获取用户失败");
            return R.fail(HttpStatus.UNAUTHORIZED,"登录信息已失效");
        }
        loginUser1.setUserid(loginUser.getUserid());
        loginUser1.setUsername(loginUser.getUsername());
        return R.ok(loginUser,"操作成功");
    }

    /**
     * 获取统一登录的url
     * @param
     * @return
     */
    @GetMapping("getLoginUrl")
    public R<?> getLoginUrl(HttpServletRequest request){
        log.info("进入获取统一门户登录地址，请求ip为{}",IpUtils.getIpAddr(request));
        String loginUrl = ssoLoginProperties.getLoginUrl();
        if(StringUtils.isEmpty(loginUrl)){
            log.error("获取配置参数失败");
            return R.fail("获取登录地址失败");
        }
        return R.ok(loginUrl,"操作成功");
    }
    private String  getToken(HttpServletRequest request){
        String token=request.getHeader(TokenConstants.AUTHENTICATION);
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX))
        {
            token = token.replaceFirst(TokenConstants.PREFIX, StringUtils.EMPTY);
        }
        return token;
    }
    private String getClientId(HttpServletRequest request){
        String clientId=request.getHeader(TokenConstants.CLIENTID);
        return clientId;
    }

}
