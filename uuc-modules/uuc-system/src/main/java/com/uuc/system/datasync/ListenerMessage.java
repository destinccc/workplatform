package com.uuc.system.datasync;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuc.common.redis.configure.RedisStreamProperties;
import com.uuc.common.redis.service.RedisService;
import com.uuc.system.api.model.UucUserInfo;
import com.uuc.system.uuc.sync.domain.SyncBody;
import com.uuc.system.uuc.sync.service.RedisStreamSyncService;
import com.uuc.system.uuc.sync.service.ResourceSyncService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.stream.StreamListener;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fxm
 * @Description 监听redis stream消息
 * @Date 2021/3/10.
 */
@Slf4j
@ConditionalOnProperty(prefix = "redis.stream", name = "enabled", havingValue = "true")
@Configuration
public class ListenerMessage implements StreamListener<String, MapRecord<String, String, String>> {

    @Autowired
    private RedisService redisService;

    @Autowired(required = false)
    private RedisStreamProperties streamProperties;

    @Autowired
    private RedisStreamSyncService streamSyncService;

    @Override
    public void onMessage(MapRecord<String,String, String> entries) {
        log.info("receive msg from redis stream, stream name: {}, message id: {}, body: {}", entries.getStream(), entries.getId(), entries.getValue());
        // todo 1、对数据进行对比 2、发送mq通知消息 3、消费完成业务判断是否删除消息
        log.info("接受到来自redis的消息");
        System.out.println("message id "+entries.getId());
        System.out.println("stream "+entries.getStream());
        System.out.println("body "+entries.getValue());
        try {
            String receiveResponse=entries.getValue().get(streamProperties.getFieldName());
            receiveResponse = receiveResponse.replaceAll("\\\\","");
            receiveResponse = receiveResponse.substring(1, receiveResponse.length()-1);
            System.out.println(receiveResponse);
            JSONObject jsonObject = JSONObject.parseObject(receiveResponse);
            String beanName = String.valueOf(jsonObject.get("beanName"));
            SyncBody syncBody =streamSyncService.toSyncBody(beanName,receiveResponse) ;
            streamSyncService.dealRedisStreamMQ(syncBody);
            removeMsgFromStream(entries.getStream(),entries.getId());
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }


    }
    
    /**
     * 删除stream指定消息
     * @param stream
     * @param dataId
     * @return
     */
    private Long removeMsgFromStream(String stream, RecordId dataId) {
        return redisService.redisTemplate.opsForStream().delete(stream, dataId);
    }

    public String add(String key, Map<String, Object> content){
        return redisService.redisTemplate.opsForStream().add(key, content).getValue();
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

}
