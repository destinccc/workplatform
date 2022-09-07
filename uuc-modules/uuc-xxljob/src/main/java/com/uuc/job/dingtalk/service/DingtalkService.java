package com.uuc.job.dingtalk.service;


import com.dingtalk.api.response.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.uuc.job.dingtalk.util.DingTalkUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DingtalkService {

    @Autowired
    private DingTalkUtils dingtalkUtils;


    // 获取所有部门
    public Set<OapiV2DepartmentListsubResponse.DeptBaseResponse> getDeptList() {
        Set<OapiV2DepartmentListsubResponse.DeptBaseResponse> result = Sets.newHashSet();
        OapiV2DepartmentListsubResponse.DeptBaseResponse rootDept = new OapiV2DepartmentListsubResponse.DeptBaseResponse();
        rootDept.setDeptId(1L);
        rootDept.setName("组织架构");
        rootDept.setParentId(-1L);
        result.add(rootDept);
        getChildDept(result,dingtalkUtils.getDepList(null));
        return result;
    }

    // 获取子部门
    public void getChildDept(Set<OapiV2DepartmentListsubResponse.DeptBaseResponse> result, List<OapiV2DepartmentListsubResponse.DeptBaseResponse> depList) {
        if (CollectionUtils.isNotEmpty(depList)){
            result.addAll(depList);
            for (OapiV2DepartmentListsubResponse.DeptBaseResponse child : depList) {
                getChildDept(result,dingtalkUtils.getDepList(child.getDeptId()));
            }
        }
    }

    // 所有部门下用户列表
    public List<OapiV2UserGetResponse.UserGetResponse> getListUser(){
        List<OapiV2UserListResponse.ListUserResponse> results = Lists.newArrayList();
        List<OapiV2UserGetResponse.UserGetResponse> userDetailResults = Lists.newArrayList();
        List<OapiV2UserListResponse.PageResult> wholeUserList = getWholeUserList();
        for (OapiV2UserListResponse.PageResult pageResult : wholeUserList) {
            List<OapiV2UserListResponse.ListUserResponse> list = pageResult.getList();
            if (CollectionUtils.isNotEmpty(list)){
                results.addAll(list);
            }
        }
        //全量去重 属于多个部门会有重复人员
        Set<String> userIds = Sets.newHashSet();
        results = results.stream().filter( item -> userIds.add(item.getUserid())).collect(Collectors.toList());
        // 获取员工详情
        for (OapiV2UserListResponse.ListUserResponse result : results) {
            String userid = result.getUserid();
            OapiV2UserGetResponse.UserGetResponse userDetailInfo = dingtalkUtils.getUserDetailInfo(userid);
            userDetailResults.add(userDetailInfo);
        }

        return userDetailResults;
    }


    // 获取所有部门所有人员PageResult
    public List<OapiV2UserListResponse.PageResult> getWholeUserList(){
        List<OapiV2UserListResponse.PageResult> results = Lists.newArrayList();
        // 部门dept为 1L 的人员
        OapiV2UserListResponse.PageResult depUserList = dingtalkUtils.getDepUserList(1L, null);
        getUserListUnderDept(1L,results,depUserList);
        // 先获取所有部门, 在查询所有部门下人员
        Set<OapiV2DepartmentListsubResponse.DeptBaseResponse> deptList = getDeptList();
        for (OapiV2DepartmentListsubResponse.DeptBaseResponse dept : deptList) {
            results.addAll(getUserListUnderDept(dept));
        }
        return results;
    }

    // 返回某个部门下所有用户集合
    private List<OapiV2UserListResponse.PageResult> getUserListUnderDept(OapiV2DepartmentListsubResponse.DeptBaseResponse dept) {
        List<OapiV2UserListResponse.PageResult> userListResults = Lists.newArrayList();
        OapiV2UserListResponse.PageResult depUserList = dingtalkUtils.getDepUserList(dept.getDeptId(), null);
        getUserListUnderDept(dept.getDeptId(),userListResults,depUserList);
        return userListResults;
    }

    // 分页获取
   private void  getUserListUnderDept(Long deptId, List<OapiV2UserListResponse.PageResult> userListResults, OapiV2UserListResponse.PageResult depUserList){
       if (Objects.nonNull(depUserList)){
           userListResults.add(depUserList);
           Boolean hasMore = depUserList.getHasMore();
           if (Objects.nonNull(hasMore) && hasMore){
               getUserListUnderDept(deptId,userListResults,dingtalkUtils.getDepUserList(deptId, depUserList.getNextCursor()));
           }
       }
   }

    // 获取所有部门详情
    public List<OapiV2DepartmentGetResponse.DeptGetResponse> getDeptDetailList() {
        Set<OapiV2DepartmentListsubResponse.DeptBaseResponse> deptList = this.getDeptList();
        List<OapiV2DepartmentGetResponse.DeptGetResponse> detailLists = Lists.newArrayList();
        for (OapiV2DepartmentListsubResponse.DeptBaseResponse deptBaseRespe : deptList) {
            detailLists.add(dingtalkUtils.getDepDetails(deptBaseRespe.getDeptId()));
        }
        OapiV2DepartmentGetResponse.DeptGetResponse rootDept = new OapiV2DepartmentGetResponse.DeptGetResponse();
        return detailLists;
    }


    // 根据审批流程名称 获取所有实例
    public List<OapiProcessinstanceListidsResponse.PageResult> getWholeInstanceList(String processName, Long inteDataId, String batchNo){
        List<OapiProcessinstanceListidsResponse.PageResult> results = Lists.newArrayList();
        String processCode = this.getProcessCodeByName(processName,inteDataId,batchNo);
        OapiProcessinstanceListidsResponse.PageResult instance = dingtalkUtils.getProcessInstanceList(processCode, null);
        getNextCursorInstanceList(results,instance,processCode);
        return results;
    }

    // 获取审批流标识
    public String getProcessCodeByName(String processName,Long inteDataId, String batchNo){
        return dingtalkUtils.getProcessCode(processName);
    }


    // 分页获取所有模板实例
    private void  getNextCursorInstanceList(List<OapiProcessinstanceListidsResponse.PageResult> InstanceListResults, OapiProcessinstanceListidsResponse.PageResult instance, String processCode){
        if (Objects.nonNull(instance)){
            InstanceListResults.add(instance);
            Long nextCursor = instance.getNextCursor();
            if (Objects.nonNull(nextCursor)){
                OapiProcessinstanceListidsResponse.PageResult nextCursorInstance = dingtalkUtils.getProcessInstanceList(processCode, nextCursor);
                getNextCursorInstanceList(InstanceListResults,nextCursorInstance,processCode);
            }
        }
    }

    // 根据流程名称获取所有实例详情
    public List<OapiProcessinstanceGetResponse.ProcessInstanceTopVo> getInstanceDetailList(String processName, Long inteDataId, String batchNo){
        List<OapiProcessinstanceGetResponse.ProcessInstanceTopVo> results = Lists.newArrayList();
        List<OapiProcessinstanceListidsResponse.PageResult> wholeInstanceList = getWholeInstanceList(processName, inteDataId, batchNo);
        if (CollectionUtils.isNotEmpty(wholeInstanceList)){
            for (OapiProcessinstanceListidsResponse.PageResult pageResult : wholeInstanceList) {
                List<String> instanceIds = pageResult.getList();
                if (CollectionUtils.isNotEmpty(instanceIds)){
                    for (String instanceId : instanceIds) {
                        OapiProcessinstanceGetResponse.ProcessInstanceTopVo processInstanceDetail = dingtalkUtils.getProcessInstanceDetail(instanceId);
                        if (Objects.nonNull(processInstanceDetail)){
                            // 实例Id 替换businessId 作为唯一标识
                            processInstanceDetail.setBusinessId(instanceId);
                            results.add(processInstanceDetail);
                        }
                    }
                }
            }
        }

        return results;
    }
}
