package com.jh.app.taskcontrol.callback;
/**
 * 线程池策略
 * @author 099
 *
 */
public interface IThreadPoolStrategy {
	/**
	 * 执行runnable
	 * @param runnable
	 */
	public void execute(Runnable runnable);
	/**线程池是否空闲*/
	public boolean isFree();
	/**是否可以执行紧急任务*/
	public boolean isCanFroceExec();
	/**
	 * 执行完成
	 * @param mIsTempThreadPool false 常规线程池执行完成
	 * 							true  temp线程池执行完成
	 */
	public void exeFinished(boolean mIsTempThreadPool);
}
