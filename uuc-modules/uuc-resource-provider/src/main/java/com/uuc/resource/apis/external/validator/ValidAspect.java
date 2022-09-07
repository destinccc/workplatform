package com.uuc.resource.apis.external.validator;

import com.uuc.common.core.constant.HttpStatus;
import com.uuc.common.core.exception.ServiceException;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.resource.apis.external.service.DeptResourceService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @author deng
 * @date 2022/7/20 0020
 * @description
 */
@Aspect
@Order(2)
@Component
public class ValidAspect {
    @Pointcut("@annotation(com.uuc.resource.apis.external.validator.RequestValid)")
    private void pointcut(){}
    @Autowired
    private DeptResourceService deptResourceService;
    //环绕通知
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        Object[] args = joinPoint.getArgs();
        System.out.println("进入切面处理逻辑=============================");
        if(args!=null&&args.length>0){
            for(Object obj:args){
                System.out.println("参数对象:"+obj.toString());
                Class<?> objClass = obj.getClass();
                Field[] declaredFields = objClass.getDeclaredFields();
                String deptCode=null;
                String projectCode=null;
                for(Field field:declaredFields){
                    if("deptCode".equals(field.getName())){
                        field.setAccessible(true);
                        //修改前的值
                        Object o = field.get(obj);
                        if(o==null){
                            deptCode=deptResourceService.getDeptCode();
                            if(StringUtils.isNotEmpty(deptCode)){
                                field.set(obj,deptCode);
                            }
                        }else {
                            deptCode=String.valueOf(o);
                        }
                    }
                    if("projectCode".equals(field.getName())){
                        field.setAccessible(true);
                        //修改前的值
                        Object projectObj = field.get(obj);
                        if(projectObj!=null){
                            projectCode=String.valueOf(projectObj);
                        }
                    }


                }
                boolean checkFlag = deptResourceService.checkProjectCode(deptCode, projectCode);
                if(!checkFlag){
                    throw new ServiceException("参数异常", HttpStatus.BAD_REQUEST);
                }

            }

        }
        return joinPoint.proceed(args);
    }
}
