package com.jh.app.util;

import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jh.app.util.AllTaskFinish;



import android.util.Log;

public class RunnableExecutor {
    private ExecutorService executor;
    private static RunnableExecutor instance;
    private int nThreads; 
    public RunnableExecutor(int nThreads){
    	executor = Executors.newFixedThreadPool(nThreads);
    	this.nThreads = nThreads;
    }
    public static RunnableExecutor getInstance(){
        if(instance==null){
            synchronized (RunnableExecutor.class) {
                if(instance==null){
                instance = new RunnableExecutor(5);
                }
            }
        }
        return instance;
    }
    public Runnable getNextTask(){
    	synchronized(waitTasks){
    		if(waitTasks.size()>0){
    			return waitTasks.get(0);
    		}
    	}
    	return null;
    }
   /**
    * 获取一个任务执行器
    * @param nThreads
    * @return
    */
    public static RunnableExecutor newInstance(int nThreads){
    	return new RunnableExecutor(nThreads);
    }
    private volatile Vector<Runnable> waitTasks = new Vector<Runnable>();
    private Vector<Runnable> runningTasks = new Vector<Runnable>(10);

    public Vector<Runnable> getWaitTasks() {
		return waitTasks;
	}
	public Vector<Runnable> getRunningTasks() {
		return runningTasks;
	}
	public void executeTaskExclude(Runnable task){
        if(waitTasks.contains(task)||waitTasks.contains(runningTasks)){
          
        }else{
           
            executeTask(task);
        }
       
    }
   
    public boolean isTaskFull() {
        return runningTasks.size() >= 5;
    }

    public int getSize() {
        return waitTasks.size()+runningTasks.size();
    }
    public boolean hasWaitingTask(){
    	 return waitTasks.size()>0;
    }
    public void removeTask(Runnable task){
    	waitTasks.remove(task);
    	runningTasks.remove(task);
    }
    public void executeTask(Runnable task){
    	
    	waitTasks.add(task);
    	
    	if(runningTasks.size()<=nThreads){
    		executor.execute(iterateTask);
    	}
    		
    }
    public void executeTaskNoWaiting(Runnable task){
    	if(!waitTasks.contains(task))
    	{
    		executeTask(task);
    	}
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
  		if(waitTasks.size()==0&&runningTasks.size()==0&&allFinish!=null){
  			allFinish.finish();
  		}
  	}
    private Runnable iterateTask = new Runnable(){

        @Override
        public void run() {
            // TODO Auto-generated method stub
          //  Log.e("executeTask", "task"+waitTasks.size());
        	
            	Runnable currentTask = null;
                synchronized (waitTasks) {
                	
                	if(waitTasks.size()>0)
                	{
                		currentTask = waitTasks.remove(0);
                	}
				}
            	if(currentTask!=null){
            		 runningTasks.add(currentTask);
            		 if(currentTask!=null){
         				try{
         					
         					currentTask.run();
         				}
         				catch(Exception e){
         					e.printStackTrace();
         				}
         				
         				runningTasks.remove(currentTask);
         				finishExecute();
         				if(runningTasks.size()<=nThreads&&waitTasks.size()>0){
         		    		executor.execute(iterateTask);
         		    	}
         			}
            	}
               
        }};
    /*private class RunnableWrapper implements Runnable{
    	private Runnable task;
    	public Runnable getTask() {
			return task;
		}
		public void setTask(Runnable task) {
			this.task = task;
		}
		public RunnableWrapper(Runnable task){
    		this.task = task;
    	}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(task!=null){
				try{
					Log.i("RunnableWrapper", "run");
					task.run();
				}
				catch(Exception e){
					
				}
				runningTasks.remove(task);
				if(runningTasks.size()<=nThreads){
		    		executor.execute(iterateTask);
		    	}
			}
		}
    } */
    public void cancel(){
    	if(waitTasks!=null){
    		waitTasks.clear();
    	}
    	if(runningTasks!=null){
    		runningTasks.clear();
    	}
    }
}
