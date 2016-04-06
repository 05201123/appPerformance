package com.jh.app.taskcontrol.exception;
/**
 * 任务等待时间超长异常
 * @author 099
 * 对targettask，操作异常的命名
 */
public class JHTaskWaitTimeOutException extends JHTaskException {

	private static final long serialVersionUID = 1L;
	
	private static final String TASK_WAIT_TIMEOUT_EXCEPTION="taskwaittimeoutexception";
	@Override
	public String getDefaultMessage() {
		return TASK_WAIT_TIMEOUT_EXCEPTION;
	}
	

}
