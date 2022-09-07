package com.uuc.common.redis.configure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisLockUtil {

    private static RedisLockRegistry redisLockRegistry;

    public static void setRedisLockRegistry(RedisLockRegistry redisLockRegistry) {
        RedisLockUtil.redisLockRegistry = redisLockRegistry;
    }

    public static RedisLockRegistry getRedisLockRegistry() {
        return redisLockRegistry;
    }

    @Autowired
    private StringRedisTemplate template;

    @Autowired
    private DefaultRedisScript<Long> redisScript;

    private static final Long RELEASE_SUCCESS = 1L;

    private long timeout = 50000;

    public boolean lock(String key, String value) {
        //执行set命令
        Boolean absent = template.opsForValue().setIfAbsent(key, value, timeout, TimeUnit.MILLISECONDS);
        //其实没必要判NULL，这里是为了程序的严谨而加的逻辑
        if (absent == null) {
            return false;
        }
        //是否成功获取锁
        return absent;
}

    public boolean unlock(String key, String value) {
        //使用Lua脚本：先判断是否是自己设置的锁，再执行删除
        Long result = template.execute(redisScript, Arrays.asList(key, value));
        //返回最终结果
        return RELEASE_SUCCESS.equals(result);
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Bean
    public DefaultRedisScript<Long> defaultRedisScript() {
        DefaultRedisScript<Long> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setResultType(Long.class);
        defaultRedisScript.setScriptText("if redis.call('get', KEYS[1]) == KEYS[2] then return redis.call('del', KEYS[1]) else return 0 end");
        return defaultRedisScript;
    }

    public boolean lock2one(String key,Long secondTimeOut) {
        //执行set命令
        Boolean absent = template.opsForValue().setIfAbsent(key, "1", secondTimeOut, TimeUnit.SECONDS);
        if (absent == null) {
            return false;
        }
        if (!absent){
            log.info("----- 【" + key + " 】已存在 -----");
        }
        //是否成功获取锁
        return absent;
    }


    public boolean unlock2one(String key) {
        //使用Lua脚本：先判断是否是自己设置的锁，再执行删除
        Long result = template.execute(redisScript, Arrays.asList(key, "1"));
        //返回最终结果
        return RELEASE_SUCCESS.equals(result);
    }

}
