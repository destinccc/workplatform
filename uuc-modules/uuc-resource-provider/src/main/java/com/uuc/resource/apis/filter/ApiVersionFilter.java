package com.uuc.resource.apis.filter;

import com.google.common.collect.Lists;
import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.domain.R;
import com.uuc.common.core.exception.ServiceException;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.system.api.RemoteSystemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Component
@Slf4j
public class ApiVersionFilter implements Filter {
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Autowired
    private RemoteSystemService systemService;

    // 白名单
    private static List<String> whiteUrlList = Lists.newArrayList("/actuator/health");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String url = request.getRequestURI();
        R<List<String>> effectiveList = systemService.getEffectiveList(SecurityConstants.INNER);
        List<String> effectiveApiList = effectiveList.getData();
        if (!isValidUrl(url, effectiveApiList)) {
            log.warn("URL : {} 已禁止访问! , 有效的API版本: {} ", url, effectiveApiList);
            resolver.resolveException(request, response, null, new ServiceException("URL: " + StringUtils.getSlfStr(url) + " 该版本API已禁止访问,请联系管理员!"));
            return;
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private static boolean isValidUrl(String url, List<String> effectiveApiList) {
        if (whiteUrlList.contains(url)){
            return true;
        }
        if (CollectionUtils.isEmpty(effectiveApiList)) {
            return false;
        }
        String[] content = StringUtils.splitByWholeSeparatorPreserveAllTokens(url, "/");
        for (String s : content) {
            if (effectiveApiList.contains(s)){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        List<String> apiVersionList = Lists.newArrayList();
        apiVersionList.add("v1.2");
        apiVersionList.add("v1.0");
        apiVersionList.add("v1.3");
        apiVersionList.add("v1");
        String url  = "/inner/v1.3/perms/userProjects";
        System.out.println(isValidUrl(url,apiVersionList));
    }

}
