package com.jh.net.bean;

public enum WebSiteStatusEnum {

	//不可用
	Disable(0),
	//可用
	Available(1);
	public int getValue() {
		return value;
	}
	
	int value;

	WebSiteStatusEnum(int value) {
		this.value = value;
	};
}
