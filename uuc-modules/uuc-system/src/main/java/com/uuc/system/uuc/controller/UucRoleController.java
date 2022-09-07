package com.uuc.system.uuc.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.uuc.system.uuc.domain.UucRole;
import com.uuc.system.uuc.service.IUucRoleService;
import com.uuc.common.core.utils.poi.ExcelUtil;
import com.uuc.common.core.web.controller.BaseController;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.core.web.page.TableDataInfo;
import com.uuc.common.log.annotation.Log;
import com.uuc.common.log.enums.BusinessType;
import com.uuc.common.security.annotation.RequiresPermissions;

/**
 * 角色信息Controller
 * 
 * @author uuc
 * @date 2022-04-01
 */
@RestController
@RequestMapping("/uucRole")
public class UucRoleController extends BaseController
{
    @Autowired
    private IUucRoleService uucRoleService;

    /**
     * 查询角色信息列表
     */
    @RequiresPermissions("system:uucRole:list")
    @GetMapping("/list")
    public TableDataInfo list(UucRole uucRole)
    {
        startPage();
        List<UucRole> list = uucRoleService.selectUucRoleList(uucRole);
        return getDataTable(list);
    }

    /**
     * 导出角色信息列表
     */
    @RequiresPermissions("system:uucRole:export")
    @Log(title = "角色信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UucRole uucRole)
    {
        List<UucRole> list = uucRoleService.selectUucRoleList(uucRole);
        ExcelUtil<UucRole> util = new ExcelUtil<UucRole>(UucRole.class);
        util.exportExcel(response, list, "角色信息数据");
    }

    /**
     * 获取角色信息详细信息
     */
    @RequiresPermissions("system:uucRole:query")
    @GetMapping(value = "/{roleId}")
    public AjaxResult getInfo(@PathVariable("roleId") Long roleId)
    {
        return AjaxResult.success(uucRoleService.selectUucRoleByRoleId(roleId));
    }

    /**
     * 新增角色信息
     */
    @RequiresPermissions("system:uucRole:add")
    @Log(title = "角色信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UucRole uucRole)
    {
        return toAjax(uucRoleService.insertUucRole(uucRole));
    }

    /**
     * 修改角色信息
     */
    @RequiresPermissions("system:uucRole:edit")
    @Log(title = "角色信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UucRole uucRole)
    {
        return toAjax(uucRoleService.updateUucRole(uucRole));
    }

    /**
     * 删除角色信息
     */
    @RequiresPermissions("system:uucRole:remove")
    @Log(title = "角色信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{roleIds}")
    public AjaxResult remove(@PathVariable Long[] roleIds)
    {
        return toAjax(uucRoleService.deleteUucRoleByRoleIds(roleIds));
    }
}
