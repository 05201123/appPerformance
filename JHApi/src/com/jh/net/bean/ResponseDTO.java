package com.jh.net.bean;

public class ResponseDTO {
	/*
	 * //reqrecodeDTO 1 int responsecode 2 int responseTime;
	 */
	/**
	 * 返回code
	 */
	
	int responseCode;
	/**
	 * 超时事件
	 */
	long responseTime;

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public long getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(long responseTime) {
		this.responseTime = responseTime;
	}

	

}
