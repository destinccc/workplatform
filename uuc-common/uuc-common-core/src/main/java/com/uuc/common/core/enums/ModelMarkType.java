package com.uuc.common.core.enums;

import lombok.Data;

public enum ModelMarkType {
    Bind_Employee("Employee", "人员"), Bind_Organization("Organization", "组织"), Bind_Project("Project", "项目");

    private String code;

    private String value;

    ModelMarkType(String code,String value){
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
