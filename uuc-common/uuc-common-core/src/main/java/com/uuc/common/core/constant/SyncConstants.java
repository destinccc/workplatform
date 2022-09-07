package com.uuc.common.core.constant;

/**
 * @description: 钉钉同步到门户   门户同步到CMDB 关系存储的问题
 * @author: Destin
 * @create: 2022-04-15 16:06
 */
public class SyncConstants {


    // ------------ 人员同步 -------------
    // 人员 管理 人员 (下级)
    public static String  MANAGE_PEOPLE = "managePeoples";
    // 人员 管理 人员 (上级)
    public static String  LEADER_PEOPLE = "leaderPeoples";
    // 组织 包含 人员
    public static String CONTAINED_ORG = "containedOrgs";
    // 人员 管理 组织
    public static String  MANAGE_ORG = "manageOrgs";


    // ------------ 项目同步 -------------
    // 组织管理项目
    public static String  ORG_MANAGE_PROJECT = "orgManageProjects";
    // 组织使用项目
    public static String  ORG_USE_PROJECT = "orgUseProjects";
    // 人员参与项目
    public static String  EMPLOYEE_JOIN_PROJECT = "joinProjects";
    // 人员管理项目
    public static String  EMPLOYEE_MANAGE_PROJECT = "manageProjects";
    //人员维护项目
    public static String  EMPLOYEE_MAINTAIN_PROJECT = "maintainProjects";


    // ------------ 组织同步 -------------
    // 组织 管理 组织 (上级组织)
    public static String  PARENT_ORG = "parentOrgs";
    // 组织 管理 组织 (下级组织)
    public static String  CHILD_ORG = "childOrgs";


}
