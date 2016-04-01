package com.jh.app.taskcontrol.exception;
/**
 * 任务特殊标记异常
 * @author 099
 * 对targettask，操作异常的命名
 */
public class TargetTaskExeception extends JHTaskException {

	private static final long serialVersionUID = 1L;
	
	private static final String TARGET_TASK_EXCEPTION="targettaskexcepiton";
	@Override
	public String getDefaultMessage() {
		return TARGET_TASK_EXCEPTION;
	}
	

}
