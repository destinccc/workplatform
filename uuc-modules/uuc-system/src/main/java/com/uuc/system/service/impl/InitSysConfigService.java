package com.uuc.system.service.impl;

import com.uuc.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author shendezhi
 * @date 2022-08-16 09:33
 */
@Component
public class InitSysConfigService implements ApplicationRunner {
    @Autowired
    private ISysConfigService configService;

    /**
     * 项目启动时，初始化参数到缓存
     */
    public void loadConfig2Cache() {
        configService.loadingConfigCache();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        loadConfig2Cache();
    }
}
