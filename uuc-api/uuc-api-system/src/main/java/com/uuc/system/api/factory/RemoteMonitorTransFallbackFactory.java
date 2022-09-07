package com.uuc.system.api.factory;

import com.uuc.common.core.domain.R;
import com.uuc.system.api.RemoteClientService;
import com.uuc.system.api.RemoteMonitorTransService;
import com.uuc.system.api.domain.UucModelInfo;
import com.uuc.system.api.model.cmdb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 监控transfor服务回调
 * 
 * @author uuc
 */
@Component
public class RemoteMonitorTransFallbackFactory implements FallbackFactory<RemoteMonitorTransService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteMonitorTransFallbackFactory.class);

    @Override
    public RemoteMonitorTransService create(Throwable throwable)
    {
        log.error("查询云平台资源状态信息:{}", throwable.getMessage());
        return new RemoteMonitorTransService()
        {

            @Override
            public R<ResourceInfo> getDeptReource(MonitorResourceVo monitorResourceVo, String source) {
                return R.fail("查询云平台资源状态信息:" + throwable.getMessage());
            }

            @Override
            public R<ServerInfo> getServerInfo(MonitorResourceVo monitorResourceVo, String source) {
                return R.fail("查询指定资源信息:" + throwable.getMessage());
            }

            @Override
            public R<ServerMonitor> monitorStatistics(ServerMonitorVo serverMonitorVo, String source) {
                return  R.fail("查询指定资源监控统计信息:" + throwable.getMessage());
            }
        };
    }
}
