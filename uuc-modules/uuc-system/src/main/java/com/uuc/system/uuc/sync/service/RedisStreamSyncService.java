package com.uuc.system.uuc.sync.service;

import cn.hutool.json.JSONUtil;
import com.uuc.common.core.enums.ModelOperationType;
import com.uuc.common.core.exception.SyncException;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.redis.configure.RedisStreamProperties;
import com.uuc.common.redis.service.RedisService;
import com.uuc.system.uuc.mq.TopicUmpService;
import com.uuc.system.uuc.sync.domain.SyncBody;
import com.uuc.system.uuc.sync.domain.UucResourceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.Record;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class RedisStreamSyncService {

       private static final Logger log = LoggerFactory.getLogger(RedisStreamSyncService.class);
       @Resource
       private Map<String,ResourceSyncService> resourceSyncServiceMap;

       @Autowired
       private RedisService redisService;

       @Autowired
       private TopicUmpService topicUmpService;

       @Autowired(required = false)
       private RedisStreamProperties streamProperties;


       /**
        * 调用公共的方法将数据传递给redisStream中的消息队列
        * @param syncBody
        */
       public void toRedisStreamMQ(SyncBody syncBody){
           try{
               verifyParameter(syncBody);
               //调用公共方法将消息发送给redisStream中
               String s = JSONUtil.toJsonStr(syncBody);
               System.out.println(s);
               //sendSreamSmg("test",s);
               add(streamProperties.getStreamName(),streamProperties.getFieldName(),s);
           }catch (Exception e){
               log.error("执行toRedisStreamMQ方法异常，错误信息："+e.getMessage());
               throw new SyncException(e.getMessage());
           }
       }
       /**
        * 调用公共的方法处理redisStream中的消息
        */
       public void dealRedisStreamMQ(SyncBody syncBody){
           try {
               verifyParameter(syncBody);
               UucResourceDto uucResourceDto = resourceSyncServiceMap.get(syncBody.getBeanName()).syncBody(syncBody.getOldObject(), syncBody.getNewObject(), syncBody.getOperationType());
               //将对象转换为json字符串
               String msg = JSONUtil.toJsonStr(uucResourceDto);
               //往rabbitMQ发送消息
               topicUmpService.umpToMQ(msg);
           }catch (Exception e){
               log.error("执行dealRedisStreamMQ方法异常，错误信息："+e.getMessage());
               throw new SyncException(e.getMessage());
           }
       }

       /**
        * 校验参数
        *
        */
       public void verifyParameter(SyncBody syncBody){
           if(syncBody==null){
              throw new SyncException("参数实体不能为空");
           }
           Set<String> syncKeyMap = resourceSyncServiceMap.keySet();
           if(StringUtils.isEmpty(syncBody.getBeanName())||!syncKeyMap.contains(syncBody.getBeanName())){
              throw new SyncException("处理的beanName值不正确");
           }
           if(StringUtils.isEmpty(syncBody.getOperationType())||!ModelOperationType.codeSet.contains(syncBody.getOperationType())){
               throw new SyncException("操作类型值不正确");
           }
           if(syncBody.getOperationType().equals(ModelOperationType.INSERT.getCode())&&syncBody.getNewObject()==null){//新增则newObject不能为空
               throw new SyncException("新增操作缺少参数");
           }
           if(syncBody.getOperationType().equals(ModelOperationType.UPDATE.getCode())){//编辑则newObject和oldObject都不能为空
               if(syncBody.getNewObject()==null||syncBody.getNewObject()==null){
                   throw new SyncException("编辑操作缺少参数");
               }
           }
           if(syncBody.getOperationType().equals(ModelOperationType.DELETE.getCode())&&syncBody.getOldObject()==null){//删除则oldObject不能为空
               throw new SyncException("删除操作缺少参数");
           }
       }

    /**
     * 统一处理成syncBody格式
     * @param beanName
     * @param receiveString
     * @return
     */
      public SyncBody toSyncBody(String beanName,String receiveString){
       return resourceSyncServiceMap.get(beanName).toSyncBody(receiveString);
       }

    /**
     * 追加消息
     * @param key
     * @param field
     * @param value
     * @return
     */
    public String add(String key, String field, Object value){
        Map<String, Object> content = new HashMap<>(1);
        content.put(field, value);
        return add(key, content);
    }
    public String add(String key, Map<String, Object> content){
        return redisService.redisTemplate.opsForStream().add(key, content).getValue();
    }

}
