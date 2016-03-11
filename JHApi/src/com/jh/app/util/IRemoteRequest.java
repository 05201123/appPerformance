package com.jh.app.util;

import java.util.HashMap;

public interface IRemoteRequest {

	public <T> void request(String url,Object req,IResultCallBack<T> callBack,Class<T> cls);
	public <T> void request(String url, HashMap<String, String> headers,Object req,IResultCallBack<T> callBack,Class<T> cls);
}
