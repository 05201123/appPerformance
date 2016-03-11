package com.jh.app.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;


public class BaseToast {

	private static Toast toast;
	private BaseToast(){};
	private static Handler mainHandler ;
	private static String message;
	/**
	 * 获取toast
	 * @param context
	 * @param message
	 * @return
	 */
	public static Toast getInstance(Context context,String message)
	{
		if(toast == null)
		{
			toast = Toast.makeText(context.getApplicationContext()
					, message, Toast.LENGTH_SHORT);
			
			mainHandler = new Handler(Looper.getMainLooper());
		}
		else
		{
			toast.setText(message);
			BaseToast.message = message;
		}
		return toast;
	}
	public static Toast getInstance(Context context,int resId)
	{
		return getInstance(context,context.getString(resId));
	}
	public static  void show(Context context,int resId)
	{
		toast = getInstance(context,resId);
		mainHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				toast.show();
			}
		});
	}
	public static void hide()
	{
		mainHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub

				if(toast != null)
				{
					toast.cancel();
				}
			}
		});
	}
	/**
	 * 退出应用程序时调用
	 */
	public static void cancle()
	{
		mainHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(toast != null)
				{
					toast = null;
				}
			}
		});
	}
}
