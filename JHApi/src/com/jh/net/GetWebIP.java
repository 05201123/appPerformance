package com.jh.net;

import java.util.List;

import com.jh.net.bean.DomainInfoCDTO;
import com.jh.net.bean.LoaddingCDTO;
import com.jh.net.bean.ResultDTO;

public interface GetWebIP {
	public List<ResultDTO> getAddresses(String domain,int responseCode,int bizCode);
	public String getUserId();
	public List<ResultDTO> getAddresses(List<DomainInfoCDTO> domianInfos,int bizCode);
}
