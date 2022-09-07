package com.uuc.system.uuc.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.uuc.common.core.constant.HttpStatus;
import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.security.annotation.InnerAuth;
import com.uuc.common.security.utils.SecurityUtils;
import com.uuc.system.api.RemoteResourceService;
import com.uuc.system.api.model.UucProject;
import com.uuc.system.api.model.cmdb.ProjectDto;
import com.uuc.system.uuc.service.impl.UucProjectService;
import com.uuc.system.uuc.service.impl.UucProjectUserService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.uuc.system.api.model.UucProjectUser;
import com.uuc.common.core.utils.poi.ExcelUtil;
import com.uuc.common.core.web.controller.BaseController;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.core.web.page.TableDataInfo;
import com.uuc.common.log.annotation.Log;
import com.uuc.common.log.enums.BusinessType;
import com.uuc.common.security.annotation.RequiresPermissions;

/**
 * 用户项目Controller
 *
 * @author uuc
 * @date 2022-04-01
 */
@RestController
@RequestMapping("/userProject")
public class UucProjectUserController extends BaseController {
    @Autowired
    private UucProjectUserService uucProjectUserService;

    @Autowired
    private UucProjectService uucProjectService;

    @Autowired
    private RemoteResourceService remoteResourceService;

    /**
     * 查询用户项目列表
     */
    @GetMapping("/getCurrentUserProjectCounts")
    public AjaxResult getCurrentUserProjectCounts() {
//        if(1 == SecurityUtils.getUserId()) {
//            // 超管特殊处理，查全部项目
//            return AjaxResult.success(uucProjectService.selectUucProjectList(new UucProject()).size());
//        }
//        UucProjectUser uucProjectUser = new UucProjectUser();
//        uucProjectUser.setUserCode(String.valueOf(SecurityUtils.getUserId()));
//        return AjaxResult.success(uucProjectUserService.selectUucProjectUserList(uucProjectUser).size());
        int count = 0;
        AjaxResult result = remoteResourceService.userProjects(String.valueOf(SecurityUtils.getUserId()), SecurityConstants.INNER);
        if (!HttpStatus.SUCCESS_CODE.equals(result.getCodeStatus())){
            return result;
        }
        if (result.getData() != null) {
            List<ProjectDto> projectList = (List<ProjectDto>) result.getData();
            return AjaxResult.success(projectList.size());
        }
        return AjaxResult.success(count);
    }

    /**
     * 查询用户项目列表
     */
    @RequiresPermissions("system:userProject:list")
    @GetMapping("/list")
    public TableDataInfo list(UucProjectUser uucProjectUser) {
        startPage();
        List<UucProjectUser> list = uucProjectUserService.selectUucProjectUserList(uucProjectUser);
        return getDataTable(list);
    }

    /**
     * 导出用户项目列表
     */
    @RequiresPermissions("system:userProject:export")
    @Log(title = "用户项目", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UucProjectUser uucProjectUser) {
        List<UucProjectUser> list = uucProjectUserService.selectUucProjectUserList(uucProjectUser);
        ExcelUtil<UucProjectUser> util = new ExcelUtil<UucProjectUser>(UucProjectUser.class);
        util.exportExcel(response, list, "用户项目数据");
    }

    /**
     * 获取用户项目详细信息
     */
    @RequiresPermissions("system:userProject:query")
    @GetMapping(value = "/{userCode}")
    public AjaxResult getInfo(@PathVariable("userCode") String userCode) {
        return AjaxResult.success(uucProjectUserService.selectUucProjectUserByUserCode(userCode));
    }

    /**
     * 新增用户项目
     */
    @RequiresPermissions("system:userProject:add")
    @Log(title = "用户项目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UucProjectUser uucProjectUser) {
        return toAjax(uucProjectUserService.insertUucProjectUser(uucProjectUser));
    }

    /**
     * 修改用户项目
     */
    @RequiresPermissions("system:userProject:edit")
    @Log(title = "用户项目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UucProjectUser uucProjectUser) {
        return toAjax(uucProjectUserService.updateUucProjectUser(uucProjectUser));
    }

    /**
     * 删除用户项目
     */
    @RequiresPermissions("system:userProject:remove")
    @Log(title = "用户项目", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userCodes}")
    public AjaxResult remove(@PathVariable String[] userCodes) {
        return toAjax(uucProjectUserService.deleteUucProjectUserByUserCodes(userCodes));
    }
}
