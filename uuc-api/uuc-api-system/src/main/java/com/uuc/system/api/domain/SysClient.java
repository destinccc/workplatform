package com.uuc.system.api.domain;

import com.uuc.common.core.web.domain.BaseEntity;

public class SysClient extends BaseEntity {
    private String ClientId;
    private String ClientSecret;

    public String getClientId() {
        return ClientId;
    }

    public void setClientId(String clientId) {
        ClientId = clientId;
    }

    public String getClientSecret() {
        return ClientSecret;
    }

    public void setClientSecret(String clientSecret) {
        ClientSecret = clientSecret;
    }
}
