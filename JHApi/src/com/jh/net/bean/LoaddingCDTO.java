package com.jh.net.bean;

import java.util.List;

import com.jh.net.bean.BizCodeEnum;

public class LoaddingCDTO {

	// / <summary>
	// / 用户编号
	// / </summary>
	private String UserId;

	private List<DomainInfoCDTO> DomainInfo;

	public List<DomainInfoCDTO> getDomainInfo() {
		return DomainInfo;
	}

	public void setDomainInfo(List<DomainInfoCDTO> domainInfo) {
		DomainInfo = domainInfo;
	}

	// public List<DomainInfoCDTO> getDomain() {
	// return DomainInfo;
	// }
	// public void setDomain(List<DomainInfoCDTO> DomainInfo) {
	// this.DomainInfo = DomainInfo;
	// }
	// / <summary>
	// / 客户端编号
	// / </summary>
	// /
	private String AppId;
	// / <summary>
	// / 代码块编号
	// / </summary>
	// /
	// private BizCodeEnum BizCode;
	private int BizCode;

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}

	public String getAppId() {
		return AppId;
	}

	public void setAppId(String appId) {
		AppId = appId;
	}

	public int getBizCode() {
		return BizCode;
	}

	public void setBizCode(int bizCode) {
		BizCode = bizCode;
	}

}
