package com.jh.app.taskcontrol.exception;
/**
 * 任务执行时间超长异常
 * @author 099
 */
public class JHTaskRunningTimeOutException extends JHTaskException {

	private static final long serialVersionUID = 1L;
	
	private static final String TASK_RUNNING_TIMEOUT_EXCEPTION="taskrunningtimeoutexception";
	@Override
	public String getDefaultMessage() {
		return TASK_RUNNING_TIMEOUT_EXCEPTION;
	}
	

}
