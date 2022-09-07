package com.uuc.job.service.usercenter.model.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class UcError implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer error_code;

}
