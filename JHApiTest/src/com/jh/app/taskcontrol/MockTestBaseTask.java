package com.jh.app.taskcontrol;

import android.os.Looper;
import com.jh.app.taskcontrol.constants.TaskConstants.TaskPriority;
import com.jh.exception.JHException;
/**
 * task mock
 * @author 099
 *
 */
public class MockTestBaseTask extends JHBaseTask {

	private int mPriority =TaskPriority.PRIORITY_NORMAL;
	
	private boolean isCancelLisExe=false;
	
	private boolean isNeedWait=false;
	
	private boolean isOnPreExe=false;
	private boolean isOnPreExeInMainUI=false;
	
	private boolean isOnFailedExe=false;
	private boolean isOnSuccessExe=false;
	
	private Object bussinessData;
	private  int num;
	
	
	public MockTestBaseTask(){
		
	}
	
	public void setmPriority(int priority){
		mPriority=priority;
	}
	public int getPriority() {
		return mPriority;
	}
	
	void notifyPre() {
		super.notifyPre();
	}
	@Override
	public void onPreExecute() {
		super.onPreExecute();
		setOnPreExe(true);
		isOnPreExeInMainUI=Looper.getMainLooper()==Looper.myLooper();
	}
	
	@Override
	public void doTask() throws JHException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void success() {
		super.success();
		setOnSuccessExe(true);
	}
	@Override
	public void fail(String errorMessage) {
		super.fail(errorMessage);
		setOnFailedExe(true);
	}
	@Override
	public void onProgressChanged(Object bussinessData, int newProgress) {
		super.onProgressChanged(bussinessData, newProgress);
		this.setBussinessData(bussinessData);
		this.setNum(newProgress);
		
	}
	
	/**
	 * @return the isCancelLisExe
	 */
	public boolean isCancelLisExe() {
		return isCancelLisExe;
	}
	/**
	 * @param isCancelLisExe the isCancelLisExe to set
	 */
	public void setCancelLisExe(boolean isCancelLisExe) {
		this.isCancelLisExe = isCancelLisExe;
	}
	/**
	 * @return the isNeedWait
	 */
	public boolean isNeedWait() {
		return isNeedWait;
	}
	/**
	 * @param isNeedWait the isNeedWait to set
	 */
	public void setNeedWait(boolean isNeedWait) {
		this.isNeedWait = isNeedWait;
	}

	/**
	 * @return the isOnPreExe
	 */
	public boolean isOnPreExe() {
		return isOnPreExe;
	}

	/**
	 * @param isOnPreExe the isOnPreExe to set
	 */
	public void setOnPreExe(boolean isOnPreExe) {
		this.isOnPreExe = isOnPreExe;
	}

	/**
	 * @return the isOnFailedExe
	 */
	public boolean isOnFailedExe() {
		return isOnFailedExe;
	}

	/**
	 * @param isOnFailedExe the isOnFailedExe to set
	 */
	public void setOnFailedExe(boolean isOnFailedExe) {
		this.isOnFailedExe = isOnFailedExe;
	}

	/**
	 * @return the isOnSuccessExe
	 */
	public boolean isOnSuccessExe() {
		return isOnSuccessExe;
	}

	/**
	 * @param isOnSuccessExe the isOnSuccessExe to set
	 */
	public void setOnSuccessExe(boolean isOnSuccessExe) {
		this.isOnSuccessExe = isOnSuccessExe;
	}

	/**
	 * @return the isOnPreExeInMainUI
	 */
	public boolean isOnPreExeInMainUI() {
		return isOnPreExeInMainUI;
	}

	/**
	 * @param isOnPreExeInMainUI the isOnPreExeInMainUI to set
	 */
	public void setOnPreExeInMainUI(boolean isOnPreExeInMainUI) {
		this.isOnPreExeInMainUI = isOnPreExeInMainUI;
	}

	/**
	 * @return the bussinessData
	 */
	public Object getBussinessData() {
		return bussinessData;
	}

	/**
	 * @param bussinessData the bussinessData to set
	 */
	public void setBussinessData(Object bussinessData) {
		this.bussinessData = bussinessData;
	}

	/**
	 * @return the num
	 */
	public int getNum() {
		return num;
	}

	/**
	 * @param num the num to set
	 */
	public void setNum(int num) {
		this.num = num;
	}

}
