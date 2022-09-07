package com.uuc.system.uuc.controller;

import java.util.List;

import com.uuc.common.core.exception.CheckedException;
import com.uuc.common.core.web.page.PageDomain;
import com.uuc.common.core.web.page.TableDataInfo;
import com.uuc.common.core.web.page.TableSupport;
import com.uuc.system.api.model.UucProjectUser;
import com.uuc.system.api.model.UucUserInfo;
import com.uuc.system.uuc.service.impl.UucProjectService;
import com.uuc.system.uuc.service.impl.UucUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.uuc.system.api.model.UucProject;
import com.uuc.common.core.web.controller.BaseController;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.log.annotation.Log;
import com.uuc.common.log.enums.BusinessType;
import com.uuc.common.security.annotation.RequiresPermissions;

/**
 * 项目信息Controller
 *
 * @author uuc
 * @date 2022-04-01
 */
@RestController
@RequestMapping("/project")
public class UucProjectController extends BaseController
{
    @Autowired
    private UucProjectService uucProjectService;

    @Autowired
    private UucUserInfoService uucUserInfoService;

    /**
     * 查询项目信息列表
     */
    @RequiresPermissions("system:project:list")
    @GetMapping("/list")
    public AjaxResult list(UucProject uucProject)
    {
        List<UucProject> list = uucProjectService.selectUucProjectList(uucProject);
        return AjaxResult.success(list);
    }

//    /**
//     * 导出项目信息列表
//     */
//    @RequiresPermissions("system:project:export")
//    @Log(title = "项目信息", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, UucProject uucProject)
//    {
//        List<UucProject> list = uucProjectService.selectUucProjectList(uucProject);
//        ExcelUtil<UucProject> util = new ExcelUtil<UucProject>(UucProject.class);
//        util.exportExcel(response, list, "项目信息数据");
//    }

    /**
     * 获取项目信息详细信息
     */
    @RequiresPermissions("system:project:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(uucProjectService.selectUucProjectById(id));
    }

    /**
     * 获取项目关联人员
     */
    @RequiresPermissions("system:project:query")
    @GetMapping(value = "/relation/{id}")
    public AjaxResult getRelationUserList(@PathVariable("id") Long id)
    {
        List<UucProjectUser> relationUserList = uucProjectService.getRelationUserList(id);
        return AjaxResult.success(relationUserList);
    }
    /**
     * 获取项目关联人员(分页)
     */
    @RequiresPermissions("system:project:query")
    @GetMapping(value = "/relationPage")
    public TableDataInfo getRelationUserPageList(UucProjectUser uucProjectUser)
    {
       /* PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();*/
        List<UucProjectUser> relationUserList = uucProjectService.selectUucProjectUserByCond(uucProjectUser);
        if(relationUserList!=null&&relationUserList.size()>0){
            for(UucProjectUser item:relationUserList){
                UucUserInfo uucUserInfo = uucUserInfoService.selectUucUserInfoById(Long.parseLong(item.getUserCode()));
                if(uucUserInfo!=null){
                    item.setDeptName(uucUserInfo.getDeptName());
                    item.setUserName(uucUserInfo.getUserName());
                    item.setPostName(uucUserInfo.getPostName());
                }
            }
        }
        return getDataTable(relationUserList);
    }

    /**
     * 新增项目信息
     */
    @RequiresPermissions("system:project:add")
    @Log(title = "项目信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UucProject uucProject)
    {
        return toAjax(uucProjectService.insertUucProject(uucProject));
    }

    /**
     * 修改项目信息
     */
    @RequiresPermissions("system:project:edit")
    @Log(title = "项目信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UucProject uucProject)
    {
        return toAjax(uucProjectService.updateUucProject(uucProject));
    }

    /**
     * 删除项目信息
     */
    @RequiresPermissions("system:project:remove")
    @Log(title = "项目信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id)
    {
        if (id == 1) {
            throw new CheckedException("空闲资源池不能删除！");
        }
        return toAjax(uucProjectService.deleteUucProjectById(id));
    }



    /**
     * 修改项目人员
     */
    @RequiresPermissions("system:project:edit")
    @Log(title = "项目信息", businessType = BusinessType.UPDATE)
    @PutMapping("/saveUsers")
    public AjaxResult saveRelationUsers(@RequestBody UucProject uucProject)
    {
        return toAjax(uucProjectService.saveRelationUsers(uucProject));
    }



}
