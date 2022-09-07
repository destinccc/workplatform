package com.uuc.system.uuc.controller;

import com.uuc.common.core.constant.ClientConstants;
import com.uuc.common.core.constant.UserConstants;
import com.uuc.common.core.constant.VerifyConstants;
import com.uuc.common.core.domain.R;
import com.uuc.common.core.utils.LongUtils;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.core.utils.sign.RSAEncrypt;
import com.uuc.common.core.web.controller.BaseController;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.core.web.page.TableDataInfo;
import com.uuc.common.log.annotation.Log;
import com.uuc.common.log.enums.BusinessType;
import com.uuc.common.security.annotation.InnerAuth;
import com.uuc.common.security.annotation.RequiresPermissions;
import com.uuc.common.security.auth.AuthUtil;
import com.uuc.common.security.utils.SecurityUtils;
import com.uuc.system.api.domain.SysRole;
import com.uuc.system.api.domain.UserAdminDeptVO;
import com.uuc.system.api.domain.UucModelInfo;
import com.uuc.system.api.model.LoginUser;
import com.uuc.system.api.model.UucLoginAccount;
import com.uuc.system.api.model.UucUserInfo;
import com.uuc.system.service.ISysConfigService;
import com.uuc.system.service.ISysPermissionService;
import com.uuc.system.service.ISysRoleService;
import com.uuc.system.uuc.domain.SignatureUserVo;
import com.uuc.system.uuc.service.IUucLoginAccountService;
import com.uuc.system.uuc.service.IUucModelInfoService;
import com.uuc.system.uuc.service.impl.UucUserDeptService;
import com.uuc.system.uuc.service.impl.UucUserInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 用户信息Controller
 *
 * @author uuc
 * @date 2022-04-01
 */
@RestController
@RequestMapping("/userInfo")
public class UucUserInfoController extends BaseController {
    @Autowired
    private UucUserInfoService uucUserInfoService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysPermissionService permissionService;

    @Autowired
    private IUucLoginAccountService uucLoginAccountService;

    @Autowired
    private UucUserDeptService uucUserDeptService;

    @Autowired
    private IUucModelInfoService uucModelInfoService;

    @Autowired
    private ISysConfigService sysConfigService;

    /**
     * 查询用户信息列表
     */
    @RequiresPermissions("system:userInfo:list")
    @GetMapping("/list")
    public TableDataInfo list(UucUserInfo uucUserInfo) {
        startPage();
        List<UucUserInfo> list = uucUserInfoService.selectUucUserInfoList(uucUserInfo);
        return getDataTable(list);
    }

    /**
     * 查询所有用户信息列表
     */
    @InnerAuth
    @GetMapping("/listAllUser")
    public AjaxResult listAllUser()
    {
        List<SignatureUserVo> list = uucUserInfoService.listAllUser();
        return AjaxResult.success(list);
    }

//    /**
//     * 查询用户信息列表
//     */
//    @PostMapping("/listAPI")
//    public AjaxResult listAPI(@RequestBody UucUserVO vo)
//    {
//        startPage();
//        UucUserInfo userInfo = new UucUserInfo();
//        List<UucUserInfo> list = uucUserInfoService.selectUucUserInfoList(userInfo);
//        return AjaxResult.success(list);
//    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        Long userCode = SecurityUtils.getUserId();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(userCode);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(userCode);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", uucUserInfoService.selectUucUserInfoById(userCode));
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 根据用户编号获取详细信息
     */
    @RequiresPermissions("system:userInfo:query")
    @GetMapping(value = {"/", "/{userId}"})
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        AjaxResult ajax = AjaxResult.success();

        List<SysRole> roles = null;
        if (LongUtils.isEmpty(userId)) {
            // 不知道哪里有调用，备用
            ajax.put("roles", roleService.selectRoleAll().stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        } else {
            if (UucUserInfo.isAdmin(userId)) {
                roles = roleService.selectRoleAll();
            } else {
                roles = roleService.selectRolesByUserId(userId).stream().filter(SysRole::isFlag).collect(Collectors.toList());
            }
            ajax.put("roles", roles);
        }
        if (StringUtils.isNotNull(userId)) {
            UucUserInfo uucUserInfo = uucUserInfoService.selectUucUserInfoById(userId);

            ajax.put(AjaxResult.DATA_TAG, uucUserInfo);
            ajax.put("roleIds", roles.stream().map(SysRole::getRoleId).collect(Collectors.toList()));
        }
        return ajax;
    }

    /**
     * 根据用户编号获取详细信息
     * (外部接口查询用户个人信息)
     */
    @GetMapping(value = {"/query/{userId}"})
    public AjaxResult getUserInfo(@PathVariable(value = "userId", required = true) Long userId) {
        UucUserInfo uucUserInfo = uucUserInfoService.selectUucUserInfoById(userId);
        return AjaxResult.success(uucUserInfo);
    }

    /**
     * 新增用户信息
     */
    @RequiresPermissions("system:userInfo:add")
    @Log(title = "用户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UucUserInfo uucUserInfo) {
        List<UucLoginAccount> accountList = uucUserInfo.getAccountList();
        String[] acctNames = accountList.stream().map(UucLoginAccount::getLoginAcct).collect(Collectors.toList()).toArray(new String[accountList.size()]);
        if (UserConstants.NOT_UNIQUE.equals(uucLoginAccountService.checkLoginAcctUnique(acctNames))) {
            return AjaxResult.error("新增用户'" + uucUserInfo.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(uucUserInfo.getPhone())
                && uucUserInfoService.checkPhoneUnique(uucUserInfo.getPhone()) >= 1) {
            return AjaxResult.error("新增用户'" + uucUserInfo.getUserName() + "'失败，手机号码已存在");
        }

        String identity = uucUserInfo.getIdentityCard();
        if (StringUtils.isNotEmpty(identity) && identity.length() > 18) {
            return AjaxResult.error("新增用户'" + uucUserInfo.getUserName() + "'失败，身份证号码超长");
        }

        String pattern1 = sysConfigService.selectConfigByKey(VerifyConstants.VERIFY_USER_LOGINACCT);
        Pattern r1 = Pattern.compile(pattern1);
        String pattern2 = sysConfigService.selectConfigByKey(VerifyConstants.VERIFY_USER_LOGINPWD);
        Pattern r2 = Pattern.compile(pattern2);

        for (UucLoginAccount loginAccount : accountList) {
            if (!r1.matcher(loginAccount.getLoginAcct()).matches()) {
                return AjaxResult.error("新增用户'" + uucUserInfo.getUserName() + "'失败，账号[手机号]校验失败");
            }
            String password = descPassword(loginAccount.getLoginPwd());
            if (!r2.matcher(password).matches()) {
                return AjaxResult.error("新增用户'" + uucUserInfo.getUserName() + "'失败，账户密码必须包含大小写字母和数字的组合，可以使用特殊字符，长度在8-20之间");
            }
            loginAccount.setLoginPwd(password);
        }
        uucUserInfo.setAccountList(accountList);
        return toAjax(uucUserInfoService.insertUucUserInfo(uucUserInfo));
    }

    /**
     * 密码解密
     *
     * @param str
     * @return
     */
    private String descPassword(String str) {
        String result = "";
        // 密码解密
        UucModelInfo uucModelInfo = uucModelInfoService.selectUucModelInfoByCLientId(ClientConstants.MODEL_CODE_UUC);
        if (uucModelInfo != null) {
            String privateKey = uucModelInfo.getClientRsaPrivate();
            try {
                result = RSAEncrypt.decryptPrivate(str, privateKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 修改用户信息
     */
    @RequiresPermissions("system:userInfo:edit")
    @Log(title = "用户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UucUserInfo uucUserInfo) {
        List<UucLoginAccount> accountList = uucUserInfo.getAccountList();
        String[] acctNames = accountList.stream().map(UucLoginAccount::getLoginAcct).collect(Collectors.toList()).toArray(new String[accountList.size()]);
        if (UserConstants.NOT_UNIQUE.equals(uucLoginAccountService.checkLoginAcctUnique(acctNames))) {
            return AjaxResult.error("修改用户'" + uucUserInfo.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(uucUserInfo.getPhone())
                && UserConstants.NOT_UNIQUE.equals(uucUserInfoService.checkPhoneUnique(uucUserInfo.getPhone()))) {
            return AjaxResult.error("修改用户'" + uucUserInfo.getUserName() + "'失败，手机号码已存在");
        }
        uucUserInfoService.checkUserAllowed(uucUserInfo);
        return toAjax(uucUserInfoService.updateUucUserInfo(uucUserInfo));
    }

    /**
     * 删除用户信息
     */
    @RequiresPermissions("system:userInfo:remove")
    @Log(title = "用户信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        uucUserInfoService.checkUserAllowed(ids);
        return toAjax(uucUserInfoService.deleteUucUserInfoByIds(ids));
    }

    /**
     * 状态修改
     */
    @RequiresPermissions("system:userInfo:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody UucUserInfo uucUserInfo) {
        uucUserInfoService.checkUserAllowed(uucUserInfo);
        uucUserInfo.setUpdateBy(String.valueOf(SecurityUtils.getUserId()));
        return toAjax(uucUserInfoService.updateUserStatus(uucUserInfo));
    }


    /**
     * 获取当前用户信息
     */
    @InnerAuth
    @GetMapping("/info/{username}")
    public R<LoginUser> info(@PathVariable("username") String username) {
        UucUserInfo uucUserInfo = uucUserInfoService.selectUucUserInfoByUserName(username);
        if (StringUtils.isNull(uucUserInfo)) {
            return R.fail("用户名或密码错误");
        }
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(uucUserInfo.getId());
        uucUserInfo.setRoles(
                roleService.selectRolesByUserId(uucUserInfo.getId())
                        .stream().filter(SysRole::isFlag).collect(Collectors.toList())
        );
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(uucUserInfo.getId());
        LoginUser loginUser = new LoginUser();
        loginUser.setUserid(uucUserInfo.getId());
        loginUser.setUsername(uucUserInfo.getAccountList().get(0).getLoginAcct());
        loginUser.setUsernameZh(uucUserInfo.getUserName());
        loginUser.setUucUserInfo(uucUserInfo);
        loginUser.setRoles(roles);
        loginUser.setPermissions(permissions);
        return R.ok(loginUser);
    }

    /**
     * 获取当前用户信息
     */
    @InnerAuth
    @GetMapping("/uid/{userId}")
    public R<LoginUser> getByUid(@PathVariable("userId") String userId) {
        UucUserInfo uucUserInfo = uucUserInfoService.selectUucUserInfoByUid(userId);
        if (StringUtils.isNull(uucUserInfo)) {
            return R.fail("用户名或密码错误");
        }
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(uucUserInfo.getId());
        uucUserInfo.setRoles(
                roleService.selectRolesByUserId(uucUserInfo.getId())
                        .stream().filter(SysRole::isFlag).collect(Collectors.toList())
        );
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(uucUserInfo.getId());
        LoginUser loginUser = new LoginUser();
        loginUser.setUserid(uucUserInfo.getId());
        loginUser.setUsername(uucUserInfo.getAccountList().get(0).getLoginAcct());
        loginUser.setUsernameZh(uucUserInfo.getUserName());
        loginUser.setUucUserInfo(uucUserInfo);
        loginUser.setRoles(roles);
        loginUser.setPermissions(permissions);
        return R.ok(loginUser);
    }

    /**
     * 重置密码
     */
    @RequiresPermissions("system:userInfo:resetPwd")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody UucLoginAccount loginAccount) {
        if (LongUtils.isEmpty(loginAccount.getId())) {
            return AjaxResult.error("请求参数错误！");
        }
        UucUserInfo userInfo = new UucUserInfo();
        userInfo.setId(Long.valueOf(loginAccount.getId()));
        // uucUserInfoService.checkUserAllowed(userInfo);

        try {
            loginAccount.setLoginPwd(descPassword(loginAccount.getLoginPwd()));
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("密码解密失败！");
        }
        return toAjax(uucLoginAccountService.resetPwd(loginAccount));
    }

    /**
     * 根据用户编号获取授权角色
     */
    @RequiresPermissions("system:userInfo:query")
    @GetMapping("/authRole/{userId}")
    public AjaxResult authRole(@PathVariable("userId") String userId) {
        AjaxResult ajax = AjaxResult.success();
        Long userCode = Long.valueOf(userId);
        UucUserInfo uucUserInfo = uucUserInfoService.selectUucUserInfoById(userCode);
        List<SysRole> roles = roleService.selectRolesByUserId(userCode);
        ajax.put("user", uucUserInfo);
        ajax.put("roles", UucUserInfo.isAdmin(userCode) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        return ajax;
    }

    /**
     * 用户授权角色
     */
    @RequiresPermissions("system:userInfo:edit")
    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PutMapping("/authRole")
    public AjaxResult insertAuthRole(Long userId, Long[] roleIds) {
        uucUserInfoService.insertUserAuth(userId, roleIds);
        return success();
    }


    @InnerAuth
    @PostMapping("/loginUser/{token}")
    @ApiOperation(value = "获取登录用户信息", notes = "获取登录用户信息")
    public AjaxResult getloginUser(@PathVariable("token") String token) {
        if (StringUtils.isBlank(token)) {
            token = SecurityUtils.getToken();
        }
        return AjaxResult.success(AuthUtil.getLoginUser(token));
    }

    @InnerAuth
    @PostMapping("/adminUserList")
    @ApiOperation(value = "获取登录用户信息", notes = "获取登录用户信息")
    public AjaxResult getAdminUserList() {
        return AjaxResult.success(uucUserInfoService.getAdminUserList());
    }

    @InnerAuth
    @PostMapping("/getAdminDeptList")
    @ApiOperation(value = "根据人员筛选管理组织", notes = "根据人员筛选管理组织")
    public AjaxResult getAdminDeptList(@RequestBody UserAdminDeptVO vo) {
        return AjaxResult.success(uucUserInfoService.getAdminDeptList(vo));
    }

    @PostMapping("/sync/{userType}")
    @ApiOperation(value = "根据用户类型同步数据", notes = "根据用户类型同步数据")
    public AjaxResult syncUserByUserType(@PathVariable("userType") String userType) {
        return AjaxResult.success(uucUserInfoService.syncUserByUserType(userType));
    }

}
