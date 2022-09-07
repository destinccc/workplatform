package com.uuc.resource.apis.inner.vo;

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
public class ProjectVO implements Serializable {
    /**
     * 用户编码
     */
    private String userCode;
    /**
     * 项目id列表
     */
    private List<String> projectCodes;

    /**
     * 资源类型列表
     */
    private List<String> resTypes = new ArrayList<>();
}
