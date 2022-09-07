package com.uuc.system.uuc.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.uuc.common.core.utils.poi.ExcelUtil;
import com.uuc.common.core.web.controller.BaseController;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.core.web.page.TableDataInfo;
import com.uuc.common.log.annotation.Log;
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
import com.uuc.system.uuc.domain.UucCategoryMenu;
import com.uuc.system.uuc.service.IUucCategoryMenuService;

/**
 * 类目菜单关联Controller
 * 
 * @author uuc
 * @date 2022-04-19
 */
@RestController
@RequestMapping("/categoryMenu")
public class UucCategoryMenuController extends BaseController
{
    @Autowired
    private IUucCategoryMenuService uucCategoryMenuService;

    /**
     * 查询类目菜单关联列表
     */
    @RequiresPermissions("system:categoryMenu:list")
    @GetMapping("/list")
    public TableDataInfo list(UucCategoryMenu uucCategoryMenu)
    {
        startPage();
        List<UucCategoryMenu> list = uucCategoryMenuService.selectUucCategoryMenuList(uucCategoryMenu);
        return getDataTable(list);
    }

    /**
     * 导出类目菜单关联列表
     */
    @RequiresPermissions("system:categoryMenu:export")
    @Log(title = "类目菜单关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UucCategoryMenu uucCategoryMenu)
    {
        List<UucCategoryMenu> list = uucCategoryMenuService.selectUucCategoryMenuList(uucCategoryMenu);
        ExcelUtil<UucCategoryMenu> util = new ExcelUtil<UucCategoryMenu>(UucCategoryMenu.class);
        util.exportExcel(response, list, "类目菜单关联数据");
    }

    /**
     * 获取类目菜单关联详细信息
     */
    @RequiresPermissions("system:categoryMenu:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(uucCategoryMenuService.selectUucCategoryMenuById(id));
    }

    /**
     * 新增类目菜单关联
     */
    @RequiresPermissions("system:categoryMenu:add")
    @Log(title = "类目菜单关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UucCategoryMenu uucCategoryMenu)
    {
        return toAjax(uucCategoryMenuService.insertUucCategoryMenu(uucCategoryMenu));
    }

    /**
     * 修改类目菜单关联
     */
    @RequiresPermissions("system:categoryMenu:edit")
    @Log(title = "类目菜单关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UucCategoryMenu uucCategoryMenu)
    {
        return toAjax(uucCategoryMenuService.updateUucCategoryMenu(uucCategoryMenu));
    }

    /**
     * 删除类目菜单关联
     */
    @RequiresPermissions("system:categoryMenu:remove")
    @Log(title = "类目菜单关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(uucCategoryMenuService.deleteUucCategoryMenuByIds(ids));
    }
}
