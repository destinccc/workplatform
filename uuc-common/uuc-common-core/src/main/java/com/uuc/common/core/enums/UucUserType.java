package com.uuc.common.core.enums;

/**
 * 用户类型
 * @author uuc
 */
public enum UucUserType
{
    SYSTEM("00", "系统"),
    DINGDING("01", "钉钉"),
    USERCENTER("02", "用户中心");

    private final String code;
    private final String info;

    UucUserType(String code, String info)
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
