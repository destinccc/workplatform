-- ----------------------------
-- 枚举项变更
-- ----------------------------
update uuc_cloud.sys_dict_data set dict_value = 'COMMERCIAL_OPPORTUNITY' where  dict_type ='sys_project_type' and dict_label = '商机';
update uuc_cloud.sys_dict_data set dict_value = 'CONTRACT' where  dict_type ='sys_project_type' and dict_label = '合同';
update uuc_cloud.sys_dict_data set dict_value = 'INTERNAL_PROJECT' where  dict_type ='sys_project_type' and dict_label = '内部';

update uuc_cloud.sys_dict_data set dict_value = 'INSIDER' where  dict_type ='sys_authorize_type' and dict_label = '内部';
update uuc_cloud.sys_dict_data set dict_value = 'OUTSIDER' where  dict_type ='sys_authorize_type' and dict_label = '外部';
update uuc_cloud.sys_dict_data set dict_value = 'OTHER' where  dict_type ='sys_authorize_type' and dict_label = '其他';

update uuc_cloud.sys_dict_data set dict_value = 'COMPANY' where  dict_type ='sys_project_level' and dict_label = '公司';
update uuc_cloud.sys_dict_data set dict_value = 'DEPARTMENT' where  dict_type ='sys_project_level' and dict_label = '部门';


-- ----------------------------
-- 枚举项变更影响表数据变更
-- ----------------------------
ALTER TABLE `uuc_cloud`.`uuc_user_info`
MODIFY COLUMN `authorize_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'INSIDER' COMMENT '授权类型' AFTER `post_name`;

update uuc_cloud.uuc_project set project_source = 'COMMERCIAL_OPPORTUNITY' where project_source = '1';
update uuc_cloud.uuc_project set project_source = 'CONTRACT' where project_source = '2';
update uuc_cloud.uuc_project set project_source = 'INTERNAL_PROJECT' where project_source = '0';

update uuc_cloud.uuc_user_info set authorize_type = 'INSIDER' where authorize_type = '0';
update uuc_cloud.uuc_user_info set authorize_type = 'OUTSIDER' where authorize_type = '1';
update uuc_cloud.uuc_user_info set authorize_type = 'OTHER' where authorize_type = '2';

ALTER TABLE `uuc_cloud`.`uuc_project`
MODIFY COLUMN `project_level` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目分级(1公司 2部门)' AFTER `project_type`;
update uuc_cloud.uuc_project set project_level = 'COMPANY' where project_level = '1';
update uuc_cloud.uuc_project set project_level = 'DEPARTMENT' where project_level = '2';



