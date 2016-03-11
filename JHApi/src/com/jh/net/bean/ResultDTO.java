package com.jh.net.bean;

import java.util.List;

public class ResultDTO {
	 /// <summary>
    /// BizCode
    /// </summary>
    private int BizCode;

    /// <summary>
    /// WebSiteCDTO
    /// </summary>
    private List<WebSiteCDTO> WebSiteCDTO;

	public int getBizCode() {
		return BizCode;
	}

	public void setBizCode(int bizCode) {
		BizCode = bizCode;
	}

	public List<WebSiteCDTO> getWebSiteCDTO() {
		return WebSiteCDTO;
	}

	public void setWebSiteCDTO(List<WebSiteCDTO> webSiteCDTO) {
		WebSiteCDTO = webSiteCDTO;
	}
}
