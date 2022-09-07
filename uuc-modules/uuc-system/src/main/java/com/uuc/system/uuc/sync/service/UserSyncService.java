package com.uuc.system.uuc.sync.service;

import com.alibaba.fastjson.JSONObject;
import com.uuc.common.core.enums.ModelMarkType;
import com.uuc.common.core.enums.ModelOperationType;
import com.uuc.common.core.enums.ModelRelationType;
import com.uuc.common.core.exception.ServiceException;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.system.api.model.UucUserDept;
import com.uuc.system.api.model.UucUserInfo;
import com.uuc.system.uuc.mapper.UucUserInfoMapper;
import com.uuc.system.uuc.service.impl.UucUserInfoService;
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
import java.util.List;

@Service
public class UserSyncService implements ResourceSyncService{

    private static final Logger log = LoggerFactory.getLogger(UserSyncService.class);
    private static final String USERMODELKEY=ModelMarkType.Bind_Employee.getCode();
    private static final String DEPTMODELKEY=ModelMarkType.Bind_Organization.getCode();



    @Autowired
    private UucUserInfoMapper uucUserInfoMapper;
    @Override
    public UucResourceDto syncBody(Object oldObject, Object newObject,String operationType) {
        log.info("start userSyncService...................");
        UucResourceDto uucResourceDto=new UucResourceDto();
        List<UucRelDto> uucRelDtoList=new ArrayList<>();
        try{
            UucUserInfo uucUserInfo=(UucUserInfo)newObject;
            uucResourceDto.setModelKey(USERMODELKEY);
            uucResourceDto.setChangeType(operationType);
            if(operationType.equals(ModelOperationType.INSERT.getCode())){
                List<UucFieldDto> uucFieldDtoList = SyncUtil.insertSync(uucUserInfo, uucResourceDto);//获取新增属性及属性值列表
                if(uucFieldDtoList!=null&&uucFieldDtoList.size()>0){
                    uucResourceDto.setFiledChangeList(uucFieldDtoList);
                }
                 //新增人员管理人员关系，主要增加上级管理本人关系
                String leaderCode = uucUserInfo.getLeaderCode();
                if(StringUtils.isNotEmpty(leaderCode)){
                    UucRelDto uucRelDto = addManagerRelation(uucUserInfo);
                    if(uucRelDto!=null){
                        uucRelDtoList.add(uucRelDto);
                    }
                }
                //新增组织包含人员关系
                List<UucUserDept> deptList = uucUserInfo.getDeptList();
                if(deptList!=null&&deptList.size()>0){
                    UucRelDto uucRelDto = addDeptMainTainRelation(uucUserInfo);
                    if(uucRelDto!=null){
                        uucRelDtoList.add(uucRelDto);
                    }
                }
                uucResourceDto.setRelChangeList(uucRelDtoList);
                return uucResourceDto;
            }else if(operationType.equals(ModelOperationType.UPDATE.getCode())){
                UucUserInfo oldUser=(UucUserInfo)oldObject;
                uucResourceDto.setModelCode(StringUtils.null2Empty(oldUser.getId()));
                List<UucFieldDto> uucFieldDtoList = SyncUtil.updateSync(oldUser, uucUserInfo);//获取新增属性及属性值列表
                if(uucFieldDtoList!=null&&uucFieldDtoList.size()>0){
                    uucResourceDto.setFiledChangeList(uucFieldDtoList);
                }
                //变更人员管理人员关系，包括上级领导关系变更或者下级人员关系变更
                UucRelDto uucRelDto = updateManagerRelation(uucUserInfo);
                if(uucRelDto!=null){
                    uucRelDtoList.add(uucRelDto);
                }
                //变更组织包含人员关系
                List<UucUserDept> uucUserDepts=uucUserInfo.getDeptList();
                UucRelDto deptUucRelDto=new UucRelDto();
                deptUucRelDto.setSourceModelKey(DEPTMODELKEY);
                deptUucRelDto.setRelationKey(ModelRelationType.CONTAINS.getCode());
                deptUucRelDto.setTargetModelKey(USERMODELKEY);
                List<String> sourceModelCodes=new ArrayList<>();
                for(UucUserDept item:uucUserDepts){
                    sourceModelCodes.add(item.getDeptCode());
                }
                deptUucRelDto.setSourceModelCodes(sourceModelCodes);
                uucRelDtoList.add(deptUucRelDto);
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
                log.error("未知操作类型..................................");
                throw new ServiceException("未知操作类型");
            }
        }catch (Exception e){
            log.error("模型{}执行{}出错，错误信息：{}",USERMODELKEY,operationType,e.getMessage());
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
        UucUserInfo newObject = jsonObject.getObject("newObject", UucUserInfo.class);
        if(newObject!=null){
            syncBody.setNewObject(newObject);
        }
        if(operationType.equals(ModelOperationType.UPDATE.getCode())){
            UucUserInfo oldObject= jsonObject.getObject("oldObject", UucUserInfo.class);
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

    //新增上级领导管理人员关系
    public UucRelDto addManagerRelation(UucUserInfo uucUserInfo){
        UucRelDto uucRelDto=null;
        //查找上级领导
        UucUserInfo uucUserInfoPo = uucUserInfoMapper.selectUucUserInfoById(Long.parseLong(uucUserInfo.getLeaderCode()));
        if(uucUserInfoPo!=null&&uucUserInfoPo.getId()!=null){
            uucRelDto=new UucRelDto();
            uucRelDto.setSourceModelKey(USERMODELKEY);
            uucRelDto.setRelationKey(ModelRelationType.MANAGE.getCode());
            uucRelDto.setTargetModelKey(USERMODELKEY);
            List<String> sourceModelCodes=new ArrayList<>();
            sourceModelCodes.add(StringUtils.null2Empty(uucUserInfo.getLeaderCode()));
            uucRelDto.setSourceModelCodes(sourceModelCodes);
        }
        return uucRelDto;
    }
    //新增组织包含人员关系
    public UucRelDto addDeptMainTainRelation(UucUserInfo uucUserInfo){
        UucRelDto uucRelDto=null;
        List<UucUserDept> deptList = uucUserInfo.getDeptList();
        if(deptList!=null&&deptList.size()>0){
            uucRelDto=new UucRelDto();
            uucRelDto.setSourceModelKey(DEPTMODELKEY);
            uucRelDto.setRelationKey(ModelRelationType.CONTAINS.getCode());
            uucRelDto.setTargetModelKey(USERMODELKEY);
            List<String> sourceModelCodes=new ArrayList<>();
            for(UucUserDept item:deptList){
                sourceModelCodes.add(item.getDeptCode());
            }
            uucRelDto.setSourceModelCodes(sourceModelCodes);
        }
        return uucRelDto;
    }
    //修改人员管理人员关系
    public UucRelDto updateManagerRelation(UucUserInfo newUser){
        UucRelDto uucRelDto=new UucRelDto();
        String newLeaderCode=newUser.getLeaderCode();
        List<String> sourceModelCodes=new ArrayList<>();
        uucRelDto.setSourceModelKey(USERMODELKEY);
        uucRelDto.setRelationKey(ModelRelationType.MANAGE.getCode());
        uucRelDto.setTargetModelKey(USERMODELKEY);
        if(StringUtils.isNotEmpty(newLeaderCode)){
            sourceModelCodes.add(newLeaderCode);
        }
        uucRelDto.setSourceModelCodes(sourceModelCodes);
        //判断用户的下级人员
        UucUserInfo uucUserInfo=new UucUserInfo();
        uucUserInfo.setLeaderCode(String.valueOf(newUser.getId()));
        List<UucUserInfo> uucUserInfos = uucUserInfoMapper.selectUucUserInfoList(uucUserInfo);
        List<String> targetModelCodes=new ArrayList<>();
        if(uucUserInfos!=null&&uucUserInfos.size()>0){
            for(UucUserInfo item:uucUserInfos){
                targetModelCodes.add(String.valueOf(item.getId()));
            }
        }
        uucRelDto.setTargetModelCodes(targetModelCodes);
        return uucRelDto;
    }
}
