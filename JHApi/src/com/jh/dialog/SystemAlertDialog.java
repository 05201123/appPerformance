package com.jh.dialog;

import android.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;

public class SystemAlertDialog {
	private Dialog dialog;

	private void setDialog(Dialog dialog) {
		this.dialog = dialog;
	}
	public Dialog getDialog(){
		return dialog;
	}
	public boolean isShowing() {
		return dialog != null && dialog.isShowing();
	}

	/**
	 * 
	 * @param context
	 * @param title
	 * @param message
	 * @param oklistener
	 * @param canCancel
	 * @return
	 */
	public static SystemAlertDialog showSystemDialog(Context context,
			String title, String message,
			DialogInterface.OnClickListener oklistener, boolean canCancel) {
		return showSystemDialog(context, title, message, oklistener, null,
				canCancel);
	}

	public static SystemAlertDialog showSystemDialog(Context context,
			String title, String message,
			DialogInterface.OnClickListener oklistener) {
		return showSystemDialog(context, title, message, oklistener, true);
		/*
		 * SystemAlertDialog systemDialog = new SystemAlertDialog(); Builder
		 * builder = new AlertDialog.Builder(context); builder.setTitle(title);
		 * builder.setCancelable(true); builder.setMessage(message);
		 * 
		 * builder.setNegativeButton("取消", new OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * dialog.dismiss(); } }); builder.setPositiveButton("确定", oklistener);
		 * final AlertDialog dialog = builder.create();
		 * systemDialog.setDialog(dialog); //在dialog
		 * show方法之前添加如下代码，表示该dialog是一个系统的dialog**
		 * dialog.getWindow().setType((WindowManager
		 * .LayoutParams.TYPE_SYSTEM_ALERT)); dialog.show(); return
		 * systemDialog;
		 */
	}

	public static SystemAlertDialog showSystemDialog(Context context,
			String title, String message,
			DialogInterface.OnClickListener oklistener,
			DialogInterface.OnClickListener cancellistener) {
		return showSystemDialog(context, title, message, oklistener,
				cancellistener, true);
	}

	public static SystemAlertDialog showSystemDialog(Context context,
			String title, String message,
			DialogInterface.OnClickListener oklistener,
			DialogInterface.OnClickListener cancellistener, boolean canCancel) {


		SystemAlertDialog systemDialog = new SystemAlertDialog();
		final Dialog dialog = createDialog(context,title,message,oklistener,cancellistener,canCancel);
		systemDialog.setDialog(dialog);
		// 在dialog show方法之前添加如下代码，表示该dialog是一个系统的dialog**
		dialog.getWindow().setType(
				(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		return systemDialog;

	}
	public static SystemAlertDialog showSystemDialogWidthLayout(Context context,
			String title, String message,
			DialogInterface.OnClickListener oklistener,
			DialogInterface.OnClickListener cancellistener, boolean canCancel,String positiveName,String negativeName,View layout,int theme) {
		
		
		SystemAlertDialog systemDialog = new SystemAlertDialog();
		final Dialog dialog = createDialogWidthLayout(context,title,message,oklistener,cancellistener,canCancel,positiveName,negativeName,layout,theme);
		systemDialog.setDialog(dialog);
		// 在dialog show方法之前添加如下代码，表示该dialog是一个系统的dialog**
		dialog.getWindow().setType(
				(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		 Window dialogWindow = dialog.getWindow();  
		 WindowManager.LayoutParams lp = dialogWindow.getAttributes();  
		 dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);  
		 lp.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.85);
		 lp.height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.7);
		 dialogWindow.setAttributes(lp);
		return systemDialog;
		
	}
	public static Dialog createDialog(final Context context,
			String title, String message,
			DialogInterface.OnClickListener oklistener,
			DialogInterface.OnClickListener cancellistener, boolean canCancel){
		return createDialogWidthLayout(context, title, message,oklistener, cancellistener, canCancel, "", "",null,-1);
	}
	@SuppressLint("NewApi")
	public static Dialog createDialogWidthLayout(final Context context,
			String title, String message,
			DialogInterface.OnClickListener oklistener,
			DialogInterface.OnClickListener cancellistener, boolean canCancel,String positiveName,String negativeName,View layout,int theme){
		if(layout != null){
//			AlertDialog updateDialog = new AlertDialog.Builder(context,theme).create();  
			Dialog updateDialog = new Dialog(context, theme);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    		params.leftMargin = 20;
    		params.rightMargin = 20;
    		params.topMargin = 20;
    		params.bottomMargin = 20;
//			updateDialog.setView(layout,0,0,0,0);  
			updateDialog.addContentView(layout, params);
			
			if (canCancel) {
				if (cancellistener == null) {
					cancellistener = new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					};
				}
			}
			
//			updateDialog.setButton(DialogInterface.BUTTON_NEUTRAL, TextUtils.isEmpty(negativeName)?context.getString(R.string.cancel):negativeName, cancellistener == null ? null :cancellistener);  
//			updateDialog.setButton(DialogInterface.BUTTON_POSITIVE,TextUtils.isEmpty(positiveName)?context.getString(R.string.ok):positiveName, oklistener);  
			updateDialog.setCancelable(canCancel);
			updateDialog.setCanceledOnTouchOutside(false);
			return updateDialog;
		}else{
			if (canCancel) {
				if (cancellistener == null) {
					cancellistener = new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					};
				}
			}
			Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(title);
			builder.setCancelable(canCancel);
			builder.setMessage(message);
			if (canCancel) {
				builder.setNegativeButton(TextUtils.isEmpty(negativeName)?context.getString(R.string.cancel):negativeName,
						cancellistener);
			}
			
			builder.setPositiveButton(TextUtils.isEmpty(positiveName)?context.getString(R.string.ok):positiveName, oklistener);
			//builder.setOnCancelListener(cancellistener);
			final AlertDialog dialog = builder.create();
			dialog.setCanceledOnTouchOutside(false);
			return dialog;
		}
	}
}
