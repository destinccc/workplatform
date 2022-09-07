package com.uuc.alarm.service.impl;

import com.uuc.alarm.domain.AlarmEventLog;
import com.uuc.alarm.domain.dto.AlarmEventLogListDto;
import com.uuc.alarm.domain.po.AlarmEventLogListPo;
import com.uuc.alarm.domain.vo.AlarmEventLogListVo;
import com.uuc.alarm.mapper.AlarmEventLogMapper;
import com.uuc.alarm.service.IAlarmEventLogService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * alarm_event_logService业务层处理
 *
 * @author uuc
 * @date 2022-08-01
 */
@Service
public class AlarmEventLogServiceImpl implements IAlarmEventLogService {
    private final AlarmEventLogMapper alarmEventLogMapper;

    public AlarmEventLogServiceImpl(AlarmEventLogMapper alarmEventLogMapper) {
        this.alarmEventLogMapper = alarmEventLogMapper;
    }

    /**
     * 查询alarm_event_log
     *
     * @param id alarm_event_log主键
     * @return alarm_event_log
     */
    @Override
    public AlarmEventLog selectAlarmEventLogById(Long id) {
        return alarmEventLogMapper.selectAlarmEventLogById(id);
    }

    /**
     * 查询alarm_event_log列表
     *
     * @param dto alarm_event_log
     * @return alarm_event_log
     */
    @Override
    public List<AlarmEventLogListVo> selectAlarmEventLogList(AlarmEventLogListDto dto) {
        List<AlarmEventLogListPo> pos = alarmEventLogMapper.selectAlarmEventLogList(dto);
        List<AlarmEventLogListVo> vos = new ArrayList<>();
        for (AlarmEventLogListPo po : pos) {
            AlarmEventLogListVo vo = new AlarmEventLogListVo();
            vo.setId(po.getId());
            vo.setEventId(po.getEventId());
            vo.setStatus(po.getStatus());
            vo.setLogTime(po.getLogTime().getTime());
            vo.setValue(po.getValue());
            vo.setNotifyWay(po.getNotifyWay());
            vo.setContent(po.getContent());
            vos.add(vo);
        }
        return vos;
    }

    /**
     * 新增alarm_event_log
     *
     * @param alarmEventLog alarm_event_log
     * @return 结果
     */
    @Override
    public int insertAlarmEventLog(AlarmEventLog alarmEventLog) {
        return alarmEventLogMapper.insertAlarmEventLog(alarmEventLog);
    }

    /**
     * 修改alarm_event_log
     *
     * @param alarmEventLog alarm_event_log
     * @return 结果
     */
    @Override
    public int updateAlarmEventLog(AlarmEventLog alarmEventLog) {
        return alarmEventLogMapper.updateAlarmEventLog(alarmEventLog);
    }

    /**
     * 批量删除alarm_event_log
     *
     * @param ids 需要删除的alarm_event_log主键
     * @return 结果
     */
    @Override
    public int deleteAlarmEventLogByIds(Long[] ids) {
        return alarmEventLogMapper.deleteAlarmEventLogByIds(ids);
    }

    /**
     * 删除alarm_event_log信息
     *
     * @param id alarm_event_log主键
     * @return 结果
     */
    @Override
    public int deleteAlarmEventLogById(Long id) {
        return alarmEventLogMapper.deleteAlarmEventLogById(id);
    }

}
