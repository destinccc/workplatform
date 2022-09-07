package com.uuc.alarm.controller;

import com.uuc.alarm.domain.AlarmEventLog;
import com.uuc.alarm.domain.dto.AlarmEventLogListDto;
import com.uuc.alarm.domain.vo.AlarmEventLogListVo;
import com.uuc.alarm.service.IAlarmEventLogService;
import com.uuc.common.core.utils.poi.ExcelUtil;
import com.uuc.common.core.web.controller.BaseController;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.core.web.page.TableDataInfo;
import com.uuc.common.log.annotation.Log;
import com.uuc.common.log.enums.BusinessType;
import com.uuc.common.security.annotation.RequiresPermissions;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * alarm_event_logController
 *
 * @author uuc
 * @date 2022-08-01
 */
@RestController
@RequestMapping("/log")
public class AlarmEventLogController extends BaseController {
    private final IAlarmEventLogService alarmEventLogService;

    public AlarmEventLogController(IAlarmEventLogService alarmEventLogService) {
        this.alarmEventLogService = alarmEventLogService;
    }

    /**
     * 按条件查询alarm_event_log列表
     */
    @GetMapping("/list")
    public TableDataInfo list(AlarmEventLogListDto dto) {
        startPage();
        List<AlarmEventLogListVo> alarmEventLogList = alarmEventLogService.selectAlarmEventLogList(dto);
        if (CollectionUtils.isEmpty(alarmEventLogList)) {
            return new TableDataInfo();
        }
        return getDataTable(alarmEventLogList);
    }

    /**
     * 导出alarm_event_log列表
     */
    @RequiresPermissions("system:log:export")
    @Log(title = "alarm_event_log", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AlarmEventLogListDto dto) {
        List<AlarmEventLogListVo> list = alarmEventLogService.selectAlarmEventLogList(dto);
        ExcelUtil<AlarmEventLogListVo> util = new ExcelUtil<>(AlarmEventLogListVo.class);
        util.exportExcel(response, list, "alarm_event_log数据");
    }

    /**
     * 获取alarm_event_log详细信息
     */
    @RequiresPermissions("system:log:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(alarmEventLogService.selectAlarmEventLogById(id));
    }

    /**
     * 新增alarm_event_log
     */
    @RequiresPermissions("system:log:add")
    @Log(title = "alarm_event_log", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AlarmEventLog alarmEventLog) {
        return toAjax(alarmEventLogService.insertAlarmEventLog(alarmEventLog));
    }

    /**
     * 修改alarm_event_log
     */
    @RequiresPermissions("system:log:edit")
    @Log(title = "alarm_event_log", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AlarmEventLog alarmEventLog) {
        return toAjax(alarmEventLogService.updateAlarmEventLog(alarmEventLog));
    }

    /**
     * 删除alarm_event_log
     */
    @RequiresPermissions("system:log:remove")
    @Log(title = "alarm_event_log", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(alarmEventLogService.deleteAlarmEventLogByIds(ids));
    }
}
