package com.uuc.common.core.enums;

/**
 * 组织类型
 * @author uuc
 */
public enum UucDeptType
{
    SYSTEM("00", "系统"),
    DINGDING("01", "钉钉"),
    USERCENTER("02", "用户中心");

    private final String code;
    private final String info;

    UucDeptType(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
