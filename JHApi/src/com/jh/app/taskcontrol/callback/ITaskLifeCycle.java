package com.jh.app.taskcontrol.callback;

import com.jh.app.taskcontrol.task.JHBaseTask;
import com.jh.exception.JHException;

/**
 * task基础生命周期
 * @author 099
 * @since 2016-3-29
 */
public interface ITaskLifeCycle {
	/**
	 * 在主线程执行之前，执行在{@link #doTask}.
	 */
	void onPreExecute();
	/**
	 * 耗时操作，在子线程中执行
	 * @throws JHException
	 */
	void doTask() throws JHException;
    /**
     * 通知任务执行进度
     * 在主线程中执行，触发条件为{@link JHBaseTask #onProgressChanged(Object, int)}
     * @param bussinessData 具体业务数据（可以用泛型，但感觉兼容老的task有些麻烦）
     * @param newProgress   进度值
     */
    void onProgressChanged(Object bussinessData,int newProgress);
    /***
     * 执行成功
     */
    void success();
    /**
     * 执行失败
     * @param errorMessage 失败信息
     */
    void fail(String errorMessage);
	
}
