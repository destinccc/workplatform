package com.uuc.system.uuc.mq;

import com.alibaba.fastjson.JSON;
import com.uuc.common.core.utils.uuid.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TopicUmpService {

    private static final Logger log = LoggerFactory.getLogger(TopicUmpService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMqProperties rabbitMqProperties;
 
    //模拟用户下单
    public void umpToMQ(String message){
        log.info("准备发送消息至rabbitMq,消息内容为："+message);
        //3.通过MQ来完成消息的分发
        //String exchangeName = "topic_ump_sync_exchange";
        //String exchangeName = "topic_ump_sync_exchange";
       // String routingKey = "topic.sync.queue";
        //String routingKey = "topic.sync.test.queue";
        //参数1：交换机 参数2：路由key/queue队列名称 参数3：消息内容
        //UucResourceDto uucResourceDto = JSONObject.parseObject(message, UucResourceDto.class);
        log.info("发送的exchangeName:{},routingKey:{}",rabbitMqProperties.getExchangeName(),rabbitMqProperties.getRoutingKey());
        try {
            // 添加消息id和时间戳
            MessagePostProcessor messagePostProcessor = (messageVo) -> {
                MessageProperties messageProperties = messageVo.getMessageProperties();
                messageProperties.setMessageId(UUID.randomUUID().toString());
                messageProperties.setTimestamp(new Date());
                return messageVo;
            };
            // 发送消息
            rabbitTemplate.convertAndSend(rabbitMqProperties.getExchangeName(), rabbitMqProperties.getRoutingKey(), message, messagePostProcessor);
            log.info("发送消息完成.........................................");
        } catch (AmqpException e) {
            log.error(String.format("send message failed, exchange=%s, routingKey=%s, msg=%s", rabbitMqProperties.getExchangeName(), rabbitMqProperties.getRoutingKey(), JSON.toJSONString(message)), e);
        }

    }
}
