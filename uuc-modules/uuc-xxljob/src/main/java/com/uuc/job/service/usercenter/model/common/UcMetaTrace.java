package com.uuc.job.service.usercenter.model.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class UcMetaTrace implements Serializable {

    private String trace_id;

    private String parent_id;

    private Object span_id;

    private String request_id;

}
