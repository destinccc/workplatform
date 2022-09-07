package com.uuc.resource.apis.inner.controller;

import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.log.annotation.Log;
import com.uuc.common.log.enums.BusinessType;
import com.uuc.system.api.domain.ResourceVO;
import com.uuc.system.api.RemoteSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: fxm
 * @date: 2022-06-23
 * @description: 用户信息标准API
 **/
@RestController
@RequestMapping("/inner")
public class UserInfoController {

    @Autowired
    private RemoteSystemService systemService;

    /**
     * 查询用户资源
     * @param v
     * @return
     */
    @PostMapping("/v1/userInfo/list")
    @Log(title = "用户信息", businessType = BusinessType.SELECT,desc = "获取用户信息")
    public AjaxResult userResources(@RequestBody ResourceVO v){
        return AjaxResult.success();
    }
}
