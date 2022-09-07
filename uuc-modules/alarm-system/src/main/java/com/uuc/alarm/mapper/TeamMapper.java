package com.uuc.alarm.mapper;

import com.uuc.alarm.domain.Team;
import com.uuc.common.datasource.annotation.Slave;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author uuc
 * @date 2022-08-01
 */
@Mapper
public interface TeamMapper {
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
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    int deleteTeamById(Integer id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteTeamByIds(Integer[] ids);

    @Slave
    @MapKey("id")
    Map<Integer, Map<String, Object>> selectTeamNameByIds(@Param("ids") Set<Integer> ids);

    @Slave
    List<String> selectTeamNameListByIds(@Param("ids") Set<Integer> ids);
}
