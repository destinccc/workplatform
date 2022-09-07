package com.uuc.job.service.dingtalk;

import cn.hutool.json.JSONUtil;
import com.dingtalk.api.response.OapiV2DepartmentGetResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.google.common.collect.Maps;
import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.job.dingtalk.service.DingtalkService;
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


@Slf4j
@Service
public class Dingtalk2UucService {


    @Autowired
    private DingtalkService dingtalkService;

    @Autowired
    private DingApiParseService dingApiParseService;

    @Autowired
    private RemoteSystemService remoteSystemService;

    @XxlJob("syncDingtalkDept")
    public  void getDepts(){
        System.out.println("------------------【同步钉钉组织 start】------------------");
        List<OapiV2DepartmentGetResponse.DeptGetResponse> deptDetailList = dingtalkService.getDeptDetailList();
        AjaxResult dingTalkDeptLists  = remoteSystemService.getDingTalkDept(SecurityConstants.INNER);
        Object exsistsDeptIds = dingTalkDeptLists.getData();
        List<String> apiDeptIds = deptDetailList.stream().map(OapiV2DepartmentGetResponse.DeptGetResponse::getDeptId).map(String::valueOf).collect(Collectors.toList());
        for (OapiV2DepartmentGetResponse.DeptGetResponse detpTmp : deptDetailList) {
            // 查询更新组织管理
            Long deptId = detpTmp.getDeptId();
            AjaxResult ajaxResult = remoteSystemService.selectDeptByDingtalkId(String.valueOf(deptId), SecurityConstants.INNER);
            Object uucDeptInfo = ajaxResult.getData();
            Map<String,Object> apiDeptInfo = dingApiParseService.dingDept2Map(detpTmp, deptDetailList);
            if (Objects.isNull(uucDeptInfo)){
                // insert
                UucDeptInfo updateDept = JSONUtil.toBean(JSONUtil.toJsonStr(apiDeptInfo), UucDeptInfo.class);
                remoteSystemService.saveDingtalkDept(updateDept, SecurityConstants.INNER);
                continue;
            }else {
                // judge
                Boolean isChange = false;
                Map<String,Object> exsists = JSONUtil.toBean(JSONUtil.toJsonStr(uucDeptInfo), Map.class);
                for (String key : apiDeptInfo.keySet()) {
                    Object origin = exsists.get(key);
                    Object now = apiDeptInfo.get(key);
                    if (! StringUtils.null2Empty(origin).equals(StringUtils.null2Empty(now))){
                        isChange = true;
                        log.info("钉钉部门变更属性(旧值 -> 新值): " +StringUtils.null2Empty(origin)+"  -->  "+StringUtils.null2Empty(now));
                        break;
                    }
                }
                if (isChange){
                    // update
                    UucDeptInfo originDept = JSONUtil.toBean(JSONUtil.toJsonStr(uucDeptInfo), UucDeptInfo.class);
                    UucDeptInfo updateDept = JSONUtil.toBean(JSONUtil.toJsonStr(apiDeptInfo), UucDeptInfo.class);
                    updateDept.setId(originDept.getId());
                    remoteSystemService.saveDingtalkDept(updateDept, SecurityConstants.INNER);
                }
            }
        }
        // delete
        try {
            if ( exsistsDeptIds instanceof List){
                List<String> tmpIds = (List) exsistsDeptIds;
                tmpIds.removeAll(apiDeptIds);
                if (CollectionUtils.isNotEmpty(tmpIds)){
                    Map param = Maps.newHashMap();
                    param.put("deteleIds",tmpIds);
                    log.info("删除的组织codes : "+JSONUtil.toJsonStr(tmpIds));
                    remoteSystemService.deleteDingtalkDept(param, SecurityConstants.INNER);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("------------------【同步钉钉组织 end】------------------");
    }





    @XxlJob("syncDingtalkUser")
    public  void getDeptUsers(){
        System.out.println("------------------【同步钉钉人员 start】------------------");
        List<OapiV2UserGetResponse.UserGetResponse> listUser = dingtalkService.getListUser();
        for (OapiV2UserGetResponse.UserGetResponse user : listUser) {
            String userid = user.getUserid();
            Map<String, Object> apiUserMap = dingApiParseService.dingUser2Map(user, listUser);
            AjaxResult userInfo = remoteSystemService.getDingTalkUser(userid,SecurityConstants.INNER);
            Object data = userInfo.getData();
            if (Objects.isNull(data)){
                // insert
                UucUserInfo updateUser = dingApiParseService.getUucUserInfo(apiUserMap);
                remoteSystemService.saveDingtalkUser(updateUser,SecurityConstants.INNER);
            }else{
                // judge
                UucUserInfo exsistUser = JSONUtil.toBean(JSONUtil.toJsonStr(data), UucUserInfo.class);
                Map<String, Object> exUserMap = dingApiParseService.uucUser2Map(exsistUser);
                boolean isChange = false;
                for (String key : apiUserMap.keySet()) {
                    Object now = apiUserMap.get(key);
                    Object origin = exUserMap.get(key);
                    if (! StringUtils.null2Empty(origin).equals(StringUtils.null2Empty(now))){
                        isChange = true;
                        log.info("钉钉员工变更属性(旧值 -> 新值): <"+key+">  " +StringUtils.null2Empty(origin)+"  -->  "+StringUtils.null2Empty(now));
//                        break;
                    }
                }
                //update
                // 如果被逻辑删除,直接算为变更用户
                if ("1".equals(exsistUser.getDelFlag())){
                    isChange = true;
                }
                if (isChange){
                    UucUserInfo updateUser = dingApiParseService.getUucUserInfo(apiUserMap);
                    updateUser.setDelFlag("0");
                    updateUser.setId(exsistUser.getId());
                    // 此时的用户部门关系 传递的都是钉钉的userid与deptid
                    remoteSystemService.saveDingtalkUser(updateUser,SecurityConstants.INNER);
                }
            }
        }
        // delete
        AjaxResult dingUser = remoteSystemService.getDingUser(SecurityConstants.INNER);
        Object data = dingUser.getData();
        if (data instanceof List){
            List<String> exsistsDingUserCodes = (List) data;
            List<String> apiUsers = listUser.stream().map(OapiV2UserGetResponse.UserGetResponse::getUserid).collect(Collectors.toList());
            exsistsDingUserCodes.removeAll(apiUsers);
            if (CollectionUtils.isNotEmpty(exsistsDingUserCodes)){
                log.info("删除的用户codes : "+JSONUtil.toJsonStr(exsistsDingUserCodes));
                Map param = Maps.newHashMap();
                param.put("deleteUserCodes",exsistsDingUserCodes);
                remoteSystemService.deleteDingUser(param,SecurityConstants.INNER);
            }
        }

        // 统一更新user的leaderCode为mysql表中的userid
        remoteSystemService.syncUpdateUser(SecurityConstants.INNER);
        System.out.println("------------------【同步钉钉人员 end】------------------");
    }




}
