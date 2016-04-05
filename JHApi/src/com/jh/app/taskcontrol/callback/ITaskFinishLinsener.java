package com.jh.app.taskcontrol.callback;


/***
 * 一组task执行完成的linsener
 * @author 099
 *
 */
public interface ITaskFinishLinsener {
	/**
	 * 通知某个GroupTag的task组执行完
	 * @param taskGroupTag
	 */
	public abstract void notifyGroupTagFinish(String taskGroupTag);
	
}
