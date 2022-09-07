package com.uuc.resource.apis.external.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.protobuf.ServiceException;
import com.google.common.net.HostAndPort;
import com.orbitz.consul.CatalogClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.model.ConsulResponse;
import com.orbitz.consul.model.catalog.CatalogService;
import com.orbitz.consul.model.health.ServiceHealth;
import com.uuc.resource.apis.external.config.ResourceConsulProperties;
import com.uuc.resource.apis.external.controller.DeptResourceController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author deng
 * @date 2022/8/10 0010
 * @description
 */
@Service
public class ConsulService {

    private static final Logger log = LoggerFactory.getLogger(ConsulService.class);
    @Autowired
    private ResourceConsulProperties resourceConsulProperties;


    public List<String> getServerList() throws ServiceException{
        try {
            Consul client = Consul.builder()
                    .withHostAndPort(HostAndPort.fromString(resourceConsulProperties.getUrl()))
                    .build();

            CatalogClient catalogClient = client.catalogClient();
            List<String> addressList=new ArrayList<>();
//            HealthClient healthClient = client.healthClient();

//            ConsulResponse<List<ServiceHealth>> allServiceInstances = healthClient.getAllServiceInstances(resourceConsulProperties.getServiceName());
//            List<ServiceHealth> serviceHealths = allServiceInstances.getResponse();

//        for(ServiceHealth item:serviceHealths){
//            String address = item.getService().getAddress();
//            addressList.add(address);
//            System.out.println("==========================获取到的地址为："+address);
//        }

//            ConsulResponse<Map<String, List<String>>> response = catalogClient.getServices();

            ConsulResponse<List<CatalogService>> service = catalogClient.getService(resourceConsulProperties.getServiceName());
            List<CatalogService> catalogServiceList=service.getResponse();

            for(CatalogService item:catalogServiceList){
                String address = item.getServiceAddress();
                addressList.add(address);
                System.out.println("==========================获取到的地址为："+address);
            }
            log.info("获取已经注册的地址列表:{}", JSON.toJSONString(addressList));
            return addressList;
        }catch (Exception e){
            log.error("获取已经注册的批量地址失败:{}",e.getMessage());
            throw new ServiceException(e.getMessage());
        }

    }

}
