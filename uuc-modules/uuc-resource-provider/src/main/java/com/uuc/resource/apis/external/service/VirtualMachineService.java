package com.uuc.resource.apis.external.service;

import com.google.common.net.HostAndPort;
import com.orbitz.consul.CatalogClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.model.ConsulResponse;
import com.orbitz.consul.model.catalog.CatalogService;
import com.uuc.common.core.constant.HttpStatus;
import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.exception.ServiceException;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.resource.apis.external.utils.BeanCopyUtil;
import com.uuc.system.api.RemoteCmdbService;
import com.uuc.system.api.RemoteMonitorMonService;
import com.uuc.system.api.RemoteMonitorTransService;
import com.uuc.system.api.model.cmdb.*;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author deng
 * @date 2022/7/19 0019
 * @description 虚拟机相关接口
 */
@Service
public class VirtualMachineService {

    private static final Logger log = LoggerFactory.getLogger(VirtualMachineService.class);
    @Autowired
    private RemoteCmdbService remoteCmdbService;
    @Autowired
    private DeptResourceService deptResourceService;
    @Autowired
    private RemoteMonitorTransService remoteMonitorTransService;
    @Autowired
    private RemoteMonitorMonService remoteMonitorMonService;

    /**
     * 查询虚拟机列表信息
     * @param requestBodyVo
     * @return
     */
    public List<VirtualMachineInfo> getInfoList(RequestBodyVo requestBodyVo){
       //requestBodyVo=deptResourceService.checkByRequest(requestBodyVo);
        log.info("requestBodyVo:{},开始执行远程cmdb查询虚拟机列表信息================================",requestBodyVo);
       List<VirtualMachineInfo> virtualMachineInfoList = remoteCmdbService.serverList(requestBodyVo, SecurityConstants.INNER).getData();
        log.info("执行远程cmdb查询指定虚拟机列表信息完成,获取的数据为:{}",virtualMachineInfoList);
       return virtualMachineInfoList;
    }

    /**
     * 查询指定资源信息
     * @param serverId
     * @return
     */
    public ServerInfo getServerInfo(String serverId){
        log.info("请求的serverId:{},开始执行远程cmdb查询指定虚拟机监控信息================================",serverId);
        ServerInfo info = remoteCmdbService.serverMonitorInfo(serverId, SecurityConstants.INNER).getData();
        log.info("执行远程cmdb查询指定虚拟机监控信息完成,获取的数据为:{}",info);
        MonitorResourceVo monitorResourceVo=new MonitorResourceVo();
        monitorResourceVo.setResourceId(info.getServerId());
        List<Nic> nics = info.getNics();
        List<Disk> disks=info.getDisks();
        List<String> macAddressList=new ArrayList<>();
        List<String> devices=new ArrayList<>();
        if(CollectionUtils.isNotEmpty(nics)){
            for(Nic item:nics){
                if(StringUtils.isNotEmpty(item.getMacAddress())){
                    macAddressList.add(item.getMacAddress());
                }
            }
        }

        mergeDiskInfoList(disks,info);
//        for(Disk item:disks){
//            if(StringUtils.isNotEmpty(item.getMountPoint())){
//                devices.add(item.getMountPoint());
//            }
//        }
        //填充磁盘基本信息

        monitorResourceVo.setMacAddresses(macAddressList);
        //monitorResourceVo.setDevices(devices);
        log.info("请求的serverId:{},开始执行远程monitor查询指定虚拟机监控信息================================",serverId);
        ServerInfo serverInfo = remoteMonitorTransService.getServerInfo(monitorResourceVo, SecurityConstants.INNER).getData();
        log.info("执行远程monitor查询指定虚拟机监控信息完成,获取的数据为:{}",serverInfo);
        mergeDiskMonitorList(serverInfo.getDisks(),info);
        //合并两个对象的属性,排除List属性
        BeanCopyUtil.mergeObject(serverInfo,info);
        ServerInfo serverInfoRes = transParam(info, serverInfo);
        serverInfoRes.setDisks(null);
        return serverInfoRes;
    }

    /**
     * 查询指定资源统计监控信息
     * @param requestBodyVo
     * @return
     */
    public ServerMonitor getServerMonitor(RequestBodyVo requestBodyVo){
        if(StringUtils.isEmpty(requestBodyVo.getServerId())||requestBodyVo.getStartTime()==null){
            throw new ServiceException("参数异常", HttpStatus.BAD_REQUEST);
        }
        ServerMonitorVo serverMonitorVo=new ServerMonitorVo();
        serverMonitorVo.setResourceId(requestBodyVo.getServerId());
        serverMonitorVo.setStartTime(requestBodyVo.getStartTime());
        serverMonitorVo.setEndTime(requestBodyVo.getEndTime());
        log.info("requestBodyVo:{},开始执行远程monitor查询指定虚拟机监控统计信息================================",requestBodyVo);
        ServerMonitor serverMonitor = remoteMonitorTransService.monitorStatistics(serverMonitorVo, SecurityConstants.INNER).getData();
        log.info("执行远程monitor查询指定虚拟机监控统计信息完成,获取的数据为:{}",serverMonitor);
        serverMonitor.setServerId(requestBodyVo.getServerId());
        return serverMonitor;
    }

    /**
     * 查询虚机告警列表
     * @return
     */
    public List<Warning> getWarning(){
        //获取卫健委虚机列表
        String deptCode = deptResourceService.getDeptCode();
        RequestBodyVo requestBodyVo=new RequestBodyVo();
        requestBodyVo.setDeptCode(deptCode);
        List<VirtualMachineInfo> list = getInfoList(requestBodyVo);
        List<String> resourceIds = list.stream().map(VirtualMachineInfo::getId).collect(Collectors.toList());
        String[] strings = resourceIds.toArray(new String[resourceIds.size()]);
        ServerMonitorVo serverMonitorVo=new ServerMonitorVo();
        serverMonitorVo.setResourceIds(strings);
        log.info("serverMonitorVo:{},开始执行远程monitor查询虚拟机告警列表信息================================",serverMonitorVo);
        List<Warning> warningList = remoteMonitorMonService.getWarnings(serverMonitorVo, SecurityConstants.INNER).getData();
        log.info("执行远程monitor查询虚拟机告警列表信息完成,获取的数据为:{}",warningList);
        return warningList;
    }
    /**
     * 合并两个对象中的List,目前监控磁盘取不到数据，以cmdb的数据为准，网卡通过mac地址匹配,磁盘通过挂载点匹配
     * @param info
     * @param serverInfo
     * @return
     */
    public ServerInfo transParam(ServerInfo info,ServerInfo serverInfo){
        List<Nic> nics = info.getNics();
        List<Nic> nicList = serverInfo.getNics();
        if(nics!=null&&nics.size()>0&&nicList!=null&&nicList.size()>0){
            for(Nic item:nics){
                for(Nic one:nicList){
                    if(one.getMacAddress().equals(item.getMacAddress())){
                        item.setInboundRate(one.getInboundRate());
                        item.setOutboundRate(one.getOutboundRate());
                        item.setStatus(one.getStatus());
                    }
                }
            }
        }
//        if(CollectionUtils.isNotEmpty(cmdbDisks)&&CollectionUtils.isNotEmpty(monitorDisks)){
//            for(Disk item:cmdbDisks){
//                for(Disk one:monitorDisks){
//                    if(one.getDevice().equals(item.getMountPoint())){
//                        item.setUsage(one.getUsage());
//                        item.setUsageRate(one.getUsageRate());
//                        item.setReadIops(one.getReadIops());
//                        item.setWriteIops(one.getWriteIops());
//                        item.setReadThroughput(one.getReadThroughput());
//                        item.setWriteThroughput(one.getWriteThroughput());
//                        item.setReadRate(one.getReadRate());
//                        item.setWriteRate(one.getWriteRate());
//                    }
//                }
//            }
//        }
        return info;
    }

    //合并两个集合中相同属性值
    public ServerInfo mergeDiskInfoList(List<Disk> list,ServerInfo serverInfo){
        List<DiskInfo> diskInfos=new ArrayList<>();
        if(CollectionUtils.isNotEmpty(list)){
            for(Disk item:list){
                DiskInfo diskInfo=new DiskInfo();
                BeanCopyUtil.copyProperties(item,diskInfo,true);
                diskInfos.add(diskInfo);
            }
        }
        serverInfo.setDiskInfos(diskInfos);
        return serverInfo;
    }
    //合并两个集合中相同属性值
    public ServerInfo mergeDiskMonitorList(List<Disk> list,ServerInfo serverInfo){
        List<DiskMonitor> diskMonitors=new ArrayList<>();
        if(CollectionUtils.isNotEmpty(list)){
            for(Disk item:list){
                DiskMonitor diskMonitor=new DiskMonitor();
                BeanCopyUtil.copyProperties(item,diskMonitor,true);
                diskMonitors.add(diskMonitor);
            }
        }
        serverInfo.setDiskMonitors(diskMonitors);
        return serverInfo;
    }
}
