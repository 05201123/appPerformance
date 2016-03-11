package com.jh.net;

import com.jh.exception.JHException;

public class JHPermissionException extends JHException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7952867004226065293L;
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "不被授权的程序";
	}
}
