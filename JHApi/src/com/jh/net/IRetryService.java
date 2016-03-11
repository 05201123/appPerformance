package com.jh.net;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Policy.Parameters;

import com.jh.exception.JHException;

public abstract class IRetryService {
	// 失败重试次数
	private int retryTimes = 3;
	// 失败重试间隔
	private int retryInterval = 5000;

	public int getRetryTimes() {
		return retryTimes;
	}

	public void setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
	}

	public int getRetryInterval() {
		return retryInterval;
	}

	public void setRetryInterval(int retryInterval) {
		this.retryInterval = retryInterval;
	}

	protected Object doTaskRetry(Method method, Object[] args)
			throws JHException {
		int requestTime = retryTimes;
		while (requestTime > 0) {
			try {
				return method.invoke(this, args);
			} catch (JHException e) {
				if (requestTime == 1)
					throw e;
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			requestTime--;
			try {
				Thread.sleep(retryInterval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	protected Method getMethod(String methodName, Object[] args){
		return getMethod(this,methodName,args);
	}
	protected Method getMethod(Object owner,String methodName, Object[] args){
		Class ownerClass = owner.getClass();

		Class[] argsClass = new Class[args.length];

		for (int i = 0, j = args.length; i < j; i++) {
			argsClass[i] = args[i].getClass();
		}
		Method method = null;
		try {
			method = ownerClass.getMethod(methodName, argsClass);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return method;
	}
	protected Object invokeMethod(String methodName, Object[] args){
		return invokeMethod(this,methodName,args);
	}
	protected Object invokeMethod(Object owner, String methodName, Object[] args) {

		Class ownerClass = owner.getClass();

		Class[] argsClass = new Class[args.length];

		for (int i = 0, j = args.length; i < j; i++) {
			argsClass[i] = args[i].getClass();
		}

		Method method;
		try {
			method = getMethod(owner,methodName,args);
			return method.invoke(owner, args);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	//	protected abstract<T,K> T doTask(K k);
}
