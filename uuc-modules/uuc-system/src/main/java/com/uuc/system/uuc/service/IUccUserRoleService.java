package com.uuc.system.uuc.service;

import java.util.List;
import com.uuc.system.uuc.domain.UccUserRole;

/**
 * 用户角色关联Service接口
 * 
 * @author uuc
 * @date 2022-04-01
 */
public interface IUccUserRoleService 
{
    /**
     * 查询用户角色关联
     * 
     * @param userCode 用户角色关联主键
     * @return 用户角色关联
     */
    public UccUserRole selectUccUserRoleByUserCode(String userCode);

    /**
     * 查询用户角色关联列表
     * 
     * @param uccUserRole 用户角色关联
     * @return 用户角色关联集合
     */
    public List<UccUserRole> selectUccUserRoleList(UccUserRole uccUserRole);

    /**
     * 新增用户角色关联
     * 
     * @param uccUserRole 用户角色关联
     * @return 结果
     */
    public int insertUccUserRole(UccUserRole uccUserRole);

    /**
     * 修改用户角色关联
     * 
     * @param uccUserRole 用户角色关联
     * @return 结果
     */
    public int updateUccUserRole(UccUserRole uccUserRole);

    /**
     * 批量删除用户角色关联
     * 
     * @param userCodes 需要删除的用户角色关联主键集合
     * @return 结果
     */
    public int deleteUccUserRoleByUserCodes(String[] userCodes);

    /**
     * 删除用户角色关联信息
     * 
     * @param userCode 用户角色关联主键
     * @return 结果
     */
    public int deleteUccUserRoleByUserCode(String userCode);
}
