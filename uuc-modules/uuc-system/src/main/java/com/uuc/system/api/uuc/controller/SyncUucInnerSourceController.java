package com.uuc.system.api.uuc.controller;

import com.uuc.common.core.domain.R;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.security.annotation.InnerAuth;
import com.uuc.system.api.model.UucDeptInfo;
import com.uuc.system.api.model.UucUserInfo;
import com.uuc.system.api.uuc.dto.CmdbDeptDto;
import com.uuc.system.uuc.service.impl.UucDeptInfoService;
import com.uuc.system.uuc.service.impl.UucProjectService;
import com.uuc.system.uuc.service.impl.UucUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/system")
@Api(tags = "同步工作台资源")
@Slf4j
public class SyncUucInnerSourceController {

    @Autowired
    private UucDeptInfoService uucDeptInfoService;
    @Autowired
    private UucUserInfoService uucUserInfoService;
    @Autowired
    private UucProjectService uucProjectService;


    // --------------钉钉-DEPT同步到UUC--------------
    @InnerAuth
    @GetMapping("/dingtalkDept/{deptId}")
    @ApiOperation(value = "工作台查询钉钉Org", notes = "工作台查询钉钉Org")
    public AjaxResult getByDeptId(@PathVariable String deptId) {
        return AjaxResult.success(uucDeptInfoService.selectByDingtalkId(deptId));
    }

    @InnerAuth
    @PostMapping("/dingtalkDept/saveDingDept")
    @ApiOperation(value = "工作台保存钉钉Org", notes = "工作台保存钉钉Org")
    public void saveDingDept(@RequestBody UucDeptInfo uucDeptInfo) {
        uucDeptInfoService.saveDingDept(uucDeptInfo);
    }

    @InnerAuth
    @GetMapping("/dingtalkDept/getDingDept")
    @ApiOperation(value = "获取存量钉钉部门", notes = "获取存量钉钉部门")
    public AjaxResult getDingDept() {
        return AjaxResult.success(uucDeptInfoService.getDingDept());
    }

    @InnerAuth
    @PostMapping("/dingtalkDept/deleteDingDept")
    @ApiOperation(value = "删除钉钉部门", notes = "删除钉钉部门")
    public void deleteDingDept(@RequestBody Map param) {
        uucDeptInfoService.deleteDingDept(param);
    }


    // --------------UUC-DEPT同步到CMDB--------------
    @InnerAuth
    @PostMapping("/cmdb/depts")
    @ApiOperation(value = "获取CMDB需要的全量组织信息", notes = "获取CMDB需要的全量组织信息")
    public AjaxResult getWholeDepts() {
        return AjaxResult.success(uucDeptInfoService.getWholeDept());
    }


    // --------------钉钉-USER同步到UUC--------------
    @InnerAuth
    @GetMapping("/dingtalkUser/{dingUid}}")
    @ApiOperation(value = "查询钉钉用户", notes = "查询钉钉用户")
    public AjaxResult getDingUserInfo(@PathVariable String dingUid) {
        return AjaxResult.success(uucUserInfoService.getDingUserInfo(dingUid));
    }

    @InnerAuth
    @PostMapping("/dingtalkUser/saveDingUser")
    @ApiOperation(value = "保存钉钉用户", notes = "保存钉钉用户")
    public AjaxResult saveDingUser(@RequestBody UucUserInfo uucUserInfo) {
        uucUserInfoService.saveDingUser(uucUserInfo);
        return AjaxResult.success();
    }

    @InnerAuth
    @GetMapping("/dingtalkUser/getDingUser")
    @ApiOperation(value = "查询存量钉钉用户", notes = "查询存量钉钉用户")
    public AjaxResult getDingUser() {
        return AjaxResult.success(uucUserInfoService.getDingUser());
    }

    @InnerAuth
    @PostMapping("/dingtalkUser/deleteDingUser")
    @ApiOperation(value = "删除钉钉用户", notes = "删除钉钉用户")
    public void deleteDingUser(@RequestBody Map deleteUserCodes) {
        uucUserInfoService.deleteDingUser(deleteUserCodes);
    }

    @InnerAuth
    @PostMapping("/dingtalkUser/updateLeaderCode")
    @ApiOperation(value = "更新钉钉用户直接领导", notes = "更新钉钉用户直接领导")
    public void updateLeaderCode() {
        uucUserInfoService.updateLeaderCode();
    }

    // --------------UUC-USER同步到CMDB--------------
    @InnerAuth
    @PostMapping("/cmdb/users")
    @ApiOperation(value = "获取CMDB需要的全量用户信息", notes = "获取CMDB需要的全量用户信息")
    public AjaxResult getWholeUsers() {
        return AjaxResult.success(uucUserInfoService.getWholeUser());
    }


    @InnerAuth
    @PostMapping("/cmdb/projects")
    @ApiOperation(value = "获取CMDB需要的全量项目信息", notes = "获取CMDB需要的项目信息")
    public AjaxResult getWholeProjects() {
        return AjaxResult.success(uucProjectService.getWholeProjects());
    }


    @InnerAuth
    @GetMapping("/homepage/depts")
    @ApiOperation(value = "获取CMDB首页所需的组织列表", notes = "获取CMDB首页所需的组织列表")
    public R<List<CmdbDeptDto>> cmdbHomepage() {
        return R.ok(uucDeptInfoService.cmdbDeptList());
    }


    @InnerAuth
    @PostMapping("/dept/saveDept")
    @ApiOperation(value = "工作台保存部门", notes = "工作台保存部门")
    public void saveOrUpdateDept(@RequestBody UucDeptInfo uucDeptInfo) {
        uucDeptInfoService.saveOrUpdateDept(uucDeptInfo);
    }

    @InnerAuth
    @GetMapping("/dept/getDeptByDeptType/{deptType}")
    @ApiOperation(value = "获取存量部门", notes = "获取存量部门")
    public AjaxResult getDeptByDeptType(@PathVariable String deptType) {
        return AjaxResult.success(uucDeptInfoService.getDeptByDeptType(deptType));
    }

    @InnerAuth
    @GetMapping("/user/getUserByUserId/{userId}/{userType}")
    @ApiOperation(value = "通过用户Id和类型获取用户", notes = "通过用户Id和类型获取用户")
    public AjaxResult getUserByPhone(@PathVariable String userId, @PathVariable String userType) {
        return AjaxResult.success(uucUserInfoService.getUserByUserId(userId, userType));
    }

    @InnerAuth
    @PostMapping("/user/saveUser")
    @ApiOperation(value = "工作台保存用户", notes = "工作台保存用户")
    public void saveUser(@RequestBody UucUserInfo uucUserInfo) {
        uucUserInfoService.saveUser(uucUserInfo);
    }

    @InnerAuth
    @GetMapping("/user/getUserByUserType/{userType}")
    @ApiOperation(value = "获取存量人员", notes = "获取存量人员")
    public AjaxResult getUserByUserType(@PathVariable String userType) {
        return AjaxResult.success(uucUserInfoService.getUserByUserType(userType));
    }

}
