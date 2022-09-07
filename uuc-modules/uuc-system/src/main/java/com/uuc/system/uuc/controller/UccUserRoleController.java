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
import com.uuc.system.uuc.domain.UccUserRole;
import com.uuc.system.uuc.service.IUccUserRoleService;
import com.uuc.common.core.utils.poi.ExcelUtil;
import com.uuc.common.core.web.controller.BaseController;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.core.web.page.TableDataInfo;
import com.uuc.common.log.annotation.Log;

/**
 * 用户角色关联Controller
 * 
 * @author uuc
 * @date 2022-04-01
 */
@RestController
@RequestMapping("/userRole")
public class UccUserRoleController extends BaseController
{
    @Autowired
    private IUccUserRoleService uccUserRoleService;

    /**
     * 查询用户角色关联列表
     */
    @RequiresPermissions("system:userRole:list")
    @GetMapping("/list")
    public TableDataInfo list(UccUserRole uccUserRole)
    {
        startPage();
        List<UccUserRole> list = uccUserRoleService.selectUccUserRoleList(uccUserRole);
        return getDataTable(list);
    }

    /**
     * 导出用户角色关联列表
     */
    @RequiresPermissions("system:userRole:export")
    @Log(title = "用户角色关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UccUserRole uccUserRole)
    {
        List<UccUserRole> list = uccUserRoleService.selectUccUserRoleList(uccUserRole);
        ExcelUtil<UccUserRole> util = new ExcelUtil<UccUserRole>(UccUserRole.class);
        util.exportExcel(response, list, "用户角色关联数据");
    }

    /**
     * 获取用户角色关联详细信息
     */
    @RequiresPermissions("system:userRole:query")
    @GetMapping(value = "/{userCode}")
    public AjaxResult getInfo(@PathVariable("userCode") String userCode)
    {
        return AjaxResult.success(uccUserRoleService.selectUccUserRoleByUserCode(userCode));
    }

    /**
     * 新增用户角色关联
     */
    @RequiresPermissions("system:userRole:add")
    @Log(title = "用户角色关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UccUserRole uccUserRole)
    {
        return toAjax(uccUserRoleService.insertUccUserRole(uccUserRole));
    }

    /**
     * 修改用户角色关联
     */
    @RequiresPermissions("system:userRole:edit")
    @Log(title = "用户角色关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UccUserRole uccUserRole)
    {
        return toAjax(uccUserRoleService.updateUccUserRole(uccUserRole));
    }

    /**
     * 删除用户角色关联
     */
    @RequiresPermissions("system:userRole:remove")
    @Log(title = "用户角色关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userCodes}")
    public AjaxResult remove(@PathVariable String[] userCodes)
    {
        return toAjax(uccUserRoleService.deleteUccUserRoleByUserCodes(userCodes));
    }
}
