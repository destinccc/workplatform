package com.uuc.system.uuc.controller;

import java.util.List;

import com.uuc.common.core.domain.R;
import com.uuc.common.core.web.controller.BaseController;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.core.web.page.TableDataInfo;
import com.uuc.common.log.annotation.Log;
import com.uuc.common.log.enums.BusinessType;
import com.uuc.common.security.annotation.InnerAuth;
import com.uuc.common.security.annotation.RequiresPermissions;
import com.uuc.system.service.impl.UucApiVersionService;
import com.uuc.system.uuc.domain.UucApiVersion;
import io.swagger.annotations.Api;
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


/**
 * API版本控制
 */
@RestController
@RequestMapping("/version/api")
@Api(tags = "API版本控制")
public class UucApiVersionController extends BaseController {
    @Autowired
    private UucApiVersionService uucApiVersionService;


    @RequiresPermissions("system:version:list")
    @GetMapping("/list")
    @ApiOperation(value = "获取API版本", notes = "获取API版本")
    public TableDataInfo list(UucApiVersion uucApiVersion) {
        startPage();
        List<UucApiVersion> list = uucApiVersionService.selectUucApiVersionList(uucApiVersion);
        return getDataTable(list);
    }


    @GetMapping(value = "/{id}")
    @ApiOperation(value = "查询API版本", notes = "查询API版本")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(uucApiVersionService.selectUucApiVersionById(id));
    }


    @RequiresPermissions("system:version:edit")
    @Log(title = "API版本控制", businessType = BusinessType.INSERT)
    @PostMapping(value = "/add")
    @ApiOperation(value = "新增API版本", notes = "新增API版本")
    public AjaxResult add(@RequestBody UucApiVersion uucApiVersion) {
        return toAjax(uucApiVersionService.insertUucApiVersion(uucApiVersion));
    }

    @RequiresPermissions("system:version:edit")
    @Log(title = "API版本控制", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/status")
    @ApiOperation(value = "修改API版本激活状态", notes = "修改API版本激活状态")
    public AjaxResult status(@RequestBody UucApiVersion uucApiVersion) {
        uucApiVersionService.updateUucApiVersionStatus(uucApiVersion);
        return AjaxResult.success();
    }


    @RequiresPermissions("system:version:edit")
    @Log(title = "API版本控制 ", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/update")
    @ApiOperation(value = "修改API版本", notes = "修改API版本")
    public AjaxResult edit(@RequestBody UucApiVersion uucApiVersion) {
        return toAjax(uucApiVersionService.updateUucApiVersion(uucApiVersion));
    }


    @RequiresPermissions("system:version:edit")
    @Log(title = "API版本控制 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @ApiOperation(value = "删除API版本", notes = "删除API版本")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(uucApiVersionService.deleteUucApiVersionByIds(ids));
    }


    @GetMapping("/effective")
    @InnerAuth
    @ApiOperation(value = "查询有效API版本", notes = "查询有效API版本")
    public R<List<String>> effective() {
        return R.ok(uucApiVersionService.getEffectiveVersion());
    }
}
