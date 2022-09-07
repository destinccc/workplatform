
-- ----------------------------
-- 组织表结构变更
-- ----------------------------
ALTER TABLE `uuc_cloud`.`uuc_dept_info`
ADD COLUMN `remark` varchar(1024) NULL COMMENT '备注' AFTER `address`;

-- ----------------------------
-- 配置项变更
-- ----------------------------
update `uuc_cloud`.`sys_config` set config_value = '^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$' where config_key = 'sys.user.acct.pattern';
INSERT INTO `uuc_cloud`.`sys_config` (`config_name`, `config_key`, `config_value`, `config_type`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('大屏标题', 'bigscreen', '数康云监控告警平台', 'Y', 'admin', '2022-05-25 15:44:51', 'admin', '2022-05-25 15:45:18', '勿删');

-- ----------------------------
-- 枚举项变更
-- ----------------------------
INSERT INTO `uuc_cloud`.`sys_dict_type` (`dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('大屏CPU服务器类型', 'big_cpu_type', '0', 'admin', '2022-05-25 17:34:05', '', NULL, NULL);
INSERT INTO `uuc_cloud`.`sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (0, '通用型N1', '0', 'big_cpu_type', NULL, 'default', 'N', '0', 'admin', '2022-05-25 17:36:46', '', NULL, NULL);
INSERT INTO `uuc_cloud`.`sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (0, '通用型N2', '1', 'big_cpu_type', NULL, 'default', 'N', '0', 'admin', '2022-05-25 17:36:54', '', NULL, NULL);
INSERT INTO `uuc_cloud`.`sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (0, '通用型N3', '2', 'big_cpu_type', NULL, 'default', 'N', '0', 'admin', '2022-05-25 17:37:03', '', NULL, NULL);

