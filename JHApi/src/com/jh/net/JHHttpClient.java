/*
 * @project testcursor
 * @package com.testcursor.util
 * @file HttpClient.java
 * @version  1.0
 * @author  yourname
 * @time  2012-1-31 下午01:21:47
 * CopyRight:北京金和软件信息�?��有限公司 2012-1-31
 */
package com.jh.net;

import java.io.BufferedInputStream;





import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;

import com.jh.exception.JHException;
import com.jh.net.exception.JHProtocolException;
import com.jh.util.GzipUtil;
import com.jh.util.LogUtil;





import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.NetworkOnMainThreadException;
import android.text.TextUtils;
import android.util.Log;

public class JHHttpClient implements IClient{
	//读取超时时间
	private int readTimeout = defaultValue;
	public enum DATAFORMAT{
		JSON,XML
	};
	private DATAFORMAT dataFormat = DATAFORMAT.JSON;
	/**
	 * 获取数据请求格式，决定了http请求头的Content-type
	 * @return
	 */
	public DATAFORMAT getDataFormat() {
		return dataFormat;
	}
	private static final int defaultValue = -1;
	private static int globalRetryTimes = defaultValue;
	/**
	 * 设置全局重试次数，当设置对象的重试次数时，以JHHttpClient的对象重试次数为主
	 * @param globalRetryTimes 重试次数
	 */
	public static void setGlobalRetryTimes(int globalRetryTimes) {
		JHHttpClient.globalRetryTimes = globalRetryTimes;
	}
	/**
	 * 设置全局读取超时时间，当设置对象的读取超时时间时，以JHHttpClient的对象读取超时时间为主
	 * @param globalReadTimeout ms
	 */
	public static void setGlobalReadTimeout(int globalReadTimeout) {
		JHHttpClient.globalReadTimeout = globalReadTimeout;
	}
	/**
	 * 设置全局连接超时时间，当设置对象的连接超时时间时，以JHHttpClient的对象连接超时时间为主
	 * @param globalConnectTimeout 毫秒
	 */
	public static void setGlobalConnectTimeout(int globalConnectTimeout) {
		JHHttpClient.globalConnectTimeout = globalConnectTimeout;
	}
	private static int globalReadTimeout = defaultValue;
	private static int globalConnectTimeout = defaultValue;
	private static int globalRetryInterval = defaultValue;
	
	/**
	 * 设置网络请求格式，默认为json格式，决定了请求头的内容格式
	 * @param dataFormat
	 */
	public void setDataFormat(DATAFORMAT dataFormat) {
		this.dataFormat = dataFormat;
	}
	/***
	 * 获取重试次数
	 * @return
	 */
	private int getHttpRetryTimes(){
		if(retryTimes>0){
			return retryTimes;
		}
		else if(globalRetryTimes>0){
			return globalRetryTimes;
		}
		return 3;
	}
	/***
	 * 获取连接超时时间
	 * @return
	 */
	private int getHttpConnectTimeout(){
		if(connectTimeout>0){
			return connectTimeout;
		}
		else if(globalConnectTimeout>0){
			return globalConnectTimeout;
		}
		return 10000;
	}
	/***
	 * 获取读取超时时间
	 * @return
	 */
	private int getHttpReadTimeout(){
		if(readTimeout>0){
			return readTimeout;
		}
		else if(globalReadTimeout>0){
			return globalReadTimeout;
		}
		return 10000;
	}
	/***
	 * 获取重试间隔时间（ms）
	 * @return
	 */
	private int getHttpRetryInterval(){
		if(retryInterval>0){
			return retryInterval;
		}
		else if(globalRetryInterval>0){
			return globalRetryInterval;
		}
		return 5000;
	}
	/**
	 * 获取对象读取超时时间,已过期，请采用getHttpReadTimeout方法
	 * @return
	 */
	@Deprecated
	public int getReadTimeout() {
		return readTimeout;
	}
	/**
	 * 设置读取超时时间
	 * @param readTimeout
	 */
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
	/**
	 * 获取连接超时时间
	 * @return
	 */
	public int getConnectTimeout() {
		return connectTimeout;
	}
	/**
	 * 设置连接超时时间
	 * @param connectTimeout
	 */
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	private static Context context;
	//链接超时时间
	private int connectTimeout = defaultValue;
	//失败重试次数
	private int retryTimes = defaultValue;
	//服务器返回参数
	private HttpResponse response;
	public HttpResponse getResponse() {
		return response;
	}
	private DefaultHttpClient reqClient;
	/**
	 * 使用keep-alive来重用连接发请求。
	 */
	public String requestKeepAlive(String url,String req) throws JHIOException{
		// TODO Auto-generated method stub
		InputStream is = null;
		try {
			if (context == null || NetStatus.hasNet(context)) {
				addHeader("Connection", "keep-alive");
				BasicHttpParams httpParameters = getParams();
				if(reqClient==null)
				{
					reqClient = new DefaultHttpClient(httpParameters);
				}
				
				HttpPost request = new HttpPost(url);
				request.setEntity(new StringEntity(req, "utf-8"));
				setHttpHeaders(request);
				response = reqClient.execute(request);
				// response.getHeaders("Content-Encoding");
				//validateStatus(response);
				//url
				validateStatus(url,response);
				is = response.getEntity().getContent();
				byte[] result = readStream(is);
				return new String(result,"utf-8");
			} else
				throw new NoNetWorkException();

		} catch (MalformedURLException e) {
			e.printStackTrace();
			// closeInputStream(is);
			reqClient = null;
		//	closeInputStream(is);
			throw new JHMalformedURLException(e);
		} catch (IOException e) {
			e.printStackTrace();
			// closeInputStream(is);
			reqClient = null;
		//	closeInputStream(is);
			throw new JHNetIOException(e);
		} catch (AssertionError error) {
			reqClient = null;
		//	closeInputStream(is);
			throw new JHNetIOException();
		} finally {
			closeInputStream(is);
		}
	}
	/**
	 * 获取重试次数
	 * @return
	 */
	public int getRetryTimes() {
		return retryTimes;
	}
	/**
	 * 设置重试次数
	 * @param retryTimes
	 */
	public void setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
	}
	//失败重试间隔
	private int retryInterval = defaultValue;
	public JHHttpClient()
	{
		
	}
	/**
	 * 设置应用程序上下文，设置后可以判断是否有网络，建议在Application的Oncreate中设�?
	 * @param context
	 */
	public static void setContext(Context context)
	{
		JHHttpClient.context = context.getApplicationContext();
	}

	/**
	 * 获取网络流以及内容长度，主要用于get方法
	 * @param url
	 * @param isGzip 是否压缩
	 * @return
	 * 
	 */
	public HttpContent getContent(String url)throws JHException{
	//	 URL tmpurl;
		int requestTime = getHttpRetryTimes();
		while(requestTime>0)
		{
			try{
				return getContentOnce(url);
			}
			catch(JHException e){
				if(requestTime==1)
					throw  e;
			}
			requestTime--;
			try {
				Thread.sleep(getHttpRetryInterval());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
		
	}
	private HttpContent getContentOnce(String url)throws JHException{
		try {
			if(context==null||NetStatus.hasNet(context))
			{
			/*tmpurl = new URL(url);
			 HttpURLConnection conn = (HttpURLConnection) tmpurl.openConnection();
				conn.setRequestMethod("GET"); 
				conn.setReadTimeout(readTimeout);
				conn.setConnectTimeout(connectTimeout);
				conn.connect();
				 int length = conn.getContentLength(); //这就是s6000.jpg的大�?
				 HttpContent content = new HttpContent();
				 content.setContentLength(length);
				 content.setStream(conn.getInputStream());*/
				BasicHttpParams httpParameters = getParams();
				DefaultHttpClient client = new DefaultHttpClient(httpParameters);
				HttpGet request = new HttpGet(url);
				setHttpHeaders(request);
				response = client.execute(request);
				
			//	validateStatus(response);
				validateStatus(url,response);
				 HttpContent content = new HttpContent();
				 content.setContentLength(response.getEntity().getContentLength());
				 content.setStream( response.getEntity().getContent());
				 return content;
			}
			else
				throw new NoNetWorkException();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new JHMalformedURLException(e);
		} catch (ProtocolException e) {
			e.printStackTrace();
			throw new JHProtocolException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new JHNetIOException(e);
		}
	}
	public static class HttpContent{
		private long contentLength;
		private InputStream stream;
		public long getContentLength() {
			return contentLength;
		}
		public void setContentLength(long contentLength) {
			this.contentLength = contentLength;
		}
		public InputStream getStream() {
			return stream;
		}
		public void setStream(InputStream stream) {
			this.stream = stream;
		} 
	}
	/**
	 * 获取url对应的资源大小（如果返回-1，表示请求错误）
	 * @param url
	 * @return
	 */
	public long getContentLength(String url){
		try{
			return getContent(url).contentLength;
		}
		catch(JHException e)
		{
			e.printStackTrace();
			return -1;
		}
		/* try {
			 URL tmpurl = new URL(url);
			 HttpURLConnection conn = (HttpURLConnection) tmpurl.openConnection();
			conn.setRequestMethod("GET"); 
			conn.connect();
			 int length = conn.getContentLength(); //这就是s6000.jpg的大�?
			 conn.disconnect();
			 return length;
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}*/
		
	}
	public String requestGzip(String url,String req) throws JHNetIOException,JHUnsupportedEncodingException
	{
		LogUtil.println("req:" + req);
		LogUtil.println("url:"+url);
		this.addHeader("Accept-Encoding", "gzip, deflate");
		byte[]bits = requestByte(url, req);
		String result = null;
		/*try {
			 try {
				result = new String(GzipUtil.decompress(bits),"utf-8");
			} catch (Exception e) {//如果给的流不是压缩流 尝试一次读流
				result = new String(bits, "utf-8");
				e.printStackTrace();
			}
			LogUtil.println("res:" + result);
			return result;

		}  catch (UnsupportedEncodingException e) {
			throw new JHUnsupportedEncodingException();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new JHNetIOException(e);
		}*/
		return getResponseContent(bits);
	}
	/***
	 * 请求服务器�?（使用post方式�?
	 * @param url 请求地址
	 * @param req 请求参数
	 * @return 返回服务器结�?
	 */
	@Override
	public String request(String url, String req) throws JHNetIOException,JHUnsupportedEncodingException {
		// TODO Auto-generated method stub
		LogUtil.println("req:" + req);
		LogUtil.println("url:"+url);
		byte[] result = requestByte(url,req);
		String res;
		try {
			res = new String(result, "utf-8");
			LogUtil.println("res:" + res);
			return res;
		} catch (UnsupportedEncodingException e) {
			throw new JHUnsupportedEncodingException();
		}
	}
	/**
	 * 将文件的字节流转换为。net支持的字节流，主要设�?128-127�?-255.
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private static int[] getFileInt(String file) throws IOException
	{
		FileInputStream input = null;
		try
		{
			input = new FileInputStream(file);
			byte[] bits = readStream(input);
			int[] out = new int[bits.length];
			for (int ii = 0; ii < bits.length; ii++) {
				if (bits[ii] < 0) {
					out[ii] = bits[ii] + 256;
				} else
					out[ii] = bits[ii];
			}
			input.close();
			return out;
		}
		catch(IOException e)
		{
			closeInputStream(input);
			throw new IOException();
		}
		
	}
	 /**
	  * 
	  * <code>readStream</code>
	  * @description: TODO() 
	  * @param inStream 输入�?
	  * @return 得到输入流的字节�?
	  * @throws IOException
	  * @since   2011-12-20   liaoyp
	  */
	@SuppressLint("NewApi")
	public static byte[] readStream(InputStream inStream) throws IOException{
		 BufferedInputStream buffer = null;
		 ByteArrayOutputStream outStream = null;
		try
		{
		    buffer = new BufferedInputStream(inStream);
			outStream = new ByteArrayOutputStream();
			byte[] outbits;
			byte[] bytes = new byte[1024];
			int len= -1;
			while((len=buffer.read(bytes))!=-1)
			{
				outStream.write(bytes, 0, len);
			}
			outStream.flush();
			outbits = outStream.toByteArray();
			outStream.close();
			
			return outbits;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			closeInputStream(buffer);
			closeOutputStream(outStream);
			throw new IOException(e);
		}
	}
	/**
	 * 使用post方式请求服务器，url为服务地�?��req为请求串
	 * 返回值为服务器返回的字节数组�?
	 */
	@Override
	public byte[] requestByte(String url, String req) throws JHNetIOException{
		int requestTime = getHttpRetryTimes();
		while(requestTime>0)
		{
			try{
				return requestByteOnce(url,req);
			}
			catch (ResponseErrorException e) {
				// TODO: handle exception
				if(requestTime==1)
				{
					if(errorHandler!=null){
						errorHandler.error(e.getErrorCode(), e.getErrorString());
					}
					throw e;
				}
			}
			catch(JHException e){
				if(requestTime==1)
					throw  e;
			}
			requestTime--;
			try {
				Thread.sleep(getHttpRetryInterval());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private byte[] requestByteOnce(String url, String req) throws JHNetIOException {
		// TODO Auto-generated method stub
			InputStream is = null;
		try {
			if(context==null||NetStatus.hasNet(context))
			{
				BasicHttpParams httpParameters = getParams();
				DefaultHttpClient client = new DefaultHttpClient(httpParameters);
				HttpPost request = new HttpPost(url);
				request.setEntity(new StringEntity(req, "utf-8"));
				setHttpHeaders(request);
				response = client.execute(request);
				//response.getHeaders("Content-Encoding");
				//validateStatus(response);
				validateStatus(url,response);
				is = response.getEntity().getContent();
				byte[] result = readStream(is);
				new SwitchIP(context).addRequestRecord(url, response, null);
				return result;
			}
			else
				throw new NoNetWorkException();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		//	closeInputStream(is);
			throw new JHMalformedURLException(e);
		} catch (IOException e) {
			new SwitchIP(context).addRequestRecord(url, response, e);
			e.printStackTrace();
			//closeInputStream(is);
			throw new JHNetIOException(e);
		} 
		catch(AssertionError error){
			
			throw new JHNetIOException();
		}
		catch(NetworkOnMainThreadException error){
			
			throw new JHNetIOException();
		}
		finally{
			closeInputStream(is);
		}
	}
	private static void closeInputStream(InputStream is) {
		if(is!=null)
		{
				try {
					is.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}
	} 
	private static void closeOutputStream(OutputStream out) {
		if(out!=null)
		{
				try {
					out.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}
	} 
	public byte[] requestByte(String url, String req, boolean notJson) throws JHException {
		// TODO Auto-generated method stub
			if(context==null||NetStatus.hasNet(context))
			{
				return requestByte(url,req);
			}
			else
				throw new NoNetWorkException();
	}
	/**
	 * 下载文件
	 * @param path 文件本地保存地址
	 * @param url 服务器资源路�?
	 * @return 下载是否成功
	 */
	public boolean downloadFile(String path,String url) throws JHFileNotFoundException,JHNetIOException{
		FileOutputStream outputStream = null;
		InputStream is = null;
		try {
			outputStream = new FileOutputStream(path);
			if (context == null || NetStatus.hasNet(context)) {
				is = httpGet(url);
				transferData(is, outputStream);
			}
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new JHFileNotFoundException();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new JHMalformedURLException(e);
		} catch (JHException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new JHNetIOException(e);
		}
		finally{
			closeInputStream(is);
			closeOutputStream(outputStream);
		}
		return false;
	}
	private void transferData(InputStream is,OutputStream os) throws IOException
	{
		byte[] bs = new byte[1024];
		int len = 0;
		BufferedInputStream bufferIn = new BufferedInputStream(is);
		BufferedOutputStream bufferOut = new BufferedOutputStream(os);
		while((len = bufferIn.read(bs))!=-1){
			bufferOut.write(bs, 0, len);
		}
		bufferOut.flush();
		bufferOut.close();
//		os.close();
		bufferIn.close();
//		is.close();
	}
	/**
	 * 请求url返回的字节数组列�?
	 * @param url 请求地址
	 * @return 字节数组
	 * @throws JHException
	 */
	public byte[] getDataFromURL(String url)throws  JHException
	{
		return getDataFromURL(url,false);
	}
	/**
	 * 请求url返回的字节数组列�?
	 * @param url 请求地址
	 * @param isGzip 是否压缩
	 * @return 字节数组
	 * @throws JHException
	 */
	public byte[] getDataFromURL(String url,boolean isGzip)throws  JHException
	{
		InputStream is;
		is = httpGet(url);
		try {
			return readStream(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeInputStream(is);
			throw new JHException(e);
		}
	}
	/**
	 * 使用get方法获取的输入流
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws JHException
	 */
	public InputStream httpGet(String url) throws  JHException {
	/*	BasicHttpParams httpParameters = getParams();
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);
		HttpGet request = new HttpGet(url);
		setHttpHeaders(request);
		HttpResponse response = client.execute(request);
		validateStatus(response);
		InputStream is = response.getEntity().getContent();*/
		return httpGet(url,false);
		//return is;
	}
	/**
	 * 使用get方法获取的输入流
	 * @param url
	 * @param isGzip 是否为压缩
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws JHException
	 */
	public InputStream httpGet(String url,boolean isGzip) throws  JHException {
	/*	BasicHttpParams httpParameters = getParams();
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);
		HttpGet request = new HttpGet(url);
		setHttpHeaders(request);
		HttpResponse response = client.execute(request);
		validateStatus(response);
		InputStream is = response.getEntity().getContent();*/
		if(isGzip){

			this.addHeader("Accept-Encoding", "gzip, deflate");
		}
		return getContent(url).getStream();
		//return is;
	}
	public  interface SessionInvalideHandler
	{
		public void process();
	}
	public static interface ErrorHandler{
		public void error(int responseCode,String responseMessage);
	}
	private ErrorHandler errorHandler;
	
	private static SessionInvalideHandler defaultHandler;
	public static void setSessionHandler(SessionInvalideHandler handler){
		defaultHandler = handler;
	}
	public static class ResponseErrorException extends JHException{
		private int errorCode;
		private String errorString;
		public int getErrorCode() {
			return errorCode;
		}
		public void setErrorCode(int errorCode) {
			this.errorCode = errorCode;
		}
		public String getErrorString() {
			return errorString;
		}
		public void setErrorString(String errorString) {
			this.errorString = errorString;
		}
		public ResponseErrorException(int errorCode,
				String errorString) {
			this.errorCode = errorCode;
			this.errorString = errorString;
		}
		
	}
	private void validateStatus(HttpResponse response)
			throws SessionInvalidException, JHNetIOException {
		LogUtil.println(response.getStatusLine().toString());

		// session异常
		if (response.getStatusLine().getStatusCode() == 401) {
			response.getStatusLine().getStatusCode();
			if (defaultHandler != null) {
				defaultHandler.process();
			}
			// throw new SessionInvalidException();
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new ResponseErrorException(response.getStatusLine().getStatusCode(),
					response.getStatusLine().getReasonPhrase());
			// throw new JHNetIOException();
		}

	}
	private void validateStatus(String url,HttpResponse response) throws SessionInvalidException,JHNetIOException {
		LogUtil.println(response.getStatusLine().toString());
		
		//session异常 
		if (response.getStatusLine().getStatusCode() == 401) {
			if(defaultHandler!=null)
			{
				defaultHandler.process();
			}
			//throw new SessionInvalidException();
		}
		if(response.getStatusLine().getStatusCode()>=500){
			new SwitchIP(context).addRequestRecord(url, response, null);
		}
		if (response.getStatusLine().getStatusCode() != 200)
		{
			throw new ResponseErrorException(response.getStatusLine().getStatusCode(),response.getStatusLine().getReasonPhrase());
			//throw new JHNetIOException();
		}
	}
	/**
	 * 使用get方法获取的字符串
	 * @param url
	 * @return
	 * @throws JHException
	 */
	public String getData(String url)throws  JHException
	{
		/*try 
		{
			if(context==null||NetStatus.hasNet(context))
			{
				return new String(getDataFromURL(url),"utf-8");
			}
			else
				throw new NoNetWorkException();
		}catch (IOException e) {
			throw new JHNetIOException(e);
		}*/
		return getData(url,false);
	}
	/**
	 * 通过get方法获取参数
	 * @param url
	 * @param isGzip 是否为gzip加密
	 * @return
	 * @throws JHException
	 */
	public String getData(String url,boolean isGzip)throws  JHException
	{
		return getResponseContent(getDataFromURL(url)); 
	}
	/**
	 * 返回服务器调用的头，如果没有或失败，返回null.
	 * @param name
	 * @return
	 */
	public Header[] getResponseHeader(String name){
		if(response!=null){
			return response.getHeaders(name);
		}
		return null;
	}
	private String getResponseContent(byte[] bits) throws JHException{
		String result = null;
		if(isGzipResponse()){
			try {
				result = new String(GzipUtil.decompress(bits),"utf-8");
				LogUtil.println("Gzipres:" + result);
				return result;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			//	throw new JHUnsupportedEncodingException();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//throw new JHException(e);
			} 
		}
		try {
			result = new String(bits,"utf-8");
			LogUtil.println("res:" + result);
			return result;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new JHUnsupportedEncodingException();
		}
		
	}

	/**
	 * 是否服务器返回为gzip压缩
	 * @return 
	 */
	private boolean isGzipResponse(){
		Header[] headers = getResponseHeader("Content-Encoding");
		if(headers!=null&&headers.length>0){
			for(Header header:headers){
				if(header!=null&&!TextUtils.isEmpty(header.toString())){
					if(header.toString().toLowerCase().contains("gzip")){
						return true;
					}
				}
			}
		}
		return false;
	}
	public BasicHttpParams getParams() {
		BasicHttpParams httpParameters = new BasicHttpParams();// Set the timeout in milliseconds until a connection is established.  
		HttpConnectionParams.setConnectionTimeout(httpParameters, getHttpConnectTimeout());// Set the default socket timeout (SO_TIMEOUT) // in milliseconds which is the timeout for waiting for data.  
		HttpConnectionParams.setSoTimeout(httpParameters, getHttpReadTimeout());
		httpParameters.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getHttpConnectTimeout());
		httpParameters.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, getHttpReadTimeout());
		return httpParameters;
	}
	/**
	 * 上传文件
	 * @param filePath 待上传本地文件路�?
	 * @param url 上传地址
	 * @return 服务器上传后返回结果
	 * @throws JHException
	 */
	public String uploadFile(String filePath,String url)throws  JHException
	{
		FileInputStream input = null;
		try {
			input = new FileInputStream(filePath);
			String result = upLoadData(input,url);
			
			return result;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw  new JHFileNotFoundException();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw  new JHNetIOException(e);
		}
		finally{
			closeInputStream(input);
		}
	} 
	public String upLoadData(InputStream input,String url) throws  JHException{
		int requestTime = getHttpRetryTimes();
		while(requestTime>0)
		{
			try{
				return uploadDataOnce(input,url);
			}
			catch(JHException e){
				if(requestTime==1)
					throw  e;
			}
			requestTime--;
			try {
				Thread.sleep(getHttpRetryInterval());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 上传输入流到url服务
	 * @param input
	 * @param url
	 * @return 服务器返回�?
	 * @throws JHException
	 */
	private String uploadDataOnce(InputStream input,String url)throws  JHException
	{
		
//		name=aa&sign=33333333333%2402C818B5058D7C4F2EC6F6B7AECC9071DF61C763&moduletype=contact
//		url = "http://192.168.1.83/c63.0sp3wpfamp/JHSoft.WCFamp/UploadFile.aspx?name=aa&sign=33333333333%24205AD701C98237BD53C3FDFBF3665717C6225FF2&moduletype=contact";
		
			try{
				if(context==null||NetStatus.hasNet(context))
				{
					BasicHttpParams httpParameters = getParams();
					httpParameters.setParameter("Charsert", "UTF-8");
					httpParameters.setParameter(
							"Content-Type",
							"multipart/form-data; boundary="
									+ System.currentTimeMillis());
					httpParameters.setParameter("KeepAlive", "true");
					httpParameters.setParameter("Accept-Language", "zh-cn");
					
					DefaultHttpClient client = new DefaultHttpClient(httpParameters);
					HttpPost request = new HttpPost(url);
					setHttpHeaders(request);
					request.setEntity(new InputStreamEntity(input, input.available()));
					HttpResponse response = client.execute(request);
					
					//validateStatus(response);
					validateStatus(url,response);
					//validateStatus();
					InputStream is = response.getEntity().getContent();
					byte[] result = readStream(is);
					return new String(result,"utf-8");
					
				}
				else
				{
						throw new NoNetWorkException();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
					throw  new JHNetIOException(e);
			}catch(RuntimeException e){
					throw  new JHException(e);
			}
			
	}

/*	private void setHttpHeaders(HttpURLConnection httpConn) {
		if(headers!=null)
		{
			Iterator<Entry<String, String>> iterator = headers.entrySet().iterator();
			Entry<String, String> headers;
			while(iterator.hasNext())
			{
				headers = iterator.next();
				httpConn.setRequestProperty(headers.getKey(), headers.getValue());
			}
		}
	}
	private void setHttpHeaders(BasicHttpParams params) {
		if(headers!=null)
		{
			Iterator<Entry<String, String>> iterator = headers.entrySet().iterator();
			Entry<String, String> headers;
			while(iterator.hasNext())
			{
				headers = iterator.next();
				params.setParameter(headers.getKey(), headers.getValue());
			}
		}
	}*/
	private void setHttpHeaders(HttpRequestBase request) {
		if(headers!=null)
		{
			Iterator<Entry<String, String>> iterator = headers.entrySet().iterator();
			Entry<String, String> headers;
			while(iterator.hasNext())
			{
				headers = iterator.next();
				request.setHeader(headers.getKey(), headers.getValue());
			}
		}
		if(dataFormat==DATAFORMAT.JSON)
		{
			request.addHeader("content-type","application/json; charset=utf-8");
		}
	}
	private HashMap<String,String> headers;
	/**
	 * 设置请求头参�?
	 * @param headers
	 */
	public void setHeaders(HashMap<String,String> headers)
	{
		this.headers = headers;
	}
	/**
	 * 添加http请求头参�?
	 * @param field
	 * @param newValue
	 */
	public void addHeader(String field, String newValue)
	{
		if(headers==null)
		{
			headers = new HashMap<String,String>();
		}
		headers.put(field, newValue);
	}
	public ErrorHandler getErrorHandler() {
		return errorHandler;
	}
	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}
}
