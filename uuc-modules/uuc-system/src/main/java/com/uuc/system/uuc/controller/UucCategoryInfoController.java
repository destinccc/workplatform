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
import com.uuc.system.uuc.domain.UucCategoryMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.uuc.system.uuc.domain.UucCategoryInfo;
import com.uuc.system.uuc.service.IUucCategoryInfoService;


/**
 * 类目信息表Controller
 * 
 * @author uuc
 * @date 2022-04-19
 */
@RestController
@RequestMapping("/categoryInfo")
public class UucCategoryInfoController extends BaseController
{
    @Autowired
    private IUucCategoryInfoService uucCategoryInfoService;

    /**
     * 查询类目信息表列表
     */
    @RequiresPermissions("system:categoryInfo:list")
    @GetMapping("/list")
    public TableDataInfo list(UucCategoryInfo uucCategoryInfo)
    {
        startPage();
        List<UucCategoryInfo> list = uucCategoryInfoService.selectUucCategoryInfoList(uucCategoryInfo);
        return getDataTable(list);
    }
    /**
     * 查询类目信息表列表
     */
    @GetMapping("/listInfo")
    public AjaxResult listInfo(UucCategoryInfo uucCategoryInfo)
    {
        List<UucCategoryInfo> list = uucCategoryInfoService.selectUucCategoryInfoListVo(uucCategoryInfo);
        return AjaxResult.success(list);
    }

    /**
     * 导出类目信息表列表
     */
    @RequiresPermissions("system:categoryInfo:export")
    @Log(title = "类目信息表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UucCategoryInfo uucCategoryInfo)
    {
        List<UucCategoryInfo> list = uucCategoryInfoService.selectUucCategoryInfoList(uucCategoryInfo);
        ExcelUtil<UucCategoryInfo> util = new ExcelUtil<UucCategoryInfo>(UucCategoryInfo.class);
        util.exportExcel(response, list, "类目信息表数据");
    }

    /**
     * 获取类目信息表详细信息
     */
    @RequiresPermissions("system:categoryInfo:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(uucCategoryInfoService.selectUucCategoryInfoById(id));
    }

    /**
     * 新增类目信息表
     */
    @RequiresPermissions("system:categoryInfo:add")
    @Log(title = "类目信息表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UucCategoryInfo uucCategoryInfo)
    {

        return toAjax(uucCategoryInfoService.insertUucCategoryInfo(uucCategoryInfo));
    }

    /**
     * 修改类目信息表
     */
    @RequiresPermissions("system:categoryInfo:edit")
    @Log(title = "类目信息表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UucCategoryInfo uucCategoryInfo)
    {
        return toAjax(uucCategoryInfoService.updateUucCategoryInfo(uucCategoryInfo));
    }

    /**
     * 删除类目信息表
     */
    @RequiresPermissions("system:categoryInfo:remove")
    @Log(title = "类目信息表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(uucCategoryInfoService.deleteUucCategoryInfoByIds(ids));
    }
}
