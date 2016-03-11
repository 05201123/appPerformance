package com.jh.net;

import com.jh.exception.JHException;

public class JHFileNotFoundException extends JHException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1735621398952725749L;

	public JHFileNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getDefaultMessage() {
		// TODO Auto-generated method stub
		
		return "文件不存在";
	}

	public JHFileNotFoundException(String errorMessage) {
		super(errorMessage);
		// TODO Auto-generated constructor stub
	}

}
