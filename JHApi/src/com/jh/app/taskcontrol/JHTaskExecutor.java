package com.jh.app.taskcontrol;
import java.util.HashSet;

import com.jh.app.taskcontrol.JHBaseTask.TaskStatus;
import com.jh.app.taskcontrol.callback.ITaskFinishLinsener;
import com.jh.app.taskcontrol.exception.JHTaskRemoveException;
import com.jh.app.taskcontrol.handler.JHTaskHandler;

import android.os.Handler;
import android.os.Message;
 /**
  * JH任务控制器
  * @author 099
  * @since 2016-3-11
  */
public class JHTaskExecutor {
	/**单例**/
	private static JHTaskExecutor excutor=new JHTaskExecutor();
	/**CPU数量*/
	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	/**线程池tread数量**/
	private int corePoolSize=CPU_COUNT * 2 + 1;	
	/**任务的等待超时msg**/
	private static final int MSG_TASK_WAIT_TIMEOUT=100;
	/**子线程Handler**/
	private Handler mChildThreadHandler;
	/**任务队列*/
	private JHTaskQueue mTaskQueue;
	/**任务线程池*/
	private JHTaskThreadPool mTaskThreadPool;
	
	private JHTaskExecutor(){
		mChildThreadHandler=new Handler(JHTaskHandler.getTaskLooper()){
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case MSG_TASK_WAIT_TIMEOUT:
						//TODO task等待超时
						break;
				}
			};
		};
		mTaskQueue=new JHTaskQueue();
		mTaskThreadPool=new JHTaskThreadPool(corePoolSize, corePoolSize+5, null, mChildThreadHandler);
	}
	/**
	 * 获取实例
	 * @return
	 */
	public static JHTaskExecutor getInstance(){
		return excutor;
	}
	
	/**
	 * 将task添加到同Priority的队列
	 * @param baseTask
	 */
	public  void addTask(JHBaseTask baseTask){
		addTaskFirst(baseTask, false);
	}
	/**
	 * 将task添加到同Priority的队列第一位上
	 * @param baseTask
	 */
	public  void addTaskFirst(JHBaseTask baseTask){
		addTaskFirst(baseTask, true);
	}
	/**
	 * 将task添加到同Priority的队列第一位上
	 * @param baseTask
	 */
	public  void addTaskFirst(JHBaseTask baseTask,boolean isSetFirst){
		//TODO 枷锁
		if(baseTask!=null){
			if(baseTask.isInvoked()){
				throw new  RuntimeException("task multiple invoke exception");
			}
			if(mTaskQueue.enqueueTask(baseTask, isSetFirst)){
				baseTask.setTaskStatus(TaskStatus.PENDING);
				sendWaitDelayTimeOutMessage(baseTask);
				executeTask();
			}
		}else{
			throw new NullPointerException();
		}
	}
	/**
	 * 执行task
	 */
	private void executeTask() {
		// TODO Auto-generated method stub
		
	}
	/***
	 * 发送task等待队列中超时的task
	 * @param baseTask
	 */
	private void sendWaitDelayTimeOutMessage(JHBaseTask baseTask) {
		if(baseTask.getTaskRunningTimeOut()>0){
			Message msg=Message.obtain();
			msg.what=MSG_TASK_WAIT_TIMEOUT;
			msg.obj=baseTask;
			mChildThreadHandler.sendMessageDelayed(msg, baseTask.getTaskRunningTimeOut());
		}
	}
	/**
	 * 删除task等待队列中超时的task
	 * @param baseTask
	 */
	private void clearWaitDelayTimeOutMessage(JHBaseTask baseTask) {
		mChildThreadHandler.removeMessages(MSG_TASK_WAIT_TIMEOUT, baseTask);
	}

	/**
	 * 从等待队列中删除task
	 * @param baseTask
	 */
	public void removeWaitTask(JHBaseTask baseTask){
		if(baseTask==null){
			throw new NullPointerException();
		}
		if(baseTask.isWaiting()){
			if(mTaskQueue.removeWaitTask(baseTask)){
				baseTask.setTaskStatus(TaskStatus.FINISHED);
				baseTask.setException(new JHTaskRemoveException());
				//TODO 通知task执行完成，调用task的onfailed 方法
				clearWaitDelayTimeOutMessage(baseTask);
			}
		}
	}
	
	/**
	 * 从等待队列中删除有traget标记的Task
	 * @param taskTraget
	 */
	public void removeWaitTaskByTraget(String taskTraget){
		HashSet<JHBaseTask> removeTasks=mTaskQueue.getTaskByTraget(taskTraget);
		for(JHBaseTask task:removeTasks){
			removeWaitTask(task);
		}
	}
	/**
	 * 取消task，从等待队列中删除，若在执行中，则只是标记task为canceled
	 * @param baseTask
	 */
	public void cancelTask(JHBaseTask baseTask){
		if(baseTask==null){
			throw new NullPointerException();
		}
		if(baseTask.isWaiting()){
			removeWaitTask(baseTask);
		}else if(baseTask.isRunning()){
			baseTask.cancel(false);
		}
		
	}
	/**
	 * 通过taskTrager取消task，从等待队列中删除，若在执行中，则只是标记task为canceled
	 * @param baseTask
	 */
	public void cancelTaskByTraget(String taskTraget){
		HashSet<JHBaseTask> removeTasks=mTaskQueue.getTaskByTraget(taskTraget);
		for(JHBaseTask task:removeTasks){
			if(task.isWaiting()){
				removeWaitTask(task);
			}else if(task.isRunning()){
				task.cancel(false);
			}
		}
		
	}
	/**
	 * 是否存在task（等待中，执行中的task）
	 * @param task
	 * @return false 无
	 * 		   true  有
	 */
	public boolean hasTask(JHBaseTask task){
		return mTaskQueue.contains(task);
	}
	/**
	 * 是否存在标记为traget的task（等待中，执行中的task）
	 * @param taskTraget
	 * @return false 无
	 * 		   true  有
	 */
	public boolean hasTask(String taskTraget){
		return mTaskQueue.contains(taskTraget);
	}
	/**
	 * 将task任务滞后
	 * @param task
	 */
	public void waitTask(JHBaseTask task){
		if(task!=null){
			if(mTaskQueue.contains(task)){
				task.setActive(false);
			}else{
				task.setActive(false);
				addTask(task);
			}
			
		}
	}
	/**
	 * 将task任务滞后通过Traget
	 * @param task
	 */
	public void waitTask(String taskTraget){
		if(taskTraget!=null){
			HashSet<JHBaseTask> taskSets=mTaskQueue.getTaskByTraget(taskTraget);
			for(JHBaseTask task:taskSets){
				if(task.isWaiting()){
					task.setActive(false);
				}
			}
		}
	}
	/**
	 * 重新激活task
	 * @param task
	 */
	public void reActiveTask(JHBaseTask task){
		if(task!=null){
			if(mTaskQueue.contains(task)){
				task.setActive(true);
			}else{
				task.setActive(true);
				addTask(task);
			}
			
		}
	}
	/**
	 * 重新激活taskByTraget
	 * @param task
	 */
	public void reActiveTask(String taskTraget){
		if(taskTraget!=null){
			HashSet<JHBaseTask> taskSets=mTaskQueue.getTaskByTraget(taskTraget);
			for(JHBaseTask task:taskSets){
				if(task.isWaiting()){
					task.setActive(true);
				}
			}
		}
	}
	

	/***
	 * 添加任务执行完成的listener
	 * @param linsener
	 */
	public void addTaskFinishLinsener(ITaskFinishLinsener linsener){
		
		//TODO
	}
	/**
	 * 移除任务执行完成的listener
	 * @param linsener
	 */
	public void removeTaskFinishLinsener(ITaskFinishLinsener linsener){
		
		//TODO
	}
	
	
}
