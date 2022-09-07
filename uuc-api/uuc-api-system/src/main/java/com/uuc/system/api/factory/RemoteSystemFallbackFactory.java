package com.uuc.system.api.factory;


import com.uuc.common.core.domain.R;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.system.api.RemoteSystemService;
import com.uuc.system.api.domain.UserAdminDeptVO;
import com.uuc.system.api.domain.UucUserVO;
import com.uuc.system.api.model.SysConfig;
import com.uuc.system.api.model.SysMenu;
import com.uuc.system.api.model.UucDeptInfo;
import com.uuc.system.api.model.UucUserInfo;
import org.apache.commons.compress.utils.Lists;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 用户服务降级处理
 *
 * @author uuc
 */
@Component
public class RemoteSystemFallbackFactory  implements FallbackFactory<RemoteSystemService> {


    private static final Logger log = LoggerFactory.getLogger(RemoteSystemFallbackFactory.class);

    @Override
    public RemoteSystemService create(Throwable throwable)
    {
        log.error("日志服务调用失败:{}", throwable.getMessage());
        return new RemoteSystemService()
        {

            @Override
            public AjaxResult selectDeptByDingtalkId(String deptId, String source) {
                return AjaxResult.error();
            }

            @Override
            public void saveDingtalkDept(UucDeptInfo updateDept, String inner) {

            }

            @Override
            public AjaxResult getDingTalkDept(String source) {
                return AjaxResult.error();
            }

            @Override
            public void deleteDingtalkDept(Map param, String inner) {

            }

            @Override
            public AjaxResult getDingTalkUser(String dingUid, String inner) {
                return AjaxResult.error();
            }

            @Override
            public void saveDingtalkUser(UucUserInfo updateUser, String inner) {

            }

            @Override
            public AjaxResult getDingUser(String source) {
                return AjaxResult.error();
            }

            @Override
            public void deleteDingUser(Map deleteCodes, String inner) {

            }

            @Override
            public void syncUpdateUser(String inner) {

            }

            @Override
            public AjaxResult getUucProjects(String source) {
                return AjaxResult.error();
            }

            @Override
            public AjaxResult getUucUsers(String source) {
                return AjaxResult.error();
            }

            @Override
            public AjaxResult getUucDepts(String source) {
                return AjaxResult.error();
            }

            @Override
            public AjaxResult getDefaultRole(String source) {
                return AjaxResult.error("获取默认角色失败:" + throwable.getMessage());
            }

            @Override
            public AjaxResult sync(SysMenu sysMenuVo, String source) {
                return AjaxResult.error("同步菜单失败:" + throwable.getMessage());
            }
            @Override
            public AjaxResult syncUpdate(SysMenu sysMenuVo, String source) {
                return AjaxResult.error("同步修改菜单失败:" + throwable.getMessage());
            }

            @Override
            public AjaxResult syncDelete(Long menuId, String source) {
                return AjaxResult.error("同步删除菜单失败:" + throwable.getMessage());
            }

            @Override
            public AjaxResult listByCond(SysMenu sysMenuVo, String source) {
                return AjaxResult.error("查询菜单失败:" + throwable.getMessage());
            }

            @Override
            public AjaxResult initUpdateDeptLevel(String source) {
                return AjaxResult.error("全量更新组织level失败:" + throwable.getMessage());
            }

            @Override
            public AjaxResult updateConfigByKey(SysConfig sysConfig, String source) {
                return AjaxResult.error("更新配置项失败:" + throwable.getMessage());
            }

            @Override
            public AjaxResult selectAllDepts(String source) {
                return AjaxResult.error("获取全量组织失败:" + throwable.getMessage());
            }

            @Override
            public AjaxResult getAdminUserList(String source) {
                return AjaxResult.error("获取全量管理员ID失败:" + throwable.getMessage());
            }

            @Override
            public AjaxResult getDeptAdminUserList(String source) {
                return AjaxResult.error("获取组织管理元用户失败:" + throwable.getMessage());
            }

            @Override
            public AjaxResult getAdminDeptList(UserAdminDeptVO vo, String source) {
                return AjaxResult.error("查询用户管理的组织:" + throwable.getMessage());
            }

            @Override
            public AjaxResult getUserList(UucUserVO vo, String source) {
                return AjaxResult.error("获取用户列表失败:" + throwable.getMessage());
            }

            @Override
            public R<List<String>> getEffectiveList(String source) {
                return R.fail(Lists.newArrayList(),"获取API有效版本失败:" + throwable.getMessage());
            }

            @Override
            public void saveOrUpdateDept(UucDeptInfo updateDept, String source) {

            }

            @Override
            public AjaxResult getDeptByDeptType(String deptType, String source) {
                return AjaxResult.error();
            }

            @Override
            public AjaxResult getUserByUserId(String userId, String userType, String inner) {
                return AjaxResult.error();
            }

            @Override
            public void saveOrUpdateUser(UucUserInfo updateUser, String inner) {

            }

            @Override
            public AjaxResult getUserByUserType(String userType, String source) {
                return AjaxResult.error();
            }
        };

    }

}
