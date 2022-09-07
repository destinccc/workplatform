package com.uuc.system.api.model.cmdb;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ModelDetailInfoDto  implements Serializable {

    private Long entityId;
    private String entityForm;
    private String entityName;
    List<ModelRelDetailInfoDto> entityRelationList;
//    List<ModelRelDetailInfoDto> target;
//    List<ModelRelDetailInfoDto> source;


}