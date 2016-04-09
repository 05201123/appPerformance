package com.jh.app.taskcontrol;

import java.io.ObjectInputStream.GetField;

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
	
	

}
