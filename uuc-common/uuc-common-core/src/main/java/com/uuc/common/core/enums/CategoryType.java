package com.uuc.common.core.enums;

public enum CategoryType {
    TEXT("0", "文字类型"), IMAGE("1", "图文类型");

    private final String code;
    private final String info;

    CategoryType(String code, String info)
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
