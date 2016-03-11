package com.jh.net.bean;

public enum WebSiteCodeEnum {
	
	CMCC(1),
	CUCC(2),
	CTCC(3);
	int value=0;
	WebSiteCodeEnum(int value){
		this.value=value;
	}
	
	public int getValue() {
		return value;
	}
}
