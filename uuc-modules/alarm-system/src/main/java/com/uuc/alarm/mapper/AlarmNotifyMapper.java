package com.uuc.alarm.mapper;

import com.uuc.alarm.domain.AlarmNotify;
import com.uuc.alarm.domain.dto.AlarmNotifyListDto;
import com.uuc.alarm.domain.po.AlarmNotifyListPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * alarm_notifyMapper接口
 *
 * @author uuc
 * @date 2022-08-01
 */
@Mapper
public interface AlarmNotifyMapper {
    /**
     * 查询alarm_notify
     *
     * @param id alarm_notify主键
     * @return alarm_notify
     */
    AlarmNotify selectAlarmNotifyById(Long id);

    /**
     * 查询alarm_notify列表
     *
     * @param dto alarm_notify
     * @return alarm_notify集合
     */
    List<AlarmNotifyListPo> selectAlarmNotifyList(AlarmNotifyListDto dto);

    /**
     * 新增alarm_notify
     *
     * @param alarmNotify alarm_notify
     * @return 结果
     */
    int insertAlarmNotify(AlarmNotify alarmNotify);

    /**
     * 修改alarm_notify
     *
     * @param alarmNotify alarm_notify
     * @return 结果
     */
    int updateAlarmNotify(AlarmNotify alarmNotify);

    /**
     * 删除alarm_notify
     *
     * @param id alarm_notify主键
     * @return 结果
     */
    int deleteAlarmNotifyById(Long id);

    /**
     * 批量删除alarm_notify
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteAlarmNotifyByIds(Long[] ids);
}
