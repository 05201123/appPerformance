package com.jh.app.taskcontrol;

import junit.framework.TestCase;
import android.os.Handler;
import android.os.Looper;

import com.jh.app.taskcontrol.JHBaseTask.ITaskCancel;
import com.jh.app.taskcontrol.JHBaseTask.TaskPriority;
import com.jh.app.taskcontrol.JHBaseTask.TaskStatus;
import com.jh.app.taskcontrol.exception.JHTaskCancelException;
import com.jh.exception.JHException;

/**
 * jhbaseTask test类
 * @author 099
 * @2016-4-7
 */
public class JHBaseTaskTest extends TestCase{
	private Handler mHandler;
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mHandler=new Handler(Looper.getMainLooper());
	}
	/***
	 *  测试CompareTo
	 *  priority越大越先执行，priority相等，先进先出
	 */
	public void test1CompareTo(){
		int sequence=0;
		MockTestBaseTask low1=new MockTestBaseTask();
		low1.setmPriority(TaskPriority.PRIORITY_DELAY);
		low1.setSequence(sequence++);
		
		MockTestBaseTask low2=new MockTestBaseTask();
		low2.setmPriority(TaskPriority.PRIORITY_DELAY);
		low2.setSequence(sequence++);
		
		MockTestBaseTask high3=new MockTestBaseTask();
		high3.setmPriority(TaskPriority.PRIORITY_FOREGROUND);
		high3.setSequence(sequence++);
		
		MockTestBaseTask immediate4=new MockTestBaseTask();
		immediate4.setmPriority(TaskPriority.PRIORITY_IMMEDIATE);
		immediate4.setSequence(sequence++);
		
		assertTrue(low1.compareTo(high3) > 0);
        assertTrue(high3.compareTo(low1) < 0);
        assertTrue(low1.compareTo(low2) < 0);
        assertTrue(low1.compareTo(immediate4) > 0);
        assertTrue(immediate4.compareTo(high3) < 0);
	}
	/**
	 * 测试单线程下task的status的方法是否正常
	 */
	public void test2TaskStatusSingleThread(){
		JHBaseTask task=new MockTestBaseTask();
		assertFalse(task.isInvoked());
		assertFalse(task.isWaiting());
		assertFalse(task.isRunning());
		assertFalse(task.isFinished());
		
		task.setTaskStatus(TaskStatus.PENDING);
		assertTrue(task.isInvoked());
		assertTrue(task.isWaiting());
		assertFalse(task.isRunning());
		assertFalse(task.isFinished());
		
		task.setTaskStatus(TaskStatus.RUNNING);
		assertTrue(task.isInvoked());
		assertFalse(task.isWaiting());
		assertTrue(task.isRunning());
		assertFalse(task.isFinished());
		
		task.setTaskStatus(TaskStatus.FINISHED);
		assertTrue(task.isInvoked());
		assertFalse(task.isWaiting());
		assertFalse(task.isRunning());
		assertTrue(task.isFinished());
	}
	
	
	
	/**
	 * 测试task cancel 是否执行正常
	 */
	public void test3TaskCancel(){
		MockTestBaseTask task=new MockTestBaseTask();
		assertEquals(false, task.isCancelled());
		task.cancel(false);
		assertEquals(true, task.isCancelled());
		assertEquals(false, task.isCancelLisExe());
		
		MockTestBaseTask task2=new MockTestBaseTask();
		task2.setCancelListener(new ITaskCancel() {
			
			@Override
			public void cancel(JHBaseTask task) {
				((MockTestBaseTask)task).setCancelLisExe(true);
			}
		});
		task2.cancel(true);
		assertEquals(true, task2.isCancelLisExe());
		assertEquals(true, task2.getException()!=null&&task2.getException() instanceof JHTaskCancelException);
	}
	
	/**
	 * 测试task等待与激活
	 */
	public void test4TaskWaitAndActive(){
		MockTestBaseTask task=new MockTestBaseTask();
		assertEquals(true, task.isActive());
		task.setActive(false);
		assertEquals(false, task.isActive());
		task.setActive(true);
		task.setNeedWait(true);
		assertEquals(false, task.isActive());
		task.setNeedWait(false);
		assertEquals(true, task.isActive());
		
		
	}
	/***
	 * 测试task内onpre消息执行 
	 */
	public void test5TaskPreMessageExe(){
		final MockTestBaseTask task=new MockTestBaseTask();
		assertEquals(false, task.isOnPreExe);
		assertEquals(true, task.getException()==null);
		task.notifyPre();
		mHandler.post(new Runnable() {//通过logcat查看那异常观看是否执行，暂时无好办法。
			@Override
			public void run() {
				assertEquals(true, task.isOnPreExe);
				assertEquals(true, task.isOnPreExeInMainUI);
				
			}
		});
		
		
		final MockTestBaseTask task2=new MockTestBaseTask();
		assertEquals(false, task2.isOnPreExe);
		assertEquals(true, task2.getException()==null);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				task2.notifyPre();
				mHandler.post(new Runnable() {//通过logcat查看那异常观看是否执行，暂时无好办法。
					@Override
					public void run() {
						assertEquals(true, task2.isOnPreExe);
						assertEquals(true, task2.isOnPreExeInMainUI);
						
					}
				});
				
			}
		}).start();
	}
	/***
	 * 测试task内onfailed消息执行 
	 */
	public void test6TaskFailedMessageExe(){
		final MockTestBaseTask task=new MockTestBaseTask();
		assertEquals(false, task.isOnFailedExe);
		assertEquals(true, task.getException()==null);
		task.setException(new JHException());
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				task.notifyFailed();
				mHandler.post(new Runnable() {//通过logcat查看那异常观看是否执行，暂时无好办法。
					@Override
					public void run() {
						assertEquals(true, task.isOnFailedExe);
						
					}
				});
				
			}
		}).start();
	}
	/***
	 * 测试task内onsuccess消息执行 
	 */
	public void test7TaskSuccessMessageExe(){
		final MockTestBaseTask task=new MockTestBaseTask();
		assertEquals(false, task.isOnSuccessExe);
		assertEquals(true, task.getException()==null);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				task.notifySuccess();
				mHandler.post(new Runnable() {//通过logcat查看那异常观看是否执行，暂时无好办法。
					@Override
					public void run() {
						assertEquals(true, task.isOnSuccessExe);
					}
				});
				
			}
		}).start();
	}
	/***
	 * 测试task内onsuccess消息执行 
	 */
	public void test8TaskUpdateMessageExe(){
		final MockTestBaseTask task=new MockTestBaseTask();
		assertEquals(true, task.getException()==null);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				task.onProgressChanged(task, 20);
				mHandler.post(new Runnable() {//通过logcat查看那异常观看是否执行，暂时无好办法。
					@Override
					public void run() {
						assertEquals(task,task.getBussinessData());
						assertEquals(20,task.getNum());
					}
				});
				
			}
		}).start();
	}
	
	
	
	         
	
	//TODO  测试多线程下task的status
	
	
	
	
	
	private class MockTestBaseTask extends JHBaseTask{
		
		private int mPriority =TaskPriority.PRIORITY_NORMAL;
		
		private boolean isCancelLisExe=false;
		
		private boolean isNeedWait=false;
		
		private boolean isOnPreExe=false;
		private boolean isOnPreExeInMainUI=false;
		
		private boolean isOnFailedExe=false;
		private boolean isOnSuccessExe=false;
		
		private Object bussinessData;
		private  int num;
		
		
		private MockTestBaseTask(){
			
		}
		
		public void setmPriority(int priority){
			mPriority=priority;
		}
		public int getPriority() {
			return mPriority;
		}
		@Override
		void notifyPre() {
			super.notifyPre();
		}
		@Override
		public void onPreExecute() {
			super.onPreExecute();
			setOnPreExe(true);
			isOnPreExeInMainUI=Looper.getMainLooper()==Looper.myLooper();
		}
		
		@Override
		public void doTask() throws JHException {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void success() {
			super.success();
			setOnSuccessExe(true);
		}
		@Override
		public void fail(String errorMessage) {
			super.fail(errorMessage);
			setOnFailedExe(true);
		}
		@Override
		public void onProgressChanged(Object bussinessData, int newProgress) {
			super.onProgressChanged(bussinessData, newProgress);
			this.setBussinessData(bussinessData);
			this.setNum(newProgress);
			
		}
		
		/**
		 * @return the isCancelLisExe
		 */
		public boolean isCancelLisExe() {
			return isCancelLisExe;
		}
		/**
		 * @param isCancelLisExe the isCancelLisExe to set
		 */
		public void setCancelLisExe(boolean isCancelLisExe) {
			this.isCancelLisExe = isCancelLisExe;
		}
		/**
		 * @return the isNeedWait
		 */
		public boolean isNeedWait() {
			return isNeedWait;
		}
		/**
		 * @param isNeedWait the isNeedWait to set
		 */
		public void setNeedWait(boolean isNeedWait) {
			this.isNeedWait = isNeedWait;
		}

		/**
		 * @return the isOnPreExe
		 */
		public boolean isOnPreExe() {
			return isOnPreExe;
		}

		/**
		 * @param isOnPreExe the isOnPreExe to set
		 */
		public void setOnPreExe(boolean isOnPreExe) {
			this.isOnPreExe = isOnPreExe;
		}

		/**
		 * @return the isOnFailedExe
		 */
		public boolean isOnFailedExe() {
			return isOnFailedExe;
		}

		/**
		 * @param isOnFailedExe the isOnFailedExe to set
		 */
		public void setOnFailedExe(boolean isOnFailedExe) {
			this.isOnFailedExe = isOnFailedExe;
		}

		/**
		 * @return the isOnSuccessExe
		 */
		public boolean isOnSuccessExe() {
			return isOnSuccessExe;
		}

		/**
		 * @param isOnSuccessExe the isOnSuccessExe to set
		 */
		public void setOnSuccessExe(boolean isOnSuccessExe) {
			this.isOnSuccessExe = isOnSuccessExe;
		}

		/**
		 * @return the isOnPreExeInMainUI
		 */
		public boolean isOnPreExeInMainUI() {
			return isOnPreExeInMainUI;
		}

		/**
		 * @param isOnPreExeInMainUI the isOnPreExeInMainUI to set
		 */
		public void setOnPreExeInMainUI(boolean isOnPreExeInMainUI) {
			this.isOnPreExeInMainUI = isOnPreExeInMainUI;
		}

		/**
		 * @return the bussinessData
		 */
		public Object getBussinessData() {
			return bussinessData;
		}

		/**
		 * @param bussinessData the bussinessData to set
		 */
		public void setBussinessData(Object bussinessData) {
			this.bussinessData = bussinessData;
		}

		/**
		 * @return the num
		 */
		public int getNum() {
			return num;
		}

		/**
		 * @param num the num to set
		 */
		public void setNum(int num) {
			this.num = num;
		}
		
	}
	
	
}
