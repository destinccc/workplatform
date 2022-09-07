package com.uuc.system.uuc.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uuc.system.uuc.mapper.UccUserRoleMapper;
import com.uuc.system.uuc.domain.UccUserRole;
import com.uuc.system.uuc.service.IUccUserRoleService;

/**
 * 用户角色关联Service业务层处理
 * 
 * @author uuc
 * @date 2022-04-01
 */
@Service
public class UccUserRoleServiceImpl implements IUccUserRoleService 
{
    @Autowired
    private UccUserRoleMapper uccUserRoleMapper;

    /**
     * 查询用户角色关联
     * 
     * @param userCode 用户角色关联主键
     * @return 用户角色关联
     */
    @Override
    public UccUserRole selectUccUserRoleByUserCode(String userCode)
    {
        return uccUserRoleMapper.selectUccUserRoleByUserCode(userCode);
    }

    /**
     * 查询用户角色关联列表
     * 
     * @param uccUserRole 用户角色关联
     * @return 用户角色关联
     */
    @Override
    public List<UccUserRole> selectUccUserRoleList(UccUserRole uccUserRole)
    {
        return uccUserRoleMapper.selectUccUserRoleList(uccUserRole);
    }

    /**
     * 新增用户角色关联
     * 
     * @param uccUserRole 用户角色关联
     * @return 结果
     */
    @Override
    public int insertUccUserRole(UccUserRole uccUserRole)
    {
        return uccUserRoleMapper.insertUccUserRole(uccUserRole);
    }

    /**
     * 修改用户角色关联
     * 
     * @param uccUserRole 用户角色关联
     * @return 结果
     */
    @Override
    public int updateUccUserRole(UccUserRole uccUserRole)
    {
        return uccUserRoleMapper.updateUccUserRole(uccUserRole);
    }

    /**
     * 批量删除用户角色关联
     * 
     * @param userCodes 需要删除的用户角色关联主键
     * @return 结果
     */
    @Override
    public int deleteUccUserRoleByUserCodes(String[] userCodes)
    {
        return uccUserRoleMapper.deleteUccUserRoleByUserCodes(userCodes);
    }

    /**
     * 删除用户角色关联信息
     * 
     * @param userCode 用户角色关联主键
     * @return 结果
     */
    @Override
    public int deleteUccUserRoleByUserCode(String userCode)
    {
        return uccUserRoleMapper.deleteUccUserRoleByUserCode(userCode);
    }
}
