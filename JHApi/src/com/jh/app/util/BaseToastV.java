package com.jh.app.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class BaseToastV {
	private Context context;
	private Toast toast;
	private static BaseToastV instance;
	private Handler mainHandler ;
	private BaseToastV(Context context){
		this.context = context.getApplicationContext();
		mainHandler = new Handler(Looper.getMainLooper());
	//	toast = Toast.makeText(BaseToastV.instance.context, "", Toast.LENGTH_LONG);
		createToast();
	}
	private void createToast() {
		mainHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
			}
		});
	}
	private void showToast(final String message,final int length){
		mainHandler.post(new Runnable() {
			@Override
			public void run() {
				toast.setText(message);
				toast.setDuration(length);
				toast.show();
			}
		});
		
	}
	public static BaseToastV getInstance(Context context){
		if(instance!=null){
		}
		else{
				synchronized (context) {
					if(instance==null){
						instance = new BaseToastV(context);
					}
				}
			
		}
		return instance;
	}
	public void showToastLong(final String message){
		if(toast!=null)
		{
			/*toast.setText(message);
			toast.setDuration(Toast.LENGTH_LONG);
			toast.show();*/
			showToast(message,Toast.LENGTH_LONG);
		}
		else{
			createToast();
			mainHandler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					showToast(message,Toast.LENGTH_LONG);
				}
			});
		}
	}
	public void showToastShort(final String message){
		if(toast!=null)
		{
			/*toast.setText(message);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.show();*/
			showToast(message,Toast.LENGTH_SHORT);
		}else{
			createToast();
			mainHandler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					showToast(message,Toast.LENGTH_SHORT);
				}
			});
		}
	}
	public void cancel(){
		if(toast!=null)
		{
			toast.cancel();
		}
	}
}
