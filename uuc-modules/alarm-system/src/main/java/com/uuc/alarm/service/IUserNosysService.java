package com.uuc.alarm.service;

import com.uuc.alarm.domain.UserNosys;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author uuc
 * @date 2022-08-01
 */
public interface IUserNosysService {
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    UserNosys selectUserNosysById(Integer id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param userNosys 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    List<UserNosys> selectUserNosysList(UserNosys userNosys);

    /**
     * 新增【请填写功能名称】
     *
     * @param userNosys 【请填写功能名称】
     * @return 结果
     */
    int insertUserNosys(UserNosys userNosys);

    /**
     * 修改【请填写功能名称】
     *
     * @param userNosys 【请填写功能名称】
     * @return 结果
     */
    int updateUserNosys(UserNosys userNosys);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    int deleteUserNosysByIds(Integer[] ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    int deleteUserNosysById(Integer id);
}
