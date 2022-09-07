package com.uuc.system.api.uuc.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CmdbDeptDto implements Serializable {

    private String deptCode;

    private String deptName;

    private String parentCode;

    private String ancestors;

    private String deptWholeName;
}
