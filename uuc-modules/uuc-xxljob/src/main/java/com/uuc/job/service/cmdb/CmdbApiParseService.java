package com.uuc.job.service.cmdb;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import com.uuc.common.core.constant.SyncConstants;
import com.uuc.common.core.utils.DateUtils;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.system.api.model.UucDeptInfo;
import com.uuc.system.api.model.UucProject;
import com.uuc.system.api.model.UucUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;


@Slf4j
@Service
public class CmdbApiParseService {

    // Map左侧为CMDB元模型字段
    public Map<String,Object> uucProject2Map(UucProject project) {
        Map<String,Object> result = Maps.newHashMap();
        result.put("ModelCode",StringUtils.null2Empty(project.getId()));
        result.put("ModelName",StringUtils.null2Empty(project.getProjectName()));
        result.put("ShortName",StringUtils.null2Empty(project.getShortName()));
        result.put("ApprovalNo",StringUtils.null2Empty(project.getProjectArchiveCode()));
        result.put("Level",StringUtils.null2Empty(project.getProjectLevel()));
        result.put("Source", StringUtils.null2Empty(project.getProjectSource()));
        result.put("Applicant",StringUtils.null2Empty(project.getApplicant()));
        result.put("Version",StringUtils.null2Empty(project.getVersion()));
        result.put("StartDate", DateUtils.parseYYYYMMDD(project.getProjectStartDate()));
        result.put("EndDate",DateUtils.parseYYYYMMDD(project.getProjectEndDate()));
        result.put("Budget",StringUtils.null2Empty(project.getProjectBudget()));
        result.put("Target",StringUtils.null2Empty(project.getProjectGoal()));
        result.put("Description",StringUtils.null2Empty(project.getProjectDesc()));
        result.put("Remark",StringUtils.null2Empty(project.getRemark()));

        Map<String, Object> params = project.getParams();
        System.out.println("---门户UU---【项目】关系列表:"+ JSONUtil.toJsonStr(params));
    /*
        同步项目同步的关系:
        1. 组织管理项目
        2. 组织使用项目
        3. 人员参与项目
        4. 人员管理项目
        5. 人员维护项目
     */
        result.put(SyncConstants.ORG_MANAGE_PROJECT,params.get(SyncConstants.ORG_MANAGE_PROJECT));
        result.put(SyncConstants.ORG_USE_PROJECT,params.get(SyncConstants.ORG_USE_PROJECT));
        result.put(SyncConstants.EMPLOYEE_JOIN_PROJECT,params.get(SyncConstants.EMPLOYEE_JOIN_PROJECT));
        result.put(SyncConstants.EMPLOYEE_MANAGE_PROJECT,params.get(SyncConstants.EMPLOYEE_MANAGE_PROJECT));
        result.put(SyncConstants.EMPLOYEE_MAINTAIN_PROJECT,params.get(SyncConstants.EMPLOYEE_MAINTAIN_PROJECT));
        return result;
    }

    public Map<String, Object> uucUser2Map(UucUserInfo userInfo) {
        Map<String,Object> result = Maps.newHashMap();
        result.put("ModelCode",StringUtils.null2Empty(userInfo.getId()));
        result.put("ModelName",StringUtils.null2Empty(userInfo.getUserName()));
        result.put("Typeauthorize",StringUtils.null2Empty(userInfo.getAuthorizeType()));
        result.put("Mobile", userInfo.getPhone());
        result.put("JobNumber", userInfo.getUserJobNumber());
        result.put("Title",userInfo.getPostName());
        result.put("Email", userInfo.getEmail());
        result.put("HiredDate", userInfo.getHiredTime());
        result.put("LiveSpace", userInfo.getLiveSpace());
        result.put("WorkPlace", userInfo.getWorkLocation());
        result.put("IdentityCard", userInfo.getIdentityCard());

        Map<String, Object> params = userInfo.getParams();
        System.out.println("---门户UU---【用户"+userInfo.getUserName()+"】关系列表:"+JSONUtil.toJsonStr(params));
    /*
        同步人员同步的关系:
        1. 人员管理人员
        2. 组织包含人员
        3. 人员管理组织
     */
        result.put(SyncConstants.MANAGE_PEOPLE,params.get(SyncConstants.MANAGE_PEOPLE));
        result.put(SyncConstants.LEADER_PEOPLE,params.get(SyncConstants.LEADER_PEOPLE));
        result.put(SyncConstants.CONTAINED_ORG,params.get(SyncConstants.CONTAINED_ORG));
        result.put(SyncConstants.MANAGE_ORG,params.get(SyncConstants.MANAGE_ORG));

        return result;
    }

    public Map<String, Object> uucDept2Map(UucDeptInfo deptInfo) {

        Map<String,Object> result = Maps.newHashMap();
        result.put("ModelCode",StringUtils.null2Empty(deptInfo.getId()));
        result.put("ModelName",StringUtils.null2Empty(deptInfo.getDeptName()));
        result.put("OrgLevel",StringUtils.null2Empty(deptInfo.getLevel()));
        result.put("OrgPhoneNumber",deptInfo.getPhone());
        result.put("OrgAddress",deptInfo.getAddress());


        Map<String, Object> params = deptInfo.getParams();
        System.out.println("---门户UU---【组织"+deptInfo.getDeptName()+"】关系列表:"+JSONUtil.toJsonStr(params));
    /*
        同步组织同步的关系:
        1. 组织管理组织
     */
        result.put(SyncConstants.PARENT_ORG,params.get(SyncConstants.PARENT_ORG));
        result.put(SyncConstants.CHILD_ORG,params.get(SyncConstants.CHILD_ORG));
        return result;
    }
}
