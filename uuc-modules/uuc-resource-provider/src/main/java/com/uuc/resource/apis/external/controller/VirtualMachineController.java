package com.uuc.resource.apis.external.controller;

import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.log.annotation.Log;
import com.uuc.common.log.enums.BusinessType;
import com.uuc.resource.apis.external.constant.WjwApiConstant;
import com.uuc.resource.apis.external.service.ConsulService;
import com.uuc.resource.apis.external.service.DeptResourceService;
import com.uuc.resource.apis.external.service.VirtualMachineService;
import com.uuc.resource.apis.external.validator.RequestValid;
import com.uuc.system.api.model.cmdb.*;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author deng
 * @date 2022/7/15 0015
 * @description 虚机相关接口
 */
@RestController
@RequestMapping("/v1")
@Api(tags = "虚拟主机服务")
public class VirtualMachineController {

    private static final Logger log = LoggerFactory.getLogger(VirtualMachineController.class);
    @Autowired
    private VirtualMachineService virtualMachineService;
    @Autowired
    private DeptResourceService deptResourceService;
    @Autowired
    private ConsulService consulService;
    /**
     * 获取云平台的虚机详情列表
     * @param requestBodyVo
     * @return
     */

    @PostMapping("/servers")
    @Log(title = WjwApiConstant.VirtualMachine, businessType = BusinessType.SELECT, desc = "获取云平台的虚机详情列表")
    public AjaxResult getServerList(@RequestBody(required = false) RequestBodyVo requestBodyVo){
        try{
            RequestBodyVo requestBodyVoResult = deptResourceService.checkByRequest(requestBodyVo);
            List<VirtualMachineInfo> infoList = virtualMachineService.getInfoList(requestBodyVoResult);
            List<String> serverList = consulService.getServerList();
            for(VirtualMachineInfo item:infoList){
                if(serverList.contains(item.getEip())){
                    item.setHasExpoter(true);
                }
            }
            return AjaxResult.success(infoList);
        }catch (Exception e){
            e.printStackTrace();
            log.error("获取云平台的虚机详情列表失败:{}",e.getMessage());
            return AjaxResult.error("获取云平台的虚机详情列表失败");
        }
    }

    /**
     * 查询指定虚机监控信息
     * @param serverId
     * @return
     */
    @GetMapping("/server/monitor/{serverId}")
    @Log(title = WjwApiConstant.VirtualMachine, businessType = BusinessType.SELECT, desc = "查询指定虚机监控信息")
    public AjaxResult getServerMonitor(@PathVariable("serverId") String serverId){
        try{
            ServerInfo serverInfo = virtualMachineService.getServerInfo(serverId);
            return AjaxResult.success(serverInfo);
        }catch (Exception e){
            e.printStackTrace();
            log.error("获取云平台的虚机详情列表失败:{}",e.getMessage());
            return AjaxResult.error("获取云平台的虚机详情列表失败");
        }

    }

    /**
     * 查询指定虚机统计监控信息
     * @param requestBodyVo
     * @return
     */
    @PostMapping("/server/monitor/statistics")
    @Log(title = WjwApiConstant.VirtualMachine, businessType = BusinessType.SELECT, desc = "查询指定虚机统计监控信息")
    public AjaxResult getServerStatistics(@RequestBody(required = false) RequestBodyVo requestBodyVo){
        try{
            ServerMonitor serverMonitor = virtualMachineService.getServerMonitor(requestBodyVo);
            return AjaxResult.success(serverMonitor==null?new ServerMonitor():serverMonitor);
        }catch (Exception e){
            e.printStackTrace();
            log.error("获取云平台的虚机详情列表失败:{}",e.getMessage());
            return AjaxResult.error("获取云平台的虚机详情列表失败");
        }
    }

    /**
     * 查询虚拟机告警列表
     * @return
     */
    @GetMapping("/server/warnings")
    @Log(title = WjwApiConstant.VirtualMachine, businessType = BusinessType.SELECT, desc = "查询虚拟机告警列表")
    public AjaxResult getServerWarnings(){
        try{
            List<Warning> warnings = virtualMachineService.getWarning();
            return AjaxResult.success(warnings==null?new ArrayList<>():warnings);
        }catch (Exception e){
            e.printStackTrace();
            log.error("查询虚拟机告警列表失败:{}",e.getMessage());
            return AjaxResult.error("查询虚拟机告警列表失败");
        }
    }
}
