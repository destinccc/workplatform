package com.uuc.system.uuc.sync.util;

import com.uuc.common.core.annotation.CMDBField;
import com.uuc.common.core.utils.DateUtils;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.system.api.domain.SysUser;
import com.uuc.system.uuc.sync.domain.UucFieldDto;
import com.uuc.system.uuc.sync.domain.UucResourceDto;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SyncUtil {

    /**
     * 统一处理新增时根据各类型注解生成标准对象
     * @return
     */
    public static List<UucFieldDto> insertSync(Object obj, UucResourceDto uucResourceDto) throws Exception{
        List<UucFieldDto> uucFieldDtoList=new ArrayList<>();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        if(declaredFields!=null&&declaredFields.length>0){
            for(Field item:declaredFields){
                item.setAccessible(true);
                CMDBField annotation = item.getAnnotation(CMDBField.class);
                if(annotation!=null){
                    String name=annotation.name();
                    Object value=item.get(obj);
                    UucFieldDto uucFieldDto=new UucFieldDto();
                    uucFieldDto.setVModel(name);
                    if(annotation.isDateFormat()==1&&value!=null){
                        uucFieldDto.setValue(DateUtils.parseYYYYMMDD((Date)value));
                    }else{
                        uucFieldDto.setValue(StringUtils.null2Empty(value));
                    }
                    uucFieldDtoList.add(uucFieldDto);
                    if(annotation.isModelCode()==1){
                        uucResourceDto.setModelCode(StringUtils.null2Empty(value));
                    }
                }
            }
        }
            return uucFieldDtoList;
    }
    /**
     * 统一处理编辑时根据新对象和旧对象生成标准对象
     */
    public static List<UucFieldDto> updateSync(Object oldobj,Object newobj)throws Exception{
        List<UucFieldDto> uucFieldDtoList=new ArrayList<>();
        Field[] declaredFields = oldobj.getClass().getDeclaredFields();
        if(declaredFields!=null&&declaredFields.length>0){
            for(Field item:declaredFields){
                item.setAccessible(true);
                CMDBField annotation = item.getAnnotation(CMDBField.class);
                if(annotation!=null){
                    String name=annotation.name();
                    Object oldValue=item.get(oldobj);
                    Object newValue=item.get(newobj);
                    String oldValueString=null;
                    String newValueString=null;
                    if(annotation.isDateFormat()==1){
                       oldValueString=oldValue!=null?DateUtils.parseYYYYMMDD((Date)oldValue):StringUtils.null2Empty(oldValue);
                       newValueString=newValue!=null?DateUtils.parseYYYYMMDD((Date)newValue):StringUtils.null2Empty(newValue);
                    }else{
                       oldValueString=StringUtils.null2Empty(oldValue);
                       newValueString=StringUtils.null2Empty(newValue);
                    }

                    if(!oldValueString.equals(newValueString)){
                        UucFieldDto uucFieldDto=new UucFieldDto();
                        uucFieldDto.setVModel(name);
                        uucFieldDto.setValue(StringUtils.null2Empty(newValueString));
                        uucFieldDtoList.add(uucFieldDto);
                    }
                }
            }
        }
        return uucFieldDtoList;
    }
}
