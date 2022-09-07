package com.uuc.system.uuc.mapper;

import java.util.List;
import com.uuc.system.uuc.domain.UucUserPost;

/**
 * 用户职位关联Mapper接口
 * 
 * @author uuc
 * @date 2022-04-01
 */
public interface UucUserPostMapper 
{
    /**
     * 查询用户职位关联
     * 
     * @param id 用户职位关联主键
     * @return 用户职位关联
     */
    public UucUserPost selectUucUserPostById(Long id);

    /**
     * 查询用户职位关联列表
     * 
     * @param uucUserPost 用户职位关联
     * @return 用户职位关联集合
     */
    public List<UucUserPost> selectUucUserPostList(UucUserPost uucUserPost);

    /**
     * 新增用户职位关联
     * 
     * @param uucUserPost 用户职位关联
     * @return 结果
     */
    public int insertUucUserPost(UucUserPost uucUserPost);

    /**
     * 修改用户职位关联
     * 
     * @param uucUserPost 用户职位关联
     * @return 结果
     */
    public int updateUucUserPost(UucUserPost uucUserPost);

    /**
     * 删除用户职位关联
     * 
     * @param id 用户职位关联主键
     * @return 结果
     */
    public int deleteUucUserPostById(Long id);

    /**
     * 批量删除用户职位关联
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUucUserPostByIds(Long[] ids);
}
