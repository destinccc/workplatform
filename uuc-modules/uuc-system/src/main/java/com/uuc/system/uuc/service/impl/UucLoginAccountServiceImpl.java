package com.uuc.system.uuc.service.impl;

import java.util.List;

import com.uuc.common.core.utils.DateUtils;
import com.uuc.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uuc.system.uuc.mapper.UucLoginAccountMapper;
import com.uuc.system.api.model.UucLoginAccount;
import com.uuc.system.uuc.service.IUucLoginAccountService;

/**
 * 登录用户Service业务层处理
 *
 * @author uuc
 * @date 2022-04-01
 */
@Service
public class UucLoginAccountServiceImpl implements IUucLoginAccountService
{
    @Autowired
    private UucLoginAccountMapper uucLoginAccountMapper;

    /**
     * 查询登录用户
     *
     * @param id 登录用户主键
     * @return 登录用户
     */
    @Override
    public UucLoginAccount selectUucLoginAccountById(Long id)
    {
        return uucLoginAccountMapper.selectUucLoginAccountById(id);
    }

    /**
     * 查询登录用户列表
     *
     * @param uucLoginAccount 登录用户
     * @return 登录用户
     */
    @Override
    public List<UucLoginAccount> selectUucLoginAccountList(UucLoginAccount uucLoginAccount)
    {
        return uucLoginAccountMapper.selectUucLoginAccountList(uucLoginAccount);
    }

    /**
     * 新增登录用户
     *
     * @param uucLoginAccount 登录用户
     * @return 结果
     */
    @Override
    public int insertUucLoginAccount(UucLoginAccount uucLoginAccount)
    {
        uucLoginAccount.setCreateTime(DateUtils.getNowDate());
        return uucLoginAccountMapper.insertUucLoginAccount(uucLoginAccount);
    }

    /**
     * 修改登录用户
     *
     * @param uucLoginAccount 登录用户
     * @return 结果
     */
    @Override
    public int updateUucLoginAccount(UucLoginAccount uucLoginAccount)
    {
        uucLoginAccount.setUpdateTime(DateUtils.getNowDate());
        return uucLoginAccountMapper.updateUucLoginAccount(uucLoginAccount);
    }

    /**
     * 批量删除登录用户
     *
     * @param ids 需要删除的登录用户主键
     * @return 结果
     */
    @Override
    public int deleteUucLoginAccountByIds(Long[] ids)
    {
        return uucLoginAccountMapper.deleteUucLoginAccountByIds(ids);
    }

    /**
     * 删除登录用户信息
     *
     * @param id 登录用户主键
     * @return 结果
     */
    @Override
    public int deleteUucLoginAccountById(Long id)
    {
        return uucLoginAccountMapper.deleteUucLoginAccountById(id);
    }

    @Override
    public int checkLoginAcctUnique(String[] loginAcct) {
        return uucLoginAccountMapper.checkLoginAcctUnique(loginAcct);
    }

    /**
     * 用户更新别的用户的密码
     * @param loginAccount
     * @return
     */
    @Override
    public int resetPwd(UucLoginAccount loginAccount) {
        loginAccount.setLoginPwd(SecurityUtils.encryptPassword(loginAccount.getLoginPwd()));
        loginAccount.setUpdateBy(String.valueOf(SecurityUtils.getUserId()));
        loginAccount.setUpdateTime(DateUtils.getNowDate());
        loginAccount.setUserCode(String.valueOf(loginAccount.getId()));
        return uucLoginAccountMapper.updateUucLoginAccount(loginAccount);
    }
}
