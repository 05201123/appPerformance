package com.jh.app.taskcontrol.callback;

import com.jh.app.taskcontrol.constants.TaskContants;

/***
 * 一组task执行完成的linsener
 * @author 099
 *
 */
public abstract class ITaskFinishLinsener {
	/**task分组tag，用于上传，下载等特殊task*/
	private String taskGroupTag;
	public ITaskFinishLinsener(){
		this(TaskContants.ALL_TASK);
	}
	
	public ITaskFinishLinsener(String taskGroupTag){
		this.taskGroupTag=taskGroupTag;
	}
	/**
	 * 获取taskGroup标识
	 * @return
	 */
	public String getTaskGroupTag() {
		return taskGroupTag;
	}
	/**
	 * 通知某个GroupTag的task组执行完
	 * @param taskGroupTag
	 */
	public abstract void notifyGroupTagFinish(String taskGroupTag);
	
}
