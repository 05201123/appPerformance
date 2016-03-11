package com.jh.dialog;

import android.R.integer;
import android.content.DialogInterface;

public interface IDialog {
	public void showLoading();
	public void showLoading(int resId);
	public void showLoading(String message);
	public void showLoading(String title,String loadingString);
	public void showLoading(String title,String loadingString,boolean cancelable);
	public void showLoading(String message,boolean cancelDialog);
	public void hideLoading();
	public void showDialog(int dialogCode,String title,String message,DialogInterface.OnClickListener oklistener);
	public void dismissDialog1(int id);
}
