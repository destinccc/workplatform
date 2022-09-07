package com.uuc.job.service.usercenter;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.enums.UucDeptType;
import com.uuc.common.core.enums.UucUserType;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.job.service.usercenter.model.org.UserCenterOrg;
import com.uuc.job.service.usercenter.model.user.UserCenterUser;
import com.uuc.system.api.RemoteSystemService;
import com.uuc.system.api.model.UucDeptInfo;
import com.uuc.system.api.model.UucUserInfo;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @description: 同步四川 用户中心的组织和用户
 * @author: Destin
 */
@Service
@Slf4j
public class UserCenterJobService {

    @Autowired
    private UserCenterApiService userCenterApiService;
    @Autowired
    private RemoteSystemService remoteSystemService;

    @XxlJob("userCenterResourceSync")
    public void userCenterResourceSync() throws Exception {
        log.info("------------------【同步用户中心里的组织 start】------------------");
        syncDeptResource();
        log.info("------------------【同步用户中心里的组织 end】------------------");

        log.info("------------------【同步用户中心里的人员 start】------------------");
        syncUserResource();
        log.info("------------------【同步用户中心里的人员 end】------------------");

    }

    public void syncDeptResource() throws Exception {
        //查询用户中心里的组织
        List<UserCenterOrg> userCenterOrgList = userCenterApiService.getUserCenterOrgList();
        log.info("获取到的组织共：{}", userCenterOrgList.size());
        List<String> apiDeptIds = userCenterOrgList.stream().map(UserCenterOrg::getCode).collect(Collectors.toList());
        for (UserCenterOrg userCenterOrg : userCenterOrgList) {
            String deptId = userCenterOrg.getCode();
            AjaxResult ajaxResult = remoteSystemService.selectDeptByDingtalkId(deptId, SecurityConstants.INNER);
            Object uucDeptInfo = ajaxResult.getData();
            Map<String, Object> apiDeptInfo = userCenterApiService.userCenterDept2Map(userCenterOrg);
            if (Objects.isNull(uucDeptInfo)) {
                //新增
                UucDeptInfo updateDept = JSONUtil.toBean(JSONUtil.toJsonStr(apiDeptInfo), UucDeptInfo.class);
                updateDept.setDeptType(UucDeptType.USERCENTER.getCode());
                remoteSystemService.saveOrUpdateDept(updateDept, SecurityConstants.INNER);
                continue;
            } else {
                //修改
                Boolean isChange = false;
                Map<String, Object> exsists = JSONUtil.toBean(JSONUtil.toJsonStr(uucDeptInfo), Map.class);
                for (String key : apiDeptInfo.keySet()) {
                    if (key.equals("ancestors")) {
                        continue;
                    }
                    Object origin = exsists.get(key);
                    Object now = apiDeptInfo.get(key);
                    if (!StringUtils.null2Empty(origin).equals(StringUtils.null2Empty(now))) {
                        isChange = true;
                        log.info("deptCode:{}", deptId);
                        log.info("用户中心部门变更属性(旧值 -> 新值): <" + key + "> " + StringUtils.null2Empty(origin) + "  -->  " + StringUtils.null2Empty(now));
                        break;
                    }
                }
                if (isChange) {
                    UucDeptInfo originDept = JSONUtil.toBean(JSONUtil.toJsonStr(uucDeptInfo), UucDeptInfo.class);
                    UucDeptInfo updateDept = JSONUtil.toBean(JSONUtil.toJsonStr(apiDeptInfo), UucDeptInfo.class);
                    updateDept.setId(originDept.getId());
                    remoteSystemService.saveOrUpdateDept(updateDept, SecurityConstants.INNER);
                }
            }
        }
        //删除
        AjaxResult dingTalkDeptLists = remoteSystemService.getDeptByDeptType(UucDeptType.USERCENTER.getCode(), SecurityConstants.INNER);
        Object exsistsDeptIds = dingTalkDeptLists.getData();
        try {
            if (exsistsDeptIds instanceof List) {
                List<String> tmpIds = (List) exsistsDeptIds;
                tmpIds.removeAll(apiDeptIds);
                if (CollectionUtils.isNotEmpty(tmpIds)) {
                    Map param = Maps.newHashMap();
                    param.put("deteleIds", tmpIds);
                    log.info("删除的组织codes : " + JSONUtil.toJsonStr(tmpIds));
                    remoteSystemService.deleteDingtalkDept(param, SecurityConstants.INNER);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void syncUserResource() throws Exception {
        //查询用户中心里的人员
        List<UserCenterUser> userCenterUserList = userCenterApiService.getUserCenterUserPoolList(Lists.newArrayList(), null, null);
        log.info("获取到的人员共：{}", userCenterUserList.size());
        for (UserCenterUser user : userCenterUserList) {
            String userId = String.valueOf(user.getUid());
            Map<String, Object> apiUserMap = userCenterApiService.userCenterUser2Map(user);
            if (StringUtils.isEmpty(userId)) {
                //没有用户Id不同步
                log.error("用户中心同步用户：{},没有用户Id不同步", user.getName());
                continue;
            }
            AjaxResult userInfo = remoteSystemService.getUserByUserId(userId, UucUserType.USERCENTER.getCode(), SecurityConstants.INNER);
            Object data = userInfo.getData();
            if (Objects.isNull(data)) {
                // 新增
                UucUserInfo updateUser = userCenterApiService.getUucUserInfo(apiUserMap);
                updateUser.setUserType(UucUserType.USERCENTER.getCode());
                remoteSystemService.saveOrUpdateUser(updateUser, SecurityConstants.INNER);
            } else {
                // 更新
                UucUserInfo exsistUser = JSONUtil.toBean(JSONUtil.toJsonStr(data), UucUserInfo.class);
                Map<String, Object> exUserMap = userCenterApiService.uucUser2Map(exsistUser);
                boolean isChange = false;
                for (String key : apiUserMap.keySet()) {
                    if (key.equals("userLoginAccount")) {
                        continue;
                    }
                    Object now = apiUserMap.get(key);
                    Object origin = exUserMap.get(key);
                    if (!StringUtils.null2Empty(origin).equals(StringUtils.null2Empty(now))) {
                        isChange = true;
                        log.info("extendId: {}", userId);
                        log.info("用户中心里的员工变更属性(旧值 -> 新值): <" + key + ">  " + StringUtils.null2Empty(origin) + "  -->  " + StringUtils.null2Empty(now));
                        break;
                    }
                }
                if (isChange) {
                    UucUserInfo updateUser = userCenterApiService.getUucUserInfo(apiUserMap);
                    updateUser.setId(exsistUser.getId());
                    // 此时的用户部门关系 传递的都是userid与deptid
                    remoteSystemService.saveOrUpdateUser(updateUser, SecurityConstants.INNER);
                }
            }
        }
        // 删除
        AjaxResult dingUser = remoteSystemService.getUserByUserType(UucUserType.USERCENTER.getCode(), SecurityConstants.INNER);
        Object data = dingUser.getData();
        if (data instanceof List) {
            List<String> exsistsDingUserCodes = (List) data;
            List<String> apiUsers = userCenterUserList.stream().map(UserCenterUser::getUid).map(String::valueOf).collect(Collectors.toList());
            exsistsDingUserCodes.removeAll(apiUsers);
            try {
                if (CollectionUtils.isNotEmpty(exsistsDingUserCodes)) {
                    log.info("删除的用户codes : " + JSONUtil.toJsonStr(exsistsDingUserCodes));
                    Map param = Maps.newHashMap();
                    param.put("deleteUserCodes", exsistsDingUserCodes);
                    remoteSystemService.deleteDingUser(param, SecurityConstants.INNER);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
