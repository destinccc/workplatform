package com.uuc.system.uuc.mapper;

import java.util.List;

import com.uuc.system.api.model.SysMenu;
import com.uuc.system.uuc.domain.UucUserVisit;

/**
 * 用户菜单访问记录Mapper接口
 * 
 * @author uuc
 * @date 2022-04-19
 */
public interface UucUserVisitMapper 
{
    /**
     * 查询用户菜单访问记录
     * 
     * @param id 用户菜单访问记录主键
     * @return 用户菜单访问记录
     */
    public UucUserVisit selectUucUserVisitById(Long id);

    /**
     * 查询用户菜单访问记录列表
     * 
     * @param uucUserVisit 用户菜单访问记录
     * @return 用户菜单访问记录集合
     */
    public List<UucUserVisit> selectUucUserVisitList(UucUserVisit uucUserVisit);

    /**
     * 新增用户菜单访问记录
     * 
     * @param uucUserVisit 用户菜单访问记录
     * @return 结果
     */
    public int insertUucUserVisit(UucUserVisit uucUserVisit);

    /**
     * 修改用户菜单访问记录
     * 
     * @param uucUserVisit 用户菜单访问记录
     * @return 结果
     */
    public int updateUucUserVisit(UucUserVisit uucUserVisit);

    /**
     * 删除用户菜单访问记录
     * 
     * @param id 用户菜单访问记录主键
     * @return 结果
     */
    public int deleteUucUserVisitById(Long id);

    /**
     * 批量删除用户菜单访问记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUucUserVisitByIds(Long[] ids);

    /**
     * 查询用户最近访问的十条记录
     * @param userId
     * @return
     */
    public List<SysMenu> selectUucUserRecentVisit(Long userId);
}
