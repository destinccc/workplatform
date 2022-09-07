package com.uuc.system.api;

import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.constant.ServiceNameConstants;
import com.uuc.common.core.domain.R;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.system.api.domain.UserAdminDeptVO;
import com.uuc.system.api.domain.UucUserVO;
import com.uuc.system.api.factory.RemoteSystemFallbackFactory;
import com.uuc.system.api.model.SysConfig;
import com.uuc.system.api.model.SysMenu;
import com.uuc.system.api.model.UucDeptInfo;
import com.uuc.system.api.model.UucUserInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * System服务
 */
@FeignClient(contextId = "remoteSystemService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteSystemFallbackFactory.class)
public interface RemoteSystemService {

    public static String UUC_SYNCAPI_PREFIX = "/api/system";

    // 钉钉组织 ------------------
    @GetMapping(value = UUC_SYNCAPI_PREFIX + "/dingtalkDept/{deptId}")
    @ApiOperation(value = "根据钉钉部门id查询组织信息", notes = "根据钉钉部门id查询组织信息")
    public AjaxResult selectDeptByDingtalkId(@PathVariable("deptId") String deptId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @ApiOperation(value = "保存钉钉组织", notes = "新增/更新钉钉组织")
    @PostMapping(value = UUC_SYNCAPI_PREFIX + "/dingtalkDept/saveDingDept")
    public void saveDingtalkDept(@RequestBody UucDeptInfo updateDept, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @ApiOperation(value = "获取存量钉钉部门", notes = "获取存量钉钉部门")
    @GetMapping(value = UUC_SYNCAPI_PREFIX + "/dingtalkDept/getDingDept")
    public AjaxResult getDingTalkDept(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @ApiOperation(value = "删除钉钉部门", notes = "删除钉钉部门")
    @PostMapping(value = UUC_SYNCAPI_PREFIX + "/dingtalkDept/deleteDingDept")
    public void deleteDingtalkDept(@RequestBody Map param, @RequestHeader(SecurityConstants.FROM_SOURCE) String inner);


    // 钉钉用户 ------------------
    @ApiOperation(value = "获取钉钉用户", notes = "获取钉钉用户")
    @GetMapping(value = UUC_SYNCAPI_PREFIX + "/dingtalkUser/{dingUid}}")
    public AjaxResult getDingTalkUser(@PathVariable("dingUid") String dingUid, @RequestHeader(SecurityConstants.FROM_SOURCE) String inner);

    @ApiOperation(value = "保存钉钉用户", notes = "保存钉钉用户")
    @PostMapping(value = UUC_SYNCAPI_PREFIX + "/dingtalkUser/saveDingUser")
    public void saveDingtalkUser(@RequestBody UucUserInfo updateUser, @RequestHeader(SecurityConstants.FROM_SOURCE) String inner);


    @ApiOperation(value = "获取存量钉钉用户", notes = "获取存量钉钉用户")
    @GetMapping(value = UUC_SYNCAPI_PREFIX + "/dingtalkUser/getDingUser")
    public AjaxResult getDingUser(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @ApiOperation(value = "删除钉钉用户", notes = "删除钉钉用户")
    @PostMapping(value = UUC_SYNCAPI_PREFIX + "/dingtalkUser/deleteDingUser")
    public void deleteDingUser(Map exsistsDingUserCodes, @RequestHeader(SecurityConstants.FROM_SOURCE) String inner);

    @ApiOperation(value = "更新钉钉用户", notes = "更新钉钉用户")
    @PostMapping(value = UUC_SYNCAPI_PREFIX + "/dingtalkUser/updateLeaderCode")
    public void syncUpdateUser(@RequestHeader(SecurityConstants.FROM_SOURCE) String inner);

    // 门户的项目 ------------------
    @ApiOperation(value = "获取门户项目", notes = "获取门户项目")
    @PostMapping(value = UUC_SYNCAPI_PREFIX + "/cmdb/projects")
    public AjaxResult getUucProjects(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    // 门户的用户
    @ApiOperation(value = "获取门户用户", notes = "获取门户用户")
    @PostMapping(value = UUC_SYNCAPI_PREFIX + "/cmdb/users")
    public AjaxResult getUucUsers(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    // 门户的组织
    @ApiOperation(value = "获取门户组织", notes = "获取门户组织")
    @PostMapping(value = UUC_SYNCAPI_PREFIX + "/cmdb/depts")
    public AjaxResult getUucDepts(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    // 获取默认的角色
    @ApiOperation(value = "获取默认的角色", notes = "获取默认的角色")
    @GetMapping(value = "/system/getDefaultRole")
    public AjaxResult getDefaultRole(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    // -------------------- CMDB数据仓库动态菜单功能迁移 --------------------

    /*
     * @Description  模型分类数据同步到菜单
     **/
    @PostMapping("/menu/sync")
    public AjaxResult sync(@Validated @RequestBody SysMenu sysMenu, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    /*
     * @Description   模型分类数据同步修改到菜单
     **/
    @PostMapping("/menu/syncUpdate")
    public AjaxResult syncUpdate(@RequestBody SysMenu sysMenu, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    /**
     * 删除分类数据时同步删除对应菜单
     */
    @GetMapping("/menu/syncDelete/{menuId}")
    public AjaxResult syncDelete(@PathVariable("menuId") Long menuId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    /**
     * 查询菜单信息
     */
    @GetMapping("/menu/listByCond")
    public AjaxResult listByCond(@RequestBody SysMenu sysMenuVo, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 查询组织列表
     */
    @GetMapping("/deptInfo/initUpdateDeptLevel")
    public AjaxResult initUpdateDeptLevel(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 新增配置项
     */
    @GetMapping("/config/updateByKey")
    public AjaxResult updateConfigByKey(@RequestBody SysConfig sysConfig, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 获取全量组织
     */
    @GetMapping("/deptInfo/selectAllDepts")
    public AjaxResult selectAllDepts(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 获取管理员id集合
     *
     * @param source 请求来源
     * @return 结果
     */
    @PostMapping("/userInfo/adminUserList")
    public AjaxResult getAdminUserList(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 获取管理员id集合
     *
     * @param source 请求来源
     * @return 结果
     */
    @PostMapping("/userDept/adminUserList")
    public AjaxResult getDeptAdminUserList(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 判断当前人是否是管理员角色或者组织负责人
     *
     * @param source 请求来源
     * @return 结果
     */
    @PostMapping("/userInfo/getAdminDeptList")
    public AjaxResult getAdminDeptList(@RequestBody UserAdminDeptVO vo, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    /**
     * 获取用户信息列表
     *
     * @param source 请求来源
     * @return 结果
     */
    @PostMapping("/userInfo/userList")
    public AjaxResult getUserList(@RequestBody UucUserVO vo, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    /**
     * 获取有效的API
     */
    @GetMapping("/version/api/effective")
    public R<List<String>> getEffectiveList(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    //-------------------------同步用户中心组织和用户---------------------------------------
    @ApiOperation(value = "新增/更新组织", notes = "新增/更新组织")
    @PostMapping(value = UUC_SYNCAPI_PREFIX + "/dept/saveDept")
    public void saveOrUpdateDept(@RequestBody UucDeptInfo updateDept, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @ApiOperation(value = "获取存量部门", notes = "获取存量部门")
    @GetMapping(value = UUC_SYNCAPI_PREFIX + "/dept/getDeptByDeptType/{deptType}")
    public AjaxResult getDeptByDeptType(@PathVariable("deptType") String deptType, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @ApiOperation(value = "通过用户Id和类型获取用户", notes = "通过用户Id和类型获取用户")
    @GetMapping(value = UUC_SYNCAPI_PREFIX + "/user/getUserByUserId/{userId}/{userType}")
    public AjaxResult getUserByUserId(@PathVariable("userId") String userId, @PathVariable("userType") String userType, @RequestHeader(SecurityConstants.FROM_SOURCE) String inner);

    @ApiOperation(value = "保存用户", notes = "保存用户")
    @PostMapping(value = UUC_SYNCAPI_PREFIX + "/user/saveUser")
    public void saveOrUpdateUser(@RequestBody UucUserInfo updateUser, @RequestHeader(SecurityConstants.FROM_SOURCE) String inner);

    @ApiOperation(value = "获取存量钉钉用户", notes = "获取存量钉钉用户")
    @GetMapping(value = UUC_SYNCAPI_PREFIX + "/user/getUserByUserType/{userType}")
    public AjaxResult getUserByUserType(@PathVariable("userType") String userType, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
