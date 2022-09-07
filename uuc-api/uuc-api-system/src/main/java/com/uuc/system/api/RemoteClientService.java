package com.uuc.system.api;

import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.constant.ServiceNameConstants;
import com.uuc.common.core.domain.R;
import com.uuc.system.api.domain.UucModelInfo;
import com.uuc.system.api.factory.RemoteClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 用户服务
 * 
 * @author uuc
 */
@FeignClient(contextId = "remoteClientService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteClientFallbackFactory.class)
public interface RemoteClientService
{
    /**
     * 通过用户名查询用户信息
     *
     * @param clientId 客户端信息
     * @param source 请求来源
     * @return 结果
     */

    @GetMapping("/model/client/{clientId}")
    public R<UucModelInfo> checkClient(@PathVariable("clientId") String clientId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
