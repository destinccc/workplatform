package com.uuc.system.uuc.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.core.web.controller.BaseController;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.log.annotation.Log;
import com.uuc.common.log.enums.BusinessType;
import com.uuc.common.security.annotation.InnerAuth;
import com.uuc.common.security.annotation.RequiresPermissions;
import com.uuc.common.security.utils.SecurityUtils;
import com.uuc.system.uuc.service.impl.UucDeptInfoService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.uuc.system.api.model.UucDeptInfo;


/**
 * 组织信息Controller
 *
 * @author uuc
 * @date 2022-04-01
 */
@RestController
@RequestMapping("/deptInfo")
public class UucDeptInfoController extends BaseController
{
    @Autowired
    private UucDeptInfoService uucDeptInfoService;

    /**
     * 查询组织信息列表
     */
    @RequiresPermissions("system:deptInfo:list")
    @GetMapping("/list")
    public AjaxResult list(UucDeptInfo uucDeptInfo)
    {
        List<UucDeptInfo> list = uucDeptInfoService.selectUucDeptInfoList(uucDeptInfo);
        return AjaxResult.success(list);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @RequiresPermissions("system:deptInfo:list")
    @GetMapping("/list/exclude/{deptId}")
    public AjaxResult excludeChild(@PathVariable(value = "deptId", required = false) Long deptId)
    {
        List<UucDeptInfo> depts = uucDeptInfoService.selectUucDeptInfoList(new UucDeptInfo());
        Iterator<UucDeptInfo> it = depts.iterator();
        while (it.hasNext())
        {
            UucDeptInfo d = (UucDeptInfo) it.next();
            if (d.getId().intValue() == deptId
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + ""))
            {
                it.remove();
            }
        }
        return AjaxResult.success(depts);
    }

    /**
     * 获取组织信息详细信息
     */
    @RequiresPermissions("system:deptInfo:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return AjaxResult.success(uucDeptInfoService.selectUucDeptInfoById(id));
    }

    /**
     * 获取部门下拉树列表
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(UucDeptInfo dept)
    {
        List<UucDeptInfo> depts = uucDeptInfoService.selectUucDeptInfoList(dept);
        return AjaxResult.success(uucDeptInfoService.buildDeptTreeSelect(depts));
    }

    /**
     * 新增组织信息
     */
    @RequiresPermissions("system:deptInfo:add")
    @Log(title = "组织信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UucDeptInfo uucDeptInfo)
    {
        return toAjax(uucDeptInfoService.insertUucDeptInfo(uucDeptInfo));
    }

    /**
     * 修改组织信息
     */
    @RequiresPermissions("system:deptInfo:edit")
    @Log(title = "组织信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UucDeptInfo uucDeptInfo)
    {
        return toAjax(uucDeptInfoService.updateUucDeptInfo(uucDeptInfo));
    }

    /**
     * 删除组织信息
     */
    @RequiresPermissions("system:deptInfo:remove")
    @Log(title = "组织信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable String id)
    {
        return toAjax(uucDeptInfoService.deleteUucDeptInfoById(Long.valueOf(id)));
    }





    @InnerAuth
    @GetMapping("/initUpdateDeptLevel")
    @ApiOperation(value ="全量更新组织level", notes = "全量更新组织level")
    public AjaxResult initUpdateDeptLevel()
    {
        return AjaxResult.success(uucDeptInfoService.initUpdateDeptLevel());
    }

    @InnerAuth
    @GetMapping("/selectAllDepts")
    @ApiOperation(value ="获取全量组织", notes = "获取全量组织")
    public AjaxResult selectAllDepts()
    {
        return AjaxResult.success(uucDeptInfoService.selectAllDepts());
    }
}
