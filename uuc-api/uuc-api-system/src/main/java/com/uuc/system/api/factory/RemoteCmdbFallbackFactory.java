package com.uuc.system.api.factory;


import com.uuc.common.core.domain.R;
import com.uuc.common.core.web.domain.AjaxResult;
import com.uuc.system.api.RemoteCmdbService;
import com.uuc.system.api.model.cmdb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RemoteCmdbFallbackFactory implements FallbackFactory<RemoteCmdbService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteCmdbFallbackFactory.class);

    @Override
    public RemoteCmdbService create(Throwable throwable) {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteCmdbService() {


            @Override
            public AjaxResult getProjectEntityList( String source) {
                return AjaxResult.error();
            }


            @Override
            public void saveUucProject(Map<String, Object> param, String inner) {

            }


            @Override
            public AjaxResult getUserEntityList(String source) {
                return AjaxResult.error();
            }

            @Override
            public void saveUucUser(Map<String, Object> param, String inner) {

            }

            @Override
            public AjaxResult getDeptEntityList(String source) {
                return AjaxResult.error();
            }

            @Override
            public void saveUucDept(Map<String, Object> param, String inner) {

            }

            @Override
            public void deleteUucEntityByModelKey(Map<String, Object> param, String inner) {

            }

            @Override
            public AjaxResult selectCmdbCategoryData(String inner) {
                return AjaxResult.error();
            }

            @Override
            public AjaxResult getModel(Long menuId, String source) {
                return AjaxResult.error();
            }

            @Override
            public AjaxResult startExtract(Long dataId, String source) {
                return AjaxResult.error();
            }

            @Override
            public AjaxResult getModelDetails(SelectModelDto dto, String source) {
                return AjaxResult.error("获取模型信息失败：" + throwable.getMessage());
            }

            @Override
            public R<List<ResourceInfoDto>> userResources(UserPermsRequest request, String source) {
                return R.fail("获取用户组织失败：" + throwable.getMessage());
            }

            @Override
            public R<List<DeptDto>> userDepts(UserDeptRequest request, String source) {
                return R.fail("获取用户组织失败：" + throwable.getMessage());
            }

            @Override
            public R<List<ProjectDto>> userProjects(UserProjectRequest request, String source) {
                return R.fail("获取用户项目失败：" + throwable.getMessage());
            }

            @Override
            public R<List<ProjectDto>> deptProjects(DeptResourceRequest request, String source) {
                return R.fail("获取组织项目失败：" + throwable.getMessage());
            }

            @Override
            public R<List<ResourceInfoDto>> deptResources(DeptResourceRequest request, String source) {
                return R.fail("获取组织资源失败：" + throwable.getMessage());
            }

            @Override
            public R<List<ResourceInfoDto>> projectResources(ProjectResourceRequest request, String source) {
                return R.fail("获取项目资源失败：" + throwable.getMessage());
            }

            @Override
            public R<List<DeptPo>> deptList(DeptPo deptPo, String source) {
                return R.fail("查询组织/部门列表信息失败：" + throwable.getMessage());
            }

            @Override
            public R<List<ProjectVo>> projectList(RequestBodyVo requestBodyVo, String source) {
                return R.fail("查询组织/部门下的项目列表信息失败：" + throwable.getMessage());
            }

            @Override
            public R<ResourceInfo> serverTotalInfo(RequestBodyVo requestBodyVo, String source) {
                return R.fail("查询云平台资源状态失败：" + throwable.getMessage());
            }

            @Override
            public R<List<VirtualMachineInfo>> serverList(RequestBodyVo requestBodyVo, String source) {
                return R.fail("查询虚拟机列表信息失败：" + throwable.getMessage());
            }

            @Override
            public R<ServerInfo> serverMonitorInfo(String serverId, String source) {
                return R.fail("查询资源告警信息失败：" + throwable.getMessage());
            }

            @Override
            public R<List<PhysicalMachineInfo>> physicalList(RequestBodyVo requestBodyVo, String source) {
                return R.fail("查询物理机列表失败：" + throwable.getMessage());
            }

            @Override
            public R<ServerInfo> hostMonitorInfo(String hostId, String source) {
                return R.fail("查询指定物理机监控信息失败：" + throwable.getMessage());
            }

            @Override
            public R<List<NetworkDevice>> networkDeviceList(RequestBodyVo requestBodyVo, String source) {
                return R.fail("查询网络设备列表信息失败：" + throwable.getMessage());
            }

            @Override
            public R<List<NetworkDevice>> securityDeviceList(RequestBodyVo requestBodyVo, String source) {
                return R.fail("查询安全设备列表信息失败：" + throwable.getMessage());
            }
        };
    }
}
