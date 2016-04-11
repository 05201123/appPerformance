package com.jh.app.taskcontrol.task;

import com.jh.app.taskcontrol.JHBaseTask;
/**
 * 下载的任务
 * @author 099
 *
 */
public abstract class DownLoadTask extends JHBaseTask {
	/**downLoad 任务类的task*/
	private static final String DOWNLOAD_TASK_TRAGET="download";
		@Override
		protected String getmTaskTraget() {
			return DOWNLOAD_TASK_TRAGET;
		}
    
    
}
