package com.jh.app.taskcontrol;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


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
	 private final Map<String,Set<JHBaseTask>> mTargetTasks=new HashMap<String, Set<JHBaseTask>>();
	 
	 /**
	  * 添加task到队列中
	  * @param baseTask
	  */
	  void addTask(JHBaseTask baseTask){
		  //TODO
		
	 }
	  /**
	   * 将task设置超前的顺序
	   * @param baseTask
	   */
	  void addTaskSetBefore(JHBaseTask baseTask){
		  //TODO
	  }
	  
	 /**
	  * 删除队列中task
	  * @param baseTask
	  */
	  void removeWaitTask(JHBaseTask baseTask){
		 //TODO
	 }
	
	  /**
	   * 从等待队列中删除有traget标记的Task
	   * @param taskTraget
	   */
	 void removeWaitTaskByTraget(String taskTraget){
		 //TODO
	}
	 
	 
	 
	 
	 
	 
	 
	 
	
	 
	 
	 
}
