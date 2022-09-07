package com.uuc.auth.api.enums;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Oauth2授权方式
 * 
 * @author deng
 */
public enum GrantTypeEnum {

	/**
	 * 授权码模式
	 */
	AUTHORIZATION_CODE("authorization_code"), 

	/**
	 * 密码模式
	 */
	PASSWORD("password"),

	/**
	 * 简化模式
	 */
	IMPLICIT("implicit"),

	/**
	 * 客户端模式
	 */
	CLIENT_CREDENTIAL("client_credential");


	private String value;

	private GrantTypeEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}