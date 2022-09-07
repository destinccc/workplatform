package com.uuc.common.core.enums;

public enum AdminFlag {
    IS("1", "是"), NO("0", "否");

    private String code;

    private String value;

    AdminFlag(String code, String value){
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
