package com.uuc.system.uuc.sync.service;

import com.uuc.common.core.enums.ModelOperationType;
import com.uuc.system.api.model.UucUserInfo;
import com.uuc.system.uuc.sync.domain.SyncBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ToSyncBodyService {

    private static final Logger log = LoggerFactory.getLogger(ToSyncBodyService.class);

    @Autowired
    private RedisStreamSyncService syncToRedisStreamService;

    @Async
    public void insertSyncBody(Object newObj,String beanName){
        log.info("组装插入的数据结构，处理的beanName为{}",beanName);
        SyncBody syncBody=new SyncBody();
        syncBody.setBeanName(beanName);
        syncBody.setOperationType(ModelOperationType.INSERT.getCode());
        syncBody.setNewObject(newObj);
        syncToRedisStreamService.toRedisStreamMQ(syncBody);
        log.info("发送插入数据结构到redis队列完成...................................");
    }
    @Async
    public void updateSyncBody(Object oldObj,Object newObj,String beanName){
        log.info("组装编辑的数据结构，处理的beanName为{}",beanName);
        SyncBody syncBody=new SyncBody();
        syncBody.setBeanName(beanName);
        syncBody.setOperationType(ModelOperationType.UPDATE.getCode());
        syncBody.setNewObject(newObj);
        syncBody.setOldObject(oldObj);
        syncToRedisStreamService.toRedisStreamMQ(syncBody);
        log.info("发送编辑数据结构到redis队列完成...................................");
    }
    @Async
    public void deleteSyncBody(Object oldObj,String beanName){
        log.info("组装删除的数据结构，处理的beanName为{}",beanName);
        SyncBody syncBody=new SyncBody();
        syncBody.setBeanName(beanName);
        syncBody.setOperationType(ModelOperationType.DELETE.getCode());
        syncBody.setOldObject(oldObj);
        syncToRedisStreamService.toRedisStreamMQ(syncBody);
        log.info("发送删除数据结构到redis队列完成...................................");
    }

}
