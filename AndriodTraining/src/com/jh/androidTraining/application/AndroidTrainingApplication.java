package com.jh.androidTraining.application;

import android.app.Application;
import android.content.Context;

/**
 * 应用application
 * @author 099
 *
 */
public class AndroidTrainingApplication extends Application {
	private static Context publicContext=null;
	@Override
	public void onCreate() {
		super.onCreate();
		publicContext=this;
	}
	/**
	 * @return the publicContext
	 */
	public static Context getPublicContext() {
		return publicContext;
	}

}
