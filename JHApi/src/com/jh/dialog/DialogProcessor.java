package com.jh.dialog;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

@SuppressLint("NewApi")
public class DialogProcessor implements IDialog{
	private Activity activity;
	public DialogProcessor(Activity activity) {
		super();
		this.activity = activity;
	}
	private static final int progress = 11112;
	private ProgressDialog progressDialog;
	private boolean isDestory = false;
	public void destory() {
		hideLoading();
		this.isDestory = true;
	//	this.activity = null;
	}
	private String title= "",message;
	private static final String TITLE_TAG = "Title",MESSAGE_TAG = "MESSAGE",DEFAULT_DIALOG = "DEFAULT";
	private static final String CANCEL_TAG = "CANCELABLE";

	private final String LOADING = "装载中";
	private HashMap<Integer, DialogInterface.OnClickListener> onOkListeners = new HashMap<Integer, DialogInterface.OnClickListener>();
	public void showLoading(String loadingString) {
		// TODO Auto-generated method stub
		showLoading(loadingString,true);
	}
	public void hideLoading() {
		if (progressDialog!=null) {
			if(progressDialog.isShowing()&&this.activity!=null)
			{
				activity.dismissDialog(progress);
			}
		}
	}
	public Dialog onCreateDialog(int id,Bundle args) {
		// TODO Auto-generated method stub
//		return super.onCreateDialog(id, args);
		
		if(!isDestory)
		{   if(id ==progress)
			{
				progressDialog = new ProgressDialog(activity);
				//在窗口弹出的时候，把back键给屏蔽
			//	progressDialog.setCancelable(false);
				return progressDialog;
			}
			else{
				if(args!=null)
				{
					boolean defaultDialog = args.getBoolean(DEFAULT_DIALOG, false);
					if(defaultDialog){
						return SystemAlertDialog.createDialog(activity, "", "", onOkListeners.get(id), null, true);
					}
				}
				
				
			}
		}
		return null;
	}
	public void onPrepareDialog(int id, Dialog dialog, Bundle args) {
		// TODO Auto-generated method stub 
		if(id==progress&&progressDialog!=null)
		{
			if(args!=null)
			{
				progressDialog.setCancelable(args.getBoolean(CANCEL_TAG, true));
				progressDialog.setCanceledOnTouchOutside(args.getBoolean(CANCEL_TAG, true));
			}
			progressDialog.setTitle(title);
			progressDialog.setMessage(message);
		}
		else{
			if(args!=null)
			{
				boolean defaultDialog = args.getBoolean(DEFAULT_DIALOG, false);
				if (defaultDialog) {
					dialog.setTitle(args.getString(TITLE_TAG));
					((AlertDialog) dialog).setMessage(args
							.getString(MESSAGE_TAG));
					((AlertDialog) dialog)
							.setButton(
									DialogInterface.BUTTON_POSITIVE,
									"确定",
									onOkListeners.get(id));
				}
			}
			
		}
	}
	@SuppressLint("NewApi")
	public void showDialog(int dialogCode, String title, String message,
			OnClickListener oklistener) {
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
		bundle.putString(TITLE_TAG, title);
		bundle.putString(MESSAGE_TAG, message);
		bundle.putBoolean(DEFAULT_DIALOG, true);
		onOkListeners.put(dialogCode, oklistener);
		activity.showDialog(dialogCode, bundle);
	}
	@Override
	public void dismissDialog1(int id) { 
		// TODO Auto-generated method stub
		try{
			activity.dismissDialog(id);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public void showLoading(String loadingString, boolean cancelable) {
		// TODO Auto-generated method stub
		
		showLoading("请稍候",loadingString,cancelable);
	}
	@Override
	public void showLoading() {
		// TODO Auto-generated method stub
		this.showLoading(LOADING,true);
	}
	@Override
	public void showLoading(int resId) {
		// TODO Auto-generated method stub
		this.showLoading(activity.getString(resId),true);
	}
	@Override
	public void showLoading(String title, String loadingString) {
		// TODO Auto-generated method stub
		showLoading("请稍候",message,true);
	}
	@Override
	public void showLoading(String title, String loadingString,
			boolean cancelable) {
		// TODO Auto-generated method stub
		this.title = title;
		message = loadingString;
		Bundle bundle = new Bundle();
		bundle.putBoolean(CANCEL_TAG, cancelable);
		activity.showDialog(progress, bundle);
	}
	
}
