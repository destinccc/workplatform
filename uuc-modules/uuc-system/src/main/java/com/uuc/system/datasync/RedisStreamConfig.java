package com.uuc.system.datasync;

import com.uuc.common.redis.configure.RedisStreamProperties;
import com.uuc.common.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

import java.time.Duration;

/**
 * @author Huhailong
 * @Description
 * @Date 2021/3/12.
 */
@Configuration
@ConditionalOnProperty(prefix = "redis.stream", name = "enabled", havingValue = "true")
@Slf4j
public class RedisStreamConfig {

    @Autowired
    private RedisStreamProperties streamProperties;

    @Autowired
    private StreamListener streamListener;

    @Autowired
    private RedisService redisService;

    @Bean
    public Subscription subscription(RedisConnectionFactory factory) {
        initStream(streamProperties.getStreamName(), streamProperties.getConsumerGroup());
        var options = StreamMessageListenerContainer
                .StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(Duration.ofSeconds(1))
                .build();
        var listenerContainer = StreamMessageListenerContainer.create(factory, options);
        var subscription = listenerContainer.receiveAutoAck(
                Consumer.from(streamProperties.getConsumerGroup(), streamProperties.getConsumerName()),
                StreamOffset.create(streamProperties.getStreamName(), ReadOffset.lastConsumed()), streamListener);
        listenerContainer.start();
        return subscription;
    }

    /**
     * 初始化创建stream
     * @param streamName
     * @param group
     */
    private void initStream(String streamName, String group) {
        //判断key是否存在，如果不存在则创建
        boolean hasKey = redisService.hasKey(streamName);
        if (!hasKey) {
            StreamOperations streamOperations = redisService.redisTemplate.opsForStream();
            streamOperations.createGroup(streamName, group);
            log.info(" redis stream:{} -group:{} initialize success", streamName, group);
        }
    }

}
