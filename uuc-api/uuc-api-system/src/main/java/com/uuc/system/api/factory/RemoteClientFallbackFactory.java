package com.uuc.system.api.factory;

import com.uuc.common.core.domain.R;
import com.uuc.system.api.RemoteClientService;
import com.uuc.system.api.domain.UucModelInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 用户服务降级处理
 * 
 * @author uuc
 */
@Component
public class RemoteClientFallbackFactory implements FallbackFactory<RemoteClientService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteClientFallbackFactory.class);

    @Override
    public RemoteClientService create(Throwable throwable)
    {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteClientService()
        {
            @Override
            public R<UucModelInfo> checkClient(String clientId, String source)
            {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }
        };
    }
}
