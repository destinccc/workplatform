package com.uuc.resource.apis.external.service;

import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.resource.apis.external.controller.PhysicalMachineController;
import com.uuc.resource.apis.external.utils.BeanCopyUtil;
import com.uuc.system.api.RemoteCmdbService;
import com.uuc.system.api.RemoteMonitorMonService;
import com.uuc.system.api.RemoteMonitorTransService;
import com.uuc.system.api.model.cmdb.*;
import org.apache.commons.collections4.CollectionUtils;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author deng
 * @date 2022/7/19 0019
 * @description
 */
@Service
public class PhysicalMachineService {

    @Autowired
    private RemoteCmdbService remoteCmdbService;
    @Autowired
    private DeptResourceService deptResourceService;
    @Autowired
    private RemoteMonitorTransService remoteMonitorTransService;
    @Autowired
    private VirtualMachineService virtualMachineService;
    @Autowired
    private RemoteMonitorMonService remoteMonitorMonService;
    private static final Logger log = LoggerFactory.getLogger(PhysicalMachineService.class);

    /**
     * 获取物理机列表信息
     * @return
     */
    public List<PhysicalMachineInfo> getList(RequestBodyVo requestBodyVo){
        //requestBodyVo= deptResourceService.checkByRequest(requestBodyVo);
        log.info("requestBodyVo:{},开始执行远程cmdb查询物理机列表信息================================",requestBodyVo);
        List<PhysicalMachineInfo> physicalMachineInfos = remoteCmdbService.physicalList(requestBodyVo, SecurityConstants.INNER).getData();
        log.info("执行远程cmdb查询物理机信息列表完成,获取的数据为:{}",physicalMachineInfos);
        return physicalMachineInfos;
    }

    /**
     * 查询指定物理机的监控信息
     * @param serverId
     * @return
     */
    public ServerInfo getServerInfo(String serverId){
        log.info("请求的serverId:{},开始执行远程cmdb查询指定物理机监控信息================================",serverId);
        ServerInfo serverInfo = remoteCmdbService.hostMonitorInfo(serverId, SecurityConstants.INNER).getData();
        log.info("执行远程cmdb查询指定物理机监控信息完成,获取的数据为:{}",serverInfo);
        //请求监控告警查询其它属性
        MonitorResourceVo monitorResourceVo=new MonitorResourceVo();
        monitorResourceVo.setResourceId(serverInfo.getServerId());
        List<Nic> nics = serverInfo.getNics();
        List<Disk> disks=serverInfo.getDisks();
        List<String> macAddressList=new ArrayList<>();
        //List<String> mountPointList=new ArrayList<>();
        if(nics!=null&&nics.size()>0){
            for(Nic item:nics){
                if(StringUtils.isNotEmpty(item.getMacAddress())){
                    macAddressList.add(item.getMacAddress());
                }
            }
        }
//        if(CollectionUtils.isNotEmpty(disks)){
//            for(Disk item:disks){
//                if(StringUtils.isNotEmpty(item.getMountPoint())){
//                    mountPointList.add(item.getMountPoint());
//                }
//            }
//        }
        monitorResourceVo.setMacAddresses(macAddressList);
        virtualMachineService.mergeDiskInfoList(disks,serverInfo);
       // monitorResourceVo.setMountPoints(mountPointList);
        log.info("请求的serverId:{},开始执行远程monitor查询指定物理机监控信息================================",serverId);
        ServerInfo info = remoteMonitorTransService.getServerInfo(monitorResourceVo, SecurityConstants.INNER).getData();
        log.info("执行远程monitor查询指定物理机监控信息完成,获取的数据为:{}",info);
        virtualMachineService.mergeDiskMonitorList(info.getDisks(),serverInfo);
        //合并两个对象的属性,排除List属性
        BeanCopyUtil.mergeObject(info,serverInfo);
        ServerInfo serverInfoRes = virtualMachineService.transParam(serverInfo,info);
        serverInfoRes.setDisks(null);
        return serverInfoRes;
    }

    /**
     * 获取网络设备列表
     * @param requestBodyVo
     * @return
     */
    public List<NetworkDevice> getNetWorkDeviceList(RequestBodyVo requestBodyVo){
        log.info("requestBodyVo:{},开始执行远程cmdb获取网络设备列表信息================================",requestBodyVo);
        //requestBodyVo=deptResourceService.checkByRequest(requestBodyVo);
        List<NetworkDevice> networkDevices = remoteCmdbService.networkDeviceList(requestBodyVo, SecurityConstants.INNER).getData();
        log.info("执行远程cmdb获取网络设备列表完成,获取的数据为:{}",networkDevices);
        return networkDevices;
    }

    /**
     * 获取安全设备列表
     * @param requestBodyVo
     * @return
     */
    public List<NetworkDevice> getSecurityDeviceList(RequestBodyVo requestBodyVo){
        //requestBodyVo=deptResourceService.checkByRequest(requestBodyVo);
        log.info("requestBodyVo:{},开始执行远程cmdb获取安全设备列表信息================================",requestBodyVo);
        List<NetworkDevice> networkDevices = remoteCmdbService.securityDeviceList(requestBodyVo, SecurityConstants.INNER).getData();
        log.info("执行远程cmdb获取安全设备列表完成,获取的数据为:{}",networkDevices);
        return networkDevices;
    }

    /**
     * 获取物理机告警列表
     */
    public List<Warning> getWarnings(){
        //获取卫健委虚机列表
        String deptCode = deptResourceService.getDeptCode();
        RequestBodyVo requestBodyVo=new RequestBodyVo();
        requestBodyVo.setDeptCode(deptCode);
        List<PhysicalMachineInfo> list = getList(requestBodyVo);
        List<String> resourceIds = list.stream().map(PhysicalMachineInfo::getId).collect(Collectors.toList());
        String[] strings = resourceIds.toArray(new String[resourceIds.size()]);
        ServerMonitorVo serverMonitorVo=new ServerMonitorVo();
        serverMonitorVo.setResourceIds(strings);
        log.info("serverMonitorVo:{},开始执行远程monitor查询物理机告警列表信息================================",serverMonitorVo);
        List<Warning> warningList = remoteMonitorMonService.getWarnings(serverMonitorVo, SecurityConstants.INNER).getData();
        log.info("执行远程monitor查询物理机告警列表信息完成,获取的数据为:{}",warningList);
        return warningList;
    }
}
