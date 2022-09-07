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
import com.uuc.system.uuc.domain.UccUserApplicaiton;
import com.uuc.system.uuc.service.IUccUserApplicaitonService;


/**
 * 用户应用关联Controller
 * 
 * @author uuc
 * @date 2022-04-01
 */
@RestController
@RequestMapping("/userApplicaiton")
public class UccUserApplicaitonController extends BaseController
{
    @Autowired
    private IUccUserApplicaitonService uccUserApplicaitonService;

    /**
     * 查询用户应用关联列表
     */
    @RequiresPermissions("system:userApplicaiton:list")
    @GetMapping("/list")
    public TableDataInfo list(UccUserApplicaiton uccUserApplicaiton)
    {
        startPage();
        List<UccUserApplicaiton> list = uccUserApplicaitonService.selectUccUserApplicaitonList(uccUserApplicaiton);
        return getDataTable(list);
    }

    /**
     * 导出用户应用关联列表
     */
    @RequiresPermissions("system:userApplicaiton:export")
    @Log(title = "用户应用关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UccUserApplicaiton uccUserApplicaiton)
    {
        List<UccUserApplicaiton> list = uccUserApplicaitonService.selectUccUserApplicaitonList(uccUserApplicaiton);
        ExcelUtil<UccUserApplicaiton> util = new ExcelUtil<UccUserApplicaiton>(UccUserApplicaiton.class);
        util.exportExcel(response, list, "用户应用关联数据");
    }

    /**
     * 获取用户应用关联详细信息
     */
    @RequiresPermissions("system:userApplicaiton:query")
    @GetMapping(value = "/{userCode}")
    public AjaxResult getInfo(@PathVariable("userCode") String userCode)
    {
        return AjaxResult.success(uccUserApplicaitonService.selectUccUserApplicaitonByUserCode(userCode));
    }

    /**
     * 新增用户应用关联
     */
    @RequiresPermissions("system:userApplicaiton:add")
    @Log(title = "用户应用关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UccUserApplicaiton uccUserApplicaiton)
    {
        return toAjax(uccUserApplicaitonService.insertUccUserApplicaiton(uccUserApplicaiton));
    }

    /**
     * 修改用户应用关联
     */
    @RequiresPermissions("system:userApplicaiton:edit")
    @Log(title = "用户应用关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UccUserApplicaiton uccUserApplicaiton)
    {
        return toAjax(uccUserApplicaitonService.updateUccUserApplicaiton(uccUserApplicaiton));
    }

    /**
     * 删除用户应用关联
     */
    @RequiresPermissions("system:userApplicaiton:remove")
    @Log(title = "用户应用关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userCodes}")
    public AjaxResult remove(@PathVariable String[] userCodes)
    {
        return toAjax(uccUserApplicaitonService.deleteUccUserApplicaitonByUserCodes(userCodes));
    }
}
