package com.jh.net;

import java.io.IOException;

import com.jh.exception.JHException;

public class JHIOException extends JHException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7026144800061851010L;
	public JHIOException(Exception e) {
		// TODO Auto-generated constructor stub
		super(e);
	}
	public JHIOException(){
		
	}
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "读取数据异常";
	}
}
