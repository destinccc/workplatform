package com.uuc.system.uuc.mapper;

import java.util.List;
import com.uuc.system.api.model.UucUserInfo;
import com.uuc.system.uuc.domain.SignatureUserVo;

/**
 * 用户信息Mapper接口
 *
 * @author uuc
 * @date 2022-04-01
 */
public interface UucUserInfoMapper
{
    /**
     * 查询用户信息
     *
     * @param id 用户信息主键
     * @return 用户信息
     */
    public UucUserInfo selectUucUserInfoById(Long id);
    /**
     * 查询用户信息
     *
     * @param uid 外部用户id
     * @return 用户信息
     */
    public UucUserInfo selectUucUserInfoByUid(String uid);

    /**
     * 查询用户信息列表
     *
     * @param uucUserInfo 用户信息
     * @return 用户信息集合
     */
    public List<UucUserInfo> selectUucUserInfoList(UucUserInfo uucUserInfo);

    /**
     * 新增用户信息
     *
     * @param uucUserInfo 用户信息
     * @return 结果
     */
    public int insertUucUserInfo(UucUserInfo uucUserInfo);

    /**
     * 修改用户信息
     *
     * @param uucUserInfo 用户信息
     * @return 结果
     */
    public int updateUucUserInfo(UucUserInfo uucUserInfo);

    /**
     * 删除用户信息
     *
     * @param id 用户信息主键
     * @return 结果
     */
    public int deleteUucUserInfoById(Long id);

    /**
     * 批量删除用户信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUucUserInfoByIds(Long[] ids);

    UucUserInfo selectUucUserInfoByDingUid(String extendId);

    List<String> selectDingUser();

    List<String> getUserByUserType(String userType);

    int deleteDingUserByCode(String userCode);

    /**
     * 检查手机号是否唯一
     * @param phone
     * @return
     */
    public int checkPhoneUnique(String phone);

    List<UucUserInfo> selectAllUser();

    List<UucUserInfo> selectDingUserList();

    int deleteDingUucUserInfoByIds(List<Long> deleteDingUser);

    List<String> getAdminUserList();

    /**
     * 根据手机同步用户中心userId
     * @param uucUserInfo
     * @return
     */
    int syncUserByPhone(UucUserInfo uucUserInfo);

    List<Long> selectUucNotHasRoleUser();

    List<SignatureUserVo> listAllUser();
}
