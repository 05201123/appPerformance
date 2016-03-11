package com.jh.net.bean;

public class WebSiteCDTO {
   
   
	 /// <summary>
    /// 站点编号
    /// </summary>
 
    private String Id;

    /// <summary>
    /// 站点名称
    /// </summary>
    private String Name;

    /// <summary>
    /// 基域名带端口
    /// </summary>
    private String Domain;    
    /// <summary>
    /// IP
    /// </summary>
    private String IP;
  
    /// <summary>
    /// 电商
    /// </summary>
    private int Code;

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getDomain() {
		return Domain;
	}

	public void setDomain(String domain) {
		Domain = domain;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public int getCode() {
		return Code;
	}

	public void setCode(int code) {
		Code = code;
	}


}
