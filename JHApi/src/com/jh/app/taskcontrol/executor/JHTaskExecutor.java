package com.jh.app.taskcontrol.executor;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.jh.app.taskcontrol.task.JHBaseTask;
 /**
  * JH任务控制器
  * @author 099
  * @since 2016-3-11
  */
public class JHTaskExecutor {
	/**单例**/
	private static JHTaskExecutor excutor=new JHTaskExecutor();
	private static Handler mChildThreadHandler;
	private static final int ADD_MESSAGE=100;
	private static final int ADD_MESSAGE_FIRST=101;
	
	private JHTaskExecutor(){
		mChildThreadHandler=new Handler(new HandlerThread("TaskChildThread").getLooper()){
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case ADD_MESSAGE:
					excutor.addTask((JHBaseTask)msg.obj);
					break;
				
				case ADD_MESSAGE_FIRST:
					excutor.addTaskFirst((JHBaseTask)msg.obj);
					break;
				}
			};
		};
	}
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 将task添加到同Priority的队列
	 * @param baseTask
	 */
	public  static void addTask(JHBaseTask baseTask){
		if(baseTask!=null){
			mChildThreadHandler.obtainMessage(ADD_MESSAGE, baseTask);
		}else{
			throw new NullPointerException();
		}
	}
	/**
	 * 将task添加到同Priority的队列第一位上
	 * @param baseTask
	 */
	public  static void addTaskFirst(JHBaseTask baseTask){
		if(baseTask!=null){
			mChildThreadHandler.obtainMessage(ADD_MESSAGE_FIRST, baseTask);
		}else{
			throw new NullPointerException();
		}
	}
	
	
	
}
