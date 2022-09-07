//package com.uuc.auth.filter;
//
//import com.uuc.auth.config.properties.IgnoreWhiteProperties;
//import com.uuc.common.core.constant.TokenConstants;
//import com.uuc.common.core.utils.StringUtils;
//import com.uuc.common.redis.service.RedisService;
//import com.uuc.common.security.service.TokenService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.Ordered;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
//
///**
// * 网关鉴权
// *
// * @author uuc
// */
//@Component
//public class AuthFilter implements Filter, Ordered
//{
//    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);
//
//    // 排除过滤的 uri 地址，nacos自行添加
//    @Autowired
//    private IgnoreWhiteProperties ignoreWhite;
//
//    @Autowired
//    private RedisService redisService;
//
//    @Autowired
//    private TokenService tokenService;
//
//
//    @Override
//    public int getOrder()
//    {
//        return -200;
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        Filter.super.init(filterConfig);
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest request=(HttpServletRequest)servletRequest;
//        String url=request.getRequestURI();
//        // 跳过不需要验证的路径
//        if (StringUtils.matches(url, ignoreWhite.getWhites()))
//        {
//            filterChain.doFilter(servletRequest,servletResponse);
//            return;
//        }
//        String token = getToken(request);
//        Object cacheObject = tokenService.getLoginUserByToken(token);
//        if(token==null||cacheObject==null){
//            return;
//        }else {
//            filterChain.doFilter(servletRequest,servletResponse);
//        }
//    }
//
//    @Override
//    public void destroy() {
//        Filter.super.destroy();
//    }
//
//    /**
//     * 获取请求token
//     */
//    private String getToken(HttpServletRequest request)
//    {
//        String token = request.getHeader(TokenConstants.AUTHENTICATION);
//        // 如果前端设置了令牌前缀，则裁剪掉前缀
//        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX))
//        {
//            token = token.replaceFirst(TokenConstants.PREFIX, StringUtils.EMPTY);
//        }
//        return token;
//    }
//}