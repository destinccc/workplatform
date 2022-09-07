//package com.uuc.job.service.cmdb;
//
//import com.alibaba.fastjson.JSON;
//import com.google.common.collect.Maps;
//import com.uuc.common.core.constant.CacheConstants;
//import com.uuc.common.core.constant.SecurityConstants;
//import com.uuc.common.core.domain.R;
//import com.uuc.common.core.web.domain.AjaxResult;
//import com.uuc.common.redis.service.RedisService;
//import com.uuc.job.constants.CmdbModelCodeConstants;
//import com.uuc.system.api.RemoteCmdbService;
//import com.uuc.system.api.RemoteSystemService;
//import com.uuc.system.api.model.UucDeptInfo;
//import com.uuc.system.api.model.UucUserDept;
//import com.uuc.system.api.model.cmdb.*;
//import org.apache.commons.collections4.CollectionUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
///**
// * @author: fxm
// * @date: 2022-06-22
// * @description:
// **/
//@Service
//public class ResourcePermsService {
//
//    @Autowired
//    private RemoteSystemService systemService;
//
//    @Autowired
//    private RemoteCmdbService cmdbService;
//
//    @Autowired
//    private RedisService redisService;
//
//    /**
//     * 构造线程池
//     */
//    private ExecutorService threadPool = new ThreadPoolExecutor(100, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
//
//    /**
//     * 二级缓存
//     *  通过二级缓存方式，提供无缝缓存服务
//     */
//    public void resourcePermsCacheBackup(){
////        Collection<String> keys = redisService.keys(CacheConstants.CACHE_NAMESPACE_RESOURCE_PERMS+"*");
////        if (CollectionUtils.isNotEmpty(keys)) {
////            //todo redis缓存删除之前，缓存到服务器并提供API服务
////            redisService.deleteObject(keys);
////        }
//    }
//
//    /**
//     * 删除所有数据权限缓存
//     *  当前数据量不大，通过keys遍历删除
//     */
//    public void deleteAllResourcePerms() {
//        Collection<String> keys = redisService.keys(CacheConstants.CACHE_NAMESPACE_RESOURCE_PERMS+"*");
//        if (CollectionUtils.isNotEmpty(keys)) {
//            redisService.deleteObject(keys);
//        }
//    }
//
//    /**
//     * 人员对应资源信息缓存
//     */
//    public void cacheUserPerms2Redis() {
//        // 获取所有管理员的id集合
//        Set<String> adminRoleSets = new HashSet<String>();
//        List<String> adminIds = (List<String>) systemService.getAdminUserList(SecurityConstants.INNER).getData();
//        adminRoleSets.addAll(adminIds);
//
//        // neo4j节点数据没有保存父级组织？这里先从门户查询
//        List<UucDeptInfo> allDeptList = this.caseListMapToListBean(
//                systemService.selectAllDepts(SecurityConstants.INNER).getData(),UucDeptInfo.class);
//
//        // 从uuc查询组织负责人,并缓存
//        List<UucUserDept> adminUserDept = this.caseListMapToListBean(
//                systemService.getDeptAdminUserList(SecurityConstants.INNER).getData(),UucUserDept.class);
//        Set<String> deptAdminRoleSets = new HashSet<>();
//        for (UucUserDept userDept: adminUserDept) {
//            deptAdminRoleSets.add(userDept.getUserCode());
//        }
//
//        // 缓存组织id和父id的关系
//        Map<String, String> deptParentCache = Maps.newHashMap();
//        for (UucDeptInfo deptInfo: allDeptList) {
//            deptParentCache.put(String.valueOf(deptInfo.getId()), deptInfo.getParentCode());
//        }
//
//        // 获取所有人员
//        List<ModelDetailInfoDto> userList = this.getModelDetailInfoDtos(CmdbModelCodeConstants.CMDB_MODEL_USER_IDENTIFY);
//        // 获取所有项目
//        List<ModelDetailInfoDto> projectList = this.getModelDetailInfoDtos(CmdbModelCodeConstants.CMDB_MODEL_PROJECT_IDENTIFY);
//        // 获取所有组织
//        List<ModelDetailInfoDto> deptList = this.getModelDetailInfoDtos(CmdbModelCodeConstants.CMDB_MODEL_DEPT_IDENTIFY);
//
//        // 遍历人员信息，缓存对应的资源
//        for (ModelDetailInfoDto dto: userList) {
////            threadPool.submit()
//            // cmdb实体名称保存的是用户编码
//            String userCode = dto.getEntityName();
//            // 查询用户对应的资源权限，并缓存
//            this.cacheUserResource(adminRoleSets, userCode);
//            // 查询用户对应的组织，并缓存
//            this.cacheUserDeptRelation(adminRoleSets, deptAdminRoleSets, deptParentCache, userCode);
//            // 查询用户对应的项目，并缓存
//            this.cacheUserProjectRelation(adminRoleSets, userCode);
//            // 遍历项目，缓存人-项目的资源
//            this.cacheUserProjectResource(adminRoleSets, projectList, userCode);
//        }
//        // 缓存组织和项目的关系
//        this.cacheDeptProjects2Redis(deptList);
//        // 缓存组织下的资源
//        this.cacheDeptResource(deptList);
//    }
//
//    /**
//     * 根据模型标识查询模型实例信息
//     * @param modelKey
//     * @return
//     */
//    private List<ModelDetailInfoDto> getModelDetailInfoDtos(String modelKey) {
//        SelectModelDto userModelDto = new SelectModelDto();
//        userModelDto.setCenterModelKey(modelKey);
//        AjaxResult userResult = cmdbService.getModelDetails(userModelDto, SecurityConstants.INNER);
//        return this.caseListMapToListBean(userResult.getData(), ModelDetailInfoDto.class);
//    }
//
//    /**
//     * 缓存用户关联项目下的资源
//     * @param adminRoleSets
//     * @param projectList
//     * @param userCode
//     */
//    private void cacheUserProjectResource(Set<String> adminRoleSets, List<ModelDetailInfoDto> projectList, String userCode) {
//        for (ModelDetailInfoDto projectDto: projectList) {
//            // 缓存项目对应的资源
//            String projectCode = projectDto.getEntityName();
//            ProjectResourceRequest project = new ProjectResourceRequest();
//            project.setProjectCode(projectCode);
//            project.setUserCode(userCode);
//            if (adminRoleSets.contains(userCode)) {
//                project.setAdminFlag(true);
//            }
//            R<List<ResourceInfoDto>> r = cmdbService.projectResources(project, SecurityConstants.INNER);
//            cache2Redis(CacheConstants.USER_PROJECT_RESOURCE_PERMS + userCode + "#" + projectCode, r.getData());
////            cache2Redis(CacheConstants.USER_PROJECT_RESOURCE_PERMS + userCode + "#" + projectCode, this.caseListMapToListBean(r3.getData(), ResourceInfoDto.class));
////            XxlJobHelper.log("=========> cache user&project&resource to redis complete");
//        }
//    }
//
//    /**
//     * 缓存用户关联的项目
//     * @param adminRoleSets
//     * @param userCode
//     */
//    private void cacheUserProjectRelation(Set<String> adminRoleSets, String userCode) {
//        UserProjectRequest userProject = new UserProjectRequest();
//        userProject.setUserCode(userCode);
//        if (adminRoleSets.contains(userCode)) {
//            userProject.setAdminFlag(true);
//        }
//        R<List<ProjectDto>> r = cmdbService.userProjects(userProject, SecurityConstants.INNER);
//        cache2Redis(CacheConstants.USER_PROJECT_PERMS + userCode, r.getData());
////        cache2Redis(CacheConstants.USER_PROJECT_PERMS + userCode, this.caseListMapToListBean(r2.getData(), ProjectDto.class));
////        XxlJobHelper.log("=========> cache user&project to redis complete");
//    }
//
//    /**
//     * 缓存用户关联的组织
//     * @param deptAdminCache
//     * @param deptParentCache
//     * @param userCode
//     */
//    private void cacheUserDeptRelation(Set<String> deptAdminCache, Set<String> adminRoleSets, Map<String, String> deptParentCache, String userCode) {
//        UserDeptRequest userDept = new UserDeptRequest();
//        userDept.setUserCode(userCode);
//        // 如果当前人是组织负责人，或者当前人是管理员角色
//        if (deptAdminCache.contains(userCode) || adminRoleSets.contains(userCode)) {
//            userDept.setAdminFlag(true);
//        }
//        R<List<DeptDto>> r = cmdbService.userDepts(userDept, SecurityConstants.INNER);
////        List<DeptDto> deptDtoList = this.caseListMapToListBean(r1.getData(), DeptDto.class);
//        List<DeptDto> deptDtoList = r.getData();
//        for (DeptDto deptDto: deptDtoList) {
//            deptDto.setParentId(deptParentCache.get(deptDto.getId()));
//        }
//        cache2Redis(CacheConstants.USER_DEPT_PERMS + userCode, deptDtoList);
////        XxlJobHelper.log("=========> cache user&dept to redis complete");
//    }
//
//    /**
//     * 缓存用户资源
//     * @param adminRoleSets
//     * @param userCode
//     */
//    private void cacheUserResource(Set<String> adminRoleSets, String userCode) {
//        UserPermsRequest user = new UserPermsRequest();
//        user.setUserCode(userCode);
//        if (adminRoleSets.contains(userCode)) {
//            user.setAdminFlag(true);
//        }
//        R<List<ResourceInfoDto>> r = cmdbService.userResources(user, SecurityConstants.INNER);
//        cache2Redis(CacheConstants.USER_RESOURCE_PERMS + userCode, r.getData());
////        XxlJobHelper.log("=========> cache user&resource to redis complete");
//    }
//
//    /**
//     * 缓存组织对应资源
//     */
//    public void cacheDeptResource(List<ModelDetailInfoDto> deptList) {
//        // 如果人是组织负责人、则有组织下的所有资源；如果没有，则无需缓存
//        // 遍历组织信息，缓存对应的资源
//        for (ModelDetailInfoDto dto: deptList) {
//            String deptCode = dto.getEntityName();
//            DeptResourceRequest deptRequest = new DeptResourceRequest();
//            deptRequest.setDeptCode(deptCode);
//            R<List<ResourceInfoDto>> r = cmdbService.deptResources(deptRequest, SecurityConstants.INNER);
//            cache2Redis(CacheConstants.DEPT_RESOURCE_PERMS + deptCode, r.getData());
////                cache2Redis(CacheConstants.DEPT_RESOURCE_PERMS + userCode + "#" + deptCode, this.caseListMapToListBean(r.getData(), ResourceInfoDto.class));
//        }
////      XxlJobHelper.log("=========> cache user&dept&resource to redis complete");
//    }
//
//    /**
//     * 组织关联项目缓存
//     */
//    public void cacheDeptProjects2Redis(List<ModelDetailInfoDto> deptList) {
//        for (ModelDetailInfoDto dto: deptList) {
//            String deptCode = dto.getEntityName();
//            DeptResourceRequest deptResourceRequest = new DeptResourceRequest();
//            deptResourceRequest.setDeptCode(deptCode);
//            R<List<ProjectDto>> r = cmdbService.deptProjects(deptResourceRequest, SecurityConstants.INNER);
//            cache2Redis(CacheConstants.DEPT_PROJECT_PERMS + deptCode, r.getData());
//        }
////        XxlJobHelper.log("=========> cache dept&projects to redis complete");
//    }
//
//    /**
//     * 设置缓存，默认ttl 2h
//     * @param key
//     * @param values
//     */
//    private void cache2Redis(final String key, final List values) {
//        if (CollectionUtils.isNotEmpty(values)) {
//            Object[] cacheValue = values.toArray(new Object[values.size()]);
//            redisService.redisTemplate.opsForSet().add(CacheConstants.CACHE_NAMESPACE_RESOURCE_PERMS + key, cacheValue);
////            redisService.redisTemplate.expire(key, 2L, TimeUnit.HOURS);
//        }
//    }
//
//    /**
//     * 转化feign调用之后的List<Map>
//     * @param source
//     * @param clz
//     * @param <T>
//     * @return
//     */
//    private <T> List<T> caseListMapToListBean(Object source, Class<T> clz) {
//        String json = JSON.toJSONString(source);
//        return JSON.parseArray(json, clz);
//    }
//
////    /**
////     * 缓存线程任务
////     */
////    public class cacheTaskThread implements Runnable {
////        private ModelDetailInfoDto dto;
////        private
////        @Override
////        public void run() {
////            // cmdb实体名称保存的是用户编码
////            String userCode = dto.getEntityName();
////            // 查询用户对应的资源权限，并缓存
////            this.cacheUserResource(adminRoleSets, userCode);
////            // 查询用户对应的组织，并缓存
////            this.cacheUserDeptRelation(adminRoleSets, deptAdminRoleSets, deptParentCache, userCode);
////            // 查询用户对应的项目，并缓存
////            this.cacheUserProjectRelation(adminRoleSets, userCode);
////            // 遍历项目，缓存人-项目的资源
////            this.cacheUserProjectResource(adminRoleSets, projectList, userCode);
////        }
////    }
//}
