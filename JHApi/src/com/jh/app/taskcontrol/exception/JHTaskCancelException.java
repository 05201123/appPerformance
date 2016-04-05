package com.jh.app.taskcontrol.exception;
/**
 * 任务主动取消的异常
 * @author 099
 *
 */
public class JHTaskCancelException  extends JHTaskException{
	private static final long serialVersionUID = 1L;
	private static final String CANCEL_TASK_EXCEPTION="canceltaskexcepiton";
	@Override
	public String getDefaultMessage() {
		return CANCEL_TASK_EXCEPTION;
	}
}
