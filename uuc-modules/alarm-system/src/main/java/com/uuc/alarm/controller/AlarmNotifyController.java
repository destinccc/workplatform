package com.uuc.alarm.controller;

import com.uuc.alarm.domain.AlarmNotify;
import com.uuc.alarm.domain.dto.AlarmNotifyListDto;
import com.uuc.alarm.domain.vo.AlarmNotifyVo;
import com.uuc.alarm.service.IAlarmNotifyService;
import com.uuc.common.core.utils.poi.ExcelUtil;
import com.uuc.common.core.web.controller.BaseController;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.core.web.page.TableDataInfo;
import com.uuc.common.log.annotation.Log;
import com.uuc.common.log.enums.BusinessType;
import com.uuc.common.security.annotation.RequiresPermissions;
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
 * alarm_notifyController
 *
 * @author uuc
 * @date 2022-08-01
 */
@RestController
@RequestMapping("/notify")
public class AlarmNotifyController extends BaseController {
    private final IAlarmNotifyService alarmNotifyService;

    public AlarmNotifyController(IAlarmNotifyService alarmNotifyService) {
        this.alarmNotifyService = alarmNotifyService;
    }

    /**
     * 查询alarm_notify列表
     */
    @GetMapping("/list")
    public TableDataInfo list(AlarmNotifyListDto dto) {
        startPage();
        List<AlarmNotifyVo> list = alarmNotifyService.selectAlarmNotifyList(dto);
        return getDataTable(list);
    }

    /**
     * 导出alarm_notify列表
     */
    @RequiresPermissions("system:notify:export")
    @Log(title = "alarm_notify", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AlarmNotifyListDto dto) {
        List<AlarmNotifyVo> list = alarmNotifyService.selectAlarmNotifyList(dto);
        ExcelUtil<AlarmNotifyVo> util = new ExcelUtil<>(AlarmNotifyVo.class);
        util.exportExcel(response, list, "alarm_notify数据");
    }

    /**
     * 获取alarm_notify详细信息
     */
    @RequiresPermissions("system:notify:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(alarmNotifyService.selectAlarmNotifyById(id));
    }

    /**
     * 新增alarm_notify
     */
    @RequiresPermissions("system:notify:add")
    @Log(title = "alarm_notify", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AlarmNotify alarmNotify) {
        return toAjax(alarmNotifyService.insertAlarmNotify(alarmNotify));
    }

    /**
     * 修改alarm_notify
     */
    @RequiresPermissions("system:notify:edit")
    @Log(title = "alarm_notify", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AlarmNotify alarmNotify) {
        return toAjax(alarmNotifyService.updateAlarmNotify(alarmNotify));
    }

    /**
     * 删除alarm_notify
     */
    @RequiresPermissions("system:notify:remove")
    @Log(title = "alarm_notify", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(alarmNotifyService.deleteAlarmNotifyByIds(ids));
    }
}
