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
import com.uuc.system.uuc.domain.UucPostInfo;
import com.uuc.system.uuc.service.IUucPostInfoService;
import com.uuc.common.core.utils.poi.ExcelUtil;
import com.uuc.common.core.web.controller.BaseController;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.core.web.page.TableDataInfo;
import com.uuc.common.log.annotation.Log;
import com.uuc.common.log.enums.BusinessType;
import com.uuc.common.security.annotation.RequiresPermissions;


/**
 * 职位信息Controller
 * 
 * @author uuc
 * @date 2022-04-01
 */
@RestController
@RequestMapping("/postInfo")
public class UucPostInfoController extends BaseController
{
    @Autowired
    private IUucPostInfoService uucPostInfoService;

    /**
     * 查询职位信息列表
     */
    @RequiresPermissions("system:postInfo:list")
    @GetMapping("/list")
    public TableDataInfo list(UucPostInfo uucPostInfo)
    {
        startPage();
        List<UucPostInfo> list = uucPostInfoService.selectUucPostInfoList(uucPostInfo);
        return getDataTable(list);
    }

    /**
     * 导出职位信息列表
     */
    @RequiresPermissions("system:postInfo:export")
    @Log(title = "职位信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UucPostInfo uucPostInfo)
    {
        List<UucPostInfo> list = uucPostInfoService.selectUucPostInfoList(uucPostInfo);
        ExcelUtil<UucPostInfo> util = new ExcelUtil<UucPostInfo>(UucPostInfo.class);
        util.exportExcel(response, list, "职位信息数据");
    }

    /**
     * 获取职位信息详细信息
     */
    @RequiresPermissions("system:postInfo:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(uucPostInfoService.selectUucPostInfoById(id));
    }

    /**
     * 新增职位信息
     */
    @RequiresPermissions("system:postInfo:add")
    @Log(title = "职位信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UucPostInfo uucPostInfo)
    {
        return toAjax(uucPostInfoService.insertUucPostInfo(uucPostInfo));
    }

    /**
     * 修改职位信息
     */
    @RequiresPermissions("system:postInfo:edit")
    @Log(title = "职位信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UucPostInfo uucPostInfo)
    {
        return toAjax(uucPostInfoService.updateUucPostInfo(uucPostInfo));
    }

    /**
     * 删除职位信息
     */
    @RequiresPermissions("system:postInfo:remove")
    @Log(title = "职位信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(uucPostInfoService.deleteUucPostInfoByIds(ids));
    }
}
