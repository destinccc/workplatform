package com.uuc.alarm.service.impl;

import com.uuc.alarm.domain.Team;
import com.uuc.alarm.mapper.TeamMapper;
import com.uuc.alarm.service.ITeamService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author uuc
 * @date 2022-08-01
 */
@Service
public class TeamServiceImpl implements ITeamService {
    private final TeamMapper teamMapper;

    public TeamServiceImpl(TeamMapper teamMapper) {
        this.teamMapper = teamMapper;
    }

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public Team selectTeamById(Integer id) {
        return teamMapper.selectTeamById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param team 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<Team> selectTeamList(Team team) {
        return teamMapper.selectTeamList(team);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param team 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertTeam(Team team) {
        return teamMapper.insertTeam(team);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param team 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateTeam(Team team) {
        return teamMapper.updateTeam(team);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteTeamByIds(Integer[] ids) {
        return teamMapper.deleteTeamByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteTeamById(Integer id) {
        return teamMapper.deleteTeamById(id);
    }
}
