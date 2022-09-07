package com.uuc.system.uuc.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.uuc.common.log.enums.BusinessType;
import com.uuc.common.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.uuc.system.uuc.domain.UucApplication;
import com.uuc.system.uuc.service.IUucApplicationService;
import com.uuc.common.core.utils.poi.ExcelUtil;
import com.uuc.common.core.web.controller.BaseController;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.core.web.page.TableDataInfo;
import com.uuc.common.log.annotation.Log;

/**
 * 应用信息Controller
 * 
 * @author uuc
 * @date 2022-04-01
 */
@RestController
@RequestMapping("/application")
public class UucApplicationController extends BaseController
{
    @Autowired
    private IUucApplicationService uucApplicationService;

    /**
     * 查询应用信息列表
     */
    @RequiresPermissions("system:application:list")
    @GetMapping("/list")
    public TableDataInfo list(UucApplication uucApplication)
    {
        startPage();
        List<UucApplication> list = uucApplicationService.selectUucApplicationList(uucApplication);
        return getDataTable(list);
    }

    /**
     * 导出应用信息列表
     */
    @RequiresPermissions("system:application:export")
    @Log(title = "应用信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UucApplication uucApplication)
    {
        List<UucApplication> list = uucApplicationService.selectUucApplicationList(uucApplication);
        ExcelUtil<UucApplication> util = new ExcelUtil<UucApplication>(UucApplication.class);
        util.exportExcel(response, list, "应用信息数据");
    }

    /**
     * 获取应用信息详细信息
     */
    @RequiresPermissions("system:application:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(uucApplicationService.selectUucApplicationById(id));
    }

    /**
     * 新增应用信息
     */
    @RequiresPermissions("system:application:add")
    @Log(title = "应用信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UucApplication uucApplication)
    {
        return toAjax(uucApplicationService.insertUucApplication(uucApplication));
    }

    /**
     * 修改应用信息
     */
    @RequiresPermissions("system:application:edit")
    @Log(title = "应用信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UucApplication uucApplication)
    {
        return toAjax(uucApplicationService.updateUucApplication(uucApplication));
    }

    /**
     * 删除应用信息
     */
    @RequiresPermissions("system:application:remove")
    @Log(title = "应用信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(uucApplicationService.deleteUucApplicationByIds(ids));
    }
}
