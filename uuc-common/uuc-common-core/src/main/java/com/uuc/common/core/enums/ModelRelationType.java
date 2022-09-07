package com.uuc.common.core.enums;

public enum ModelRelationType {
    JOIN("Join", "参与"), MANAGE("Manage", "管理"), CONTAINS("Contains", "包含"),MAINTAIN("Maintain","维护"),USE("Use","使用");

    private String code;

    private String value;

    ModelRelationType(String code, String value){
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
