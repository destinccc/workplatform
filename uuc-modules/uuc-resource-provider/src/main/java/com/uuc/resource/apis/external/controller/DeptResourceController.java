package com.uuc.resource.apis.external.controller;

import com.uuc.common.core.constant.HttpStatus;
import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.constant.TokenConstants;
import com.uuc.common.core.domain.R;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.log.annotation.Log;
import com.uuc.common.log.enums.BusinessType;
import com.uuc.resource.apis.external.config.ResourceProperties;
import com.uuc.resource.apis.external.service.ConsulService;
import com.uuc.resource.apis.external.validator.RequestValid;
import com.uuc.resource.apis.external.constant.WjwApiConstant;
import com.uuc.system.api.RemoteCmdbService;
import com.uuc.system.api.model.cmdb.ProjectVo;
import com.uuc.system.api.model.cmdb.RequestBodyVo;
import com.uuc.resource.apis.external.service.DeptResourceService;
import com.uuc.resource.apis.inner.vo.DeptTreeVo;
import com.uuc.system.api.RemoteClientService;
import com.uuc.system.api.domain.UucModelInfo;
import com.uuc.system.api.model.cmdb.ResourceInfo;
import com.uuc.system.api.model.cmdb.VirtualMachineInfo;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author deng
 * @date 2022/7/15 0015
 * @description 用于对外提供卫健委组织及其相关项目
 */
@RestController
@RequestMapping("/v1")
@Api(tags = WjwApiConstant.Resource)
public class DeptResourceController {

    private static final Logger log = LoggerFactory.getLogger(DeptResourceController.class);

    @Autowired
    private RemoteCmdbService remoteCmdbService;
    @Autowired
    private DeptResourceService deptResourceService;
    @Autowired
    private ResourceProperties resourceProperties;
    @Autowired
    private ConsulService consulService;

    /**
     * 查询卫健委的所有组织以树形结构返回
     * @return
     */
    @GetMapping("/depts")
    @Log(title = WjwApiConstant.Resource, businessType = BusinessType.SELECT, desc = "获取组织列表")
    public AjaxResult getDepts(){
        try {
            UucModelInfo uucModelInfo = deptResourceService.getClient(resourceProperties.getWClientId()==null?"wjw":resourceProperties.getWClientId());
            List<DeptTreeVo> deptTree = deptResourceService.getDeptTree(String.valueOf(uucModelInfo.getDeptCode()),false);
            return AjaxResult.success(deptTree);
        }
        catch (Exception e){
            e.printStackTrace();
            log.error("查询卫健委组织失败,错误:{}",e.getMessage());
            return AjaxResult.error("查询卫健委组织失败",HttpStatus.ERROR);
        }
    }

    /**
     * 查询项目列表信息
     * @param deptCode
     * @return
     */
    @GetMapping("/projects")
    @Log(title = WjwApiConstant.Resource, businessType = BusinessType.SELECT, desc = "获取项目列表")
    public AjaxResult getDepts(@RequestParam(value = "deptCode",required = false)String deptCode){
        try {
            if(StringUtils.isEmpty(deptCode)){
                deptCode=deptResourceService.getDeptCode();
            }else {
                boolean checkFlag = deptResourceService.checkDept(deptCode);
                if(!checkFlag){
                    return AjaxResult.error(HttpStatus.BAD_REQUEST,"非法组织参数");
                }
            }
            RequestBodyVo requestBodyVo=new RequestBodyVo();
            requestBodyVo.setDeptCode(deptCode);
            R<List<ProjectVo>> listR = remoteCmdbService.projectList(requestBodyVo, SecurityConstants.INNER);
            return AjaxResult.success(listR.getData());
        }catch (Exception e){
            e.printStackTrace();
            log.error("根据卫健委组织deptCode查询下面的资源失败,错误:{}",e.getMessage());
            return AjaxResult.error("根据卫健委组织deptCode查询下面的资源失败",HttpStatus.ERROR);
        }
    }

    /**
     * 查询云平台资源状态信息
     * @param requestBodyVo
     * @return
     */
    @PostMapping("/resources")
    @Log(title = WjwApiConstant.Resource, businessType = BusinessType.SELECT, desc = "获取云平台资源")
    public AjaxResult getResourceByDeptAndproject(@RequestBody(required = false) RequestBodyVo requestBodyVo){
        try{
            RequestBodyVo requestBodyVoResult = deptResourceService.checkByRequest(requestBodyVo);
            ResourceInfo resource = deptResourceService.getResource(requestBodyVoResult);
            return AjaxResult.success(resource==null?new ResourceInfo():resource);
        }catch (Exception  e){
            e.printStackTrace();
            log.error("查询云平台资源状态信息失败:{}",e.getMessage());
            return AjaxResult.error("查询云平台资源状态信息");
        }
    }

    /**
     * 查询所有注册的服务列表
     */
    @GetMapping("/getAllInstances")
    public AjaxResult getAllRegisterHost(){
        try {
            return AjaxResult.success(consulService.getServerList());
        }catch (Exception e){
            e.printStackTrace();
            log.error("获取所有注册服务器列表失败:{}",e.getMessage());
            return AjaxResult.error("获取所有注册服务器列表失败");
        }
    }


}
