package com.uuc.system.uuc.service.impl;

import java.util.*;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import com.uuc.common.core.constant.SyncConstants;
import com.uuc.common.core.enums.ModelOperationType;
import com.uuc.common.core.enums.SyncBeanType;
import com.uuc.common.core.exception.CheckedException;
import com.uuc.common.core.utils.DateUtils;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.core.utils.uuid.IdUtils;
import com.uuc.common.security.utils.SecurityUtils;
import com.uuc.system.api.model.*;
import com.uuc.system.uuc.mapper.UucProjectDeptMapper;
import com.uuc.system.uuc.mapper.UucProjectUserMapper;
import com.uuc.system.uuc.sync.service.ToSyncBodyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.uuc.system.uuc.mapper.UucProjectMapper;
import org.springframework.transaction.annotation.Transactional;

import static com.uuc.common.core.utils.PageUtils.startPage;

/**
 * 项目信息Service业务层处理
 *
 * @author uuc
 * @date 2022-04-01
 */
@Service
@Slf4j
public class UucProjectService {

    @Autowired
    private UucProjectMapper uucProjectMapper;

    @Autowired
    private UucProjectUserMapper uucProjectUserMapper;

    @Autowired
    private UucProjectDeptMapper uucProjectDeptMapper;

    @Autowired
    private UucUserInfoService userInfoService;

    @Autowired
    private UucProjectUserService projectUserService;

    @Autowired
    private UucDeptInfoService deptInfoService;

    @Autowired
    private ToSyncBodyService toSyncBodyService;

    /**
     * 查询项目信息
     *
     * @param id 项目信息主键
     * @return 项目信息
     */

    public UucProject selectUucProjectById(Long id) {
        UucProjectDept projectDept = new UucProjectDept();
        projectDept.setProjectCode(String.valueOf(id));
        List<UucProjectDept> useDeptList = uucProjectDeptMapper.selectUucProjectDeptList(projectDept);

        UucProject project = uucProjectMapper.selectUucProjectById(id);
        project.setUseDeptList(useDeptList);
        return project;
    }

    /**
     * 查询项目关联人员
     *
     * @param id 项目信息主键
     * @return 项目信息
     */

    public List<UucProjectUser> getRelationUserList(Long id) {
//        List<UucProjectUser> uucProjectUserList = uucProjectUserMapper.selectUucProjectUserByProjectCode(String.valueOf(id));
//        List<String> checkList = new ArrayList<>();
//        for (UucProjectUser uucProjectUser: uucProjectUserList) {
//            checkList.add(uucProjectUser.getUserCode());
//        }
        return uucProjectUserMapper.selectUucProjectUserByProjectCode(String.valueOf(id));
    }

    /**
     * 查询项目信息列表
     *
     * @param uucProject 项目信息
     * @return 项目信息
     */

    public List<UucProject> selectUucProjectList(UucProject uucProject) {
        return uucProjectMapper.selectUucProjectList(uucProject);
    }

    /**
     * 新增项目信息
     *
     * @param uucProject 项目信息
     * @return 结果
     */

    public int insertUucProject(UucProject uucProject) {
        // 检查项目编码是否唯一
        int count = uucProjectMapper.checkProjectCodeUnique(uucProject.getProjectCode());
        if (count > 0) {
            throw new CheckedException("项目编码重复!");
        }
        Date nowData = DateUtils.getNowDate();
        String operator = String.valueOf(SecurityUtils.getUserId());

        uucProject.setId(IdUtils.snowflakeId());
        // 插入项目归属组织使用关系
        List<UucProjectDept> useDeptList = uucProject.getUseDeptList();
        if (CollectionUtils.isNotEmpty(useDeptList)) {
            for (UucProjectDept projectDept : useDeptList) {
                projectDept.setProjectCode(String.valueOf(uucProject.getId()));
                projectDept.setCreateBy(operator);
                projectDept.setCreateTime(nowData);
            }
            uucProjectDeptMapper.batchInsertUucProjectDept(useDeptList);
        }

        // 插入项目表
        uucProject.setCreateTime(nowData);
        uucProject.setCreateBy(operator);
        int i = uucProjectMapper.insertUucProject(uucProject);
        if(i>0){
            toSyncBodyService.insertSyncBody(uucProject, SyncBeanType.PROJECTSYNCBEAN.getCode());
        }
        return i;
    }

    /**
     * 修改项目信息
     *
     * @param uucProject 项目信息
     * @return 结果
     */

    public int updateUucProject(UucProject uucProject) {
        UucProject oldProject=uucProjectMapper.selectUucProjectById(uucProject.getId());
        List<UucProjectUser> userList = getRelationUserList(uucProject.getId());
        if(userList!=null&&userList.size()>0){
            uucProject.setUserList(userList);
        }
        oldProject.setUserList(userList);
        Date nowData = DateUtils.getNowDate();
        String operator = String.valueOf(SecurityUtils.getUserId());

        // 先删除项目归属组织使用关系
        String projectCode = String.valueOf(uucProject.getId());
        uucProjectDeptMapper.deleteUucProjectDeptByProjectCode(projectCode);
        // 插入项目归属组织使用关系
        List<UucProjectDept> useDeptList = uucProject.getUseDeptList();
        if (CollectionUtils.isNotEmpty(useDeptList)) {
            for (UucProjectDept projectDept : useDeptList) {
//                projectDept.setCreateBy(operator);
//                projectDept.setCreateTime(nowData);
                projectDept.setProjectCode(projectCode);
                projectDept.setUpdateBy(operator);
                projectDept.setUpdateTime(nowData);
            }
            uucProjectDeptMapper.batchInsertUucProjectDept(useDeptList);
        }

        // 修改组织
        uucProject.setUpdateTime(nowData);
        uucProject.setUpdateBy(operator);
        int i= uucProjectMapper.updateUucProject(uucProject);
        if(i>0){
            toSyncBodyService.updateSyncBody(oldProject,uucProject, SyncBeanType.PROJECTSYNCBEAN.getCode());
        }
        return i;
    }

    /**
     * 批量删除项目信息
     *
     * @param ids 需要删除的项目信息主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteUucProjectByIds(Long[] ids) {
        int i =  uucProjectMapper.deleteUucProjectByIds(ids);
        if(i>0){
            List<String> idList=new ArrayList<>();
            for(Long item:ids){
                idList.add(String.valueOf(item));
            }
            toSyncBodyService.deleteSyncBody(idList,SyncBeanType.PROJECTSYNCBEAN.getCode());
        }
        return 1;
    }

    /**
     * 删除项目信息信息
     *
     * @param id 项目信息主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteUucProjectById(Long id) {
//        //查找当前项目的所有子项目
//        List<Long> children = uucProjectMapper.selectChildrenProjectsByParentId(id);
//        children.add(id);
        Long[] ids = new Long[]{id};
        // 删除项目组织关联关系
        uucProjectDeptMapper.deleteUucProjectDeptByProjectCode(String.valueOf(id));
        // 删除项目用户关联关系
        uucProjectUserMapper.deleteUucProjectUserByProjectCodes(ids);
        // 删除项目数据
        int i= uucProjectMapper.deleteUucProjectByIds(ids);
        if(i>0){
            List<String> idList=new ArrayList<>();
            if(id!=null){
                idList.add(String.valueOf(id));
            }
            toSyncBodyService.deleteSyncBody(idList,SyncBeanType.PROJECTSYNCBEAN.getCode());
        }
        return i;
    }

    public List<UucProject> getWholeProjects() {
        List<UucProject> result = uucProjectMapper.selectAllProjects();
        // 关系数据 必须使用CMDB的唯一标识表示 ,也就是表的id
        if (CollectionUtils.isNotEmpty(result)) {
//            List<UucUserInfo> uucUserInfos = userInfoService.selectAllUser();
            for (UucProject uucProject : result) {
                Map<String, Object> params = uucProject.getParams();
                if (MapUtils.isEmpty(params)) {
                    params = Maps.newHashMap();
                    uucProject.setParams(params);
                }
                List<UucProjectUser> uucProjectUsers = projectUserService.selectUucProjectUserByProjectCode(String.valueOf(uucProject.getId()));
                List<String> joinProjectPeople = Lists.newArrayList();
                List<String> manageProjectPeople = Lists.newArrayList();
                List<String> maintenerProjectPeople = Lists.newArrayList();
                List<String> projectManageOrgs = Lists.newArrayList();
                List<String> projectUseOrgs = Lists.newArrayList();
                for (UucProjectUser tmp : uucProjectUsers) {
                    String adminFlag = tmp.getAdminFlag();
                    String maintenerFlag = tmp.getMaintenerFlag();
                    // userCode是用户id
                    String userCode = tmp.getUserCode();
                    // 人员 参与 项目
                    joinProjectPeople.add(userCode);
                    if ("1".equals(adminFlag)) {
                        // 人员 管理  项目
                        manageProjectPeople.add(userCode);
                    }
                    // 人员运维项目
                    if ("1".equals(maintenerFlag)) {
                        maintenerProjectPeople.add(userCode);
                    }
                }
                // 组织管理项目 与 组织使用项目
                String ownDeptCode = uucProject.getOwnDeptCode();
                if (StringUtils.isNotBlank(ownDeptCode)) {
                    UucDeptInfo uucDeptInfo = deptInfoService.selectUucDeptInfoById(Long.parseLong(ownDeptCode));
                    if (Objects.nonNull(uucDeptInfo)) {
                        projectManageOrgs.add(String.valueOf(uucDeptInfo.getId()));
                    }
                }
                // Todo 组织使用项目
                projectUseOrgs.addAll(this.selectUsedDepts(String.valueOf(uucProject.getId())));
                CollectionUtil.sortByPinyin(joinProjectPeople);
                CollectionUtil.sortByPinyin(manageProjectPeople);
                CollectionUtil.sortByPinyin(maintenerProjectPeople);
                CollectionUtil.sortByPinyin(projectManageOrgs);
                CollectionUtil.sortByPinyin(projectUseOrgs);
                params.put(SyncConstants.EMPLOYEE_JOIN_PROJECT, joinProjectPeople);
                params.put(SyncConstants.EMPLOYEE_MANAGE_PROJECT, manageProjectPeople);
                params.put(SyncConstants.EMPLOYEE_MAINTAIN_PROJECT, maintenerProjectPeople);
                params.put(SyncConstants.ORG_MANAGE_PROJECT, projectManageOrgs);
                params.put(SyncConstants.ORG_USE_PROJECT, projectUseOrgs);
            }
        }


        return result;

    }

    private List<String> selectUsedDepts(String deptCode) {
        return uucProjectDeptMapper.selectProjectUsedDepts(deptCode);
    }


    /**
     * 保存项目关联人员
     *
     * @param uucProject
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int saveRelationUsers(UucProject uucProject) {

        // 先删除关系，再新增关系
        Long projectId = uucProject.getId();
        List<UucProjectUser> userProjectList= getRelationUserList(uucProject.getId());

        uucProjectUserMapper.deleteUucProjectUserByProjectCode(String.valueOf(projectId));
        List<UucProjectUser> userList = uucProject.getUserList();
        if (CollectionUtils.isNotEmpty(userList)) {
            Date nowDate = DateUtils.getNowDate();
            String userId = String.valueOf(SecurityUtils.getUserId());
            for (UucProjectUser projectUser : userList) {
                projectUser.setProjectCode(String.valueOf(projectId));
//                projectUser.setAdminFlag("0");
                projectUser.setCreateBy(userId);
                projectUser.setCreateTime(nowDate);
                projectUser.setUpdateBy(userId);
                projectUser.setUpdateTime(nowDate);
            }
            UucProject oldProject=uucProjectMapper.selectUucProjectById(uucProject.getId());

            UucProjectDept projectDept = new UucProjectDept();
            projectDept.setProjectCode(String.valueOf(uucProject.getId()));
            List<UucProjectDept> useDeptList = uucProjectDeptMapper.selectUucProjectDeptList(projectDept);
            oldProject.setUserList(userProjectList);
            oldProject.setUseDeptList(useDeptList);
            int i = uucProjectUserMapper.batchInsertUucProjectUser(userList);
            if(i>0){
                UucProject newProject=oldProject;
                newProject.setUserList(userList);
                toSyncBodyService.updateSyncBody(oldProject,newProject, SyncBeanType.PROJECTSYNCBEAN.getCode());
            }
        }
        return 1;
    }
    /**
     * 查询项目关联用户，带搜索
     */

    public List<UucProjectUser> selectUucProjectUserByCond(UucProjectUser uucProjectUser){
        startPage();
        return uucProjectUserMapper.selectUucProjectUserByCond(uucProjectUser);
    }
}
