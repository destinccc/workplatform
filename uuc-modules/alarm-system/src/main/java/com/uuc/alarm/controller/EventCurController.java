package com.uuc.alarm.controller;

import com.uuc.alarm.domain.EventCur;
import com.uuc.alarm.service.IEventCurService;
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
 * eventController
 *
 * @author uuc
 * @date 2022-07-28
 */
@RestController
@RequestMapping("/cur")
public class EventCurController extends BaseController {
    private final IEventCurService eventCurService;

    public EventCurController(IEventCurService eventCurService) {
        this.eventCurService = eventCurService;
    }

    /**
     * 查询event列表
     */
    @RequiresPermissions("system:cur:list")
    @GetMapping("/list")
    public TableDataInfo list(EventCur eventCur) {
        startPage();
        List<EventCur> list = eventCurService.selectEventCurList(eventCur);
        return getDataTable(list);
    }

    /**
     * 导出event列表
     */
    @RequiresPermissions("system:cur:export")
    @Log(title = "event", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EventCur eventCur) {
        List<EventCur> list = eventCurService.selectEventCurList(eventCur);
        ExcelUtil<EventCur> util = new ExcelUtil<>(EventCur.class);
        util.exportExcel(response, list, "event数据");
    }

    /**
     * 获取event详细信息
     */
    @RequiresPermissions("system:cur:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(eventCurService.selectEventCurById(id));
    }

    /**
     * 新增event
     */
    @RequiresPermissions("system:cur:add")
    @Log(title = "event", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EventCur eventCur) {
        return toAjax(eventCurService.insertEventCur(eventCur));
    }

    /**
     * 修改event
     */
    @RequiresPermissions("system:cur:edit")
    @Log(title = "event", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EventCur eventCur) {
        return toAjax(eventCurService.updateEventCur(eventCur));
    }

    /**
     * 删除event
     */
    @RequiresPermissions("system:cur:remove")
    @Log(title = "event", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(eventCurService.deleteEventCurByIds(ids));
    }

    /**
     * 屏蔽告警
     */
    @RequiresPermissions("system:alarm:shield")
    @Log(title = "告警", businessType = BusinessType.UPDATE)
    @PutMapping("/shield/{id}")
    public AjaxResult shield(@PathVariable Long id) {
        return toAjax(eventCurService.shieldEventCur(id));
    }

    /**
     * 确认告警
     */
    @RequiresPermissions("system:alarm:confirm")
    @Log(title = "告警", businessType = BusinessType.UPDATE)
    @PutMapping("/confirm/{id}")
    public AjaxResult confirm(@PathVariable Long id) {
        return toAjax(eventCurService.confirmEventCur(id));
    }

    /**
     * 关闭告警
     */
    @RequiresPermissions("monitor:alert:stop")
    @Log(title = "告警", businessType = BusinessType.UPDATE)
    @PutMapping("/close/{id}")
    public AjaxResult close(@PathVariable Long id) {
        return toAjax(eventCurService.closeEventCur(id));
    }

    /**
     * 批量关闭告警
     */
    @RequiresPermissions("monitor:alert:stop")
    @Log(title = "告警", businessType = BusinessType.DELETE)
    @PutMapping("/batchClose/{ids}")
    public AjaxResult batchClose(@PathVariable Long[] ids) {
        return toAjax(eventCurService.batchCloseEventCur(ids));
    }
}
