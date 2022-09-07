package com.uuc.system.uuc.service;

import java.util.List;
import com.uuc.system.uuc.domain.UucRole;

/**
 * 角色信息Service接口
 * 
 * @author uuc
 * @date 2022-04-01
 */
public interface IUucRoleService 
{
    /**
     * 查询角色信息
     * 
     * @param roleId 角色信息主键
     * @return 角色信息
     */
    public UucRole selectUucRoleByRoleId(Long roleId);

    /**
     * 查询角色信息列表
     * 
     * @param uucRole 角色信息
     * @return 角色信息集合
     */
    public List<UucRole> selectUucRoleList(UucRole uucRole);

    /**
     * 新增角色信息
     * 
     * @param uucRole 角色信息
     * @return 结果
     */
    public int insertUucRole(UucRole uucRole);

    /**
     * 修改角色信息
     * 
     * @param uucRole 角色信息
     * @return 结果
     */
    public int updateUucRole(UucRole uucRole);

    /**
     * 批量删除角色信息
     * 
     * @param roleIds 需要删除的角色信息主键集合
     * @return 结果
     */
    public int deleteUucRoleByRoleIds(Long[] roleIds);

    /**
     * 删除角色信息信息
     * 
     * @param roleId 角色信息主键
     * @return 结果
     */
    public int deleteUucRoleByRoleId(Long roleId);
}
