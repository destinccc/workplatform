package com.uuc.system.uuc.service;

import java.util.List;
import com.uuc.system.uuc.domain.UccUserApplicaiton;

/**
 * 用户应用关联Service接口
 * 
 * @author uuc
 * @date 2022-04-01
 */
public interface IUccUserApplicaitonService 
{
    /**
     * 查询用户应用关联
     * 
     * @param userCode 用户应用关联主键
     * @return 用户应用关联
     */
    public UccUserApplicaiton selectUccUserApplicaitonByUserCode(String userCode);

    /**
     * 查询用户应用关联列表
     * 
     * @param uccUserApplicaiton 用户应用关联
     * @return 用户应用关联集合
     */
    public List<UccUserApplicaiton> selectUccUserApplicaitonList(UccUserApplicaiton uccUserApplicaiton);

    /**
     * 新增用户应用关联
     * 
     * @param uccUserApplicaiton 用户应用关联
     * @return 结果
     */
    public int insertUccUserApplicaiton(UccUserApplicaiton uccUserApplicaiton);

    /**
     * 修改用户应用关联
     * 
     * @param uccUserApplicaiton 用户应用关联
     * @return 结果
     */
    public int updateUccUserApplicaiton(UccUserApplicaiton uccUserApplicaiton);

    /**
     * 批量删除用户应用关联
     * 
     * @param userCodes 需要删除的用户应用关联主键集合
     * @return 结果
     */
    public int deleteUccUserApplicaitonByUserCodes(String[] userCodes);

    /**
     * 删除用户应用关联信息
     * 
     * @param userCode 用户应用关联主键
     * @return 结果
     */
    public int deleteUccUserApplicaitonByUserCode(String userCode);
}
