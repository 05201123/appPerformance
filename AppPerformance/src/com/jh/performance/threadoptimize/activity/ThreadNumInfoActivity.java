package com.jh.performance.threadoptimize.activity;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jh.performance.utils.PerformanceUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
/**
 * 线程数量信息
 * @author 099
 *
 */
public class ThreadNumInfoActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		Log.e("performance","cup num ="+PerformanceUtils.getNumCores());
		
		for(int i=0;i<10;i++){
			Thread thread=new Thread(new MyRunnable2(i+5));
			thread.start();
		}
		for(int i=0;i<10;i++){
			Thread thread=new Thread(new MyRunnable(i));
			thread.start();
		}
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		executorService.execute(new MyRunnable(41));
		executorService.execute(new MyRunnable2(42));
		
		Log.e("performance", "thread active num = "+Thread.activeCount());
		
		 Map<Thread, StackTraceElement[]> threadMap=Thread.getAllStackTraces();
		 Set<Thread> threadSet=threadMap.keySet();
		 for(Thread thread:threadSet){
			 Log.e("performance", "treadinfo name= "+thread.getName()+" id= "+thread.getId()+" isAlive="+thread.isAlive());
			 StackTraceElement[] Elements=threadMap.get(thread);
			 if(Elements!=null&&Elements.length>0){
				 for(int i=0;i<Elements.length;i++){
					 StackTraceElement element=Elements[i];
					 if(i==5){
						 Log.e("performance", "classname= "+element.getClassName()+" methodname= "+element.getMethodName()+" All="+element.toString());
						 break;
					 }
					 if(i==Elements.length-1){
						 Log.e("performance", "classname= "+element.getClassName()+" methodname= "+element.getMethodName()+" All="+element.toString());
						 break;
					 }
					 if(element.toString().contains("com.jh")){
						 Log.e("performance", "classname= "+element.getClassName()+" methodname= "+element.getMethodName()+" All="+element.toString());
						 break; 
					 }
					 
				 }
			 }

		 }
		 
	
		
	}
	public static long fib(int n) {
        if (n <= 1) return n;
        else return fib(n-1) + fib(n-2);
    }
	 class MyRunnable implements Runnable{
		
		String myName;
		MyRunnable(int i){
			myName="subthread i="+i;
		}
		@Override
		public void run() {
			long starttime=System.currentTimeMillis();
			long startThreadTime=SystemClock.currentThreadTimeMillis();
			fib(35);
			long endtime=System.currentTimeMillis();
			long endThreadTime=SystemClock.currentThreadTimeMillis();
//			Log.e("performance",myName+"starttime= "+starttime+", endtime ="+endtime+", exectime ="+(endtime-starttime)
//					+"; startThreadTime ="+startThreadTime+", endThreadTime ="+endThreadTime+",execThreadtime ="
//					+(endThreadTime-startThreadTime));
			
		}
		
	}
	 class MyRunnable2 implements Runnable{
			
			String myName;
			MyRunnable2(int i){
				myName="subthread i="+i;
			}
			@Override
			public void run() {
				android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
				long starttime=System.currentTimeMillis();
				long startThreadTime=SystemClock.currentThreadTimeMillis();
				fib(35);
				long endtime=System.currentTimeMillis();
				long endThreadTime=SystemClock.currentThreadTimeMillis();
//				Log.e("performance",myName+"starttime= "+starttime+", endtime ="+endtime+", exectime ="+(endtime-starttime)
//						+"; startThreadTime ="+startThreadTime+", endThreadTime ="+endThreadTime+",execThreadtime ="
//						+(endThreadTime-startThreadTime));
				
			}
			
		}
	
	
}
