package com.uuc.alarm.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.uuc.alarm.domain.Event;
import com.uuc.alarm.domain.dto.EventListDto;
import com.uuc.alarm.domain.po.EventInfoPo;
import com.uuc.alarm.domain.po.EventListPo;
import com.uuc.alarm.domain.po.GroupPo;
import com.uuc.alarm.domain.vo.EventInfoVo;
import com.uuc.alarm.domain.vo.EventListVo;
import com.uuc.alarm.mapper.EventMapper;
import com.uuc.alarm.mapper.TeamMapper;
import com.uuc.alarm.mapper.UserNosysMapper;
import com.uuc.alarm.service.IEventService;
import com.uuc.alarm.util.CommonUtil;
import com.uuc.alarm.util.PageUtil;
import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.utils.DateUtils;
import com.uuc.common.core.utils.PageUtils;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.security.service.TokenService;
import com.uuc.system.api.RemoteResourceService;
import com.uuc.system.api.domain.ResourceVO;
import com.uuc.system.api.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static com.uuc.alarm.util.CommonUtil.convertStringToIntegerList;
import static com.uuc.alarm.util.CommonUtil.convertStringToIntegerSet;

/**
 * eventService业务层处理
 *
 * @author uuc
 * @date 2022-08-01
 */
@Service
@Slf4j
public class EventServiceImpl implements IEventService {
    public static final Pattern COMPILE = Pattern.compile("[\\[\\]]");

    private final EventMapper eventMapper;
    private final TeamMapper teamMapper;
    private final UserNosysMapper userNosysMapper;
    private final TokenService tokenService;
    @Resource
    private RemoteResourceService remoteResourceService;

    @Autowired
    public EventServiceImpl(TeamMapper teamMapper, EventMapper eventMapper, UserNosysMapper userNosysMapper, TokenService tokenService) {
        this.teamMapper = teamMapper;
        this.eventMapper = eventMapper;
        this.userNosysMapper = userNosysMapper;
        this.tokenService = tokenService;
    }

    private static Integer getNotifyStatus(Integer notifyStatus) {
        List<Integer> ret = Lists.newArrayList();
        if (notifyStatus == 0) {
            ret.add(0);
            return ret.get(0);
        }
        if (((notifyStatus >> 5) & 0x01) == 1) {
            ret.add(32);
        }
        if (((notifyStatus >> 3) & 0x01) == 1) {
            ret.add(8);
            return ret.get(0);
        }
        if (((notifyStatus >> 2) & 0x01) == 1) {
            ret.add(4);
            return ret.get(0);
        }
        if ((notifyStatus & 0x01) == 1) {
            ret.add(1);
        }
        if (((notifyStatus >> 1) & 0x01) == 1) {
            ret.add(2);
        }
        if (((notifyStatus >> 4) & 0x01) == 1) {
            ret.add(16);
        }
        return ret.get(0);
    }

    @Override
    public Event selectEventById(Long id) {
        return eventMapper.selectEventById(id);
    }

    /**
     * 查询event
     *
     * @param id event主键
     * @return event
     */
    @Override
    public EventInfoVo selectEventInfoById(Long id) {
        EventInfoPo po = eventMapper.selectEventInfoById(id);
        if (po == null) {
            EventInfoVo eventInfoVo = new EventInfoVo();
            eventInfoVo.setId(id);
            return eventInfoVo;
        }
        EventInfoVo vo = new EventInfoVo();
        vo.setId(po.getId());
        vo.setStrategy(po.getStrategy());
        vo.setPriority(po.getPriority());
        vo.setNotifyStatus(getNotifyStatus(po.getNotifyStatus()));
        Set<Integer> userIdSet = CommonUtil.convertStringToIntegerSet(po.getUsers().replaceAll(COMPILE.pattern(), ""));
        Set<Integer> teamIdSet = CommonUtil.convertStringToIntegerSet(po.getGroups().replaceAll(COMPILE.pattern(), ""));
        Map<Integer, Map<String, Object>> userMap = null;
        Map<Integer, Map<String, Object>> teamMap = null;

        // 告警联系人
        if (CollUtil.isNotEmpty(userIdSet)) {
            userMap = userNosysMapper.selectUserNameByIds(userIdSet);
        }
        // 告警用户组
        if (CollUtil.isNotEmpty(teamIdSet)) {
            teamMap = teamMapper.selectTeamNameByIds(teamIdSet);
        }

        if (userMap != null && !userMap.isEmpty()
                && po.getUsers() != null && StringUtils.isNotEmpty(po.getUsers()) && !"[]".equals(po.getUsers())) {
            String notifyUserStr = po.getUsers();
            List<String> nameList = getNameList(userMap, notifyUserStr);
            vo.setUsers(nameList);
        }
        if (teamMap != null && !teamMap.isEmpty()
                && po.getGroups() != null && StringUtils.isNotEmpty(po.getGroups()) && !"[]".equals(po.getGroups())) {
            String notifyGroupStr = po.getGroups();
            List<String> nameList = getNameList(teamMap, notifyGroupStr);
            vo.setGroups(nameList);
        }
        vo.setEventType(po.getEventType());
        vo.setCreated(po.getCreated());
        vo.setSummary(po.getSummary());
        vo.setSid(po.getSid());
        vo.setStraGroupId(po.getStraGroupId());
        if (po.getCloseTime() != null) {
            vo.setCloseTime(po.getCloseTime().getTime());
        }
        vo.setCloser(po.getCloser());
        vo.setMetricName(StringUtils.substringBefore(po.getValue(), ":"));
        return vo;
    }

    /**
     * 查询event列表
     *
     * @param dto event
     * @return event
     */
    public List<EventListVo> selectEventListVo(EventListDto dto) {
        List<EventListPo> eventListPos = selectEventListPo(dto);
        return selectEventListVo(eventListPos);
    }

    /**
     * 新增event
     *
     * @param event event
     * @return 结果
     */
    @Override
    public int insertEvent(Event event) {
        return eventMapper.insertEvent(event);
    }

    /**
     * 修改event
     *
     * @param event event
     * @return 结果
     */
    @Override
    public int updateEvent(Event event) {
        event.setUpdateTime(DateUtils.getNowDate());
        return eventMapper.updateEvent(event);
    }

    /**
     * 批量删除event
     *
     * @param ids 需要删除的event主键
     * @return 结果
     */
    @Override
    public int deleteEventByIds(Long[] ids) {
        return eventMapper.deleteEventByIds(ids);
    }

    /**
     * 删除event信息
     *
     * @param id event主键
     * @return 结果
     */
    @Override
    public int deleteEventById(Long id) {
        return eventMapper.deleteEventById(id);
    }

    public List<GroupPo> selectGroupListByPriority(Set<Long> list) {
        return eventMapper.selectGroupListByPriority(list);
    }

    /**
     * 查询event列表
     *
     * @param dto event
     * @return event
     */
    @Override
    public PageInfo<EventListVo> page(EventListDto dto) {
        PageUtils.startPage();
        List<EventListPo> pos = selectEventListPo(dto);
        if (pos == null || pos.isEmpty()) {
            return new PageInfo<>(Lists.newArrayList());
        }
        Page<EventListPo> eventListPos = (Page<EventListPo>) pos;
        List<EventListVo> eventListVos = selectEventListVo(eventListPos);
        return PageUtil.pageInfo2PageInfoVo(eventListPos, eventListVos);
    }

    private List<EventListVo> selectEventListVo(List<EventListPo> eventListPos) {
        if (eventListPos.isEmpty()) {
            return Lists.newArrayList();
        }
        Set<Integer> userIdSet = new HashSet<>();
        Set<Integer> teamIdSet = new HashSet<>();
        Map<Integer, Map<String, Object>> userMap = null;
        Map<Integer, Map<String, Object>> teamMap = null;
        eventListPos.forEach(po -> {
            userIdSet.addAll(convertStringToIntegerList(po.getNotifyUser().replaceAll(COMPILE.pattern(), "")));
            teamIdSet.addAll(convertStringToIntegerList(po.getNotifyGroup().replaceAll(COMPILE.pattern(), "")));
        });
        // 告警联系人
        if (CollUtil.isNotEmpty(userIdSet)) {
            userMap = userNosysMapper.selectUserNameByIds(userIdSet);
        }
        // 告警用户组
        if (CollUtil.isNotEmpty(teamIdSet)) {
            teamMap = teamMapper.selectTeamNameByIds(teamIdSet);
        }
        List<EventListVo> eventListVos = Lists.newArrayList();
        for (EventListPo po : eventListPos) {
            EventListVo vo = getEventListVo(userMap, teamMap, po);
            eventListVos.add(vo);
        }
        return eventListVos;
    }

    @SuppressWarnings("unchecked")
    private List<EventListPo> selectEventListPo(EventListDto dto) {
        LoginUser loginUser = tokenService.getLoginUser();
        if (loginUser == null) {
            log.info("登录已失效");
            return Lists.newArrayList();
        }
        Long userid = loginUser.getUserid();
        ResourceVO resourceVO = new ResourceVO();
        resourceVO.setUserCode(userid + "");
        log.info("当前用户Id：" + userid);
        List<String> resTypes = Lists.newArrayList();
        resTypes.add("VirtualMachine");
        resTypes.add("PhysicalServer");
        resourceVO.setResTypes(resTypes);

        AjaxResult ajaxResult = remoteResourceService.userEndPonitResouce(resourceVO, SecurityConstants.INNER);
        if (ajaxResult == null || ajaxResult.getData() == null || !(ajaxResult.getData() instanceof List)) {
            log.info("获取资源信息异常，没获取到资源信息");
            return Lists.newArrayList();
        }
        Set<String> endpoints = Sets.newHashSet();
        try {
            List<Map<String, Object>> data = (List<Map<String, Object>>) ajaxResult.getData();
            log.info("获取的资源信息：" + data.size());
            for (Map<String, Object> map : data) {
                String resType = (String) map.get("resType");
                String resourceId = (String) map.get("resourceId");
                Map<String, Object> resourceMap = (Map<String, Object>) map.get("resourceMap");
                if (resourceMap != null && "VirtualMachine".equals(resType) && StringUtils.isNotEmpty((String) resourceMap.get("publicIp"))) {
                    endpoints.add((String) resourceMap.get("publicIp"));
                } else if (resourceMap != null && "PhysicalServer".equals(resType) && StringUtils.isNotEmpty((String) resourceMap.get("IPForBusiness"))) {
                    endpoints.add((String) resourceMap.get("IPForBusiness"));
                } else {
                    log.info("资源信息有误，忽略。。。" + resType + "---" + resourceId);
                }
            }
            if (endpoints.isEmpty()) {
                log.info("该用户没有可用资源，返回空列表");
                return Lists.newArrayList();
            }
            dto.setEndpointList(endpoints);
        } catch (Exception e) {
            log.info("获取资源信息异常");
            return Lists.newArrayList();
        }
        if (StringUtils.isNotEmpty(dto.getPriorities())) {
            List<Integer> priorityList = convertStringToIntegerList(dto.getPriorities());
            dto.setPriorityList(priorityList);
        }
        if (dto.getHours() != null && dto.getHours() != 0L) {
            dto.setStartTime(LocalDateTime.now().minusHours(dto.getHours()).toInstant(ZoneOffset.ofHours(8)).toEpochMilli() / 1000L);
            dto.setEndTime(0L);
        }
        return eventMapper.selectEventList(dto);
    }

    private EventListVo getEventListVo(Map<Integer, Map<String, Object>> userMap, Map<Integer, Map<String, Object>> teamMap, EventListPo po) {
        EventListVo vo = new EventListVo();
        vo.setId(po.getId());
        vo.setResourceId(po.getResourceId());
        vo.setResourceName(po.getResourceName());
        vo.setPriority(po.getPriority());
        vo.setEtime(po.getEtime());
        vo.setStrategyId(po.getStrategyId());
        vo.setStrategy(po.getStrategy());
        vo.setSummary(po.getSummary());
        vo.setAlarmTimes(po.getAlarmTimes());
        vo.setEventType(po.getEventType());
        vo.setNotifyStatus(getNotifyStatus(po.getNotifyStatus()));
        vo.setEndpoint(po.getEndpoint());
        vo.setNotifyTime(po.getNotifyTime());
        if (po.getCloseTime() != null) {
            vo.setCloseTime(po.getCloseTime().getTime());
        }
        vo.setCloser(po.getCloser());
        if (userMap != null && !userMap.isEmpty()
                && po.getNotifyUser() != null && StringUtils.isNotEmpty(po.getNotifyUser()) && !"[]".equals(po.getNotifyUser())) {
            String notifyUserStr = po.getNotifyUser();
            List<String> nameList = getNameList(userMap, notifyUserStr);
            vo.setNotifyUser(nameList);
        }
        if (teamMap != null && !teamMap.isEmpty()
                && po.getNotifyGroup() != null && StringUtils.isNotEmpty(po.getNotifyGroup()) && !"[]".equals(po.getNotifyGroup())) {
            String notifyGroupStr = po.getNotifyGroup();
            List<String> nameList = getNameList(teamMap, notifyGroupStr);
            vo.setNotifyGroup(nameList);
        }
        return vo;
    }

    private List<String> getNameList(Map<Integer, Map<String, Object>> nameMap, String notifyGroupStr) {
        Set<Integer> notifyGroup = convertStringToIntegerSet(notifyGroupStr.replaceAll(COMPILE.pattern(), ""));
        List<String> nameList = Lists.newArrayList();
        notifyGroup.forEach(id -> {
            Map<String, Object> idNameMap = nameMap.get(id);
            if (idNameMap != null) {
                String name = (String) idNameMap.get("name");
                if (StringUtils.isNotEmpty(name)) {
                    nameList.add(name);
                }
            }
        });
        return nameList;
    }
}
