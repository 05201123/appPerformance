package com.jh.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
/**
 * 使用Gzip进行压缩
 * @author jhzhangnan1
 *
 */
public class GzipUtil {
	/**
	 * 解压
	 * @param src 待解压数据
	 * @return 解压后数据
	 * @throws Exception
	 */
	public static byte[] decompress(byte[] src)throws Exception 
	{
		InputStream is = new ByteArrayInputStream(src); 
		GZIPInputStream  stream = new GZIPInputStream(is);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte data[] = new byte[1024];  
		int count = 0;
		while((count = stream.read(data, 0, 1024)) != -1)
		{
			out.write(data, 0, count);
		}
		is.close();
		out.flush();
		out.close();
		stream.close();
		 byte[] result = out.toByteArray();
		 return result;
	}
	/**
	 * 压缩
	 * @param bits 待压缩数据
	 * @return 压缩后数据
	 * @throws Exception
	 */
	public static byte[]  compress(byte[] bits)throws Exception 
	{
		InputStream is = new ByteArrayInputStream(bits); 
		ByteArrayOutputStream out = new ByteArrayOutputStream();
	    GZIPOutputStream gos = new GZIPOutputStream(out);  
	  
	    int count;  
	    byte data[] = new byte[1024];  
	    while ((count = is.read(data, 0, 1024)) != -1) {  
	        gos.write(data, 0, count);  
	    }  
	  
	    gos.flush();  
	    gos.finish();  
	    gos.close();  
	    byte[] result = out.toByteArray();
	    return result;
	}
	/**
	 * 压缩字符
	 * @param src
	 * @return
	 * @throws Exception
	 */
	public static String compress(String src) throws Exception 
	{
		return new String(compress(src.getBytes("utf-8")),"utf-8");
	}
	/**
	 * 
	 * @param src
	 * @return
	 * @throws Exception
	 */
	public static String decompress(String src) throws Exception 
	{
		return new String(decompress(src.getBytes("utf-8")),"utf-8");
	}
}
