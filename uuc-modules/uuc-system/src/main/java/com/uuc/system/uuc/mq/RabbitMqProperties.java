package com.uuc.system.uuc.mq;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author: fxm
 * @date: 2022-06-20
 * @description:
 **/
@Data
@Configuration
@ConfigurationProperties(
        prefix = "rabbitmq.config"
)
public class RabbitMqProperties {
    /**
     * 消费组
     */
    private String exchangeName;
    /**
     * 消费组名字
     */
    private String routingKey;

    /**
     * 绑定的queueName
     */
    private String queueName;


}
