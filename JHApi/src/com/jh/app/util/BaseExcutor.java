package com.jh.app.util;

import java.util.HashMap;

import com.jh.exception.JHException;

import android.os.AsyncTask;
/**
 * 异步任务执行类，对于界面中，可以直接使用BaseActivity.excuteTask,该类主要用于service中执行
 * @author jhzhangnan1
 *
 */
@Deprecated
public class BaseExcutor extends AsyncTask<Void, Void, Void> {

	private HashMap result;
	private final String RESULTFLAG = "RESULT";
	private final String ERROR = "ERROR";
	private final String ERRORCODE = "ERRORCODE";
	private BaseTask task;
	public BaseExcutor(BaseTask task)
	{
		this.task = task;
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		task.prepareTask();
	}
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		if(result == null)
		{
			result = new HashMap();
		}
		try
		{
			task.doTask();
			this.setResult(true);
		} 
		catch(JHException e)
		{
			this.setResult(false);
//			this.result.put(ERRORCODE, e.getCode());
			this.setError(e);
		}
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(this.getResult())
		{
			task.success();
		}
		else
		{
			task.fail(this.getError());
		}
	}
	private void setResult(boolean result)
	{
		this.result.put(RESULTFLAG, result);
	}
	public boolean getResult()
	{
		if(result.containsKey(RESULTFLAG))
		{
			return (Boolean)result.get(RESULTFLAG);
		}
		else
		{
			return false;
		}
	}
	private void setError(JHException e)
	{
		result.put(ERROR, e);
	}
	public Exception getException(){
		if(result.containsKey(ERROR))
		{
			return (Exception)result.get(ERROR);
		}
		else
		{
			return null;
		}
	}
	public String getError()
	{
		Exception e = getException();
		if(e!=null)
		{
			return e.getMessage();
		}
		return null;
	}
/*	public int getErrorCode()
	{
		if(result.containsKey(ERRORCODE))
		{
			return (Integer)result.get(ERRORCODE);
		}
		else
		{
			return -1;
		}
	}*/
}
