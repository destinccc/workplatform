package com.uuc.resource.apis.external.controller;

import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.log.annotation.Log;
import com.uuc.common.log.enums.BusinessType;
import com.uuc.resource.apis.external.constant.WjwApiConstant;
import com.uuc.resource.apis.external.service.ConsulService;
import com.uuc.resource.apis.external.service.DeptResourceService;
import com.uuc.resource.apis.external.service.PhysicalMachineService;
import com.uuc.resource.apis.external.service.VirtualMachineService;
import com.uuc.resource.apis.external.validator.RequestValid;
import com.uuc.system.api.model.cmdb.*;
import io.swagger.annotations.Api;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author deng
 * @date 2022/7/15 0015
 * @description 物理机查询相关接口
 */

@RestController
@RequestMapping("/v1")
@Api(tags = "物理设备服务")
public class PhysicalMachineController {

    private static final Logger log = LoggerFactory.getLogger(PhysicalMachineController.class);
    @Autowired
    private PhysicalMachineService physicalMachineService;
    @Autowired
    private VirtualMachineService virtualMachineService;
    @Autowired
    private DeptResourceService deptResourceService;
    @Autowired
    private ConsulService consulService;


    /**
     * 查询物理服务器列表信息
     * @param requestBodyVo
     * @return
     */
    @PostMapping("/hosts")
    @Log(title = WjwApiConstant.PhysicalServer, businessType = BusinessType.SELECT, desc = "查询物理服务器列表信息")
    public AjaxResult getHostsList(@RequestBody(required = false) RequestBodyVo requestBodyVo){
        try{
            RequestBodyVo requestBodyVoResult = deptResourceService.checkByRequest(requestBodyVo);
            List<PhysicalMachineInfo> physicalMachineServiceList = physicalMachineService.getList(requestBodyVoResult);
            if(CollectionUtils.isNotEmpty(physicalMachineServiceList)){
                List<String> serverList = consulService.getServerList();
                for(PhysicalMachineInfo item:physicalMachineServiceList){
                    if(serverList.contains(item.getIpAddress())){
                        item.setHasExpoter(true);
                    }
                }
            }
            return AjaxResult.success(physicalMachineServiceList);
        }catch (Exception e){
            e.printStackTrace();
            log.error("查询物理服务器列表信息失败:{}",e.getMessage());
            return AjaxResult.error("查询物理服务器列表信息失败");
        }
    }

    /**
     * 查询指定物理机监控信息
     * @param serverId
     * @return
     */
    @GetMapping("/host/monitor/{serverId}")
    @Log(title = WjwApiConstant.PhysicalServer, businessType = BusinessType.SELECT, desc = "查询指定物理机监控信息")
    public AjaxResult getHostMonitor(@PathVariable("serverId") String serverId){
        try {
            ServerInfo serverInfo = physicalMachineService.getServerInfo(serverId);
            return AjaxResult.success(serverInfo);
        }catch (Exception e){
            e.printStackTrace();
            log.error("查询指定物理机监控信息失败:{}",e.getMessage());
            return AjaxResult.error("查询指定物理机监控信息失败");
        }
    }

    /**
     * 查询指定物理机统计监控信息
     * @param requestBodyVo
     * @return
     */
    @PostMapping("/host/monitor/statistics")
    @Log(title = WjwApiConstant.PhysicalServer, businessType = BusinessType.SELECT, desc = "查询指定物理机统计监控信息")
    public AjaxResult getHostStatistics(@RequestBody RequestBodyVo requestBodyVo){
        try{
            ServerMonitor serverMonitor = virtualMachineService.getServerMonitor(requestBodyVo);
            return AjaxResult.success(serverMonitor);
        }catch (Exception e){
            e.printStackTrace();
            log.error("查询指定物理机统计监控信息失败:{}",e.getMessage());
            return AjaxResult.error("查询指定物理机统计监控信息失败");
        }
    }

    /**
     * 查询网络设备列表信息
     * @param requestBodyVo
     * @return
     */
    @PostMapping("/networkDevice")
    @Log(title = WjwApiConstant.NetworkDevice, businessType = BusinessType.SELECT, desc = "查询网络设备列表信息")
    public AjaxResult getNetworkDecvice(@RequestBody(required = false) RequestBodyVo requestBodyVo){
        try{
            RequestBodyVo requestBodyVoResult = deptResourceService.checkByRequest(requestBodyVo);
            List<NetworkDevice> netWorkDeviceList = physicalMachineService.getNetWorkDeviceList(requestBodyVoResult);
            return AjaxResult.success(netWorkDeviceList);
        }catch (Exception e){
            e.printStackTrace();
            log.error("查询网络设备列表信息失败:{}",e.getMessage());
            return AjaxResult.error("查询网络设备列表信息失败");
        }
    }
    /**
     * 查询安全设备列表信息
     * @param requestBodyVo
     * @return
     */
    @PostMapping("/securityDevice")
    @Log(title = WjwApiConstant.SecurityDevice, businessType = BusinessType.SELECT, desc = "查询安全设备列表信息")
    public AjaxResult getSecurityDecvice(@RequestBody(required = false) RequestBodyVo requestBodyVo){
        try{
            RequestBodyVo requestBodyVoResult = deptResourceService.checkByRequest(requestBodyVo);
            List<NetworkDevice> securityDeviceList = physicalMachineService.getSecurityDeviceList(requestBodyVoResult);
            return AjaxResult.success(securityDeviceList);
        }catch (Exception e){
            e.printStackTrace();
            log.error("查询安全设备列表信息失败:{}",e.getMessage());
            return AjaxResult.error("查询安全设备列表信息失败");
        }
    }


    /**
     * 查询物理机告警列表
     * @return
     */
    @GetMapping("/device/warnings")
    @Log(title = WjwApiConstant.PhysicalServer, businessType = BusinessType.SELECT, desc = "查询物理机告警列表")
    public AjaxResult getHostWarnings(){
        try {
            List<Warning> warnings = physicalMachineService.getWarnings();
            return AjaxResult.success(warnings==null?new ArrayList<>():warnings);
        }catch (Exception e){
            e.printStackTrace();
            log.error("查询物理机告警列表失败:{}",e.getMessage());
            return AjaxResult.error("查询物理机告警列表失败");
        }
    }

}
