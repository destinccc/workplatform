package com.uuc.alarm.mapper;

import com.uuc.alarm.domain.AlarmEventLog;
import com.uuc.alarm.domain.dto.AlarmEventLogBatchInsertDto;
import com.uuc.alarm.domain.dto.AlarmEventLogListDto;
import com.uuc.alarm.domain.po.AlarmEventLogListPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * alarm_event_logMapper接口
 *
 * @author uuc
 * @date 2022-08-01
 */
@Mapper
public interface AlarmEventLogMapper {
    /**
     * 查询alarm_event_log
     *
     * @param id alarm_event_log主键
     * @return alarm_event_log
     */
    AlarmEventLog selectAlarmEventLogById(Long id);

    /**
     * 查询alarm_event_log列表
     *
     * @param dto alarm_event_log
     * @return alarm_event_log集合
     */
    List<AlarmEventLogListPo> selectAlarmEventLogList(AlarmEventLogListDto dto);


    /**
     * 新增alarm_event_log
     *
     * @param alarmEventLog alarm_event_log
     * @return 结果
     */
    int insertAlarmEventLog(AlarmEventLog alarmEventLog);

    /**
     * 修改alarm_event_log
     *
     * @param alarmEventLog alarm_event_log
     * @return 结果
     */
    int updateAlarmEventLog(AlarmEventLog alarmEventLog);

    /**
     * 删除alarm_event_log
     *
     * @param id alarm_event_log主键
     * @return 结果
     */
    int deleteAlarmEventLogById(Long id);

    /**
     * 批量删除alarm_event_log
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteAlarmEventLogByIds(Long[] ids);

    /**
     * 批量新增alarm_event_log
     *
     * @param dto alarm_event_log
     * @return 结果
     */
    int batchInsertAlarmEventLog(AlarmEventLogBatchInsertDto dto);

}
