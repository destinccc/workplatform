package com.uuc.system.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.util.IdUtil;
import com.uuc.common.core.constant.ClientConstants;
import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.utils.DateUtils;
import com.uuc.common.core.utils.LongUtils;
import com.uuc.common.core.utils.uuid.IdUtils;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.system.api.RemoteCmdbService;
import com.uuc.system.api.domain.UucModelInfo;
import com.uuc.system.uuc.service.IUucModelInfoService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uuc.common.core.constant.Constants;
import com.uuc.common.core.constant.UserConstants;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.security.utils.SecurityUtils;
import com.uuc.system.api.domain.SysRole;
import com.uuc.system.api.domain.SysUser;
import com.uuc.system.api.model.SysMenu;
import com.uuc.system.domain.vo.MetaVo;
import com.uuc.system.domain.vo.RouterVo;
import com.uuc.system.domain.vo.TreeSelect;
import com.uuc.system.mapper.SysMenuMapper;
import com.uuc.system.mapper.SysRoleMapper;
import com.uuc.system.mapper.SysRoleMenuMapper;

/**
 * 菜单 业务层处理
 *
 * @author uuc
 */
@Service
public class SysMenuService
{
    public static final String PREMISSION_STRING = "perms[\"{0}\"]";

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Autowired
    private RemoteCmdbService remoteCmdbService;

    @Autowired
    private IUucModelInfoService modelInfoService;

    /**
     * 根据用户查询系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */

    public List<SysMenu> selectMenuList(Long userId)
    {
        return selectMenuList(new SysMenu(), userId);
    }

    /**
     * 根据用户查询系统菜单列表（拼装树结构）
     *
     * @param userId 用户ID
     * @return 菜单列表
     */

    public List selectMenuListTree(Long userId)
    {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setMenuType("M,C");
        sysMenu.setStatus("0");
        sysMenu.setAttribution("1");
        List<SysMenu> menus = selectMenuList(sysMenu, userId);
        SysMenu cond = new SysMenu();
        cond.setMenuName("数据仓库");
        cond.setClientId("cmdb");
        List<SysMenu> sysMenuList = this.selectMenuByCond(cond);
        SysMenu storeMenu = sysMenuList.stream().findFirst().orElse(new SysMenu());
        List<SysMenu> retList = getChildPerms(menus, storeMenu.getMenuId().intValue());
        retList.sort(Comparator.comparing(sysMenuSort -> sysMenuSort.getOrderNum()));
        return transMenu(retList);
    }

    private List transMenu(List<SysMenu> menus){
        List list = new ArrayList();
        if(Objects.nonNull(menus)){
            for (SysMenu menu : menus) {
                Map map = new HashMap();
                Long menuId = menu.getMenuId();
                String menuName = menu.getMenuName();
                AjaxResult model = remoteCmdbService.getModel(menuId, SecurityConstants.INNER);
                String modelId = String.valueOf(model.get("data"));
                List<SysMenu> children = menu.getChildren();
                if(Objects.nonNull(children)&&children.size()>0){
                    if("M".equals(menu.getMenuType())){
                        List list1 = transMenu(children);
                        if(Objects.nonNull(list1)&&list1.size()>0) {
                            map.put("label", menuName);
                            map.put("id", modelId);
                            map.put("children",list1 );
                            list.add(map);
                        }
                    }else{
                        map.put("label",menuName);
                        map.put("id",modelId);
                        list.add(map);
                    }
                }else if("C".equals(menu.getMenuType())){
                    map.put("label",menuName);
                    map.put("id",modelId);
                    list.add(map);
                }

            }
        }
        return list;
    }

    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */

    public List<SysMenu> selectMenuList(SysMenu menu, Long userId)
    {
        List<SysMenu> menuList = null;
        // 管理员显示所有菜单信息
        if (SysUser.isAdmin(userId))
        {
            menuList = menuMapper.selectMenuList(menu);
        }
        else
        {
            menu.getParams().put("userId", userId);
            menuList = menuMapper.selectMenuListByUserId(menu);
        }
        return menuList;
    }


    public List<SysMenu> selectTreeMenuList(SysMenu menu, Long userId)
    {
        List<SysMenu> menuList = null;
        // 管理员显示所有菜单信息
        if (SysUser.isAdmin(userId))
        {
            menuList = menuMapper.selectMenuList(menu);
        }
        else
        {
            menu.getParams().put("userId", userId);
            menuList = menuMapper.selectMenuListByUserId(menu);
        }
        return getChildPerms(menuList,0);
    }



    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */

    public Set<String> selectMenuPermsByUserId(Long userId)
    {
        List<String> perms = menuMapper.selectMenuPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms)
        {
            if (StringUtils.isNotEmpty(perm))
            {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户名称
     * @return 菜单列表
     */

    public List<SysMenu> selectMenuTreeByUserId(Long userId)
    {
        List<SysMenu> menus = null;
        if (SecurityUtils.isAdmin(userId))
        {
            menus = menuMapper.selectMenuTreeAll();
        }
        else
        {
            menus = menuMapper.selectMenuTreeByUserId(userId);
        }
        return getChildPerms(menus, 0);
    }

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户名称
     * @return 菜单列表
     */

    public List<SysMenu> selectUucHeaderMenuTreeByUserId(Long userId)
    {
        List<SysMenu> menus = null;
        if (SecurityUtils.isAdmin(userId))
        {
            SysMenu params = new SysMenu();
            params.setClientId(ClientConstants.MODEL_CODE_UUC);
            params.setStatus("0");
            params.setVisible("0");
            menus = menuMapper.selectMenuList(params);
        }
        else
        {
            menus = menuMapper.selectUucHeaderMenuTreeByUserId(userId);
        }
        return getChildPerms(menus, 0);
    }

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */

    public List<Long> selectMenuListByRoleId(Long roleId)
    {
        SysRole role = roleMapper.selectRoleById(roleId);
        return menuMapper.selectMenuListByRoleId(roleId, role.isMenuCheckStrictly());
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */

    public List<RouterVo> buildMenus(List<SysMenu> menus)
    {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menus)
        {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
            List<SysMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && cMenus.size() > 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType()))
            {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            }
            else if (isMenuFrame(menu))
            {
                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getPath()));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
                children.setQuery(menu.getQuery());
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            else if (menu.getParentId().intValue() == 0 && isInnerLink(menu))
            {
                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
                router.setPath("/");
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                String routerPath = innerLinkReplaceEach(menu.getPath());
                children.setPath(routerPath);
                children.setComponent(UserConstants.INNER_LINK);
                children.setName(StringUtils.capitalize(routerPath));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 构建前端所需要的树结构（虚机模块节点菜单）
     * @param menus
     * @return
     */
    private void virtualClientNodes(List<SysMenu> menus) {
        List<UucModelInfo> modelInfoList = modelInfoService.selectUucModelInfoList(new UucModelInfo());
        Map<String, UucModelInfo> modelInfoMap = new HashMap<>();
        Map<String, Long> idMap = new HashMap<>();
        Long initValue = 1L;
        for (UucModelInfo modelInfo: modelInfoList) {
            modelInfoMap.put(modelInfo.getClientId(), modelInfo);
            idMap.put(modelInfo.getClientId(), -initValue++);
        }
        Set<String> idTops = new HashSet<>();
        List<SysMenu> menuTops = new ArrayList<>();
        for (SysMenu menu: menus) {
            // 顶级节点在数据库中parentId为0
            if (menu.getParentId() == 0L) {
                String clientId = menu.getClientId();
                if (!idTops.contains(menu.getClientId())) {
                    SysMenu top = new SysMenu();
                    top.setMenuId(idMap.get(clientId));
                    top.setMenuName(modelInfoMap.get(clientId).getClientName());
                    // 默认顶级节点parentId为0，适配前端
                    top.setParentId(0L);
                    menuTops.add(top);
                    idTops.add(clientId);
                }
                menu.setParentId(idMap.get(clientId));
            }
        }
        menus.addAll(menuTops);
    }

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    public List<SysMenu> buildMenuTree(List<SysMenu> menus)
    {
        virtualClientNodes(menus);
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        List<Long> tempList = new ArrayList<Long>();
        for (SysMenu dept : menus)
        {
            tempList.add(dept.getMenuId());
        }
        for (Iterator<SysMenu> iterator = menus.iterator(); iterator.hasNext();)
        {
            SysMenu menu = (SysMenu) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId()))
            {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = menus;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */

    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus)
    {
        List<SysMenu> menuTrees = buildMenuTree(menus);
        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */

    public SysMenu selectMenuById(Long menuId)
    {
        return menuMapper.selectMenuById(menuId);
    }

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */

    public boolean hasChildByMenuId(Long menuId)
    {
        int result = menuMapper.hasChildByMenuId(menuId);
        return result > 0 ? true : false;
    }

    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */

    public boolean checkMenuExistRole(Long menuId)
    {
        int result = roleMenuMapper.checkMenuExistRole(menuId);
        return result > 0 ? true : false;
    }

    /**
     * 新增保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */

    public int insertMenu(SysMenu menu)
    {
        return menuMapper.insertMenu(menu);
    }

    /**
     * 修改保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */

    public int updateMenu(SysMenu menu)
    {
        return menuMapper.updateMenu(menu);
    }

    public int updateCmdbMenu(SysMenu menu)
    {
        Long menuId = menu.getMenuId();
        String olderName = menuMapper.selectMenuById(menuId).getMenuName();
        String newerName = menu.getMenuName();
        SysMenu cond = new SysMenu();
        cond.setParentId(menuId);
        List<SysMenu> childMenu = menuMapper.selectMenuByCond(cond);
        for (SysMenu cMenu : childMenu) {
            String childTmp = cMenu.getMenuName();
            cMenu.setMenuName(childTmp.replaceAll(olderName, newerName));
            cMenu.setUpdateTime(DateUtils.getNowDate());
            menuMapper.updateMenu(cMenu);
        }
        menu.setUpdateTime(DateUtils.getNowDate());
        return menuMapper.updateMenu(menu);
    }

    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */

    public int deleteMenuById(Long menuId)
    {
        return menuMapper.deleteMenuById(menuId);
    }

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */

    public String checkMenuNameUnique(SysMenu menu)
    {
        Long menuId = StringUtils.isNull(menu.getMenuId()) ? -1L : menu.getMenuId();
        SysMenu info = menuMapper.checkMenuNameUnique(menu.getMenuName(), menu.getParentId());
        if (StringUtils.isNotNull(info) && info.getMenuId().longValue() != menuId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }


    public int syncMenu(SysMenu sysMenu) {
//        return menuMapper.syncMenu(sysMenu);
        if (StringUtils.isBlank(sysMenu.getClientId())){
            sysMenu.setClientId(ClientConstants.MODE_CODE_CMDB);
        }
        int menuId = menuMapper.insertMenu(sysMenu);
        // 返回 菜单id
        return menuId;
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(SysMenu menu)
    {
        String routerName = StringUtils.capitalize(menu.getPath());
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu))
        {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu)
    {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentId().intValue() != 0 && isInnerLink(menu))
        {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType())
                && UserConstants.NO_FRAME.equals(menu.getIsFrame()))
        {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu))
        {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysMenu menu)
    {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu))
        {
            component = menu.getComponent();
        }
        else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu))
        {
            component = UserConstants.INNER_LINK;
        }
        else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu))
        {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(SysMenu menu)
    {
        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsFrame().equals(UserConstants.NO_FRAME);
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(SysMenu menu)
    {
        return menu.getIsFrame().equals(UserConstants.NO_FRAME) && StringUtils.ishttp(menu.getPath());
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysMenu menu)
    {
        return menu.getParentId().intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list 分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, int parentId)
    {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext();)
        {
            SysMenu t = (SysMenu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId)
            {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t)
    {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t)
    {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext())
        {
            SysMenu n = (SysMenu) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t)
    {
        return getChildList(list, t).size() > 0;
    }

    /**
     * 内链域名特殊字符替换
     *
     * @return
     */
    public String innerLinkReplaceEach(String path)
    {
        return StringUtils.replaceEach(path, new String[] { Constants.HTTP, Constants.HTTPS },
                new String[] { "", "" });
    }

    public List<SysMenu> selectMenuByCond(SysMenu menu) {
        return menuMapper.selectMenuByCond(menu);
    }
}
