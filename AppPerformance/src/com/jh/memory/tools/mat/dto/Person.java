package com.jh.memory.tools.mat.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/***
 * 人类
 * @author 099
 *
 */
public class Person {
	/**名字*/
	private String name;
	/**性别*/
	private String sex;
	/**身高*/
	private String height;
	/**体重*/
	private String weight;
	/**生日**/
	private Date birthday;
	/**兴趣*/
	private ArrayList<Interest> interestList;
	/**读过的书*/
	private ArrayList<Book> readBooks;
	/**当前的计划*/
	private Map<String,Plan>  currentMap;
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public ArrayList<Interest> getInterestList() {
		return interestList;
	}
	public void setInterestList(ArrayList<Interest> interestList) {
		this.interestList = interestList;
	}
	/**
	 * @return the readBooks
	 */
	public ArrayList<Book> getReadBooks() {
		return readBooks;
	}
	/**
	 * @param readBooks the readBooks to set
	 */
	public void setReadBooks(ArrayList<Book> readBooks) {
		this.readBooks = readBooks;
	}
	/**
	 * @return the currentMap
	 */
	public Map<String,Plan> getCurrentMap() {
		return currentMap;
	}
	/**
	 * @param currentMap the currentMap to set
	 */
	public void setCurrentMap(Map<String,Plan> currentMap) {
		this.currentMap = currentMap;
	}
	
}
