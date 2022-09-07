package com.uuc.system.controller;


import com.uuc.common.core.constant.ClientConstants;
import com.uuc.common.core.constant.UserConstants;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.core.utils.bean.BeanUtils;
import com.uuc.common.core.web.controller.BaseController;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.common.log.annotation.Log;
import com.uuc.common.log.enums.BusinessType;
import com.uuc.common.security.annotation.InnerAuth;
import com.uuc.common.security.annotation.RequiresPermissions;
import com.uuc.common.security.utils.SecurityUtils;
import com.uuc.system.api.domain.MenuVo;
import com.uuc.system.api.model.SysMenu;
import com.uuc.system.api.model.UucUserInfo;
import com.uuc.system.service.impl.SysMenuService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.compress.archivers.sevenz.CLI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 菜单信息
 *
 * @author uuc
 */
@RestController
@RequestMapping("/menu")
public class SysMenuController extends BaseController
{
    @Autowired
    private SysMenuService menuService;

    /**
     * 获取菜单列表
     */
    @RequiresPermissions("system:menu:list")
    @GetMapping("/list")
    public AjaxResult list(SysMenu menu)
    {
        if (StringUtils.isEmpty(menu.getClientId()))
        {
            return AjaxResult.error("请设置模块编码！");
        }
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        return AjaxResult.success(menus);
    }

    /**
     * 获取菜单列表
     * (对外API接口)
     */
//    @RequiresPermissions("system:menu:list")
    @GetMapping("/listAll")
    public AjaxResult listAll(SysMenu menu)
    {
        if (StringUtils.isEmpty(menu.getClientId()))
        {
            return AjaxResult.error("请设置模块编码！");
        }
        Long userId = SecurityUtils.getUserId();
        menu.setStatus("0");
        menu.setVisible("0");
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        return AjaxResult.success(menus);
    }
    /**
     * 获取菜单列表
     * (对外API接口)
     */
//    @RequiresPermissions("system:menu:list")
    @GetMapping("/listAllByUserId")
    public AjaxResult listAll(MenuVo menuVo)
    {
        if (StringUtils.isEmpty(menuVo.getClientId())||StringUtils.isEmpty(menuVo.getUserId()))
        {
            return AjaxResult.error("缺少请求参数！");
        }
        //Long userId = SecurityUtils.getUserId();
        SysMenu sysMenu=new SysMenu();
        sysMenu.setStatus("0");
        sysMenu.setVisible("0");
        sysMenu.setClientId(menuVo.getClientId());
        List<SysMenu> menus = menuService.selectMenuList(sysMenu, Long.parseLong(menuVo.getUserId()));
        return AjaxResult.success(menus);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @RequiresPermissions("system:menu:query")
    @GetMapping(value = "/{menuId}")
    public AjaxResult getInfo(@PathVariable Long menuId)
    {
        return AjaxResult.success(menuService.selectMenuById(menuId));
    }

    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysMenu menu)
    {
//        Long userId = SecurityUtils.getUserId();
        // 固定查询所有菜单下拉树
        List<SysMenu> menus = menuService.selectMenuList(menu, 1L);
        return AjaxResult.success(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public AjaxResult roleMenuTreeselect(@PathVariable("roleId") Long roleId)
    {
//        Long userId = SecurityUtils.getUserId();
        // 固定查询所有菜单下拉树
        List<SysMenu> menus = menuService.selectMenuList(1L);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeSelect(menus));
        return ajax;
    }

    /**
     * 新增菜单
     */
    @RequiresPermissions("system:menu:add")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysMenu menu)
    {
        if (StringUtils.isEmpty(menu.getClientId())) {
            return AjaxResult.error("新增菜单'" + menu.getMenuName() + "'失败，模块编码必填");
        }
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu)))
        {
            return AjaxResult.error("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath()))
        {
            return AjaxResult.error("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        menu.setCreateBy(SecurityUtils.getUsername());
        return toAjax(menuService.insertMenu(menu));
    }

    /**
     * 修改菜单
     */
    @RequiresPermissions("system:menu:edit")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysMenu menu)
    {
        if (StringUtils.isEmpty(menu.getClientId())) {
            return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，模块编码必填");
        }
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu)))
        {
            return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath()))
        {
            return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        else if (menu.getMenuId().equals(menu.getParentId()))
        {
            return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        menu.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(menuService.updateMenu(menu));
    }

    /**
     * 删除菜单
     */
    @RequiresPermissions("system:menu:remove")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuId}")
    public AjaxResult remove(@PathVariable("menuId") Long menuId)
    {
        if (menuService.hasChildByMenuId(menuId))
        {
            return AjaxResult.error("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId))
        {
            return AjaxResult.error("菜单已分配,不允许删除");
        }
        return toAjax(menuService.deleteMenuById(menuId));
    }

    /**
     * 获取路由信息
     * (给门户头部用)
     * @return 路由信息
     */
    @GetMapping("getUucRouters")
    public AjaxResult getUucRouters()
    {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectUucHeaderMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }

    /**
     * 获取路由信息
     * @return 路由信息
     */
    @GetMapping("getRouters")
    @ApiOperation(value = "权限菜单", notes = "权限菜单")
    public AjaxResult getRouters(@RequestParam String clientId)
    {
        if (StringUtils.isEmpty(clientId)) {
            return AjaxResult.error("clientId must not be null");
        }

        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus;

        SysMenu menu = new SysMenu();
        menu.setClientId(clientId);
        menu.setStatus("0");
        menu.setVisible("0");
        menu.setMenuType("M,C");
        if (!ClientConstants.MODE_CODE_CMDB.equals(clientId)) {
            menus = menuService.selectTreeMenuList(menu, userId);
        } else {
//            UucUserInfo uucUserInfo = SecurityUtils.getLoginUser().getUucUserInfo();
            // Todo 若用户创建了模型 , 给默认数据仓库权限
//        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
            menu.setAttribution("0");
            Map<String, Object> params = menu.getParams();
            if (MapUtils.isEmpty(params)){
                menu.setParams(params);
            }
//        params.put("extralFilter"," ")
            menus = menuService.selectTreeMenuList(menu, userId);
//        menus.sort(Comparator.comparing(sysMenuSort -> sysMenuSort.getOrderNum()));
        }
        return AjaxResult.success(menuService.buildMenus(menus));
    }


    /**
     * 内部同步菜单接口
     */
    @InnerAuth
    @PostMapping("/sync")
    @ApiOperation(value = "同步CMDB分类模型菜单信息", notes = "同步CMDB分类模型菜单信息")
    public AjaxResult sync(@Validated @RequestBody SysMenu sysMenuVo)
    {
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(sysMenuVo,sysMenu);
        sysMenu.setAttribution("1");
        sysMenu.setClientId(ClientConstants.MODE_CODE_CMDB);
        int result = menuService.syncMenu(sysMenu);
        Long menuId = sysMenu.getMenuId();
        return result>0?AjaxResult.success(menuId):AjaxResult.error();
    }


    @InnerAuth
    @PostMapping("/syncUpdate")
    @ApiOperation(value = "同步修改菜单信息", notes = "内部同步修改菜单信息")
    public AjaxResult syncUpdate(@RequestBody SysMenu sysMenuVo)
    {
        SysMenu sysMenu = new SysMenu();
        org.springframework.beans.BeanUtils.copyProperties(sysMenuVo,sysMenu);
        return toAjax(menuService.updateCmdbMenu(sysMenu));
    }


    /**
     * 同步删除菜单信息
     */
    @InnerAuth
    @GetMapping("/syncDelete/{menuId}")
    @ApiOperation(value = "同步删除菜单信息", notes = "内部同步删除菜单信息")
    public AjaxResult syncDelete(@PathVariable("menuId") Long menuId)
    {
        //如果时菜单类型的菜单，需要同时将所属的权限记录(F类型菜单)删除
        SysMenu cond = new SysMenu();
        cond.setParentId(menuId);
        cond.setMenuType("F");//按钮类型
        List<SysMenu> childMenu = menuService.selectMenuByCond(cond);
        if(Objects.nonNull(childMenu)&&childMenu.size()>0){
            for (SysMenu menu : childMenu) {
                menuService.deleteMenuById(menu.getMenuId());
            }
        }
        return toAjax(menuService.deleteMenuById(menuId));
    }


    @InnerAuth
    @PostMapping("/listByCond")
    @ApiOperation(value = "内部查询菜单信息", notes = "内部查询菜单信息")
    public AjaxResult listByCond(@RequestBody SysMenu sysMenuVo)
    {
        SysMenu menu = new SysMenu();
        org.springframework.beans.BeanUtils.copyProperties(sysMenuVo,menu);
        List<SysMenu> menus = menuService.selectMenuByCond(menu);
        Long menuId = menus.stream().findFirst().orElse(new SysMenu()).getMenuId();
        return AjaxResult.success(menuId);
    }


    @GetMapping("/listSysInnerMenus")
    @ApiOperation(value = "CMDB数据仓库", notes = "CMDB数据仓库")
    public AjaxResult listSysInnerMenus()
    {
        Long userId = SecurityUtils.getUserId();
        List menus = menuService.selectMenuListTree(userId);
        return AjaxResult.success(menus);
    }
}
