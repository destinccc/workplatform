package com.uuc.job.dingtalk.util;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.google.common.collect.Lists;
import com.uuc.common.core.exception.ServiceException;
import com.uuc.common.core.utils.DateUtils;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.redis.service.RedisService;
import com.uuc.job.dingtalk.constant.DingTalkApiConstants;
import com.uuc.job.dingtalk.constant.DingtalkRelConstants;
import com.uuc.job.dingtalk.property.DingTalkConfig;
import com.uuc.job.dingtalk.token.DingToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@ConditionalOnWebApplication
@Configuration
@Slf4j
public class DingTalkUtils {

    public static  String DINGTALK_ACCESS_TOKEN = "async:token_dingtalk";

    @Autowired
    private RedisService redisService;

    @Autowired
    private DingTalkConfig dingTalkConfig;

    // 最大重试次数 3
    private final int maxRetryTimes = 3;

    /**
     * 获取钉钉的token认证
     * access_token的有效期为7200秒（2小时），有效期内重复获取会返回相同结果并自动续期，过期后获取会返回新的access_token。
     */
    public String getAccessToken() {
        int retryTimes = 0;
        String APP_KEY = dingTalkConfig.getAppKey();
        String APP_SECRET = dingTalkConfig.getAppSecret();
        DingToken cacheObject = redisService.getCacheObject(DINGTALK_ACCESS_TOKEN);
        if (Objects.nonNull(cacheObject)) {
            DingToken redisToken = JSONUtil.toBean(JSONUtil.toJsonStr(cacheObject),DingToken.class);
            if (StringUtils.isNotBlank(APP_KEY) && StringUtils.isNotBlank(APP_SECRET)
                    && APP_KEY.equals(redisToken.getAppKey()) && APP_SECRET.equals(redisToken.getAppSecret())) {
                return redisToken.getToken();
            }
        }
        String token = "";
        if (StrUtil.isBlank(APP_KEY) || StrUtil.isBlank(APP_SECRET)){
            throw new ServiceException("未配置钉钉账号信息,请联系管理员!");
        }
//        String APP_KEY = "dingka5voydzynfocyp2";
//        String APP_SECRET = "GL68Fhn70oo2XiVxYO-CWeXH10thpMa9w79cY6kj1BLvvTUZIxZ3iMKywuKD3Z7b";
        DingTalkClient client = new DefaultDingTalkClient(DingTalkApiConstants.GET_TOKEN);
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(APP_KEY);
        request.setAppsecret(APP_SECRET);
        request.setHttpMethod("GET");

        do {
            try {
                OapiGettokenResponse response = client.execute(request);
                Long errcode = response.getErrcode();
                if (Objects.nonNull(errcode) && 0 == errcode.longValue()){
                    // 成功日志 错误码为0
                    token = response.getAccessToken();
                    // 缓存60分钟
                    DingToken dingToken = DingToken.builder().token(token).appKey(APP_KEY).appSecret(APP_SECRET).build();
                    redisService.setCacheObject(DINGTALK_ACCESS_TOKEN, dingToken, 60L, TimeUnit.MINUTES);
                    // Todo Api调用成功日志
                    return token;
                }else {
                    throw new ServiceException(response.getErrmsg());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                if (retryTimes + 1 > maxRetryTimes) {
                    log.warn("获取钉钉Token【" + DingTalkApiConstants.GET_TOKEN + "】错误重试达到最大次数【{}】,错误信息 :{}", maxRetryTimes,e.getMessage());
                    // Todo Api调用失败日志

                    throw new ServiceException("钉钉token调用失败,请联系管理员! " + e.getMessage());
                }
            }
        } while (retryTimes++ < maxRetryTimes);

        return token;
    }



    /**
     * 通过授权码获取钉钉部门列表 调用不会出错,会返回错误码
     * @return java.lang.String
     * {
     * 	"errcode":0,
     * 	"result":[
     *                {
     * 			"auto_add_user":true,
     * 			"parent_id":561014463,
     * 			"name":"财务部",
     * 			"dept_id":564674164,
     * 			"create_dept_group":true
     *        },
     *        {
     * 			"auto_add_user":true,
     * 			"parent_id":561014463,
     * 			"name":"HRBP",
     * 			"dept_id":564710150,
     * 			"create_dept_group":true
     *        }
     * 	],
     * 	"errmsg":"ok",
     * 	"request_id":"oe2bj0yc21oy"
     * }
     **/
    public List<OapiV2DepartmentListsubResponse.DeptBaseResponse> getDepList(Long parentDeptId) {
        int retryTimes = 0;
        // 最上级默认1L
        String accessToken = getAccessToken();
        parentDeptId = Objects.isNull(parentDeptId) ? 1L : parentDeptId;
        DingTalkClient client = new DefaultDingTalkClient(DingTalkApiConstants.GET_DEP_LIST_V2);
        OapiV2DepartmentListsubRequest request = new OapiV2DepartmentListsubRequest();
        request.setHttpMethod("GET");
        request.setDeptId(parentDeptId);
        OapiV2DepartmentListsubResponse response = null;
        log.info("钉钉【部门信息获取】endPoint : {} ", DingTalkApiConstants.GET_DEP_LIST_V2 + " 上级部门ID :" + parentDeptId);
        List body = Lists.newArrayList();
        Long start = DateUtils.getCurrentTimeMillis();
        do {
            try {
                response = client.execute(request, accessToken);
                Long errcode = response.getErrcode();
                if (Objects.nonNull(errcode) && 0 == errcode.longValue()){
                    // 成功日志 错误码为0
                    body = response.getResult();

                }else{
                }
                // 错误日志返回空集合
                return body;
            } catch (Exception e) {
                log.error(e.getMessage());
                if (retryTimes + 1 > maxRetryTimes) {
                    log.warn("钉钉【部门信息获取】endPoint :【" + DingTalkApiConstants.GET_DEP_LIST_V2 + "】错误重试达到最大次数【{}】", maxRetryTimes);
                    throw new ServiceException(DingTalkApiConstants.ApiErrorStr);
                }
            }
        } while (retryTimes++ < maxRetryTimes);

        return body;
    }


/*
获取部门下人员信息
https://developers.dingtalk.com/document/app/queries-the-complete-information-of-a-department-user
 */
    public OapiV2UserListResponse.PageResult getDepUserList(Long deptID, Long cursor) {
        int retryTimes = 0;
        // 最上级默认1L
        String accessToken = getAccessToken();
        deptID = Objects.isNull(deptID) ? 1L : deptID;
        DingTalkClient client = new DefaultDingTalkClient(DingTalkApiConstants.DEP_USERID_LIST);
        OapiV2UserListRequest request = new OapiV2UserListRequest();
//        cursor 分页查询 未查完 返回参数中带下次分页参数
        cursor = Objects.isNull(cursor) ? 0L : cursor;
        request.setDeptId(deptID);
        request.setCursor(cursor);
        // 最大分页pageSize为 100
        request.setSize(100L);
        OapiV2UserListResponse response = null;
        log.info("钉钉【部门人员详细信息获取】endPoint : {} ", DingTalkApiConstants.DEP_USERID_LIST + " 部门ID :" + deptID+" 分页游标 :"+cursor);
        OapiV2UserListResponse.PageResult body = null;
        Long start = DateUtils.getCurrentTimeMillis();
        do {
            try {
                response = client.execute(request, accessToken);
                Long errcode = response.getErrcode();
                if (Objects.nonNull(errcode) && 0 == errcode.longValue()){
                    // 成功日志 错误码为0
                    body = response.getResult();
                    try {
                        // 设置部门leader
                        List<OapiV2UserListResponse.ListUserResponse> list = body.getList();
                        if (CollectionUtils.isNotEmpty(list)){
                            for (OapiV2UserListResponse.ListUserResponse deptUser : list) {
                                if (deptUser.getLeader()){
                                    List<Long> leaderDepts = DingtalkRelConstants.DeptLeader.get(deptUser.getUserid());
                                    if (CollectionUtils.isEmpty(leaderDepts)){
                                        leaderDepts = Lists.newArrayList();
                                        DingtalkRelConstants.DeptLeader.put(deptUser.getUserid(),leaderDepts);
                                    }
                                    leaderDepts.add(deptID);
                                }
                            }
                        }
                    } catch (Exception e) {

                    }
                }else{
                }
                // 错误日志 返回null
                return body;
            } catch (Exception e) {
                log.error(e.getMessage());
                if (retryTimes + 1 > maxRetryTimes) {
                    log.warn("钉钉【部门人员详细信息获取】endPoint :【" + DingTalkApiConstants.DEP_USERID_LIST + "】错误重试达到最大次数【{}】", maxRetryTimes);
                    // Todo Api调用失败日志
                    throw new ServiceException(DingTalkApiConstants.ApiErrorStr);
                }
            }
        } while (retryTimes++ < maxRetryTimes);

        return body;
    }

/*
获取部门详情
 */
    public OapiV2DepartmentGetResponse.DeptGetResponse getDepDetails(Long deptID) {
        int retryTimes = 0;
        String accessToken = getAccessToken();
        DingTalkClient client = new DefaultDingTalkClient(DingTalkApiConstants.DEP_DETAILS);
        OapiV2DepartmentGetRequest request = new OapiV2DepartmentGetRequest();
        request.setDeptId(deptID);
        OapiV2DepartmentGetResponse  response = null;
        log.info("钉钉【部门详细信息获取】endPoint : {} ", DingTalkApiConstants.DEP_DETAILS + " 部门ID :" + deptID);
        OapiV2DepartmentGetResponse.DeptGetResponse result = null;
        Long start = DateUtils.getCurrentTimeMillis();
        do {
            try {
                response = client.execute(request, accessToken);
                Long errcode = response.getErrcode();
                if (Objects.nonNull(errcode) && 0 == errcode.longValue()){
                    // 成功日志 错误码为0
                    result = response.getResult();
                }else{
                }
                // 错误日志 返回null
                return result;
            } catch (Exception e) {
                log.error(e.getMessage());
                if (retryTimes + 1 > maxRetryTimes) {
                    log.warn("钉钉【部门人员详细信息获取】endPoint :【" + DingTalkApiConstants.DEP_USERID_LIST + "】错误重试达到最大次数【{}】", maxRetryTimes);
                    // Todo Api调用失败日志
                    throw new ServiceException(DingTalkApiConstants.ApiErrorStr);
                }
            }
        } while (retryTimes++ < maxRetryTimes);

        return result;
    }


    /*
     获取审批流 指定模板 的审批流程 实例Id 的列表
     https://open-dev.dingtalk.com/apiExplorer?spm=ding_open_doc.document.0.0.3206722f3O0nv6#/?devType=org&api=dingtalk.oapi.processinstance.listids
     */
    public OapiProcessinstanceListidsResponse.PageResult getProcessInstanceList(String processCode,Long cursor) {
        int retryTimes = 0;
        String accessToken = getAccessToken();
        DingTalkClient client = new DefaultDingTalkClient(DingTalkApiConstants.PROCESS_INSTANCE_LIST);
        OapiProcessinstanceListidsRequest request = new OapiProcessinstanceListidsRequest();
        request.setProcessCode(processCode);
        // 支持最近120天内
        request.setStartTime(DateUtils.getLastDay(DateUtils.getNowDate(), 110).getTime());
        // 分页大小 10
        request.setSize(10L);
        cursor = Objects.isNull(cursor) ? 0L : cursor;
        request.setCursor(cursor);
        OapiProcessinstanceListidsResponse  response = null;
        log.info("钉钉【审批实例列表获取】endPoint : {} ", DingTalkApiConstants.PROCESS_INSTANCE_LIST + " 模板标识 :" + processCode);
        OapiProcessinstanceListidsResponse.PageResult result = null;
        Long start = DateUtils.getCurrentTimeMillis();
        do {
            try {
                response = client.execute(request, accessToken);
                Long errcode = response.getErrcode();
                if (Objects.nonNull(errcode) && 0 == errcode.longValue()){
                    // 成功日志 错误码为0
                    result = response.getResult();
                    // recordService.saveDingtalkRecord(start, inteDataId,batchNo,DingTalkApiConstants.PROCESS_INSTANCE_LIST,"获取审批实例列表","1",null," 模板标识 :" + processCode+" 分页游标 :"+cursor);
                }else{
                    // recordService.saveDingtalkRecord(start, inteDataId,batchNo,DingTalkApiConstants.PROCESS_INSTANCE_LIST,"获取审批实例列表","0",response.getErrmsg()," 模板标识 :" + processCode+" 分页游标 :"+cursor);
                }
                // 错误日志 返回null
                return result;
            } catch (Exception e) {
                log.error(e.getMessage());
                if (retryTimes + 1 > maxRetryTimes) {
                    log.warn("钉钉【获取审批实例列表】endPoint :【" + DingTalkApiConstants.PROCESS_INSTANCE_LIST + "】错误重试达到最大次数【{}】", maxRetryTimes);
                    // Todo Api调用失败日志
                    // recordService.saveDingtalkRecord(start, inteDataId,batchNo,DingTalkApiConstants.PROCESS_INSTANCE_LIST,"获取审批实例列表","0",e.getMessage()," 模板标识 :" + processCode+" 分页游标 :"+cursor);
                    throw new ServiceException(DingTalkApiConstants.ApiErrorStr);
                }
            }
        } while (retryTimes++ < maxRetryTimes);

        return result;
    }

    /*
 获取审批流 实例详情
 https://open-dev.dingtalk.com/apiExplorer?spm=ding_open_doc.document.0.0.3206722f3O0nv6#/?devType=org&api=dingtalk.oapi.processinstance.get
 */
    public OapiProcessinstanceGetResponse.ProcessInstanceTopVo getProcessInstanceDetail(String instanceId) {
        int retryTimes = 0;
        String accessToken = getAccessToken();
        DingTalkClient client = new DefaultDingTalkClient(DingTalkApiConstants.PROCESS_INSTANCE_DETAIL );
        OapiProcessinstanceGetRequest request = new OapiProcessinstanceGetRequest();
        request.setProcessInstanceId(instanceId);
        OapiProcessinstanceGetResponse response = null;
        log.info("钉钉【流程实例详情获取】endPoint : {} ", DingTalkApiConstants.PROCESS_INSTANCE_DETAIL + " 实例Id :" + instanceId);
        OapiProcessinstanceGetResponse.ProcessInstanceTopVo result = null;
        Long start = DateUtils.getCurrentTimeMillis();
        do {
            try {
                response = client.execute(request, accessToken);
                Long errcode = response.getErrcode();
                if (Objects.nonNull(errcode) && 0 == errcode.longValue()){
                    // 成功日志 错误码为0
                    result = response.getProcessInstance();
                    // recordService.saveDingtalkRecord(start, inteDataId,batchNo,DingTalkApiConstants.PROCESS_INSTANCE_DETAIL,"获取实例详情","1",null, " 实例Id :" + instanceId);
                }else{
                    // recordService.saveDingtalkRecord(start, inteDataId,batchNo,DingTalkApiConstants.PROCESS_INSTANCE_DETAIL,"获取实例详情","0",response.getErrmsg()," 实例Id :" + instanceId);
                }
                // 错误日志 返回null
                return result;
            } catch (Exception e) {
                log.error(e.getMessage());
                if (retryTimes + 1 > maxRetryTimes) {
                    log.warn("钉钉【获取实例详情】endPoint :【" + DingTalkApiConstants.PROCESS_INSTANCE_LIST + "】错误重试达到最大次数【{}】", maxRetryTimes);
                    // Todo Api调用失败日志
                    // recordService.saveDingtalkRecord(start, inteDataId,batchNo,DingTalkApiConstants.PROCESS_INSTANCE_LIST,"获取实例详情","0",e.getMessage()," 实例Id :" + instanceId);
                    throw new ServiceException(DingTalkApiConstants.ApiErrorStr);
                }
            }
        } while (retryTimes++ < maxRetryTimes);

        return result;
    }


    public OapiRoleListResponse.PageVo getRoleList() {
        int retryTimes = 0;
        String accessToken = getAccessToken();
        DingTalkClient client = new DefaultDingTalkClient(DingTalkApiConstants.ROLE_LIST );
        OapiRoleListRequest request = new OapiRoleListRequest();
        OapiRoleListResponse response = null;
        log.info("钉钉【角色列表获取】endPoint : {} ", DingTalkApiConstants.ROLE_LIST );
        OapiRoleListResponse.PageVo result = null;
        Long start = DateUtils.getCurrentTimeMillis();
        do {
            try {
                response = client.execute(request, accessToken);
                Long errcode = response.getErrcode();
                if (Objects.nonNull(errcode) && 0 == errcode.longValue()){
                    // 成功日志 错误码为0
                    result = response.getResult();
                    // recordService.saveDingtalkRecord(start, inteDataId,batchNo,DingTalkApiConstants.ROLE_LIST,"获取角色列表","1",null, "");
                }else{
                    // recordService.saveDingtalkRecord(start, inteDataId,batchNo,DingTalkApiConstants.ROLE_LIST,"获取角色列表","0",response.getErrmsg(),"");
                }
                // 错误日志 返回null
                return result;
            } catch (Exception e) {
                log.error(e.getMessage());
                if (retryTimes + 1 > maxRetryTimes) {
                    log.warn("钉钉【获取角色列表详情】endPoint :【" + DingTalkApiConstants.ROLE_LIST + "】错误重试达到最大次数【{}】", maxRetryTimes);
                    // Todo Api调用失败日志
                    // recordService.saveDingtalkRecord(start, inteDataId,batchNo,DingTalkApiConstants.ROLE_LIST,"获取实例详情","0",e.getMessage(),"");
                    throw new ServiceException(DingTalkApiConstants.ApiErrorStr);
                }
            }
        } while (retryTimes++ < maxRetryTimes);
        return result;
    }



    public OapiRoleSimplelistResponse.PageVo getRoleUserList(Long roleId) {
        int retryTimes = 0;
        String accessToken = getAccessToken();
        DingTalkClient client = new DefaultDingTalkClient(DingTalkApiConstants.ROLE_USERLIST );
        OapiRoleSimplelistRequest request = new OapiRoleSimplelistRequest();
        request.setRoleId(roleId);
        request.setSize(100L);
        request.setOffset(0L);
        log.info("钉钉【角色下员工列表获取】endPoint : {} ", DingTalkApiConstants.ROLE_USERLIST );
        OapiRoleSimplelistResponse.PageVo result = null;
        Long start = DateUtils.getCurrentTimeMillis();
        do {
            try {
                OapiRoleSimplelistResponse response = client.execute(request, accessToken);
                Long errcode = response.getErrcode();
                if (Objects.nonNull(errcode) && 0 == errcode.longValue()){
                    // 成功日志 错误码为0
                    result = response.getResult();
                    // recordService.saveDingtalkRecord(start, inteDataId,batchNo,DingTalkApiConstants.ROLE_USERLIST,"获取角色下员工列表获取","1",null, "");
                }else{
                    // recordService.saveDingtalkRecord(start, inteDataId,batchNo,DingTalkApiConstants.ROLE_USERLIST,"获取角色下员工列表获取","0",response.getErrmsg(),"");
                }
                // 错误日志 返回null
                return result;
            } catch (Exception e) {
                log.error(e.getMessage());
                if (retryTimes + 1 > maxRetryTimes) {
                    log.warn("钉钉【获取角色列表详情】endPoint :【" + DingTalkApiConstants.ROLE_USERLIST + "】错误重试达到最大次数【{}】", maxRetryTimes);
                    // Todo Api调用失败日志
                    // recordService.saveDingtalkRecord(start, inteDataId,batchNo,DingTalkApiConstants.ROLE_USERLIST,"获取角色下员工列表获取","0",e.getMessage(),"");
                    throw new ServiceException(DingTalkApiConstants.ApiErrorStr);
                }
            }
        } while (retryTimes++ < maxRetryTimes);
        return result;
    }



    public OapiV2UserGetResponse.UserGetResponse getUserDetailInfo(String userId) {
        int retryTimes = 0;
        String accessToken = getAccessToken();
        DingTalkClient client = new DefaultDingTalkClient(DingTalkApiConstants.USER_DETAIL );
        OapiV2UserGetRequest request = new OapiV2UserGetRequest();
        request.setUserid(userId);
        log.info("钉钉【获取用户详情】endPoint : {} ", DingTalkApiConstants.USER_DETAIL );
        OapiV2UserGetResponse.UserGetResponse result = null;
        Long start = DateUtils.getCurrentTimeMillis();
        do {
            try {
                OapiV2UserGetResponse response = client.execute(request, accessToken);
                Long errcode = response.getErrcode();
                if (Objects.nonNull(errcode) && 0 == errcode.longValue()){
                    // 成功日志 错误码为0
                    result = response.getResult();
                    // recordService.saveDingtalkRecord(start, inteDataId,batchNo,DingTalkApiConstants.USER_DETAIL,"获取用户详情","1",null, "");
                }else{
                    // recordService.saveDingtalkRecord(start, inteDataId,batchNo,DingTalkApiConstants.USER_DETAIL,"获取用户详情","0",response.getErrmsg(),"");
                }
                // 错误日志 返回null
                return result;
            } catch (Exception e) {
                log.error(e.getMessage());
                if (retryTimes + 1 > maxRetryTimes) {
                    log.warn("钉钉【获取用户详情】endPoint :【" + DingTalkApiConstants.USER_DETAIL + "】错误重试达到最大次数【{}】", maxRetryTimes);
                    // Todo Api调用失败日志
                    // recordService.saveDingtalkRecord(start, inteDataId,batchNo,DingTalkApiConstants.USER_DETAIL,"获取用户详情","0",e.getMessage(),"");
                    throw new ServiceException(DingTalkApiConstants.ApiErrorStr);
                }
            }
        } while (retryTimes++ < maxRetryTimes);
        return result;
    }

    public String getProcessCode(String processName) {
        int retryTimes = 0;
        String processCode = "";
        DingTalkClient client = new DefaultDingTalkClient(DingTalkApiConstants.PROCESS_NAME);
        OapiProcessGetByNameRequest request = new OapiProcessGetByNameRequest();
        request.setName(processName);
        String accessToken = this.getAccessToken();
        Long start = DateUtils.getCurrentTimeMillis();
        do {
            try {
                OapiProcessGetByNameResponse response = client.execute(request, accessToken);
                Long errcode = response.getErrcode();
                if (Objects.nonNull(errcode) && 0 == errcode.longValue()){
                    processCode = response.getProcessCode();
                    return processCode;
                }else {
                    throw new ServiceException(response.getErrmsg());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                if (retryTimes + 1 > maxRetryTimes) {
                    log.warn("获取审批模板标识【" + DingTalkApiConstants.PROCESS_NAME + "】错误重试达到最大次数【{}】,错误信息 :{}", maxRetryTimes,e.getMessage());
                    throw new ServiceException(DingTalkApiConstants.ApiErrorStr);
                }
            }
        } while (retryTimes++ < maxRetryTimes);
        return processCode;
    }

    // 获取审批流标识
    public String getProcessCodeByName(String processName){
        return getProcessCode(processName);
    }

    // 分页获取所有模板实例
    private void  getNextCursorInstanceList(List<OapiProcessinstanceListidsResponse.PageResult> InstanceListResults, OapiProcessinstanceListidsResponse.PageResult instance, String processCode){
        if (Objects.nonNull(instance)){
            InstanceListResults.add(instance);
            Long nextCursor = instance.getNextCursor();
            if (Objects.nonNull(nextCursor)){
                OapiProcessinstanceListidsResponse.PageResult nextCursorInstance = getProcessInstanceList(processCode, nextCursor);
                getNextCursorInstanceList(InstanceListResults,nextCursorInstance,processCode);
            }
        }
    }
}
