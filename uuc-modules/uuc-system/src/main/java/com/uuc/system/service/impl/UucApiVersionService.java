package com.uuc.system.service.impl;

import com.uuc.common.core.exception.ServiceException;
import com.uuc.common.core.utils.DateUtils;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.security.utils.SecurityUtils;
import com.uuc.system.mapper.UucApiVersionMapper;
import com.uuc.system.uuc.domain.UucApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.swing.StringUIClientPropertyKey;

import java.util.List;
import java.util.Objects;


/**
 * @author uuc
 * @date 2022-07-13
 */
@Service
public class UucApiVersionService {

    @Autowired
    private UucApiVersionMapper uucApiVersionMapper;

    public UucApiVersion selectUucApiVersionById(Long id) {
        return uucApiVersionMapper.selectUucApiVersionById(id);
    }


    public List<UucApiVersion> selectUucApiVersionList(UucApiVersion uucApiVersion) {
        return uucApiVersionMapper.selectUucApiVersionList(uucApiVersion);
    }


    public int insertUucApiVersion(UucApiVersion uucApiVersion) {
        String apiVersion = uucApiVersion.getApiVersion();
        if (StringUtils.isBlank(apiVersion)){
            throw new ServiceException("版本号不能为空!");
        }
        if (Objects.nonNull(uucApiVersionMapper.selectByApiVersion(apiVersion,null))){
            throw new ServiceException("版本号已存在!");
        }
        uucApiVersion.setCreateTime(DateUtils.getNowDate());
        uucApiVersion.setUpdateTime(DateUtils.getNowDate());
        uucApiVersion.setOperator(SecurityUtils.getUsername());
        return uucApiVersionMapper.insertUucApiVersion(uucApiVersion);
    }


    public int updateUucApiVersion(UucApiVersion u) {
        if (StringUtils.isBlank(u.getApiVersion())){
            throw new ServiceException("版本号不能为空!");
        }
        if (Objects.nonNull(uucApiVersionMapper.selectByApiVersion(u.getApiVersion(),u.getId()))){
            throw new ServiceException("版本号已存在!");
        }
        u.setUpdateTime(DateUtils.getNowDate());
        u.setOperator(SecurityUtils.getUsername());
        return uucApiVersionMapper.updateUucApiVersion(u);
    }


    public int deleteUucApiVersionByIds(Long[] ids) {
        return uucApiVersionMapper.deleteUucApiVersionByIds(ids);
    }


    public int deleteUucApiVersionById(Long id) {
        return uucApiVersionMapper.deleteUucApiVersionById(id);
    }

    public List<String> getEffectiveVersion() {
        return uucApiVersionMapper.getEffectiveVersion();
    }

    public void updateUucApiVersionStatus(UucApiVersion u) {
        UucApiVersion update = selectUucApiVersionById(u.getId());
        update.setActivate(u.getActivate());
        update.setUpdateTime(DateUtils.getNowDate());
        update.setOperator(SecurityUtils.getUsername());
        uucApiVersionMapper.updateUucApiVersion(update);
    }
}
