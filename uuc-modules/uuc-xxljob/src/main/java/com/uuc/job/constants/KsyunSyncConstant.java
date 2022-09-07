package com.uuc.job.constants;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @description: 金山云资源保存在cmdb 数据表 sys_integration_data 中 的固定映射值
 * @author: Destin
 */
public class KsyunSyncConstant {

    public static final Map<String,Long> KSYUN_DATA_MAPPING  = Maps.newHashMap();

    static {
        // 云主机
        KSYUN_DATA_MAPPING.put("VirtualResource",1L);
        // 镜像
        KSYUN_DATA_MAPPING.put("ImageResource",10L);
        // VPC
        KSYUN_DATA_MAPPING.put("VpcResource",11L);
        // 网卡
        KSYUN_DATA_MAPPING.put("NetworkResource",12L);
        // 安全组资源
        KSYUN_DATA_MAPPING.put("SecurityGroupResource",13L);
        // 云硬盘
        KSYUN_DATA_MAPPING.put("VolumesResource",14L);
        // 弹性IP
        KSYUN_DATA_MAPPING.put("EipResource",15L);
        // 本地磁盘
        KSYUN_DATA_MAPPING.put("LocalVolumesResource",16L);
        // 云Mysql
        KSYUN_DATA_MAPPING.put("MysqlResource",20L);
        // Mysql安全组
        KSYUN_DATA_MAPPING.put("MysqlSecurityGroupResource",21L);
        // 子网
        KSYUN_DATA_MAPPING.put("SubnetResource",22L);
        // Redis
        KSYUN_DATA_MAPPING.put("RedisResource",30L);
        // Redis安全组
        KSYUN_DATA_MAPPING.put("RedisSecurityGroupResource",31L);
    }

}
