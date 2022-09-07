package com.uuc.system.uuc.mapper;

import com.uuc.system.api.model.UucProjectDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author uuc
 * @date 2022-05-23
 */
public interface UucProjectDeptMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param deptCode 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public UucProjectDept selectUucProjectDeptByDeptCode(String deptCode);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param uucProjectDept 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<UucProjectDept> selectUucProjectDeptList(UucProjectDept uucProjectDept);

    /**
     * 新增【请填写功能名称】
     *
     * @param uucProjectDept 【请填写功能名称】
     * @return 结果
     */
    public int insertUucProjectDept(UucProjectDept uucProjectDept);

    /**
     * 修改【请填写功能名称】
     *
     * @param uucProjectDept 【请填写功能名称】
     * @return 结果
     */
    public int updateUucProjectDept(UucProjectDept uucProjectDept);

    /**
     * 删除【请填写功能名称】
     *
     * @param deptCode 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteUucProjectDeptByDeptCode(String deptCode);

    /**
     * 删除【请填写功能名称】
     *
     * @param projectCode 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteUucProjectDeptByProjectCode(String projectCode);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param deptCodes 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUucProjectDeptByDeptCodes(String[] deptCodes);

    public int batchInsertUucProjectDept(List<UucProjectDept> useDeptList);

    public int checkProjectAndDeptRelationsExist(@Param("deptCode") String deptCode);

    List<String> selectProjectUsedDepts(String deptCode);
}
