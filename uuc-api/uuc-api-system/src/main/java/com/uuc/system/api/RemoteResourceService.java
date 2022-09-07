package com.uuc.system.api;

import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.constant.ServiceNameConstants;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.system.api.factory.RemoteResourceFallbackFactory;
import com.uuc.system.api.domain.ResourceVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * resource服务
 */
@FeignClient(contextId = "remoteResourceService", value = ServiceNameConstants.RESOURCE_SERVICE, fallbackFactory = RemoteResourceFallbackFactory.class)
public interface RemoteResourceService {

    /**
     * 获取人员关联项目数
     *
     * @param source 请求来源
     * @return 结果
     */
    @GetMapping("/inner/v1/perms/userProjects")
    public AjaxResult userProjects(@RequestParam("userCode") String userCode, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    /**
     * 获取人员关联资源
     *
     * @param source 请求来源
     * @return 结果
     */
    @PostMapping("/inner/v1/perms/userResources")
    public AjaxResult userResources(@RequestBody ResourceVO v, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    /**
     * 获取人员关联项目数
     *
     * @param source 请求来源
     * @return 结果
     */
    @GetMapping("/inner/v1/perms/endPointResources")
    public AjaxResult userEndPonitResouce(@RequestBody ResourceVO v, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
