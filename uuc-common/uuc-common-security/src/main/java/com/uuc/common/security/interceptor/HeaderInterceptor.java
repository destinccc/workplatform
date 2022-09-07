package com.uuc.common.security.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.context.SecurityContextHolder;
import com.uuc.common.core.utils.ServletUtils;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.security.auth.AuthUtil;
import com.uuc.common.security.utils.SecurityUtils;
import com.uuc.system.api.model.LoginUser;

/**
 * 自定义请求头拦截器，将Header数据封装到线程变量中方便获取
 * 注意：此拦截器会同时验证当前用户有效期自动刷新有效期
 *
 * @author uuc
 */
@Slf4j
public class HeaderInterceptor implements AsyncHandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        SecurityContextHolder.setUserId(ServletUtils.getHeader(request, SecurityConstants.DETAILS_USER_ID));
        SecurityContextHolder.setUserName(ServletUtils.getHeader(request, SecurityConstants.DETAILS_USERNAME));
        SecurityContextHolder.setUserNameZh(ServletUtils.getHeader(request, SecurityConstants.DETAILS_USERNAME_ZH));
        SecurityContextHolder.setUserKey(ServletUtils.getHeader(request, SecurityConstants.USER_KEY));
        SecurityContextHolder.setClientId(ServletUtils.getHeader(request, SecurityConstants.CLIENT_ID));

        String token = SecurityUtils.getToken();
        if (StringUtils.isNotEmpty(token)) {
            LoginUser loginUser = AuthUtil.getLoginUser(token);
            if (StringUtils.isNotNull(loginUser)) {
                loginUser.setToken(token);
                //log.info("URL : {} , Token : {} , LoginUser : {} ", request.getRequestURI(), loginUser.getToken(), JSONUtil.toJsonStr(loginUser));
//                AuthUtil.verifyLoginUserExpire(loginUser);
                SecurityContextHolder.set(SecurityConstants.LOGIN_USER, loginUser);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        SecurityContextHolder.remove();
    }
}
