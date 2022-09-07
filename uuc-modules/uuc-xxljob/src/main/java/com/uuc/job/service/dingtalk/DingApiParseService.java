package com.uuc.job.service.dingtalk;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.dingtalk.api.response.OapiV2DepartmentGetResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.uuc.common.core.constant.SyncConstants;
import com.uuc.common.core.utils.DateUtils;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.system.api.model.UucUserDept;
import com.uuc.system.api.model.UucUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DingApiParseService {


    public Map<String,Object> dingDept2Map(OapiV2DepartmentGetResponse.DeptGetResponse detpTmp, List<OapiV2DepartmentGetResponse.DeptGetResponse> deptDetailList) {
        Map result = Maps.newHashMap();
        Long deptId = detpTmp.getDeptId();
        Long parentId = detpTmp.getParentId();
        result.put("deptCode",String.valueOf(deptId));
        result.put("dingDeptId",String.valueOf(deptId));
        result.put("deptName",detpTmp.getName());
        if (Objects.nonNull(parentId)) {
            result.put("parentCode",String.valueOf(parentId));
        }else{
            result.put("parentCode","");
        }
        // 祖级列表
        List<Long> deptIds = Objects.nonNull(parentId) ?  Lists.newArrayList(deptId,parentId) :  Lists.newArrayList(deptId);
        while(Objects.nonNull(parentId) && (parentId.longValue() != 1 || parentId.longValue() == -1)){
            Long finalParentId = parentId;
            OapiV2DepartmentGetResponse.DeptGetResponse parentDept = deptDetailList.stream().filter(item -> finalParentId.equals(item.getDeptId())).findFirst().get();
            if (Objects.isNull(parentDept)){
                break;
            }
            parentId = parentDept.getParentId();
            deptIds.add(parentId);
        }
        Collections.reverse(deptIds);
        List<String> deptStrs = deptIds.stream().map(String::valueOf).collect(Collectors.toList());
        String ancestors = String.join(",",deptStrs);
        result.put("ancestors",ancestors);
        result.put("level",deptStrs.size());
        result.put("phone", StringUtils.null2Empty(detpTmp.getTelephone()));

        log.info("组织信息:  "+ JSONUtil.toJsonStr(result));
        return result;
    }

    //钉钉用户映射为Map
    public Map<String,Object> dingUser2Map(OapiV2UserGetResponse.UserGetResponse user, List<OapiV2UserGetResponse.UserGetResponse> userList){
        Map result = Maps.newHashMap();
        String userid = user.getUserid();
        result.put("extendId",userid);
        result.put("hiredTime", DateUtils.getUnixDateStr(user.getHiredDate()) );
        result.put("userJobNumber", user.getJobNumber());
        result.put("phone", StringUtils.null2Empty(user.getMobile()));
        result.put("email", StringUtils.null2Empty(user.getEmail()));
        result.put("userName", user.getName());
        result.put("userCode", userid);
        result.put("postName", StringUtils.null2Empty(user.getTitle()));
        result.put("leaderCode",Objects.isNull(user.getManagerUserid()) ? "" : user.getManagerUserid());
        result.put("leaderName","");
        result.put("workLocation",StringUtils.null2Empty(user.getWorkPlace()));
        if (StrUtil.isNotBlank(user.getManagerUserid())) {
            for (OapiV2UserGetResponse.UserGetResponse userGetResponse : userList) {
                String leaderUserId = userGetResponse.getUserid();
                if (user.getManagerUserid().equals(leaderUserId)) {
                    result.put("leaderName", userGetResponse.getName());
                    break;
                }
            }
        }

        // 归属组织
        List<Long> deptIdList = user.getDeptIdList();
        List<String> belongOrgs = deptIdList.stream().map(String::valueOf).collect(Collectors.toList());
        result.put(SyncConstants.CONTAINED_ORG,belongOrgs);
        // 管理组织
        List<String> manageOrgs = user.getLeaderInDept().stream().filter(OapiV2UserGetResponse.DeptLeader::getLeader).map(item -> String.valueOf(item.getDeptId())).collect(Collectors.toList());
        result.put(SyncConstants.MANAGE_ORG,manageOrgs);
        log.info("用户信息:  "+JSONUtil.toJsonStr(result));
        return result;
    }

    //UUC用户映射为Map
    public Map<String,Object> uucUser2Map(UucUserInfo userInfo){
        Map result = Maps.newHashMap();
        result.put("extendId",userInfo.getExtendId());
        result.put("hiredTime", userInfo.getHiredTime());
        result.put("userJobNumber", userInfo.getUserJobNumber());
        result.put("phone", userInfo.getPhone());
        result.put("email", userInfo.getEmail());
        result.put("userName", userInfo.getUserName());
        result.put("userCode", userInfo.getUserCode());
        result.put("postName", userInfo.getPostName());
        // 此处的leaderCode 是用户id
        result.put("leaderCode",userInfo.getLeaderCode());
        result.put("leaderName",userInfo.getLeaderName());
        result.put("workLocation",userInfo.getWorkLocation());

        List<UucUserDept> deptList = userInfo.getDeptList();
        List<String> belongOrgs = Lists.newArrayList();
        List<String> manageOrgs = Lists.newArrayList();
        for (UucUserDept uucUserDept : deptList) {
            String deptCode = uucUserDept.getDeptCode();
            belongOrgs.add(deptCode);
            String adminFlag = uucUserDept.getAdminFlag();
            if (StrUtil.isNotBlank(adminFlag) && "1".equals(adminFlag)){
                manageOrgs.add(deptCode);
            }
        }
        result.put(SyncConstants.CONTAINED_ORG,belongOrgs);
        result.put(SyncConstants.MANAGE_ORG,manageOrgs);

        return result;
    }


    public UucUserInfo getUucUserInfo(Map<String, Object> apiUserMap) {
        UucUserInfo updateUser = JSONUtil.toBean(JSONUtil.toJsonStr(apiUserMap), UucUserInfo.class);
        String hiredTime = updateUser.getHiredTime();
        if (StringUtils.isNotBlank(hiredTime)){
            hiredTime = DateUtils.parse2OtherFormate(hiredTime,DateUtils.YYYY_MM_DD_HH_MM_SS,DateUtils.YYYY_MM_DD);
            updateUser.setHiredTime(hiredTime);
        }else {
            updateUser.setHiredTime(null);
        }
        List<String> belongOrgs = (List) apiUserMap.get(SyncConstants.CONTAINED_ORG);
        List<String> manageOrgs = (List) apiUserMap.get(SyncConstants.MANAGE_ORG);
        List<UucUserDept> deptList = Lists.newArrayList();
        for (String belongOrg : belongOrgs) {
            UucUserDept userDept = new UucUserDept();
            userDept.setUserCode(updateUser.getUserCode());
            userDept.setDeptCode(belongOrg);
            userDept.setAdminFlag(manageOrgs.contains(belongOrg) ? "1" : "0");
            deptList.add(userDept);
        }
        updateUser.setDeptList(deptList);
        return updateUser;
    }

}
