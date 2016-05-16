package com.jh.peroptimize.utils.thread;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.jh.peroptimize.utils.log.LogWriter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

/**
 * 获取线程信息Utils
 * @author 099
 *
 */
public class ThreadCanary {
	public static final String SEPARATOR = "\r\n";
	/**单例*/
	public static ThreadCanary instance=new ThreadCanary();
	private Handler mHandler;
	/**文件存档路径*/
	private String savePath="/performance/threadcanary";
	/**收集时间间隔*/
	private long timespace=300;
	/**是否重复收集**/
	private boolean isRepeat=true;
	/**上下文*/
	private Context mContext;
	/****/
	private Runnable mTicker;
	
	private ThreadCanary(){
		 android.os.HandlerThread handlerThread = new android.os.HandlerThread("ThreadCanary");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
        mTicker=new GetThreadInfoRunnable();
	}
	public static ThreadCanary getInstance(){
		return instance;
	}
	public ThreadCanary init(Context context,CollectThreadInfoParams params){
		mContext=context;
		if(params!=null){
			if(!TextUtils.isEmpty(params.getSavePath())){
				savePath=params.getSavePath();
			}
			if(params.getTimespace()>100){
				timespace=params.getTimespace();
			}
			isRepeat=params.isRepeat();
		}
		return instance;
	}
	
	/**
	 * 开启收集信息
	 * @param conetxt
	 */
	public  void startGetThreadInfo(){
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				mHandler.removeCallbacks(mTicker);
				mHandler.postDelayed(mTicker, timespace);
				
			}
		});
	}
	
	
	
	/**
	 * 开启收集信息
	 * @param conetxt
	 */
	public  void stopGetThreadInfo(){
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				mHandler.removeCallbacks(mTicker);
				
			}
		});
		
	}
	/***
	 * 获取线程信息
	 * @return
	 */
	private  String getThreadInfo(){
		StringBuffer sb=new StringBuffer();
		sb.append("currenttime = "+new Date().toString()+" thread active num = "+Thread.activeCount()).append(SEPARATOR);
		 Map<Thread, StackTraceElement[]> threadMap=Thread.getAllStackTraces();
		 Set<Thread> threadSet=threadMap.keySet();
		 for(Thread thread:threadSet){
			 sb.append("treadinfo name= "+thread.getName()+" id= "+thread.getId()+" isAlive="+thread.isAlive()).append(SEPARATOR);
			 StackTraceElement[] Elements=threadMap.get(thread);
			 if(Elements!=null&&Elements.length>0){
				 for(int i=0;i<Elements.length;i++){
					 StackTraceElement element=Elements[i];
//					 sb.append("  treadinfo = "+element.toString()).append(SEPARATOR);
					 if(i==0&&element.toString().contains("java.lang.Object.wait")){
						 sb.append("  treadinfo = "+element.toString()).append(SEPARATOR); 
					 }
					 if(i==5){
						 sb.append("  treadinfo = "+element.toString()).append(SEPARATOR);
						 break;
					 }
					 if(i==Elements.length-1){
						 sb.append("  treadinfo = "+element.toString()).append(SEPARATOR);
						 break;
					 }
					 if(element.toString().contains("com.jh")){
						 sb.append("  treadinfo = "+element.toString()).append(SEPARATOR);
						 break; 
					 }
					 
				 }
			 }

		 }
		 return sb.toString();
	}
	/**
	 * 获取线程信息的Runnable
	 * @author 099
	 *
	 */
	class GetThreadInfoRunnable implements Runnable{

		@Override
		public void run() {
			
			//TODO 填写字符串
			LogWriter.saveLooperLog(mContext, savePath, getThreadInfo());
			
			if(isRepeat){
				mHandler.postDelayed(mTicker, timespace);
			}
			
		}
		
	}
	
	
}
