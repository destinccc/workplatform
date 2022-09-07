package com.uuc.system.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.uuc.common.core.annotation.CMDBField;
import com.uuc.common.core.annotation.Excel;
import com.uuc.common.core.web.domain.BaseEntity;
import com.uuc.system.api.domain.SysRole;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;


/**
 * 用户信息对象 uuc_user_info
 *
 * @author uuc
 * @date 2022-04-01
 */
public class UucUserInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @JsonSerialize(using = ToStringSerializer.class)
    @CMDBField(name = "ModelCode",isModelCode = 1)
    private Long id;

    /** 用户编码 */
    @Excel(name = "用户编码")
    private String userCode;

    /** 用户名称 */
    @Excel(name = "用户名称")
    @CMDBField(name = "ModelName")
    private String userName;

    /** 用户来源(00系统用户01同步用户) */
    @Excel(name = "用户来源(00系统用户01同步用户)")
    private String userType;

    /** 用户邮箱 */
    @Excel(name = "用户邮箱")
    @CMDBField(name = "Email")
    private String email;

    /** 手机号码 */
    @Excel(name = "手机号码")
    @CMDBField(name = "Mobile")
    private String phone;

    /** 用户性别(0男1女2未知) */
    @Excel(name = "用户性别(0男1女2未知)")
    private String sex;

    /** 用户工号 */
    @Excel(name = "用户工号")
    @CMDBField(name = "JobNumber")
    private String userJobNumber;

    /** 工作地区 */
    @Excel(name = "工作地区")
    @CMDBField(name = "WorkPlace")
    private String workLocation;

    /** 入职日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入职日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date entryTime;

//    /** 用户状态(0在职1离职) */
//    @Excel(name = "用户状态(0在职1离职)")
//    private String userStatus;

    /** 直属领导编码 */
    @Excel(name = "直属领导编码")
    private String leaderCode;

    /** 直属领导名字 */
    @Excel(name = "直属领导名字")
    private String leaderName;

//    /** 试用期截止日期 */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "试用期截止日期", width = 30, dateFormat = "yyyy-MM-dd")
//    private Date probationEndTime;
//
//    /** 离职日期 */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "离职日期", width = 30, dateFormat = "yyyy-MM-dd")
//    private Date dimissionTime;

    /** 用户状态(0正常1停用) */
    @Excel(name = "用户状态(0正常1停用)")
    private String status;

    /** 删除标识(0正常1删除) */
    private String delFlag;

    /** 组织编码 */
    private String deptCode;

    /** 组织名称 */
    private String deptName;

    /** 角色组 */
    private Long[] roleIds;

    /** 钉钉员工标识 */
    private String extendId;

    /** 组织信息 */
    private List<UucUserDept> deptList;

    /** 用户角色信息 */
    private List<SysRole> roles;

    /** 岗位 */
    @Excel(name = "职位")
    @CMDBField(name = "Title")
    private String postName;

    /** 用户授权类型 */
    @CMDBField(name = "Typeauthorize")
    private String authorizeType;

    /** 入职日期 */
    @CMDBField(name = "HiredDate")
    private String hiredTime;

    /** 住址 */
    @CMDBField(name = "LiveSpace")
    private String liveSpace;

    /** 备注 */
    private String remark;

    /** 身份证号码 */
    @CMDBField(name = "IdentityCard")
    private String identityCard;

    /** 用户中心userId */
    private String centerUid;

    /** 用户账号 */
    private String userLoginAccount;

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }

    public String getExtendId() {
        return extendId;
    }

    public void setExtendId(String extendId) {
        this.extendId = extendId;
    }

    public List<UucUserDept> getDeptList() {
        return deptList;
    }

    public void setDeptList(List<UucUserDept> deptList) {
        this.deptList = deptList;
    }

    /** 账户信息 */
    private List<UucLoginAccount> accountList;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setUserCode(String userCode)
    {
        this.userCode = userCode;
    }

    public boolean isAdmin()
    {
        return isAdmin(this.id);
    }

    public static boolean isAdmin(Long id)
    {
        return id != null && id == 1;
    }

    public String getUserCode()
    {
        return userCode;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
    }
    public void setUserType(String userType)
    {
        this.userType = userType;
    }

    public String getUserType()
    {
        return userType;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPhone()
    {
        return phone;
    }
    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getSex()
    {
        return sex;
    }
    public void setUserJobNumber(String userJobNumber)
    {
        this.userJobNumber = userJobNumber;
    }

    public String getUserJobNumber()
    {
        return userJobNumber;
    }
    public void setWorkLocation(String workLocation)
    {
        this.workLocation = workLocation;
    }

    public String getWorkLocation()
    {
        return workLocation;
    }
    public void setEntryTime(Date entryTime)
    {
        this.entryTime = entryTime;
    }

    public Date getEntryTime()
    {
        return entryTime;
    }
    public void setLeaderCode(String leaderCode)
    {
        this.leaderCode = leaderCode;
    }

    public String getLeaderCode()
    {
        return leaderCode;
    }
    public void setLeaderName(String leaderName)
    {
        this.leaderName = leaderName;
    }

    public String getLeaderName()
    {
        return leaderName;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public List<UucLoginAccount> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<UucLoginAccount> accountList) {
        this.accountList = accountList;
    }

    public Long[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds) {
        this.roleIds = roleIds;
    }

    public String getAuthorizeType() {
        return authorizeType;
    }

    public void setAuthorizeType(String authorizeType) {
        this.authorizeType = authorizeType;
    }

    public String getHiredTime() {
        return hiredTime;
    }

    public void setHiredTime(String hiredTime) {
        this.hiredTime = hiredTime;
    }

    public String getLiveSpace() {
        return liveSpace;
    }

    public void setLiveSpace(String liveSpace) {
        this.liveSpace = liveSpace;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getCenterUid() {
        return centerUid;
    }

    public void setCenterUid(String centerUid) {
        this.centerUid = centerUid;
    }

    public String getUserLoginAccount() {
        return userLoginAccount;
    }

    public void setUserLoginAccount(String userLoginAccount) {
        this.userLoginAccount = userLoginAccount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userCode", getUserCode())
                .append("userName", getUserName())
                .append("userType", getUserType())
                .append("email", getEmail())
                .append("phone", getPhone())
                .append("sex", getSex())
                .append("userJobNumber", getUserJobNumber())
                .append("workLocation", getWorkLocation())
                .append("entryTime", getEntryTime())
//                .append("userStatus", getUserStatus())
                .append("leaderCode", getLeaderCode())
                .append("leaderName", getLeaderName())
//                .append("probationEndTime", getProbationEndTime())
//                .append("dimissionTime", getDimissionTime())
                .append("status", getStatus())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("updateTime", getUpdateTime())
                .append("updateBy", getUpdateBy())
                .append("delFlag", getDelFlag())
                .toString();
    }

}
