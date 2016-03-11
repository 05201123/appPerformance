package com.jh.app.util;



import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.jh.app.util.BaseTask;
import com.jh.exception.JHException;
/**
 * 串行执行任务
 * @author jhzhangnan1
 *
 */
public class ConcurrenceExcutorForSpecialTaskList {

	protected Handler mainHandler ;
	protected Vector<BaseTask> waitTasks,tmptasks,processTasks;
//	protected BaseTask currentTask;
	private int maxThreads = 10;
	private ExecutorService executorService; 
	
	private ConcurrenceExcutorForSpecialTaskList(){
		this(10);
	}
	public static ConcurrenceExcutorForSpecialTaskList getInstance(){
		return excutor;
	} 
	private static ConcurrenceExcutorForSpecialTaskList excutor = new ConcurrenceExcutorForSpecialTaskList();
	/**
	 *ֹ
	 */
/*	private void cancelTaskAtDialogDismiss(){
		if(currentTask!=null&&currentTask.isCancelAtDialogDismiss())
		{
			cancelCurrentTask();
		}
	}
	private void cancelCurrentTask(){
		removeTask(currentTask);
	}*/
	public ConcurrenceExcutorForSpecialTaskList(int maxThreadCount)
	{
		this.maxThreads = maxThreadCount;
		mainHandler = new Handler(Looper.getMainLooper());
		waitTasks = new Vector<BaseTask>();
		tmptasks = new Vector<BaseTask>();
		processTasks = new Vector<BaseTask>();
		executorService = Executors.newFixedThreadPool(maxThreads); 
		
	}
	public void cancelTask(BaseTask task){
		if(task!=null)
		{
			waitTasks.remove(task);
			processTasks.remove(task);
			finishExecute();
			task.cancelTask();
		}
	}
	public void addTaskFirst(BaseTask task)
	{
		waitTasks.add(0,task);
		start();
	}
	public void addTask(BaseTask task)
	{
		waitTasks.add(task);
		start();
	}
	
	public void removeTask(BaseTask task){
		if (waitTasks!=null && waitTasks.contains(task)) {
			waitTasks.remove(task);
		}
		/*if (processTasks!=null && processTasks.contains(task)) {
			processTasks.remove(task);
		}*/
	}
	
	
	public void appendTask(BaseTask task)
	{
		waitTasks.add(task);
		start();
	}
	public void start()
	{
		iterateTask();
	}
	/**
	 * 是否存在任务
	 * @param task
	 * @return
	 */
	public boolean hasTask(BaseTask task) {
		synchronized (waitTasks) {
			if (waitTasks != null) {
				// return allTasks.contains(task);
				if (waitTasks.contains(task))
					return true;
				else if (processTasks != null && processTasks.contains(task)) {
					return true;
				}
				return false;

			} else {
				if (processTasks != null && processTasks.contains(task)) {
					return true;
				}
				return false;
			}
		}

		// return allTasks.contains(task)||processTasks.contains(task);
	}
	/**
	 * 如果任务不在执行时，则添加到任务中，否则不做任何处理
	 * @param task
	 */
	public void executeTaskIfNotExist(BaseTask task){
		try {
			if(!hasTask(task)){
				appendTask(task);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 如果任务正在执行，则先取消任务，然后再加入到任务队列中。
	 * @param task
	 */
	public void cancelBeforeExecute(BaseTask task){
		if(task!=null){
			cancelTask(task);
		}
	}
	static abstract class BaseRunnable implements Runnable
	{
		public BaseTask currentTask;
		public BaseRunnable(BaseTask currentTask)
		{
			this.currentTask = currentTask;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
	}
	protected  void iterateTask() {
			try {
				if(waitTasks != null&&waitTasks.size()>0)
				{
					if (waitTasks.size()>0&&processTasks.size()<maxThreads) {
						System.out.println("waitTasks.size:"+waitTasks.size()+",processTasks.size()<maxThreads,"+"processTasks.size():"+processTasks.size()+",maxThreads:"+maxThreads);
					}else {
						System.out.println("waitTasks.size:"+waitTasks.size()+",processTasks.size()<maxThreads,"+"processTasks.size():"+processTasks.size()+",maxThreads:"+maxThreads);
					}
					
					while(waitTasks.size()>0)
					{
						BaseTask currentTask;						
						if(processTasks.size()>=maxThreads){
								break;
						}
						synchronized(this)
						{		
							if(processTasks.size()>=maxThreads){
								break;
							}
							currentTask = waitTasks.remove(0);
							if(currentTask!=null)
							processTasks.add(currentTask);
						}

						if(currentTask!=null)
						{
							//processTasks.add(currentTask);
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
											cancelTask(currentTask);
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
/*			executorService.submit(new Runnable() {
						private BaseTask currentTask;
						@Override
						public void run() {
							// TODO Auto-generated method stub
					//		Looper.prepare();
							if(tasks != null&&tasks.size()>0)
							{
								currentTask = null;
								while(tasks.size()>0)
								{
									currentTask = tasks.remove(0);
									if(currentTask!=null&&!currentTask.isCancel())
									{
										mainHandler.post(new BaseRunnable(currentTask) {
											
											@Override
											public void run() {
												// TODO Auto-generated method stub
												if(currentTask!=null)
												{
													if(!currentTask.isCancel())
														excute(currentTask);
													else
														currentTask.cancel();
												}
												else
												{
													iterateTask();
												}
											}
										});
									}
								}
							}
						}
					});*/
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	void prepare(BaseTask currentTask) {
		if(currentTask!=null)
		{
	//		ConcurrenceExcutor.this.currentTask = currentTask;
			if(!currentTask.isCancel())
			{
				currentTask.prepareTask();
				executorService.submit(new BaseRunnable(currentTask) {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						super.run();
						try {
							doInBackground(currentTask);
						} catch (final JHException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							taskFailed(currentTask, e);
						}
						 catch (final Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								taskFailed(currentTask, new JHException(e));
						}
					}

				});
			}
			else
			{
				cancelTask(currentTask);
			}
		}
	}
	private void doInBackground(BaseTask currentTask)
			throws JHException {
		if(!currentTask.isCancel())
		{
		//	currentTask.setSuccessFlag(true);
		//	Log.e("doTask", "doTask");
			currentTask.doTask();
			System.out.println(currentTask.hashCode()+"做任务");
			if(!currentTask.hasFinish()){
				currentTask.setSuccessFlag(true);
			}
			if(currentTask.isSuccess()){
			//	Log.e("doTask", "isSuccess");
				mainHandler.post(new BaseRunnable(currentTask){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						super.run();
						if(currentTask!=null)
						{
							if(!currentTask.isCancel())
							{
								currentTask.success();
								processTasks.remove(currentTask);
								finishExecute();
							}
							else
								cancelTask(currentTask);
						}
				//		ConcurrenceExcutor.this.currentTask = null;
						iterateTask();
					}});
			}
			else{
			//	Log.e("doTask", "failed");
				if(currentTask.getException()!=null){
					taskFailed(currentTask, currentTask.getException());
				}
				else{
					taskFailed(currentTask,new JHException(currentTask.getErrorMessage()));
				}
			}
		}
		else
		{
			mainHandler.post(new BaseRunnable(currentTask){
				@Override
				public void run() {
					cancelTask(currentTask);
				}
			});
			iterateTask();
		}
	}

	private void taskFailed(BaseTask currentTask, final JHException e) {
		currentTask.setSuccessFlag(false);
		currentTask.setException(e);
		mainHandler.post(new BaseRunnable(currentTask) {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				if (currentTask != null) {
					if (!currentTask.isCancel()) {
						currentTask.fail(e);
						processTasks.remove(currentTask);
						finishExecute();
					} else
						cancelTask(currentTask);
				}
				// ConcurrenceExcutor.this.currentTask = null;
				iterateTask();
			}

		});
	}
	//所有任务执行完成后执行的操作
	
	private AllTaskFinish allFinish;
	/**
	 * 设置所有任务完成后的回调
	 * @param allFinish
	 */
	public void setTaskFinishCallBack(AllTaskFinish allFinish){
		this.allFinish = allFinish;
	}
	private void finishExecute(){
		if(waitTasks.size()==0&&processTasks.size()==0&&allFinish!=null){
			allFinish.finish();
		}
	}
	public void exit()
	{
		if(waitTasks!=null)
		{
			tmptasks.clear();
			for(BaseTask task:waitTasks)
				task.cancelTask();
			waitTasks.clear();
			processTasks.clear();
			tmptasks = null;
			waitTasks = null;
		}
		
	}
	public void clearall()
	{
		if(waitTasks!=null)
		{
			for(BaseTask task:waitTasks)
				task.cancelTask();
			waitTasks.clear();
			processTasks.clear();
		}
		
	}
	public void exit(Context context)
	{
		if(waitTasks!=null)
		{
			tmptasks.clear();
			for(BaseTask task:waitTasks)
			{
				if(task!=null)
				{
					if(task.getContext()==null||task.getContext()!=context)
						tmptasks.add(task);
					else
						task.cancelTask();
				}
			}
			waitTasks.clear();
			waitTasks.addAll(tmptasks);
		}
	}

}
