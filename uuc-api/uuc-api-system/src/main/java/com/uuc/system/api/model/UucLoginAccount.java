package com.uuc.system.api.model;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.uuc.common.core.annotation.Excel;
import com.uuc.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 登录用户对象 uuc_login_account
 *
 * @author uuc
 * @date 2022-04-01
 */
public class UucLoginAccount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 用户编码 */
    @Excel(name = "用户编码")
    private String userCode;

    /** 账号类型(00系统用户01同步用户) */
    @Excel(name = "账号类型(00系统用户01同步用户)")
    private String accountType;

    /** 登录账户 */
    @Excel(name = "登录账户")
    private String loginAcct;

    /** 登录密码 */
    @Excel(name = "登录密码")
    private String loginPwd;

    /** 用户头像 */
    @Excel(name = "用户头像")
    private String avatar;

    /** 账户过期状态Y/N */
    @Excel(name = "账户过期状态Y/N")
    private String accountExpiredStatus;

    /** 密码过期状态Y/N */
    @Excel(name = "密码过期状态Y/N")
    private String passwdExpiredStatus;

    /** 账户是否锁定Y/N */
    @Excel(name = "账户是否锁定Y/N")
    private String accountLockedStatus;

    /** 是否激活Y/N */
    @Excel(name = "是否激活Y/N")
    private String active;

    /** 是否有效Y/N */
    @Excel(name = "是否有效Y/N")
    private String enabled;

    /** 密码有效期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "密码有效期时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date pwdValidPeriodDate;

    /** 账号有效期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "账号有效期时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date acctValidPeriodDate;

    /** 删除标志(0正常1删除) */
    private String delFlag;

    /** 帐号状态（0正常 1停用） */
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getUserCode()
    {
        return userCode;
    }
    public void setAccountType(String accountType)
    {
        this.accountType = accountType;
    }

    public String getAccountType()
    {
        return accountType;
    }
    public void setLoginAcct(String loginAcct)
    {
        this.loginAcct = loginAcct;
    }

    public String getLoginAcct()
    {
        return loginAcct;
    }
    public void setLoginPwd(String loginPwd)
    {
        this.loginPwd = loginPwd;
    }

    public String getLoginPwd()
    {
        return loginPwd;
    }
    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getAvatar()
    {
        return avatar;
    }
    public void setAccountExpiredStatus(String accountExpiredStatus)
    {
        this.accountExpiredStatus = accountExpiredStatus;
    }

    public String getAccountExpiredStatus()
    {
        return accountExpiredStatus;
    }
    public void setPasswdExpiredStatus(String passwdExpiredStatus)
    {
        this.passwdExpiredStatus = passwdExpiredStatus;
    }

    public String getPasswdExpiredStatus()
    {
        return passwdExpiredStatus;
    }
    public void setAccountLockedStatus(String accountLockedStatus)
    {
        this.accountLockedStatus = accountLockedStatus;
    }

    public String getAccountLockedStatus()
    {
        return accountLockedStatus;
    }
    public void setActive(String active)
    {
        this.active = active;
    }

    public String getActive()
    {
        return active;
    }
    public void setEnabled(String enabled)
    {
        this.enabled = enabled;
    }

    public String getEnabled()
    {
        return enabled;
    }
    public void setPwdValidPeriodDate(Date pwdValidPeriodDate)
    {
        this.pwdValidPeriodDate = pwdValidPeriodDate;
    }

    public Date getPwdValidPeriodDate()
    {
        return pwdValidPeriodDate;
    }
    public void setAcctValidPeriodDate(Date acctValidPeriodDate)
    {
        this.acctValidPeriodDate = acctValidPeriodDate;
    }

    public Date getAcctValidPeriodDate()
    {
        return acctValidPeriodDate;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userCode", getUserCode())
            .append("accountType", getAccountType())
            .append("loginAcct", getLoginAcct())
            .append("loginPwd", getLoginPwd())
            .append("avatar", getAvatar())
            .append("accountExpiredStatus", getAccountExpiredStatus())
            .append("passwdExpiredStatus", getPasswdExpiredStatus())
            .append("accountLockedStatus", getAccountLockedStatus())
            .append("active", getActive())
            .append("enabled", getEnabled())
            .append("pwdValidPeriodDate", getPwdValidPeriodDate())
            .append("acctValidPeriodDate", getAcctValidPeriodDate())
            .append("delFlag", getDelFlag())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
