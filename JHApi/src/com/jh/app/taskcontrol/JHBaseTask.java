package com.jh.app.taskcontrol;

import java.util.concurrent.atomic.AtomicBoolean;

import com.jh.app.taskcontrol.callback.ITaskLifeCycle;
import com.jh.app.taskcontrol.exception.JHTaskException;
import com.jh.app.taskcontrol.exception.JHTaskRemoveException;
import com.jh.exception.JHException;

/**
 * JH基础任务类
 * @author 099
 * @since 2016-3-11
 */
public abstract class JHBaseTask implements ITaskLifeCycle, Comparable<JHBaseTask>{
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
	/**用于特有标记比如上传，下载，activity等特殊任务**/
	private String mTaskTraget;
	/**task执行顺序，先进先出 */
    private Integer mSequence;
    /**是否激活*/
    private volatile boolean isActive=true;
	/**task权重**/
//	private int mTaskPriority=TaskPriority.PRIORITY_NORMAL;
    
    /***
     * 获取task等待超时时间
     * @return
     */
    protected long getTaskWaitTimeOut(){
    	return waitTimeOut;
    }
    /**
     * 获取task执行超时时间
     * @return
     */
    protected long getTaskRunningTimeOut(){
    	return runningTimeOut;
    }
    
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
	 final boolean cancel(boolean mayInterruptIfRunning) {
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
	/***
	 * 获取Task权重
	 * @return
	 */
	protected int getPriority() {
	    return TaskPriority.PRIORITY_NORMAL;
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
	/***
	 * 是否已经被调用
	 * @return true 已经执行过
	 * 		   false 未执行过
	 * 一个task应该只能执行一次
	 */
	public boolean isInvoked() {
		return mStatus!=TaskStatus.NONE;
	}
	/**
	 * 是否处于等待状态
	 * @return true  处于等待状态
	 * 		   false 处于其他状态
	 */
	public boolean isWaiting() {
		return mStatus==TaskStatus.PENDING;
	}
	/**
	 * 是否处于运行状态
	 * @return  true  处于等待状态
	 * 			false 处于其他状态
	 */
	public boolean isRunning(){
		return mStatus==TaskStatus.RUNNING;
	}
	/**
	 * 设置task状态值
	 * @param taskStatus
	 */
	 void setTaskStatus(int taskStatus){
		mStatus=taskStatus;
	}
	
	final JHBaseTask setSequence(int sequence) {
        mSequence = sequence;
        return this;
    }
	@Override
	public int compareTo(JHBaseTask another) {
		int left = this.getPriority();
        int right = another.getPriority();
        return left == right ?
                this.mSequence - another.mSequence :
                	left-right;
	}
	
	
	/**
	 * @return the mTaskTraget
	 */
	protected String getmTaskTraget() {
		return mTaskTraget;
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
	/**
	 * 设置金和taskException
	 * @param jhTaskRemoveException
	 */
	public void setException(JHTaskException jhTaskRemoveException) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * @return the isActive
	 */
	 boolean isActive() {
		return isActive;
	}
	/**
	 * @param isActive the isActive to set
	 */
	 void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	
}
