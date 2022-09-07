package com.uuc.system.uuc.service.impl;

import java.util.List;

import com.uuc.common.core.enums.CategoryType;
import com.uuc.common.core.exception.ServiceException;
import com.uuc.common.core.utils.DateUtils;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.system.api.model.SysMenu;
import com.uuc.system.mapper.SysMenuMapper;
import com.uuc.system.uuc.mapper.UucCategoryMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import com.uuc.system.uuc.domain.UucCategoryMenu;
import com.uuc.system.uuc.mapper.UucCategoryInfoMapper;
import com.uuc.system.uuc.domain.UucCategoryInfo;
import com.uuc.system.uuc.service.IUucCategoryInfoService;

/**
 * 类目信息表Service业务层处理
 * 
 * @author uuc
 * @date 2022-04-19
 */
@Service
public class UucCategoryInfoServiceImpl implements IUucCategoryInfoService 
{
    @Autowired
    private UucCategoryInfoMapper uucCategoryInfoMapper;

    @Autowired
    private UucCategoryMenuMapper uucCategoryMenuMapper;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    /**
     * 查询类目信息表
     * 
     * @param id 类目信息表主键
     * @return 类目信息表
     */
    @Override
    public UucCategoryInfo selectUucCategoryInfoById(Long id)
    {
        return uucCategoryInfoMapper.selectUucCategoryInfoById(id);
    }

    /**
     * 查询类目信息表列表
     * 
     * @param uucCategoryInfo 类目信息表
     * @return 类目信息表
     */
    @Override
    public List<UucCategoryInfo> selectUucCategoryInfoList(UucCategoryInfo uucCategoryInfo)
    {
        return uucCategoryInfoMapper.selectUucCategoryInfoList(uucCategoryInfo);
    }

    /**
     * 新增类目信息表
     * 
     * @param uucCategoryInfo 类目信息表
     * @return 结果
     */
    @Transactional
    @Override
    public int insertUucCategoryInfo(UucCategoryInfo uucCategoryInfo)
    {
        List<UucCategoryMenu> uucCategoryMenus=uucCategoryInfo.getUucCategoryMenuList();
        //先保存类目信息
        uucCategoryInfo.setCreateTime(DateUtils.getNowDate());
        int rows = uucCategoryInfoMapper.insertUucCategoryInfo(uucCategoryInfo);
        //再执行类目删除对应菜单
        //uucCategoryMenuMapper.deleteUucCategoryMenuByCategoryId(uucCategoryInfo.getId());
        //保存类目菜单对应数据
        if(uucCategoryMenus!=null&&uucCategoryMenus.size()>0){
            for(UucCategoryMenu item:uucCategoryMenus){
                item.setCategoryId(uucCategoryInfo.getId());
                //查询每个菜单是否有父类，如果有就将父类path拼接上，否则只取子类路径
                SysMenu sysMenu = sysMenuMapper.selectMenuById(item.getMenuId());
                if(sysMenu==null){
                    throw new ServiceException("数据有误");
                }
                Long parentId=sysMenu.getParentId();
                if(parentId==null){
                    throw new ServiceException("缺少父类数据");
                }
                SysMenu parentSysMenu = sysMenuMapper.selectMenuById(parentId);
                String path=sysMenu.getPath();

                if(parentSysMenu!=null){
                    if(!path.startsWith("/")){
                        path="/"+path;
                    }
                    path=parentSysMenu.getPath()+path;
                }
                item.setPath(path);
            }
            uucCategoryMenuMapper.insertList(uucCategoryMenus);
        }
        return rows;
    }

    /**
     * 修改类目信息表
     * 
     * @param uucCategoryInfo 类目信息表
     * @return 结果
     */
    @Transactional
    @Override
    public int updateUucCategoryInfo(UucCategoryInfo uucCategoryInfo)
    {
        int i=0;
        List<UucCategoryMenu> uucCategoryMenus=uucCategoryInfo.getUucCategoryMenuList();
        uucCategoryInfo.setUpdateTime(DateUtils.getNowDate());
        i=uucCategoryInfoMapper.updateUucCategoryInfo(uucCategoryInfo);
        //再执行类目删除对应菜单
        uucCategoryMenuMapper.deleteUucCategoryMenuByCategoryId(uucCategoryInfo.getId());
        //保存类目菜单对应数据
        if(uucCategoryMenus!=null&&uucCategoryMenus.size()>0){
            for(UucCategoryMenu item:uucCategoryMenus){
                item.setCategoryId(uucCategoryInfo.getId());
                //查询每个菜单是否有父类，如果有就将父类path拼接上，否则只取子类路径
                String menuPath = getMenuPath(item.getMenuId());
                item.setPath(menuPath);
            }
            uucCategoryMenuMapper.insertList(uucCategoryMenus);
        }
        return i;
    }

    /**
     * 批量删除类目信息表
     * 
     * @param ids 需要删除的类目信息表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteUucCategoryInfoByIds(Long[] ids)
    {
        uucCategoryInfoMapper.deleteUucCategoryMenuByCategoryIds(ids);
        return uucCategoryInfoMapper.deleteUucCategoryInfoByIds(ids);
    }

    /**
     * 删除类目信息表信息
     * 
     * @param id 类目信息表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteUucCategoryInfoById(Long id)
    {
        uucCategoryInfoMapper.deleteUucCategoryMenuByCategoryId(id);
        return uucCategoryInfoMapper.deleteUucCategoryInfoById(id);
    }

    @Override
    public List<UucCategoryInfo> selectUucCategoryInfoListVo(UucCategoryInfo uucCategoryInfo) {
        List<UucCategoryInfo> uucCategoryInfos = uucCategoryInfoMapper.selectUucCategoryInfoListVo(uucCategoryInfo);
        if(uucCategoryInfos!=null&&uucCategoryInfos.size()>0){
            for(UucCategoryInfo item:uucCategoryInfos){
                String categoryType=item.getType();
                List<SysMenu> sysMenus = item.getSysMenus();
                if(sysMenus!=null&&sysMenus.size()>0){
                    for(SysMenu po:sysMenus){
                        if(po.getMenuId()==null){
                            sysMenus.remove(po);
                            break;
                        }
                    }
                }
                if(sysMenus!=null&&sysMenus.size()>0){
                    //是图文模式就将该菜单对应的图片内容设置到菜单里
                    UucCategoryMenu uucCategoryMenu=new UucCategoryMenu();
                     for(SysMenu one:sysMenus){
                        uucCategoryMenu.setMenuId(one.getMenuId());
                        uucCategoryMenu.setCategoryId(item.getId());
                        List<UucCategoryMenu> uucCategoryMenus = uucCategoryMenuMapper.selectUucCategoryMenuList(uucCategoryMenu);
                        //原则上一个菜单只能挂在一个类目下，也就是只可能有一个在类目菜单关联表
                        if(uucCategoryMenus!=null&&uucCategoryMenus.size()>0){
                            if(categoryType.equals(CategoryType.IMAGE.getCode())){
                                String imageContent = uucCategoryMenus.get(0).getImageContent();
                                one.setImageContent(imageContent);
                            }
                           String showName=uucCategoryMenus.get(0).getShowName();
                            if(StringUtils.isNotEmpty(showName)){
                                one.setMenuName(showName);
                            }
                            //拼接菜单全路径用于产品说明列表菜单跳转
                            String menuPath = getMenuPath(one.getMenuId());
                            one.setPath(menuPath);
                        }

                    }
                }
            }
        }
        return uucCategoryInfos;
    }

    /**
     * 新增类目菜单关联信息
     * 
     * @param uucCategoryInfo 类目信息表对象
     */
    public void insertUucCategoryMenu(UucCategoryInfo uucCategoryInfo)
    {
        List<UucCategoryMenu> uucCategoryMenuList = uucCategoryInfo.getUucCategoryMenuList();
        Long id = uucCategoryInfo.getId();
        if (StringUtils.isNotNull(uucCategoryMenuList))
        {
            List<UucCategoryMenu> list = new ArrayList<UucCategoryMenu>();
            for (UucCategoryMenu uucCategoryMenu : uucCategoryMenuList)
            {
                uucCategoryMenu.setCategoryId(id);
                list.add(uucCategoryMenu);
            }
            if (list.size() > 0)
            {
                uucCategoryInfoMapper.batchUucCategoryMenu(list);
            }
        }
    }

    /**
     * 获取菜单的路径，用于产品说明跳转拼接路径
     */
    public String getMenuPath(Long menuId){
        SysMenu sysMenu = sysMenuMapper.selectMenuById(menuId);
        if(sysMenu==null){
            throw new ServiceException("数据有误");
        }
        Long parentId=sysMenu.getParentId();
        if(parentId==null){
            throw new ServiceException("缺少父类数据");
        }
        SysMenu parentSysMenu = sysMenuMapper.selectMenuById(parentId);
        String path=sysMenu.getPath();

        if(parentSysMenu!=null&&parentSysMenu.getMenuId()!=null){
            if(!path.startsWith("/")){
                path="/"+path;
            }
            path=parentSysMenu.getPath()+path;
        }
        return path;
    }
}
