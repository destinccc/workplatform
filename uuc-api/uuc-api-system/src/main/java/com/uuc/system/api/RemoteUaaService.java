package com.uuc.system.api;

import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.constant.ServiceNameConstants;
import com.uuc.common.core.domain.R;
import com.uuc.system.api.factory.RemoteUaaFallbackFactory;
import com.uuc.system.api.model.LoginUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 用户服务
 *
 * @author uuc
 */
@FeignClient(contextId = "remoteUaaService", value = ServiceNameConstants.GDMARS_AUTH_SERVICE, fallbackFactory = RemoteUaaFallbackFactory.class)
public interface RemoteUaaService
{
    /**
     * 通过用户名查询用户信息
     */
    @GetMapping("/token/getUserInfo")
    public R<LoginUser> getUserInfo(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    /**
     * 获取四川统一用户中心调用地址
     */
    @GetMapping("/token/getUserCenterUrl")
    public R<String> getUserCenterUrl(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
