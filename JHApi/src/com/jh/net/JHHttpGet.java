package com.jh.net;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import com.jh.exception.JHException;
import com.jh.net.JHHttpClient.HttpContent;
import com.jh.net.exception.JHProtocolException;
import com.jh.util.LogUtil;

public class JHHttpGet extends IHttpRetryService{
	/**
	 * 使用get方法获取的输入流
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws JHException
	 */
	public InputStream getInput(String url) throws  JHException {

		return getContent(url).getStream();
	}
	/**
	 * 使用get方法获取的字符串
	 * @param url
	 * @return
	 * @throws JHException
	 */
	public String get(String url)throws  JHException
	{
		try {
			return new String(getbytes(url),responseCharset.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new JHUnsupportedEncodingException();
		} 
	}
	/**
	 * 请求url返回的字节数组列表
	 * @param url 请求地址
	 * @return 字节数组
	 * @throws JHException
	 */
	public byte[] getbytes(String url)throws  JHException
	{
		return (byte[])doTaskRetry(this.getMethod("getbytesOnce", new String[]{url}),new String[]{url});
	}
	private byte[] getbytesOnce(String url)throws  JHException{
		InputStream is;
		is = getInput(url);
		try {
			return readStream(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeInputStream(is);
			throw new JHException(e);
		}
	}
	public HttpContent getContent(String url) throws JHException{
		return (HttpContent)doTaskRetry(this.getMethod("getContentOnce", new String[]{url}),new String[]{url});
		//doTaskRetry(,url);
	}
	private HttpContent getContentOnce(String url)throws JHException{

		JHException e1 = null;;
			DefaultHttpClient client = null;
			HttpContent content = null;
			InputStream is = null;
		try {
			if(hasNet())
			{
				BasicHttpParams httpParameters = getParams();
				client = new DefaultHttpClient(httpParameters);
				HttpGet request = new HttpGet(url);
				setRequestHeaders(request);
				HttpResponse response = client.execute(request);
				
				validateStatus(response);
				setResponseCharset(response);
				content  = new HttpContent();
				 content.setContentLength(response.getEntity().getContentLength());
				 is = response.getEntity().getContent();
				 content.setStream(new BufferedInputStream(is));
			//	 return content;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		//	closeHttpConnection(httpClient);
			e1 = new JHMalformedURLException(e);
		} catch (ProtocolException e) {
			e.printStackTrace();
			e1 = new JHProtocolException(e);
		} catch (IOException e) {
			e.printStackTrace();
			e1 = new JHIOException(e);
		}
		finally{
			
			closeHttpConnection(client);
			if(e1!=null){
				closeInputStream(is);
				throw e1;
			}
		}
		return content;
	
	}

}
