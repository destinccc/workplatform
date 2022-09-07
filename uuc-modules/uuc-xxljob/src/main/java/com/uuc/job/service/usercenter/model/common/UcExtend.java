package com.uuc.job.service.usercenter.model.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 */
@Data
public class UcExtend implements Serializable {

    private static final long serialVersionUID = 1L;

    private String key;

    private String value;

    private String name;


}
