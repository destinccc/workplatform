package com.uuc;

import com.uuc.common.core.utils.SpringUtils;
import com.uuc.common.security.annotation.EnableCustomConfig;
import com.uuc.common.security.annotation.EnableRyFeignClients;
import com.uuc.common.swagger.annotation.EnableCustomSwagger2;
import com.uuc.job.util.AsynCallUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.TimeZone;


@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication
@Slf4j
public class UccXxljobApplication {

    public static void main(String[] args) throws UnknownHostException {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        ConfigurableApplicationContext run = SpringApplication.run(UccXxljobApplication.class, args);
        ConfigurableEnvironment env = run.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        log.info("\n----------------------------------------------------------\n\t" +
                "Application [[ UUC- XXLjob ]] is running! Access URLs:\n\t" +
                "Doc: \t\thttp://" + ip + ":" + port + (Objects.isNull(path) ? "" : path) + "/doc.html\n" +
                "----------------------------------------------------------");
//        try{
//            ResourcePermsHandler bean = SpringUtils.getBean(ResourcePermsHandler.class);
//            AsynCallUtil.run(() ->{
//                bean.ResourcePermsSave2Cache();
//
//            });
//        }catch (Exception e){
//            log.error("启动更新缓存失败");
//            log.error(e.getMessage());
//        }

    }

}
