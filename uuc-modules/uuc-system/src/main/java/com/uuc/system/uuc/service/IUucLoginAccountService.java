package com.uuc.system.uuc.service;

import java.util.List;
import com.uuc.system.api.model.UucLoginAccount;

/**
 * 登录用户Service接口
 *
 * @author uuc
 * @date 2022-04-01
 */
public interface IUucLoginAccountService
{
    /**
     * 查询登录用户
     *
     * @param id 登录用户主键
     * @return 登录用户
     */
    public UucLoginAccount selectUucLoginAccountById(Long id);

    /**
     * 查询登录用户列表
     *
     * @param uucLoginAccount 登录用户
     * @return 登录用户集合
     */
    public List<UucLoginAccount> selectUucLoginAccountList(UucLoginAccount uucLoginAccount);

    /**
     * 新增登录用户
     *
     * @param uucLoginAccount 登录用户
     * @return 结果
     */
    public int insertUucLoginAccount(UucLoginAccount uucLoginAccount);

    /**
     * 修改登录用户
     *
     * @param uucLoginAccount 登录用户
     * @return 结果
     */
    public int updateUucLoginAccount(UucLoginAccount uucLoginAccount);

    /**
     * 批量删除登录用户
     *
     * @param ids 需要删除的登录用户主键集合
     * @return 结果
     */
    public int deleteUucLoginAccountByIds(Long[] ids);

    /**
     * 删除登录用户信息
     *
     * @param id 登录用户主键
     * @return 结果
     */
    public int deleteUucLoginAccountById(Long id);

    /**
     * 检查登录账号是否唯一
     * @param loginAcct
     * @return
     */
    public int checkLoginAcctUnique(String[] loginAcct);

    /**
     *
     * @param loginAccount
     * @return
     */
    public int resetPwd(UucLoginAccount loginAccount);
}
