package com.uuc.system.api.factory;

import com.uuc.system.api.RemoteXxlJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class RemoteXxlJobFallbackFactory implements FallbackFactory<RemoteXxlJobService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteXxlJobFallbackFactory.class);


    @Override
    public RemoteXxlJobService create(Throwable cause) {
        log.error("XxlJob服务调用失败:{}", cause.getMessage());
        return new RemoteXxlJobService() {
            @Override
            public void syncDeptAndUser(String source) {

            }

        };
    }
}
