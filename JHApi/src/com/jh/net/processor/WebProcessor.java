package com.jh.net.processor;

import com.jh.app.util.BaseTask;
import com.jh.exception.JHException;
import com.jh.net.JHHttpClient;
import com.jh.util.GsonUtil;

public class WebProcessor<T> extends BaseTask{
	private JHHttpClient client ;
	private String url;
	private Object req;
	private Class<T> value;
	private IWebResult<T> callback;
	private T result ;
	public WebProcessor(JHHttpClient client, String url, Object req,
			Class<T> value, IWebResult<T> callback) {
		super();
		this.client = client;
		this.url = url;
		this.req = req;
		this.value = value;
		this.callback = callback;
	}
	@Override
	public void doTask() throws JHException {
		// TODO Auto-generated method stub
		String reqString = GsonUtil.format(req);
		//JHHttpClient client = new JHHttpClient();
		String responseString = client.request(url, reqString);
		result = GsonUtil.parseMessage(responseString, value);
	}
	@Override
	public void success() {
		// TODO Auto-generated method stub
		super.success();
	}
	@Override
	public void fail(JHException e) {
		// TODO Auto-generated method stub
		super.fail(e);
	}
	
}
