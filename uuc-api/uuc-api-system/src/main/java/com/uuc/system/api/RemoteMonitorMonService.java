package com.uuc.system.api;

import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.constant.ServiceNameConstants;
import com.uuc.common.core.domain.R;
import com.uuc.system.api.factory.RemoteMonitorMonFallbackFactory;
import com.uuc.system.api.factory.RemoteMonitorTransFallbackFactory;
import com.uuc.system.api.model.cmdb.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * 监控mon服务
 * 
 * @author uuc
 */
@FeignClient(contextId = "remoteMonitorMonService", value = ServiceNameConstants.MONITOR_MON_HTTP, fallbackFactory = RemoteMonitorMonFallbackFactory.class)
public interface RemoteMonitorMonService
{
    /**
     * 查询资源告警列表
     *
     * @param serverMonitorVo
     * @param source 请求来源
     * @return 结果
     */

    @PostMapping("/api/mon/v1/server/warnings")
    public R<List<Warning>> getWarnings(@RequestBody ServerMonitorVo serverMonitorVo, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
