package com.uuc.system.uuc.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import com.uuc.common.core.constant.SyncConstants;
import com.uuc.common.core.constant.UserConstants;
import com.uuc.common.core.enums.SyncBeanType;
import com.uuc.common.core.exception.CheckedException;
import com.uuc.common.core.utils.DateUtils;
import com.uuc.common.core.utils.LongUtils;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.core.utils.uuid.IdUtils;
import com.uuc.common.security.utils.SecurityUtils;
import com.uuc.system.api.model.UucDeptInfo;
import com.uuc.system.api.model.UucOrganInfo;
import com.uuc.system.api.model.UucUserDept;
import com.uuc.system.api.uuc.dto.CmdbDeptDto;
import com.uuc.system.domain.vo.TreeSelect;
import com.uuc.system.uuc.mapper.UucDeptInfoMapper;
import com.uuc.system.uuc.mapper.UucOrganInfoMapper;
import com.uuc.system.uuc.mapper.UucProjectDeptMapper;
import com.uuc.system.uuc.mapper.UucUserDeptMapper;
import com.uuc.system.uuc.sync.service.ToSyncBodyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 组织信息Service业务层处理
 *
 * @author uuc
 * @date 2022-04-01
 */
@Lazy
@Service
@Slf4j
public class UucDeptInfoService {
    @Autowired
    private UucDeptInfoMapper uucDeptInfoMapper;

    @Autowired
    private UucOrganInfoMapper uucOrganInfoMapper;

    @Autowired
    private UucUserDeptMapper uucUserDeptMapper;

    @Autowired
    private UucProjectDeptMapper uucProjectDeptMapper;

    @Autowired
    private ToSyncBodyService toSyncBodyService;

    /**
     * 查询组织信息
     *
     * @param id 组织信息主键
     * @return 组织信息
     */

    public UucDeptInfo selectUucDeptInfoById(Long id) {
        UucDeptInfo deptInfo = uucDeptInfoMapper.selectUucDeptInfoById(id);
        if (Objects.isNull(deptInfo)) {
            return null;
        }
        // 查询组织负责人信息
        UucUserDept userDept = new UucUserDept();
        userDept.setDeptCode(String.valueOf(id));
        userDept.setAdminFlag("1");
        deptInfo.setUserDeptList(uucUserDeptMapper.selectUucUserDeptList(userDept));
//        // 如果存在机构信息，则查询并赋值
//        if (UserConstants.DEPT_ORG_FLAG.equals(deptInfo.getOrganFlag())) {
//            deptInfo.setOrganInfo(uucOrganInfoMapper.selectUucOrganInfoById(id));
//        }
        return deptInfo;
    }

    /**
     * 查询组织信息列表
     *
     * @param uucDeptInfo 组织信息
     * @return 组织信息
     */

    public List<UucDeptInfo> selectUucDeptInfoList(UucDeptInfo uucDeptInfo) {
        return uucDeptInfoMapper.selectUucDeptInfoList(uucDeptInfo);
    }

    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */

    public List<UucDeptInfo> buildDeptTree(List<UucDeptInfo> depts) {
        List<UucDeptInfo> returnList = new ArrayList<UucDeptInfo>();
        List<String> tempList = new ArrayList<String>();
        for (UucDeptInfo dept : depts) {
            tempList.add(String.valueOf(dept.getId()));
        }
        for (UucDeptInfo dept : depts) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentCode())) {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = depts;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<UucDeptInfo> list, UucDeptInfo t) {
        // 得到子节点列表
        List<UucDeptInfo> childList = getChildList(list, t);
        t.setChildren(childList);
        for (UucDeptInfo tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<UucDeptInfo> getChildList(List<UucDeptInfo> list, UucDeptInfo t) {
        List<UucDeptInfo> tlist = new ArrayList<UucDeptInfo>();
        Iterator<UucDeptInfo> it = list.iterator();
        while (it.hasNext()) {
            UucDeptInfo n = (UucDeptInfo) it.next();
            if (StringUtils.isNotNull(n.getParentCode()) && n.getParentCode().equals(String.valueOf(t.getId()))) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<UucDeptInfo> list, UucDeptInfo t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }


    public List<TreeSelect> buildDeptTreeSelect(List<UucDeptInfo> depts) {
        List<UucDeptInfo> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 新增组织信息
     *
     * @param uucDeptInfo 组织信息
     * @return 结果
     */

    @Transactional(rollbackFor = Exception.class)
    public int insertUucDeptInfo(UucDeptInfo uucDeptInfo) {
//        UucDeptInfo info = uucDeptInfoMapper.selectUucDeptInfoById(Long.valueOf(uucDeptInfo.getParentCode()));
//        // 如果父节点不为正常状态,则不允许新增子节点
//        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus()))
//        {
//            throw new ServiceException("部门停用，不允许新增");
//        }
        Date nowData = DateUtils.getNowDate();
        uucDeptInfo.setCreateTime(nowData);
        uucDeptInfo.setCreateBy(String.valueOf(SecurityUtils.getUserId()));

        Long deptCode = IdUtils.snowflakeId();
        uucDeptInfo.setId(deptCode);
        // 如果是机构，须插入机构表数据
//        if (UserConstants.DEPT_ORG_FLAG.equals(uucDeptInfo.getOrganFlag())) {
//            UucOrganInfo organInfo = uucDeptInfo.getOrganInfo();
//            organInfo.setId(deptCode);
//            organInfo.setOrganName(uucDeptInfo.getDeptName());
//            organInfo.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
//            organInfo.setCreateTime(nowData);
//            uucOrganInfoMapper.insertUucOrganInfo(organInfo);
//            //设置机构编码，暂时不知道有啥用
//            uucDeptInfo.setOrganCode(deptCode);
//        }
        // 更新组织负责人信息
        List<UucUserDept> uucUserDeptList = uucDeptInfo.getUserDeptList();
        if (CollectionUtils.isNotEmpty(uucUserDeptList)) {
            // 校验当前负责人是不是属于当前组织
            List<String> userCodeList = new ArrayList<>();
            for (UucUserDept udpt : uucUserDeptList) {
                userCodeList.add(udpt.getUserCode());
            }
            int count = uucUserDeptMapper.selectUucUserInDeptCounts(userCodeList, String.valueOf(deptCode));
            if (count != userCodeList.size()) {
                throw new CheckedException("非当前组织的人员不能设置为负责人！");
            }
            // 更新当前组织负责人（清空关系表负责人状态）
            UucUserDept userDept = new UucUserDept();
            userDept.setAdminFlag("0");
            userDept.setDeptCode(String.valueOf(uucDeptInfo.getId()));
            uucUserDeptMapper.updateUucUserDeptAdminFlagByDeptCode(userDept);

            for (UucUserDept udpt : uucUserDeptList) {
                udpt.setDeptCode(String.valueOf(deptCode));
                udpt.setAdminFlag("1");
                uucUserDeptMapper.updateUucUserDeptAdminFlag(udpt);
            }
        }

        // 插入组织信息
        String parentCode = uucDeptInfo.getParentCode();
        if (StringUtils.isNotEmpty(parentCode)) {
            // 设置祖级列表
            UucDeptInfo info = uucDeptInfoMapper.selectUucDeptInfoById(Long.valueOf(parentCode));
            uucDeptInfo.setAncestors(info.getAncestors() + "," + parentCode);
            uucDeptInfo.setLevel(Long.valueOf(uucDeptInfo.getAncestors().split(",").length));
        } else {
            uucDeptInfo.setAncestors(String.valueOf(deptCode));
            uucDeptInfo.setLevel(1L);
        }
        int i = uucDeptInfoMapper.insertUucDeptInfo(uucDeptInfo);
        if (i > 0) {
            toSyncBodyService.insertSyncBody(uucDeptInfo, SyncBeanType.DEPTSYNCBEAN.getCode());
        }
        return i;
    }

    /**
     * 修改组织信息
     *
     * @param uucDeptInfo 组织信息
     * @return 结果
     */

    @Transactional(rollbackFor = Exception.class)
    public int updateUucDeptInfo(UucDeptInfo uucDeptInfo) {
        Date nowData = DateUtils.getNowDate();
        uucDeptInfo.setUpdateTime(nowData);
//        // 不管是不是机构，先删除机构表数据再新增
//        uucOrganInfoMapper.deleteUucOrganInfoById(uucDeptInfo.getId());
//        // 如果是机构，则同步更新机构表
//        if (UserConstants.DEPT_ORG_FLAG.equals(uucDeptInfo.getOrganFlag())) {
//            UucOrganInfo organInfo = uucDeptInfo.getOrganInfo();
//            organInfo.setOrganName(uucDeptInfo.getDeptName());
//            organInfo.setId(uucDeptInfo.getId());
//            organInfo.setCreateBy(uucDeptInfo.getCreateBy());
//            organInfo.setCreateTime(uucDeptInfo.getCreateTime());
//            organInfo.setUpdateBy(String.valueOf(SecurityUtils.getUserId()));
//            organInfo.setUpdateTime(nowData);
//            uucOrganInfoMapper.insertUucOrganInfo(organInfo);
//        }
        UucDeptInfo oldDept = uucDeptInfoMapper.selectUucDeptInfoById(uucDeptInfo.getId());
        UucUserDept uucUserDept = new UucUserDept();
        uucUserDept.setAdminFlag("1");
        uucUserDept.setDeptCode(String.valueOf(uucDeptInfo.getId()));
        List<UucUserDept> uucUserDepts = uucUserDeptMapper.selectUucUserDeptList(uucUserDept);
        oldDept.setUserDeptList(uucUserDepts);
        // 更新负责人关联
        UucUserDept userDept = new UucUserDept();
        userDept.setAdminFlag("0");
        userDept.setDeptCode(String.valueOf(uucDeptInfo.getId()));
        uucUserDeptMapper.updateUucUserDeptAdminFlagByDeptCode(userDept);

        List<UucUserDept> uucUserDeptList = uucDeptInfo.getUserDeptList();
        if (CollectionUtils.isNotEmpty(uucUserDeptList)) {
            // 校验当前负责人是不是属于当前组织
            List<String> userCodeList = new ArrayList<>();
            for (UucUserDept udpt : uucUserDeptList) {
                userCodeList.add(udpt.getUserCode());
            }
            int count = uucUserDeptMapper.selectUucUserInDeptCounts(userCodeList, String.valueOf(uucDeptInfo.getId()));
            if (count != userCodeList.size()) {
                throw new CheckedException("非当前组织的人员不能设置为负责人！");
            }

            for (UucUserDept udpt : uucUserDeptList) {
                udpt.setDeptCode(String.valueOf(uucDeptInfo.getId()));
                udpt.setAdminFlag("1");
                uucUserDeptMapper.updateUucUserDeptAdminFlag(udpt);
            }
        }

        // 同步更新祖级列表和层级
        String parentCode = uucDeptInfo.getParentCode();
        if (StringUtils.isNotEmpty(parentCode) && !"0".equals(parentCode)) {
            UucDeptInfo deptInfo = uucDeptInfoMapper.selectUucDeptInfoById(Long.valueOf(parentCode));
            String ancestors = deptInfo.getAncestors();
            uucDeptInfo.setAncestors(ancestors + "," + uucDeptInfo.getId());
            uucDeptInfo.setLevel(Long.valueOf(uucDeptInfo.getAncestors().split(",").length));
        } else {
            uucDeptInfo.setAncestors(String.valueOf(uucDeptInfo.getId()));
            uucDeptInfo.setLevel(1L);
        }
        int i = uucDeptInfoMapper.updateUucDeptInfo(uucDeptInfo);
        if (i > 0) {
            toSyncBodyService.updateSyncBody(oldDept, uucDeptInfo, SyncBeanType.DEPTSYNCBEAN.getCode());
        }
        return i;
    }

    /**
     * 批量删除组织信息
     *
     * @param ids 需要删除的组织信息主键
     * @return 结果
     */

//    @Transactional(rollbackFor = Exception.class)
//    public int deleteUucDeptInfoByIds(Long[] ids) {
////        uucOrganInfoMapper.deleteUucOrganInfoByIds(ids);
//        // 存在组织，不允许删除
////        uucDeptInfoMapper.selectUucDeptInfoList()
//        // 存在组织人员关系，不允许删除
//        String[] deptCodes = LongUtils.caseLongArr2StringArr(ids);
//        if (uucUserDeptMapper.checkUserAndDeptRelationsExist(deptCodes) > 0) {
//            throw new CheckedException("存在组织人员关系，不允许删除!");
//        }
//        // 存在组织项目关系，不允许删除
//        if (uucProjectDeptMapper.checkProjectAndDeptRelationsExist(deptCodes) > 0) {
//            throw new CheckedException("存在组织项目关系，不允许删除!");
//        }
//        return uucDeptInfoMapper.deleteUucDeptInfoByIds(ids);
//    }

    /**
     * 删除组织信息信息
     *
     * @param id 组织信息主键
     * @return 结果
     */

    @Transactional(rollbackFor = Exception.class)
    public int deleteUucDeptInfoById(Long id) {
//        uucOrganInfoMapper.deleteUucOrganInfoById(id);
        String deptCode = String.valueOf(id);
        // 存在子组织，不允许删除
        UucDeptInfo deptInfo = new UucDeptInfo();
        deptInfo.setParentCode(deptCode);
        List<UucDeptInfo> childDeptList = uucDeptInfoMapper.selectUucDeptInfoList(deptInfo);
        if (CollectionUtils.isNotEmpty(childDeptList)) {
            throw new CheckedException("存在下级组织，不允许删除!");
        }
        // 存在组织人员关系，不允许删除
        if (uucUserDeptMapper.checkUserAndDeptRelationsExist(deptCode) > 0) {
            throw new CheckedException("存在组织人员关系，不允许删除!");
        }
        // 存在组织项目关系，不允许删除
        if (uucProjectDeptMapper.checkProjectAndDeptRelationsExist(deptCode) > 0) {
            throw new CheckedException("存在组织项目关系，不允许删除!");
        }
        int i = uucDeptInfoMapper.deleteUucDeptInfoById(id);
        if (i > 0) {
            List<String> idList = new ArrayList<>();
            if (id != null) {
                idList.add(String.valueOf(id));
            }
            toSyncBodyService.deleteSyncBody(idList, SyncBeanType.DEPTSYNCBEAN.getCode());
        }
        return i;
    }

    public UucDeptInfo selectByDingtalkId(String deptId) {
        return uucDeptInfoMapper.selectByDingtalkId(deptId);
    }

    public void saveDingDept(UucDeptInfo uucDeptInfo) {

        Long id = uucDeptInfo.getId();
        if (Objects.isNull(id)) {
            if (Objects.isNull(selectByDingtalkId(uucDeptInfo.getDingDeptId()))) {
                // 钉钉的部门ID保存为uuc部门ID
                uucDeptInfo.setId(Long.parseLong(uucDeptInfo.getDingDeptId()));
//                uucDeptInfo.setOrganFlag("-1");
                uucDeptInfo.setStatus("0");
                uucDeptInfo.setDeptType("01");
                log.info("新增钉钉部门: " + JSONUtil.toJsonStr(uucDeptInfo));
                uucDeptInfoMapper.insertUucDeptInfo(uucDeptInfo);
            }
        } else {
            log.info("更新钉钉部门: " + JSONUtil.toJsonStr(uucDeptInfo));
            uucDeptInfoMapper.updateUucDeptInfo(uucDeptInfo);
        }
    }

    public void saveOrUpdateDept(UucDeptInfo uucDeptInfo) {
        Long id = uucDeptInfo.getId();
        if (Objects.isNull(id)) {
            // 部门ID保存为uuc部门ID
            uucDeptInfo.setId(IdUtils.snowflakeId());
            uucDeptInfo.setStatus("0");
            //祖级部门取的是id而不是deptCode
            StringBuilder sb = new StringBuilder();
            sb = sb.append(uucDeptInfo.getAncestors()).append(",").append(id);
            uucDeptInfo.setAncestors(sb.toString());
            log.info("新增部门: " + JSONUtil.toJsonStr(uucDeptInfo));
            uucDeptInfoMapper.insertUucDeptInfo(uucDeptInfo);
        } else {
            log.info("更新部门: " + JSONUtil.toJsonStr(uucDeptInfo));
            uucDeptInfoMapper.updateUucDeptInfo(uucDeptInfo);
        }
    }

    public List<String> getDingDept() {

        return uucDeptInfoMapper.selectDingDept();
    }

    public List<String> getDeptByDeptType(String deptType) {

        return uucDeptInfoMapper.selectDeptByDeptType(deptType);
    }

    public void deleteDingDept(Map param) {
        Object deteleIds = param.get("deteleIds");
        if (Objects.nonNull(deteleIds)) {
            List<String> del = (List) deteleIds;
            if (CollectionUtils.isNotEmpty(del)) {
                log.info("删除钉钉部门: " + JSONUtil.toJsonStr(del));
                uucDeptInfoMapper.deleteDingDepts(del);
            }
        }
    }

    public UucDeptInfo selectByDeptCode(String deptCode) {
        return uucDeptInfoMapper.selectByDeptCode(deptCode);
    }

    public List<UucDeptInfo> selectAllDepts() {
        return uucDeptInfoMapper.selectAllDepts();
    }

    public List<UucDeptInfo> getWholeDept() {
        List<UucDeptInfo> uucDeptInfos = selectAllDepts();
        for (UucDeptInfo uucDeptInfo : uucDeptInfos) {
            Map<String, Object> params = uucDeptInfo.getParams();
            if (MapUtils.isEmpty(params)) {
                params = Maps.newHashMap();
                uucDeptInfo.setParams(params);
            }
            List<String> parentOrgs = Lists.newArrayList();
            List<String> childOrgs = Lists.newArrayList();
            String deptCode = uucDeptInfo.getDeptCode();
            String parentDeptCode = uucDeptInfo.getParentCode();
            for (UucDeptInfo deptTmp : uucDeptInfos) {
//                String aboutCode = deptTmp.getDeptCode();
                String aboutCode = String.valueOf(deptTmp.getId());
                String aboutParentCode = deptTmp.getParentCode();
                if (StrUtil.isNotBlank(aboutCode) && aboutCode.equals(parentDeptCode)) {
                    parentOrgs.add(String.valueOf(deptTmp.getId()));
                }
                if (StrUtil.isNotBlank(aboutParentCode) && aboutParentCode.equals(deptCode)) {
                    childOrgs.add(String.valueOf(deptTmp.getId()));
                }
            }
            params.put(SyncConstants.PARENT_ORG, parentOrgs);
            params.put(SyncConstants.CHILD_ORG, childOrgs);
        }
        return uucDeptInfos;
    }

    /**
     * 全量更新dept level
     *
     * @return
     */
    public int initUpdateDeptLevel() {
        int count = 0;
        List<UucDeptInfo> deptInfoList = uucDeptInfoMapper.selectAllDepts();
        for (UucDeptInfo deptInfo : deptInfoList) {
            UucDeptInfo dept = new UucDeptInfo();
            dept.setId(deptInfo.getId());
            dept.setLevel(Long.valueOf(deptInfo.getAncestors().split(",").length));
            count += uucDeptInfoMapper.updateUucDeptInfo(dept);
        }
        return count;
    }

    public List<CmdbDeptDto> cmdbDeptList() {
        List<CmdbDeptDto> cmdbDeptDtos = uucDeptInfoMapper.cmdbDeptList();
        for (CmdbDeptDto c : cmdbDeptDtos) {
            c.setDeptWholeName(getWholeName(c, cmdbDeptDtos));
        }
        return cmdbDeptDtos;
    }

    private String getWholeName(CmdbDeptDto c, List<CmdbDeptDto> cmdbDeptDtos) {
        List<CmdbDeptDto> useFor = new ArrayList(cmdbDeptDtos);
        List<String> names = Lists.newArrayList();
        List<String> acList = new ArrayList(Arrays.asList(StringUtils.splitByWholeSeparator(c.getAncestors(), ",")));
        if (CollectionUtils.isEmpty(acList)) {
            return c.getDeptName();
        }
        acList.remove(c.getDeptCode());
        if (CollectionUtils.isNotEmpty(acList)) {
            for (String anc : acList) {
                String nameTmp = useFor.stream().filter(i -> anc.equals(i.getDeptCode())).findFirst().get().getDeptName();
                names.add(nameTmp);
            }
        }
        names.add(c.getDeptName());
        CollectionUtil.reverse(names);
        return String.join("-", names);
    }
}
