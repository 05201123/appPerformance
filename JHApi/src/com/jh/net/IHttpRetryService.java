package com.jh.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import android.content.Context;

import com.jh.exception.JHException;
import com.jh.net.JHHttpClient.DATAFORMAT;
import com.jh.util.LogUtil;

public class IHttpRetryService extends IRetryService{

	public IHttpRetryService(){
		
	}
	public enum JHChaset{
		UTF8("utf-8"),GBK("gbk");
		private String name;
		private JHChaset(String name){
			this.name = name;
		}
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return name;
		}
		public static JHChaset parse(String charset){
			if(charset==null||charset.trim().length()==0)
			{
				return UTF8;
			}else{
				if(charset.equalsIgnoreCase(GBK.toString()))
				{
					return GBK;
				}
				else{
					return UTF8;
				}
			}
		}
	}
	//请求的编码格式
	protected JHChaset reqCharset = JHChaset.UTF8;
	//服务器返回的编码格式
	protected JHChaset responseCharset = JHChaset.UTF8;
	protected void setResponseCharset(HttpResponse response){
		this.setResponseCharset(JHChaset.parse(response.getEntity().getContentEncoding().getValue()));
	}
	protected JHChaset getResponseCharset() {
		return responseCharset;
	}
	private void setResponseCharset(JHChaset responseCharset) {
		this.responseCharset = responseCharset;
	}
	public JHChaset getCharset() {
		return reqCharset;
	}
	
	public void setCharset(JHChaset charset) {
		this.reqCharset = charset;
	}
		//读取超时时间
		private int readTimeout = 10000;
		protected static Context context;
		private HashMap<String,String> headers;
		 protected HashMap<String, String> getHeaders() {
			return headers;
		}
		/**
		  * 
		  * <code>readStream</code>
		  * @description: TODO() 
		  * @param inStream 输入流
		  * @return 得到输入流的字节数
		  * @throws IOException
		  * @since   2011-12-20   liaoyp
		  */
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
		protected static void closeInputStream(InputStream is) {
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
		protected static void closeOutputStream(OutputStream out) {
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
		/**
		 * 关闭服务器连接
		 * @param httpClient
		 */
		protected void closeHttpConnection(HttpClient httpClient){
			if (httpClient != null && httpClient.getConnectionManager() != null) {
				 httpClient.getConnectionManager().shutdown();
			}
		}
		/**
		 * 设置请求头参数
		 * @param headers
		 */
		public void setHeaders(HashMap<String,String> headers)
		{
			this.headers = headers;
		}
		/**
		 * 添加http请求头参数
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
		public static Context getContext() {
			return context;
		}
		public static void setContext(Context context) {
			IHttpRetryService.context = context;
		}
		//链接超时时间
		private int connectTimeout = 10000;
		public enum DATAFORMAT{
			JSON,XML
		};
		private DATAFORMAT dataFormat = DATAFORMAT.JSON;
		public DATAFORMAT getDataFormat() {
			return dataFormat;
		}
		public void setDataFormat(DATAFORMAT dataFormat) {
			this.dataFormat = dataFormat;
		}
		/**
		 * 获取读超时时间
		 * @return
		 */
		public int getReadTimeout() {
			return readTimeout;
		}
		/**
		 * 设置读超时时间
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
		protected boolean hasNet() throws JHException{
			if(context==null||NetStatus.hasNet(context))
			{
				return true;
			}
			else 
				throw new NoNetWorkException();
		}
		protected BasicHttpParams getParams() {
			BasicHttpParams httpParameters = new BasicHttpParams();// Set the timeout in milliseconds until a connection is established.  
			HttpConnectionParams.setConnectionTimeout(httpParameters, connectTimeout);// Set the default socket timeout (SO_TIMEOUT) // in milliseconds which is the timeout for waiting for data.  
			HttpConnectionParams.setSoTimeout(httpParameters, readTimeout);

			return httpParameters;
		}
		protected void setRequestHeaders(HttpRequestBase request) {
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
				request.addHeader("content-type","application/json; charset=" +reqCharset+
						"");
			}
		}
		protected void validateStatus(HttpResponse response) throws SessionInvalidException,JHIOException {
			LogUtil.println(response.getStatusLine().toString());
			
			if (response.getStatusLine().getStatusCode() == 500) {
				throw new JHIOException();
			}
			else if (response.getStatusLine().getStatusCode() == 404) {
				throw new JHIOException();
			}
			//session异常
			else if (response.getStatusLine().getStatusCode() == 401) {
				throw new SessionInvalidException();
			}
			else if (response.getStatusLine().getStatusCode() != 200)
			{
				throw new JHIOException();
			}
		}
		protected void transferData(InputStream is,OutputStream os) throws JHIOException
		{
			BufferedInputStream bufferIn = null;
			BufferedOutputStream bufferOut = null;
			try{
				byte[] bs = new byte[1024];
				int len = 0;
				bufferIn = new BufferedInputStream(is);
				bufferOut = new BufferedOutputStream(os);
				while((len = bufferIn.read(bs))!=-1){
					bufferOut.write(bs, 0, len);
				}
				bufferOut.flush();
				closeInputStream(bufferIn);
	//			os.close();
	//			bufferIn.close();
				closeOutputStream(bufferOut);
			}
			catch(IOException e)
			{
				closeInputStream(bufferIn);
				closeOutputStream(bufferOut);
				throw new JHIOException(e);
			}
//			is.close();
		}
}
