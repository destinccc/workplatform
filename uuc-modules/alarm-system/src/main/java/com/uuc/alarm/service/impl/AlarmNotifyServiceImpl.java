package com.uuc.alarm.service.impl;

import com.uuc.alarm.domain.AlarmNotify;
import com.uuc.alarm.domain.dto.AlarmNotifyListDto;
import com.uuc.alarm.domain.po.AlarmNotifyListPo;
import com.uuc.alarm.domain.vo.AlarmNotifyVo;
import com.uuc.alarm.mapper.AlarmNotifyMapper;
import com.uuc.alarm.mapper.TeamMapper;
import com.uuc.alarm.mapper.UserNosysMapper;
import com.uuc.alarm.service.IAlarmNotifyService;
import com.uuc.common.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static com.uuc.alarm.util.CommonUtil.convertStringToIntegerSet;

/**
 * alarm_notifyService业务层处理
 *
 * @author uuc
 * @date 2022-08-01
 */
@Service
public class AlarmNotifyServiceImpl implements IAlarmNotifyService {
    public static final Pattern COMPILE = Pattern.compile("[\\[\\]]");
    private final AlarmNotifyMapper alarmNotifyMapper;

    private final UserNosysMapper userNosysMapper;

    private final TeamMapper teamMapper;

    @Autowired
    public AlarmNotifyServiceImpl(UserNosysMapper userNosysMapper, AlarmNotifyMapper alarmNotifyMapper, TeamMapper teamMapper) {
        this.userNosysMapper = userNosysMapper;
        this.alarmNotifyMapper = alarmNotifyMapper;
        this.teamMapper = teamMapper;
    }

    /**
     * 查询alarm_notify
     *
     * @param id alarm_notify主键
     * @return alarm_notify
     */
    @Override
    public AlarmNotify selectAlarmNotifyById(Long id) {
        return alarmNotifyMapper.selectAlarmNotifyById(id);
    }

    /**
     * 查询alarm_notify列表
     *
     * @param dto alarm_notify
     * @return alarm_notify
     */
    @Override
    public List<AlarmNotifyVo> selectAlarmNotifyList(AlarmNotifyListDto dto) {
        List<AlarmNotifyListPo> list = alarmNotifyMapper.selectAlarmNotifyList(dto);
        List<AlarmNotifyVo> alarmNotifyVoList = new ArrayList<>();
        for (AlarmNotifyListPo po : list) {
            AlarmNotifyVo vo = new AlarmNotifyVo();
            vo.setId(po.getId());
            vo.setEventTime(po.getEventTime().getTime());
            vo.setNotifyWay(po.getNotifyWay());
            vo.setContent(po.getContent());
            vo.setEventId(po.getEventId());
            vo.setNotifyTime(po.getNotifyTime().getTime());
            vo.setStatus(po.getStatus());
            if (StringUtils.isNotEmpty(po.getNotifyUsers()) && !"[]".equals(po.getNotifyUsers())) {
                Set<Integer> notifyUser = convertStringToIntegerSet(po.getNotifyUsers().replaceAll(COMPILE.pattern(), ""));
                List<String> users = userNosysMapper.selectUserNameListByIds(notifyUser);
                vo.setNotifyUsers(users);
            }
            if (StringUtils.isNotEmpty(po.getNotifyGroups()) && !"[]".equals(po.getNotifyGroups())) {
                Set<Integer> notifyGroup = convertStringToIntegerSet(po.getNotifyGroups().replaceAll(COMPILE.pattern(), ""));
                List<String> teams = teamMapper.selectTeamNameListByIds(notifyGroup);
                vo.setNotifyGroups(teams);
            }
            alarmNotifyVoList.add(vo);
        }
        return alarmNotifyVoList;
    }

    /**
     * 新增alarm_notify
     *
     * @param alarmNotify alarm_notify
     * @return 结果
     */
    @Override
    public int insertAlarmNotify(AlarmNotify alarmNotify) {
        return alarmNotifyMapper.insertAlarmNotify(alarmNotify);
    }

    /**
     * 修改alarm_notify
     *
     * @param alarmNotify alarm_notify
     * @return 结果
     */
    @Override
    public int updateAlarmNotify(AlarmNotify alarmNotify) {
        return alarmNotifyMapper.updateAlarmNotify(alarmNotify);
    }

    /**
     * 批量删除alarm_notify
     *
     * @param ids 需要删除的alarm_notify主键
     * @return 结果
     */
    @Override
    public int deleteAlarmNotifyByIds(Long[] ids) {
        return alarmNotifyMapper.deleteAlarmNotifyByIds(ids);
    }

    /**
     * 删除alarm_notify信息
     *
     * @param id alarm_notify主键
     * @return 结果
     */
    @Override
    public int deleteAlarmNotifyById(Long id) {
        return alarmNotifyMapper.deleteAlarmNotifyById(id);
    }
}
