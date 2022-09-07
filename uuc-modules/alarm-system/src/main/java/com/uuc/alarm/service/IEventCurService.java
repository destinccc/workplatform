package com.uuc.alarm.service;

import com.uuc.alarm.domain.EventCur;

import java.util.List;

/**
 * eventService接口
 *
 * @author uuc
 * @date 2022-07-28
 */
public interface IEventCurService {
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
     * @param eventCur event
     * @return event集合
     */
    List<EventCur> selectEventCurList(EventCur eventCur);

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
     * 批量删除event
     *
     * @param ids 需要删除的event主键集合
     * @return 结果
     */
    int deleteEventCurByIds(Long[] ids);

    /**
     * 删除event信息
     *
     * @param id event主键
     * @return 结果
     */
    int deleteEventCurById(Long id);

    /**
     * 屏蔽告警
     * @param id event主键
     * @return int 结果
     **/
    int shieldEventCur(Long id);

    /**
     * 确认告警
     * @param id event主键
     * @return int 无返回值
     **/
    int confirmEventCur(Long id);

    /**
     * 关闭告警
     * @param id event主键
     * @return int 结果
     **/
    int closeEventCur(Long id);

    /**
     * 批量关闭告警
     * @param ids 需要关闭的event主键集合
     * @return int 结果
     **/
    int batchCloseEventCur(Long[] ids);
}
