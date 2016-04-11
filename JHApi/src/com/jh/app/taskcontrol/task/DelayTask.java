package com.jh.app.taskcontrol.task;

import com.jh.app.taskcontrol.JHBaseTask;
import com.jh.app.taskcontrol.constants.TaskConstants.TaskPriority;
/***
 * 可延迟Task
 * @author 099
 * 等待时间无限制
 */
public abstract class DelayTask extends JHBaseTask {
	@Override
	protected int getPriority() {
		return TaskPriority.PRIORITY_DELAY;
	}

}
