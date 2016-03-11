package com.jh.app.util;

import java.util.HashMap;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jh.dialog.DialogProcessor;
import com.jh.dialog.IDialog;
import com.jh.exception.JHException;
import com.jh.net.RemoteTask;
import com.jh.util.LogUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.WindowManager.BadTokenException;
import android.widget.Toast;

/**
 * <code>BaseActivity</code>
 * @description: TODO(activity类，提供执行异步线程、显示toast以及加载框的方法) 
 * @version  1.0
 * @author  yourname
 * @since 2011-8-17
 */
public abstract class BaseActivity extends Activity implements IRemoteRequest,IDialog{
	/** 
	 * BaseActivity progressDialog : TODO(进度条)    
	 * @since  2011-8-31 张楠
	 */
	/** 
	 * BaseActivity progressDialog : TODO(进度条)    
	 * @since  2011-8-31 张楠
	 */
	private ProgressDialog progressDialog;
	private int maxThread = 10;
	public void setMaxThread(int maxThread) {
		this.maxThread = maxThread;
	}
	//显示加载框
	private DialogProcessor dialogProcessor;
	@Override
	public void showLoading() {
		// TODO Auto-generated method stub
		dialogProcessor.showLoading();
	}
	@Override
	public void showLoading(int resId) {
		// TODO Auto-generated method stub
		dialogProcessor.showLoading(resId);
	}
	@Override
	public void showLoading(String message) {
		// TODO Auto-generated method stub
		dialogProcessor.showLoading(message);
	}
	@Override
	public void showLoading(String title, String loadingString) {
		// TODO Auto-generated method stub
		dialogProcessor.showLoading(title, loadingString);
	}
	@Override
	public void showLoading(String message, boolean cancelDialog) {
		// TODO Auto-generated method stub
		dialogProcessor.showLoading(message, cancelDialog);
	}
	@Override
	public void hideLoading() {
		// TODO Auto-generated method stub
		dialogProcessor.hideLoading();
	}
	@Override
	public void showDialog(int dialogCode, String title, String message,
			OnClickListener oklistener) {
		// TODO Auto-generated method stub
		dialogProcessor.showDialog(dialogCode, title, message, oklistener);
	}
	@Override
	public void dismissDialog1(int id) {
		// TODO Auto-generated method stub
		dialogProcessor.dismissDialog1(id);
	}
	public void showDialog1 (int id) {
		if(!isDestory()){
			try{
				showDialog(id);
			}
			catch(BadTokenException e){
				
			}
		}
	}
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public  boolean showDialog1 (int id, Bundle args)  {
		if(!isDestory()){
			try{
				return showDialog(id,args);
			}
			catch(BadTokenException e){
				
			}
		}
		return false;
	}
	/**
	 * 当前显示进度条的任务
	 */
	private BaseTask currentTask;
	public BaseTask getCurrentTask() {
		return currentTask;
	}
	public void setCurrentTask(BaseTask currentTask) {
		this.currentTask = currentTask;
	}
	/** 
	 * BaseActivity toast : TODO(提示条)    
	 * @since  2011-8-31 张楠
	 */
	private Toast toast;
	//点击back按钮时是否关闭任务。
	private boolean cancelTask = true;
	public boolean isCancelTask() {
		return cancelTask;
	}
	public void setCancelTask(boolean cancelTask) {
		this.cancelTask = cancelTask;
	}
	//预留字段区间，低于该值的dialog都是预留字段
	private final static int RETAIN_NUM = 100;
	//显示单选对话框
	private final static int SHOW_SINGLESELECT = 1;
	//显示多选对话框
	private final static int SHOW_MULTIPLESELECT = 2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	dialogProcessor = new DialogProcessor(this);
    /*	if(cancelTask)
    	{
    		setBackPressListener(defaultListener);
    	}*/
    }
    private DialogInterface.OnClickListener singleSelect;
    private String[] items;
    public void showSingleSelectDialog(String title,List<String> items,DialogInterface.OnClickListener singleSelect){
    	this.showDialog(SHOW_SINGLESELECT);
    	if(items!=null)
    		this.items = (String[])items.toArray();
    	this.singleSelect = singleSelect;
    	this.title = title;
    }
    /**
     * <code>init</code>
     * @description: 初始化控件值
     * @since   2011-8-17    yourname
     */
    public void init(){};
    /**
     * 显示加载框
     
    public void showLoading()
    {
    	this.showLoading(LOADING,true);
    }*/
    private LogUtil log;
    private synchronized void initLog(){
    	if(log==null){
    		log = LogUtil.newInstance(this, this.getClass().getName());
    	}
    }
    public void logInfo(String message){
    	initLog();
    	log.info(message);
    }
    public void logWarn(String message){
    	initLog();
    	log.warn(message);
    }
    public void logError(String message){
    	initLog();
    	log.error(message);
    }
    /**
     *  显示加载框
     * @param resId string对应的资源属性
     
    public void showLoading(int resId)
    {
    	this.showLoading(this.getString(resId),true);
    }*/
	/**
	 * <code>showLoading</code>
	 * @description: TODO(显示加载框) 
	 * @param message 对话框正文
	 * @since   2011-8-17    张楠
	 
	public void showLoading(String message,boolean cancelDialog)
	{
		showLoading("请稍候",message,cancelDialog);
	}*/
	/**
	 * <code>showLoading</code>
	 * @description: TODO(显示加载框) 
	 * @param message 对话框正文
	 * @since   2011-8-17    张楠
	 
	public void showLoading(String message)
	{
		showLoading("请稍候",message,true);
	}*/
	private static final String CancelDialog = "cancelDialog";
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public void showLoading(String title,String message,boolean cancelDialog){
		dialogProcessor.showLoading(title,message, cancelDialog);
	}
	private String title,message;
	/**
	 * <code>showLoading</code>
	 * @description: TODO(显示加载框) 
	 * @param title 对话框标题
	 * @param message 对话框正文
	 * @since   2011-8-17    张楠
	 
	public void showLoading(String title,String message)
	{
		showLoading(title, message, true);
	}*/
	/**
	 * <code>hideLoading</code>
	 * @description: TODO(关闭加载提示框) 
	 * @since   Jun 9, 2011    zhangzhanqiang
	 
	public void hideLoading()
	{
		if (progressDialog!=null) {
			if(progressDialog.isShowing())
			{
				this.dismissDialog(progress);
			}
		}
	}*/
	public void showToast(int resId)
	{
		this.showToast(this.getString(resId));
	}
	/**
	 * <code>showToast</code>
	 * @description: TODO(这里用一句话描述这个方法的作用) 
	 * @param message 提示信息
	 * @since   2011-8-17    张楠
	 */
	public void showToast(String message)
	{
		toast = BaseToast.getInstance(BaseActivity.this,message);
		toast.show();
	}
	/**
	 * <code>showToast</code>
	 * @description: TODO(关闭提示) 
	 * @since   2011-8-17    张楠
	 */
	public void hideToast()
	{
		if(toast!=null)
		{
			toast.cancel();
		}
	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		isDestory = true;
		dialogProcessor.destory();
		super.finish();
	}
	private boolean isDestory = false;
	public boolean isDestory() {
		return isDestory;
	}
	public void setDestory(boolean isDestory) {
		this.isDestory = isDestory;
	}
	@Override
	protected void onDestroy() {
		isDestory = true;
		dialogProcessor.destory();
		if(excutor!=null)
		{
			excutor.exit(this);
		}
		super.onDestroy();
	}

	@Override
	protected Dialog onCreateDialog(int id,Bundle bundle) {
		// TODO Auto-generated method stub
//		return super.onCreateDialog(id, args);
		
		if(!isDestory)
		{
			/*if(id ==progress)
			{
				progressDialog = new ProgressDialog(this);
				progressDialog.setOnCancelListener(new OnCancelListener() {
					
					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						if(excutor!=null&&currentTask!=null&&currentTask.isCancelAtDialogDismiss())
						{
							excutor.cancelTask(currentTask);
						}
					}
				});
				
				//在窗口弹出的时候，把back键给屏蔽
			//	progressDialog.setCancelable(false);
				Dialog dialog = dialogProcessor.onCreateDialog(id,bundle);
				if(dialog!=null){
					return dialog;
				}
			}*/
			Dialog dialog = dialogProcessor.onCreateDialog(id,bundle);
			if(dialog!=null){
				return dialog;
			}
			if( id == SHOW_SINGLESELECT)
			{
				return new AlertDialog.Builder(this).
						setTitle(title).
						setItems(items, singleSelect).create();
			}
			else
			{
				BaseToast.getInstance(this, "不能建立Id小于"+RETAIN_NUM+"的对话框");
			}
		}
		return super.onCreateDialog(id);
	}
	public interface ShowDialog
	{
		public Dialog onCreateDialog(int id);
	}
	private ShowDialog showDialog;
	public void setShowDialog(ShowDialog showDialog)
	{
		this.showDialog = showDialog;
	}
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
		// TODO Auto-generated method stub
		super.onPrepareDialog(id, dialog, args);
		dialogProcessor.onPrepareDialog(id,dialog,args);
		/*if(id==progress&&progressDialog!=null)
		{
			progressDialog.setTitle(title);
			progressDialog.setMessage(message);
			if(args!=null){
				boolean cancelDialog =  args.getBoolean(CancelDialog,false);
				progressDialog.setCancelable(cancelDialog);
				progressDialog.setCanceledOnTouchOutside(cancelDialog);
			}
		}*/
	}
	/**
	 * 执行异步任务
	 * @param task
	 */
	public void excuteTask(BaseTask task)
	{
		if(excutor==null)
		{
			excutor = new ConcurrenceExcutor(maxThread);
		}
		excutor.addTaskFirst(task);
	//	excutor.start();
	}
	public void executeTaskIfNotExist(BaseTask task)
	{	if(excutor==null)
		{
			excutor = new ConcurrenceExcutor(maxThread);
		}
		excutor.executeTaskIfNotExist(task);
	}
	public void removeTask(BaseTask baseTask){
		if(excutor==null)
		{
			return;
		}
		excutor.removeTask(baseTask);
	}
	/**
	 * 应用程序关闭时，调用停止所有的异步任务
	 
	public static void quit()
	{
		if(excutor!=null)
		{
			excutor.exit();
			excutor=null;
		}
	}*/
	/**
	 * 清除所有的待执行异步任务
	 
	public static void clearAllTask()
	{
		if(excutor!=null)
		{
			excutor.clearall();
		}
	}*/
	/**
	 * 停止与该上下文关联的所有异步任务
	 
	private static void exit(Context context)
	{
		if(excutor!=null&&context!=null)
		{
			excutor.exit(context);
		}
	}*/
/*	private static class BaseRunnable implements Runnable
	{
		public BaseTask currentTask;
		public BaseRunnable(BaseTask currentTask)
		{
			this.currentTask = currentTask;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
	}*/
	private  ConcurrenceExcutor excutor;
	/**
	 * 串行执行任务
	 * @param task
	 * @return 返回值表示任务是否在执行。
	 */
	public synchronized boolean executeExclude(BaseTask task){
		if(excutor!=null&&excutor.hasTask(task)){
			return false;
		}
		else{
			excuteTask(task);
			return true;
		}
	}
/*	private static class BaseExcutor{
		private Handler mainHandler ;
		private List<BaseTask> tasks,tmptasks;
		private BaseTask currentTask;
		ExecutorService executorService = Executors.newFixedThreadPool(10); 
		*//**
		 * 当加载框消失时任务是否停止
		 *//*
		public void cancelTaskAtDialogDismiss(){
			if(currentTask!=null&&currentTask.isCancelAtDialogDismiss())
			{
				currentTask.cancelTask();
			}
		}
		public void cancelCurrentTask(){
			if(currentTask!=null)
			{
				currentTask.cancelTask();
			}
		}
		public BaseExcutor(String name)
		{
			mainHandler = new Handler(Looper.getMainLooper());
			tasks = new LinkedList<BaseTask>();
			tmptasks = new LinkedList<BaseTask>();
		}
		public void addTask(BaseTask task)
		{
			tasks.add(0,task);
			start();
		}
		public void start()
		{
			iterateTask();
		}
		private void iterateTask() {
				if(tasks != null&&tasks.size()>0)
				{
					executorService.submit(new Runnable() {
						private BaseTask currentTask;
						@Override
						public void run() {
							// TODO Auto-generated method stub
					//		Looper.prepare();
							if(tasks != null&&tasks.size()>0)
							{
								currentTask = null;
								while(tasks.size()>0)
								{
									currentTask = tasks.remove(0);
									if(currentTask!=null&&!currentTask.isCancel())
									{
										mainHandler.post(new BaseRunnable(currentTask) {
											
											@Override
											public void run() {
												// TODO Auto-generated method stub
												if(currentTask!=null)
												{
													if(!currentTask.isCancel())
														excute(currentTask);
													else
														currentTask.cancel();
												}
												else
												{
													iterateTask();
												}
											}
										});
									}
								}
							}
						}
					});
				}
		}
		private void excute(BaseTask currentTask) {
			if(currentTask!=null)
			{
				BaseExcutor.this.currentTask = currentTask;
				if(!currentTask.isCancel())
				{
					currentTask.prepareTask(null);
					executorService.submit(new BaseRunnable(currentTask) {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							super.run();
							try {
								taskExcute(currentTask);
							} catch (final JHException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								taskFailed(currentTask, e);
								
							}
						}

					});
				}
				else
				{
					currentTask.cancel();
				}
			}
		}
		private void taskExcute(BaseTask currentTask)
				throws JHException {
			if(!currentTask.isCancel())
			{
				currentTask.doTask();
				mainHandler.post(new BaseRunnable(currentTask){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						super.run();
						if(currentTask!=null)
						{
							if(!currentTask.isCancel())
								currentTask.success();
							else
								currentTask.cancel();
						}
						BaseExcutor.this.currentTask = null;
						iterateTask();
					}});
			}
			else
			{
				mainHandler.post(new BaseRunnable(currentTask){
					@Override
					public void run() {
						currentTask.cancel();
					}
				});
				iterateTask();
			}
		}

		private void taskFailed(BaseTask currentTask,final JHException e) {
				mainHandler.post(new BaseRunnable(currentTask){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						super.run();
						if(currentTask!=null)
						{
							if(!currentTask.isCancel())
								currentTask.fail(e.getMessage());
							else
								currentTask.cancel();
						}
						BaseExcutor.this.currentTask = null;
						iterateTask();
					}
					
				});
		}
		public void exit()
		{
			if(tasks!=null)
			{
				tmptasks.clear();
				for(BaseTask task:tasks)
					task.cancelTask();
				tasks.clear();
				tmptasks = null;
				tasks = null;
			}
			
		}
		public void clearall()
		{
			if(tasks!=null)
			{
				for(BaseTask task:tasks)
					task.cancelTask();
				tasks.clear();
			}
			if(mainHandler!=null)
			{
				
			}
		}
		public void exit(Context context)
		{
			if(tasks!=null)
			{
				tmptasks.clear();
				for(BaseTask task:tasks)
				{
					if(task!=null)
					{
						if(task.getContext()==null||task.getContext()!=context)
							tmptasks.add(task);
						else
							task.cancelTask();
					}
				}
				tasks.clear();
				tasks.addAll(tmptasks);
			}
		}
		private Thread workthread;
	}*/
	public interface BackPressListener
	{
		public void press(Activity activity);
	}
/*	private BackPressListener defaultListener = new BackPressListener(){

		@Override
		public void press(Activity activity) {
			// TODO Auto-generated method stub
			if(excutor!=null)
			{
				excutor.cancelCurrentTask();
			}
			superBackPressed();
		}};*/
	public void setBackPressListener(BackPressListener backPress)
	{
		this.backPress = backPress;
	}
	private BackPressListener backPress;
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(backPress!=null)
		{
			backPress.press(this);
		} else
			superBackPressed();
	}

	private void superBackPressed() {
		super.onBackPressed();
	}

	public ProgressDialog getProgressDialog() {
		return progressDialog;
	}
	public  void startActivity(Class activity)
	{
		Intent intent;
		intent = new Intent();
		intent.setClass(this, activity);
		this.startActivity(intent);
	}
	@Override
	public <T> void request(String url, Object req, IResultCallBack<T> callBack,Class<T> cls) {
		// TODO Auto-generated method stub
		request(url,null,req,callBack,cls);
	}
	@Override
	public <T> void request(String url, HashMap<String, String> headers,
			Object req, IResultCallBack<T> callBack, Class<T> cls) {
		// TODO Auto-generated method stub
		this.excuteTask(new RemoteTask<T>(url,headers,req,callBack,cls));
	}
	
}
