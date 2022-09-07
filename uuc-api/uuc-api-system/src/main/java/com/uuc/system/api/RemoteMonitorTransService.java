package com.uuc.system.api;

import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.constant.ServiceNameConstants;
import com.uuc.common.core.domain.R;
import com.uuc.system.api.domain.UucModelInfo;
import com.uuc.system.api.factory.RemoteClientFallbackFactory;
import com.uuc.system.api.factory.RemoteMonitorTransFallbackFactory;
import com.uuc.system.api.model.cmdb.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 监控transfor服务
 * 
 * @author uuc
 */
@FeignClient(contextId = "remoteMonitorTransService", value = ServiceNameConstants.MONITOR_TRANSFOR_HTTP, fallbackFactory = RemoteMonitorTransFallbackFactory.class)
public interface RemoteMonitorTransService
{
    /**
     * 查询云平台资源状态信息
     *
     * @param monitorResourceVo
     * @param source 请求来源
     * @return 结果
     */

    @PostMapping("/api/transfer/v1/resources")
    public R<ResourceInfo> getDeptReource(@RequestBody MonitorResourceVo monitorResourceVo, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    /**
     * 查询指定资源信息
     *
     * @param monitorResourceVo
     * @param source 请求来源
     * @return 结果
     */

    @PostMapping("/api/transfer/v1/server/monitor")
    public R<ServerInfo> getServerInfo(@RequestBody MonitorResourceVo monitorResourceVo, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    /**
     * 查询指定资源统计监控信息
     *
     * @param serverMonitorVo
     * @param source 请求来源
     * @return 结果
     */

    @PostMapping("/api/transfer/v1/server/monitor/statistics")
    public R<ServerMonitor> monitorStatistics(@RequestBody ServerMonitorVo serverMonitorVo, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
