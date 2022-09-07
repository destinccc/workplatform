package com.uuc.alarm.controller;

import com.uuc.alarm.domain.UserNosys;
import com.uuc.alarm.service.IUserNosysService;
import com.uuc.common.core.utils.poi.ExcelUtil;
import com.uuc.common.core.web.controller.BaseController;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.core.web.page.TableDataInfo;
import com.uuc.common.log.annotation.Log;
import com.uuc.common.log.enums.BusinessType;
import com.uuc.common.security.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 【请填写功能名称】Controller
 *
 * @author uuc
 * @date 2022-08-01
 */
@RestController
@RequestMapping("/nosys")
public class UserNosysController extends BaseController {
    private final IUserNosysService userNosysService;

    public UserNosysController(IUserNosysService userNosysService) {
        this.userNosysService = userNosysService;
    }

    /**
     * 查询【请填写功能名称】列表
     */
    @RequiresPermissions("system:nosys:list")
    @GetMapping("/list")
    public TableDataInfo list(UserNosys userNosys) {
        startPage();
        List<UserNosys> list = userNosysService.selectUserNosysList(userNosys);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @RequiresPermissions("system:nosys:export")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserNosys userNosys) {
        List<UserNosys> list = userNosysService.selectUserNosysList(userNosys);
        ExcelUtil<UserNosys> util = new ExcelUtil<>(UserNosys.class);
        util.exportExcel(response, list, "【请填写功能名称】数据");
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    @RequiresPermissions("system:nosys:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id) {
        return AjaxResult.success(userNosysService.selectUserNosysById(id));
    }

    /**
     * 新增【请填写功能名称】
     */
    @RequiresPermissions("system:nosys:add")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserNosys userNosys) {
        return toAjax(userNosysService.insertUserNosys(userNosys));
    }

    /**
     * 修改【请填写功能名称】
     */
    @RequiresPermissions("system:nosys:edit")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserNosys userNosys) {
        return toAjax(userNosysService.updateUserNosys(userNosys));
    }

    /**
     * 删除【请填写功能名称】
     */
    @RequiresPermissions("system:nosys:remove")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids) {
        return toAjax(userNosysService.deleteUserNosysByIds(ids));
    }
}
