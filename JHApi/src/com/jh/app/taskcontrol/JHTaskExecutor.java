package com.jh.app.taskcontrol;
import com.jh.app.taskcontrol.callback.ITaskFinishLinsener;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
 /**
  * JH任务控制器
  * @author 099
  * @since 2016-3-11
  */
public class JHTaskExecutor {
	/**单例**/
	private static JHTaskExecutor excutor=new JHTaskExecutor();
	
//	private static Handler mChildThreadHandler;
//	private static final int ADD_MESSAGE=100;
//	private static final int ADD_MESSAGE_FIRST=101;
	
	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	
	private JHTaskExecutor(){
//		mChildThreadHandler=new Handler(new HandlerThread("TaskChildThread").getLooper()){
//			public void handleMessage(Message msg) {
//				switch (msg.what) {
//				
//				}
//			};
//		};
	}
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 将task添加到同Priority的队列
	 * @param baseTask
	 */
	public  void addTask(JHBaseTask baseTask){
		if(baseTask!=null){
			//TODO
		}else{
			throw new NullPointerException();
		}
	}
	/**
	 * 将task添加到同Priority的队列第一位上
	 * @param baseTask
	 */
	public  void addTaskFirst(JHBaseTask baseTask){
		if(baseTask!=null){
			//TODO
		}else{
			throw new NullPointerException();
		}
	}
	/**
	 * 从等待队列中删除task
	 * @param baseTask
	 */
	public void removeWaitTask(JHBaseTask baseTask){
		//TODO
	}
	/**
	 * 从等待队列中删除有traget标记的Task
	 * @param taskTraget
	 */
	public void removeWaitTaskByTraget(String taskTraget){
		//TODO
	}
	/**
	 * 取消task，从等待队列中删除，若在执行中，则只是标记task为canceled
	 * @param baseTask
	 */
	public void cancelTask(JHBaseTask baseTask){
		//TODO
		
	}
	/**
	 * 通过taskTrager取消task，从等待队列中删除，若在执行中，则只是标记task为canceled
	 * @param baseTask
	 */
	public void cancelTaskByTraget(String taskTraget){
		//TODO
		
	}
	/**
	 * 是否存在task（等待中，执行中的task）
	 * @param task
	 * @return false 无
	 * 		   true  有
	 */
	public boolean hasTask(JHBaseTask task){
		//TODO
		return false;
	}
	/**
	 * 是否存在标记为traget的task（等待中，执行中的task）
	 * @param taskTraget
	 * @return false 无
	 * 		   true  有
	 */
	public boolean hasTaskByTraget(String taskTraget){
		//TODO
		return false;
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
