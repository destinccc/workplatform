
-- ----------------------------
-- 组织表结构变更
-- ----------------------------
ALTER TABLE `uuc_cloud`.`uuc_dept_info`
ADD COLUMN `address` varchar(512) NULL COMMENT '地址' AFTER `ding_dept_id`;

ALTER TABLE `uuc_cloud`.`uuc_dept_info`
MODIFY COLUMN `organ_flag` char(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '如果organ 为1 ，则机构编码执行这个虚拟组织对应的真实机构code' AFTER `id`;

-- ----------------------------
-- 用户表结构变更
-- ----------------------------
ALTER TABLE `uuc_cloud`.`uuc_user_info`
ADD COLUMN `authorize_type` char(1) NOT NULL DEFAULT '0' COMMENT '授权类型' AFTER `post_name`,
ADD COLUMN `hired_time` datetime NULL COMMENT '入职日期' AFTER `authorize_type`,
ADD COLUMN `live_space` varchar(512) NULL COMMENT '住址' AFTER `hired_time`,
ADD COLUMN `remark` varchar(512) NULL COMMENT '备注' AFTER `live_space`,
ADD COLUMN `identity_card` varchar(18) NULL COMMENT '身份证号码' AFTER `remark`;

ALTER TABLE `uuc_cloud`.`uuc_user_info`
MODIFY COLUMN `id` bigint(20) NOT NULL COMMENT '主键ID' FIRST;

-- ----------------------------
-- 项目表结构变更
-- ----------------------------
ALTER TABLE `uuc_cloud`.`uuc_project`
ADD COLUMN `project_source` varchar(32) NULL COMMENT '项目来源（商机 commercial opportunityor合同contract\r\nor内部 internal project）' AFTER `update_by`,
ADD COLUMN `project_level` char(1) NULL COMMENT '项目分级(1公司 2部门)' AFTER `project_type`,
ADD COLUMN `applicant` varchar(100) NULL COMMENT '立项申请人' AFTER `project_level`,
ADD COLUMN `version` varchar(20) NULL COMMENT '项目版本' AFTER `applicant`,
ADD COLUMN `remark` varchar(1024) NULL COMMENT '备注' AFTER `version`;

-- ----------------------------
-- 项目人员关系表结构变更
-- ----------------------------
ALTER TABLE `uuc_cloud`.`uuc_project_user`
ADD COLUMN `maintener_flag` char(1) NULL COMMENT '是否项目维护人(0否1是)' AFTER `admin_flag`;

-- ----------------------------
-- 枚举类型表新增枚举类型
-- ----------------------------
INSERT INTO `uuc_cloud`.`sys_dict_type` (`dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('项目分级', 'sys_project_level', '0', 'admin', '2022-05-19 15:46:18', 'admin', '2022-05-19 15:48:28', NULL);
INSERT INTO `uuc_cloud`.`sys_dict_type` (`dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('项目类型', 'sys_project_type', '0', 'admin', '2022-05-19 15:48:19', 'admin', '2022-05-19 15:48:31', NULL);
INSERT INTO `uuc_cloud`.`sys_dict_type` (`dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('授权类型', 'sys_authorize_type', '0', 'admin', '2022-05-19 15:51:48', '', NULL, NULL);
INSERT INTO `uuc_cloud`.`sys_dict_type` (`dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('用户在职状态', 'sys_user_profess_status', '0', 'admin', '2022-05-19 16:00:06', '', NULL, NULL);

-- ----------------------------
-- 枚举数据表新增枚举类型数据
-- ----------------------------
INSERT INTO `uuc_cloud`.`sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (0, '公司', '1', 'sys_project_level', NULL, 'default', 'N', '0', 'admin', '2022-05-19 15:47:23', 'admin', '2022-05-19 15:55:04', NULL);
INSERT INTO `uuc_cloud`.`sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, '部门', '2', 'sys_project_level', NULL, 'default', 'N', '0', 'admin', '2022-05-19 15:47:32', '', NULL, NULL);
INSERT INTO `uuc_cloud`.`sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (0, '商机', '1', 'sys_project_type', NULL, 'default', 'N', '0', 'admin', '2022-05-19 15:49:48', '', NULL, 'commercial opportunityor');
INSERT INTO `uuc_cloud`.`sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, '合同', '2', 'sys_project_type', NULL, 'default', 'N', '0', 'admin', '2022-05-19 15:50:06', 'admin', '2022-05-19 15:50:37', 'contract');
INSERT INTO `uuc_cloud`.`sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, '内部', '0', 'sys_project_type', NULL, 'default', 'N', '0', 'admin', '2022-05-19 15:50:30', 'admin', '2022-05-19 15:53:37', 'internal  project');
INSERT INTO `uuc_cloud`.`sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (0, '内部', '0', 'sys_authorize_type', NULL, 'default', 'N', '0', 'admin', '2022-05-19 15:52:27', 'admin', '2022-05-19 15:52:39', 'INSIDER');
INSERT INTO `uuc_cloud`.`sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (0, '外部', '1', 'sys_authorize_type', NULL, 'default', 'N', '0', 'admin', '2022-05-19 15:52:58', '', NULL, 'OUTSIDER');
INSERT INTO `uuc_cloud`.`sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (0, '其他', '2', 'sys_authorize_type', NULL, 'default', 'N', '0', 'admin', '2022-05-19 15:53:23', '', NULL, 'OTHER');
INSERT INTO `uuc_cloud`.`sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (0, '在职', '0', 'sys_user_profess_status', NULL, 'default', 'N', '0', 'admin', '2022-05-19 16:00:32', '', NULL, 'EMPLOYEE');
INSERT INTO `uuc_cloud`.`sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, '离职', '1', 'sys_user_profess_status', NULL, 'default', 'N', '0', 'admin', '2022-05-19 16:00:44', '', NULL, 'QUIT');
INSERT INTO `uuc_cloud`.`sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, '试用期', '2', 'sys_user_profess_status', NULL, 'default', 'N', '0', 'admin', '2022-05-19 16:00:59', 'admin', '2022-05-19 16:01:02', 'PROBATION');

-- ----------------------------
-- 项目关联组织关系表
-- ----------------------------
CREATE TABLE `uuc_project_dept`  (
  `dept_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '组织编码',
  `project_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '项目编码',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;