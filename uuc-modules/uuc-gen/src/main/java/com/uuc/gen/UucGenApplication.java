package com.uuc.gen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.uuc.common.security.annotation.EnableCustomConfig;
import com.uuc.common.security.annotation.EnableRyFeignClients;
import com.uuc.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 代码生成
 * 
 * @author uuc
 */
@EnableDiscoveryClient
@EnableCustomConfig
@EnableCustomSwagger2   
@EnableRyFeignClients
@SpringBootApplication
public class UucGenApplication
{
    public static void main(String[] args)
    {
        System.setProperty("spring.main.allow-circular-references", "true");
        SpringApplication.run(UucGenApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  代码生成模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n" +
                " | (_,_).' __  ___(_ o _)'          \n" +
                " |  |\\ \\  |  ||   |(_,_)'         \n" +
                " |  | \\ `'   /|   `-'  /           \n" +
                " |  |  \\    /  \\      /           \n" +
                " ''-'   `'-'    `-..-'              ");
    }
}
