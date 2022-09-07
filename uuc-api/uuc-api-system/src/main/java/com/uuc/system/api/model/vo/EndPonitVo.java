package com.uuc.system.api.model.vo;

import com.uuc.system.api.model.cmdb.ResourceInfoDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @description:
 * @author: Destin  监控需要的
 */
@Data
public class EndPonitVo extends ResourceInfoDto implements Serializable {

    // 资源
    private Map<String,Object> resourceMap;
}
