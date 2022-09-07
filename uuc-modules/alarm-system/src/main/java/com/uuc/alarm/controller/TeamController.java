package com.uuc.alarm.controller;

import com.uuc.alarm.domain.Team;
import com.uuc.alarm.service.ITeamService;
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
@RequestMapping("/team")
public class TeamController extends BaseController {
    private final ITeamService teamService;

    public TeamController(ITeamService teamService) {
        this.teamService = teamService;
    }

    /**
     * 查询【请填写功能名称】列表
     */
    @RequiresPermissions("system:team:list")
    @GetMapping("/list")
    public TableDataInfo list(Team team) {
        startPage();
        List<Team> list = teamService.selectTeamList(team);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @RequiresPermissions("system:team:export")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Team team) {
        List<Team> list = teamService.selectTeamList(team);
        ExcelUtil<Team> util = new ExcelUtil<>(Team.class);
        util.exportExcel(response, list, "【请填写功能名称】数据");
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    @RequiresPermissions("system:team:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id) {
        return AjaxResult.success(teamService.selectTeamById(id));
    }

    /**
     * 新增【请填写功能名称】
     */
    @RequiresPermissions("system:team:add")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Team team) {
        return toAjax(teamService.insertTeam(team));
    }

    /**
     * 修改【请填写功能名称】
     */
    @RequiresPermissions("system:team:edit")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Team team) {
        return toAjax(teamService.updateTeam(team));
    }

    /**
     * 删除【请填写功能名称】
     */
    @RequiresPermissions("system:team:remove")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids) {
        return toAjax(teamService.deleteTeamByIds(ids));
    }
}
