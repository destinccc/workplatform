package com.uuc.alarm.mapper;

import com.uuc.alarm.domain.UserNosys;
import com.uuc.common.datasource.annotation.Slave;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author uuc
 * @date 2022-08-01
 */
@Mapper
public interface UserNosysMapper {
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
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    int deleteUserNosysById(Integer id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteUserNosysByIds(Integer[] ids);

    @Slave
    @MapKey("id")
    Map<Integer, Map<String, Object>> selectUserNameByIds(@Param("ids") Set<Integer> ids);

    @Slave
    List<String> selectUserNameListByIds(@Param("ids") Set<Integer> ids);
}
