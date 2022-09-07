package com.uuc.resource.apis.inner.controller;

import com.uuc.common.core.exception.CheckedException;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.log.annotation.Log;
import com.uuc.common.log.enums.BusinessType;
import com.uuc.common.security.annotation.InnerAuth;
import com.uuc.resource.apis.inner.service.ResourcePermsService;
import com.uuc.resource.apis.inner.vo.DeptVO;
import com.uuc.resource.apis.inner.vo.ProjectVO;
import com.uuc.system.api.domain.ResourceVO;
import com.uuc.system.api.model.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: fxm
 * @date: 2022-06-22
 * @description: 数据权限标准API
 **/
@RestController
@RequestMapping("/inner")
@Api(tags = "资源权限")
public class ResourcePermsController {
    @Autowired
    private ResourcePermsService resourcePermsService;

    /**
     * 查询用户资源
     * @param v
     * @return
     */
    @InnerAuth
    @PostMapping("/v1/perms/userResources")
    @Log(title = "数据权限", businessType = BusinessType.SELECT, desc = "获取用户资源")
    @ApiOperation(value = "获取用户资源", notes = "获取用户资源")
    public AjaxResult userResources(@RequestBody ResourceVO v){
        if (StringUtils.isEmpty(v.getUserCode())) {
            throw new CheckedException("userCode can't be null");
        }
        return AjaxResult.success(resourcePermsService.userResources(v));
    }


    @InnerAuth
    @PostMapping("/v1/perms/endPointResources")
    @Log(title = "数据权限", businessType = BusinessType.SELECT, desc = "获取物理机/云主机EndPoint信息")
    @ApiOperation(value = "获取物理机/云主机EndPoint信息", notes = "获取物理机/云主机EndPoint信息")
    public AjaxResult endPointResources(@RequestBody ResourceVO v){
        if (StringUtils.isEmpty(v.getUserCode())) {
            throw new CheckedException("userCode can't be null");
        }
        return AjaxResult.success(resourcePermsService.endPointResources(v));
    }

    /**
     * 查询用户关联组织，以树结构返回
     * @return
     */
    @InnerAuth
    @GetMapping("/v1/perms/userDepts")
    @Log(title = "数据权限", businessType = BusinessType.SELECT, desc = "获取用户组织")
    @ApiOperation(value = "获取用户组织", notes = "获取用户组织")
    public AjaxResult userDepts(@RequestParam String userCode){
        if (StringUtils.isEmpty(userCode)) {
            throw new CheckedException("userCode can't be null");
        }
        return AjaxResult.success(resourcePermsService.userDepts(userCode));
    }

    /**
     * 查询用户关联项目
     * @return
     */
    @InnerAuth
    @GetMapping("/v1/perms/userProjects")
    @Log(title = "数据权限", businessType = BusinessType.SELECT, desc = "获取用户项目")
    @ApiOperation(value = "获取用户项目", notes = "获取用户项目")
    public AjaxResult userProjects(@RequestParam String userCode){
        if (StringUtils.isEmpty(userCode)) {
            throw new CheckedException("userCode can't be null");
        }
        return AjaxResult.success(resourcePermsService.userProjects(userCode));
    }

    /**
     * 查询组织关联项目
     *  目前查询的是组织下的所有项目，没有和人关联
     * @return
     */
    @InnerAuth
    @PostMapping("/v1/perms/deptProjects")
    @Log(title = "数据权限", businessType = BusinessType.SELECT, desc = "获取组织项目")
    @ApiOperation(value = "获取组织项目", notes = "获取组织项目")
    public AjaxResult deptProjects(@RequestBody DeptVO v){
        if (StringUtils.isEmpty(v.getUserCode())) {
            throw new CheckedException("userCode can't be null");
        }
        if (CollectionUtils.isEmpty(v.getDeptCodes())) {
            throw new CheckedException("deptCodes can't be null");
        }
        return AjaxResult.success(resourcePermsService.deptProjects(v));
    }

    /**
     * 查询组织关联资源
     *  如果人是管理员角色 或者 组织管理员、则查全部；如果不是组织管理员，则为空
     * @return
     */
    @InnerAuth
    @PostMapping("/v1/perms/deptResources")
    @Log(title = "数据权限", businessType = BusinessType.SELECT, desc = "获取组织资源")
    @ApiOperation(value = "获取组织资源", notes = "获取组织资源")
    public AjaxResult deptResources(@RequestBody DeptVO v){
        if (StringUtils.isEmpty(v.getUserCode())) {
            throw new CheckedException("userCode can't be null");
        }
        if (CollectionUtils.isEmpty(v.getDeptCodes())) {
            throw new CheckedException("deptCodes can't be null");
        }
        return AjaxResult.success(resourcePermsService.deptResources(v));
    }

    /**
     * 查询项目关联资源
     * @return
     */
    @InnerAuth
    @PostMapping("/v1/perms/projectResources")
    @Log(title = "数据权限", businessType = BusinessType.SELECT, desc = "获取项目资源")
    @ApiOperation(value = "获取项目资源", notes = "获取项目资源")
    public AjaxResult projectResources(@RequestBody ProjectVO v){
        if (StringUtils.isEmpty(v.getUserCode())) {
            throw new CheckedException("userCode can't be null");
        }
        if (StringUtils.isEmpty(v.getProjectCodes())) {
            throw new CheckedException("projectCodes can't be null");
        }
        return AjaxResult.success(resourcePermsService.projectResources(v));
    }


}
