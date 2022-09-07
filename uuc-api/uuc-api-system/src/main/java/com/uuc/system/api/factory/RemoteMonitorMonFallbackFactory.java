package com.uuc.system.api.factory;

import com.uuc.common.core.domain.R;
import com.uuc.system.api.RemoteMonitorMonService;
import com.uuc.system.api.RemoteMonitorTransService;
import com.uuc.system.api.model.cmdb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 监控mon服务回调
 * 
 * @author uuc
 */
@Component
public class RemoteMonitorMonFallbackFactory implements FallbackFactory<RemoteMonitorMonService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteMonitorMonFallbackFactory.class);

    @Override
    public RemoteMonitorMonService create(Throwable throwable)
    {
        log.error("查询云平台资源状态信息:{}", throwable.getMessage());
        return new RemoteMonitorMonService()
        {

            @Override
            public R<List<Warning>> getWarnings(ServerMonitorVo serverMonitorVo, String source) {
                return R.fail("查询资源告警列表失败：" + throwable.getMessage());
            }
        };
    }
}
