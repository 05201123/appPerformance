package com.jh.app.taskcontrol;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import android.os.Handler;
import android.util.Log;

import com.jh.app.taskcontrol.callback.IThreadPoolStrategy;
import com.jh.app.taskcontrol.handler.JHTaskHandler;
/**
 * 任务线程池
 * @author 099
 * @since 2016-3-31
 * temp线程池，打算用延迟销毁的方式，以免频繁的校验
 * //如果以后有机会，可以根据手机性能，型号，来决定使用哪种线程策略
 */
public class JHTaskThreadPool {
    /**金和线程池选择策略**/
    private IThreadPoolStrategy iThreadPoolStrategy;
	public JHTaskThreadPool(int corePoolSize,
            int maximumPoolSize,IThreadPoolStrategy iThreadPoolStrategy,Handler handler){
		if (corePoolSize <= 0 || maximumPoolSize <= 0 ||maximumPoolSize <= corePoolSize){
			throw new IllegalArgumentException();
		}
        if(iThreadPoolStrategy==null){
        	this.iThreadPoolStrategy=new JHDefaultThreadPool(corePoolSize,maximumPoolSize-corePoolSize);
		}
	}
	/**
	 * 执行runnalbe
	 * @param runnable
	 */
	public synchronized void executeRunnable(Runnable runnable){
		if(runnable==null){
			 throw new NullPointerException();
		}
		iThreadPoolStrategy.execute(runnable);
	}
	/**
	 * 是否可以执行runnalbe
	 * @return 	true 有空闲
	 * 			false 满栈
	 */
	public synchronized boolean isCanExecRunnable(){
		
		return iThreadPoolStrategy.isFree();
	
	}
	/**
	 * 是否可以强制执行runnalbe
	 * @return 	true 有空闲
	 * 			false 满栈
	 */
	public synchronized boolean isCanForceExecRunnable(){
		
		return iThreadPoolStrategy.isCanFroceExec();
	}
	public synchronized void exeFinished(boolean mIsTempThreadPool) {
		
		iThreadPoolStrategy.exeFinished(mIsTempThreadPool);
	}
	/***
	 * 金和默认线程池
	 * @author 099
	 * @since 2016-3-31
	 */
	private class JHDefaultThreadPool implements IThreadPoolStrategy{
		private final ReentrantLock mainLock = new ReentrantLock();
		 /**子线程Handler*/
	    private Handler mHandler;
		/**常规线程池**/
		private ThreadPoolExecutor executorService;
		/**临时线程池**/
		private ThreadPoolExecutor tempExecutorService;
		/**临时线程池数量**/
		private int mTempTreadPoolCount;
		/**临时线程池数量**/
		private int mCoreTreadPoolCount;
		/**临时线程池一分钟之内无任务，直接释放*/
		private static final long OVERLOAD_FREETIMEOUT=1000*60;
		/**常规线程池，当前执行的数量*/
		private volatile int curExeCoreCount=0;
		/**临时线程池，当前执行的数量**/
		private volatile int curExeTempCount=0;
		
		/**过载空闲超时的runnable**/
		private Runnable mOverLoadRunnable=new Runnable() {
			@Override
			public void run() {
				final ReentrantLock mLock=mainLock;
				mLock.lock();
				if(tempExecutorService!=null&&tempExecutorService.isTerminated()){
					tempExecutorService.shutdown();
					tempExecutorService=null;
				}
				mLock.unlock();
			}
		};
		
		private JHDefaultThreadPool(int corePoolSize,int tempPoolSize){
			mHandler=JHTaskHandler.getTaskHandler();
			mCoreTreadPoolCount=corePoolSize;
			executorService=newFixedThreadPool(corePoolSize);
			mTempTreadPoolCount=tempPoolSize;
		}
		/**
		 * 生成线程池{@link Executors #newFixedThreadPool(int)}
		 * @param nThreads
		 * @return
		 */
		private ThreadPoolExecutor newFixedThreadPool(int nThreads){
			return new ThreadPoolExecutor(nThreads, nThreads,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>());
		}
		@Override
		public void execute(Runnable runnable) {
			if(isFree()){
				curExeCoreCount++;
				Log.e("bbbbbbbbb", "curExeCoreCount ="+curExeCoreCount);
				executorService.execute(runnable);
			}else{
				final ReentrantLock mLock=mainLock;
				mLock.lock();
				if(tempExecutorService==null){
					tempExecutorService=newFixedThreadPool(mTempTreadPoolCount);
				}
				curExeTempCount++;
				tempExecutorService.execute(runnable);
				mHandler.removeCallbacks(mOverLoadRunnable);
				mHandler.postDelayed(mOverLoadRunnable, OVERLOAD_FREETIMEOUT);
				mLock.unlock();
			}
			
		}
		@Override
		public boolean isFree() {
			return curExeCoreCount<mCoreTreadPoolCount;
		}
		@Override
		public boolean isCanFroceExec() {
			/**临时线程池满栈，不执行**/
			boolean temp=true;
			final ReentrantLock mLock=mainLock;
			mLock.lock();
				if(tempExecutorService!=null&&curExeTempCount==mTempTreadPoolCount){
					temp= false;
				}
			mLock.unlock();
			
			return temp;
		}
		@Override
		public void exeFinished(boolean mIsTempThreadPool) {
			if(mIsTempThreadPool){
				curExeTempCount--;
				Log.e("cccccccccc", "curExeCoreCount ="+curExeCoreCount);
			}else{
				curExeCoreCount--;
				Log.e("aaaaaaaaaa", "curExeCoreCount ="+curExeCoreCount);
			}
			
			
		}
	}

	
}
