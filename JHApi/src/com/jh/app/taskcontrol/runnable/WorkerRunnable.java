package com.jh.app.taskcontrol.runnable;

import com.jh.app.taskcontrol.JHBaseTask;

/**
 * 实际放入线程池的runnnable
 * @author 099
 * @since 2016-4-5
 */
public  abstract class WorkerRunnable implements Runnable{
	protected JHBaseTask mTask;
	private  boolean mIsTempThreadPool;
	protected WorkerRunnable(JHBaseTask task,boolean isTmep){
		mTask=task;
		setmIsTempThreadPool(isTmep);
	}
	/**
	 * @return the mIsTempThreadPool
	 */
	public boolean ismIsTempThreadPool() {
		return mIsTempThreadPool;
	}
	/**
	 * @param mIsTempThreadPool the mIsTempThreadPool to set
	 */
	public void setmIsTempThreadPool(boolean mIsTempThreadPool) {
		this.mIsTempThreadPool = mIsTempThreadPool;
	}
}
