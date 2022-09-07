package com.uuc.system.api.domain;

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
public class ResourceVO implements Serializable {
    /**
     * 用户编码
     */
    private String userCode;
    /**
     * 资源类型（对应cmdb元模型标识）
     */
    private List<String> resTypes = new ArrayList<>();
}
