package com.uuc.system.uuc.sync.test;

import com.uuc.common.core.annotation.CMDBField;
import com.uuc.system.api.model.UucUserInfo;

import java.lang.reflect.Field;

public class CmdbAnnationTest {
    public static void main(String[] args) throws Exception {
        UucUserInfo.class.getAnnotation(CMDBField.class);
        UucUserInfo uucUserInfo = new UucUserInfo();
        uucUserInfo.setId(1L);
        test(uucUserInfo);
        System.out.println(System.currentTimeMillis());
    }

    public static void test(Object obj) throws Exception{
        System.out.println(obj.getClass());
        CMDBField annotation = obj.getClass().getAnnotation(CMDBField.class);
        //System.out.println(annotation.name());
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for(Field item:declaredFields){
            item.setAccessible(true);
            String name =item.getName();
            if(name.equals("id")){
                Object o = item.get(obj);
                System.out.println(o.toString());
            }
        }
    }
}
