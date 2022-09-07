package com.uuc.alarm.service;

import com.github.pagehelper.PageInfo;
import com.uuc.alarm.domain.Event;
import com.uuc.alarm.domain.dto.EventListDto;
import com.uuc.alarm.domain.po.GroupPo;
import com.uuc.alarm.domain.vo.EventInfoVo;
import com.uuc.alarm.domain.vo.EventListVo;

import java.util.List;
import java.util.Set;

/**
 * eventService接口
 *
 * @author uuc
 * @date 2022-08-01
 */
public interface IEventService {
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
    EventInfoVo selectEventInfoById(Long id);

    /**
     * 查询event列表
     *
     * @param dto event
     * @return event集合
     */
    List<EventListVo> selectEventListVo(EventListDto dto);

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
     * 批量删除event
     *
     * @param ids 需要删除的event主键集合
     * @return 结果
     */
    int deleteEventByIds(Long[] ids);

    /**
     * 删除event信息
     *
     * @param id event主键
     * @return 结果
     */
    int deleteEventById(Long id);

    /**
     * 查询悬浮数据
     *
     * @param list 查询悬浮数据
     * @return 结果
     */
    List<GroupPo> selectGroupListByPriority(Set<Long> list);

    /**
     * 查询event列表(Page)
     *
     * @param dto event
     * @return event集合
     */
    PageInfo<EventListVo> page(EventListDto dto);
}
