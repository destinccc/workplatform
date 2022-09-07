package com.uuc.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.uuc.common.security.annotation.EnableCustomConfig;
import com.uuc.common.security.annotation.EnableRyFeignClients;
import com.uuc.common.swagger.annotation.EnableCustomSwagger2;

/**
 * 定时任务
 * 
 * @author uuc
 */
@EnableCustomConfig
@EnableCustomSwagger2   
@EnableRyFeignClients
@SpringBootApplication
public class UucJobApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(UucJobApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  定时任务模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
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
