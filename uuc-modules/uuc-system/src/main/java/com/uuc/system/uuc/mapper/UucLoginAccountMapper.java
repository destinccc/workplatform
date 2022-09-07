package com.uuc.system.uuc.mapper;

import java.util.List;
import com.uuc.system.api.model.UucLoginAccount;
import org.apache.ibatis.annotations.Param;

/**
 * 登录用户Mapper接口
 *
 * @author uuc
 * @date 2022-04-01
 */
public interface UucLoginAccountMapper
{
    /**
     * 查询登录用户
     *
     * @param id 登录用户主键
     * @return 登录用户
     */
    public UucLoginAccount selectUucLoginAccountById(Long id);

    /**
     * 查询登录用户
     *
     * @param userCode 登录用户主键
     * @return 登录用户
     */
    public UucLoginAccount selectUucLoginAccountByCode(Long userCode);

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
     * 删除登录用户
     *
     * @param id 登录用户主键
     * @return 结果
     */
    public int deleteUucLoginAccountById(Long id);

    /**
     * 批量删除登录用户
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUucLoginAccountByIds(Long[] ids);

    /**
     * 批量插入登录账号
     * @param accountList
     */
    public void insertUucLoginAccountBatch(List<UucLoginAccount> accountList);

    /**
     * 检查登录账号是否唯一
     * @param loginAcct
     * @return
     */
    public int checkLoginAcctUnique(String[] loginAcct);

    /**
     * 重置用户密码
     *
     * @return 结果
     */
    public int resetUserPwd(UucLoginAccount uucLoginAccount);

    int deleteByUserCode(Long userId);

    List<UucLoginAccount> selectByUserId(Long userInfoId);

    int deleteDingAccountByIds(List<Long> deleteDingUser);
}
