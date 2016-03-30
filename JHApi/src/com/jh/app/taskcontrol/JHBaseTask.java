package com.jh.app.taskcontrol;

import java.util.concurrent.atomic.AtomicBoolean;

import android.os.AsyncTask;

import com.jh.app.taskcontrol.callback.ITaskLifeCycle;
import com.jh.exception.JHException;

/**
 * JH基础任务类
 * @author 099
 * @since 2016-3-11
 */
public abstract class JHBaseTask implements ITaskLifeCycle{
	/**超时时间未设置，认为是无限大**/
	private static final long WAIT_TIMEOUT_NONE=-1;
	/**执行超时时间未设置，任务是无限大**/
	private static final long RUNNING_TIMEOUT_NONE=-1;
	/**task的状态值*/
	private volatile int mStatus=TaskStatus.NONE;
	/**task是否已经取消*/
	private final AtomicBoolean mCancelled = new AtomicBoolean();
	/**等待超时时间**/
	private long waitTimeOut=WAIT_TIMEOUT_NONE;
	/**执行超时时间**/
	private long runningTimeOut=RUNNING_TIMEOUT_NONE;
	/**用于分组标记**/
	private String taskTraget;
	/**task权重**/
	private int taskPriority=TaskPriority.PRIORITY_NORMAL;
	/**
	 * 判断任务是否已经取消
	 * @return
	 */
	public final boolean isCancelled() {
        return mCancelled.get();      
    }
	/**
	 * 取消task
	 * @param mayInterruptIfRunning 是否终止线程
	 * @return 
	 */
	public final boolean cancel(boolean mayInterruptIfRunning) {
        mCancelled.set(true);
        //TODO 取消任务
        return false;
    }
	/**
	 * 判断是否需要等待
	 * @return true  将task压入线程池中
	 * 		   false 不将task压入线程池中，而是压入等待队列中
	 */
	protected boolean  isNeedWait() {
		return false;
	}
	
	/**
	 * 调用在dotask方法中，用于通知更新UI进度条，{@link ITaskLifeCycle #onProgressChanged(Object, int)}
	 * @param values
	 */
	 protected final void publishProgress(Object bussiness,int newProgress) {
	        if (!isCancelled()) {
//	            sHandler.obtainMessage(MESSAGE_POST_PROGRESS,
//	                    new AsyncTaskResult<Progress>(this, values)).sendToTarget();
	        }
	    }
	
	@Override
	/**doSomething before doTask(); in UIthread**/
	public void onPreExecute() {
	
	}
	@Override
	/**notify progress in backgroundThread*/
	public abstract void doTask() throws JHException;
	@Override
	/**notify progress in UIThread*/
	public void onProgressChanged(Object bussinessData, int newProgress) {
	
	}
	@Override
	/**notify success in UIThread*/
	public void success() {
		
	}
	@Override
	/**notify fail in UIThread*/
	public void fail(String errorMessage) {
		
		
	}
	/**
	 * 设置task状态值
	 * @param taskStatus
	 */
	public void setTaskStatus(int taskStatus){
		mStatus=taskStatus;
	}
	
	/**
	 * 任务状态静态类
	 * @author 099
	 * 0----排队中
	 * 1----执行中
	 * 2----完成
	 */
	public static  class TaskStatus{
		/**初始状态未放到等待队列之中**/
		static int NONE=-1;
		/**等待中**/
		static int PENDING=0;
		/**执行中**/
		static int RUNNING=1;
		/**结束中**/
		static int FINISHED=2;
	}
	
	/**
	 * 任务权重静态类
	 * @author 099
	 * 1----可延迟
	 * 2----普通
	 * 3----重要
	 * 4----立即执行
	 */
	public static  class TaskPriority{
		/**立即执行**/
		static int PRIORITY_IMMEDIATE=4;
		/**重要**/
		static int PRIORITY_FOREGROUND=3;
		/**普通**/
		static int PRIORITY_NORMAL=2;
		/**可延迟**/
		static int PRIORITY_DELAY=1;
	}
	
}
