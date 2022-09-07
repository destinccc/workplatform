package com.uuc.auth.controller;

import com.uuc.auth.service.ServerTokenService;
import com.uuc.common.core.constant.ClientConstants;
import com.uuc.common.core.constant.SecurityConstants;
import com.uuc.common.core.domain.R;
import com.uuc.common.core.exception.ServiceException;
import com.uuc.common.core.utils.StringUtils;
import com.uuc.common.core.utils.ip.IpUtils;
import com.uuc.common.security.utils.AESUtil;
import com.uuc.system.api.RemoteClientService;
import com.uuc.system.api.domain.UucModelInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/server")
public class ServerController {
    private static final Logger log = LoggerFactory.getLogger(ServerController.class);
    @Autowired
    private ServerTokenService serverTokenService;
    @Autowired
    private RemoteClientService remoteClientService;
    @RequestMapping("/getServerToken")
    public R<?> getAccessToken(HttpServletRequest request) {
        log.info("进入获取服务token流程,请求IP为{}", IpUtils.getIpAddr(request));
        String clientId = request.getHeader("clientId");
        String clientSecret = request.getHeader("clientSecret");
        if(StringUtils.isEmpty(clientId)||StringUtils.isEmpty(clientSecret)){
        log.error("缺少客户端参数");
        return R.fail("缺少客户端参数");
        }
        boolean flag = matchClientSecret(clientId, clientSecret);
        if(!flag){
            log.error("客户端密钥匹配失败");
            return R.fail("解密客户端密钥失败");
        }
        Map<String, Object> serverTokenMap = serverTokenService.createToken(clientId);
        return R.ok(serverTokenMap);
    }

    /**
     * 密码解密
     * @param clientId
     * @param secret
     * @return
     */
    private boolean matchClientSecret(String clientId,String secret) {
        // 密码解密
        log.info("远程调用获取模块信息");
        R<UucModelInfo> uucModelInfoR = remoteClientService.checkClient(clientId, SecurityConstants.INNER);
        UucModelInfo uucModelInfo = uucModelInfoR.getData();
        if(uucModelInfo!=null){
            String clientSecret=uucModelInfo.getClientSecret();
            try {
                String decrypt = AESUtil.decrypt(secret, clientSecret);
                if(decrypt.equals(clientSecret)){
                    return true;
                }
                return false;
            } catch (Exception e) {
                log.error("解密字符串失败");
                e.printStackTrace();
                throw new ServiceException("解密客户端密钥字符串失败");
            }
        }
        return false;
    }

}
