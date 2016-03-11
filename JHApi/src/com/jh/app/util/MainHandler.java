package com.jh.app.util;

import android.os.Handler;
import android.os.Looper;

public class MainHandler {
	private static Handler mainHandler = new Handler(Looper.getMainLooper());
	public static Handler getHandler(){
		return mainHandler;
	}
	public static Handler newInstance(){
		return new Handler(Looper.getMainLooper());
	}
}
