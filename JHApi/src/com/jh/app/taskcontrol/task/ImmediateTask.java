package com.jh.app.taskcontrol.task;

import com.jh.app.taskcontrol.JHBaseTask;
import com.jh.app.taskcontrol.constants.TaskConstants.TaskPriority;
/**
 * 立即执行的task
 * @author 099
 *
 */
public abstract class ImmediateTask extends JHBaseTask {
	/**任务执行超时时间**/
	private static final long TASK_RUNNING_TIMEOUT=40*1000;
    @Override
    protected int getPriority() {
    	return TaskPriority.PRIORITY_IMMEDIATE;
    }
    
    @Override
    protected long getTaskRunningTimeOut() {
    	return TASK_RUNNING_TIMEOUT;
    }
}
