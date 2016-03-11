package com.jh.app.util;

public interface IResultCallBack<T> {
	public void success(T result);
	public void fail(String errorMes);
}
