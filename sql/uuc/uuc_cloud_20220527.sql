
-- ----------------------------
-- 项目用户中间表主键约束
-- ----------------------------
ALTER TABLE uuc_project_user ADD CONSTRAINT  upprimary PRIMARY KEY (user_code,project_code);
