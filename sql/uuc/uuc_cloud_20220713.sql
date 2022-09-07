-- ----------------------------
-- 添加API版本控制菜单及权限按钮
-- ----------------------------
INSERT INTO sys_menu VALUES (120, '版本管理', 1, 10, 'version', 'system/version/index', 1, 0, 'C', '0', '0', NULL, 'clipboard', 'admin', '2022-07-12 16:47:46', 'admin', '2022-07-12 16:48:17', '', 'system:version:list', 'uuc', NULL);
INSERT INTO sys_menu VALUES (121, '版本编辑', 120, 1, '', NULL, 1, 0, 'F', '0', '0', NULL, '#', 'admin', '2022-07-13 14:54:23', '', NULL, '', 'system:version:edit', 'uuc', NULL);

-- ----------------------------
-- 添加API版本控制表
-- ----------------------------
CREATE TABLE uuc_api_version (
   id bigint(10) NOT NULL AUTO_INCREMENT COMMENT '自增id',
   api_version varchar(255) NOT NULL COMMENT '接口版本号',
   activate char(1) DEFAULT '1' COMMENT '是否激活 0 否 1 是',
   create_time datetime DEFAULT NULL COMMENT '创建时间',
   update_time datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
   operator varchar(255) DEFAULT NULL COMMENT '操作人',
   remark varchar(255) DEFAULT NULL COMMENT '备注',
   PRIMARY KEY (id),
   UNIQUE KEY avs (api_version)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='API版本控制';

INSERT INTO uuc_api_version VALUES (1, 'v1', '1', '2022-07-13 10:56:37', '2022-07-13 12:10:31', 'UUC', 'v1版本');

-- ----------------------------
-- 接口操作类型字典数据
-- ----------------------------
INSERT INTO sys_dict_data VALUES (NULL, 0, '查询', '10', 'sys_oper_type', NULL, 'primary', 'N', '0', 'admin', '2022-07-14 14:49:37', 'admin', '2022-07-14 14:51:34', NULL);

ALTER TABLE sys_oper_log ADD COLUMN client_id varchar(512) DEFAULT NULL COMMENT '客户端' AFTER dept_name;
ALTER TABLE sys_oper_log ADD COLUMN oper_desc varchar(512) DEFAULT NULL COMMENT '请求描述' AFTER oper_param;
