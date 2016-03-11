package com.jh.net;

import com.jh.exception.JHException;

public class NoNetWorkException extends JHException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4979892205466357307L;
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "无网络";
	}
}
