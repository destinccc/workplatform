package com.uuc.system.api;


import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.constant.ServiceNameConstants;
import com.uuc.common.core.domain.R;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.system.api.factory.RemoteCmdbFallbackFactory;
import com.uuc.system.api.model.cmdb.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(contextId = "remoteCmdbService", value = ServiceNameConstants.CMDB_SERVICE, fallbackFactory = RemoteCmdbFallbackFactory.class)
public interface RemoteCmdbService {

    public static String CMDB_SYNCAPI_PREFIX = "/api/uuc";

    // ------------ 项目 ----------------
    @GetMapping(value = CMDB_SYNCAPI_PREFIX +"/entitylist/project")
    @ApiOperation(value = "获取项目实例列表", notes = "获取项目实例列表")
    public AjaxResult getProjectEntityList(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @PostMapping(value = CMDB_SYNCAPI_PREFIX +"/project/save")
    @ApiOperation(value = "保存项目实例", notes = "保存项目实例")
    public void saveUucProject(@RequestBody  Map<String, Object> param,@RequestHeader(SecurityConstants.FROM_SOURCE) String inner);

    // ------------ 用户 ----------------
    @GetMapping(value = CMDB_SYNCAPI_PREFIX +"/entitylist/user")
    @ApiOperation(value = "获取用户实例列表", notes = "获取用户实例列表")
    public AjaxResult getUserEntityList(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @PostMapping(value = CMDB_SYNCAPI_PREFIX +"/user/save")
    @ApiOperation(value = "保存用户实例", notes = "保存项目实例")
    public void saveUucUser(@RequestBody Map<String, Object> param, @RequestHeader(SecurityConstants.FROM_SOURCE) String inner);

    // ------------ 组织 ----------------
    @GetMapping(value = CMDB_SYNCAPI_PREFIX +"/entitylist/dept")
    @ApiOperation(value = "获取组织实例列表", notes = "获取组织实例列表")
    public AjaxResult getDeptEntityList(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @PostMapping(value = CMDB_SYNCAPI_PREFIX +"/dept/save")
    @ApiOperation(value = "保存组织实例", notes = "保存组织实例")
    public void saveUucDept(@RequestBody Map<String, Object> param, @RequestHeader(SecurityConstants.FROM_SOURCE) String inner);

    // ------------ 统一删除 ----------------
    @PostMapping(value = CMDB_SYNCAPI_PREFIX +"/modelkey/delete")
    @ApiOperation(value = "根据模型标识及实例标识删除实例", notes = "根据模型标识及实例标识删除实例")
    public void deleteUucEntityByModelKey(@RequestBody  Map<String, Object> param,@RequestHeader(SecurityConstants.FROM_SOURCE) String inner);


    // ------------ 首页CMDB资源分类查询 ----------------
    @PostMapping(value = "/category/homePage")
    @ApiOperation(value = "cmdb模型分类首页", notes = "cmdb模型分类首页")
    public AjaxResult selectCmdbCategoryData(@RequestHeader(SecurityConstants.FROM_SOURCE) String inner);


    // ------------ 菜单查询模型id----------------
    @GetMapping(value = "/model/getModel/{menuId}")
    @ApiOperation(value = "获取模型标识", notes = "获取模型标识")
    public AjaxResult getModel(@PathVariable("menuId") Long menuId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);



    // ------------ cmdb同步资源----------------
    @GetMapping(value = "/data/start/{dataId}")
    @ApiOperation(value = "数据同步", notes = "数据同步")
    public AjaxResult startExtract(@PathVariable("dataId") Long dataId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);



    // -------------模型详情查询-------------
    @PostMapping(value = "/model/modelDetails")
    @ApiOperation(value = "查询模型详情", notes = "查询模型详情")
    public AjaxResult getModelDetails(@RequestBody SelectModelDto dto, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    // -------------查询用户所有资源-------------
    @PostMapping(value = "/api/perms/userResources")
    @ApiOperation(value = "查询用户所有资源", notes = "查询用户所有资源")
    public R<List<ResourceInfoDto>> userResources(@RequestBody UserPermsRequest request, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    // -------------查询用户所有资源-------------
    @PostMapping(value = "/api/perms/userDepts")
    @ApiOperation(value = "查询用户所有组织", notes = "查询用户所有组织")
    public R<List<DeptDto>> userDepts(@RequestBody UserDeptRequest request, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    // -------------查询用户关联的项目-------------
    @PostMapping(value = "/api/perms/userProjects")
    @ApiOperation(value = "查询用户关联的项目", notes = "查询用户关联的项目")
    public R<List<ProjectDto>> userProjects(@RequestBody UserProjectRequest request, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    // -------------查询组织关联的项目-------------
    @PostMapping(value = "/api/perms/deptProjects")
    @ApiOperation(value = "查询组织关联的项目", notes = "查询组织关联的项目")
    public R<List<ProjectDto>> deptProjects(@RequestBody DeptResourceRequest request, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    // -------------查询组织关联的资源-------------
    @PostMapping(value = "/api/perms/deptResources")
    @ApiOperation(value = "查询组织关联的资源", notes = "查询组织关联的资源")
    public R<List<ResourceInfoDto>> deptResources(@RequestBody DeptResourceRequest request, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    // -------------查询项目关联的资源-------------
    @PostMapping(value = "/api/perms/projectResources")
    @ApiOperation(value = "查询项目关联的资源", notes = "查询项目关联的资源")
    public R<List<ResourceInfoDto>> projectResources(@RequestBody ProjectResourceRequest request, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    //---------------查询组织/部门列表信息---------------
    @PostMapping(value = "/api/uuc/dept/deptList")
    @ApiOperation(value = "查询组织/部门列表信息", notes = "查询组织/部门列表信息")
    public R<List<DeptPo>> deptList(@RequestBody DeptPo deptPo, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    //---------------查询组织/部门下的项目列表信息---------------
    @PostMapping(value = "/api/uuc/dept/projectList")
    @ApiOperation(value = "查询组织/部门下的项目列表信息", notes = "查询组织/部门下的项目列表信息")
    public R<List<ProjectVo>> projectList(@RequestBody RequestBodyVo requestBodyVo, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    //---------------查询云平台资源状态---------------
    @PostMapping(value = "/api/uuc/dept/serverTotalInfo")
    @ApiOperation(value = "查询云平台资源状态", notes = "查询云平台资源状态")
    public R<ResourceInfo> serverTotalInfo(@RequestBody RequestBodyVo requestBodyVo, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    //---------------获取云平台的虚机详情列表---------------
    @PostMapping(value = "/api/uuc/dept/serverList")
    @ApiOperation(value = "获取云平台的虚机详情列表", notes = "获取云平台的虚机详情列表")
    public R<List<VirtualMachineInfo>> serverList(@RequestBody RequestBodyVo requestBodyVo, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    //---------------查询指定虚机监控信息---------------
    @GetMapping(value = "/api/uuc/dept/serverMonitorInfo")
    @ApiOperation(value = "查询指定虚机监控信息", notes = "查询指定虚机监控信息")
    public R<ServerInfo> serverMonitorInfo(@RequestParam("serverId") String serverId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    //---------------获取物理服务器列表---------------
    @PostMapping(value = "/api/uuc/dept/hostList")
    @ApiOperation(value = "获取物理服务器列表", notes = "获取物理服务器列表")
    public R<List<PhysicalMachineInfo>> physicalList(@RequestBody RequestBodyVo requestBodyVo, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    //---------------查询指定物理机监控信息---------------
    @GetMapping(value = "/api/uuc/dept/hostMonitorInfo")
    @ApiOperation(value = "查询指定物理机监控信息", notes = "查询指定物理机监控信息")
    public R<ServerInfo> hostMonitorInfo(@RequestParam("hostId") String hostId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    //---------------查询网络设备列表信息---------------
    @PostMapping(value = "/api/uuc/dept/networkDeviceList")
    @ApiOperation(value = "查询网络设备列表信息", notes = "查询网络设备列表信息")
    public R<List<NetworkDevice>> networkDeviceList(@RequestBody RequestBodyVo requestBodyVo, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    //---------------查询安全设备列表信息---------------
    @PostMapping(value = "/api/uuc/dept/securityDeviceList")
    @ApiOperation(value = "查询安全设备列表信息", notes = "查询安全设备列表信息")
    public R<List<NetworkDevice>> securityDeviceList(@RequestBody RequestBodyVo requestBodyVo, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
