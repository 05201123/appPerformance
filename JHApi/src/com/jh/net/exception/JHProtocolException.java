package com.jh.net.exception;

import com.jh.net.JHIOException;

public class JHProtocolException extends JHIOException {
	public JHProtocolException(Exception e){
		super(e);
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "错误的通讯协议";
	}
	
}
