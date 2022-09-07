package com.uuc.common.core.constant;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/*
 * @Author Destin
 * @Description  CMDB 模型标识
 **/
public class CmdbConstants {


    // 约定的模型    组织 人员 项目 的标识
    public static final String Bind_Organization = "Organization";
    public static final String Bind_Employee = "Employee";
    public static final String Bind_Project = "Project";
    public static final String VirtualMachine = "VirtualMachine"; //云主机
    public static final String PhysicalServer = "PhysicalServer";//物理服务器
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
    public static final String Bind_SubnetResource = "SubnetResource";
    // Redis
    public static final String Bind_RedisResource = "Redis";
    // Redis安全组
    public static final String Bind_RedisSecurityGroupsResource = "RedisSecurityGroupsResource";



}



