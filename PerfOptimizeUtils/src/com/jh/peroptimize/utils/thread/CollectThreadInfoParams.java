package com.jh.peroptimize.utils.thread;
/***
 * 收集线程
 * @author 099
 *
 */
public interface CollectThreadInfoParams {
	/**保存路径*/
	public String getSavePath();
	/**设置时间间隔*/
	public long getTimespace() ;
	/**是否一直重复收集*/
	public boolean isRepeat();

}
