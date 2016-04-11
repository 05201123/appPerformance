package com.jh.app.taskcontrol;

import java.io.ObjectInputStream.GetField;
import java.util.HashSet;

import junit.framework.TestCase;

/**
 * taskQueue测试类
 * @author 099
 * @since 2016-4-9
 */
public class JHTaskQueueTest extends TestCase{
	JHTaskQueue mTaskQueue;
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mTaskQueue=new JHTaskQueue();
	}
	
	/***
	 * add task to queue
	 */
	public void test1EnQueue(){
		MockTestBaseTask task1=new MockTestBaseTask();
		mTaskQueue.enqueueTask(task1, false);
		assertEquals(true, task1.isInvoked());
		assertEquals(true, mTaskQueue.getWaitTaskSize()==1);
		
		MockTestBaseTask task2=new MockTestBaseTask();
		mTaskQueue.enqueueTask(task2, false);
		assertEquals(true, task2.isInvoked());
		assertEquals(true, mTaskQueue.getWaitTaskSize()==2);
		
		MockTestBaseTask task3=new MockTestBaseTask();
		mTaskQueue.enqueueTask(task3, true);
		assertEquals(true, task3.isInvoked());
		assertEquals(true, mTaskQueue.getWaitTaskSize()==3);
		
		MockTestBaseTask task4=new MockTestBaseTask();
		mTaskQueue.enqueueTask(task4, false);
		assertEquals(true, task4.isInvoked());
		assertEquals(true, mTaskQueue.getWaitTaskSize()==4);
		
		assertEquals(true,task1.compareTo(task2)<0);
		assertEquals(true,task2.compareTo(task4)<0);
		assertEquals(true,task3.compareTo(task1)<0);
		
		assertEquals(task3,mTaskQueue.getFirstTask());
		assertEquals(task1,mTaskQueue.getFirstTask());
		assertEquals(task2,mTaskQueue.getFirstTask());
		assertEquals(task4,mTaskQueue.getFirstTask());
		
		assertEquals(true, mTaskQueue.getWaitTaskSize()==0);
	}
	/***
	 * remove 方法 
	 */
	public void test2removeEnQueue(){
		MockTestBaseTask task1=new MockTestBaseTask();
		mTaskQueue.enqueueTask(task1, false);
		assertEquals(true, mTaskQueue.contains(task1));
		assertEquals(true, mTaskQueue.removeWaitTask(task1));
		assertEquals(true, mTaskQueue.getWaitTaskSize()==0);
		
		MockTestBaseTask task2=new MockTestBaseTask();
		mTaskQueue.enqueueTask(task2, false);
		mTaskQueue.getFirstTask();
		assertEquals(false, mTaskQueue.contains(task2));
		assertEquals(true,mTaskQueue.getWaitTaskSize()==0);
		mTaskQueue.removeRunningTask(task2);
		assertEquals(true,mTaskQueue.isEmpty());
		
	}
	/**
	 * traget
	 */
	public void test3Traget(){
		MockTestBaseTask task1=new MockTestBaseTask();
		task1.setTempTraget("downLoad");
		mTaskQueue.enqueueTask(task1, false);
		MockTestBaseTask task2=new MockTestBaseTask();
		task2.setTempTraget("downLoad");
		mTaskQueue.enqueueTask(task2, false);
		MockTestBaseTask task3=new MockTestBaseTask();
		task3.setTempTraget("downLoad");
		mTaskQueue.enqueueTask(task3, false);
		MockTestBaseTask task4=new MockTestBaseTask();
		task4.setTempTraget("downLoad");
		mTaskQueue.enqueueTask(task4, false);
		MockTestBaseTask task5=new MockTestBaseTask();
		task5.setTempTraget("downLoad");
		mTaskQueue.enqueueTask(task5, false);
		MockTestBaseTask task6=new MockTestBaseTask();
		task6.setTempTraget("downLoad");
		mTaskQueue.enqueueTask(task6, false);
		MockTestBaseTask task7=new MockTestBaseTask();
		mTaskQueue.enqueueTask(task7, false);
		MockTestBaseTask task8=new MockTestBaseTask();
		task8.setTempTraget("upload");
		mTaskQueue.enqueueTask(task8, false);
		MockTestBaseTask task9=new MockTestBaseTask();
		task9.setTempTraget("upload");
		mTaskQueue.enqueueTask(task9, false);
		MockTestBaseTask task10=new MockTestBaseTask();
		task10.setTempTraget("upload");
		mTaskQueue.enqueueTask(task10, false);
		
		assertEquals(true, mTaskQueue.contains("downLoad"));
		assertEquals(false,mTaskQueue.isTragetEmpty("downLoad"));
		HashSet<JHBaseTask> set=mTaskQueue.getTaskByTraget("downLoad");
		assertEquals(6,set.size());
		
		assertEquals(task1, mTaskQueue.getFirstTask());
		assertEquals(task2, mTaskQueue.getFirstTask());
		assertEquals(task3, mTaskQueue.getFirstTask());
		assertEquals(task4, mTaskQueue.getFirstTask());
		assertEquals(task5, mTaskQueue.getFirstTask());
		assertEquals(task7, mTaskQueue.getFirstTask());
		assertEquals(task8, mTaskQueue.getFirstTask());
		mTaskQueue.removeRunningTask(task1);
		assertEquals(task6, mTaskQueue.getFirstTask());
		
	}
	
	

}
