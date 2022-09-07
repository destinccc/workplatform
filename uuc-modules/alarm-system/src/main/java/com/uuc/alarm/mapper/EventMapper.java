package com.uuc.alarm.mapper;

import com.uuc.alarm.domain.Event;
import com.uuc.alarm.domain.dto.EventDto;
import com.uuc.alarm.domain.dto.EventListDto;
import com.uuc.alarm.domain.po.EventInfoPo;
import com.uuc.alarm.domain.po.EventListPo;
import com.uuc.alarm.domain.po.GroupPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * eventMapper接口
 *
 * @author uuc
 * @date 2022-08-01
 */
@Mapper
public interface EventMapper {
    /**
     * 查询event
     *
     * @param id event主键
     * @return event
     */
    Event selectEventById(Long id);

    /**
     * 查询event info
     *
     * @param id event主键
     * @return event
     */
    EventInfoPo selectEventInfoById(Long id);

    /**
     * 查询event列表
     *
     * @param dto event
     * @return event集合
     */
    List<EventListPo> selectEventList(EventListDto dto);

    /**
     * 新增event
     *
     * @param event event
     * @return 结果
     */
    int insertEvent(Event event);

    /**
     * 修改event
     *
     * @param event event
     * @return 结果
     */
    int updateEvent(Event event);

    /**
     * 删除event
     *
     * @param id event主键
     * @return 结果
     */
    int deleteEventById(Long id);

    /**
     * 批量删除event
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteEventByIds(Long[] ids);

    /**
     * 告警等级悬浮
     *
     * @param set 资源id list
     * @return 结果
     */
    List<GroupPo> selectGroupListByPriority(@Param("set") Set<Long> set);

    /**
     * 关闭告警
     *
     * @param dto event
     * @return 结果
     */
    int closeEvent(@Param("dto") EventDto dto);

    /**
     * 告警总数
     *
     * @param set 资源id list
     * @return 结果
     */
    List<GroupPo> selectGroupList(@Param("set") Set<Long> set);
}
