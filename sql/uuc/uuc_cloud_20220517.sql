-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
# 2022/5/18添加关系类型
update  sys_dict_data set dict_sort = 4 where dict_label = 'M:N' and dict_type = 'cmdb_relation_type';
INSERT INTO sys_dict_data VALUES (null, 3, 'N:1', '4', 'cmdb_relation_type', NULL, NULL, 'N', '0', 'admin', '2021-09-22 13:43:59', NULL, NULL, 'N对1(N:1)');
#
