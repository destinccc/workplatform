package com.uuc.system.uuc.controller;

import com.uuc.common.core.utils.poi.ExcelUtil;
import com.uuc.common.core.web.controller.BaseController;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.core.web.page.TableDataInfo;
import com.uuc.common.log.annotation.Log;
import com.uuc.common.log.enums.BusinessType;
import com.uuc.common.security.annotation.RequiresPermissions;
import com.uuc.system.api.model.UucOrganInfo;
import com.uuc.system.uuc.service.IUucOrganInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 机构信息Controller
 *
 * @author uuc
 * @date 2022-04-01
 */
@RestController
@RequestMapping("/organInfo")
public class UucOrganInfoController extends BaseController
{
    @Autowired
    private IUucOrganInfoService uucOrganInfoService;

    /**
     * 查询机构信息列表
     */
    @RequiresPermissions("system:organInfo:list")
    @GetMapping("/list")
    public TableDataInfo list(UucOrganInfo uucOrganInfo)
    {
        startPage();
        List<UucOrganInfo> list = uucOrganInfoService.selectUucOrganInfoList(uucOrganInfo);
        return getDataTable(list);
    }

    /**
     * 导出机构信息列表
     */
    @RequiresPermissions("system:organInfo:export")
    @Log(title = "机构信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UucOrganInfo uucOrganInfo)
    {
        List<UucOrganInfo> list = uucOrganInfoService.selectUucOrganInfoList(uucOrganInfo);
        ExcelUtil<UucOrganInfo> util = new ExcelUtil<UucOrganInfo>(UucOrganInfo.class);
        util.exportExcel(response, list, "机构信息数据");
    }

    /**
     * 获取机构信息详细信息
     */
    @RequiresPermissions("system:organInfo:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(uucOrganInfoService.selectUucOrganInfoById(id));
    }

    /**
     * 新增机构信息
     */
    @RequiresPermissions("system:organInfo:add")
    @Log(title = "机构信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UucOrganInfo uucOrganInfo)
    {
        return toAjax(uucOrganInfoService.insertUucOrganInfo(uucOrganInfo));
    }

    /**
     * 修改机构信息
     */
    @RequiresPermissions("system:organInfo:edit")
    @Log(title = "机构信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UucOrganInfo uucOrganInfo)
    {
        return toAjax(uucOrganInfoService.updateUucOrganInfo(uucOrganInfo));
    }

    /**
     * 删除机构信息
     */
    @RequiresPermissions("system:organInfo:remove")
    @Log(title = "机构信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(uucOrganInfoService.deleteUucOrganInfoByIds(ids));
    }
}
