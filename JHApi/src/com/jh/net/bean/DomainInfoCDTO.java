package com.jh.net.bean;

public class DomainInfoCDTO {
	 /// <summary>
    /// 基域名带端口
    /// </summary>
    private String Domain;
    /// <summary>
    /// 异常编码：-100为服务器异常不能访问
    /// </summary>
    private int ResponseCode;
	public String getDomain() {
		return Domain;
	}
	public void setDomain(String domain) {
		this.Domain = domain;
	}
	public int getResponseCode() {
		return ResponseCode;
	}
	public void setResponseCode(int responseCode) {
		this.ResponseCode = responseCode;
	}
    

}
