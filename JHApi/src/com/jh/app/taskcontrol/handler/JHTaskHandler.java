package com.jh.app.taskcontrol.handler;


import android.os.Handler;
import android.os.Looper;

/**
 * 子线程Handler
 * @author 099
 *
 */
public class JHTaskHandler {
	
	private static HandlerThreadWrapper taskThread = new HandlerThreadWrapper("subtask");
	/**获取taskHanlder**/
	public static Handler getTaskHandler(){
		return taskThread.getHandler();
	}
	/***
	 * 获取looper
	 * @return
	 */
	public static Looper getTaskLooper(){
		return taskThread.getLooper();
	}
	private static class HandlerThreadWrapper {
        private Handler handler = null;
        private android.os.HandlerThread handlerThread;
        public HandlerThreadWrapper(String name) {
            handlerThread = new android.os.HandlerThread(name);
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper());
        }
        public Handler getHandler() {
            return handler;
        }
        public Looper getLooper(){
        	return handlerThread.getLooper();
        }
    }
}
