package com.jh.app.taskcontrol;

import java.util.HashSet;

import android.os.SystemClock;

import com.jh.app.taskcontrol.callback.ITaskFinishLinsener;
import com.jh.exception.JHException;

import junit.framework.TestCase;

/**
 * JHTaskExecutor测试类
 * @author 099
 * @since 2016-4-9
 */
public class JHTaskExecutorTest extends TestCase{
	JHTaskExecutor executor=JHTaskExecutor.getInstance();
	boolean isDownFinished;
	boolean isAllFinished;
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		 isDownFinished=false;
		 isAllFinished=false;
	}
	
	/***
	 * add task to queue
	 */
	public void test1AddTask(){
		MockTestBaseTask task1=new MockTestBaseTask();
		task1.setDoTaskTime(200);
		executor.addTask(task1);
		assertEquals(true, task1.isRunning());
		
		MockTestBaseTask task2=new MockTestBaseTask();
		task2.setDoTaskTime(200);
		executor.addTask(task2);
		assertEquals(true, task2.isRunning());
		
		MockTestBaseTask task3=new MockTestBaseTask();
		task3.setDoTaskTime(200);
		task3.setmPriority(3);
		executor.addTask(task3);
		assertEquals(true, task3.isRunning());
		
		MockTestBaseTask task4=new MockTestBaseTask();
		task4.setDoTaskTime(200);
		executor.addTask(task4);
		assertEquals(true, task4.isRunning());
		
		assertEquals(true,task1.compareTo(task2)<0);
		assertEquals(true,task2.compareTo(task4)<0);
		assertEquals(true,task3.compareTo(task1)<0);
		
	}
	/**
	 * task
	 */
	public void test2AddTask(){
		for(int i=0;i<8;i++){
			MockTestBaseTask task1=new MockTestBaseTask();
			task1.setDoTaskTime(1000);
			executor.addTask(task1);
			assertEquals(true, task1.isRunning());
		}
		MockTestBaseTask task2=new MockTestBaseTask();
		task2.setDoTaskTime(1000);
		executor.addTask(task2);
		assertEquals(true, task2.isWaiting());
		
		for(int i=0;i<5;i++){
			MockTestBaseTask task1=new MockTestBaseTask();
			task1.setDoTaskTime(1000);
			task1.setmPriority(4);
			executor.addTask(task1);
			assertEquals(true, task1.isRunning());
		}
		MockTestBaseTask task3=new MockTestBaseTask();
		task3.setDoTaskTime(1000);
		task3.setmPriority(4);
		executor.addTask(task3);
		assertEquals(true, task3.isWaiting());
		
		SystemClock.sleep(1200);
		
		assertEquals(true, task3.isRunning());
		assertEquals(true, task2.isRunning());
	}
	/**
	 * 测试执行Listener
	 */
	public void test3ExcListener(){
		  
		executor.addTragetTaskFinishLinsener("downListener", new ITaskFinishLinsener() {
			
			@Override
			public void notifyGroupTagFinish(String taskGroupTag) {
				isDownFinished=true;
			}
		});
		executor.addTragetTaskFinishLinsener(null, new ITaskFinishLinsener() {
			
			@Override
			public void notifyGroupTagFinish(String taskGroupTag) {
				isAllFinished=true;
				
			}
		});
		for(int i=0;i<5;i++){
			MockTestBaseTask task1=new MockTestBaseTask();
			task1.setDoTaskTime(1000);
			task1.setTempTraget("downListener");
			executor.addTask(task1);
			assertEquals(true, task1.isRunning());
		}
		for(int i=0;i<5;i++){
			MockTestBaseTask task1=new MockTestBaseTask();
			task1.setDoTaskTime(1000);
			executor.addTask(task1);
		}
		SystemClock.sleep(1200);
		
		assertEquals(true, isDownFinished);
		assertEquals(false, isAllFinished);
		
		SystemClock.sleep(1000);
		assertEquals(true, isAllFinished);
	}
	/***
	 * 删除任务通过traget
	 */
	public void test4removeTaskByTraget(){
		for(int i=0;i<8;i++){
			MockTestBaseTask task1=new MockTestBaseTask();
			task1.setDoTaskTime(2000000);
			executor.addTask(task1);
			assertEquals(true, task1.isRunning());
		}
		for(int i=0;i<5;i++){
			MockTestBaseTask task1=new MockTestBaseTask();
			task1.setDoTaskTime(1000);
			task1.setTempTraget("downListener");
			executor.addTask(task1);
			assertEquals(true, task1.isWaiting());
		}
		executor.removeWaitTaskByTraget("downListener");
		assertEquals(false, executor.hasTask("downListener"));
	}
	
	

}
