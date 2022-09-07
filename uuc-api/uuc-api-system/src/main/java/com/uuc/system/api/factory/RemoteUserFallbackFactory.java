package com.uuc.system.api.factory;

import com.uuc.common.core.web.domain.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import com.uuc.common.core.domain.R;
import com.uuc.system.api.RemoteUserService;
import com.uuc.system.api.domain.SysUser;
import com.uuc.system.api.model.LoginUser;

/**
 * 用户服务降级处理
 * 
 * @author uuc
 */
@Component
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteUserService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteUserFallbackFactory.class);

    @Override
    public RemoteUserService create(Throwable throwable)
    {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteUserService()
        {
            @Override
            public R<LoginUser> getUserInfo(String username, String source)
            {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> registerUserInfo(SysUser sysUser, String source)
            {
                return R.fail("注册用户失败:" + throwable.getMessage());
            }

            @Override
            public AjaxResult getAdminUserList(String source) {
                return AjaxResult.error("获取所有管理员用户失败：" + throwable.getMessage());
            }
        };
    }
}
