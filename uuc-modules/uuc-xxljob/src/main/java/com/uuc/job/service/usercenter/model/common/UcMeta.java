package com.uuc.job.service.usercenter.model.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UcMeta implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<UcMetaTrace> trace;

    private UcPagination pagination;
}


