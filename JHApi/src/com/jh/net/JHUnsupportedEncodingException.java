package com.jh.net;

import com.jh.exception.JHException;

public class JHUnsupportedEncodingException extends JHException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6270715810055963155L;

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "不支持的编码格式";
	}

}
