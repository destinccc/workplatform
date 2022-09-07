package com.uuc.job.dingtalk.constant;

/**
 * 钉钉接口Api
 */
public class DingTalkApiConstants {

    public static final String ApiErrorStr = "Sync Api Get Error";


    public static final String UTF8 = "UTF-8";

    // 授权token
    public static final String  GET_TOKEN = "https://oapi.dingtalk.com/gettoken";

    // 用户信息 https://developers.dingtalk.com/document/app/query-user-details
    public static final String  GET_USER_INFO = "https://oapi.dingtalk.com/user/getuserinfo";

    // 根据上级id查询部门列表 https://developers.dingtalk.com/document/app/obtain-the-department-list-v2
    public static final String  GET_DEP_LIST_V2 = "https://oapi.dingtalk.com/topapi/v2/department/listsub";

    // 权限范围
    public static final String  AUTH_SCOPE = "https://oapi.dingtalk.com/auth/scopes";

    // 部门人员list  https://developers.dingtalk.com/document/app/query-the-list-of-department-userids
    public static final String  DEP_USERID_LIST = "https://oapi.dingtalk.com/topapi/v2/user/list";

    // 用户详情
    public static final String  USER_DETAIL = "https://oapi.dingtalk.com/topapi/v2/user/get";

    // 部门详情
    public static final String  DEP_DETAILS = "https://oapi.dingtalk.com/topapi/v2/department/get";

    // 审批流模板名称
    public static final String  PROCESS_NAME = "https://oapi.dingtalk.com/topapi/process/get_by_name";

    // 审批流实例列表
    public static final String  PROCESS_INSTANCE_LIST = "https://oapi.dingtalk.com/topapi/processinstance/listids";

    // 审批流实例详情
    public static final String  PROCESS_INSTANCE_DETAIL = "https://oapi.dingtalk.com/topapi/processinstance/get";

    // 角色列表
    public static final String  ROLE_LIST = "https://oapi.dingtalk.com/topapi/role/list";

    // 指定角色的员工列表
    public static final String  ROLE_USERLIST = "https://oapi.dingtalk.com/topapi/role/simplelist";



}
