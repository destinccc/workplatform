package com.uuc.system.uuc.monitor;

import com.uuc.common.redis.configure.RedisLockUtil;
import com.uuc.system.uuc.service.impl.UucUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/*
 * @Description 未设置角色的用户设置默认用户
 **/
@Component
@EnableScheduling
public class SetDefaultRoleMonitor {

    @Autowired
    private RedisLockUtil redisLockUtil;
    @Autowired
    private UucUserInfoService uucUserInfoService;

    @Scheduled(cron = "0 0 1 * * ?", zone = "GMT+8:00")
    public void setDefaultRoleMonitor() {
        String lockStr = "setDefaultRoleMonitor";
        boolean getLock = redisLockUtil.lock2one(lockStr, 100L);
        if (getLock) {
            try {
                uucUserInfoService.setDefaultRoleMonitor();
            } finally {
                redisLockUtil.unlock2one(lockStr);
            }
        }
    }

}
