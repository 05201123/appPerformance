package com.jh.app.taskcontrol;

import android.os.SystemClock;
import junit.framework.TestCase;
/**
 * JHTaskThreadPool测试类
 * @author 099
 *
 */
public class JHTaskThreadPoolTest extends TestCase{
	private JHTaskThreadPool mThreadPool;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mThreadPool=new JHTaskThreadPool(10, 15, null, null);
	}
	/***
	 * 检测TaskThread isFree（）单线程
	 *//*
	public void test1IsFreeSingleThread(){
		for(int i=0;i<10;i++){
			mThreadPool.executeRunnable(createRunnable(200));
			if(i==9){
				assertEquals(false, mThreadPool.isCanExecRunnable());
			}else{
				assertEquals(true, mThreadPool.isCanExecRunnable());
			}
		}
		SystemClock.sleep(400);
		assertEquals(true, mThreadPool.isCanExecRunnable());
	}*/
	
	/***
	 * 检测TaskThread isFree（）多线程
	 */
	public void test2IsFreeMulThread(){
		Thread thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				mThreadPool.executeRunnable(createRunnable(100));
				mThreadPool.executeRunnable(createRunnable(100));
				mThreadPool.executeRunnable(createRunnable(100));
				mThreadPool.executeRunnable(createRunnable(100));
				mThreadPool.executeRunnable(createRunnable(400));
			}
		});
		Thread thread2=new Thread(new Runnable() {
			
			@Override
			public void run() {
				mThreadPool.executeRunnable(createRunnable(100));
				mThreadPool.executeRunnable(createRunnable(100));
				mThreadPool.executeRunnable(createRunnable(100));
				mThreadPool.executeRunnable(createRunnable(100));
				mThreadPool.executeRunnable(createRunnable(100));
			}
		});
		try {
			thread2.start();
			thread.start();
			thread.join();
			thread2.join();
			assertEquals(false, mThreadPool.isCanExecRunnable());
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		SystemClock.sleep(300);
		assertEquals(true, mThreadPool.isCanExecRunnable());
		mThreadPool.executeRunnable(createRunnable(100));
		assertEquals(true, mThreadPool.isCanExecRunnable());
		mThreadPool.executeRunnable(createRunnable(100));
		assertEquals(true, mThreadPool.isCanExecRunnable());
		mThreadPool.executeRunnable(createRunnable(100));
		assertEquals(true, mThreadPool.isCanExecRunnable());
		mThreadPool.executeRunnable(createRunnable(100));
		assertEquals(true, mThreadPool.isCanExecRunnable());
		mThreadPool.executeRunnable(createRunnable(100));
		assertEquals(true, mThreadPool.isCanExecRunnable());
		mThreadPool.executeRunnable(createRunnable(100));
		assertEquals(true, mThreadPool.isCanExecRunnable());
		mThreadPool.executeRunnable(createRunnable(100));
		assertEquals(true, mThreadPool.isCanExecRunnable());
		mThreadPool.executeRunnable(createRunnable(100));
		assertEquals(true, mThreadPool.isCanExecRunnable());
		mThreadPool.executeRunnable(createRunnable(100));
		assertEquals(false, mThreadPool.isCanExecRunnable());
		SystemClock.sleep(300);
	}
	
	
	
	
	
	
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	
	
	private Runnable createRunnable(long sleepTime) {
		Runnable r=new WorkRunnable(sleepTime) {
			@Override
			public void run() {
				SystemClock.sleep(mSleepTime);
				mThreadPool.exeFinished(false);
			}
		};
		return r;
	}
	private abstract class WorkRunnable implements Runnable{
		protected long mSleepTime;
		WorkRunnable(long sleepTime){
			this.mSleepTime=sleepTime;
		}
	}
}
