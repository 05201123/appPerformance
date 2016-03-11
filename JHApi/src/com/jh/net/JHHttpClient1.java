package com.jh.net;

import com.jh.exception.JHException;
import com.jh.net.JHHttpClient.HttpContent;

public class JHHttpClient1 extends IHttpRetryService implements IClient{
	@Override
	public String request(String url, String req) throws JHException {
		// TODO Auto-generated method stub
		return null;
	}
	private void initValue(IHttpRetryService client){
		client.setCharset(getCharset());
		client.setConnectTimeout(getConnectTimeout());
		client.setDataFormat(getDataFormat());
		client.setHeaders(getHeaders());
		client.setReadTimeout(getReadTimeout());
		client.setRetryTimes(getRetryTimes());
		client.setRetryInterval(getRetryInterval());
	}
	@Override
	public byte[] requestByte(String url, String req) throws JHException {
		// TODO Auto-generated method stub
		return null;
	}
	public JHHttpClient1()
	{
		
	}
	public HttpContent getContent(String url)throws JHException{
	/*	JHHttpGet client = new JHHttpGet();
		initValue(client);
		return client.getContent(url);*/
		int requestTime = getRetryTimes();
		while(requestTime>0)
		{
			try{
				JHHttpGet client = new JHHttpGet();
				initValue(client);
				return client.getContent(url);
			}
			catch(JHException e){
				if(requestTime==1)
					throw  e;
			}
			requestTime--;
			try {
				Thread.sleep(getRetryInterval());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
}
