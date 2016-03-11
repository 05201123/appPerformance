package com.jh.net.bean;

public enum BizCodeEnum {

	// 手机APP
	APP(1),
	// 广告
	ADM(2),
	
	CBC(3),
	BRC(4),
	Finance(5),
	Notify(6),
	UFM(7),
	DSS(8),
	News(9),
	EBC(10),
	FSP(11),
	Pay(12),
	Game(13),
	CSS(14),
	PIP(15),
	JQYXADM(16),
	FileServer(17);

	int value;

	public int getValue() {
		return value;
	}

	BizCodeEnum(int value) {
		this.value = value;
	};

}