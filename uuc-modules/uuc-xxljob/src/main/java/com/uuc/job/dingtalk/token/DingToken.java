package com.uuc.job.dingtalk.token;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 */
@Data
@Builder
public class DingToken implements Serializable {

    private static final long serialVersionUID = -1L;

    // 钉钉token
    private String token;

    // 内部应用key
    private String appKey;

    // 内部应用secret
    private String appSecret;

    public DingToken() {
    }

    public DingToken(String token, String appKey, String appSecret) {
        this.token = token;
        this.appKey = appKey;
        this.appSecret = appSecret;
    }
}
