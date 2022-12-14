package com.uuc.system.uuc.sync.service;

import com.alibaba.fastjson.JSONObject;
import com.uuc.common.core.enums.AdminFlag;
import com.uuc.common.core.enums.ModelMarkType;
import com.uuc.common.core.enums.ModelOperationType;
import com.uuc.common.core.enums.ModelRelationType;
import com.uuc.common.core.exception.ServiceException;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.system.api.model.UucDeptInfo;
import com.uuc.system.api.model.UucProject;
import com.uuc.system.api.model.UucProjectDept;
import com.uuc.system.api.model.UucProjectUser;
import com.uuc.system.uuc.mapper.UucDeptInfoMapper;
import com.uuc.system.uuc.mapper.UucProjectMapper;
import com.uuc.system.uuc.mapper.UucProjectUserMapper;
import com.uuc.system.uuc.service.impl.UucDeptInfoService;
import com.uuc.system.uuc.service.impl.UucProjectService;
import com.uuc.system.uuc.sync.domain.SyncBody;
import com.uuc.system.uuc.sync.domain.UucFieldDto;
import com.uuc.system.uuc.sync.domain.UucRelDto;
import com.uuc.system.uuc.sync.domain.UucResourceDto;
import com.uuc.system.uuc.sync.util.SyncUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProjectSyncService implements ResourceSyncService{
    private static final Logger log = LoggerFactory.getLogger(ProjectSyncService.class);
    private static final String DEPTMODELKEY= ModelMarkType.Bind_Organization.getCode();
    private static final String USERMODELKEY=ModelMarkType.Bind_Employee.getCode();
    private static final String PROJECTMODELKEY=ModelMarkType.Bind_Project.getCode();

    @Autowired
    private UucDeptInfoMapper uucDeptInfoMapper;
    @Autowired
    private UucProjectUserMapper uucProjectUserMapper;

    @Override
    public UucResourceDto syncBody(Object oldObject, Object newObject, String operationType) {
        log.info("start ProjectSyncService...................");
        UucResourceDto uucResourceDto=new UucResourceDto();
        List<UucRelDto> uucRelDtoList=new ArrayList<>();
        try{
            UucProject uucProject=(UucProject)newObject;
            uucResourceDto.setModelKey(PROJECTMODELKEY);
            uucResourceDto.setChangeType(operationType);
            if(operationType.equals(ModelOperationType.INSERT.getCode())){
                List<UucFieldDto> uucFieldDtoList = SyncUtil.insertSync(uucProject, uucResourceDto);//????????????????????????????????????
                if(uucFieldDtoList!=null&&uucFieldDtoList.size()>0){
                    uucResourceDto.setFiledChangeList(uucFieldDtoList);
                }
                //??????????????????????????????
                String manageDeptCode = uucProject.getOwnDeptCode();
                if(StringUtils.isNotEmpty(manageDeptCode)){
                    UucRelDto uucRelDto = addDeptManagerRelation(uucProject);
                    if(uucRelDto!=null){
                        uucRelDtoList.add(uucRelDto);
                    }
                }
                //????????????????????????
                UucRelDto uucRelDto = addDeptUseRelation(uucProject);
                if(uucRelDto!=null){
                    uucRelDtoList.add(uucRelDto);
                }
                uucResourceDto.setRelChangeList(uucRelDtoList);
                return uucResourceDto;
            }else if(operationType.equals(ModelOperationType.UPDATE.getCode())){
                UucProject oldProject=(UucProject)oldObject;
                uucResourceDto.setModelCode(StringUtils.null2Empty(oldProject.getId()));
                List<UucFieldDto> uucFieldDtoList = SyncUtil.updateSync(oldProject, uucProject);//????????????????????????????????????
                if(uucFieldDtoList!=null&&uucFieldDtoList.size()>0){
                    uucResourceDto.setFiledChangeList(uucFieldDtoList);
                }
                //??????????????????????????????
                UucRelDto uucRelDto = updateDeptManagerRelation(oldProject, uucProject);
                if(uucRelDto!=null){
                    uucRelDtoList.add(uucRelDto);
                }
                //??????????????????????????????
                UucRelDto uucRelDto2 = updateDeptUseRelation(oldProject, uucProject);
                if(uucRelDto2!=null){
                    uucRelDtoList.add(uucRelDto2);
                }
                //??????????????????????????????????????????????????????????????????
                updatePeopleXXRelation(uucProject,uucRelDtoList);
                uucResourceDto.setRelChangeList(uucRelDtoList);
                return uucResourceDto;
            }else if(operationType.equals(ModelOperationType.DELETE.getCode())){
                List list=(List)oldObject;
                if(list!=null&&list.size()>0){
                    List<String> deleteModelCodes=new ArrayList<>();
                    for(Object item:list){
                        deleteModelCodes.add(StringUtils.null2Empty(item));
                    }
                    uucResourceDto.setDeleteModelCodes(deleteModelCodes);
                }
                return uucResourceDto;
            }else {
                log.error("??????????????????..................................");
                throw new ServiceException("??????????????????");
            }
        }catch (Exception e){
            log.error("??????{}??????{}????????????????????????{}",DEPTMODELKEY,operationType,e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public SyncBody toSyncBody(String jsonString) {
        SyncBody syncBody=new SyncBody();
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        String beanName = String.valueOf(jsonObject.get("beanName"));
        String operationType=String.valueOf(jsonObject.get("operationType"));
        syncBody.setOperationType(operationType);
        syncBody.setBeanName(beanName);
        UucProject newObject = jsonObject.getObject("newObject", UucProject.class);
        if(newObject!=null){
            syncBody.setNewObject(newObject);
        }
        if(operationType.equals(ModelOperationType.UPDATE.getCode())){
            UucProject oldObject= jsonObject.getObject("oldObject", UucProject.class);
            if(oldObject!=null){
                syncBody.setOldObject(oldObject);
            }
        }
        if(operationType.equals(ModelOperationType.DELETE.getCode())){
            List list=jsonObject.getObject("oldObject", List.class);
            if(list!=null){
                syncBody.setOldObject(list);
            }
        }
        return syncBody;

    }

    //??????????????????????????????
    public UucRelDto addDeptManagerRelation(UucProject uucProject){
        UucRelDto uucRelDto=null;
        String manageDeptCode=uucProject.getOwnDeptCode();
        UucDeptInfo uucDeptInfo1 = uucDeptInfoMapper.selectByDeptCode(manageDeptCode);
        if(uucDeptInfo1!=null){
            uucRelDto=new UucRelDto();
            uucRelDto.setSourceModelKey(DEPTMODELKEY);
            uucRelDto.setRelationKey(ModelRelationType.MANAGE.getCode());
            uucRelDto.setTargetModelKey(PROJECTMODELKEY);
            List<String> sourceModelCodes=new ArrayList<>();
            sourceModelCodes.add(manageDeptCode);
            uucRelDto.setSourceModelCodes(sourceModelCodes);
        }
        return uucRelDto;
    }
    //??????????????????????????????
    public UucRelDto addDeptUseRelation(UucProject uucProject){
        UucRelDto uucRelDto=null;
        List<UucProjectDept> useDeptList = uucProject.getUseDeptList();
        if(useDeptList!=null&&useDeptList.size()>0){
            uucRelDto=new UucRelDto();
            uucRelDto.setSourceModelKey(DEPTMODELKEY);
            uucRelDto.setRelationKey(ModelRelationType.USE.getCode());
            uucRelDto.setTargetModelKey(PROJECTMODELKEY);
            List<String> sourceModelCodes=new ArrayList<>();
            for(UucProjectDept item:useDeptList){
                sourceModelCodes.add(item.getDeptCode());
            }
            uucRelDto.setSourceModelCodes(sourceModelCodes);
        }
        return uucRelDto;
    }

    //??????????????????????????????
    public UucRelDto updateDeptManagerRelation(UucProject oldProject,UucProject newProject){
        UucRelDto uucRelDto=null;
        String oldDeptCode=oldProject.getOwnDeptCode();
        String newDpetCode=newProject.getOwnDeptCode();
        if(!StringUtils.null2Empty(oldDeptCode).equals(StringUtils.null2Empty(newDpetCode))){
            uucRelDto=new UucRelDto();
            uucRelDto.setSourceModelKey(DEPTMODELKEY);
            uucRelDto.setRelationKey(ModelRelationType.MANAGE.getCode());
            uucRelDto.setTargetModelKey(PROJECTMODELKEY);
            List<String> sourceModelCodes=new ArrayList<>();
            sourceModelCodes.add(newDpetCode);
            uucRelDto.setSourceModelCodes(sourceModelCodes);
        }
        return uucRelDto;
    }
    //??????????????????????????????
    public UucRelDto updateDeptUseRelation(UucProject oldProject,UucProject newProject){
        UucRelDto uucRelDto=new UucRelDto();
        uucRelDto.setSourceModelKey(DEPTMODELKEY);
        uucRelDto.setRelationKey(ModelRelationType.USE.getCode());
        uucRelDto.setTargetModelKey(PROJECTMODELKEY);
        List<String> sourceModelCodes=new ArrayList<>();
        List<UucProjectDept> useDeptList = newProject.getUseDeptList();
        if(useDeptList!=null&&useDeptList.size()>0) {
            for (UucProjectDept item : useDeptList) {
                sourceModelCodes.add(item.getDeptCode());
            }
        }
        uucRelDto.setSourceModelCodes(sourceModelCodes);
        return uucRelDto;
    }
    //????????????????????????????????????????????????????????????????????????
    public void updatePeopleXXRelation(UucProject newProject,List<UucRelDto> uucRelDtoList){
        //????????????????????????
        UucRelDto manageUucRelDto=new UucRelDto();
        manageUucRelDto.setSourceModelKey(USERMODELKEY);
        manageUucRelDto.setRelationKey(ModelRelationType.MANAGE.getCode());
        manageUucRelDto.setTargetModelKey(PROJECTMODELKEY);
        List<String> peopleManageList=new ArrayList<>();
//        Long projectId = newProject.getId();
        //????????????????????????
        UucRelDto mainTainUucRelDto=new UucRelDto();
        mainTainUucRelDto.setSourceModelKey(USERMODELKEY);
        mainTainUucRelDto.setRelationKey(ModelRelationType.MAINTAIN.getCode());
        mainTainUucRelDto.setTargetModelKey(PROJECTMODELKEY);
        List<String> peopleMaintainList=new ArrayList<>();
        //????????????????????????
        UucRelDto joinUucRelDto=new UucRelDto();
        joinUucRelDto.setSourceModelKey(USERMODELKEY);
        joinUucRelDto.setRelationKey(ModelRelationType.JOIN.getCode());
        joinUucRelDto.setTargetModelKey(PROJECTMODELKEY);
        List<String> peopleJoinList=new ArrayList<>();

        //?????????????????????
        List<UucProjectUser> relationUserList = newProject.getUserList()==null?new ArrayList<>():newProject.getUserList();
        //List<UucProjectUser> relationUserList = uucProjectUserMapper.selectUucProjectUserByProjectCode(String.valueOf(projectId));
        if(relationUserList!=null||relationUserList.size()>0){
            for(UucProjectUser item:relationUserList){
                peopleJoinList.add(item.getUserCode());
                //?????????????????????
                if(item.getAdminFlag().equals(AdminFlag.IS.getCode())){
                    peopleManageList.add(item.getUserCode());
                }
                //?????????????????????
                if(item.getMaintenerFlag().equals(AdminFlag.IS.getCode())){
                    peopleMaintainList.add(item.getUserCode());
                }
            }
        }
        manageUucRelDto.setSourceModelCodes(peopleManageList);
        mainTainUucRelDto.setSourceModelCodes(peopleMaintainList);
        joinUucRelDto.setSourceModelCodes(peopleJoinList);
        uucRelDtoList.add(manageUucRelDto);
        uucRelDtoList.add(mainTainUucRelDto);
        uucRelDtoList.add(joinUucRelDto);
    }
}
