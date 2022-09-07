package com.uuc.system.uuc.mapper;

import java.util.List;
import com.uuc.system.api.model.UucDeptInfo;
import com.uuc.system.api.uuc.dto.CmdbDeptDto;

/**
 * 组织信息Mapper接口
 *
 * @author uuc
 * @date 2022-04-01
 */
public interface UucDeptInfoMapper
{
    /**
     * 查询组织信息
     *
     * @param id 组织信息主键
     * @return 组织信息
     */
    public UucDeptInfo selectUucDeptInfoById(Long id);

    /**
     * 查询组织信息列表
     *
     * @param uucDeptInfo 组织信息
     * @return 组织信息集合
     */
    public List<UucDeptInfo> selectUucDeptInfoList(UucDeptInfo uucDeptInfo);

    /**
     * 新增组织信息
     *
     * @param uucDeptInfo 组织信息
     * @return 结果
     */
    public int insertUucDeptInfo(UucDeptInfo uucDeptInfo);

    /**
     * 修改组织信息
     *
     * @param uucDeptInfo 组织信息
     * @return 结果
     */
    public int updateUucDeptInfo(UucDeptInfo uucDeptInfo);

    /**
     * 删除组织信息
     *
     * @param id 组织信息主键
     * @return 结果
     */
    public int deleteUucDeptInfoById(Long id);

    /**
     * 批量删除组织信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUucDeptInfoByIds(Long[] ids);

    UucDeptInfo selectByDingtalkId(String deptId);

    List<String> selectDingDept();

    List<String> selectDeptByDeptType(String deptType);

    int deleteDingDepts(List<String> del);

    UucDeptInfo selectByDeptCode(String deptCode);

    List<UucDeptInfo> selectAllDepts();

    List<CmdbDeptDto> cmdbDeptList();
}
