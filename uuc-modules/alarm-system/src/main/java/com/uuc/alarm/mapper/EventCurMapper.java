package com.uuc.alarm.mapper;

import com.uuc.alarm.domain.EventCur;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * eventMapper接口
 *
 * @author uuc
 * @date 2022-07-28
 */
@Mapper
public interface EventCurMapper {
    /**
     * 查询event
     *
     * @param id event主键
     * @return event
     */
    EventCur selectEventCurById(Long id);

    /**
     * 查询event列表
     *
     * @param event event
     * @return event集合
     */
    List<EventCur> selectEventCurList(EventCur event);

    /**
     * 新增event
     *
     * @param eventCur event
     * @return 结果
     */
    int insertEventCur(EventCur eventCur);

    /**
     * 修改event
     *
     * @param eventCur event
     * @return 结果
     */
    int updateEventCur(EventCur eventCur);

    /**
     * 删除event
     *
     * @param id event主键
     * @return 结果
     */
    int deleteEventCurById(Long id);

    /**
     * 批量删除event
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteEventCurByIds(Long[] ids);

    /**
     * 根据event表的id删除event_cur记录，通过hashid进行关联
     *
     * @param ids event主键
     * @return 结果
     */
    int deleteEventCurByEventIds(Long[] ids);
}
