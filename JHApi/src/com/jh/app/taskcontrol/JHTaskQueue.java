package com.jh.app.taskcontrol;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.jh.app.taskcontrol.exception.TargetTaskExeception;
/**
 * 金和task队列
 * @author 099
 * @since 2016-3-30
 */
public class JHTaskQueue {
	
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
	 
	 /**
	  * 添加task到队列中
	  * @param baseTask
	  * @param isSetBefore true 任务队列中提前，可以实现后进先出
	  * 				   false 任务先进先出
	  */
	  boolean enqueueTask(JHBaseTask baseTask,boolean isSetBefore){
		 
		  if(isSetBefore){
			  baseTask.setSequence(mSequenceGenerator.getAndIncrement());
		  }else{
			  baseTask.setSequence(mSequenceGenerator.getAndDecrement());
		  }
		  saveTargetTask(baseTask);
		 if( mWaitingTasks.add(baseTask)){
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
		 removeTargetTask(baseTask);
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
 
	
	 
	 
	 
}
