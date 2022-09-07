package com.uuc.job.dingtalk.constant;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class DingtalkRelConstants {

    // 部门的领导 <用户id,部门id>    员工管理多个部门
    public static final Map<String, List<Long>> DeptLeader = Maps.newConcurrentMap();

    // 约定的模型    组织 人员 项目 的标识
    public static final String Bind_Organization = "Organization";
    public static final String Bind_Employee = "Employee";
    public static final String Bind_Project = "Project";

    // 金山云 VPC 镜像 安全组 云硬盘 网卡 的标识
    public static final String Bind_VirtualMachine = "VirtualMachine";
    public static final String Bind_ImageResource = "ImageResource";
    public static final String Bind_VpcResource = "VpcResource";
    public static final String Bind_NetworkResource = "NetworkResource";
    public static final String Bind_SecurityGroupsResource = "SecurityGroupsResource";
    public static final String Bind_VolumesResource = "VolumesResource";
    public static final String Bind_EipResource = "EipResource";
    // 本地硬盘
    public static final String Bind_LocalVolumesResource = "LocalVolumesResource";
    // Mysql
    public static final String Bind_MysqlResource = "Mysql";
    // mysql安全组
    public static final String Bind_MysqlSecurityGroupsResource = "MysqlSecurityGroupsResource";
    // 子网
    public static final String Bind_SubnetResource= "SubnetResource";
    // Redis
    public static final String Bind_RedisResource= "Redis";
    // Redis安全组
    public static final String Bind_RedisSecurityGroupsResource = "RedisSecurityGroupsResource";

    // 约定的基础模型,需要做普通用户权限控制
    public static final List<String> coreModelLists = Lists.newArrayList(Bind_Employee,Bind_Organization,Bind_Project,Bind_VirtualMachine,Bind_ImageResource,
            Bind_VpcResource,Bind_NetworkResource,Bind_SecurityGroupsResource,Bind_VolumesResource,Bind_EipResource,Bind_LocalVolumesResource,
            Bind_MysqlResource,Bind_MysqlSecurityGroupsResource,Bind_SubnetResource);

    // 约定的关系  员工参与项目    员工属于组织  组织立项项目 组织管理组织  云主机包含其他模型
    public static final String Rel_Join = "Join";
    public static final String Rel_BelongTo = "BelongTo";
    public static final String Rel_Manage = "Manage";
    public static final String Rel_Contains = "Contains";
    public static final String Rel_Establish= "EstablishProject";

    // 钉钉同步  人员与部门的关系
    public static final String DingTalkUserDept = "deptIdList";
    // 钉钉同步  人员与部门的关系 人员管理部门
    public static final String DingtalkUserLeaderDepts = "leadDeptList";
    public static final String DingtalkUserLeaderUser = "leaderUserId";

    // 金山云同步 虚拟机与网卡的关系
    public static final String KsYunNetworkInterfaceSet = "NetworkInterfaceSet";
    // 弹性Eip与 网卡
    public static final String KsYunEipNetworkInterfaceSet = "EipNetworkInterfaceSet";
    // 金山云同步 虚拟机与Vpc的关系
    public static final String KsYunVpcSet = "VpcSet";
    // 金山云同步 虚拟机与子网的关系
    public static final String KsYunSubnetSet = "SubnetSet";
    // 金山云同步 虚拟机与镜像的关系
    public static final String KsYunImageSet = "ImageSet";
    // 金山云同步 虚拟机与安全组的关系
    public static final String KsYunSecurityGroupSet = "SecurityGroupSet";

    // 金山云同步 虚拟机与云硬盘的关系
    public static final String KsYunVolumesInstanceIds = "volumeInstanceIds";

    // 本地磁盘与云主机关系
    public static final String KsYunLocalVolumesInstanceIdSet = "InstanceIdSet";

    // mysql 与安全组
    public static final String KsYunMysqlSecurityGroupsSet= "MysqlSecurityGroupSet";
    // Mysql 与 VPC
    public static final String KsYunMysqlVpcSet= "MysqlVpcSet";
    // Mysql 与 子网
    public static final String KsYunMysqlSubnetSet= "MysqlSubnetSet";

    // Redis安全组包含的Redis实例
    public static final String KsYunRedisGroupRedisSet= "RedisSet";

    // Redis 与 VPC
    public static final String KsYunRedisVPCSet= "RedisVPCSet";
    // Redis 与 子网
    public static final String KsYunRedisSubnetSet= "RedisSubnetSet";

}
