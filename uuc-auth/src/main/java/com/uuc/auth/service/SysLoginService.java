package com.uuc.auth.service;

import com.uuc.common.redis.service.RedisService;
import com.uuc.system.api.model.UucLoginAccount;
import com.uuc.system.api.model.UucUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.uuc.common.core.constant.Constants;
import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.constant.UserConstants;
import com.uuc.common.core.domain.R;
import com.uuc.common.core.enums.UserStatus;
import com.uuc.common.core.exception.ServiceException;
import com.uuc.common.core.utils.ServletUtils;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.core.utils.ip.IpUtils;
import com.uuc.common.security.utils.SecurityUtils;
import com.uuc.system.api.RemoteLogService;
import com.uuc.system.api.RemoteUserService;
import com.uuc.system.api.domain.SysLogininfor;
import com.uuc.system.api.domain.SysUser;
import com.uuc.system.api.model.LoginUser;

import java.util.concurrent.TimeUnit;

/**
 * 登录校验方法
 * 
 * @author uuc
 */
@Component
public class SysLoginService
{
    @Autowired
    private RemoteLogService remoteLogService;

    @Autowired
    private RemoteUserService remoteUserService;
    @Autowired
    private RedisService redisService;

    /**
     * 登录
     */
    public LoginUser login(String username, String password)
    {
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(username, password))
        {
            recordLogininfor(username, Constants.LOGIN_FAIL, "用户/密码必须填写");
            throw new ServiceException("用户/密码必须填写");
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            recordLogininfor(username, Constants.LOGIN_FAIL, "用户密码不在指定范围");
            throw new ServiceException("用户密码不在指定范围");
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            recordLogininfor(username, Constants.LOGIN_FAIL, "用户名不在指定范围");
            throw new ServiceException("用户名不在指定范围");
        }
        // 查询用户信息
        R<LoginUser> userResult = remoteUserService.getUserInfo(username, SecurityConstants.INNER);

        if (R.FAIL == userResult.getCode())
        {
            throw new ServiceException(userResult.getMsg());
        }

        if (StringUtils.isNull(userResult) || StringUtils.isNull(userResult.getData()))
        {
            recordLogininfor(username, Constants.LOGIN_FAIL, "登录用户不存在");
            throw new ServiceException("登录用户：" + username + " 不存在");
        }
        LoginUser userInfo = userResult.getData();
        UucUserInfo user = userResult.getData().getUucUserInfo();
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            recordLogininfor(username, Constants.LOGIN_FAIL, "对不起，您的账号已被删除");
            throw new ServiceException("对不起，您的账号：" + username + " 已被删除");
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            recordLogininfor(username, Constants.LOGIN_FAIL, "用户已停用，请联系管理员");
            throw new ServiceException("对不起，您的账号：" + username + " 已停用");
        }
        UucLoginAccount loginAccount = user.getAccountList().get(0);
        if ("N".equals(loginAccount.getEnabled())) {
            throw new ServiceException("对不起，您的账号：" + username + " 已失效");
        }
        // 后续一个用户对应多个账户，这里要改
        if (!SecurityUtils.matchesPassword(password, loginAccount.getLoginPwd()))
        {
            recordLogininfor(username, Constants.LOGIN_FAIL, "用户密码错误");
            //半小时连续登录失败5次，账号被锁定
            Object sumObject = redisService.getCacheObject(username + ":login");
            if(sumObject==null){
                redisService.setCacheObject(username+":login",Integer.valueOf(0) , 30L, TimeUnit.MINUTES);
            }else{
                int sum = (int)sumObject;
                if(sum<5){
                    sum=sum+1;
                    redisService.setCacheObject(username+":login",Integer.valueOf(sum) , 30L, TimeUnit.MINUTES);
                }
                if(sum>=5){
                    throw new ServiceException(username+"连续登录5次失败，已被锁定半小时");
                }
            }
            throw new ServiceException("用户不存在/密码错误");
        }
        //从缓存中查询是否有被锁定登陆
        Object sizeObject=redisService.getCacheObject(username + ":login");
        if(sizeObject!=null){
            int sum = (int)sizeObject;
            if(sum>=5){
                throw new ServiceException(username+"连续登录5次失败，已被锁定半小时");
            }
        }
        recordLogininfor(username, Constants.LOGIN_SUCCESS, "登录成功");
        //登陆成功，删除登陆失败次数缓存
        redisService.deleteObject(username+":login");
        return userInfo;
    }

    public void logout(String loginName)
    {
        recordLogininfor(loginName, Constants.LOGOUT, "退出成功");
    }

    /**
     * 注册
     */
    public void register(String username, String password)
    {
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(username, password))
        {
            throw new ServiceException("用户/密码必须填写");
        }
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            throw new ServiceException("账户长度必须在2到20个字符之间");
        }
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            throw new ServiceException("密码长度必须在5到20个字符之间");
        }

        // 注册用户信息
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setNickName(username);
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        R<?> registerResult = remoteUserService.registerUserInfo(sysUser, SecurityConstants.INNER);

        if (R.FAIL == registerResult.getCode())
        {
            throw new ServiceException(registerResult.getMsg());
        }
        recordLogininfor(username, Constants.REGISTER, "注册成功");
    }

    /**
     * 记录登录信息
     * 
     * @param username 用户名
     * @param status 状态
     * @param message 消息内容
     * @return
     */
    public void recordLogininfor(String username, String status, String message)
    {
        SysLogininfor logininfor = new SysLogininfor();
        logininfor.setUserName(username);
        logininfor.setIpaddr(IpUtils.getIpAddr(ServletUtils.getRequest()));
        logininfor.setMsg(message);
        // 日志状态
        if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER))
        {
            logininfor.setStatus(Constants.LOGIN_SUCCESS_STATUS);
        }
        else if (Constants.LOGIN_FAIL.equals(status))
        {
            logininfor.setStatus(Constants.LOGIN_FAIL_STATUS);
        }
        remoteLogService.saveLogininfor(logininfor, SecurityConstants.INNER);
    }
}