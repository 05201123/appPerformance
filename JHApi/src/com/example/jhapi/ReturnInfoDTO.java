package com.example.jhapi;

public class ReturnInfoDTO {
	//是否成功
	 private  boolean IsSuccess;
	 //报错时的错误信息
	 private String Message;
	public boolean isIsSuccess() {
		return IsSuccess;
	}
	public void setIsSuccess(boolean isSuccess) {
		IsSuccess = isSuccess;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
}
