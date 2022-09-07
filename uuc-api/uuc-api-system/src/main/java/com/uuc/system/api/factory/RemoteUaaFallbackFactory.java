package com.uuc.system.api.factory;

import com.uuc.common.core.domain.R;
import com.uuc.system.api.RemoteUaaService;
import com.uuc.system.api.model.LoginUser;
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
public class RemoteUaaFallbackFactory implements FallbackFactory<RemoteUaaService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteUaaFallbackFactory.class);

    @Override
    public RemoteUaaService create(Throwable throwable)
    {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteUaaService()
        {
            @Override
            public R<LoginUser> getUserInfo(String source)
            {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<String> getUserCenterUrl(String source) {
                return R.fail("获取用户中心API地址失败:" + throwable.getMessage());
            }
        };
    }
}
