package com.uuc.system.uuc.service.impl;

import java.util.List;

import com.uuc.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uuc.system.uuc.mapper.UucProjectUserMapper;
import com.uuc.system.api.model.UucProjectUser;

/**
 * 用户项目Service业务层处理
 *
 * @author uuc
 * @date 2022-04-01
 */
@Service
public class UucProjectUserService
{
    @Autowired
    private UucProjectUserMapper uucProjectUserMapper;

    /**
     * 查询用户项目
     *
     * @param userCode 用户项目主键
     * @return 用户项目
     */

    public UucProjectUser selectUucProjectUserByUserCode(String userCode)
    {
        return uucProjectUserMapper.selectUucProjectUserByUserCode(userCode);
    }

    /**
     * 查询用户项目列表
     *
     * @param uucProjectUser 用户项目
     * @return 用户项目
     */

    public List<UucProjectUser> selectUucProjectUserList(UucProjectUser uucProjectUser)
    {
        return uucProjectUserMapper.selectUucProjectUserList(uucProjectUser);
    }

    /**
     * 新增用户项目
     *
     * @param uucProjectUser 用户项目
     * @return 结果
     */

    public int insertUucProjectUser(UucProjectUser uucProjectUser)
    {
        uucProjectUser.setCreateTime(DateUtils.getNowDate());
        return uucProjectUserMapper.insertUucProjectUser(uucProjectUser);
    }

    /**
     * 修改用户项目
     *
     * @param uucProjectUser 用户项目
     * @return 结果
     */

    public int updateUucProjectUser(UucProjectUser uucProjectUser)
    {
        uucProjectUser.setUpdateTime(DateUtils.getNowDate());
        return uucProjectUserMapper.updateUucProjectUser(uucProjectUser);
    }

    /**
     * 批量删除用户项目
     *
     * @param userCodes 需要删除的用户项目主键
     * @return 结果
     */

    public int deleteUucProjectUserByUserCodes(String[] userCodes)
    {
        return uucProjectUserMapper.deleteUucProjectUserByUserCodes(userCodes);
    }

    /**
     * 删除用户项目信息
     *
     * @param userCode 用户项目主键
     * @return 结果
     */

    public int deleteUucProjectUserByUserCode(String userCode)
    {
        return uucProjectUserMapper.deleteUucProjectUserByUserCode(userCode);
    }

    public List<UucProjectUser> selectUucProjectUserByProjectCode(String projectCode) {
        return uucProjectUserMapper.selectUucProjectUserByProjectCode(projectCode);
    }
}
