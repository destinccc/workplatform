package com.uuc.common.redis.configure;

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
        prefix = "redis.stream"
)
public class RedisStreamProperties {
    /**
     * 消费组
     */
    private String consumerGroup;
    /**
     * 消费组名字
     */
    private String consumerName;
    /**
     * stream名称
     */
    private String streamName;
    /**
     * field名称
     */
    private String fieldName;



}
