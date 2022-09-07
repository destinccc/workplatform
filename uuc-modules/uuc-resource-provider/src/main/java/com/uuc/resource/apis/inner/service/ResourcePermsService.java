package com.uuc.resource.apis.inner.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.uuc.common.core.constant.CacheConstants;
import com.uuc.common.core.constant.CmdbConstants;
import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.exception.CheckedException;
import com.uuc.common.core.utils.DateUtils;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.redis.service.RedisService;
import com.uuc.resource.apis.inner.vo.DeptVO;
import com.uuc.resource.apis.inner.vo.ProjectVO;
import com.uuc.resource.apis.inner.vo.TreeSelect;
import com.uuc.system.api.RemoteCmdbService;
import com.uuc.system.api.RemoteSystemService;
import com.uuc.system.api.domain.UserAdminDeptVO;
import com.uuc.system.api.model.UucDeptInfo;
import com.uuc.system.api.model.cmdb.*;
import com.uuc.system.api.domain.ResourceVO;
import com.uuc.system.api.model.vo.EndPonitVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: fxm
 * @date: 2022-06-22
 * @description:
 **/
@Slf4j
@Service
public class ResourcePermsService {
    @Autowired
    private RedisService redisService;

    @Autowired
    private RemoteSystemService systemService;
    @Autowired
    private RemoteCmdbService cmdbService;

    /**
     * 获取用户资源权限
     *
     * @param v
     * @return
     */
    public List<String> userResources(ResourceVO v) {
        List<String> resultList = Lists.newArrayList();
        Set<String> adminRoleSets = new HashSet<String>();
        List<String> adminIds = (List<String>) systemService.getAdminUserList(SecurityConstants.INNER).getData();
        adminRoleSets.addAll(adminIds);
        UserPermsRequest user = new UserPermsRequest();
        user.setUserCode(v.getUserCode());
        user.setResType(v.getResTypes());
        if (adminRoleSets.contains(v.getUserCode())) {
            user.setAdminFlag(true);
        }
        List<ResourceInfoDto> queryList = cmdbService.userResources(user, SecurityConstants.INNER).getData();
        if (CollectionUtils.isNotEmpty(queryList)) {
            resultList.addAll(queryList.stream().map(ResourceInfoDto::getResourceId).collect(Collectors.toList()));
        }

        return resultList;
    }

    /**
     * 获取用户组织权限
     *
     * @return
     */
    public List<TreeSelect> userDepts(String userCode) {
        List<TreeSelect> resultList = Lists.newArrayList();
//        String key = CacheConstants.USER_DEPT_PERMS + userCode;
//        if (hasKey(key)) {
//            resultList = buildMenuTreeSelect(getPerms4redisNormal(key));
//        }
        // 获取所有管理员的id集合
        Set<String> adminRoleSets = new HashSet<String>();
        List<String> adminIds = (List<String>) systemService.getAdminUserList(SecurityConstants.INNER).getData();
        adminRoleSets.addAll(adminIds);

        // neo4j节点数据没有保存父级组织？这里先从门户查询
        List<UucDeptInfo> allDeptList = this.caseListMapToListBean(
                systemService.selectAllDepts(SecurityConstants.INNER).getData(), UucDeptInfo.class);

        // 从uuc查询组织负责人,并缓存
//        List<UucUserDept> adminUserDept = this.caseListMapToListBean(
//                systemService.getDeptAdminUserList(SecurityConstants.INNER).getData(),UucUserDept.class);
//        Set<String> deptAdminRoleSets = new HashSet<>();
//        for (UucUserDept userDept: adminUserDept) {
//            deptAdminRoleSets.add(userDept.getUserCode());
//        }

        // 缓存组织id和父id的关系
        Map<String, String> deptParentCache = Maps.newHashMap();
        for (UucDeptInfo deptInfo : allDeptList) {
            deptParentCache.put(String.valueOf(deptInfo.getId()), deptInfo.getParentCode());
        }

        UserDeptRequest userDept = new UserDeptRequest();
        userDept.setUserCode(userCode);
        // 当前人是管理员角色
        if (adminRoleSets.contains(userCode)) {
            userDept.setAdminFlag(true);
        }
        List<DeptDto> queryList = cmdbService.userDepts(userDept, SecurityConstants.INNER).getData();
        if (CollectionUtils.isNotEmpty(queryList)) {
            for (DeptDto deptDto : queryList) {
                deptDto.setParentId(deptParentCache.get(deptDto.getId()));
            }
            resultList.addAll(buildMenuTreeSelect(queryList));
        }
        return resultList;
    }

    /**
     * 转化feign调用之后的List<Map>
     *
     * @param source
     * @param clz
     * @param <T>
     * @return
     */
    private <T> List<T> caseListMapToListBean(Object source, Class<T> clz) {
        if (source != null) {
            String json = JSON.toJSONString(source);
            return JSON.parseArray(json, clz);
        }
        return new ArrayList<>();
    }

    /**
     * 获取用户关联项目
     *
     * @return
     */
    public List<ProjectDto> userProjects(String userCode) {
        List<ProjectDto> resultList = Lists.newArrayList();
//        String key = CacheConstants.USER_PROJECT_PERMS + userCode;
//        if (hasKey(key)) {
//            resultList = getPerms4redisNormal(key);
//        }
        // 获取所有管理员的id集合
        Set<String> adminRoleSets = new HashSet<String>();
        List<String> adminIds = (List<String>) systemService.getAdminUserList(SecurityConstants.INNER).getData();
        adminRoleSets.addAll(adminIds);

        UserProjectRequest userProject = new UserProjectRequest();
        userProject.setUserCode(userCode);
        if (adminRoleSets.contains(userCode)) {
            userProject.setAdminFlag(true);
        }
        List<ProjectDto> queryList = cmdbService.userProjects(userProject, SecurityConstants.INNER).getData();
        log.info("调用CMDB人员关联项目： 【请求参数：】 {}, 【返回结果】 {}", userProject, queryList);
        if (CollectionUtils.isNotEmpty(queryList)) {
            resultList.addAll(queryList);
        }
        return resultList;
    }

    /**
     * 获取组织项目列表
     */
    public List<ProjectDto> deptProjects(DeptVO v) {
        List<ProjectDto> resultList = Lists.newArrayList();
//        String userCode = v.getUserCode();
//        // 根据指定的组织编码查全量项目
//        List<String> keys = new ArrayList<String>();
//        for (String deptCode: deptCodes) {
//            String key = CacheConstants.DEPT_PROJECT_PERMS + deptCode;
//            if (hasKey(key)) {
//                keys.add(CacheConstants.CACHE_NAMESPACE_RESOURCE_PERMS + key);
//            }
//        }
//
//        // 筛选人所属组织下的项目
//        Boolean adminFlag = (Boolean) systemService.adminOrDeptAdmin(userCode, SecurityConstants.INNER).getData();
//        if (adminFlag != null && adminFlag) {
//            if (keys.size() > 0) {
//                resultList = new ArrayList(getPerms4redisUnion(keys));
//            }
//        } else {
//            // 非管理员，查人和项目、组织和项目的交集
//            if (keys.size() > 0) {
//                keys.add(CacheConstants.USER_PROJECT_PERMS + userCode);
//                resultList = new ArrayList<>(getPerms4redisInterSect(keys));
//            } else{
//                resultList = new ArrayList<>(getPerms4redisNormal(CacheConstants.USER_PROJECT_PERMS + userCode));
//            }
//        }
        // 查询所有组织的所有项目
        DeptResourceRequest deptResourceRequest = new DeptResourceRequest();
        deptResourceRequest.setDeptCode(v.getDeptCodes());
        List<ProjectDto> queryList = cmdbService.deptProjects(deptResourceRequest, SecurityConstants.INNER).getData();
        log.info("调用CMDB组织关联项目： 【请求参数：】 {}, 【返回结果】 {}", deptResourceRequest, queryList);
        // 获取所有管理员的id集合
        Set<String> adminRoleSets = new HashSet<String>();
        List<String> adminIds = (List<String>) systemService.getAdminUserList(SecurityConstants.INNER).getData();
        adminRoleSets.addAll(adminIds);
        // 如果是管理员/运维管理员角色，查全部
        if (adminRoleSets.contains(v.getUserCode())) {
            resultList.addAll(queryList);
        } else {
//            // 根据当前人筛选是传入的组织的负责人的组织
//            UserAdminDeptVO userAdminDeptVO = new UserAdminDeptVO();
//            userAdminDeptVO.setDeptCode(v.getDeptCodes());
//            userAdminDeptVO.setUserCode(v.getUserCode());
//            List<String> userAdminDeptList = (List<String>) systemService.getAdminDeptList(userAdminDeptVO, SecurityConstants.INNER).getData();
//            if (CollectionUtils.isNotEmpty(userAdminDeptList)) {
//                deptResourceRequest.setDeptCode(userAdminDeptList);
//                List<ProjectDto> queryList = cmdbService.deptProjects(deptResourceRequest, SecurityConstants.INNER).getData();
//            }
            // 非管理员，查人和项目、组织和项目的交集
            List<ProjectDto> userProjects = this.userProjects(v.getUserCode());
            resultList.addAll(queryList.stream().filter(userProjects::contains).collect(Collectors.toList()));
        }
        log.info("Resource API组织关联项目： 【请求参数：】 {}, 【返回结果】 {}", v, resultList);
        return resultList;
    }


    /**
     * 获取组织关联资源列表
     *
     * @return
     */
    public List<String> deptResources(DeptVO v) {
        List<String> resultList = Lists.newArrayList();

        // 获取所有管理员的id集合
        Set<String> adminRoleSets = new HashSet<String>();
        List<String> adminIds = (List<String>) systemService.getAdminUserList(SecurityConstants.INNER).getData();
        adminRoleSets.addAll(adminIds);

        DeptResourceRequest deptRequest = new DeptResourceRequest();
        deptRequest.setResType(v.getResTypes());

        if (adminRoleSets.contains(v.getUserCode())) {
            // 如果当前人是管理员/运维管理员角色，直接查组织的资源
            deptRequest.setDeptCode(v.getDeptCodes());
            List<ResourceInfoDto> queryList = cmdbService.deptResources(deptRequest, SecurityConstants.INNER).getData();
            if (CollectionUtils.isNotEmpty(queryList)) {
                resultList.addAll(queryList.stream().map(ResourceInfoDto::getResourceId).collect(Collectors.toList()));
            }
        } else {
            // 根据当前人筛选是传入的组织的负责人的组织
            UserAdminDeptVO userAdminDeptVO = new UserAdminDeptVO();
            userAdminDeptVO.setDeptCode(v.getDeptCodes());
            userAdminDeptVO.setUserCode(v.getUserCode());
            List<String> userAdminDeptList = (List<String>) systemService.getAdminDeptList(userAdminDeptVO, SecurityConstants.INNER).getData();
            if (CollectionUtils.isNotEmpty(userAdminDeptList)) {
                deptRequest.setDeptCode(userAdminDeptList);
                List<ResourceInfoDto> queryList = cmdbService.deptResources(deptRequest, SecurityConstants.INNER).getData();
                if (CollectionUtils.isNotEmpty(queryList)) {
                    resultList.addAll(queryList.stream().map(ResourceInfoDto::getResourceId).collect(Collectors.toList()));
                }
            }
        }
        // 非组织管理员，不能看到组织下的资源
//        if (adminFlag!= null && adminFlag) {
        // 查询指定组织的所有资源
//            List<String> keys = new ArrayList<String>();
//            for (String deptCode: deptCodes) {
//                String key = CacheConstants.DEPT_RESOURCE_PERMS + deptCode;
//                if (hasKey(key)) {
//                    keys.add(CacheConstants.CACHE_NAMESPACE_RESOURCE_PERMS + key);
//                }
//            }
//            if (keys.size() > 0) {
//                Set<ResourceInfoDto> set = getPerms4redisUnion(keys);
//                List<String> resTypes = v.getResTypes();
//                Set<String> resTypesCache = new HashSet<>(resTypes);
//                if (CollectionUtils.isNotEmpty(resTypes)) {
//                    resultList = set.stream().filter(resourceInfoDto -> resTypesCache.contains(resourceInfoDto.getResType()))
//                            .map(ResourceInfoDto::getResourceId).collect(Collectors.toList());
//                } else {
//                    resultList = set.stream().map(ResourceInfoDto::getResourceId).collect(Collectors.toList());
//                }
//            }
//            DeptResourceRequest deptRequest = new DeptResourceRequest();
//            deptRequest.setDeptCode(v.getDeptCodes());
//            List<ResourceInfoDto> queryList = cmdbService.deptResources(deptRequest, SecurityConstants.INNER).getData();
//            if (CollectionUtils.isNotEmpty(queryList)) {
//                if (CollectionUtils.isNotEmpty(v.getResTypes())) {
//                    Set<String> resTypesCache = new HashSet<>(v.getResTypes());
//                    resultList.addAll(queryList.stream().filter(resourceInfoDto -> resTypesCache.contains(resourceInfoDto.getResType()))
//                            .map(ResourceInfoDto::getResourceId).collect(Collectors.toList()));
//                } else {
//                    resultList.addAll(queryList.stream().map(ResourceInfoDto::getResourceId).collect(Collectors.toList()));
//                }
//                resultList.addAll(queryList.stream().map(ResourceInfoDto::getResourceId).collect(Collectors.toList()));
//            }
//        }
        return resultList;
    }

    /**
     * 获取项目关联资源列表
     *
     * @return
     */
    public List<String> projectResources(ProjectVO v) {
        List<String> projectCodes = v.getProjectCodes();
        if (CollectionUtils.isEmpty(projectCodes)) {
            throw new CheckedException("projectCodes can't be null");
        }
        List<String> resultList = Lists.newArrayList();

//        // 查询指定项目下的所有资源
//        List<String> keys = new ArrayList<String>();
//        for (String projectCode: projectCodes) {
//            String key = CacheConstants.USER_PROJECT_RESOURCE_PERMS + v.getUserCode() + "#" + projectCode;
//            if (hasKey(key)) {
//                keys.add(CacheConstants.CACHE_NAMESPACE_RESOURCE_PERMS + key);
//            }
//        }
//        if (keys.size() > 0) {
//            Set<ResourceInfoDto> set = getPerms4redisUnion(keys);
//            List<String> resTypes = v.getResTypes();
//            Set<String> resTypesCache = new HashSet<>(resTypes);
//            if (CollectionUtils.isNotEmpty(resTypes)) {
//                resultList = set.stream().filter(resourceInfoDto -> resTypesCache.contains(resourceInfoDto.getResType()))
//                        .map(ResourceInfoDto::getResourceId).collect(Collectors.toList());
//            } else {
//                resultList = set.stream().map(ResourceInfoDto::getResourceId).collect(Collectors.toList());
//            }
//        }
        Set<String> adminRoleSets = new HashSet<String>();
        List<String> adminIds = (List<String>) systemService.getAdminUserList(SecurityConstants.INNER).getData();
        adminRoleSets.addAll(adminIds);

        ProjectResourceRequest project = new ProjectResourceRequest();
        project.setProjectCode(v.getProjectCodes());
        project.setUserCode(v.getUserCode());
        project.setResType(v.getResTypes());
        if (adminRoleSets.contains(v.getUserCode())) {
            project.setAdminFlag(true);
        }
        List<ResourceInfoDto> queryList = cmdbService.projectResources(project, SecurityConstants.INNER).getData();
        if (CollectionUtils.isNotEmpty(queryList)) {
            resultList.addAll(queryList.stream().map(ResourceInfoDto::getResourceId).collect(Collectors.toList()));
        }
//        List<String> resTypes = v.getResTypes();
//        Set<String> resTypesCache = new HashSet<>(resTypes);
//        if (CollectionUtils.isNotEmpty(resTypes)) {
//            resultList.addAll(queryList.stream().filter(resourceInfoDto -> resTypesCache.contains(resourceInfoDto.getResType()))
//                    .map(ResourceInfoDto::getResourceId).collect(Collectors.toList()));
//        } else {
//            resultList.addAll(queryList.stream().map(ResourceInfoDto::getResourceId).collect(Collectors.toList()));
//        }
        return resultList;

    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 组织列表
     * @return 下拉树结构列表
     */

    public List<TreeSelect> buildMenuTreeSelect(List<DeptDto> depts) {
        List<DeptDto> deptTrees = this.buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    public List<DeptDto> buildDeptTree(List<DeptDto> depts) {
        List<DeptDto> returnList = new ArrayList<DeptDto>();
        List<String> tempList = new ArrayList<String>();
        for (DeptDto dept : depts) {
            tempList.add(dept.getId());
        }
        for (DeptDto dept : depts) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = depts;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<DeptDto> list, DeptDto t) {
        // 得到子节点列表
        List<DeptDto> childList = getChildList(list, t);
        t.setChildren(childList);
        for (DeptDto tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<DeptDto> getChildList(List<DeptDto> list, DeptDto t) {
        List<DeptDto> tlist = new ArrayList<DeptDto>();
        Iterator<DeptDto> it = list.iterator();
        while (it.hasNext()) {
            DeptDto n = (DeptDto) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().equals(t.getId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<DeptDto> list, DeptDto t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }

    /**
     * 权限服务通用Set结构缓存获取
     *
     * @param key
     * @return
     */
    private Boolean hasKey(String key) {
        return redisService.hasKey(CacheConstants.CACHE_NAMESPACE_RESOURCE_PERMS + key);
    }

    /**
     * 权限服务通用Set结构缓存获取
     *
     * @param key
     * @return List
     */
    private List getPerms4redisNormal(String key) {
        List resultList = Lists.newArrayList();
        Set sets = redisService.redisTemplate.opsForSet().members(CacheConstants.CACHE_NAMESPACE_RESOURCE_PERMS + key);
        resultList.addAll(sets);
        return resultList;
    }

    /**
     * 权限服务通用Set结构缓存并集获取
     *
     * @param keys
     * @return
     */
    private Set getPerms4redisUnion(List<String> keys) {
        return redisService.redisTemplate.opsForSet().union(keys);
    }

    /**
     * 权限服务通用Set结构缓存交集获取
     *
     * @param keys
     * @return
     */
    private Set getPerms4redisInterSect(List<String> keys) {
        return redisService.redisTemplate.opsForSet().intersect(keys);
    }

    public List<EndPonitVo> endPointResources(ResourceVO v) {
        List<EndPonitVo> result = Lists.newArrayList();
        Set<String> adminRoleSets = new HashSet<String>();
        List<String> adminIds = (List<String>) systemService.getAdminUserList(SecurityConstants.INNER).getData();
        adminRoleSets.addAll(adminIds);
        UserPermsRequest user = new UserPermsRequest();
        user.setUserCode(v.getUserCode());
        user.setResType(v.getResTypes());
        if (adminRoleSets.contains(v.getUserCode())) {
            user.setAdminFlag(true);
        }
        List<ResourceInfoDto> queryList = cmdbService.userResources(user, SecurityConstants.INNER).getData();
        List<String> needMapData = Lists.newArrayList(CmdbConstants.VirtualMachine, CmdbConstants.PhysicalServer);
        List<String> modelKeyList = queryList.stream().map(ResourceInfoDto::getResType).filter(i -> needMapData.contains(i)).collect(Collectors.toList());
        Map<String, List<ModelDetailInfoDto>> modelDataListMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(modelKeyList)) {
            for (String modelKey : modelKeyList) {
                SelectModelDto dto = new SelectModelDto();
                dto.setCenterModelKey(modelKey);
                try {
                    if(Objects.nonNull(modelDataListMap.get(modelKey))){
                        continue;
                    }
                    AjaxResult modelDetails = cmdbService.getModelDetails(dto, SecurityConstants.INNER);
                    List<ModelDetailInfoDto> modelDetailInfoDtos = JSONArray.parseArray(JSONUtil.toJsonStr(modelDetails.getData()), ModelDetailInfoDto.class);
                    modelDataListMap.put(modelKey, modelDetailInfoDtos);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        for (ResourceInfoDto resourceInfoDto : queryList) {
            EndPonitVo vo = new EndPonitVo();
            BeanUtil.copyProperties(resourceInfoDto, vo);
            String resType = resourceInfoDto.getResType();
            List<ModelDetailInfoDto> modelDetailInfoDtos = modelDataListMap.get(resType);
            if (CollectionUtils.isNotEmpty(modelDetailInfoDtos)) {
                String resourceId = resourceInfoDto.getResourceId();
                ModelDetailInfoDto dto = modelDetailInfoDtos.stream().filter(i -> i.getEntityName().equals(resourceId)).findFirst().get();
                if (Objects.nonNull(dto)){
                    String entityForm = dto.getEntityForm();
                    Map map = JSONUtil.toBean(entityForm, Map.class);
                    vo.setResourceMap(map);
                }
            }
            result.add(vo);
        }
        return result;
    }
}
