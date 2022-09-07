package com.uuc.system.controller;

import com.uuc.common.core.constant.ClientConstants;
import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.constant.UserConstants;
import com.uuc.common.core.constant.VerifyConstants;
import com.uuc.common.core.domain.R;
import com.uuc.common.core.utils.DateUtils;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.core.utils.sign.RSAEncrypt;
import com.uuc.common.core.web.controller.BaseController;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.log.annotation.Log;
import com.uuc.common.log.enums.BusinessType;
import com.uuc.common.security.service.TokenService;
import com.uuc.common.security.utils.SecurityUtils;
import com.uuc.system.api.RemoteClientService;
import com.uuc.system.api.RemoteFileService;
import com.uuc.system.api.domain.UucModelInfo;
import com.uuc.system.api.model.LoginUser;
import com.uuc.system.api.model.UucLoginAccount;
import com.uuc.system.api.model.UucUserInfo;
import com.uuc.system.service.ISysConfigService;
import com.uuc.system.service.ISysUserService;
import com.uuc.system.uuc.service.IUucLoginAccountService;
import com.uuc.system.uuc.service.impl.UucUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

/**
 * 个人信息 业务处理
 * 
 * @author uuc
 */
@RestController
@RequestMapping("/userInfo/profile")
public class SysProfileController extends BaseController
{
    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private RemoteFileService remoteFileService;

    @Autowired
    private UucUserInfoService uucUserInfoService;

    @Autowired
    private RemoteClientService remoteClientService;

    @Autowired
    private IUucLoginAccountService loginAccountService;

    @Autowired
    private ISysConfigService sysConfigService;

    /**
     * 个人信息
     */
    @GetMapping
    public AjaxResult profile()
    {
//        String username = SecurityUtils.getUsername();
        UucUserInfo userInfo = SecurityUtils.getLoginUser().getUucUserInfo();
        AjaxResult ajax = AjaxResult.success(userInfo);
//        ajax.put("roleGroup", userService.selectUserRoleGroup(username));
//        ajax.put("postGroup", userService.selectUserPostGroup(username));
        return ajax;
    }

    /**
     * 本人修改本人用户信息
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateProfile(@RequestBody UucUserInfo uucUserInfo)
    {
        if (StringUtils.isNotEmpty(uucUserInfo.getPhone())
                && UserConstants.NOT_UNIQUE.equals(uucUserInfoService.checkPhoneUnique(uucUserInfo.getPhone())))
        {
            return AjaxResult.error("修改用户'" + uucUserInfo.getUserName() + "'失败，手机号码已存在");
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        UucUserInfo currentUser = loginUser.getUucUserInfo();
        uucUserInfo.setId(currentUser.getId());
        if (uucUserInfoService.updateUserProfile(uucUserInfo) > 0)
        {
            // 更新缓存用户信息
            loginUser.getUucUserInfo().setUserName(uucUserInfo.getUserName());
            loginUser.getUucUserInfo().setPhone(uucUserInfo.getPhone());
            loginUser.getUucUserInfo().setEmail(uucUserInfo.getEmail());
            loginUser.getUucUserInfo().setSex(uucUserInfo.getSex());
            tokenService.setLoginUser(loginUser);
            return AjaxResult.success();
        }
        return AjaxResult.error("修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public AjaxResult updatePwd(String oldPassword, String newPassword)
    {
        String oldPwd = descPassword(oldPassword);
        String newPwd= descPassword(newPassword);

        String username = SecurityUtils.getUsername();
        UucUserInfo uucUserInfo = uucUserInfoService.selectUucUserInfoByUserName(username);
        String password = uucUserInfo.getAccountList().get(0).getLoginPwd();
        if (!SecurityUtils.matchesPassword(oldPwd, password))
        {
            return AjaxResult.error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPwd, password))
        {
            return AjaxResult.error("新密码不能与旧密码相同");
        }
        String pattern = sysConfigService.selectConfigByKey(VerifyConstants.VERIFY_USER_LOGINPWD);
        Pattern r = Pattern.compile(pattern);
        if (!r.matcher(newPwd).matches()) {
            return AjaxResult.error("新密码必须包含大小写字母和数字的组合，可以使用特殊字符，长度在8-20之间");
        }
        if (uucUserInfoService.resetUserPwd(SecurityUtils.encryptPassword(newPwd)) > 0)
        {
            // 更新缓存用户密码
            LoginUser loginUser = SecurityUtils.getLoginUser();
            loginUser.getUucUserInfo().getAccountList().get(0).setLoginPwd(SecurityUtils.encryptPassword(newPwd));
            tokenService.setLoginUser(loginUser);
            // 设置账户状态
            String currentUserId = String.valueOf(loginUser.getUserid());
            UucLoginAccount uucLoginAccount = new UucLoginAccount();
            uucLoginAccount.setUserCode(currentUserId);
            uucLoginAccount.setUpdateBy(currentUserId);
            uucLoginAccount.setActive("Y");
            loginAccountService.updateUucLoginAccount(uucLoginAccount);
            return AjaxResult.success();
        }
        return AjaxResult.error("修改密码异常，请联系管理员");
    }

    /**
     * 密码解密
     * @param str
     * @return
     */
    private String descPassword(String str) {
        String result = "";
        // 密码解密
        R<UucModelInfo> uucModelInfoR = remoteClientService.checkClient(ClientConstants.MODEL_CODE_UUC, SecurityConstants.INNER);
        UucModelInfo uucModelInfo = uucModelInfoR.getData();
        if(uucModelInfo!=null){
            String privateKey=uucModelInfo.getClientRsaPrivate();
            try {
                result = RSAEncrypt.decryptPrivate(str, privateKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

//    /**
//     * 头像上传
//     */
//    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
//    @PostMapping("/avatar")
//    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws IOException
//    {
//        if (!file.isEmpty())
//        {
//            LoginUser loginUser = SecurityUtils.getLoginUser();
//            R<SysFile> fileResult = remoteFileService.upload(file);
//            if (StringUtils.isNull(fileResult) || StringUtils.isNull(fileResult.getData()))
//            {
//                return AjaxResult.error("文件服务异常，请联系管理员");
//            }
//            String url = fileResult.getData().getUrl();
//            if (userService.updateUserAvatar(loginUser.getUsername(), url))
//            {
//                AjaxResult ajax = AjaxResult.success();
//                ajax.put("imgUrl", url);
//                // 更新缓存用户头像
//                loginUser.getSysUser().setAvatar(url);
//                tokenService.setLoginUser(loginUser);
//                return ajax;
//            }
//        }
//        return AjaxResult.error("上传图片异常，请联系管理员");
//    }
}
