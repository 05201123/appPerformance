package com.jh.net;

import java.util.HashMap;

import com.jh.app.util.BaseTask;
import com.jh.app.util.IResultCallBack;
import com.jh.exception.JHException;
import com.jh.util.GsonUtil;

public class RemoteTask<T> extends BaseTask
{
	private String url;
	private Object req;
	private IResultCallBack<T> callBack;
	private String reponse;
	private Class<T> entityClass;
	private HashMap<String, String> headers;
	public RemoteTask(String url,HashMap<String, String> headers,Object req,IResultCallBack<T> callBack,Class<T> cls){
		this.url = url;
		this.req = req;
		this.callBack = callBack;
		this.entityClass = cls;
		this.headers = headers;
	}
	@Override
	public void doTask() throws JHException {
		// TODO Auto-generated method stub
		JHHttpClient client = new JHHttpClient();
		if(headers!=null)
			client.setHeaders(headers);
		reponse = client.request(url, GsonUtil.format(req));
	}
	@Override
	public void success() {
		// TODO Auto-generated method stub
		//super.success();
		T result = GsonUtil.parseMessage(reponse, (Class<T>)entityClass);
		callBack.success(result);
	}
	@Override
	public void fail(String errorMessage) {
		// TODO Auto-generated method stub
		callBack.fail(errorMessage);
	}
}