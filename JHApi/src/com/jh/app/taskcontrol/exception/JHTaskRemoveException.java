package com.jh.app.taskcontrol.exception;
/**
 * 任务主动删除的异常
 * @author 099
 *
 */
public class JHTaskRemoveException  extends JHTaskException{
	private static final long serialVersionUID = 1L;
	private static final String REMOVE_TASK_EXCEPTION="removetaskexcepiton";
	@Override
	public String getDefaultMessage() {
		return REMOVE_TASK_EXCEPTION;
	}
}
