package com.uuc.common.core.utils;

import cn.hutool.core.codec.Base64;
import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.constant.TokenConstants;
import com.uuc.common.core.text.Convert;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Jwt工具类
 *
 * @author uuc
 */
public class JwtUtils
{
    public static String secret = TokenConstants.SECRET;

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    public static String createToken(Map<String, Object> claims)
    {
        String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    public static Claims parseToken(String token)
    {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * 根据令牌获取用户标识
     *
     * @param token 令牌
     * @return 用户ID
     */
    public static String getUserKey(String token)
    {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.USER_KEY);
    }

    /**
     * 根据令牌获取用户标识
     *
     * @param claims 身份信息
     * @return 用户ID
     */
    public static String getUserKey(Claims claims)
    {
        return getValue(claims, SecurityConstants.USER_KEY);
    }

    /**
     * 根据令牌获取用户ID
     *
     * @param token 令牌
     * @return 用户ID
     */
    public static String getUserId(String token)
    {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.DETAILS_USER_ID);
    }

    /**
     * 根据身份信息获取用户ID
     *
     * @param claims 身份信息
     * @return 用户ID
     */
    public static String getUserId(Claims claims)
    {
        return getValue(claims, SecurityConstants.DETAILS_USER_ID);
    }

    /**
     * 根据令牌获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public static String getUserName(String token)
    {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.DETAILS_USERNAME);
    }

    /**
     * 根据身份信息获取用户名
     *
     * @param claims 身份信息
     * @return 用户名
     */
    public static String getUserName(Claims claims)
    {
        return getValue(claims, SecurityConstants.DETAILS_USERNAME);
    }

    public static String getUserNameZh(Claims claims)
    {
        return getValue(claims, SecurityConstants.DETAILS_USERNAME_ZH);
    }
    /**
     * 根据身份信息获取键值
     *
     * @param claims 身份信息
     * @param key 键
     * @return 值
     */
    public static String getValue(Claims claims, String key)
    {
        return Convert.toStr(claims.get(key), "");
    }

    /**
     * 服务间调用获取clientId
     */
    public static String getClientId(Claims claims)
    {
        return getValue(claims, SecurityConstants.CLIENT_ID);
    }
    /**
     * 服务间调用获取调用标识
     */
    public static String getSeverMark(Claims claims)
    {
        return getValue(claims, SecurityConstants.SERVER_MARK);
    }

    public static void main(String[] args) {
        String str = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2luZm8iOnsiVXNlcklkIjoxNDc0MjkwNzk4OTM2NzIzNDU2LCJOYW1lIjoi5Lit55S15rWL6K-V6LSm5Y-3In0sImF1ZCI6IuS4reeUtea1i-ivlei0puWPtyIsImV4cCI6MTY1OTQzNjIyNSwianRpIjoidWN3ZWItMTU1NDM4NDEwNzk4OTkwMTMxMiIsImlhdCI6MTY1OTQyOTAyNSwiaXNzIjoidXVjIiwibmJmIjoxNjU5NDI4OTA1LCJzdWIiOiJzc29fbG9naW4ifQ.xbszkNZqqBSJ7LXTKO8o8SgIwDiAvltzW4GoPI_NatXtnlZ2WZeOyAfn-romKlrI_8mh4H5pwq-dWK9v8T90uOpMh31RYqhXOvfs0Bs92hgAqxLPb-X0BZYQuKdEsMm2dtyew5XOb7BbCgUadbrHTGViRj7a0uBdNej6yFfKVF8EZe479ARbKx74RC1DorKkmecgI4KYhrRRAfTz41dapAZIIfFl73hUGMwWKncrwvzuPQR2bX_ybUm9zfHDx0iExJucO8FA07hSpHHi4O9KFsGNfDcbHtlunRxEOViSK4NY0FHYhElrba2Qh7vNcCCnVzQy6Lbsj3QNu3iRNO2U9A";
        String s = str.split("\\.")[1];
        System.out.println(new String(Base64.decode(s.getBytes(StandardCharsets.UTF_8))));
    }
}
