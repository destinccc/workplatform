package com.uuc.job.service.demo;

import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.job.constants.KsyunSyncConstant;
import com.uuc.system.api.RemoteCmdbService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class Ksyun2CMDBService {


    @Autowired
    private RemoteCmdbService cmdbService;

    @XxlJob("syncKsyun2Cmdb")
    public void ksyun2CMDB() {
        String param = XxlJobHelper.getJobParam();
        System.out.println("系统参数:"+ param);

        if (!KsyunSyncConstant.KSYUN_DATA_MAPPING.containsKey(param)){
            return;
        }
        Long dataId = KsyunSyncConstant.KSYUN_DATA_MAPPING.get(param);
        cmdbService.startExtract(dataId,SecurityConstants.INNER);
    }
}
