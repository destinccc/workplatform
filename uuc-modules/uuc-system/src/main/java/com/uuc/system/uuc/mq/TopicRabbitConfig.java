package com.uuc.system.uuc.mq;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;




@Configuration
public class TopicRabbitConfig {

    @Autowired
    private RabbitMqProperties rabbitMqProperties;


    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory)
    {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        //确认消息送到交换机(Exchange)回调
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback()
        {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause)
            {
                System.out.println("\n确认消息送到交换机(Exchange)结果：");
                System.out.println("相关数据：" + correlationData);
                System.out.println("是否成功：" + ack);
                System.out.println("错误原因：" + cause);
            }
        });

        //确认消息送到队列(Queue)回调
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback()
        {
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage)
            {
                System.out.println("\n确认消息送到队列(Queue)结果：");
                System.out.println("发生消息：" + returnedMessage.getMessage());
                System.out.println("回应码：" + returnedMessage.getReplyCode());
                System.out.println("回应信息：" + returnedMessage.getReplyText());
                System.out.println("交换机：" + returnedMessage.getExchange());
                System.out.println("路由键：" + returnedMessage.getRoutingKey());
            }
        });
        return rabbitTemplate;
    }

    @Bean
    public Queue cmdbQueue() {
        return new Queue(rabbitMqProperties.getQueueName(),true);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(rabbitMqProperties.getExchangeName());
    }

    @Bean
    Binding bindingExchangeMessage() {
        return BindingBuilder.bind(cmdbQueue()).to(exchange()).with(rabbitMqProperties.getRoutingKey());
    }



}
