package com.uuc.system.uuc.sync.test;

import com.uuc.common.core.annotation.CMDBField;
import com.uuc.common.core.enums.ModelOperationType;
import com.uuc.system.api.domain.SysUser;
import com.uuc.system.api.model.UucUserDept;
import com.uuc.system.api.model.UucUserInfo;
import com.uuc.system.uuc.sync.domain.SyncBody;
import com.uuc.system.uuc.sync.service.RedisStreamSyncService;
import com.uuc.system.uuc.sync.test.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/test")
public class TestController {

   @Autowired
   private TestService testService;
   @Autowired
   private RedisStreamSyncService redisStreamSyncService;

    @GetMapping("/testMap")
    public void testMap(){
        testService.testMap("projectSyncService");
        CMDBField annotation = SysUser.class.getAnnotation(CMDBField.class);
        Field[] declaredFields = SysUser.class.getDeclaredFields();
        for(Field item:declaredFields){
            item.setAccessible(true);
            CMDBField annotation1 = item.getAnnotation(CMDBField.class);
            if(annotation1!=null){
                System.out.println("属性名称为："+item.getName()+"对应的cmdb的属性名称为："+annotation1.name()+"++++++++是否是modelCode："+annotation1.isModelCode());
            }
        }
    }
    @GetMapping("/testAdd")
    public void testAdd(UucUserInfo uucUserInfo){
        UucUserInfo uucUserInfo1=null;
        if(uucUserInfo==null){
            uucUserInfo1=new UucUserInfo();
            uucUserInfo1.setId(111111L);
            uucUserInfo1.setUserName("张三");
            uucUserInfo1.setEmail("repotjrepo@qq.com");
            uucUserInfo1.setUserJobNumber("000001");
            uucUserInfo1.setWorkLocation("长沙");
            SyncBody syncBody=new SyncBody();
            syncBody.setOperationType(ModelOperationType.INSERT.getCode());
            syncBody.setNewObject(uucUserInfo1);
            syncBody.setBeanName("userSyncService");
            redisStreamSyncService.toRedisStreamMQ(syncBody);
        }else {
            SyncBody syncBody=new SyncBody();
            syncBody.setOperationType(ModelOperationType.INSERT.getCode());
            syncBody.setNewObject(uucUserInfo);
            syncBody.setBeanName("userSyncService");
            redisStreamSyncService.toRedisStreamMQ(syncBody);
        }



    }

    @GetMapping("/testUpdate")
    public void testUpdate(){
        UucUserInfo uucUserInfo=new UucUserInfo();
        uucUserInfo.setId(111111L);
        uucUserInfo.setUserName("张三");
        uucUserInfo.setEmail("repotjrepo@qq.com");
        uucUserInfo.setUserJobNumber("000001");
        uucUserInfo.setWorkLocation("长沙");
        SyncBody syncBody=new SyncBody();

        UucUserInfo uucUserInfo1=new UucUserInfo();
        uucUserInfo1.setId(111111L);
        uucUserInfo1.setUserName("张三1");
        uucUserInfo1.setEmail("repotjrepo111111@qq.com");
        uucUserInfo1.setUserJobNumber("000001");
        uucUserInfo1.setWorkLocation("长沙");
        List<UucUserDept> uucUserDeptList=new ArrayList<>();
        UucUserDept uucUserDept=new UucUserDept();
        uucUserDept.setDeptCode("000000");
        UucUserDept uucUserDept1=new UucUserDept();
        uucUserDept1.setDeptCode("000001");
        uucUserDeptList.add(uucUserDept);
        uucUserDeptList.add(uucUserDept1);
        uucUserInfo1.setDeptList(uucUserDeptList);
        syncBody.setOperationType(ModelOperationType.UPDATE.getCode());
        syncBody.setNewObject(uucUserInfo1);
        syncBody.setOldObject(uucUserInfo);
        syncBody.setBeanName("userSyncService");
        redisStreamSyncService.dealRedisStreamMQ(syncBody);



    }
    @GetMapping("/testDelete")
    public void testDelete(){
        SyncBody syncBody=new SyncBody();
        List<String> ids=new ArrayList<>();
        ids.add("0001");
        ids.add("002");
        syncBody.setOperationType(ModelOperationType.DELETE.getCode());
        syncBody.setBeanName("userSyncService");
        syncBody.setOldObject(ids);
        redisStreamSyncService.dealRedisStreamMQ(syncBody);

    }
    @GetMapping("/testAsync")
    public void testAsync(){
        System.out.println("main方法开始执行...................");
        testService.testAsync();
        System.out.println("main方法执行完成...................");
    }
}
