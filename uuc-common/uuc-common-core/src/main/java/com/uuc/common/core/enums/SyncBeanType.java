package com.uuc.common.core.enums;

public enum SyncBeanType {
    USERSYNCBEAN("userSyncService", "人员处理bean"), DEPTSYNCBEAN("organizationSyncService", "组织处理bean"), PROJECTSYNCBEAN("projectSyncService", "项目处理bean"),MAINTAIN("Maintain","维护"),USE("Use","使用");

    private String code;

    private String value;

    SyncBeanType(String code, String value){
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
