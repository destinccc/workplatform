package com.uuc.job.service.usercenter.controller;

import com.uuc.common.security.annotation.InnerAuth;
import com.uuc.job.service.usercenter.UserCenterJobService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 四川用户中心
 **/
@RestController
@RequestMapping("/userCenter")
@Slf4j
public class ScUserCenterController {

    @Autowired
    private UserCenterJobService userCenterJobService;

    @InnerAuth
    @GetMapping("/syncDeptAndUser")
    @ApiOperation(value = "同步用户中心组织及人员", notes = "同步用户中心组织及人员")
    public void syncDeptAndUser() throws Exception {
        userCenterJobService.syncDeptResource();
        userCenterJobService.syncUserResource();
    }

}
