package com.uuc.common.core.constant;

/**
 * 缓存的key 常量
 * 
 * @author uuc
 */
public class CacheConstants
{
    /**
     * 缓存有效期，默认2小时
     */
    public final static long EXPIRATION = 2;
    /**
     * 缓存有效期，默认6小时
     */
    public final static long REFRESH_EXPIRATION = 6;

    /**
     * 缓存刷新时间，默认120（分钟）
     */
    public final static long REFRESH_TIME = 120;

    /**
     * 权限缓存前缀
     */
    public final static String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * token与code对应关系
     */
    public final static String TOKEN_CODE_KEY="token_code:";

    /**
     * code与token的对应关系
     */
    public final static String CODE_TOKEN_KEY="code_token:";

    /**
     * refreshToken
     */
    public final static String REFRESH_TOKEN="refresh_token:";

    /**
     * checkToken
     */
    public final static String TOKEN_REFRESH="token_refresh:";
    /**
     * refreshTokenContent
     */
    public final static String REFRESH_TOKEN_CONTENT="refresh_token_content:";

    /**
     * 服务间的tokenkey
     */
    public final static String SERVER_TOKEN="server_token:";


    /**
     * 数据权限全局namespace
     */
    public final static String CACHE_NAMESPACE_RESOURCE_PERMS = "UMP:PERMS:";

    /**
     * 用户资源权限prefix
     */
    public final static String USER_RESOURCE_PERMS = "PERMS_USER_RESOURCE:";
    /**
     * 人员组织资源权限prefix
     */
    public final static String DEPT_RESOURCE_PERMS = "PERMS_DEPT_RESOURCE:";
    /**
     * 人员项目资源权限prefix
     */
    public final static String USER_PROJECT_RESOURCE_PERMS = "PERMS_USER_PROJECT_RESOURCE:";

    /**
     * 人员关联组织权限prefix
     */
    public final static String USER_DEPT_PERMS = "PERMS_USER_DEPT:";
    /**
     * 人员关联项目prefix
     */
    public final static String USER_PROJECT_PERMS = "PERMS_USER_PROJECT:";
    /**
     * 组织关联项目权限prefix
     */
    public final static String DEPT_PROJECT_PERMS = "PERMS_DEPT_PROJECT:";
    /**
     * 所有项目资源权限prefix
     */
    public final static String PROJECT_ALL_PERMS = "PERMS_ALL_PROJECT:";

    /**
     * 资源全量同步标识
     */
    public final static String ISRUNNINGFLAG = "IS_RUNNING_FLAG:";

    /**
     * 外部token标识
     */
    public final static String CLIENT_TOKEN="ump:client_token:";
    /**
     * RefreshToken
     */
    public final static String CLIENT_REFRESH_TOKEN="ump:client_refresh_token:";
}
