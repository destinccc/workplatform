package com.uuc.alarm.service.impl;

import com.uuc.alarm.domain.AlarmEventLog;
import com.uuc.alarm.domain.EventCur;
import com.uuc.alarm.domain.dto.AlarmEventLogBatchInsertDto;
import com.uuc.alarm.domain.dto.EventDto;
import com.uuc.alarm.mapper.AlarmEventLogMapper;
import com.uuc.alarm.mapper.EventCurMapper;
import com.uuc.alarm.mapper.EventMapper;
import com.uuc.alarm.service.IEventCurService;
import com.uuc.common.core.context.SecurityContextHolder;
import com.uuc.common.core.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * eventService业务层处理
 *
 * @author uuc
 * @date 2022-07-28
 */
@Service
@Slf4j
public class EventCurServiceImpl implements IEventCurService {
    public static final String BATCH_CLOSE_FAILED = "批量关闭告警失败";
    public static final String CLOSE_FAILED = "关闭告警失败";
    private final EventCurMapper eventCurMapper;
    private final AlarmEventLogMapper alarmEventLogMapper;
    private final EventMapper eventMapper;

    @Autowired
    public EventCurServiceImpl(EventCurMapper eventCurMapper, AlarmEventLogMapper alarmEventLogMapper, EventMapper eventMapper) {
        this.eventCurMapper = eventCurMapper;
        this.alarmEventLogMapper = alarmEventLogMapper;
        this.eventMapper = eventMapper;
    }

    /**
     * 查询event
     *
     * @param id event主键
     * @return event
     */
    @Override
    public EventCur selectEventCurById(Long id) {
        return eventCurMapper.selectEventCurById(id);
    }

    /**
     * 查询event列表
     *
     * @param eventCur event
     * @return event
     */
    @Override
    public List<EventCur> selectEventCurList(EventCur eventCur) {
        return eventCurMapper.selectEventCurList(eventCur);
    }

    /**
     * 新增event
     *
     * @param eventCur event
     * @return 结果
     */
    @Override
    public int insertEventCur(EventCur eventCur) {
        return eventCurMapper.insertEventCur(eventCur);
    }

    /**
     * 修改event
     *
     * @param eventCur event
     * @return 结果
     */
    @Override
    public int updateEventCur(EventCur eventCur) {
        return eventCurMapper.updateEventCur(eventCur);
    }

    /**
     * 批量删除event
     *
     * @param ids 需要删除的event主键
     * @return 结果
     */
    @Override
    public int deleteEventCurByIds(Long[] ids) {
        return eventCurMapper.deleteEventCurByIds(ids);
    }

    /**
     * 删除event信息
     *
     * @param id event主键
     * @return 结果
     */
    @Override
    public int deleteEventCurById(Long id) {
        return eventCurMapper.deleteEventCurById(id);
    }

    /**
     * 屏蔽告警
     * @param id event主键
     * @return int 结果
     **/
    @Override
    public int shieldEventCur(Long id) {
        // 先使用Go的接口
        EventCur eventCur = new EventCur();
        eventCur.setId(id);
        log.info("告警屏蔽");
        return eventCurMapper.updateEventCur(eventCur);
    }

    /**
     * 确认告警
     * @param id event主键
     * @return int 无返回值
     **/
    @Override
    public int confirmEventCur(Long id) {
        EventCur eventCur = new EventCur();
        eventCur.setId(id);
        log.info("告警确认");
        return eventCurMapper.updateEventCur(eventCur);
    }

    /**
     * 关闭告警
     * @param id event主键
     * @return int 结果
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int closeEventCur(Long id) {
        // 添加关闭告警的日志
        AlarmEventLog alarmEventLog = new AlarmEventLog();
        alarmEventLog.setEventId(id);
        alarmEventLog.setStatus(5);
        alarmEventLog.setLogTime(new Date());
        alarmEventLog.setValue("");
        alarmEventLog.setNotifyWay(null);
        alarmEventLog.setContent("关闭人：" + SecurityContextHolder.getUserName());
        alarmEventLog.setCreated(new Date());
        alarmEventLog.setUpdated(new Date());
        int i = alarmEventLogMapper.insertAlarmEventLog(alarmEventLog);
        if (i != 1) {
            log.info("插入关闭告警日志异常，告警Id为：{}", id);
            throw new ServiceException(CLOSE_FAILED);
        }
        // 更新告警状态
        EventDto dto = new EventDto();
        dto.setId(id);
        dto.setCloser(SecurityContextHolder.getUserName());
        dto.setCloseTime(new Date());
        i = eventMapper.closeEvent(dto);
        if (i != 1) {
            log.info("更新告警状态异常，告警Id为：{}", id);
            throw new ServiceException(CLOSE_FAILED);
        }
        // 删除活跃告警
        i = eventCurMapper.deleteEventCurByEventIds(new Long[]{id});
        if (i != 1) {
            log.info("删除活跃告警异常，告警Id为：{}", id);
        }
        return i;
    }

    /**
     * 批量关闭告警
     * @param ids 需要关闭的event主键集合
     * @return int 结果
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchCloseEventCur(Long[] ids) {
        // 添加关闭告警的日志
        AlarmEventLogBatchInsertDto dto = new AlarmEventLogBatchInsertDto();
        dto.setIds(ids);
        dto.setStatus(5);
        dto.setLogTime(new Date());
        dto.setValue("");
        dto.setNotifyWay(null);
        dto.setContent("关闭人：" + SecurityContextHolder.getUserName());
        dto.setCreated(new Date());
        dto.setUpdated(new Date());
        int i = alarmEventLogMapper.batchInsertAlarmEventLog(dto);
        if (i == 0) {
            log.info("批量插入关闭告警日志异常，告警Id为：{}", ArrayUtils.toString(ids));
            throw new ServiceException(BATCH_CLOSE_FAILED);
        }
        // 更新告警状态
        EventDto dto2 = new EventDto();
        dto2.setIds(ids);
        dto2.setCloser(SecurityContextHolder.getUserName());
        dto2.setCloseTime(new Date());
        i = eventMapper.closeEvent(dto2);
        if (i == 0) {
            log.info("批量更新告警状态异常，告警Id为：{}", ArrayUtils.toString(ids));
            throw new ServiceException(BATCH_CLOSE_FAILED);
        }
        // 删除活跃告警
        i = eventCurMapper.deleteEventCurByEventIds(ids);
        if (i == 0) {
            log.info("批量删除活跃告警异常，告警Id为：{}", ArrayUtils.toString(ids));
        }
        return i;
    }
}
