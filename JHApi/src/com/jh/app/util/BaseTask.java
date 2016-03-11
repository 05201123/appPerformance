package com.jh.app.util;

import com.jh.exception.JHException;

import android.R.integer;
import android.content.Context;


/**
 * <code>BaseTask</code>
 * @description: TODO(基本异步任务信息) 
 * @version  1.0
 * @author  yourname
 * @since 2012-2-3
 */
public abstract class BaseTask {
	//任务是否已退出。
	//private boolean canceled = false;
	//标识任务状态
	private int status = 0;
	//是否任务取消标志
	private boolean cancelFlag = false;
	//任务是否成功标志
	private boolean successFlag = false;
	public boolean isSuccess() {
		return successFlag;
	}
	public void setSuccessFlag(boolean successFlag) {
		setFinishStatus();
		this.successFlag = successFlag;
	}
	//错误信息
	private String errorMessage;
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public JHException getException() {
		return exception;
	}
	public void setException(JHException exception) {
		this.exception = exception;
	}
	//任务异常
	private JHException exception;
	//等待开始状态
	private static final int WAIT = 0;
	//任务执行中
	private static final int PROCESS = 1;
	//任务取消
	 //private static final int CANCEL = 2;
	//任务完成
	private static final int FINISH = 3;
	public void resetTask(){
		status = WAIT;
	}
	/**
	 * 退出任务
	 */
	public void cancelTask(){
		//setCancle(true);
		cancelFlag = true;
		
	}
	private void setFinishStatus(){
		status = FINISH;
	}
	/**
	 * 命令任务退出
	 * @param canceled
	 */
/*	private void setCancle(boolean canceled)
	{
		this.canceled = canceled;
	}*/
	public boolean isCancel()
	{
		return cancelFlag;
	}
	public boolean isCancelAtDialogDismiss() {
		return false;
	}
	/**
	 * <code>prepareTask</code>
	 * @description: TODO(执行前准备函数) 
	 * @param params
	 * @since   2012-2-3    yourname
	 */
	public void prepareTask(Void... params){
		status=PROCESS;
	};
	/**
	 * <code>doTask</code>
	 * @description: TODO(执行任务) 
	 * @throws POAException
	 * @since   2012-2-3    yourname
	 */
	public abstract void doTask() throws JHException;
	/**
	 * <code>success</code>
	 * @description: TODO(成功处理函数) 
	 * @since   2012-2-3    yourname
	 */
	public void success(){
		setFinishStatus();
	};
	public boolean hasFinish(){
		return status == FINISH;
	}
	/**
	 * <code>fail</code>
	 * @description: TODO(失败处理函数) 
	 * @param errorMessage
	 * @since   2012-2-3    yourname
	 */
	public void fail(JHException e){
		if(e!=null)
			fail(e.getMessage());
		else
			fail("");
	//	setFinishStatus();
	};
	/**
	 * <code>fail</code>
	 * @description: TODO(失败处理函数) 
	 * @param errorMessage
	 * @since   2012-2-3    yourname
	 */
	public void fail(String errorMessage){
		setFinishStatus();
	};
	/**
	 * 取消任务后调用函数
	 */
	public void cancel(){
		if(cancelListener!=null){
			cancelListener.cancel(this);
		}
	};
	public Context getContext(){return null;};
	private ITaskCancel cancelListener;
	ITaskCancel getCancelListener() {
		return cancelListener;
	}
	void setCancelListener(ITaskCancel cancelListener) {
		this.cancelListener = cancelListener;
	}
	public interface ITaskCancel{
		 void cancel(BaseTask task);
	}
}
