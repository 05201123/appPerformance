package com.jh.app.taskcontrol;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.os.Handler;
import android.os.Looper;

import com.jh.app.taskcontrol.callback.IThreadPoolStrategy;
import com.jh.app.taskcontrol.handler.JHTaskHandler;

/**
 * 任务线程池
 * @author 099
 * @since 2016-3-31
 *
 */
public class JHTaskThreadPool {
	//TODO   temp线程池，打算用延迟销毁的方式，以免频繁的校验
	/**正常线程数量*/
	private  int corePoolSize;
	/**最大线程数量*/
    private  int maximumPoolSize;
    /**金和线程池选择策略**/
    private IThreadPoolStrategy iThreadPoolStrategy;
    /**当前执行线程数**/
    private volatile int mCurRunningTasksNum;
    /**子线程Handler*/
    private Handler mHandler;
    
	public JHTaskThreadPool(int corePoolSize,
            int maximumPoolSize,IThreadPoolStrategy iThreadPoolStrategy,Handler handler){
		if (corePoolSize <= 0 || maximumPoolSize <= 0 ||maximumPoolSize <= corePoolSize){
			throw new IllegalArgumentException();
		}
		if(handler==null){
			mHandler=JHTaskHandler.getTaskHandler();
		}
		this.mHandler=handler;
		this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        
        if(iThreadPoolStrategy==null){
        	//如果以后有机会，可以根据手机性能，型号，来决定使用哪种线程策略
        	iThreadPoolStrategy=new JHDefaultThreadPool(corePoolSize,maximumPoolSize-corePoolSize);
		}
 
	}
	/**
	 * 执行runnalbe
	 * @param runnable
	 */
	public void executeRunnable(Runnable runnable){
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
	public boolean isCanExecRunnable(){
		return iThreadPoolStrategy.isFree();
	}
	/**
	 * 是否可以强制执行runnalbe
	 * @return 	true 有空闲
	 * 			false 满栈
	 */
	public boolean isCanForceExecRunnable(){
		
		return iThreadPoolStrategy.isCanFroceExec();
	}
	
	/***
	 * 金和默认线程池
	 * @author 099
	 * @since 2016-3-31
	 */
	private class JHDefaultThreadPool implements IThreadPoolStrategy{
		/**常规线程池**/
		private ThreadPoolExecutor executorService;
		/**临时线程池**/
		private ThreadPoolExecutor tempExecutorService;
		/**临时线程池数量**/
		private int mTempTreadPoolCount;
		/**临时线程池一分钟之内无任务，直接释放*/
		private static final long OVERLOAD_FREETIMEOUT=1000*60;
		/**过载空闲超时的runnable**/
		private Runnable mOverLoadRunnable=new Runnable() {
			@Override
			public void run() {
				if(tempExecutorService!=null&&tempExecutorService.isTerminated()){
					tempExecutorService.shutdown();
					tempExecutorService=null;
				}
			}
		};
		
		private JHDefaultThreadPool(int corePoolSize,int tempPoolSize){
			executorService=newFixedThreadPool(tempPoolSize);
			mTempTreadPoolCount=tempPoolSize;
		}
		/**
		 * 生成线程池{@link Executors #newFixedThreadPool(int)}
		 * @param nThreads
		 * @return
		 */
		private ThreadPoolExecutor newFixedThreadPool(int nThreads){
			return new ThreadPoolExecutor(corePoolSize, corePoolSize,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>());
		}
		@Override
		public void execute(Runnable runnable) {
			if(executorService.getActiveCount()==executorService.getCorePoolSize()){
				if(tempExecutorService==null){
					tempExecutorService=newFixedThreadPool(mTempTreadPoolCount);
				}
				tempExecutorService.execute(runnable);
				mHandler.removeCallbacks(mOverLoadRunnable);
				mHandler.postDelayed(mOverLoadRunnable, OVERLOAD_FREETIMEOUT);
				
			}else{
				executorService.execute(runnable);
			}
			
		}
		@Override
		public boolean isFree() {
			return executorService.getActiveCount()<executorService.getCorePoolSize();
		}
		@Override
		public boolean isCanFroceExec() {
			/**临时线程池满栈，不执行**/
			if(tempExecutorService!=null&&tempExecutorService.getActiveCount()==mTempTreadPoolCount){
				return true;
			}
			return false;
		}
	}
}
