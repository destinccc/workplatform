package com.uuc.system.api;

import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.constant.ServiceNameConstants;
import com.uuc.system.api.factory.RemoteXxlJobFallbackFactory;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * XxlJob服务
 */
@FeignClient(contextId = "remoteXxlJobService", value = ServiceNameConstants.XXLJOB_SERVICE, fallbackFactory = RemoteXxlJobFallbackFactory.class)
public interface RemoteXxlJobService {

    @ApiOperation(value = "同步用户中心组织及人员", notes = "同步用户中心组织及人员")
    @GetMapping(value = "/userCenter/syncDeptAndUser")
    public void syncDeptAndUser(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
