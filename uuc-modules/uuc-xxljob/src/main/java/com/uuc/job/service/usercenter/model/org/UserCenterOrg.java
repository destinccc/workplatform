package com.uuc.job.service.usercenter.model.org;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.uuc.job.service.usercenter.model.common.UcApiResult;
import com.uuc.job.service.usercenter.model.common.UcError;
import com.uuc.job.service.usercenter.model.common.UcExtend;
import com.uuc.job.service.usercenter.model.common.UcMeta;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.util.Json;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 用户中心组织信息
 */
@Data
public class UserCenterOrg implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "组织名称")
    private String name;

    @ApiModelProperty(value = "机构代码")
    private String code;

    @ApiModelProperty(value = "类型代码")
    private String type_code;

    @ApiModelProperty(value = "组织类型")
    private String type_name;

    @ApiModelProperty(value = "组织规模")
    private String scale_name;

    @ApiModelProperty(value = "注册地址")
    private String region_name;

    @ApiModelProperty(value = "注册地址代码")
    private String region_code;

    @ApiModelProperty(value = "管辖范围")
    private List<RegionLimit> region_limits;

    @ApiModelProperty(value = "所在地经度")
    private Float longitude;

    @ApiModelProperty(value = "所在地纬度")
    private Float latitude;

    @ApiModelProperty(value = "数据来源")
    private String source_type;

    @ApiModelProperty(value = "职业Id")
    private String profession_code;

    @ApiModelProperty(value = "职业名称")
    private String profession;

    @ApiModelProperty(value = "扩展信息")
    @SerializedName("extends")
    @JSONField(name = "extends")
    private List<UcExtend> extendInfo;


}
