package com.jh.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import com.jh.util.GzipUtil;
import com.jh.util.LogUtil;

public class JHHttpPost  extends IHttpRetryService{
	/**
	 * 使用gzip方式请求服务器，服务器返回数据为gzip压缩数据。
	 * @param url
	 * @param req
	 * @return
	 * @throws JHNetIOException
	 * @throws JHUnsupportedEncodingException
	 */
	public String postGzip(String url,String req)throws JHNetIOException,JHUnsupportedEncodingException{

		LogUtil.println("req:" + req);
		LogUtil.println("url:"+url);
		this.addHeader("Accept-Encoding", "gzip, deflate");
		byte[]bits = postBytes(url, req);
		try {
			String result = new String(GzipUtil.decompress(bits),responseCharset.toString());
			LogUtil.println("res:" + result);
			return result;

		}  catch (UnsupportedEncodingException e) {
			throw new JHUnsupportedEncodingException();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new JHNetIOException(e);
		}
	
	}
	/**
	 * 请求服务器，返回值为字节数组
	 * @param url
	 * @param req
	 * @return
	 * @throws JHIOException
	 */
	private byte[] postBytesOnce(String url,String req) throws JHIOException{

		// TODO Auto-generated method stub
			InputStream is = null;
			DefaultHttpClient client = null;
			JHIOException e1 = null;
			byte[] result = null;
		try {
			if(hasNet())
			{
				BasicHttpParams httpParameters = getParams();
				client = new DefaultHttpClient(httpParameters);
				HttpPost request = new HttpPost(url);
				request.setEntity(new StringEntity(req, reqCharset.toString()));
				setRequestHeaders(request);
				HttpResponse response = client.execute(request);
				validateStatus(response);
				setResponseCharset(response);
				is = response.getEntity().getContent();
				result = readStream(is);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			e1 = new JHMalformedURLException(e);
		} catch (IOException e) {
			e.printStackTrace();
			
			e1 =  new JHIOException(e);
		} 
		catch(AssertionError error){
			e1 = new JHIOException();
		}
		finally{
			closeHttpConnection(client);
			closeInputStream(is);
			if(e1!=null)
				throw e1;
		}
		return result;
	}
	/**
	 * 请求服务器，返回值为字节数组
	 * @param url
	 * @param req
	 * @return
	 * @throws JHIOException
	 */
	public byte[] postBytes(String url,String req) throws JHIOException{

		return (byte[])doTaskRetry(this.getMethod("postBytesOnce", new String[]{url,req}),new String[]{url,req});
	}
	/***
	 * 请求服务器。（使用post方式）
	 * @param url 请求地址
	 * @param req 请求参数
	 * @return 返回服务器结果
	 */
	public String post(String url, String req) throws JHNetIOException,JHUnsupportedEncodingException {
		// TODO Auto-generated method stub
		LogUtil.println("req:" + req);
		LogUtil.println("url:"+url);
		byte[] result = postBytes(url,req);
		String res;
		try {
			res = new String(result, "utf-8");
			LogUtil.println("res:" + res);
			return res;
		} catch (UnsupportedEncodingException e) {
			throw new JHUnsupportedEncodingException();
		}
	/*	String res;
		try {
			res = new String(result, "utf-8");
			LogUtil.println("res:" + res);
			return res;
		} catch (UnsupportedEncodingException e) {
			throw new JHUnsupportedEncodingException();
		}*/
	}
}
