package com.jh.util;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.jh.common.app.application.AppSystem;
import com.jh.common.cache.FileCache;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

public class LogUtil {
	private static boolean release = false;
	private Context context;
	private String tag;
	private static final String warnName = "log_warn.txt";
	private String filePath;
	private LogUtil(Context context){
		this(context,context.getPackageName());
	}
	 public static boolean isApkDebugable(Context context) {
	        try {
	            ApplicationInfo info= context.getApplicationInfo();
	                return (info.flags&ApplicationInfo.FLAG_DEBUGGABLE)!=0;
	        } catch (Exception e) {
	            
	        }
	        return false;
	}
	private LogUtil(Context context,String tag){
		this.context = context;
		this.tag = tag;
		filePath = FileCache.getInstance(context).createOtherFile(warnName);
		release = !isApkDebugable(context);
		//release = false;
	}
	public static LogUtil newInstance(Context context){
		return new LogUtil(context);
	}
	public static LogUtil newInstance(Context context,String tag){
		return new LogUtil(context,tag);
	}
	private void writeFile(String tag,String msg)
	{
		synchronized (warnName) {
			try {
				OutputStream warnOutput = new BufferedOutputStream(new FileOutputStream(filePath));
				if(warnOutput!=null)
				{
					;
					warnOutput.write(tag.getBytes("utf-8"));
					warnOutput.write(":".getBytes("utf-8"));
					warnOutput.write(msg.getBytes("utf-8"));
					warnOutput.write("\n".getBytes("utf-8"));
					warnOutput.write("stack:".getBytes("utf-8"));
					warnOutput.write(new Throwable().getStackTrace().toString().getBytes("utf-8"));
					warnOutput.write("\n".getBytes("utf-8"));
					close(warnOutput);
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void error(String msg)
	{
		error(tag, msg);
	}
	public void error(String tag,String msg)
	{
		if(!release)
			Log.e(tag, msg);
		writeFile(tag, msg);
	}
	public void warn(String msg)
	{
		warn(tag, msg);
	}
	public void warn(String tag,String msg)
	{
		if(!release)
			Log.w(tag, msg);
	//	writeFile(tag, msg);
	}
	public void info(String msg)
	{
		info(tag, msg);
	}
	public void info(String tag,String msg)
	{
		if(!release)
			Log.i(tag, msg);
	//	writeFile(tag, msg);
	}
	private void close(OutputStream warnOutput)
	{
		try {
			if(warnOutput!=null)
			{
				warnOutput.flush();
				warnOutput.close();
				warnOutput = null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void println(String string) {
		// TODO Auto-generated method stub
		new LogUtil(AppSystem.getInstance().getContext()).info(string);
	}

}
