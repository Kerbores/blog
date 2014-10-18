package com.kerbores.utils.ua;

/**
 * 应用类型枚举
 * 
 * @author Ixion
 *
 *         create at 2014年9月4日
 */
public enum ApplicationType {
	WEBMAIL("Webmail client"), UNKNOWN("unknown");

	private String name;

	private ApplicationType(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}