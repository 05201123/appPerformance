package com.jh.net;

import com.jh.exception.JHException;

public class WebServiceException extends JHException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4559322935080530654L;
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "服务调用错误";
	}
}
