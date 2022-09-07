package com.uuc.job.service.cmdb;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.job.dingtalk.constant.DingtalkRelConstants;
import com.uuc.system.api.RemoteCmdbService;
import com.uuc.system.api.RemoteSystemService;
import com.uuc.system.api.model.UucDeptInfo;
import com.uuc.system.api.model.UucProject;
import com.uuc.system.api.model.UucUserInfo;
import com.uuc.system.api.model.cmdb.ConfigEntity;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
public class Uuc2CMDBService {

    @Autowired
    private RemoteSystemService remoteSystemService;

    @Autowired
    private RemoteCmdbService cmdbService;

    @Autowired
    private CmdbApiParseService cmdbApiParseService;


    /*
    同步项目同步的关系:
    1. 组织管理项目
    2. 组织使用项目
    3. 人员参与项目
    4. 人员管理项目
    5. 人员维护项目
     */
    @XxlJob("syncUucProject2Cmdb")
    public void project2CMDB() {
        System.out.println("------------------【同步UUC项目 start】------------------");
        AjaxResult uucProjects = remoteSystemService.getUucProjects(SecurityConstants.INNER);
        Object uucProject = uucProjects.getData();
        List<UucProject> uucProjectList = JSONObject.parseArray(JSONUtil.toJsonStr(uucProject), UucProject.class);
        AjaxResult cmdbEntityList = cmdbService.getProjectEntityList(SecurityConstants.INNER);
        Object projectEntitys = cmdbEntityList.getData();
        List<ConfigEntity> cmdbConfigEntityList = JSONObject.parseArray(JSONUtil.toJsonStr(projectEntitys), ConfigEntity.class);
        for (UucProject project : uucProjectList) {
            Long id = project.getId();
            String specialCode = String.valueOf(id);
            ConfigEntity configEntity = getEntityBySpecialCode(specialCode, cmdbConfigEntityList);
            Map<String, Object> uucProjectMap = cmdbApiParseService.uucProject2Map(project);
            if (Objects.isNull(configEntity)) {
                // insert
                Map<String, Object> param = Maps.newHashMap();
                param.put("projectJson", uucProjectMap);
                log.info("新增项目信息:" + JSONUtil.toJsonStr(param));
                cmdbService.saveUucProject(param, SecurityConstants.INNER);
                continue;
            }
            Boolean isChange = isChangePropertyOrRel(configEntity, uucProjectMap, "【项目】", project.getProjectName());
            if (isChange) {
                Map<String, Object> param = Maps.newHashMap();
                param.put("entityId", configEntity.getEntityId());
                param.put("projectJson", uucProjectMap);
                log.info("更新项目信息:" + JSONUtil.toJsonStr(param));
                cmdbService.saveUucProject(param, SecurityConstants.INNER);
            }
        }

        List<String> currentSpecialCodes = uucProjectList.stream().map(item -> String.valueOf(item.getId())).collect(Collectors.toList());
        List<String> exsistsSpecialCodes = cmdbConfigEntityList.stream().map(item -> String.valueOf(item.getEntityName())).collect(Collectors.toList());
        deleteSomeModelEntity(currentSpecialCodes, exsistsSpecialCodes, DingtalkRelConstants.Bind_Project, "【删除项目实例】");
        System.out.println("------------------【同步UUC项目 end】------------------");
    }




    /*
    同步人员同步的关系:
    1. 人员管理人员
    2. 组织包含人员
    3. 人员管理组织
     */
    @XxlJob("syncUucUser2Cmdb")
    public void user2CMDB() {
        System.out.println("------------------【同步UUC用户 start】------------------");
        AjaxResult uucUsers = remoteSystemService.getUucUsers(SecurityConstants.INNER);
        Object uucUser= uucUsers.getData();
        List<UucUserInfo> uucUserList = JSONObject.parseArray(JSONUtil.toJsonStr(uucUser), UucUserInfo.class);
        AjaxResult cmdbEntityList = cmdbService.getUserEntityList(SecurityConstants.INNER);
        Object projectEntitys = cmdbEntityList.getData();
        List<ConfigEntity> cmdbConfigEntityList = JSONObject.parseArray(JSONUtil.toJsonStr(projectEntitys), ConfigEntity.class);
        List<Map<String, Object>> updateUsers = Lists.newArrayList();
        for (UucUserInfo userInfo : uucUserList) {
            Long id = userInfo.getId();
            String specialCode = String.valueOf(id);
            ConfigEntity configEntity = getEntityBySpecialCode(specialCode, cmdbConfigEntityList);
            Map<String, Object> uucUserMap = cmdbApiParseService.uucUser2Map(userInfo);
            if (Objects.isNull(configEntity)) {
                // insert
                Map<String, Object> param = Maps.newHashMap();
                param.put("userJson", uucUserMap);
                log.info("新增用户信息:" + JSONUtil.toJsonStr(param));
                updateUsers.add(param);
                continue;
            }
            Boolean isChange = isChangePropertyOrRel(configEntity, uucUserMap, "【用户】", userInfo.getUserName());
            if (isChange) {
                Map<String, Object> param = Maps.newHashMap();
                param.put("entityId", configEntity.getEntityId());
                param.put("userJson", uucUserMap);
                log.info("更新用户信息:" + JSONUtil.toJsonStr(param));
                updateUsers.add(param);
            }
        }
        Map request = Maps.newHashMap();
        request.put("updateUsers",updateUsers);
        //  批量保存用户,逐个存在因为实例保存先后顺序导致关系未同步过去的问题
        cmdbService.saveUucUser(request,SecurityConstants.INNER);

        List<String> currentSpecialCodes = uucUserList.stream().map(item -> String.valueOf(item.getId())).collect(Collectors.toList());
        List<String> exsistsSpecialCodes = cmdbConfigEntityList.stream().map(item -> String.valueOf(item.getEntityName())).collect(Collectors.toList());
        deleteSomeModelEntity(currentSpecialCodes, exsistsSpecialCodes, DingtalkRelConstants.Bind_Employee, "【删除用户实例】");
        System.out.println("------------------【同步UUC用户 end】------------------");
    }


    /*
    同步组织同步的关系:
    1. 组织管理组织
     */
    @XxlJob("syncUucDept2Cmdb")
    public void dept2CMDB() {
        System.out.println("------------------【同步UUC组织 start】------------------");
        AjaxResult uucDepts = remoteSystemService.getUucDepts(SecurityConstants.INNER);
        List<UucDeptInfo> uucDeptList = JSONObject.parseArray(JSONUtil.toJsonStr(uucDepts.getData()), UucDeptInfo.class);
        AjaxResult cmdbEntityList = cmdbService.getDeptEntityList(SecurityConstants.INNER);
        Object projectEntitys = cmdbEntityList.getData();
        List<ConfigEntity> cmdbConfigEntityList = JSONObject.parseArray(JSONUtil.toJsonStr(projectEntitys), ConfigEntity.class);
        List<Map<String, Object>> updateDepts = Lists.newArrayList();


        for (UucDeptInfo deptInfo : uucDeptList) {
            Long id = deptInfo.getId();
            String specialCode = String.valueOf(id);
            ConfigEntity configEntity = getEntityBySpecialCode(specialCode, cmdbConfigEntityList);
            Map<String, Object> uucDeptMap = cmdbApiParseService.uucDept2Map(deptInfo);
            if (Objects.isNull(configEntity)) {
                // insert
                Map<String, Object> param = Maps.newHashMap();
                param.put("deptJson", uucDeptMap);
                log.info("新增组织信息:" + JSONUtil.toJsonStr(param));
                updateDepts.add(param);
                continue;
            }
            Boolean isChange = isChangePropertyOrRel(configEntity, uucDeptMap, "【组织】", deptInfo.getDeptName());
            if (isChange) {
                Map<String, Object> param = Maps.newHashMap();
                param.put("entityId", configEntity.getEntityId());
                param.put("deptJson", uucDeptMap);
                log.info("更新组织信息:" + JSONUtil.toJsonStr(param));
                updateDepts.add(param);
            }
        }
        Map request = Maps.newHashMap();
        request.put("updateDepts",updateDepts);

        cmdbService.saveUucDept(request,SecurityConstants.INNER);


        List<String> currentSpecialCodes = uucDeptList.stream().map(item -> String.valueOf(item.getId())).collect(Collectors.toList());
        List<String> exsistsSpecialCodes = cmdbConfigEntityList.stream().map(item -> String.valueOf(item.getEntityName())).collect(Collectors.toList());
        deleteSomeModelEntity(currentSpecialCodes, exsistsSpecialCodes, DingtalkRelConstants.Bind_Organization, "【删除组织实例】");

        System.out.println("------------------【同步UUC组织 end】------------------");
    }





    private ConfigEntity getEntityBySpecialCode(String specialCode, List<ConfigEntity> cmdbConfigEntityList) {
        for (ConfigEntity configEntity : cmdbConfigEntityList) {
            if (specialCode.equals(configEntity.getEntityName())) {
                return configEntity;
            }
        }
        return null;
    }


    private void deleteSomeModelEntity(List<String> currentSpecialCodes, List<String> exsistsSpecialCodes, String modelKey, String logInfo) {
        exsistsSpecialCodes.removeAll(currentSpecialCodes);
        if (CollectionUtils.isNotEmpty(exsistsSpecialCodes)) {
            Map<String, Object> param = Maps.newHashMap();
            param.put("deleteIds", exsistsSpecialCodes);
            param.put("modelKey", modelKey);
            log.info(logInfo + JSONUtil.toJsonStr(param));
            cmdbService.deleteUucEntityByModelKey(param, SecurityConstants.INNER);
        }
    }

    //判断属性字段以及关系是否发生变更
    private Boolean isChangePropertyOrRel(ConfigEntity configEntity, Map<String, Object> sourceData, String sourceKey, String aboutName) {
        Boolean isChange = false;
        String entityForm = configEntity.getEntityForm();
        Map<String, Object> entityJson = JSONObject.parseObject(entityForm);
        Map<String, Object> params = configEntity.getParams();
        System.out.println("---CDMB---"+sourceKey+"关系列表:"+JSONUtil.toJsonStr(params));
        // 关系字段存在params中
        entityJson.putAll(params);
        Set<String> entityKeys = entityJson.keySet();
        for (String uucKey : sourceData.keySet()) {
            if (!entityKeys.contains(uucKey)) {
                continue;
            }
            Object now = sourceData.get(uucKey);
            Object origin = entityJson.get(uucKey);
            if (!StringUtils.null2Empty(origin).equals(StringUtils.null2Empty(now))) {
                isChange = true;
                log.info(sourceKey +"<"+ aboutName +">"+ " 变更项(旧值 -> 新值):" + uucKey + " : " + StringUtils.null2Empty(origin) + "  -->  " + StringUtils.null2Empty(now));
            }
        }
        return isChange;
    }

}
