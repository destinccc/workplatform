package com.uuc.alarm.service;

import com.uuc.alarm.domain.Team;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author uuc
 * @date 2022-08-01
 */
public interface ITeamService {
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    Team selectTeamById(Integer id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param team 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    List<Team> selectTeamList(Team team);

    /**
     * 新增【请填写功能名称】
     *
     * @param team 【请填写功能名称】
     * @return 结果
     */
    int insertTeam(Team team);

    /**
     * 修改【请填写功能名称】
     *
     * @param team 【请填写功能名称】
     * @return 结果
     */
    int updateTeam(Team team);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    int deleteTeamByIds(Integer[] ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    int deleteTeamById(Integer id);
}
