package com.uuc.system.mapper;

import com.uuc.system.uuc.domain.UucApiVersion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author uuc
 * @date 2022-07-13
 */
public interface UucApiVersionMapper {

    public UucApiVersion selectUucApiVersionById(Long id);


    public List<UucApiVersion> selectUucApiVersionList(UucApiVersion uucApiVersion);


    public int insertUucApiVersion(UucApiVersion uucApiVersion);


    public int updateUucApiVersion(UucApiVersion uucApiVersion);


    public int deleteUucApiVersionById(Long id);


    public int deleteUucApiVersionByIds(Long[] ids);


    List<String> getEffectiveVersion();

    UucApiVersion selectByApiVersion(@Param("apiVersion") String apiVersion,@Param("id") Long id);
}
