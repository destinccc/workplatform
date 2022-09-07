package com.uuc.system.uuc.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.constant.SyncConstants;
import com.uuc.common.core.enums.SyncBeanType;
import com.uuc.common.core.enums.UucUserType;
import com.uuc.common.core.exception.ServiceException;
import com.uuc.common.core.utils.DateUtils;
import com.uuc.common.core.utils.LongUtils;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.core.utils.uuid.IdUtils;
import com.uuc.common.security.utils.SecurityUtils;
import com.uuc.system.api.RemoteXxlJobService;
import com.uuc.system.api.domain.SysRole;
import com.uuc.system.api.domain.UserAdminDeptVO;
import com.uuc.system.api.model.UucDeptInfo;
import com.uuc.system.api.model.UucLoginAccount;
import com.uuc.system.api.model.UucUserDept;
import com.uuc.system.api.model.UucUserInfo;
import com.uuc.system.domain.SysUserRole;
import com.uuc.system.mapper.SysRoleMapper;
import com.uuc.system.mapper.SysUserRoleMapper;
import com.uuc.system.service.ISysConfigService;
import com.uuc.system.service.ISysRoleService;
import com.uuc.system.uuc.domain.SignatureUserVo;
import com.uuc.system.uuc.mapper.*;
import com.uuc.system.uuc.sync.service.ToSyncBodyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户信息Service业务层处理
 *
 * @author uuc
 * @date 2022-04-01
 */
@Service
@Slf4j
public class UucUserInfoService {
    @Autowired
    private UucUserInfoMapper uucUserInfoMapper;

    @Autowired
    private UucLoginAccountMapper uucLoginAccountMapper;

    @Autowired
    private UucUserDeptMapper uucUserDeptMapper;

    @Autowired
    private UucProjectUserService projectUserService;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private UucDeptInfoMapper deptInfoMapper;

    @Autowired
    private UucDeptInfoService deptInfoService;

    @Autowired
    private UucProjectUserMapper projectUserMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ToSyncBodyService toSyncBodyService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private RemoteXxlJobService remoteXxlJobService;

    /**
     * 查询用户信息
     *
     * @param id 用户信息主键
     * @return 用户信息
     */

    public UucUserInfo selectUucUserInfoById(Long id) {
        UucLoginAccount uucLoginAccount = new UucLoginAccount();
        uucLoginAccount.setUserCode(String.valueOf(id));
        List<UucLoginAccount> accountList = uucLoginAccountMapper.selectUucLoginAccountList(uucLoginAccount);

        UucUserInfo uucUserInfo = uucUserInfoMapper.selectUucUserInfoById(id);
        uucUserInfo.setAccountList(accountList);

        List<UucUserDept> deptList = uucUserDeptMapper.selectUucUserDeptByUserCode(String.valueOf(id));
        uucUserInfo.setDeptList(deptList);

        List<SysRole> userRoles = roleMapper.selectRolePermissionByUserId(id);
        uucUserInfo.setRoles(userRoles);
        return uucUserInfo;
    }

    /**
     * 查询用户信息列表
     *
     * @param uucUserInfo 用户信息
     * @return 用户信息
     */

    public List<UucUserInfo> selectUucUserInfoList(UucUserInfo uucUserInfo) {
        return uucUserInfoMapper.selectUucUserInfoList(uucUserInfo);
    }

    /**
     * 查询用户信息列表
     *
     */

    public List<SignatureUserVo> listAllUser() {
        return uucUserInfoMapper.listAllUser();
    }

    /**
     * 新增用户信息
     *
     * @param uucUserInfo 用户信息
     * @return 结果
     */

    @Transactional(rollbackFor = Exception.class)
    public int insertUucUserInfo(UucUserInfo uucUserInfo) {
        Date nowData = DateUtils.getNowDate();
        String operator = String.valueOf(SecurityUtils.getUserId());

        uucUserInfo.setCreateTime(nowData);
        uucUserInfo.setId(IdUtils.snowflakeId());
        uucUserInfo.setUserType("00");
        // 插入账号表
        List<UucLoginAccount> accountList = uucUserInfo.getAccountList();
        if (CollectionUtils.isNotEmpty(accountList)) {
            for (UucLoginAccount loginAccount : accountList) {
                loginAccount.setLoginPwd(SecurityUtils.encryptPassword(loginAccount.getLoginPwd()));
                loginAccount.setUserCode(String.valueOf(uucUserInfo.getId()));
                loginAccount.setAccountExpiredStatus("N");
                loginAccount.setPasswdExpiredStatus("N");
                loginAccount.setAccountLockedStatus("N");
//                loginAccount.setActive("N");
//                loginAccount.setEnabled("Y");
                Date date = DateUtils.parseDate("2099-01-01 12:00:00");
                loginAccount.setPwdValidPeriodDate(date);
                loginAccount.setAcctValidPeriodDate(date);
                loginAccount.setStatus("0");
                loginAccount.setDelFlag("0");
                loginAccount.setCreateBy(operator);
                loginAccount.setCreateTime(nowData);
            }
            uucLoginAccountMapper.insertUucLoginAccountBatch(accountList);
        }

        // 插入角色关系表
        Long[] roleIds = uucUserInfo.getRoleIds();
        if (roleIds.length > 0) {
            List<SysUserRole> userRoleList = new ArrayList<>();
            for (Long roleId : roleIds) {
                SysUserRole userRole = new SysUserRole();
                userRole.setRoleId(roleId);
                userRole.setUserId(uucUserInfo.getId());
                userRoleList.add(userRole);
            }
            userRoleMapper.batchUserRole(userRoleList);
        }

        // 插入组织关系表
//        UucUserDept userDept = new UucUserDept();
//        userDept.setUserCode();
//        userDept.setDeptCode(uucUserInfo.getDeptCode());
//        uucUserDeptMapper.insertUucUserDept(userDept);

        // 插入组织关系表
        String userId = String.valueOf(uucUserInfo.getId());
        List<UucUserDept> deptList = uucUserInfo.getDeptList();
        for (UucUserDept userDept : deptList) {
            userDept.setUserCode(userId);
            userDept.setAdminFlag("0");
            userDept.setCreateTime(nowData);
            userDept.setCreateBy(operator);
        }
        uucUserDeptMapper.batchInsertUucUserDept(deptList);
        int i = uucUserInfoMapper.insertUucUserInfo(uucUserInfo);
        if (i > 0) {
            toSyncBodyService.insertSyncBody(uucUserInfo, SyncBeanType.USERSYNCBEAN.getCode());
        }
        // 插入用户表
        return i;
    }

    /**
     * 修改用户信息
     *
     * @param uucUserInfo 用户信息
     * @return 结果
     */

    @Transactional(rollbackFor = Exception.class)
    public int updateUucUserInfo(UucUserInfo uucUserInfo) {
        uucUserInfo.setUpdateTime(DateUtils.getNowDate());
        // 更新部门、角色
        Date date = DateUtils.getNowDate();
        String userCode = String.valueOf(uucUserInfo.getId());
        UucUserInfo oldUser = uucUserInfoMapper.selectUucUserInfoById(uucUserInfo.getId());
        // 先查询数据做备份,再删再增
//        List<UucUserDept> deptList = uucUserDeptMapper.selectUucUserDeptByUserCode(userCode);
        // 组织更新
        List<UucUserDept> userDeptList = uucUserInfo.getDeptList();
        uucUserDeptMapper.deleteUucUserDeptByUserCode(userCode);
        if (CollectionUtils.isNotEmpty(userDeptList)) {
            for (UucUserDept uucUserDept : userDeptList) {
                uucUserDept.setUserCode(userCode);
                uucUserDept.setUpdateBy(String.valueOf(SecurityUtils.getUserId()));
                uucUserDept.setUpdateTime(date);
            }
            uucUserDeptMapper.batchInsertUucUserDept(userDeptList);
        }

        // 角色更新
        Long[] roleIds = uucUserInfo.getRoleIds();
        if (LongUtils.isNotEmpty(roleIds)) {
            userRoleMapper.deleteUserRoleByUserId(uucUserInfo.getId());
            List<SysUserRole> userRoleList = new ArrayList<>();
            for (Long roleId : roleIds) {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(uucUserInfo.getId());
                userRole.setRoleId(roleId);
                userRoleList.add(userRole);
            }
            userRoleMapper.batchUserRole(userRoleList);
        }
        // 更新账户表
        UucLoginAccount loginAccount = uucLoginAccountMapper.selectUucLoginAccountByCode(uucUserInfo.getId());
        loginAccount.setUpdateBy(String.valueOf(SecurityUtils.getUserId()));
        loginAccount.setUpdateTime(date);
        loginAccount.setActive(uucUserInfo.getAccountList().get(0).getActive());
        loginAccount.setEnabled(uucUserInfo.getAccountList().get(0).getEnabled());
        uucLoginAccountMapper.updateUucLoginAccount(loginAccount);
        int i = uucUserInfoMapper.updateUucUserInfo(uucUserInfo);
        if (i > 0) {
            toSyncBodyService.updateSyncBody(oldUser, uucUserInfo, SyncBeanType.USERSYNCBEAN.getCode());
        }
        return i;
    }

    /**
     * 批量删除用户信息
     *
     * @param ids 需要删除的用户信息主键
     * @return 结果
     */

    public int deleteUucUserInfoByIds(Long[] ids) {
        // 更新账号状态
        uucLoginAccountMapper.deleteUucLoginAccountByIds(ids);
        // 删除人员组织关系
        uucUserDeptMapper.deleteUucUserDeptByUserCodes(LongUtils.caseLongArr2StringArr(ids));
        // 删除人员角色关系
        userRoleMapper.deleteUserRole(ids);
        // 删除人员项目关系
        projectUserMapper.deleteUucProjectUserByUserCodes(LongUtils.caseLongArr2StringArr(ids));
        // 更新状态
        // 钉钉同步用户直接物理删除
        List<UucUserInfo> dingUserInfoLists = selectDingtalkUserList();
        List<Long> deleteDingUser = Lists.newArrayList();
        Set<String> deleteIdStrs = Arrays.asList(ids).stream().map(String::valueOf).collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(dingUserInfoLists)) {
            for (UucUserInfo dingUserTmp : dingUserInfoLists) {
                Long id = dingUserTmp.getId();
                if (!deleteIdStrs.add(String.valueOf(id))) {
                    deleteDingUser.add(id);
                }
            }
//            deleteDingUser = dingUserInfoLists.stream().filter(item -> deleteIds.contains(item.getId())).map(UucUserInfo::getId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(deleteDingUser)) {
                // 删除账号
                uucLoginAccountMapper.deleteDingAccountByIds(deleteDingUser);
                // 删除用户
                uucUserInfoMapper.deleteDingUucUserInfoByIds(deleteDingUser);
            }
        }
        int i = uucUserInfoMapper.deleteUucUserInfoByIds(ids);
        if (i > 0) {
            List<String> idList = new ArrayList<>();
            for (Long item : ids) {
                idList.add(String.valueOf(item));
            }
            toSyncBodyService.deleteSyncBody(idList, SyncBeanType.USERSYNCBEAN.getCode());
        }
        return 1;
    }

    /**
     * 删除用户信息信息
     *
     * @param id 用户信息主键
     * @return 结果
     */

    public int deleteUucUserInfoById(Long id) {
        int i = uucUserInfoMapper.deleteUucUserInfoById(id);
        if (i > 0) {
            List<String> idList = new ArrayList<>();
            if (id != null) {
                idList.add(String.valueOf(id));
            }
            toSyncBodyService.deleteSyncBody(idList, SyncBeanType.USERSYNCBEAN.getCode());
        }
        return i;
    }

    /**
     * 校验用户是否允许操作
     *
     * @param uucUserInfo 用户信息
     */

    public void checkUserAllowed(UucUserInfo uucUserInfo) {
        if (StringUtils.isNotNull(uucUserInfo.getId()) && uucUserInfo.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    /**
     * 校验用户是否允许操作
     *
     * @param ids 用户信息
     */

    public void checkUserAllowed(Long[] ids) {
        if (ids != null && ids.length > 0) {
            for (Long id : ids) {
                if (id != null && id == 1) {
                    throw new ServiceException("不允许操作超级管理员用户");
                }
            }
        }
    }

    /**
     * 修改用户状态
     *
     * @param uucUserInfo 用户信息
     * @return 结果
     */

    public int updateUserStatus(UucUserInfo uucUserInfo) {
        return uucUserInfoMapper.updateUucUserInfo(uucUserInfo);
    }

    public UucUserInfo getDingUserInfo(String dingUid) {
        UucUserInfo userInfo = uucUserInfoMapper.selectUucUserInfoByDingUid(dingUid);
        if (Objects.isNull(userInfo)) {
            return userInfo;
        }
        // userCode是钉钉的uid
        List<UucUserDept> uucUserDept = uucUserDeptMapper.selectUucUserDeptByUserCode(String.valueOf(userInfo.getId()));
        List<UucUserDept> dingtalkRels = Lists.newArrayList();
        for (UucUserDept userDept : uucUserDept) {
            // 转换为钉钉的用户id,部门id
            UucUserDept ding = new UucUserDept();
            ding.setAdminFlag(userDept.getAdminFlag());
            ding.setUserCode(String.valueOf(userInfo.getId()));
            ding.setDeptCode(userDept.getDeptCode());
            //钉钉的部门id就是dept_code
            dingtalkRels.add(ding);
        }
        userInfo.setDeptList(dingtalkRels);
        // leaderCode 需要由用户id转换为钉钉的用户id再比较
        String leaderCode = userInfo.getLeaderCode();
        if (StringUtils.isNotBlank(leaderCode)) {
            UucUserInfo leaderUser = uucUserInfoMapper.selectUucUserInfoById(Long.parseLong(leaderCode));
            if (Objects.nonNull(leaderUser)) {
                userInfo.setLeaderCode(leaderUser.getExtendId());
            }
        }
//        userInfo.setDeptList(uucUserDept);
        return userInfo;
    }

    public void saveDingUser(UucUserInfo uucUserInfo) {
        Long id = uucUserInfo.getId();
        if (Objects.isNull(id)) {
            //insert
            if (Objects.isNull(uucUserInfoMapper.selectUucUserInfoByDingUid(uucUserInfo.getExtendId()))) {
                uucUserInfo.setId(IdUtils.snowflakeId());
                uucUserInfo.setUserType("01");
                uucUserInfo.setStatus("0");
                uucUserInfo.setSex("2");
                log.info("新增钉钉用户: " + JSONUtil.toJsonStr(uucUserInfo));
                uucUserInfoMapper.insertUucUserInfo(uucUserInfo);
//                String userCode = uucUserInfo.getUserCode();
                Long userInfoId = uucUserInfo.getId();
                List<UucUserDept> deptList = uucUserInfo.getDeptList();
                reInsertUserDept(String.valueOf(userInfoId), deptList);
                saveDefaultAccount(uucUserInfo, "N", UucUserType.DINGDING.getCode(), uucUserInfo.getPhone());
                saveDefaultRole(uucUserInfo);
            }
        } else {
            //update
            log.info("更新钉钉用户: " + JSONUtil.toJsonStr(uucUserInfo));
            uucUserInfoMapper.updateUucUserInfo(uucUserInfo);
//            String userCode = uucUserInfo.getUserCode();
            Long userInfoId = uucUserInfo.getId();
            List<UucUserDept> deptList = uucUserInfo.getDeptList();
            reInsertUserDept(String.valueOf(userInfoId), deptList);
            saveDefaultAccount(uucUserInfo, "N", UucUserType.DINGDING.getCode(), uucUserInfo.getPhone());
            saveDefaultRole(uucUserInfo);
        }

    }

    public void saveUser(UucUserInfo uucUserInfo) {
        Long id = uucUserInfo.getId();
        if (Objects.isNull(id)) {
            // 新增
            uucUserInfo.setId(IdUtils.snowflakeId());
            uucUserInfo.setStatus("0");
            log.info("新增用户: " + JSONUtil.toJsonStr(uucUserInfo));
            uucUserInfoMapper.insertUucUserInfo(uucUserInfo);
            Long userInfoId = uucUserInfo.getId();
            List<UucUserDept> deptList = uucUserInfo.getDeptList();
            reInsertUserDept(String.valueOf(userInfoId), deptList);
            saveDefaultAccount(uucUserInfo, "Y", UucUserType.USERCENTER.getCode(), uucUserInfo.getUserLoginAccount());
            saveDefaultRole(uucUserInfo);
        } else {
            // 更新
            log.info("更新用户: " + JSONUtil.toJsonStr(uucUserInfo));
            uucUserInfoMapper.updateUucUserInfo(uucUserInfo);
            Long userInfoId = uucUserInfo.getId();
            List<UucUserDept> deptList = uucUserInfo.getDeptList();
            reInsertUserDept(String.valueOf(userInfoId), deptList);
            saveDefaultAccount(uucUserInfo, "Y", UucUserType.USERCENTER.getCode(), uucUserInfo.getUserLoginAccount());
            saveDefaultRole(uucUserInfo);
        }

    }

    public UucUserInfo getUserByUserId(String userId, String userType) {
        UucUserInfo userInfo = new UucUserInfo();
        userInfo.setUserCode(userId);
        userInfo.setUserType(userType);
        List<UucUserInfo> uucUserInfos = uucUserInfoMapper.selectUucUserInfoList(userInfo);
        if (CollectionUtils.isEmpty(uucUserInfos)) {
            return null;
        }
        UucUserInfo uucUserInfo = uucUserInfos.get(0);
        List<UucUserDept> uucUserDept = uucUserDeptMapper.selectUucUserDeptByUserCode(String.valueOf(uucUserInfo.getId()));
        List<UucUserDept> userDeptList = Lists.newArrayList();
        for (UucUserDept userDept : uucUserDept) {
            UucUserDept dept = new UucUserDept();
            dept.setUserCode(String.valueOf(uucUserInfo.getId()));
            dept.setDeptCode(userDept.getDeptCode());
            dept.setAdminFlag(userDept.getAdminFlag());
            userDeptList.add(dept);
        }
        uucUserInfo.setDeptList(userDeptList);
        return uucUserInfo;
    }

    private void saveDefaultRole(UucUserInfo uucUserInfo) {
        String userCode = String.valueOf(uucUserInfo.getId());
        List<Long> roleList = roleService.selectUucRoleListByUserCode(userCode);
        if (CollectionUtils.isEmpty(roleList)) {
            SysRole defaultRole = roleService.getDefaultRole();
            if (Objects.nonNull(defaultRole)) {
                roleService.insertUucUserRoleList(userCode, Lists.newArrayList(defaultRole.getRoleId()));
            }
        }
    }

    private void saveDefaultAccount(UucUserInfo uucUserInfo, String activeType, String accountType, String userLoginAccount) {
        // 保存默认账号
        Long userInfoId = uucUserInfo.getId();
        List<UucLoginAccount> accountList = uucLoginAccountMapper.selectByUserId(userInfoId);
        if (CollectionUtils.isEmpty(accountList)) {
            // insert
            UucLoginAccount loginAccount = new UucLoginAccount();
            String psw = configService.selectConfigByKey("sys.user.initPassword");
            loginAccount.setLoginPwd(SecurityUtils.encryptPassword(psw));
            loginAccount.setUserCode(String.valueOf(uucUserInfo.getId()));
            loginAccount.setAccountExpiredStatus("N");
            loginAccount.setPasswdExpiredStatus("N");
            loginAccount.setAccountLockedStatus("N");
            try {
                loginAccount.setLoginAcct(userLoginAccount);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Date date = DateUtils.parseDate("2099-01-01 12:00:00");
            loginAccount.setPwdValidPeriodDate(date);
            loginAccount.setAcctValidPeriodDate(date);
            loginAccount.setStatus("0");
            loginAccount.setAccountType(accountType);
            loginAccount.setDelFlag("0");
            loginAccount.setActive(activeType);
            loginAccount.setEnabled("Y");
            loginAccount.setStatus("0");
            loginAccount.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
            loginAccount.setCreateTime(DateUtils.getNowDate());
            uucLoginAccountMapper.insertUucLoginAccount(loginAccount);
        } else {
            // update
            for (UucLoginAccount uucLoginAccount : accountList) {
                // only dingtalkName
                try {
                    uucLoginAccount.setLoginAcct(userLoginAccount);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                uucLoginAccountMapper.updateUucLoginAccount(uucLoginAccount);
            }
        }


    }

    private void reInsertUserDept(String userId, List<UucUserDept> deptList) {
        uucUserDeptMapper.deleteUucUserDeptByUserCode(userId);
        if (CollectionUtils.isNotEmpty(deptList)) {
            for (UucUserDept userDept : deptList) {
                UucDeptInfo uucDeptInfo = deptInfoMapper.selectByDeptCode(userDept.getDeptCode());
                if (Objects.nonNull(uucDeptInfo)) {
                    uucDeptInfo.setCreateTime(DateUtils.getNowDate());
                    uucDeptInfo.setUpdateTime(DateUtils.getNowDate());
                    // 钉钉用户userId转换为用户数据表雪花id
                    userDept.setUserCode(String.valueOf(userId));
                    if (Objects.nonNull(uucDeptInfo)) {
                        // 钉钉部门id就是部门数据表id
                        userDept.setDeptCode(String.valueOf(uucDeptInfo.getId()));
                        log.info("钉钉的用户组织关系保存:" + JSONUtil.toJsonStr(userDept));
                        uucUserDeptMapper.insertUucUserDept(userDept);
                    }
                }
            }
        }
    }

    public List<String> getDingUser() {
        return uucUserInfoMapper.selectDingUser();
    }

    public List<String> getUserByUserType(String userType) {
        return uucUserInfoMapper.getUserByUserType(userType);
    }

    public void deleteDingUser(Map deleteUserCodes) {
        Object deleteCodes = deleteUserCodes.get("deleteUserCodes");
        if (Objects.nonNull(deleteCodes) && deleteCodes instanceof List) {
            List<String> delCodes = (List) deleteCodes;
            for (String delCode : delCodes) {
                reInsertUserDept(delCode, null);
                UucUserInfo userInfo = uucUserInfoMapper.selectUucUserInfoByDingUid(delCode);
                if (Objects.nonNull(userInfo)) {
                    // delete account
                    uucLoginAccountMapper.deleteByUserCode(userInfo.getId());
                    // delete user dept  project
                    uucUserDeptMapper.deleteUucUserDeptByUserCode(String.valueOf(userInfo.getId()));
                    projectUserService.deleteUucProjectUserByUserCode(String.valueOf(userInfo.getId()));

                }
                uucUserInfoMapper.deleteDingUserByCode(delCode);
            }

        }

    }

    /**
     * 检查手机号码是否唯一
     *
     * @param phone
     * @return
     */
    public int checkPhoneUnique(String phone) {
        return uucUserInfoMapper.checkPhoneUnique(phone);
    }

    /**
     * 查询用户信息
     *
     * @param userName 用户名
     * @return 用户信息
     */

    public UucUserInfo selectUucUserInfoByUserName(String userName) {
        UucLoginAccount loginAccount = new UucLoginAccount();
        loginAccount.setLoginAcct(userName);
        List<UucLoginAccount> accountList = uucLoginAccountMapper.selectUucLoginAccountList(loginAccount);
        if (CollectionUtils.isEmpty(accountList)) {
            return null;
        }
        UucUserInfo userInfo = uucUserInfoMapper.selectUucUserInfoById(Long.valueOf(accountList.get(0).getUserCode()));
        userInfo.setAccountList(accountList);
        return userInfo;
    }

    /**
     * 查询用户信息
     *
     * @param uid 外部用户Id
     * @return 用户信息
     */

    public UucUserInfo selectUucUserInfoByUid(String uid) {
        UucUserInfo userInfo = uucUserInfoMapper.selectUucUserInfoByUid(uid);
        UucLoginAccount loginAccount = new UucLoginAccount();
        loginAccount.setUserCode(String.valueOf(userInfo.getId()));
        List<UucLoginAccount> accountList = uucLoginAccountMapper.selectUucLoginAccountList(loginAccount);
        if (CollectionUtils.isEmpty(accountList)) {
            return null;
        }
        userInfo.setAccountList(accountList);
        return userInfo;
    }

    /**
     * 修改本人基本信息
     *
     * @param uucUserInfo
     * @return
     */
    public int updateUserProfile(UucUserInfo uucUserInfo) {
        return uucUserInfoMapper.updateUucUserInfo(uucUserInfo);
    }

    /**
     * 重置用户密码
     *
     * @param password 密码
     * @return 结果
     */
    public int resetUserPwd(String password) {
        UucLoginAccount loginAccount = new UucLoginAccount();
        loginAccount.setUserCode(String.valueOf(SecurityUtils.getUserId()));
        loginAccount.setLoginPwd(password);
        return uucLoginAccountMapper.resetUserPwd(loginAccount);
    }

    /**
     * 用户授权角色
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertUserAuth(Long userId, Long[] roleIds) {
        userRoleMapper.deleteUserRoleByUserId(userId);
        insertUserRole(userId, roleIds);
    }

    /**
     * 新增用户角色信息
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    public void insertUserRole(Long userId, Long[] roleIds) {
        if (StringUtils.isNotNull(roleIds)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roleIds) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0) {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    public List<UucUserInfo> selectAllUser() {
        return uucUserInfoMapper.selectAllUser();
    }

    public List<UucUserInfo> getWholeUser() {
        List<UucUserInfo> uucUserInfos = selectAllUser();
        List<UucDeptInfo> deptLists = deptInfoService.selectAllDepts();
        for (UucUserInfo userInfo : uucUserInfos) {
            // 人员的关系  人员管理人员  组织包含人员 人员管理组织
            List<String> managePeoples = Lists.newArrayList();
            List<String> leaderPeoples = Lists.newArrayList();
            List<String> belongOrgs = Lists.newArrayList();
            List<String> manageOrgs = Lists.newArrayList();
            List<UucUserDept> uucUserDept = uucUserDeptMapper.selectUucUserDeptByUserCode(String.valueOf(userInfo.getId()));
            for (UucUserDept userDept : uucUserDept) {
                String deptCode = userDept.getDeptCode();
                for (UucDeptInfo deptTmp : deptLists) {
                    String deptSpecialCode = String.valueOf(deptTmp.getId());
                    if (deptCode.equals(deptSpecialCode)) {
                        belongOrgs.add(deptSpecialCode);
                        if ("1".equals(userDept.getAdminFlag())) {
                            manageOrgs.add(deptSpecialCode);
                        }
                    }
                }
            }
            // 管理的人员 ,被管理的人员
            for (UucUserInfo aboutUser : uucUserInfos) {
                // 下级
                if (String.valueOf(userInfo.getId()).equals(aboutUser.getLeaderCode())) {
                    managePeoples.add(String.valueOf(aboutUser.getId()));
                }
                // 上级
                String leaderCode = userInfo.getLeaderCode();
                if (StringUtils.isNotBlank(leaderCode) && leaderCode.equals(String.valueOf(aboutUser.getId()))) {
                    leaderPeoples.add(String.valueOf(aboutUser.getId()));
                }
            }
            Map<String, Object> params = userInfo.getParams();
            if (MapUtils.isEmpty(params)) {
                params = Maps.newHashMap();
                userInfo.setParams(params);
            }
            CollectionUtil.sortByPinyin(managePeoples);
            CollectionUtil.sortByPinyin(belongOrgs);
            CollectionUtil.sortByPinyin(manageOrgs);
            CollectionUtil.sortByPinyin(leaderPeoples);
            params.put(SyncConstants.MANAGE_PEOPLE, managePeoples);
            params.put(SyncConstants.CONTAINED_ORG, belongOrgs);
            params.put(SyncConstants.MANAGE_ORG, manageOrgs);
            params.put(SyncConstants.LEADER_PEOPLE, leaderPeoples);


        }

        return uucUserInfos;
    }

    public void updateLeaderCode() {
        List<UucUserInfo> dingUserList = selectDingtalkUserList();
        for (UucUserInfo userInfo : dingUserList) {
            String leaderCode = userInfo.getLeaderCode();
            if (StringUtils.isNotBlank(leaderCode)) {
                UucUserInfo dingUserTmp = uucUserInfoMapper.selectUucUserInfoByDingUid(leaderCode);
                if (Objects.nonNull(dingUserTmp) && !leaderCode.equals(String.valueOf(dingUserTmp.getId()))) {
                    userInfo.setLeaderCode(String.valueOf(dingUserTmp.getId()));
                    uucUserInfoMapper.updateUucUserInfo(userInfo);
                }
            }
        }
    }

    private List<UucUserInfo> selectDingtalkUserList() {
        return uucUserInfoMapper.selectDingUserList();
    }

    public List<String> getAdminUserList() {
        return uucUserInfoMapper.getAdminUserList();
    }

    public List<String> getAdminDeptList(UserAdminDeptVO vo) {
        // 如果是管理员角色
        List<String> adminList = uucUserInfoMapper.getAdminUserList();
        if (adminList.contains(vo.getUserCode())) {
            return vo.getDeptCode();
        }
        // 如果是组织负责人
        UucUserDept userDept = new UucUserDept();
        userDept.setAdminFlag("1");
        userDept.setUserCode(vo.getUserCode());
        List<UucUserDept> userDeptList = uucUserDeptMapper.selectUucUserDeptList(userDept);
        if (CollectionUtils.isNotEmpty(userDeptList)) {
            return userDeptList.stream().map(UucUserDept::getDeptCode)
                    .filter(deptCode -> vo.getDeptCode().contains(deptCode))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public void setDefaultRoleMonitor() {
        List<Long> uucUserInfoList = uucUserInfoMapper.selectUucNotHasRoleUser();
        if (CollectionUtils.isNotEmpty(uucUserInfoList)) {
            SysRole defaultRole = roleService.getDefaultRole();
            if (Objects.nonNull(defaultRole)) {
                for (Long id : uucUserInfoList) {
                    roleService.insertUucUserRoleList(String.valueOf(id), Lists.newArrayList(defaultRole.getRoleId()));
                }
            }
        }
    }

    public Boolean syncUserByUserType(String userType) {
        if (userType.equals(UucUserType.USERCENTER.getCode())) {
            remoteXxlJobService.syncDeptAndUser(SecurityConstants.INNER);
            return true;
        } else {
            return false;
        }

    }

}
