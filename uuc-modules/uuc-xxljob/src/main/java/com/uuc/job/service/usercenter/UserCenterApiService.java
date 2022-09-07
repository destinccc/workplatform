package com.uuc.job.service.usercenter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.constant.SyncConstants;
import com.uuc.common.core.enums.SexEnum;
import com.uuc.common.core.exception.ServiceException;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.core.utils.http.HttpUtils;
import com.uuc.job.service.usercenter.config.UserCenterProperties;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.job.service.usercenter.model.common.UcApiResult;
import com.uuc.job.service.usercenter.model.common.UcPagination;
import com.uuc.job.service.usercenter.model.org.UserCenterOrg;
import com.uuc.job.service.usercenter.model.user.UserCenterUser;
import com.uuc.system.api.RemoteSystemService;
import com.uuc.system.api.RemoteUaaService;
import com.uuc.system.api.model.UucDeptInfo;
import com.uuc.system.api.model.UucUserDept;
import com.uuc.system.api.model.UucUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 通过API获取用户中心的组织/用户
 * @author: Destin
 */
@Service
@Slf4j
public class UserCenterApiService {

    @Autowired
    private RemoteUaaService uaaService;
    @Autowired
    private UserCenterProperties userCenterProperties;
    @Autowired
    private RemoteSystemService remoteSystemService;

    private static final String APP_ID = "App-Id";
    private static final String APP_SECRET = "App-Secret";
    // 获取组织列表
    private static final String USER_CENTER_GET_GROUP = "/v3/api/group/list";
    // 用户池,直接获取到用户与用户的归属组织
    private static final String USER_CENTER_GET_USER_POOL = "/v3/api/userpool/view";

    public Map<String, Object> userCenterUser2Map(UserCenterUser userCenterUser) {
        Map result = Maps.newHashMap();
        String userid = String.valueOf(userCenterUser.getUid());
        result.put("extendId", userid);
        result.put("phone", StringUtils.null2Empty(userCenterUser.getPhone()));
        result.put("userName", userCenterUser.getName());
        result.put("userLoginAccount", userCenterUser.getUsername());
        result.put("userCode", userid);
        String sex = getSex(userCenterUser.getSex());
        result.put("sex", sex);
        result.put("email", userCenterUser.getEmail());
        result.put("createTime", userCenterUser.getReg_time());
        //归属组织
        //顶级组织code todo zy
        String parentId = "2";
        List<String> belongOrgs = new ArrayList<>();
        List<UserCenterOrg> orgList = userCenterUser.getOrg();
        if (CollectionUtils.isNotEmpty(orgList)) {
            for (UserCenterOrg userCenterOrg : orgList) {
                AjaxResult ajaxResult = remoteSystemService.selectDeptByDingtalkId(userCenterOrg.getCode(), SecurityConstants.INNER);
                Object obj = ajaxResult.getData();
                if (null != obj) {
                    UucDeptInfo uucDeptInfo = JSONUtil.toBean(JSONUtil.toJsonStr(obj), UucDeptInfo.class);
                    belongOrgs.add(String.valueOf(uucDeptInfo.getId()));
                }
            }
        }else {
            belongOrgs.add(parentId);
        }
        CollectionUtil.sortByPinyin(belongOrgs);
        result.put(SyncConstants.CONTAINED_ORG, belongOrgs);
        log.info("用户中心里的用户信息:  " + JSONUtil.toJsonStr(result));
        return result;
    }

    public String getSex(String sex) {
        if (StringUtils.isEmpty(sex)) {
            return SexEnum.UNKONW.getCode();
        }
        if (SexEnum.MALE.getInfo().equals(sex)) {
            return SexEnum.MALE.getCode();
        } else if (SexEnum.FEMALE.getInfo().equals(sex)) {
            return SexEnum.FEMALE.getCode();
        } else {
            return SexEnum.UNKONW.getCode();
        }
    }

    //UUC用户映射为Map
    public Map<String, Object> uucUser2Map(UucUserInfo userInfo) {
        Map result = Maps.newHashMap();
        result.put("extendId", userInfo.getExtendId());
        result.put("phone", userInfo.getPhone());
        result.put("userName", userInfo.getUserName());
        result.put("userCode", userInfo.getUserCode());
        result.put("sex", userInfo.getSex());
        result.put("email", userInfo.getEmail());
        result.put("createTime", userInfo.getCreateTime());
        List<UucUserDept> deptList = userInfo.getDeptList();
        List<String> belongOrgs = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(deptList)) {
            for (UucUserDept uucUserDept : deptList) {
                String deptCode = uucUserDept.getDeptCode();
                belongOrgs.add(deptCode);
            }
        }
        CollectionUtil.sortByPinyin(belongOrgs);
        result.put(SyncConstants.CONTAINED_ORG, belongOrgs);
        log.info("UUC工作台人员信息: {} ",JSONUtil.toJsonStr(result));
        return result;
    }

    public UucUserInfo getUucUserInfo(Map<String, Object> apiUserMap) {
        UucUserInfo updateUser = JSONUtil.toBean(JSONUtil.toJsonStr(apiUserMap), UucUserInfo.class);
        List<String> belongOrgs = (List) apiUserMap.get(SyncConstants.CONTAINED_ORG);
        List<UucUserDept> deptList = Lists.newArrayList();
        for (String belongOrg : belongOrgs) {
            UucUserDept userDept = new UucUserDept();
            userDept.setUserCode(updateUser.getUserCode());
            userDept.setDeptCode(belongOrg);
            userDept.setAdminFlag("0");
            deptList.add(userDept);
        }
        updateUser.setDeptList(deptList);
        return updateUser;
    }

    public Map<String, Object> userCenterDept2Map(UserCenterOrg userCenterOrg) {
        Map result = Maps.newHashMap();
        String deptId = userCenterOrg.getCode();
        //父级code todo zy
        String parentId = "2";
        result.put("deptCode", deptId);
        result.put("dingDeptId", deptId);
        result.put("deptName", userCenterOrg.getName());
        if (StringUtils.isNotEmpty(parentId)) {
            result.put("parentCode", parentId);
        } else {
            result.put("parentCode", "");
        }
        List<String> deptIds = StringUtils.isNotEmpty(parentId) ? Lists.newArrayList(deptId, parentId) : Lists.newArrayList(deptId);
        List<String> deptStrList = CollectionUtil.reverse(deptIds.stream().map(String::valueOf).collect(Collectors.toList()));
        String ancestors = String.join(",", deptStrList);
        result.put("ancestors", ancestors);
        result.put("level", deptStrList.size());
        log.info("用户中心组织信息:{}", JSONUtil.toJsonStr(result));
        return result;
    }


    /*
     * @Author Destin
     * @Description 获取组织列表
     **/
    public List<UserCenterOrg> getUserCenterOrgList() throws Exception {
        String usercenterUrl = uaaService.getUserCenterUrl(SecurityConstants.INNER).getData();
        String apiUrl = usercenterUrl + USER_CENTER_GET_GROUP;
        Map<String, String> requestParam = Maps.newHashMap();
        requestParam.put("status", "enabled");
        Map<String, String> requestHeader = Maps.newHashMap();
        requestHeader.put(APP_ID, userCenterProperties.getAppId());
        requestHeader.put(APP_SECRET, userCenterProperties.getAppSecret());
        HttpResponse httpResponse = HttpUtils.doGet(apiUrl, "", requestHeader, requestParam);
        String apiResponse = IOUtils.toString(httpResponse.getEntity().getContent(), "utf-8");
        UcApiResult apiResult = JSONUtil.toBean(apiResponse, UcApiResult.class);
        if (200 != apiResult.getCode().intValue()) {
            String msg = "获取组织列表失败!";
            log.warn(StringUtils.getSlfStr(apiUrl) + msg);
            throw new ServiceException(msg);
        }
        JSONArray orgList = JSONUtil.parseArray(apiResult.getData());
        List<UserCenterOrg> result = JSONObject.parseArray(JSONUtil.toJsonStr(orgList), UserCenterOrg.class);
        return result;
    }

    /*
     * @Author Destin
     * @Description 获取用户池的用户及用户的组织,返回全量result
     **/
    public List<UserCenterUser> getUserCenterUserPoolList(List<UserCenterUser> result, Long page, Long pageSize) throws Exception {
        page = Objects.isNull(page) ? 1L : page;
        pageSize = Objects.isNull(pageSize) ? 1000L : pageSize;
        String usercenterUrl = uaaService.getUserCenterUrl(SecurityConstants.INNER).getData();
        String apiUrl = usercenterUrl + USER_CENTER_GET_USER_POOL;
        Map<String, String> requestHeader = Maps.newHashMap();
        Map<String, String> requestParam = Maps.newHashMap();
        requestHeader.put(APP_ID, userCenterProperties.getAppId());
        requestHeader.put(APP_SECRET, userCenterProperties.getAppSecret());
        requestParam.put("status", "enabled");
        requestParam.put("page", String.valueOf(page));
        requestParam.put("page_size", String.valueOf(pageSize));
        HttpResponse httpResponse = HttpUtils.doGet(apiUrl, "", requestHeader, requestParam);
        String apiResponse = IOUtils.toString(httpResponse.getEntity().getContent(), "utf-8");
        UcApiResult apiResult = JSONUtil.toBean(apiResponse, UcApiResult.class);
        if (200 != apiResult.getCode().intValue()) {
            String msg = "获取用户池用户列表失败!";
            log.warn(StringUtils.getSlfStr(apiUrl) + msg);
            throw new ServiceException(msg);
        }
        JSONArray userList = JSONUtil.parseArray(apiResult.getData());
        List<UserCenterUser> apiReturn = JSONObject.parseArray(JSONUtil.toJsonStr(userList), UserCenterUser.class);
        result.addAll(apiReturn);
        UcPagination pagination = apiResult.getMeta().getPagination();
        if (!pagination.getCurrent_page().equals(pagination.getTotal_pages())) {
            Long nextPage = pagination.getCurrent_page() + 1L;
            getUserCenterUserPoolList(result, nextPage, pageSize);
        }
        Set<Long> filterUid = Sets.newHashSet();
        result = result.stream().filter(i -> filterUid.add(i.getUid())).collect(Collectors.toList());
        return result;
    }


    public static void main(String[] args) throws Exception {
//        testOrgExtend();
    }

    private static void testOrgExtend() {
        String a =
                "{\n" +
                        "     \"code\": 200,\n" +
                        "     \"message\": \"成功\",\n" +
                        "     \"data\": [\n" +
                        "          {\n" +
                        "               \"name\": \"重庆医科大学附属第一医院\",\n" +
                        "               \"code\": \"12500000450405726W\",\n" +
                        "               \"type_code\": \"ORGTYPE0003\",\n" +
                        "               \"type_name\": \"事业单位\",\n" +
                        "               \"scale_code\": \"ORGSCALE0001\",\n" +
                        "               \"scale_name\": \"三级特定\",\n" +
                        "               \"region_name\": \"万州区\",\n" +
                        "               \"region_code\": 500101,\n" +
                        "               \"region_limits\": [\n" +
                        "                    {\n" +
                        "                         \"region_name\": \"南京市\",\n" +
                        "                         \"region_code\": 320100,\n" +
                        "                         \"region_lev\": 2\n" +
                        "                    }\n" +
                        "               ],\n" +
                        "               \"longitude\": 10.132345,\n" +
                        "               \"latitude\": 15.132345,\n" +
                        "               \"source_type\": \"运营添加 \",\n" +
                        "               \"extends\": [\n" +
                        "                    {\n" +
                        "                         \"key\": \"SC_ORG_HIS_CODE\",\n" +
                        "                         \"value\": \"8BCDCA4E11A44A9D888D7E70DD4FF44F\",\n" +
                        "                         \"name\": \"四川HIS组织编码\"\n" +
                        "                    }\n" +
                        "               ]\n" +
                        "          },\n" +
                        "          {\n" +
                        "               \"name\": \"重庆医科大学附属第二医院\",\n" +
                        "               \"code\": \"12500000450405750D\",\n" +
                        "               \"type_code\": \"ORGTYPE0001\",\n" +
                        "               \"type_name\": \"企业\",\n" +
                        "               \"scale_code\": \"ORGSCALE0011\",\n" +
                        "               \"scale_name\": \"特大型\",\n" +
                        "               \"region_name\": \"万州区\",\n" +
                        "               \"region_code\": 500101,\n" +
                        "               \"region_limits\": [\n" +
                        "                    {\n" +
                        "                         \"region_name\": \"江苏省\",\n" +
                        "                         \"region_code\": 320000,\n" +
                        "                         \"region_lev\": 1\n" +
                        "                    }\n" +
                        "               ],\n" +
                        "               \"longitude\": 10.132345,\n" +
                        "               \"latitude\": 15.132345,\n" +
                        "               \"source_type\": \"运营添加\",\n" +
                        "               \"extends\": [\n" +
                        "                    {\n" +
                        "                         \"key\": \"SC_ORG_HIS_CODE\",\n" +
                        "                         \"value\": \"8BCDCA4E11A44A9D888D7E70DD4FF44F\",\n" +
                        "                         \"name\": \"四川HIS组织编码\"\n" +
                        "                    }\n" +
                        "               ]\n" +
                        "          }\n" +
                        "     ],\n" +
                        "     \"error\": {\n" +
                        "          \"error_code\": 0\n" +
                        "     },\n" +
                        "     \"meta\": {\n" +
                        "          \"trace\": {\n" +
                        "               \"trace_id\": \"d6328f23-a594-4761-8eca-e9168619fa87\",\n" +
                        "               \"parent_id\": \"\",\n" +
                        "               \"span_id\": \"b4d50ee4-fed8-4e90-af9e-0a5a0dc3acd8\",\n" +
                        "               \"request_id\": \"\"\n" +
                        "          }\n" +
                        "     }\n" +
                        "}";

        UcApiResult apiResult = JSONUtil.toBean(a, UcApiResult.class);
        JSONArray objects = JSONUtil.parseArray(apiResult.getData());
        List<UserCenterOrg> orgList = JSONObject.parseArray(JSONObject.toJSONString(objects), UserCenterOrg.class);
        System.out.println("OrgListJSON: " + JSONUtil.toJsonStr(orgList));
        Gson gson = new Gson();
        List<UserCenterOrg> gonList = gson.fromJson(gson.toJson(objects), new TypeToken<List<UserCenterOrg>>() {
        }.getType());
        System.out.println("OrgListGSON: " + JSONUtil.toJsonStr(gonList));
    }

}
