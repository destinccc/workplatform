package com.uuc.alarm.service.impl;

import com.uuc.alarm.domain.UserNosys;
import com.uuc.alarm.mapper.UserNosysMapper;
import com.uuc.alarm.service.IUserNosysService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author uuc
 * @date 2022-08-01
 */
@Service
public class UserNosysServiceImpl implements IUserNosysService {
    private final UserNosysMapper userNosysMapper;

    public UserNosysServiceImpl(UserNosysMapper userNosysMapper) {
        this.userNosysMapper = userNosysMapper;
    }

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public UserNosys selectUserNosysById(Integer id) {
        return userNosysMapper.selectUserNosysById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param userNosys 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<UserNosys> selectUserNosysList(UserNosys userNosys) {
        return userNosysMapper.selectUserNosysList(userNosys);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param userNosys 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertUserNosys(UserNosys userNosys) {
        return userNosysMapper.insertUserNosys(userNosys);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param userNosys 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateUserNosys(UserNosys userNosys) {
        return userNosysMapper.updateUserNosys(userNosys);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteUserNosysByIds(Integer[] ids) {
        return userNosysMapper.deleteUserNosysByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteUserNosysById(Integer id) {
        return userNosysMapper.deleteUserNosysById(id);
    }
}
