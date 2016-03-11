package com.jh.app.util;

import com.jh.app.util.ConcurrenceExcutor;
import com.jh.app.util.ConcurrenceExcutor.BaseRunnable;

/**
 * 串行执行的任务，任务按插入顺序执行。
 * @author jhzhangnan1
 *
 */
public class SerialExcutor {
	
	private ConcurrenceExcutor taskQueue = new ConcurrenceExcutor(1){
		private BaseTask currentTask;
		@Override
		protected synchronized void iterateTask() {
			// TODO Auto-generated method stub
			if(waitTasks != null&&waitTasks.size()>0)
			{
				if(currentTask==null||currentTask.hasFinish())
				{
					currentTask = waitTasks.poll();
					
					if(currentTask!=null)
					{
						if(!currentTask.isCancel())
						{
							mainHandler.post(new BaseRunnable(currentTask) {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(currentTask!=null)
								{
									if(!currentTask.isCancel())
										prepare(currentTask);
									else
									{
										currentTask.cancel();
										iterateTask();
									}
										
								}
								else
								{
									iterateTask();
								}
							}
							});
						}
						else
						{
							currentTask.cancel();
						}
					}
				}
			}
		}
		
	};
	private static SerialExcutor executor =  new SerialExcutor();
	private SerialExcutor(){
		
	}
	public static SerialExcutor getInstance(){
		return executor;
	}
	public void execute(BaseTask task){
		taskQueue.appendTask(task);
	}
	public void removeTask(BaseTask task){
		taskQueue.cancelTask(task);
	}
}
