package com.uuc.gateway.filter;

import cn.hutool.json.JSONUtil;
import com.uuc.common.core.constant.CacheConstants;
import com.uuc.common.core.constant.HttpStatus;
import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.constant.TokenConstants;
import com.uuc.common.core.utils.DateUtils;
import com.uuc.common.core.utils.JwtUtils;
import com.uuc.common.core.utils.ServletUtils;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.core.utils.ip.IpUtils;
import com.uuc.common.redis.service.RedisService;
import com.uuc.gateway.config.properties.IgnoreWhiteProperties;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

/**
 * 网关鉴权
 *
 * @author uuc
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered
{
    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    // 排除过滤的 uri 地址，nacos自行添加
    @Autowired
    private IgnoreWhiteProperties ignoreWhite;

    @Autowired
    private RedisService redisService;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();

        String url = request.getURI().getPath();
        String clientId = request.getHeaders().getFirst("clientId");
        log.info("当前请求IP：{}, clientId:{}, url:{}", IpUtils.getIpAddr(request), clientId, url);

        // 跳过不需要验证的路径
        try{
            if (StringUtils.matches(url, ignoreWhite.getWhites()))
            {
                return chain.filter(exchange);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        //增加服务间调用校验逻辑
        String serverToken=getServerToken(request);
        /*if(StringUtils.isNotEmpty(serverToken)){
            log.info("进入服务间调用鉴权");
            boolean b = checkServerAuth(clientId, serverToken);
            if(b){
                addHeader(mutate, SecurityConstants.FROM_SOURCE, SecurityConstants.INNER);
                return chain.filter(exchange.mutate().request(mutate.build()).build());
            }else{
                log.info("服务间调用鉴权失败");
                return unauthorizedResponse(exchange, "服务间调用鉴权失败");
            }
        }*/
        if(StringUtils.isNotEmpty(serverToken)){
            log.info("进入服务间调用鉴权");
            boolean flag = checkTokenValid(serverToken);
            log.info("传入的serverToken为:{},校验结果为：{}",serverToken,flag);
            if(!flag){
                log.info("服务间调用鉴权失败");
                return unauthorizedResponse(exchange, "服务间调用鉴权失败");
            }
            addHeader(mutate, SecurityConstants.FROM_SOURCE, SecurityConstants.INNER);
            return chain.filter(exchange.mutate().request(mutate.build()).build());
        }
        else {
            log.info("进入用户token校验");
            String token = getToken(request);
//        System.out.println("Filter白名单: "+ JSONUtil.toJsonStr(ignoreWhite.getWhites()));
            if (StringUtils.isEmpty(token))
            {
                return unauthorizedResponse(exchange, "令牌不能为空");
            }
            Claims claims = null;
            try {
                claims = JwtUtils.parseToken(token);
            } catch (Exception e) {
                e.printStackTrace();
                return unauthorizedResponse(exchange, "令牌验证失败");
            }
            if (claims == null)
            {
                return unauthorizedResponse(exchange, "令牌已过期或验证不正确");
            }
            String userkey = JwtUtils.getUserKey(claims);
            boolean islogin = redisService.hasKey(getTokenKey(token));
            if (!islogin)
            {
                return unauthorizedResponse(exchange, "登录状态已过期");
            }
            String userid = JwtUtils.getUserId(claims);
            String username = JwtUtils.getUserName(claims);
            String usernameZh = JwtUtils.getUserNameZh(claims);
            if (StringUtils.isEmpty(userid) || StringUtils.isEmpty(username))
            {
                return unauthorizedResponse(exchange, "令牌解析失败");
            }
            // 设置用户信息到请求
            addHeader(mutate, SecurityConstants.USER_KEY, userkey);
            addHeader(mutate, SecurityConstants.DETAILS_USER_ID, userid);
            addHeader(mutate, SecurityConstants.DETAILS_USERNAME, username);
            addHeader(mutate, SecurityConstants.DETAILS_USERNAME_ZH, usernameZh);
            addHeader(mutate, SecurityConstants.CLIENT_ID, clientId);
            // 内部请求来源参数清除
            removeHeader(mutate, SecurityConstants.FROM_SOURCE);
            return chain.filter(exchange.mutate().request(mutate.build()).build());
        }

    }

    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value)
    {
        if (value == null)
        {
            return;
        }
        String valueStr = value.toString();
        String valueEncode = ServletUtils.urlEncode(valueStr);
        mutate.header(name, valueEncode);
    }

    private void removeHeader(ServerHttpRequest.Builder mutate, String name)
    {
        mutate.headers(httpHeaders -> httpHeaders.remove(name)).build();
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String msg)
    {
        log.error("[鉴权异常处理]请求路径:{}", exchange.getRequest().getPath());
        return ServletUtils.webFluxResponseWriter(exchange.getResponse(), msg, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 获取缓存key
     */
    private String getTokenKey(String token)
    {
        return CacheConstants.LOGIN_TOKEN_KEY + token;
    }
    /**
     * 获取缓存key
     */
    private String getServerTokenKey(String token)
    {
        return CacheConstants.SERVER_TOKEN + token;
    }

    /**
     * 获取请求token
     */
    private String getToken(ServerHttpRequest request)
    {
        String token = request.getHeaders().getFirst(TokenConstants.AUTHENTICATION);
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX))
        {
            token = token.replaceFirst(TokenConstants.PREFIX, StringUtils.EMPTY);
        }
        return token;
    }
    /**
     * 获取服务间调用请求token
     */
    private String getServerToken(ServerHttpRequest request)
    {
        String token = request.getHeaders().getFirst(TokenConstants.SERVER_AUTHENTICATION);
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX))
        {
            token = token.replaceFirst(TokenConstants.PREFIX, StringUtils.EMPTY);
        }
        return token;
    }

    /**
     * 校验服务间调用权限
     * @return
     */
    private boolean checkServerAuth(String clientId,String serverToken){
        String jwtClientId="";
        String jwtserverMark="";
        try {
            Claims  claims = JwtUtils.parseToken(serverToken);
            jwtClientId=JwtUtils.getClientId(claims);
            jwtserverMark=JwtUtils.getSeverMark(claims);
            log.info("解析的clientId:{},serverMark:{}",jwtClientId,jwtserverMark);
        } catch (Exception e) {
            log.error("jwt解析失败");
            e.printStackTrace();
            return false;
        }
        if(StringUtils.isEmpty(clientId)||!(clientId.equals(jwtClientId))){
            log.error("客户端匹配失败");
            return false;
        }
        if(!jwtserverMark.equals(SecurityConstants.SERVER_MARK_KEY)){
            log.error("服务间调用标识匹配失败");
            return false;
        }
        boolean isFlag = redisService.hasKey(getServerTokenKey(serverToken));
        if(!isFlag){
            log.error("查询serverToken");
            return false;
        }
        log.info("校验通过，进入到后续接口调用");
        return true;
    }

    /**
     * 校验token是否有效
     * @return
     */
    public boolean checkTokenValid(String token){
        Boolean hasKey = redisService.hasKey(CacheConstants.CLIENT_TOKEN + token);
        return hasKey;
    }
    @Override
    public int getOrder()
    {
        return -200;
    }
}
