package com.jh.net;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import com.jh.exception.JHException;

public class JHHttpUpload  extends IHttpRetryService{

	/**
	 * 向url上传inputStream对应的流，返回的是服务器上存储路径
	 * @param input
	 * @param url
	 * @return
	 * @throws JHException
	 */
	private String uploadDataOnce(InputStream input,String url)throws  JHException
	{
		String resultString = null;
		JHException e1 = null;
		InputStream is = null;
		DefaultHttpClient client = null;
		try{
			if(hasNet())
			{
				BasicHttpParams httpParameters = getParams();
				httpParameters.setParameter("Charset", reqCharset.toString());
				httpParameters.setParameter(
						"Content-Type",
						"multipart/form-data; boundary="
								+ System.currentTimeMillis());
				httpParameters.setParameter("KeepAlive", "true");
				httpParameters.setParameter("Accept-Language", "zh-cn");
				
				client = new DefaultHttpClient(httpParameters);
				HttpPost request = new HttpPost(url);
				setRequestHeaders(request);
				request.setEntity(new InputStreamEntity(input, input.available()));
				HttpResponse response = client.execute(request);
				
				validateStatus(response);
				is = response.getEntity().getContent();
				byte[] result = readStream(is);
				resultString = new String(result,getResponseCharset().toString());
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e1 =  new JHIOException(e);
		}catch(RuntimeException e){
			e1 = new JHException(e);
		}
		finally{
			closeInputStream(is);
			closeHttpConnection(client);
			if(e1!=null)
			{
				throw e1;
			}
		}
		return resultString;
	}
	public String uploadData(InputStream input,String url)throws  JHException{
		return (String)doTaskRetry(this.getMethod("uploadDataOnce", new Object[]{input,url}),new Object[]{input,url});
	}
}
