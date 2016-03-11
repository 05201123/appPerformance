package com.jh.net;

import com.jh.exception.JHException;

public class SessionInvalidException extends JHException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8025563296005515752L;
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "无网络";
	}
}
