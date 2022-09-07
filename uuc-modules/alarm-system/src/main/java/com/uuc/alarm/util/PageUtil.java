package com.uuc.alarm.util;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.uuc.alarm.domain.vo.EventListVo;
import com.uuc.common.core.constant.HttpStatus;
import com.uuc.common.core.web.page.TableDataInfo;

import java.util.List;

/**
 * @description Page转换
 * @author llb
 * @since 2022/8/11 13:32
 */
public class PageUtil {
    private PageUtil() {

    }

    /**
     * 将PageInfo对象泛型中的Po对象转化为Vo对象
     * @param pageInfoPo PageInfo<Po>对象</>
     * @param <P> Po类型
     * @param <V> Vo类型
     * @return
     */
    public static <P, V> PageInfo<V> pageInfo2PageInfoVo(Page<P> pageInfoPo, List<V> voList) {
        // 创建Page对象，实际上是一个ArrayList类型的集合
        Page<V> page = new Page<>(pageInfoPo.getPageNum(), pageInfoPo.getPageSize());
        page.setTotal(pageInfoPo.getTotal());
        PageInfo<V> vPageInfo = new PageInfo<>(page);
        vPageInfo.setList(voList);
        return vPageInfo;
    }

    /**
     * 将PageInfo对象封装成TableDataInfo后返回
     * @param pageInfo
     * @return
     */
    public static TableDataInfo getTableDataInfo(PageInfo<EventListVo> pageInfo) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setRows(pageInfo.getList());
        rspData.setMsg("查询成功");
        rspData.setTotal(pageInfo.getTotal());
        return rspData;
    }
}
