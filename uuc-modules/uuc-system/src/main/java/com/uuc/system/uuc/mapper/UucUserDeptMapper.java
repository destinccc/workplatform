package com.uuc.system.uuc.mapper;

import java.util.List;
import com.uuc.system.api.model.UucUserDept;
import org.apache.ibatis.annotations.Param;

/**
 * 用户组织Mapper接口
 *
 * @author uuc
 * @date 2022-04-01
 */
public interface UucUserDeptMapper
{
    /**
     * 查询用户组织
     *
     * @param userCode 用户组织主键
     * @return 用户组织
     */
    public List<UucUserDept> selectUucUserDeptByUserCode(String userCode);

    /**
     * 查询用户组织列表
     *
     * @param uucUserDept 用户组织
     * @return 用户组织集合
     */
    public List<UucUserDept> selectUucUserDeptList(UucUserDept uucUserDept);

    /**
     * 新增用户组织
     *
     * @param uucUserDept 用户组织
     * @return 结果
     */
    public int insertUucUserDept(UucUserDept uucUserDept);

    /**
     * 修改用户组织
     *
     * @param uucUserDept 用户组织
     * @return 结果
     */
    public int updateUucUserDept(UucUserDept uucUserDept);

    /**
     * 修改用户组织负责人
     *
     * @param uucUserDept 用户组织
     * @return 结果
     */
    public int updateUucUserDeptAdminFlag(UucUserDept uucUserDept);

    /**
     * 修改用户组织负责人
     *
     * @param uucUserDept 用户组织
     * @return 结果
     */
    public int updateUucUserDeptAdminFlagByDeptCode(UucUserDept uucUserDept);

    /**
     * 删除用户组织
     *
     * @param userCode 用户组织主键
     * @return 结果
     */
    public int deleteUucUserDeptByUserCode(String userCode);


    /**
     * 删除组织用户
     *
     * @param deptCode 组织主键
     * @return 结果
     */
    public int deleteUucUserDeptByDeptCode(String deptCode);

    /**
     * 批量删除用户组织
     *
     * @param userCodes 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUucUserDeptByUserCodes(String[] userCodes);

    public int batchInsertUucUserDept(List<UucUserDept> deptList);

    public int selectUucUserInDeptCounts(@Param("userCodeList") List<String> userCodeList, @Param("deptCode") String deptCode);

    public int checkUserAndDeptRelationsExist(@Param("deptCode") String deptCode);
}
