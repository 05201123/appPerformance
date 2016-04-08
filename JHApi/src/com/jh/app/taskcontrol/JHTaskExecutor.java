package com.jh.app.taskcontrol;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import android.os.SystemClock;

import com.jh.app.taskcontrol.callback.ITaskFinishLinsener;
import com.jh.app.taskcontrol.constants.TaskContants;
import com.jh.app.taskcontrol.constants.TaskContants.TaskPriority;
import com.jh.app.taskcontrol.exception.JHTaskRemoveException;
import com.jh.exception.JHException;
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

	/**任务队列*/
	private JHTaskQueue mTaskQueue;
	/**任务线程池*/
	private JHTaskThreadPool mTaskThreadPool;
	/**上一次满栈的时间**/
	private volatile long lastThredPoolFullTime;
	/**满栈一分钟前台任务可以运行**/
	private static final int TASKPOOLS_FULL_TIMEOUT=1000*60;
	 /**有特殊Target的task队列**/
	 private final Map<String,HashSet<ITaskFinishLinsener>> mTargetFinishLinseners=new HashMap<String, HashSet<ITaskFinishLinsener>>();
	
	
	private JHTaskExecutor(){
		mTaskQueue=new JHTaskQueue();
		mTaskThreadPool=new JHTaskThreadPool(corePoolSize, corePoolSize+5, null, null);
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
		if(baseTask!=null){
			if(baseTask.isInvoked()){
				throw new  RuntimeException("  multiple invoke exception");
			}
			if(mTaskQueue.enqueueTask(baseTask, isSetFirst)){
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
		JHBaseTask task=mTaskQueue.getFirstTask();
		if(task==null){
			return;
		}
		if(mTaskThreadPool.isCanExecRunnable()){
			lastThredPoolFullTime=0;
			prepare(task);
			return;
		}else{
			if(lastThredPoolFullTime!=0){
				lastThredPoolFullTime=SystemClock.elapsedRealtime();
			}
			if(mTaskThreadPool.isCanForceExecRunnable()){
				if(task.getPriority()==TaskPriority.PRIORITY_IMMEDIATE){
					prepare(task);
					return ;
				}else if(task.getPriority()==TaskPriority.PRIORITY_IMMEDIATE&&SystemClock.elapsedRealtime()-lastThredPoolFullTime>=TASKPOOLS_FULL_TIMEOUT){
					prepare(task);
					return;
				}
			}
		}
		
		mTaskQueue.reAddTaskQueue(task);
	}
	/**
	 * 执行task
	 * @param task
	 */
	private void prepare(JHBaseTask task) {
		
		if(task.getException()==null){
			task.notifyPre();
			mTaskThreadPool.executeRunnable(new WorkerRunnable(task) {
				@Override
				public void run() {
					try {
						if(mTask.getException()!=null){
							 mTask.notifyFailed();
							 return;
						}
						mTask.doTask();//耗时操作，尽量少执行
						
						if(mTask.getException()!=null){
							 mTask.notifyFailed();
							 return;
						}else{
							mTask.notifySuccess();
						}
					}catch (final JHException e) {
						e.printStackTrace();
						mTask.setException(e);
						mTask.notifyFailed();
					}
					 catch (final Exception e) {
						 e.printStackTrace();
						 mTask.setException(e);
						 mTask.notifyFailed();
					}finally{
						removeRunningTask(mTask);
						executeTask();
						
					}
				}
			});
			
			
		}else{
			task.notifyFailed();
			removeRunningTask(task);
			executeTask();
			
		}
		
		
	}
	/**
	 * 删除正在running队列中task
	 * @param currentTask
	 */
	private void removeRunningTask(JHBaseTask currentTask){
		mTaskQueue.removeRunningTask(currentTask);
		notifyTaskExeRunningListener(currentTask);
		
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
				baseTask.setException(new JHTaskRemoveException());
				baseTask.notifyFailed();
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
			mTaskQueue.removeWaitTask(baseTask);
			baseTask.cancel(false);
			baseTask.notifyFailed();
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
			cancelTask(task);
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
	/**
	 * 通知task执行监听
	 * @param currentTask
	 */
	private void notifyTaskExeRunningListener(JHBaseTask currentTask) {
		//notify allTask
		if(mTaskQueue.isEmpty()){
			synchronized (mTargetFinishLinseners) {
				HashSet<ITaskFinishLinsener> set = mTargetFinishLinseners
						.get(TaskContants.ALL_TASK);
				if(set!=null){
					for(ITaskFinishLinsener listener:set){
						listener.notifyGroupTagFinish(TaskContants.ALL_TASK);
					}
				}
			}
		}
		// notify traget task
		if(currentTask.getmTaskTraget()!=null&&mTaskQueue.isTragetEmpty(currentTask.getmTaskTraget())){
			synchronized (mTargetFinishLinseners) {
				HashSet<ITaskFinishLinsener> set = mTargetFinishLinseners
						.get(currentTask.getmTaskTraget());
				if(set!=null){
					for(ITaskFinishLinsener listener:set){
						listener.notifyGroupTagFinish(currentTask.getmTaskTraget());
					}
				}
			}
		}
		
		
	}
	/***
	 * 添加任务执行完成的listener
	 * @param linsener
	 */
	public void addTaskFinishLinsener(ITaskFinishLinsener linsener){
		addTragetTaskFinishLinsener(TaskContants.ALL_TASK,linsener);
	}
	/***
	 * 添加任务执行完成的listener
	 * @param linsener
	 */
	public void addTragetTaskFinishLinsener(String traget,ITaskFinishLinsener linsener){
		
		 if(traget==null){
			 traget=TaskContants.ALL_TASK;
		 }
		synchronized (mTargetFinishLinseners) {
			HashSet<ITaskFinishLinsener> set = mTargetFinishLinseners
					.get(traget);
			if (set == null) {
				set = new HashSet<ITaskFinishLinsener>();
				set.add(linsener);
				mTargetFinishLinseners.put(traget, set);
			} else {
				set.add(linsener);
			}
		}
		  
	}
	/**
	 * 移除任务执行完成的listener
	 * @param linsener
	 */
	public void removeTaskFinishLinsener(String traget,ITaskFinishLinsener linsener){
		if (traget == null) {
			traget = TaskContants.ALL_TASK;
		}
		synchronized (mTargetFinishLinseners) {
			HashSet<ITaskFinishLinsener> set = mTargetFinishLinseners
					.get(traget);
			if (set != null) {
				set.remove(linsener);
			}
		}
		 
		 
		 
	}
	/**
	 * 实际放入线程池的runnnable
	 * @author 099
	 * @since 2016-4-5
	 */
	private static abstract class WorkerRunnable implements Runnable{
		protected JHBaseTask mTask;
		WorkerRunnable(JHBaseTask task){
			mTask=task;
		}
	}
}
