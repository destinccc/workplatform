package com.uuc.system.uuc.mapper;

import java.util.List;
import com.uuc.system.api.model.UucProject;

/**
 * 项目信息Mapper接口
 *
 * @author uuc
 * @date 2022-04-01
 */
public interface UucProjectMapper
{
    /**
     * 查询项目信息
     *
     * @param id 项目信息主键
     * @return 项目信息
     */
    public UucProject selectUucProjectById(Long id);

    /**
     * 查询项目信息列表
     *
     * @param uucProject 项目信息
     * @return 项目信息集合
     */
    public List<UucProject> selectUucProjectList(UucProject uucProject);

    /**
     * 新增项目信息
     *
     * @param uucProject 项目信息
     * @return 结果
     */
    public int insertUucProject(UucProject uucProject);

    /**
     * 修改项目信息
     *
     * @param uucProject 项目信息
     * @return 结果
     */
    public int updateUucProject(UucProject uucProject);

    /**
     * 删除项目信息
     *
     * @param id 项目信息主键
     * @return 结果
     */
    public int deleteUucProjectById(Long id);

    /**
     * 批量删除项目信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUucProjectByIds(Long[] ids);

    List<UucProject> selectAllProjects();

    /**
     * 根据项目编码查询所有子项目
     * @param parentId
     * @return
     */
    List<Long> selectChildrenProjectsByParentId(Long parentId);

    public int checkProjectCodeUnique(String projectCode);
}
