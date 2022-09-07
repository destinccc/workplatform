//package com.uuc.system.uuc.mq;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import org.springframework.amqp.core.ExchangeTypes;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.*;
//import org.springframework.stereotype.Component;
//
//@Component
//public class TopicDuanxinConsumer {
//
//    @RabbitHandler
//    //注解的方式  配置 交换器  和队列
//    @RabbitListener(bindings =@QueueBinding(
//            value = @Queue(value = "topic.sync.test.queue",durable = "true", autoDelete = "false"),
//            exchange =@Exchange(value = "topic_ump_sync_exchange",type = ExchangeTypes.TOPIC),
//            key = "#.sync.#"
//    ))
//    public void topicreviceMessage(String message){
//        System.out.println("短信duanxin topic 接收到了的订单信息是："+message);
//    }
//}