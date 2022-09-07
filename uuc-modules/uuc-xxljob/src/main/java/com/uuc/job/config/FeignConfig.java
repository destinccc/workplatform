//package com.ruoyi.job.config;
//
//import com.alibaba.nacos.shaded.com.google.common.net.HttpHeaders;
//import feign.RequestInterceptor;
//import feign.RequestTemplate;
//import feign.Retryer;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.RequestContextListener;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Objects;
//
///**
// * @description:
// * @author: Destin
// * @create: 2022-04-13 09:14
// */
//@Component
//@Slf4j
//public class FeignConfig implements RequestInterceptor {
//    public FeignConfig() {
//    }
//
//    @Bean
//    public Retryer feignRetryer() {
//        return new Retryer.Default(100, 1000, 5);
//    }
//
//    @Bean
//    public RequestContextListener requestContextListenerBean() {
//        return new RequestContextListener();
//    }
//
//    @Override
//    public void apply(RequestTemplate requestTemplate) {
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        if (Objects.nonNull(requestAttributes)) {
//            RequestContextHolder.setRequestAttributes(requestAttributes, true);
//            try {
//                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//                HttpServletRequest request = attributes.getRequest();
//                requestTemplate.header(HttpHeaders.AUTHORIZATION, request.getHeader(HttpHeaders.AUTHORIZATION));
//            } catch (Exception e) {
//                log.info("定时任务介入，授权异常");
//            }
//
//        } else {
//            RequestContextHolder.setRequestAttributes(new NonWebRequestAttributes(), Boolean.TRUE);
//            HttpServletRequest httpRequest = this.getHttpServletRequestSafely();
//            if (null != httpRequest && null != httpRequest.getAttribute("X-Request-No")) {
//                requestTemplate.header("X-Request-No", httpRequest.getAttribute("X-Request-No").toString());
//            }
//        }
//
//    }
//
//    public HttpServletRequest getHttpServletRequestSafely() {
//        try {
//            RequestAttributes requestAttributesSafely = this.getRequestAttributesSafely();
//            return requestAttributesSafely instanceof NonWebRequestAttributes ? null : ((ServletRequestAttributes) requestAttributesSafely).getRequest();
//        } catch (Exception var2) {
//            return null;
//        }
//    }
//
//    public RequestAttributes getRequestAttributesSafely() {
//        RequestAttributes requestAttributes = null;
//        try {
//            requestAttributes = RequestContextHolder.currentRequestAttributes();
//        } catch (IllegalStateException var3) {
//            requestAttributes = new NonWebRequestAttributes();
//        }
//
//        return requestAttributes;
//    }
//
//}
//
