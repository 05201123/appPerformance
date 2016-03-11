package com.jh.net;


public class JHNetIOException extends JHIOException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5601457664982037832L;
	public JHNetIOException(Exception e) {
		// TODO Auto-generated constructor stub
		super(e);
	}
	public JHNetIOException() {
		// TODO Auto-generated constructor stub
		
	}
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "网络请求错误";
	}
}
