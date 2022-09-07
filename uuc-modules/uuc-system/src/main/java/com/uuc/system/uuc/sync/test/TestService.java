package com.uuc.system.uuc.sync.test;

import com.uuc.system.uuc.sync.service.ResourceSyncService;
import org.apache.tomcat.jni.Time;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class TestService {

    @Resource
    private Map<String, ResourceSyncService> resourceSyncServiceMap;

    public void testMap(String beanName){
        for(String item:resourceSyncServiceMap.keySet()){
            System.out.println("key: "+item+"......value: "+resourceSyncServiceMap.get(item).getClass().getName());

        }
        resourceSyncServiceMap.get(beanName).syncBody(null,null,null);
    }

    @Async
    public void testAsync(){
        System.out.println("进入异步消息.....................");
        try{
            TimeUnit.SECONDS.sleep(2L);
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("执行完异步消息....................");
    }
}
