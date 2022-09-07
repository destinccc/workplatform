package com.uuc.system.uuc.sync.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class SyncBody implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object oldObject;

    private Object newObject;

    private String beanName;

    private String operationType;

}
