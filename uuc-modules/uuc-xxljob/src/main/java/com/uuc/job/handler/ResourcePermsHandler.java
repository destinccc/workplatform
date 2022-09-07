//package com.uuc.job.handler;
//
//import com.uuc.job.service.cmdb.ResourcePermsService;
//import com.xxl.job.core.context.XxlJobHelper;
//import com.xxl.job.core.handler.annotation.XxlJob;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * @author: fxm
// * @date: 2022-06-22
// * @description: 资源数据权限缓存处理
// **/
//@Slf4j
//@Component
//public class ResourcePermsHandler {
//
//    @Autowired
//    private ResourcePermsService resourcePermsService;
//
//    @XxlJob("resourcePermsCache")
//    public void ResourcePermsSave2Cache() {
////        XxlJobHelper.log("=========> start to save resource perms to redis");
////        log.info("=========> start to save resource perms to redis");
////        long startTime = System.currentTimeMillis();
////        // 缓存热备
////        resourcePermsService.resourcePermsCacheBackup();
////        // 缓存用户资源权限
////        resourcePermsService.cacheUserPerms2Redis();
////        // 删除用户权限缓存
//////        resourcePermsService.deleteAllResourcePerms();
////        XxlJobHelper.log("=========> complete to save resource perms to redis, cos:{} ms!!!", System.currentTimeMillis()-startTime);
////        log.info("=========> 保存缓存成功");
////        XxlJobHelper.log("=========> complete to save resource perms to redis, cos:{} ms", System.currentTimeMillis()-startTime);
//    }
//
//
//}
