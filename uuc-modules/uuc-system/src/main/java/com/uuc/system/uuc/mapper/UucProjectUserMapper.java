package com.uuc.system.uuc.mapper;

import java.util.List;
import com.uuc.system.api.model.UucProjectUser;

/**
 * 用户项目Mapper接口
 *
 * @author uuc
 * @date 2022-04-01
 */
public interface UucProjectUserMapper
{
    /**
     * 查询用户项目
     *
     * @param userCode 用户项目主键
     * @return 用户项目
     */
    public UucProjectUser selectUucProjectUserByUserCode(String userCode);

    /**
     * 查询用户项目列表
     *
     * @param uucProjectUser 用户项目
     * @return 用户项目集合
     */
    public List<UucProjectUser> selectUucProjectUserList(UucProjectUser uucProjectUser);

    /**
     * 新增用户项目
     *
     * @param uucProjectUser 用户项目
     * @return 结果
     */
    public int insertUucProjectUser(UucProjectUser uucProjectUser);

    /**
     * 批量新增用户项目
     *
     * @param uucProjectUserList 用户项目
     * @return 结果
     */
    public int batchInsertUucProjectUser(List<UucProjectUser> uucProjectUserList);

    /**
     * 修改用户项目
     *
     * @param uucProjectUser 用户项目
     * @return 结果
     */
    public int updateUucProjectUser(UucProjectUser uucProjectUser);

    /**
     * 删除用户项目
     *
     * @param userCode 用户项目主键
     * @return 结果
     */
    public int deleteUucProjectUserByUserCode(String userCode);

    /**
     * 根据项目删除关联用户
     * @param projectCode
     * @return
     */
    public int deleteUucProjectUserByProjectCode(String projectCode);

    /**
     * 批量删除用户项目
     *
     * @param userCodes 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUucProjectUserByUserCodes(String[] userCodes);

    /**
     * 批量删除项目用户
     *
     * @param projectCodes 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUucProjectUserByProjectCodes(Long[] projectCodes);

    List<UucProjectUser> selectUucProjectUserByProjectCode(String projectCode);

    List<UucProjectUser> selectUucProjectUserByCond(UucProjectUser uucProjectUser);
}
