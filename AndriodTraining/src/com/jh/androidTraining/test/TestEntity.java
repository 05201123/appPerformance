package com.jh.androidTraining.test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 测试Log实体
 * @author 099
 *
 */
public class TestEntity {
	private static final String ZPH_LOG="androidtraining Log: ";
	private static final String BLANK=" ";
	private String testAction;
	private String testContent;
	public TestEntity(String operationAction, String logContent) {
		this.testAction=operationAction;
		this.testContent=logContent;
	}
	public String getTestAction() {
		return testAction;
	}
	public void setTestAction(String testAction) {
		this.testAction = testAction;
	}
	public String getTestContent() {
		return testContent;
	}
	public void setTestContent(String testContent) {
		this.testContent = testContent;
	}
	@Override
	public String toString() {
		StringBuffer sb=new StringBuffer();
		String time = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss").format(new Date());
		sb.append(ZPH_LOG).append(BLANK).append(time).append(BLANK).append(testAction).append(BLANK).append(testContent);
		return sb.toString();
	}
}
