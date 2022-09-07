package com.uuc.system.service.impl;

import com.uuc.system.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author shendezhi
 * @date 2022-08-16 09:35
 */
@Component
public class InitSysDictType implements ApplicationRunner {
    @Autowired
    private ISysDictTypeService sysDictTypeService;

    /**
     * 项目启动时，初始化字典到缓存
     */
    public void localSysDictType2Cache() {
        sysDictTypeService.loadingDictCache();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        sysDictTypeService.loadingDictCache();

    }
}
