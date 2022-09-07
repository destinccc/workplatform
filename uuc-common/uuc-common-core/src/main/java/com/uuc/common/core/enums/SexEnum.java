package com.uuc.common.core.enums;

/**
 * 用户状态
 * 
 * @author uuc
 */
public enum SexEnum
{
    MALE("0", "男"), FEMALE("1", "女"), UNKONW("2", "未知");

    private final String code;
    private final String info;

    SexEnum(String code, String info)
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
