package com.uuc.system.uuc.service.impl;

import java.util.List;

import com.uuc.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uuc.system.uuc.mapper.UccUserApplicaitonMapper;
import com.uuc.system.uuc.domain.UccUserApplicaiton;
import com.uuc.system.uuc.service.IUccUserApplicaitonService;

/**
 * 用户应用关联Service业务层处理
 * 
 * @author uuc
 * @date 2022-04-01
 */
@Service
public class UccUserApplicaitonServiceImpl implements IUccUserApplicaitonService 
{
    @Autowired
    private UccUserApplicaitonMapper uccUserApplicaitonMapper;

    /**
     * 查询用户应用关联
     * 
     * @param userCode 用户应用关联主键
     * @return 用户应用关联
     */
    @Override
    public UccUserApplicaiton selectUccUserApplicaitonByUserCode(String userCode)
    {
        return uccUserApplicaitonMapper.selectUccUserApplicaitonByUserCode(userCode);
    }

    /**
     * 查询用户应用关联列表
     * 
     * @param uccUserApplicaiton 用户应用关联
     * @return 用户应用关联
     */
    @Override
    public List<UccUserApplicaiton> selectUccUserApplicaitonList(UccUserApplicaiton uccUserApplicaiton)
    {
        return uccUserApplicaitonMapper.selectUccUserApplicaitonList(uccUserApplicaiton);
    }

    /**
     * 新增用户应用关联
     * 
     * @param uccUserApplicaiton 用户应用关联
     * @return 结果
     */
    @Override
    public int insertUccUserApplicaiton(UccUserApplicaiton uccUserApplicaiton)
    {
        uccUserApplicaiton.setCreateTime(DateUtils.getNowDate());
        return uccUserApplicaitonMapper.insertUccUserApplicaiton(uccUserApplicaiton);
    }

    /**
     * 修改用户应用关联
     * 
     * @param uccUserApplicaiton 用户应用关联
     * @return 结果
     */
    @Override
    public int updateUccUserApplicaiton(UccUserApplicaiton uccUserApplicaiton)
    {
        uccUserApplicaiton.setUpdateTime(DateUtils.getNowDate());
        return uccUserApplicaitonMapper.updateUccUserApplicaiton(uccUserApplicaiton);
    }

    /**
     * 批量删除用户应用关联
     * 
     * @param userCodes 需要删除的用户应用关联主键
     * @return 结果
     */
    @Override
    public int deleteUccUserApplicaitonByUserCodes(String[] userCodes)
    {
        return uccUserApplicaitonMapper.deleteUccUserApplicaitonByUserCodes(userCodes);
    }

    /**
     * 删除用户应用关联信息
     * 
     * @param userCode 用户应用关联主键
     * @return 结果
     */
    @Override
    public int deleteUccUserApplicaitonByUserCode(String userCode)
    {
        return uccUserApplicaitonMapper.deleteUccUserApplicaitonByUserCode(userCode);
    }
}
