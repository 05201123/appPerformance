package com.jh.persistence.db;

/*
 * @project testurl
 * @package com.urltest
 * @file SerialTask.java
 * @version  1.0
 * @author  yourname
 * @time  2012-3-26 下午1:54:08
 * CopyRight:北京金和软件信息技术有限公司 2012-3-26
 */

import com.jh.app.util.AllTaskFinish;

import android.os.Handler;
import android.os.HandlerThread;

public class SerialTask {
	/*
	 *
	 * Class Descripton goes here.
	 *
	 * @class SerialTask
	 * @version  1.0
	 * @author  yourname
	 * @time  2012-3-26 下午1:54:08
	 */
	private HandlerThread thread;
	private Handler handler;

	private AllTaskFinish allFinish;
	public SerialTask(String name){
		thread = new HandlerThread(name){};
		handler = null;
	}
	public void setTaskFinishCallBack(AllTaskFinish allFinish){
		this.allFinish = allFinish;
	}
	public void sendMessage(Runnable runnable)	
	{
		if(thread.isAlive())
		{
			
		}
		else
		{
			synchronized (this)
			{
				if(thread.isAlive())
				{
					
				}
				else
				{
					thread.start();
					handler = new Handler(thread.getLooper());
				}
			}
		}
		if (handler!=null) {
			handler.post(runnable);
		}
	}
	public void cancle()
	{
		if(handler!=null)
		{
			handler = null;
		}
		if(thread!=null)
		{
			thread.quit();
			thread = null;
		}
	}
}
