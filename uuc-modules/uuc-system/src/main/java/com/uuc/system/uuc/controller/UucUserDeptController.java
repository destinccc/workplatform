package com.uuc.system.uuc.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.uuc.common.security.annotation.InnerAuth;
import com.uuc.system.uuc.service.impl.UucUserDeptService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.uuc.system.api.model.UucUserDept;
import com.uuc.common.core.utils.poi.ExcelUtil;
import com.uuc.common.core.web.controller.BaseController;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.core.web.page.TableDataInfo;
import com.uuc.common.log.annotation.Log;
import com.uuc.common.log.enums.BusinessType;
import com.uuc.common.security.annotation.RequiresPermissions;

/**
 * 用户组织Controller
 *
 * @author uuc
 * @date 2022-04-01
 */
@RestController
@RequestMapping("/userDept")
public class UucUserDeptController extends BaseController
{
    @Autowired
    private UucUserDeptService uucUserDeptService;

    /**
     * 查询用户组织列表
     */
    @RequiresPermissions("system:userDept:list")
    @GetMapping("/list")
    public TableDataInfo list(UucUserDept uucUserDept)
    {
        startPage();
        List<UucUserDept> list = uucUserDeptService.selectUucUserDeptList(uucUserDept);
        return getDataTable(list);
    }

    @InnerAuth
    @PostMapping("/adminUserList")
    @ApiOperation(value ="获取组织负责人", notes = "获取组织负责人")
    public AjaxResult getAdminUserList()
    {
        UucUserDept uucUserDept = new UucUserDept();
        uucUserDept.setAdminFlag("1");
        return AjaxResult.success(uucUserDeptService.selectUucUserDeptList(uucUserDept));
    }

    /**
     * 导出用户组织列表
     */
    @RequiresPermissions("system:userDept:export")
    @Log(title = "用户组织", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UucUserDept uucUserDept)
    {
        List<UucUserDept> list = uucUserDeptService.selectUucUserDeptList(uucUserDept);
        ExcelUtil<UucUserDept> util = new ExcelUtil<UucUserDept>(UucUserDept.class);
        util.exportExcel(response, list, "用户组织数据");
    }

    /**
     * 获取用户组织详细信息
     */
    @RequiresPermissions("system:userDept:query")
    @GetMapping(value = "/{userCode}")
    public AjaxResult getInfo(@PathVariable("userCode") String userCode)
    {
        return AjaxResult.success(uucUserDeptService.selectUucUserDeptByUserCode(userCode));
    }

    /**
     * 新增用户组织
     */
    @RequiresPermissions("system:userDept:add")
    @Log(title = "用户组织", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UucUserDept uucUserDept)
    {
        return toAjax(uucUserDeptService.insertUucUserDept(uucUserDept));
    }

    /**
     * 修改用户组织
     */
    @RequiresPermissions("system:userDept:edit")
    @Log(title = "用户组织", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UucUserDept uucUserDept)
    {
        return toAjax(uucUserDeptService.updateUucUserDept(uucUserDept));
    }

    /**
     * 删除用户组织
     */
    @RequiresPermissions("system:userDept:remove")
    @Log(title = "用户组织", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userCodes}")
    public AjaxResult remove(@PathVariable String[] userCodes)
    {
        return toAjax(uucUserDeptService.deleteUucUserDeptByUserCodes(userCodes));
    }
}
