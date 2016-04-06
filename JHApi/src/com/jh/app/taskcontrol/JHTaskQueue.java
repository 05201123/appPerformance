package com.jh.app.taskcontrol;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.Handler;
import android.os.Message;

import com.jh.app.taskcontrol.JHBaseTask.TaskStatus;
import com.jh.app.taskcontrol.exception.JHTaskRunningTimeOutException;
import com.jh.app.taskcontrol.exception.JHTaskWaitTimeOutException;
import com.jh.app.taskcontrol.exception.TargetTaskExeception;
import com.jh.app.taskcontrol.handler.JHTaskHandler;
/**
 * 金和task队列
 * @author 099
 * @since 2016-3-30
 */
public class JHTaskQueue {
	/**任务的等待超时msg**/
	private static final int MSG_TASK_WAIT_TIMEOUT=100;
	/**任务的执行超时msg**/
	private static final int MSG_TASK_RUNNING_TIMEOUT=101;
	/**同一个traget task 同时running最大为5*/
	private static final int TragetMaxRunningNum = 5;
	/**子线程Handler**/
	private Handler mChildThreadHandler;
	  /**顺序生成器*/
	 private AtomicInteger mSequenceGenerator = new AtomicInteger();
	 /**等待执行的task队列**/
	 private final PriorityBlockingQueue<JHBaseTask> mWaitingTasks =
		        new PriorityBlockingQueue<JHBaseTask>();
	 /**当前正在执行的tasks*/
	 private final Set<JHBaseTask> mCurrentRunningTasks = new HashSet<JHBaseTask>();
	 /**临时缓存队列**/
	 private final Set<JHBaseTask> mTempRunningTasks=new HashSet<JHBaseTask>();
	 /**有特殊Target的task队列**/
	 private final Map<String,HashSet<JHBaseTask>> mTargetTasks=new HashMap<String, HashSet<JHBaseTask>>();
	 
	 public JHTaskQueue(){
		 mChildThreadHandler=new Handler(JHTaskHandler.getTaskLooper()){
				public void handleMessage(Message msg) {
					JHBaseTask task=(JHBaseTask) msg.obj;
					if(task==null){
						return;
					}
					switch (msg.what) {
						case MSG_TASK_WAIT_TIMEOUT:
							if(task.isWaiting()){
								mWaitingTasks.remove(task);
								removeTargetTask(task);
								task.setTaskStatus(TaskStatus.FINISHED);
								task.setException(new JHTaskWaitTimeOutException());
								task.notifyFailed();
							}
							break;
						case MSG_TASK_RUNNING_TIMEOUT:
							if(task.isRunning()){
								mCurrentRunningTasks.remove(task);
								removeTargetTask(task);
								task.setTaskStatus(TaskStatus.FINISHED);
								task.setException(new JHTaskRunningTimeOutException());
								task.notifyFailed();
							}
							break;
					}
				};
			};
	 }
	 
	 /**
	  * 添加task到队列中
	  * @param baseTask
	  * @param isSetBefore true 任务队列中提前，可以实现后进先出
	  * 				   false 任务先进先出
	  */
	  boolean enqueueTask(JHBaseTask baseTask,boolean isSetBefore){
		  baseTask.setTaskStatus(TaskStatus.PENDING);
		  if(isSetBefore){
			  baseTask.setSequence(mSequenceGenerator.getAndIncrement());
		  }else{
			  baseTask.setSequence(mSequenceGenerator.getAndDecrement());
		  }
		 if( mWaitingTasks.add(baseTask)){
			 sendWaitDelayTimeOutMessage(baseTask);
			 saveTargetTask(baseTask);
			 return true;
		 }
		 return false;
	 }
	  /**
	   * 将有target标记的task缓存起来
	   * @param baseTask
	   */
	  private void saveTargetTask(JHBaseTask baseTask){
		  if(baseTask.getmTaskTraget()!=null){
			  synchronized(mTargetTasks){
				  String traget=baseTask.getmTaskTraget();
				  HashSet<JHBaseTask> set=mTargetTasks.get(traget);
				  if(set==null){
					  set=new HashSet<JHBaseTask>();
					  set.add(baseTask);
					  mTargetTasks.put(traget, set);
				  }else{
					  set.add(baseTask);
				  }
			  }
		  }
	  }
	
	 /**
	  * 删除队列中task
	  * @param baseTask
	  */
	  boolean removeWaitTask(JHBaseTask baseTask){
		 baseTask.setTaskStatus(TaskStatus.FINISHED);
		 removeTargetTask(baseTask);
		 clearWaitDelayTimeOutMessage(baseTask);
		 return mWaitingTasks.remove(baseTask);
	 }
	  /**
	   * 将有target标记的task缓存起来
	   * @param baseTask
	   */
	  private void removeTargetTask(JHBaseTask baseTask){
		  
		  if(baseTask.getmTaskTraget()!=null){
			  synchronized(mTargetTasks){
				  String traget=baseTask.getmTaskTraget();
				  HashSet<JHBaseTask> set=mTargetTasks.get(traget);
				  if(set!=null){
					  set.remove(baseTask);
				  }else{
					  throw new TargetTaskExeception();
				  }
			  }
		  }
	  }
	 HashSet<JHBaseTask> getTaskByTraget(String taskTraget) {
		 if(taskTraget==null){
			 return null;
		 }
		return mTargetTasks.get(taskTraget);
	}
	 /**
	  * 是否存在某个task
	  * @param task
	  * @return
	  */
	 boolean contains(JHBaseTask task) {
		return mWaitingTasks.contains(task);
	}
	 /**
	  * 是否存在标记为traget的task
	  * @param taskTraget
	  * @return
	  */
	 boolean contains(String taskTraget) {
		return mTargetTasks.get(taskTraget)!=null;
	}
	 /**
	  * 获取等待队列中的最前面的task
	  * @return
	  */
	 JHBaseTask getFirstTask() {
		 JHBaseTask task= mWaitingTasks.poll();
		 if(task!=null){
			 if(task.isActive()||!isTragetOutOf(task)){
				 mWaitingTasks.addAll(mTempRunningTasks);
				 mTempRunningTasks.clear();
				 putTaskToRunningQueue(task);
				 sendRunningDelayTimeOutMessage(task);
				return  task;
			 }else{
				 mTempRunningTasks.add(task);
				 return getFirstTask();
			 }
		 }
		return null;
	}
	 /**
	  * 是否超过同一时间运行限制
	  * @param task
	  * @return
	  */
	 private boolean isTragetOutOf(JHBaseTask task) {
		if(task.getmTaskTraget()!=null){
			Set<JHBaseTask> set=getTaskByTraget(task.getmTaskTraget());
			if(set!=null){
				int tempNum=0;
				for(JHBaseTask mtask:set){
					if(mtask.isRunning()){
						tempNum++;
					}
				}
				if(tempNum>=TragetMaxRunningNum){
					return true;
				}
			}
		}
		return false;
	}

	/**
	  * 重新加入的任务队列
	  * @param task
	  */
	 void reAddTaskQueue(JHBaseTask task) {
		 if(task==null){
				throw new NullPointerException();
			}
		 task.setTaskStatus(TaskStatus.PENDING);
		 clearRunningDelayTimeOutMessage(task);
		 sendWaitDelayTimeOutMessage(task);
		 mWaitingTasks.add(task);
		
	}
	 /**
	  * 将task加入到Running队列
	  * @param task
	  */
	private void putTaskToRunningQueue(JHBaseTask task){
		if(task==null){
			throw new NullPointerException();
		}
		task.setTaskStatus(TaskStatus.RUNNING);
		mCurrentRunningTasks.add(task);
	}
	/**
	 * 将task从Running队列 去掉
	 * @param mTask
	 */
	 void removeRunningTask(JHBaseTask task) {
		if(task==null){
			throw new NullPointerException();
		}
		task.setTaskStatus(TaskStatus.FINISHED);
		mCurrentRunningTasks.remove(task);
		removeTargetTask(task);
		clearRunningDelayTimeOutMessage(task);
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
				mChildThreadHandler.sendMessageDelayed(msg, baseTask.getTaskWaitTimeOut());
			}
		}
		/**
		 * 删除task等待队列中超时的task
		 * @param baseTask
		 */
		private void clearWaitDelayTimeOutMessage(JHBaseTask baseTask) {
			mChildThreadHandler.removeMessages(MSG_TASK_WAIT_TIMEOUT, baseTask);
		}
		/***
		 * 发送task等待队列中超时的task
		 * @param baseTask
		 */
		private void sendRunningDelayTimeOutMessage(JHBaseTask baseTask) {
			if(baseTask.getTaskRunningTimeOut()>0){
				Message msg=Message.obtain();
				msg.what=MSG_TASK_RUNNING_TIMEOUT;
				msg.obj=baseTask;
				mChildThreadHandler.sendMessageDelayed(msg, baseTask.getTaskRunningTimeOut());
			}
		}
		/**
		 * 删除task等待队列中超时的task
		 * @param baseTask
		 */
		private void clearRunningDelayTimeOutMessage(JHBaseTask baseTask) {
			mChildThreadHandler.removeMessages(MSG_TASK_RUNNING_TIMEOUT, baseTask);
		}
		/**
		 * 队列是否为空
		 * @return
		 */
		 boolean isEmpty() {
			return (mCurrentRunningTasks.size()+mTempRunningTasks.size()+mWaitingTasks.size())==0;
		}
		 /**
		  * traget标记的队列是否执行完
		  * @return
		  */
		 boolean isTragetEmpty(String traget) {
			Set<JHBaseTask> set= mTargetTasks.get(traget);
			if(set==null||set.size()==0){
				return true;
			}
			return false;
		} 
	 
}
