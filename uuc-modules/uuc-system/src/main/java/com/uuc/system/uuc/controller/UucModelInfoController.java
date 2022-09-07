package com.uuc.system.uuc.controller;

import com.uuc.common.core.domain.R;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.core.web.controller.BaseController;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.core.web.page.TableDataInfo;
import com.uuc.common.log.annotation.Log;
import com.uuc.common.log.enums.BusinessType;
import com.uuc.common.security.annotation.InnerAuth;
import com.uuc.common.security.annotation.RequiresPermissions;
import com.uuc.system.api.domain.SysRole;
import com.uuc.system.api.domain.UucModelInfo;
import com.uuc.system.api.model.LoginUser;
import com.uuc.system.api.model.UucUserInfo;
import com.uuc.system.uuc.service.IUucModelInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 模块管理Controller
 * 
 * @author uuc
 * @date 2022-04-12
 */
@RestController
@RequestMapping("/model")
public class UucModelInfoController extends BaseController
{
    @Autowired
    private IUucModelInfoService uucModelInfoService;

    /**
     * 查询模块管理列表
     */
    //@RequiresPermissions("system:model:list")
    @GetMapping("/list")
    public TableDataInfo list(UucModelInfo uucModelInfo)
    {
        startPage();
        List<UucModelInfo> list = uucModelInfoService.selectUucModelInfoList(uucModelInfo);
        return getDataTable(list);
    }

    /**
     * 查询模块管理列表
     */
    //@RequiresPermissions("system:model:list")
    @GetMapping("/getAllList")
    public AjaxResult getAllList(UucModelInfo uucModelInfo)
    {
        List<UucModelInfo> list = uucModelInfoService.selectUucModelInfoList(uucModelInfo);
        return AjaxResult.success(list);
    }

//    /**
//     * 导出模块管理列表
//     */
//    @RequiresPermissions("system:model:export")
//    @Log(title = "模块管理", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, UucModelInfo uucModelInfo)
//    {
//        List<UucModelInfo> list = uucModelInfoService.selectUucModelInfoList(uucModelInfo);
//        ExcelUtil<UucModelInfo> util = new ExcelUtil<UucModelInfo>(UucModelInfo.class);
//        util.exportExcel(response, list, "模块管理数据");
//    }

    /**
     * 获取模块管理详细信息
     */
    @RequiresPermissions("system:model:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(uucModelInfoService.selectUucModelInfoById(id));
    }

    /**
     * 新增模块管理
     */
    @RequiresPermissions("system:model:add")
    @Log(title = "模块管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UucModelInfo uucModelInfo)
    {
        return toAjax(uucModelInfoService.insertUucModelInfo(uucModelInfo));
    }

    /**
     * 修改模块管理
     */
    @RequiresPermissions("system:model:edit")
    @Log(title = "模块管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UucModelInfo uucModelInfo)
    {
        return toAjax(uucModelInfoService.updateUucModelInfo(uucModelInfo));
    }

    /**
     * 删除模块管理
     */
    @RequiresPermissions("system:model:remove")
    @Log(title = "模块管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(uucModelInfoService.deleteUucModelInfoByIds(ids));
    }
    /**
     * 根据appId查询模块信息
     */

    /**
     * 获取当前用户信息
     */
    @InnerAuth
    @GetMapping("/client/{clientId}")
    public R<UucModelInfo> info(@PathVariable("clientId") String clientId) {
        UucModelInfo uucModelInfo = uucModelInfoService.selectUucModelInfoByCLientId(clientId);
        if (StringUtils.isNull(uucModelInfo)) {
            return R.fail("模块不存在");
        }
        return R.ok(uucModelInfo);
    }
}
