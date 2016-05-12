package com.jh.performance.threadoptimize.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jh.performance.utils.PerformanceUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
/**
 * 线程与内存的关系
 * @author 099
 *
 */
public class ThreadMemoryActivity extends Activity {
	private Handler mHandler=new Handler();
	private List<Thread> list=new ArrayList<Thread>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().getDecorView().post(new Runnable() {
			
			@Override
			public void run() {
				Log.e("performance","cup num ="+PerformanceUtils.getNumCores());
				execMainOper();
				for(int i=0;i<20;i++){
					Thread thread=new Thread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							
						}});
					thread.start();
					list.add(thread);
				}
				execMainOper();
				
				

				
			}

			private void execMainOper() {
				long starttime=System.currentTimeMillis();
				long startThreadTime=SystemClock.currentThreadTimeMillis();
				fib(35);
				long endtime=System.currentTimeMillis();
				long endThreadTime=SystemClock.currentThreadTimeMillis();
				Log.e("performance","mainThread"+"starttime= "+starttime+", endtime ="+endtime+", exectime ="+(endtime-starttime)
						+"; startThreadTime ="+startThreadTime+", endThreadTime ="+endThreadTime+",execThreadtime ="
						+(endThreadTime-startThreadTime));
			}
		});
		
		
//		for(int i=0;i<10;i++){
//			Thread thread=new Thread(new MyRunnable2(i+5));
//			thread.start();
//		}
//		for(int i=0;i<10;i++){
//			Thread thread=new Thread(new MyRunnable(i));
//			thread.start();
//		}
//		
//		ExecutorService executorService = Executors.newFixedThreadPool(5);
//		executorService.execute(new MyRunnable(41));
//		executorService.execute(new MyRunnable2(42));
		
//		long starttime=System.currentTimeMillis();
//		long startThreadTime=SystemClock.currentThreadTimeMillis();
//		fib(35);
//		long endtime=System.currentTimeMillis();
//		long endThreadTime=SystemClock.currentThreadTimeMillis();
//		Log.e("performance","mainThread"+"starttime= "+starttime+", endtime ="+endtime+", exectime ="+(endtime-starttime)
//				+"; startThreadTime ="+startThreadTime+", endThreadTime ="+endThreadTime+",execThreadtime ="
//				+(endThreadTime-startThreadTime));
		
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
			Log.e("performance",myName+"starttime= "+starttime+", endtime ="+endtime+", exectime ="+(endtime-starttime)
					+"; startThreadTime ="+startThreadTime+", endThreadTime ="+endThreadTime+",execThreadtime ="
					+(endThreadTime-startThreadTime));
			
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
				Log.e("performance",myName+"starttime= "+starttime+", endtime ="+endtime+", exectime ="+(endtime-starttime)
						+"; startThreadTime ="+startThreadTime+", endThreadTime ="+endThreadTime+",execThreadtime ="
						+(endThreadTime-startThreadTime));
				
			}
			
		}
	
	
}
