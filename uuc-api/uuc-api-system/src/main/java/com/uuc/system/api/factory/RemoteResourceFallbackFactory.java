package com.uuc.system.api.factory;


import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.system.api.RemoteResourceService;
import com.uuc.system.api.domain.ResourceVO;
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
public class RemoteResourceFallbackFactory implements FallbackFactory<RemoteResourceService> {


    private static final Logger log = LoggerFactory.getLogger(RemoteResourceFallbackFactory.class);

    @Override
    public RemoteResourceService create(Throwable throwable)
    {
        log.error("日志服务调用失败:{}", throwable.getMessage());
        return new RemoteResourceService()
        {
            @Override
            public AjaxResult userProjects(String userCode, String source) {
                return AjaxResult.error("查询用户关联项目数失败：" + throwable.getMessage());
            }

            @Override
            public AjaxResult userResources(ResourceVO v, String source) {
                return AjaxResult.error("查询用户资源失败：" + throwable.getMessage());
            }


            @Override
            public AjaxResult userEndPonitResouce(ResourceVO v, String source) {
                return AjaxResult.error("查询用户EndPoint失败：" + throwable.getMessage());
            }
        };

    }

}
