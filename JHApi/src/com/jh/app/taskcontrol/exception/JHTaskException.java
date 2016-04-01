package com.jh.app.taskcontrol.exception;

import com.jh.exception.JHException;
/***
 * 金和task异常类
 * @author 099
 *
 */
public class JHTaskException extends JHException {
	
	private static final String JH_TASK_EXCEPTION="jhtaskexcepiton";
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public String getDefaultMessage() {
		return JH_TASK_EXCEPTION;
	}
}
