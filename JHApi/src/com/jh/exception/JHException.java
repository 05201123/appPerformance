package com.jh.exception;

public class JHException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7290998729894906365L;
	public JHException(String errorMessage){
		super(errorMessage);
	}
	public JHException(Exception e){
		super(e);
	}
	public JHException(){
		super();
	}
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		if(super.getMessage()!=null)
		{
			return super.getMessage();
		}
		else 
			return getDefaultMessage();
	}
	public String getDefaultMessage(){
		return "";
	}
}
