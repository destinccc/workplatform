package com.uuc.system.uuc.service.impl;

import java.util.List;

import com.uuc.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uuc.system.uuc.mapper.UucUserPostMapper;
import com.uuc.system.uuc.domain.UucUserPost;
import com.uuc.system.uuc.service.IUucUserPostService;

/**
 * 用户职位关联Service业务层处理
 * 
 * @author uuc
 * @date 2022-04-01
 */
@Service
public class UucUserPostServiceImpl implements IUucUserPostService 
{
    @Autowired
    private UucUserPostMapper uucUserPostMapper;

    /**
     * 查询用户职位关联
     * 
     * @param id 用户职位关联主键
     * @return 用户职位关联
     */
    @Override
    public UucUserPost selectUucUserPostById(Long id)
    {
        return uucUserPostMapper.selectUucUserPostById(id);
    }

    /**
     * 查询用户职位关联列表
     * 
     * @param uucUserPost 用户职位关联
     * @return 用户职位关联
     */
    @Override
    public List<UucUserPost> selectUucUserPostList(UucUserPost uucUserPost)
    {
        return uucUserPostMapper.selectUucUserPostList(uucUserPost);
    }

    /**
     * 新增用户职位关联
     * 
     * @param uucUserPost 用户职位关联
     * @return 结果
     */
    @Override
    public int insertUucUserPost(UucUserPost uucUserPost)
    {
        uucUserPost.setCreateTime(DateUtils.getNowDate());
        return uucUserPostMapper.insertUucUserPost(uucUserPost);
    }

    /**
     * 修改用户职位关联
     * 
     * @param uucUserPost 用户职位关联
     * @return 结果
     */
    @Override
    public int updateUucUserPost(UucUserPost uucUserPost)
    {
        uucUserPost.setUpdateTime(DateUtils.getNowDate());
        return uucUserPostMapper.updateUucUserPost(uucUserPost);
    }

    /**
     * 批量删除用户职位关联
     * 
     * @param ids 需要删除的用户职位关联主键
     * @return 结果
     */
    @Override
    public int deleteUucUserPostByIds(Long[] ids)
    {
        return uucUserPostMapper.deleteUucUserPostByIds(ids);
    }

    /**
     * 删除用户职位关联信息
     * 
     * @param id 用户职位关联主键
     * @return 结果
     */
    @Override
    public int deleteUucUserPostById(Long id)
    {
        return uucUserPostMapper.deleteUucUserPostById(id);
    }
}
