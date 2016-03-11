package com.example.jhapi;

public class UpdateResponse extends ReturnInfoDTO{
	//可用的最新版本号
	private String newVersionCode;
	//下载地址
	private String downLoadUrl;
	//升级描述
	private String description;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getNewVersionCode() {
		return newVersionCode;
	}
	public void setNewVersionCode(String newVersionCode) {
		this.newVersionCode = newVersionCode;
	}
	public String getDownLoadUrl() {
		return downLoadUrl;
	}
	public void setDownLoadUrl(String downLoadUrl) {
		this.downLoadUrl = downLoadUrl;
	}
}
