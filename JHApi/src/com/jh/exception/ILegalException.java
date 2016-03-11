package com.jh.exception;

public class ILegalException extends JHException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9056311235505576826L;

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "数据不合法";
	}

}
