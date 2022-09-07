package com.uuc.common.core.enums;

import java.util.HashSet;
import java.util.Set;

public enum ModelOperationType {
    INSERT("insert", "新增"), UPDATE("update", "修改"), DELETE("delete", "删除");
    public static final Set<String> codeSet=new HashSet<>();
    static {
        codeSet.add(ModelOperationType.INSERT.code);
        codeSet.add(ModelOperationType.UPDATE.code);
        codeSet.add(ModelOperationType.DELETE.code);
    }
    private String code;

    private String value;

    ModelOperationType(String code, String value){
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
