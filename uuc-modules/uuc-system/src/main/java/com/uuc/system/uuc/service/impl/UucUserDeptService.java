package com.uuc.system.uuc.service.impl;

import java.util.List;

import com.uuc.common.core.utils.DateUtils;
import com.uuc.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uuc.system.uuc.mapper.UucUserDeptMapper;
import com.uuc.system.api.model.UucUserDept;

/**
 * 用户组织Service业务层处理
 *
 * @author uuc
 * @date 2022-04-01
 */
@Service
public class UucUserDeptService
{
    @Autowired
    private UucUserDeptMapper uucUserDeptMapper;

    /**
     * 查询用户组织
     *
     * @param userCode 用户组织主键
     * @return 用户组织
     */

    public List<UucUserDept> selectUucUserDeptByUserCode(String userCode)
    {
        return uucUserDeptMapper.selectUucUserDeptByUserCode(userCode);
    }

    /**
     * 查询用户组织列表
     *
     * @param uucUserDept 用户组织
     * @return 用户组织
     */

    public List<UucUserDept> selectUucUserDeptList(UucUserDept uucUserDept)
    {
        return uucUserDeptMapper.selectUucUserDeptList(uucUserDept);
    }

    /**
     * 新增用户组织
     *
     * @param uucUserDept 用户组织
     * @return 结果
     */

    public int insertUucUserDept(UucUserDept uucUserDept)
    {
        uucUserDept.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        uucUserDept.setCreateTime(DateUtils.getNowDate());
        return uucUserDeptMapper.insertUucUserDept(uucUserDept);
    }

    /**
     * 修改用户组织
     *
     * @param uucUserDept 用户组织
     * @return 结果
     */

    public int updateUucUserDept(UucUserDept uucUserDept)
    {
        uucUserDept.setUpdateBy(String.valueOf(SecurityUtils.getUserId()));
        uucUserDept.setUpdateTime(DateUtils.getNowDate());
        return uucUserDeptMapper.updateUucUserDept(uucUserDept);
    }

    /**
     * 批量删除用户组织
     *
     * @param userCodes 需要删除的用户组织主键
     * @return 结果
     */

    public int deleteUucUserDeptByUserCodes(String[] userCodes)
    {
        return uucUserDeptMapper.deleteUucUserDeptByUserCodes(userCodes);
    }

    /**
     * 删除用户组织信息
     *
     * @param userCode 用户组织主键
     * @return 结果
     */

    public int deleteUucUserDeptByUserCode(String userCode)
    {
        return uucUserDeptMapper.deleteUucUserDeptByUserCode(userCode);
    }
}
