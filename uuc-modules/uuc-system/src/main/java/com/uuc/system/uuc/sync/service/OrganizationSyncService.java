package com.uuc.system.uuc.sync.service;

import com.alibaba.fastjson.JSONObject;
import com.uuc.common.core.enums.ModelMarkType;
import com.uuc.common.core.enums.ModelOperationType;
import com.uuc.common.core.enums.ModelRelationType;
import com.uuc.common.core.exception.ServiceException;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.system.api.model.UucDeptInfo;
import com.uuc.system.api.model.UucUserDept;
import com.uuc.system.api.model.UucUserInfo;
import com.uuc.system.uuc.mapper.UucDeptInfoMapper;
import com.uuc.system.uuc.service.impl.UucDeptInfoService;
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
public class OrganizationSyncService implements ResourceSyncService{

    private static final Logger log = LoggerFactory.getLogger(OrganizationSyncService.class);
    private static final String DEPTMODELKEY= ModelMarkType.Bind_Organization.getCode();
    private static final String USERMODELKEY=ModelMarkType.Bind_Employee.getCode();

    @Autowired
    private UucDeptInfoMapper uucDeptInfoMapper;

    @Override
    public UucResourceDto syncBody(Object oldObject, Object newObject, String operationType) {
        log.info("start OrganizationSyncService...................");
        UucResourceDto uucResourceDto=new UucResourceDto();
        List<UucRelDto> uucRelDtoList=new ArrayList<>();
        try{
            UucDeptInfo uucDeptInfo=(UucDeptInfo)newObject;
            uucResourceDto.setModelKey(DEPTMODELKEY);
            uucResourceDto.setChangeType(operationType);
            if(operationType.equals(ModelOperationType.INSERT.getCode())){
                List<UucFieldDto> uucFieldDtoList = SyncUtil.insertSync(uucDeptInfo, uucResourceDto);//获取新增属性及属性值列表

                if(uucFieldDtoList!=null&&uucFieldDtoList.size()>0){
                    uucResourceDto.setFiledChangeList(uucFieldDtoList);
                }
                //新增组织管理组织关系，主要增加上级组织管理本级组织关系
                String parentCode = uucDeptInfo.getParentCode();
                if(StringUtils.isNotEmpty(parentCode)){
                    UucRelDto uucRelDto = addManagerRelation(uucDeptInfo);
                    if(uucRelDto!=null){
                        uucRelDtoList.add(uucRelDto);
                    }
                }
                //新增人员管理组织关系
                List<UucUserDept> userDeptList = uucDeptInfo.getUserDeptList();
                if(userDeptList!=null&&userDeptList.size()>0){
                    UucRelDto uucRelDto = addPeopleManageRelation(uucDeptInfo);
                    if(uucRelDto!=null){
                        uucRelDtoList.add(uucRelDto);
                    }
                }
                uucResourceDto.setRelChangeList(uucRelDtoList);
                return uucResourceDto;
            }else if(operationType.equals(ModelOperationType.UPDATE.getCode())){
                UucDeptInfo oldDept=(UucDeptInfo)oldObject;
                uucResourceDto.setModelCode(StringUtils.null2Empty(oldDept.getId()));
                List<UucFieldDto> uucFieldDtoList = SyncUtil.updateSync(oldDept, uucDeptInfo);//获取新增属性及属性值列表
                if(uucFieldDtoList!=null&&uucFieldDtoList.size()>0){
                    uucResourceDto.setFiledChangeList(uucFieldDtoList);
                }
                //变更组织管理组织关系
                UucRelDto uucRelDto = updateDeptManagerRelation(uucDeptInfo);
                if(uucRelDto!=null){
                    uucRelDtoList.add(uucRelDto);
                }
                //变更人员管理组织关系
                UucRelDto uucRelDto2 = updatePeopleManagerRelation(oldDept, uucDeptInfo);
                if(uucRelDto2!=null){
                    uucRelDtoList.add(uucRelDto2);
                }
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
            log.error("模型{}执行{}出错，错误信息：{}",DEPTMODELKEY,operationType,e.getMessage());
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
        UucDeptInfo newObject = jsonObject.getObject("newObject", UucDeptInfo.class);
        if(newObject!=null){
            syncBody.setNewObject(newObject);
        }
        if(operationType.equals(ModelOperationType.UPDATE.getCode())){
            UucDeptInfo oldObject= jsonObject.getObject("oldObject", UucDeptInfo.class);
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

    //新增上级组织管理组织关系
    public UucRelDto addManagerRelation(UucDeptInfo uucDeptInfo){
        UucRelDto uucRelDto=null;
        String parentCode=uucDeptInfo.getParentCode();
        UucDeptInfo uucDeptInfoPo = uucDeptInfoMapper.selectUucDeptInfoById(Long.parseLong(parentCode));
        if(uucDeptInfoPo!=null){
            uucRelDto=new UucRelDto();
            uucRelDto.setSourceModelKey(DEPTMODELKEY);
            uucRelDto.setRelationKey(ModelRelationType.MANAGE.getCode());
            uucRelDto.setTargetModelKey(DEPTMODELKEY);
            List<String> sourceModelCodes=new ArrayList<>();
            sourceModelCodes.add(parentCode);
            uucRelDto.setSourceModelCodes(sourceModelCodes);
        }
        return uucRelDto;
    }
    //新增人员管理组织关系
    public UucRelDto addPeopleManageRelation(UucDeptInfo uucDeptInfo){
        UucRelDto uucRelDto=new UucRelDto();
        List<UucUserDept> userDeptList = uucDeptInfo.getUserDeptList();
        uucRelDto.setSourceModelKey(USERMODELKEY);
        uucRelDto.setRelationKey(ModelRelationType.MANAGE.getCode());
        uucRelDto.setTargetModelKey(DEPTMODELKEY);
        List<String> sourceModelCodes=new ArrayList<>();
        sourceModelCodes.add(userDeptList.get(0).getUserCode());
        uucRelDto.setSourceModelCodes(sourceModelCodes);
        return uucRelDto;
    }
    //修改组织管理组织关系
    public UucRelDto updateDeptManagerRelation(UucDeptInfo newDept){
        UucRelDto uucRelDto=new UucRelDto();
        String newParentCode=newDept.getParentCode();
        uucRelDto.setSourceModelKey(DEPTMODELKEY);
        uucRelDto.setRelationKey(ModelRelationType.MANAGE.getCode());
        uucRelDto.setTargetModelKey(DEPTMODELKEY);
        List<String> sourceModelCodes=new ArrayList<>();
        if(StringUtils.isNotEmpty(newParentCode)){
            sourceModelCodes.add(newParentCode);
        }
        uucRelDto.setSourceModelCodes(sourceModelCodes);
        UucDeptInfo uucDeptInfo=new UucDeptInfo();
        uucDeptInfo.setParentCode(String.valueOf(newDept.getId()));
        List<UucDeptInfo> uucDeptInfos = uucDeptInfoMapper.selectUucDeptInfoList(uucDeptInfo);
        List<String> targetModelCodes=new ArrayList<>();
        if(uucDeptInfos!=null&&uucDeptInfos.size()>0){
            for(UucDeptInfo item:uucDeptInfos){
                targetModelCodes.add(String.valueOf(item.getId()));
            }
        }
        uucRelDto.setTargetModelCodes(targetModelCodes);
        return uucRelDto;
    }
    //修改人员管理组织关系
    public UucRelDto updatePeopleManagerRelation(UucDeptInfo oldDept,UucDeptInfo newDept){
        UucRelDto uucRelDto=null;
        List<UucUserDept> oldUserDeptList = oldDept.getUserDeptList();
        List<UucUserDept> newUserDeptList = newDept.getUserDeptList();
        String oldLeaderCode=null;
        String newLeaderCode=null;
        if(oldUserDeptList!=null&&oldUserDeptList.size()>0){
            oldLeaderCode=oldUserDeptList.get(0).getUserCode();
        }
        if(newUserDeptList!=null&&newUserDeptList.size()>0){
            newLeaderCode=newUserDeptList.get(0).getUserCode();
        }
        if(!StringUtils.null2Empty(oldLeaderCode).equals(StringUtils.null2Empty(newLeaderCode))){
            uucRelDto=new UucRelDto();
            uucRelDto.setSourceModelKey(USERMODELKEY);
            uucRelDto.setRelationKey(ModelRelationType.MANAGE.getCode());
            uucRelDto.setTargetModelKey(DEPTMODELKEY);
            List<String> sourceModelCodes=new ArrayList<>();
            sourceModelCodes.add(newLeaderCode);
            uucRelDto.setSourceModelCodes(sourceModelCodes);
        }
        return uucRelDto;
    }
}
