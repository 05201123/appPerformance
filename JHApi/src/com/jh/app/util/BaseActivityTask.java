package com.jh.app.util;


import android.content.Context;
/**
 * 基本的在baseactivity中运行的一补任务,在activity关闭时取消执行。
 * @author jhzhangnan1
 *
 */

public abstract class BaseActivityTask extends BaseTask{

	private BaseActivity activity;
	//设置是否在加载框消失时，任务是否停止
	private boolean cancelAtDialogDismiss = true;
	@Override
	public boolean isCancelAtDialogDismiss() {
		return cancelAtDialogDismiss;
	}
	/**
	 * 设置是否在加载框消失时，任务是否停止
	 * @param cancelAtDialogDismiss
	 */
	public void setCancelAtDialogDismiss(boolean cancelAtDialogDismiss) {
		this.cancelAtDialogDismiss = cancelAtDialogDismiss;
	}

	public BaseActivity getActivity() {
		return activity;
	}

	private String loadMes;
	//是否可以取消对话框
	private boolean cancelDialog = false;
	/**
	 * 
	 * @param activity
	 * @param loadMes
	 * @param cancelDialog 是否按返回键可以取消对话框
	 */
	public BaseActivityTask(BaseActivity activity, String loadMes,boolean cancelDialog) {
		this(activity,loadMes);
		this.cancelDialog = cancelDialog;
	}
	public BaseActivityTask(BaseActivity activity, int loadMes,boolean cancelDialog) {
		this(activity,loadMes);
		this.cancelDialog = cancelDialog;
	}
	/**
	 * 指定绑定的activity，以及异步加载时的加载框提示语
	 * @param activity
	 * @param loadMes 当传null时，不弹出加载框
	 */
	public BaseActivityTask(BaseActivity activity, String loadMes) {
		this.activity = activity;
		this.loadMes = loadMes;
	}

	public BaseActivityTask(BaseActivity activity, int loadMes) {
		this.activity = activity;
		this.loadMes = activity.getString(loadMes);
	}
	/**
	 * 主线程中运行，进行异步任务开始前的处理工作
	 */
	@Override
	public void prepareTask(Void... params) {
		// TODO Auto-generated method stub
		super.prepareTask(params);
		activity.setCurrentTask(this);
		if (loadMes != null) {
			activity.showLoading(loadMes,cancelDialog);
		}
		
	}
	/**
	 * 异步任务执行成功后调用的主线程函数
	 */
	@Override
	public void success() {
		// TODO Auto-generated method stub
		super.success();
		activity.hideLoading();
	}
	/**
	 * 异步任务调用失败后调用的主线程函数
	 */
	@Override
	public void fail(String errorMessage) {
		// TODO Auto-generated method stub
		super.fail(errorMessage);
		activity.hideLoading();
		activity.showToast(errorMessage);
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return activity;
	}
	/**
	 * 异步任务执行完，如果对应界面关闭后调用的函数
	 */
	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		super.cancel();
		activity.hideLoading();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		
		return loadMes;
	}
	
}
