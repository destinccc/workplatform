package com.uuc.auth.utils;

import com.uuc.auth.api.enums.GrantTypeEnum;

/**
 * @author deng
 * @date 2022/7/14 0014
 * @description
 */
public class GrantTypeUtil {

    public static boolean checkType(String grantType){
        for(GrantTypeEnum item:GrantTypeEnum.values()){
            String value = item.getValue();
            if(grantType.equals(value)){
                return true;
            }
        }
        return false;
    }
}
