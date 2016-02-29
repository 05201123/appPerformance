package com.jh.memory.tools.mat.dto;

import java.util.Date;

/**
 * 兴趣
 * @author 099
 *
 */
public class Interest {
	/**兴趣名字*/
	private String interestName;
	/**兴趣分类*/
	private String interestSort;
	/**兴趣开始的时间**/
	private Date beginTime;
	public String getInterestName() {
		return interestName;
	}
	public void setInterestName(String interestName) {
		this.interestName = interestName;
	}
	public String getInterestSort() {
		return interestSort;
	}
	public void setInterestSort(String interestSort) {
		this.interestSort = interestSort;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	} 
}
