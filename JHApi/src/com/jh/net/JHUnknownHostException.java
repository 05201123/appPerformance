package com.jh.net;

public class JHUnknownHostException extends JHIOException {
	public JHUnknownHostException(Exception e) {
		// TODO Auto-generated constructor stub
		super(e);
	}
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "无法连接的主机地址";
	}
}
