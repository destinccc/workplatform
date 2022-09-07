package com.uuc.resource.apis.external.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.uuc.common.core.constant.CmdbConstants;
import com.uuc.common.core.constant.Constants;
import com.uuc.common.core.constant.HttpStatus;
import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.domain.R;
import com.uuc.common.core.exception.ServiceException;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.redis.service.RedisService;
import com.uuc.resource.apis.external.config.ResourceProperties;
import com.uuc.resource.apis.external.controller.DeptResourceController;
import com.uuc.resource.apis.inner.vo.DeptTreeVo;
import com.uuc.resource.apis.inner.vo.TreeSelect;
import com.uuc.system.api.RemoteClientService;
import com.uuc.system.api.RemoteCmdbService;
import com.uuc.system.api.RemoteMonitorTransService;
import com.uuc.system.api.RemoteSystemService;
import com.uuc.system.api.domain.UucModelInfo;
import com.uuc.system.api.model.UucDeptInfo;
import com.uuc.system.api.model.cmdb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author deng
 * @date 2022/7/18 0018
 * @description 部门资源相关接口
 */
@Service
public class DeptResourceService {

    private static final Logger log = LoggerFactory.getLogger(DeptResourceService.class);

    @Autowired
    private RemoteClientService remoteClientService;
    @Autowired
    private ResourceProperties resourceProperties;
    @Autowired
    private RemoteCmdbService remoteCmdbService;
    @Autowired
    private RemoteSystemService systemService;

    @Autowired
    private RemoteMonitorTransService remoteMonitorTransService;

    /**
     * 根据顶级组织code获取树形结构
     * @param deptCode
     * @param checkDept 如果是查询所有的卫健委组织的树形结构则不需要做校验，否则需要校验
     * @return
     */
    public List<DeptTreeVo> getDeptTree(String deptCode,boolean checkDept){
        if(checkDept){
            List<String> allDept = getAllDept();
            if(!allDept.contains(deptCode)){
                log.error("非法组织参数,组织code为:{}",deptCode);
                throw new ServiceException("非法组织参数",HttpStatus.BAD_REQUEST);
            }
        }
        List<DeptPo> deptList = getDeptList(deptCode);
        //构建树形结构
        List<UucDeptInfo> uucDeptInfos = this.caseListMapToListBean(systemService.selectAllDepts(SecurityConstants.INNER).getData(), UucDeptInfo.class);
        if(deptList==null||deptList.size()==0){
            //没有子集
            for(UucDeptInfo item:uucDeptInfos){
                if(String.valueOf(item.getId()).equals(deptCode)){
                    DeptTreeVo deptTreeVo=new DeptTreeVo();
                    deptTreeVo.setDeptId(deptCode);
                    deptTreeVo.setDeptCode(deptCode);
                    deptTreeVo.setDeptName(item.getDeptName());
                    deptTreeVo.setLevel(item.getLevel());
                    deptTreeVo.setRemark(item.getRemark());
                    deptTreeVo.setChildren(null);
                    List<DeptTreeVo> deptTreeVos=new ArrayList<>();
                    deptTreeVos.add(deptTreeVo);
                    return deptTreeVos;
                }
            }
        }
        return getTree(uucDeptInfos,deptList);
    }

    /**
     * 获取卫健委所有的组织
     */
    public List<String> getAllDept(){
        String clientId= resourceProperties.getWClientId();
        if(StringUtils.isEmpty(clientId)){
            throw new ServiceException("配置文件缺少客户端Id配置", HttpStatus.ERROR);
        }
        UucModelInfo uucModelInfo = getClient(clientId);
        if(uucModelInfo.getDeptCode()==null){
            throw new ServiceException("客户端参数异常",HttpStatus.ERROR);
        }
        List<String> deptCodes=new ArrayList<>();
        deptCodes.add(String.valueOf(uucModelInfo.getDeptCode()));
        List<DeptPo> deptList = getDeptList(String.valueOf(uucModelInfo.getDeptCode()));
        if(deptList!=null&&deptList.size()>0){
            for(DeptPo item:deptList){
                if(item!=null&&item.getDeptId()!=null){
                    deptCodes.add(item.getDeptId());
                }
            }
        }
        return deptCodes;
    }

    /**
     * 获取头部clientId
     * @param reClientId
     * @return
     */
    public UucModelInfo getClient(String reClientId){
        if(StringUtils.isEmpty(reClientId)){
            throw new ServiceException("客户端参数未配置");
        }
        R<UucModelInfo> uucModelInfoR = remoteClientService.checkClient(reClientId, SecurityConstants.INNER);
        int code = uucModelInfoR.getCode();
        if(code!= Constants.SUCCESS){
            throw new ServiceException("远程调用客户端校验方法失败");
        }
        UucModelInfo uucModelInfo = uucModelInfoR.getData();
        if(uucModelInfo==null){
            throw new ServiceException("客户端校验异常");
        }
        return uucModelInfo;
    }

    /**
     * 根据组织code获取组织下面的所有组织列表
     * @param deptCode
     * @return
     */
    public List<DeptPo> getDeptList(String deptCode){
        DeptPo deptPo=new DeptPo();
        deptPo.setDeptCode(String.valueOf(deptCode));
        R<List<DeptPo>> listR = remoteCmdbService.deptList(deptPo, SecurityConstants.INNER);
        return listR.getData();
    }

    /**
     * 校验当前组织是否是卫健委下面的组织
     * @param deptCode
     * @return
     */
    public boolean checkDept(String deptCode){
       boolean checkFlag=false;
        List<String> allDept = getAllDept();
        if(allDept.contains(deptCode)){
            checkFlag=true;
        }
        return checkFlag;
    }
    /**
     * 转化feign调用之后的List<Map>
     * @param source
     * @param clz
     * @param <T>
     * @return
     */
    private <T> List<T> caseListMapToListBean(Object source, Class<T> clz) {
        if (source != null) {
            String json = JSON.toJSONString(source);
            return JSON.parseArray(json, clz);
        }
        return new ArrayList<>();
    }

    /**
     * 查询组织关联项目
     * @param deptCode
     * @return
     */
    private List<ProjectDto> deptProjectList(String deptCode){
        List<String> deptCodes=new ArrayList<>();
        deptCodes.add(deptCode);
        // 查询所有组织的所有项目
        DeptResourceRequest deptResourceRequest = new DeptResourceRequest();
        deptResourceRequest.setDeptCode(deptCodes);
        List<ProjectDto> queryList = remoteCmdbService.deptProjects(deptResourceRequest, SecurityConstants.INNER).getData();
        return queryList;
    }

    /**
     * 查询资源
     * @param requestBodyVo
     * @return
     */
    public ResourceInfo getResource(RequestBodyVo requestBodyVo){
        /*String deptCode = requestBodyVo.getDeptCode();
        String projectCode=requestBodyVo.getProjectCode();

        if(StringUtils.isEmpty(deptCode)){//为空的话就需要找卫健委的顶级组织
            deptCode=getDeptCode();
        }*/
        List<String> projectList = verifyProjectList(requestBodyVo.getDeptCode(), requestBodyVo.getProjectCode());
        //调用cmdb接口获取资源
        log.info("requestBodyVo:{},开始执行远程cmdb获取资源信息================================",requestBodyVo);
        R<ResourceInfo> resourceInfoR= remoteCmdbService.serverTotalInfo(requestBodyVo, SecurityConstants.INNER);
        log.info("执行远程cmdb获取云平台资源完成,获取的数据为:{}",resourceInfoR.getData());
        ResourceInfo resourceInfo = resourceInfoR.getData();
        //加个判断，保障所有的参数存在
        checkParam(resourceInfo);
        //调用监控接口获取资源，先需要从cmdb查询资源列表
        ProjectResourceRequest project = new ProjectResourceRequest();
        project.setProjectCode(projectList);
        project.setAdminFlag(true);
        List<String> resourceTypeList=new ArrayList<>();
        resourceTypeList.add(CmdbConstants.VirtualMachine);
        project.setResType(resourceTypeList);
        log.info("project:{},开始执行远程cmdb获取资源信息================================",project);
        List<ResourceInfoDto> resourceInfoDtos = remoteCmdbService.projectResources(project, SecurityConstants.INNER).getData();
        log.info("执行远程cmdb获取云平台资源完成,获取的数据为:{}",resourceInfoR);
        List<String> resourceIds = resourceInfoDtos.stream().map(ResourceInfoDto::getResourceId).collect(Collectors.toList());
        if(resourceIds==null||resourceIds.size()<=0){
            return null;
        }
        //构造监控服务请求，查询
        MonitorResourceVo monitorResourceVo=new MonitorResourceVo();
        monitorResourceVo.setResource_ids(resourceIds.toArray(new String[resourceIds.size()]));
        monitorResourceVo.setVcpu(resourceInfo.getVcpu());
        monitorResourceVo.setStorage(resourceInfo.getStorage());
        monitorResourceVo.setMemory(resourceInfo.getMemory());
        log.info("monitorResourceVo:{},开始执行远程monitor获取资源信息================================",monitorResourceVo);
        ResourceInfo deptReource = remoteMonitorTransService.getDeptReource(monitorResourceVo, SecurityConstants.INNER).getData();
        log.info("执行远程monitor获取云平台资源完成,获取的数据为:{}",deptReource);
        if(deptReource!=null){
            deptReource.setVcpu(resourceInfo.getVcpu());
            deptReource.setMemory(resourceInfo.getMemory());
            deptReource.setStorage(resourceInfo.getStorage());
        }
        return deptReource;
    }

    /**
     * 校验资源参数
     * @param resourceInfo
     */
    private void checkParam(ResourceInfo resourceInfo){
        if(resourceInfo==null||resourceInfo.getMemory()==null||resourceInfo.getStorage()==null||resourceInfo.getVcpu()==null){
            log.error("远程获取的资源对象为:{}",resourceInfo);
            throw new ServiceException("资源对象缺少属性值");
        }
    }

    /**
     * 获取卫健委顶级组织
     * @return
     */
    public String getDeptCode(){
        R<UucModelInfo> uucModelInfoR = remoteClientService.checkClient(resourceProperties.getWClientId()==null?"wjw":resourceProperties.getWClientId(), SecurityConstants.INNER);
        if(uucModelInfoR.getCode()!=HttpStatus.SUCCESS||uucModelInfoR.getData()==null){
            throw new ServiceException("模块参数异常");
        }
        if(StringUtils.isEmpty(uucModelInfoR.getData().getDeptCode())){
            throw new ServiceException("模块对应组织未配置");
        }
        return uucModelInfoR.getData().getDeptCode();
    }
    /**
     * 根据组织获取项目列表
     */
    public List<String> getProjectList(String deptCode){
        List<String> deptCodes=new ArrayList<>();
        deptCodes.add(deptCode);
        DeptResourceRequest deptResourceRequest = new DeptResourceRequest();
        deptResourceRequest.setDeptCode(deptCodes);
        List<ProjectDto> queryList = remoteCmdbService.deptProjects(deptResourceRequest, SecurityConstants.INNER).getData();
        List<String> projectCodes = queryList.stream().map(ProjectDto::getId).collect(Collectors.toList());
        return projectCodes;
    }
    /**
     * 校验项目是否非法，带返回参数
     */
    public List<String> verifyProjectList(String deptCode,String projectCode){
        List<String> projectList = getProjectList(deptCode);
        if(!StringUtils.isEmpty(projectCode)){//项目code不为空的话需要判断项目code是否在组织管理的项目列表中
            boolean containFlag = projectList.contains(projectCode);
            if(!containFlag){
                throw new ServiceException("非法项目参数",HttpStatus.BAD_REQUEST);
            }
            projectList = projectList.stream().filter(obj -> projectCode.equals(obj)).collect(Collectors.toList());
        }
        return projectList;
    }
    /**
     * 校验项目是否非法，无返回参数
     */
    public boolean checkProjectCode(String deptCode,String projectCode){
        List<String> projectList = getProjectList(deptCode);
        boolean checkFlag=true;
        return StringUtils.isEmpty(projectCode)?true:projectList.contains(projectCode);
    }
    /**
     * 根据请求参数校验
     */
    public RequestBodyVo checkByRequest(RequestBodyVo requestBodyVo){
        String deptCode=requestBodyVo.getDeptCode();
        String projectCode=requestBodyVo.getProjectCode();
        if(StringUtils.isEmpty(deptCode)){
            String code = getDeptCode();
            requestBodyVo.setDeptCode(code);
        }
        else{
            //判断如果传了deptCode，则需要校验是否在卫健委组织里
            List<String> deptCodes=getAllDept();
            if(!deptCodes.contains(deptCode)){
                throw new ServiceException("参数异常",HttpStatus.BAD_REQUEST);
            }
        }
        if(StringUtils.isNotEmpty(projectCode)){
            boolean flag = checkProjectCode(requestBodyVo.getDeptCode(), projectCode);
            if(!flag){
                throw new ServiceException("参数异常",HttpStatus.BAD_REQUEST);
            }
        }

        return requestBodyVo;

    }
    /**
     * 构建树形结构
     */
    private List<DeptTreeVo> getTree (List<UucDeptInfo> uucDeptInfos,List<DeptPo> deptList){
        List<DeptTreeVo> deptTreeVos=null;
        if(uucDeptInfos!=null&&deptList!=null){
            deptTreeVos=transDeptTreeVo(deptList);
            Map<String, String> deptParentCache = Maps.newHashMap();
            for (UucDeptInfo deptInfo: uucDeptInfos) {
                deptParentCache.put(String.valueOf(deptInfo.getId()), deptInfo.getParentCode());
                for(DeptTreeVo one:deptTreeVos){
                    if(one.getDeptId().equals(String.valueOf(deptInfo.getId()))){
                        one.setLevel(deptInfo.getLevel());
                        one.setRemark(deptInfo.getRemark());
                    }
                }
            }

            for (DeptTreeVo item: deptTreeVos) {
                item.setParentId(deptParentCache.get(item.getDeptId()));
            }
            return buildDeptTree(deptTreeVos);
        }
        return deptTreeVos;
    }

    private List<DeptTreeVo> transDeptTreeVo(List<DeptPo> deptList){
        List<DeptTreeVo> deptTreeVos=new ArrayList<>();
        if(deptList!=null&&deptList.size()>0){
            for(DeptPo item:deptList){
                DeptTreeVo deptTreeVo=new DeptTreeVo();
                deptTreeVo.setDeptId(item.getDeptId());
                deptTreeVo.setDeptCode(item.getDeptCode());
                deptTreeVo.setDeptName(item.getDeptName());
                deptTreeVos.add(deptTreeVo);
            }
        }
        return deptTreeVos;
    }



    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    public List<DeptTreeVo> buildDeptTree(List<DeptTreeVo> depts)
    {
        List<DeptTreeVo> returnList = new ArrayList<DeptTreeVo>();
        List<String> tempList = new ArrayList<String>();
        for (DeptTreeVo dept : depts)
        {
            tempList.add(dept.getDeptId());
        }
        for (DeptTreeVo dept : depts)
        {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId()))
            {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = depts;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<DeptTreeVo> list, DeptTreeVo t)
    {
        // 得到子节点列表
        List<DeptTreeVo> childList = getChildList(list, t);
        t.setChildren(childList);
        for (DeptTreeVo tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<DeptTreeVo> getChildList(List<DeptTreeVo> list, DeptTreeVo t)
    {
        List<DeptTreeVo> tlist = new ArrayList<DeptTreeVo>();
        Iterator<DeptTreeVo> it = list.iterator();
        while (it.hasNext())
        {
            DeptTreeVo n = (DeptTreeVo) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().equals(t.getDeptId()))
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<DeptTreeVo> list, DeptTreeVo t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }
}
