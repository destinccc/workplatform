package com.uuc.auth.api.service;

import com.uuc.auth.controller.LoginController;
import com.uuc.common.core.constant.Constants;
import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.constant.TokenConstants;
import com.uuc.common.core.domain.R;
import com.uuc.common.core.exception.ServiceException;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.system.api.RemoteClientService;
import com.uuc.system.api.domain.UucModelInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author deng
 * @date 2022/7/13 0013
 * @description
 */
@Service
public class TokenApiService {
    @Autowired
    private RemoteClientService remoteClientService;
    private static final Logger log = LoggerFactory.getLogger(TokenApiService.class);

    public UucModelInfo checkClient(String clientId){
        try{
            R<UucModelInfo> uucModelInfoR = remoteClientService.checkClient(clientId, SecurityConstants.INNER);
            int code = uucModelInfoR.getCode();
            if(code!= Constants.SUCCESS){
                throw new ServiceException("远程调用客户端校验方法失败");
            }
            UucModelInfo uucModelInfo = uucModelInfoR.getData();
            if(uucModelInfo==null|| StringUtils.isEmpty(uucModelInfo.getClientRsaPrivate())){
                throw new ServiceException("客户端校验异常");
            }
            return uucModelInfo;
        }catch (Exception e){
            log.error("客户端校验失败,错误信息：{}",e.getMessage());
            throw new ServiceException("客户端校验失败");
        }

    }
    public String getClientId(HttpServletRequest request){
        String clientId=request.getHeader(TokenConstants.CLIENTID);
        return clientId;
    }
}
