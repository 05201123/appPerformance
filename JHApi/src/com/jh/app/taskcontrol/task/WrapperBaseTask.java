package com.jh.app.taskcontrol.task;

import com.jh.app.taskcontrol.JHBaseTask;
import com.jh.app.util.BaseTask;
import com.jh.exception.JHException;
/***
 * 可延迟Task
 * @author 099
 * 等待时间无限制
 * BaseTask resetTask 不支持，测试时注意
 */
public  class WrapperBaseTask extends JHBaseTask {
	/**任务task*/
	private BaseTask mBaseTask;
	public WrapperBaseTask(BaseTask baseTask){
		mBaseTask=baseTask;
	}
	@Override
	public void doTask() throws JHException {
		mBaseTask.doTask();
	}
	
	@Override
	public void success() {
		mBaseTask.setSuccessFlag(true);
		super.success();
		mBaseTask.success();
	}
	@Override
	public void fail(String errorMessage) {
		mBaseTask.fail(getException());
		mBaseTask.setSuccessFlag(false);
		mBaseTask.setErrorMessage(errorMessage);
		super.fail(errorMessage);
		mBaseTask.fail(errorMessage);
	}
	@Override
	protected void notifyTaskCancel() {
		if(mBaseTask.getCancelListener()!=null){
			mBaseTask.getCancelListener().cancel(mBaseTask);
		}
	}
	@Override
	public void setException(JHException jhException) {
		mBaseTask.setException(jhException);
	}
	
	@Override
	protected boolean cancel(boolean mayInterruptIfRunning) {
		mBaseTask.cancelTask();
		return super.cancel(mayInterruptIfRunning);
	}
	@Override
	public void onPreExecute() {
		super.onPreExecute();
		mBaseTask.prepareTask();
	}
	
	
	
}
