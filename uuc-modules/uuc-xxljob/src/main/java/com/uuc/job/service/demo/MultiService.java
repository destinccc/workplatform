package com.uuc.job.service.demo;

import cn.hutool.json.JSONUtil;

import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.domain.R;
import com.uuc.common.security.auth.AuthLogic;
import com.uuc.system.api.RemoteUserService;
import com.uuc.system.api.model.LoginUser;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * XxlJob 多服务调用
 *
 */
@Slf4j
@Service
public class MultiService{

    @Autowired
    private RemoteUserService remoteUserService;

    /**
     * 多服务调用
     */
    @XxlJob("multiServiceHandler")
    public void multiServiceHandler() throws Exception {

        R<LoginUser> admin = remoteUserService.getUserInfo("admin", SecurityConstants.INNER);
        System.out.println("========job=========");
        System.out.println(JSONUtil.toJsonStr(admin.getData()));
    }

}
