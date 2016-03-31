package com.jh.app.taskcontrol;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jh.app.taskcontrol.callback.IThreadPoolStrategy;

/**
 * 任务线程池
 * @author 099
 * @since 2016-3-31
 */
public class JHTaskThreadPool {
	/**正常线程数量*/
	private  int corePoolSize;
	/**最大线程数量*/
    private  int maximumPoolSize;
    /**金和线程池选择策略**/
    private IThreadPoolStrategy iThreadPoolStrategy;
    /**当前执行线程数**/
    private volatile int mCurRunningTasksNum;
    
	public JHTaskThreadPool(int corePoolSize,
            int maximumPoolSize,IThreadPoolStrategy iThreadPoolStrategy){
		if (corePoolSize <= 0 || maximumPoolSize <= 0 ||maximumPoolSize <= corePoolSize){
			throw new IllegalArgumentException();
		}
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
		//TODO
	}
	/***
	 * 强制执行runnable
	 * @param runnable
	 */
	public void executeRunnableInForce(Runnable runnable){
		//TODO
	}
	/**
	 * 是否可以执行runnalbe
	 * @return 	true 有空闲
	 * 			false 满栈
	 */
	public boolean isCanExecRunnable(){
		//TODO
		return true;
	}
	/**
	 * 是否可以强制执行runnalbe
	 * @return 	true 有空闲
	 * 			false 满栈
	 */
	public boolean isCanForceExecRunnable(){
		//TODO
		return true;
	}
	
	
	
	
	/***
	 * 金和默认线程池
	 * @author 099
	 * @since 2016-3-31
	 */
	private class JHDefaultThreadPool implements IThreadPoolStrategy{
		/**常规线程池**/
		private ExecutorService executorService;
		/**临时线程池**/
		private ExecutorService tempExecutorService;
		/**临时线程池数量**/
		private int mTempTreadPoolCount;
		
		private JHDefaultThreadPool(int corePoolSize,int tempPoolSize){
			executorService=Executors.newFixedThreadPool(corePoolSize);
			mTempTreadPoolCount=tempPoolSize;
//			executorService.
		}
		
		
		
		
		
	}
	
	
	
}
