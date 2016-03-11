package com.jh.net;

import com.jh.exception.JHException;

public class JHConnectionException extends JHException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3678138775403090892L;

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "服务器连接超时";
	}

}
