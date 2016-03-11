package com.jh.net;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;

import com.jh.app.util.BaseTask;
import com.jh.app.util.ConcurrenceExcutor;
import com.jh.app.util.IRemoteRequest;
import com.jh.app.util.IResultCallBack;
import com.jh.exception.JHException;
import com.jh.util.GsonUtil;

public class RemoteExcutor implements IRemoteRequest{
	private ConcurrenceExcutor cuExcutor = ConcurrenceExcutor.getInstance();
	private static RemoteExcutor excutor = new RemoteExcutor();
	private RemoteExcutor(){
		
	}
	public static RemoteExcutor getInstance(){
		return excutor;
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
		cuExcutor.addTaskFirst(new RemoteTask<T>(url,headers,req,callBack,cls));
	}

}
