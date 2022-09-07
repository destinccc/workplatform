package com.uuc.alarm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.uuc.common.security.annotation.EnableCustomConfig;
import com.uuc.common.security.annotation.EnableRyFeignClients;
import com.uuc.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.scheduling.annotation.EnableAsync;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.TimeZone;

/**
 * 系统模块
 *
 * @author uuc
 */
@EnableDiscoveryClient
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication
@Slf4j
@EnableAsync
public class AlarmApplication {
    public static void main(String[] args) throws UnknownHostException {
        System.setProperty("spring.main.allow-circular-references", "true");
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        ConfigurableApplicationContext run = SpringApplication.run(AlarmApplication.class, args);
        ConfigurableEnvironment env = run.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        log.info("\n----------------------------------------------------------\n\t" +
                "Application [[ Alarm- System ]] is running! Access URLs:\n\t" +
                "Doc: \t\thttp://" + ip + ":" + port + (Objects.isNull(path) ? "" : path) + "/doc.html\n" +
                "----------------------------------------------------------");
    }
}
