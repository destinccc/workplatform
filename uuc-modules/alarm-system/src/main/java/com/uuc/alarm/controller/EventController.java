package com.uuc.alarm.controller;

import com.github.pagehelper.PageInfo;
import com.uuc.alarm.domain.Event;
import com.uuc.alarm.domain.dto.EventListDto;
import com.uuc.alarm.domain.vo.EventListVo;
import com.uuc.alarm.service.IEventService;
import com.uuc.alarm.util.PageUtil;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * eventController
 *
 * @author uuc
 * @date 2022-08-01
 */
@RestController
@RequestMapping("/event")
public class EventController extends BaseController {
    private final IEventService eventService;

    public EventController(IEventService eventService) {
        this.eventService = eventService;
    }

    /**
     * 查询event列表
     */
    @GetMapping("/list")
    public TableDataInfo list(EventListDto dto) {
        if (!"home".equals(dto.getType()) && (dto.getHours() == null || dto.getHours() == 0L)
                && (dto.getStartTime() == null || dto.getStartTime() == 0L)
                && (dto.getEndTime() == null || dto.getEndTime() == 0L)) {
            return getDataTable(new ArrayList<>());
        }
        // 告警列表
        PageInfo<EventListVo> page = eventService.page(dto);
        return PageUtil.getTableDataInfo(page);
    }

    /**
     * 导出event列表
     */
    @RequiresPermissions("monitor:alert:export")
    @Log(title = "event", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestParam String query,
                       @RequestParam String type, @RequestParam Long hours,
                       @RequestParam Long startTime, @RequestParam Long endTime,
                       @RequestParam List<Integer> priorities) {
        EventListDto dto = new EventListDto();
        dto.setType(type);
        dto.setHours(hours);
        dto.setStartTime(startTime);
        dto.setEndTime(endTime);
        dto.setPriorityList(priorities);
        dto.setQuery(query);
        dto.setExportFlag(true);
        List<EventListVo> list = eventService.selectEventListVo(dto);
        ExcelUtil<EventListVo> util = new ExcelUtil<>(EventListVo.class);
        util.exportExcel(response, list, "event数据");
    }

    /**
     * 获取event详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(eventService.selectEventInfoById(id));
    }

    /**
     * 新增event
     */
    @RequiresPermissions("system:event:add")
    @Log(title = "event", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Event event) {
        return toAjax(eventService.insertEvent(event));
    }

    /**
     * 修改event
     */
    @RequiresPermissions("system:event:edit")
    @Log(title = "event", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Event event) {
        return toAjax(eventService.updateEvent(event));
    }

    /**
     * 删除event
     */
    @RequiresPermissions("system:event:remove")
    @Log(title = "event", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(eventService.deleteEventByIds(ids));
    }
}
