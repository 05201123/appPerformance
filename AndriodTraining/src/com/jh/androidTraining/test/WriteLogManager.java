package com.jh.androidTraining.test;
/**
 * 写日志管理器
 * @author 099
 *
 */
public class WriteLogManager {
	/**
	 * 写日志
	 * @param operationAction
	 * @param logContent
	 */
	public static void writeLogs(String operationAction,String logContent){
		WriteLogUtils.writeLogSDCard(new TestEntity(operationAction,logContent).toString());
	}

}
