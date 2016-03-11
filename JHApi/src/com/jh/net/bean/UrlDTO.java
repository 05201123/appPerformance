package com.jh.net.bean;

import java.util.List;

public class UrlDTO {
	
	
	//url可用
	public static final int URL_COULD_USE = 0;
	//url不可用
	public static final int URL_COULD_NOT_USE = 1;
	
	public UrlDTO(){
		//默认url可用
		status = URL_COULD_USE;
	}
	
	/**
	 * 请求地址
	 */
	String url;
	/**
	 * 请求返回值（超时值，return code;）
	 * 为list的原因：1 多次请求，2 失败重试
	 */
	List<ResponseDTO> reses;
	/**
	 * 标识url是否可用
	 */
	int status;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<ResponseDTO> getReses() {
		return reses;
	}
	public void setReses(List<ResponseDTO> reses) {
		this.reses = reses;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
