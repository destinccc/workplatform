package com.uuc.system.uuc.controller;

import java.util.List;
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
import com.uuc.system.api.model.UucLoginAccount;
import com.uuc.system.uuc.service.IUucLoginAccountService;
import com.uuc.common.core.utils.poi.ExcelUtil;
import com.uuc.common.core.web.controller.BaseController;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.core.web.page.TableDataInfo;
import com.uuc.common.log.annotation.Log;
import com.uuc.common.log.enums.BusinessType;
import com.uuc.common.security.annotation.RequiresPermissions;


/**
 * 登录用户Controller
 *
 * @author uuc
 * @date 2022-04-01
 */
@RestController
@RequestMapping("/account")
public class UucLoginAccountController extends BaseController
{
    @Autowired
    private IUucLoginAccountService uucLoginAccountService;

    /**
     * 查询登录用户列表
     */
    @RequiresPermissions("system:account:list")
    @GetMapping("/list")
    public TableDataInfo list(UucLoginAccount uucLoginAccount)
    {
        startPage();
        List<UucLoginAccount> list = uucLoginAccountService.selectUucLoginAccountList(uucLoginAccount);
        return getDataTable(list);
    }

    /**
     * 导出登录用户列表
     */
    @RequiresPermissions("system:account:export")
    @Log(title = "登录用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UucLoginAccount uucLoginAccount)
    {
        List<UucLoginAccount> list = uucLoginAccountService.selectUucLoginAccountList(uucLoginAccount);
        ExcelUtil<UucLoginAccount> util = new ExcelUtil<UucLoginAccount>(UucLoginAccount.class);
        util.exportExcel(response, list, "登录用户数据");
    }

    /**
     * 获取登录用户详细信息
     */
    @RequiresPermissions("system:account:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(uucLoginAccountService.selectUucLoginAccountById(id));
    }

    /**
     * 新增登录用户
     */
    @RequiresPermissions("system:account:add")
    @Log(title = "登录用户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UucLoginAccount uucLoginAccount)
    {
        return toAjax(uucLoginAccountService.insertUucLoginAccount(uucLoginAccount));
    }

    /**
     * 修改登录用户
     */
    @RequiresPermissions("system:account:edit")
    @Log(title = "登录用户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UucLoginAccount uucLoginAccount)
    {
        return toAjax(uucLoginAccountService.updateUucLoginAccount(uucLoginAccount));
    }

    /**
     * 删除登录用户
     */
    @RequiresPermissions("system:account:remove")
    @Log(title = "登录用户", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(uucLoginAccountService.deleteUucLoginAccountByIds(ids));
    }
}
