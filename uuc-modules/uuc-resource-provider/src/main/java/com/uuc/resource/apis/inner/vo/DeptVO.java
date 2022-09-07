package com.uuc.resource.apis.inner.vo;

import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: fxm
 * @date: 2022-06-22
 * @description:
 **/
@Data
public class DeptVO implements Serializable {
    /**
     * 用户编码
     */
    private String userCode;
    /**
     * 组织id列表
     */
    private List<String> deptCodes;

    /**
     * 资源类型列表
     */
    private List<String> resTypes = Lists.newArrayList();
}
