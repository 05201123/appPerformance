package com.jh.net;

import java.net.MalformedURLException;

import com.jh.exception.JHException;

public class JHMalformedURLException extends JHIOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2555703391440366773L;
	public JHMalformedURLException(Exception e) {
		// TODO Auto-generated constructor stub
		super(e);
	}
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "非法的服务地址";
	}
}
