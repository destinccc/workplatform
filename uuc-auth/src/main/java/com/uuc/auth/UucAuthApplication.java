package com.uuc.auth;

import com.uuc.common.swagger.annotation.EnableCustomSwagger2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import com.uuc.common.security.annotation.EnableRyFeignClients;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.TimeZone;

/**
 * 认证授权中心
 *
 * @author uuc
 */
@EnableDiscoveryClient
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@Slf4j
public class UucAuthApplication
{
    public static void main(String[] args) throws UnknownHostException {
        System.setProperty("spring.main.allow-circular-references", "true");
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        ConfigurableApplicationContext run = SpringApplication.run(UucAuthApplication.class, args);
        ConfigurableEnvironment env = run.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        log.info("\n----------------------------------------------------------\n\t" +
                "Application [[ UUC- Auth ]] is running! Access URLs:\n\t" +
                "Doc: \t\thttp://" + ip + ":" + port + (Objects.isNull(path) ? "" : path) + "/doc.html\n" +
                "----------------------------------------------------------");
    }
}
