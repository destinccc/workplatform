package com.uuc.job.service.usercenter.model.user;

import com.uuc.job.service.usercenter.model.common.UcError;
import com.uuc.job.service.usercenter.model.common.UcMeta;
import com.uuc.job.service.usercenter.model.org.UserCenterOrg;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @description: 用户中心用户
 */
@Data
public class UserCenterUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private Long uid;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "登录账号")
    private String username;

    @ApiModelProperty(value = "身份证号")
    private String id_card;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "用户身份")
    private String profession;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "执业证号")
    private String practice_number;

    @ApiModelProperty(value = "组织编码")
    private List<UserCenterOrg> org;

    @ApiModelProperty(value = "注册时间")
    private String reg_time;

    @ApiModelProperty(value = "数据来源")
    private String source;

    @ApiModelProperty(value = "系统是否创建")
    private String is_sys_create;

    @ApiModelProperty(value = "错误信息")
    private UcError error;

    @ApiModelProperty(value = "Meta")
    private UcMeta meta;

    @ApiModelProperty(value = "email")
    private String email;

}
