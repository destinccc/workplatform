package com.uuc.common.core.constant;

/**
 * Token的Key常量
 * 
 * @author uuc
 */
public class TokenConstants
{
    /**
     * 令牌自定义标识
     */
    public static final String AUTHENTICATION = "Authorization";

    /**
     * 令牌前缀
     */
    public static final String PREFIX = "Bearer ";

    /**
     * 令牌秘钥
     */
    public final static String SECRET = "abcdefghijklmnopqrstuvwxyz";


    public final static String SESSIONKEY="SESSION_TOKEN";

    public final static String CLIENTID="clientId";

    /**
     * 服务间调用令牌标识
     */
    public static final String SERVER_AUTHENTICATION = "X-Authorization";

}
