package com.uuc.system.uuc.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.core.utils.poi.ExcelUtil;
import com.uuc.common.core.web.controller.BaseController;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.core.web.page.TableDataInfo;
import com.uuc.common.log.annotation.Log;
import com.uuc.common.log.enums.BusinessType;
import com.uuc.common.security.annotation.RequiresPermissions;
import com.uuc.common.security.utils.SecurityUtils;
import com.uuc.system.api.model.SysMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.uuc.system.uuc.domain.UucUserVisit;
import com.uuc.system.uuc.service.IUucUserVisitService;


/**
 * 用户菜单访问记录Controller
 * 
 * @author uuc
 * @date 2022-04-19
 */
@RestController
@RequestMapping("/userMenuVisit")
public class UucUserVisitController extends BaseController
{
    @Autowired
    private IUucUserVisitService uucUserVisitService;

    /**
     * 查询用户菜单访问记录列表
     */
    @RequiresPermissions("system:userMenuVisit:list")
    @GetMapping("/list")
    public TableDataInfo list(UucUserVisit uucUserVisit)
    {
        startPage();
        List<UucUserVisit> list = uucUserVisitService.selectUucUserVisitList(uucUserVisit);
        return getDataTable(list);
    }

    /**
     * 导出用户菜单访问记录列表
     */
    @RequiresPermissions("system:userMenuVisit:export")
    @Log(title = "用户菜单访问记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UucUserVisit uucUserVisit)
    {
        List<UucUserVisit> list = uucUserVisitService.selectUucUserVisitList(uucUserVisit);
        ExcelUtil<UucUserVisit> util = new ExcelUtil<UucUserVisit>(UucUserVisit.class);
        util.exportExcel(response, list, "用户菜单访问记录数据");
    }

    /**
     * 获取用户菜单访问记录详细信息
     */
    @RequiresPermissions("system:userMenuVisit:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(uucUserVisitService.selectUucUserVisitById(id));
    }

    /**
     * 新增用户菜单访问记录
     */
    @RequiresPermissions("system:userMenuVisit:add")
    @Log(title = "用户菜单访问记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UucUserVisit uucUserVisit)
    {
        return toAjax(uucUserVisitService.insertUucUserVisit(uucUserVisit));
    }

    /**
     * 修改用户菜单访问记录
     */
    @RequiresPermissions("system:userMenuVisit:edit")
    @Log(title = "用户菜单访问记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UucUserVisit uucUserVisit)
    {
        return toAjax(uucUserVisitService.updateUucUserVisit(uucUserVisit));
    }

    /**
     * 删除用户菜单访问记录
     */
    @RequiresPermissions("system:userMenuVisit:remove")
    @Log(title = "用户菜单访问记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(uucUserVisitService.deleteUucUserVisitByIds(ids));
    }

    /**
     * 用户菜单访问记录,查询最近访问的8个菜单，按时间降序
     */
    @Log(title = "用户菜单访问记录", businessType = BusinessType.DELETE)
    @GetMapping("/getRecentVisit")
    public AjaxResult getRecentVisit()
    {
        Long userCode = SecurityUtils.getUserId();
        List<SysMenu> sysMenus = uucUserVisitService.selectUucUserRecentVisit(userCode);
        return AjaxResult.success(sysMenus);
    }
    /**
     * 保存用户菜单访问记录
     */
    @Log(title = "用户菜单访问记录", businessType = BusinessType.DELETE)
    @PostMapping("/saveRecentVisit")
    public AjaxResult saveRecentVisit(@RequestBody UucUserVisit uucUserVisit)
    {
        if(uucUserVisit==null|| uucUserVisit.getMenuId()==null){
            return AjaxResult.error("缺少参数");
        }
        Long userCode = SecurityUtils.getUserId();
        uucUserVisit.setUserId(userCode);
        return AjaxResult.success(uucUserVisitService.insertUucUserVisit(uucUserVisit));
    }
}
