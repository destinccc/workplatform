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
import com.uuc.system.uuc.domain.UucUserPost;
import com.uuc.system.uuc.service.IUucUserPostService;
import com.uuc.common.core.utils.poi.ExcelUtil;
import com.uuc.common.core.web.controller.BaseController;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.core.web.page.TableDataInfo;
import com.uuc.common.log.annotation.Log;
import com.uuc.common.log.enums.BusinessType;
import com.uuc.common.security.annotation.RequiresPermissions;
/**
 * 用户职位关联Controller
 * 
 * @author uuc
 * @date 2022-04-01
 */
@RestController
@RequestMapping("/userPost")
public class UucUserPostController extends BaseController
{
    @Autowired
    private IUucUserPostService uucUserPostService;

    /**
     * 查询用户职位关联列表
     */
    @RequiresPermissions("system:userPost:list")
    @GetMapping("/list")
    public TableDataInfo list(UucUserPost uucUserPost)
    {
        startPage();
        List<UucUserPost> list = uucUserPostService.selectUucUserPostList(uucUserPost);
        return getDataTable(list);
    }

    /**
     * 导出用户职位关联列表
     */
    @RequiresPermissions("system:userPost:export")
    @Log(title = "用户职位关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UucUserPost uucUserPost)
    {
        List<UucUserPost> list = uucUserPostService.selectUucUserPostList(uucUserPost);
        ExcelUtil<UucUserPost> util = new ExcelUtil<UucUserPost>(UucUserPost.class);
        util.exportExcel(response, list, "用户职位关联数据");
    }

    /**
     * 获取用户职位关联详细信息
     */
    @RequiresPermissions("system:userPost:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(uucUserPostService.selectUucUserPostById(id));
    }

    /**
     * 新增用户职位关联
     */
    @RequiresPermissions("system:userPost:add")
    @Log(title = "用户职位关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UucUserPost uucUserPost)
    {
        return toAjax(uucUserPostService.insertUucUserPost(uucUserPost));
    }

    /**
     * 修改用户职位关联
     */
    @RequiresPermissions("system:userPost:edit")
    @Log(title = "用户职位关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UucUserPost uucUserPost)
    {
        return toAjax(uucUserPostService.updateUucUserPost(uucUserPost));
    }

    /**
     * 删除用户职位关联
     */
    @RequiresPermissions("system:userPost:remove")
    @Log(title = "用户职位关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(uucUserPostService.deleteUucUserPostByIds(ids));
    }
}
