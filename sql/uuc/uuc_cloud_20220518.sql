-- ----------------------------
-- 新建客户端密钥字段
-- ----------------------------
alter table uuc_cloud.uuc_model_info add COLUMN client_secret varchar(255) DEFAULT NULL COMMENT '客户端密钥';

-- ----------------------------
-- 初始化各户端密钥
-- ----------------------------
update uuc_cloud.uuc_model_info set client_secret='tMU7J1WoIYxrNaJ0' where client_id='uuc';
update uuc_cloud.uuc_model_info set client_secret='5sdKbV0xVzQb4Stw' where client_id='cmdb';
update uuc_cloud.uuc_model_info set client_secret='SR4f0zCbAJ41ue70' where client_id='monitor';
update uuc_cloud.uuc_model_info set client_secret='zGI54KL4rYV4muZE' where client_id='workOrder';
update uuc_cloud.uuc_model_info set client_secret='If915JK85BiL45B8' where client_id='cmp';
update uuc_cloud.uuc_model_info set client_secret='OYLFnyHl0eku9qtQ' where client_id='log';
update uuc_cloud.uuc_model_info set client_secret='IfmCEP0W5ngsVxiV' where client_id='wiki';