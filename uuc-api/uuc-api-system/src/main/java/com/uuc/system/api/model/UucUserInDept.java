package com.uuc.system.api.model;

import lombok.Data;

import java.util.List;

/**
 * @author: fxm
 * @date: 2022-05-23
 * @description:
 **/
@Data
public class UucUserInDept {
    private String deptCode;
    private List<String> userCodeList;
}
