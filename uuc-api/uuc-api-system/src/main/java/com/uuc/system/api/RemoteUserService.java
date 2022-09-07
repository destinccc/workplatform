package com.uuc.system.api;

import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.system.api.factory.RemoteUserFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.constant.ServiceNameConstants;
import com.uuc.common.core.domain.R;
import com.uuc.system.api.domain.SysUser;
import com.uuc.system.api.model.LoginUser;

/**
 * 用户服务
 * 
 * @author uuc
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService
{
    /**
     * 通过用户名查询用户信息
     *
     * @param username 用户名
     * @param source 请求来源
     * @return 结果
     */
    @GetMapping("/userInfo/info/{username}")
    public R<LoginUser> getUserInfo(@PathVariable("username") String username, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 注册用户信息
     *
     * @param sysUser 用户信息
     * @param source 请求来源
     * @return 结果
     */
    @PostMapping("/user/register")
    public R<Boolean> registerUserInfo(@RequestBody SysUser sysUser, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 获取管理员id集合
     *
     * @param source 请求来源
     * @return 结果
     */
    @PostMapping("/userInfo/adminUserList")
    public AjaxResult getAdminUserList(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
