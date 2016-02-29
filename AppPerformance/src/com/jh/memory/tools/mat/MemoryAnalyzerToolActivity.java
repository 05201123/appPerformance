package com.jh.memory.tools.mat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;

import com.jh.memory.R;
import com.jh.memory.tools.mat.dto.Interest;
import com.jh.memory.tools.mat.dto.Person;     
/**
 * MAT使用实例
 * @author 099
 *
 */
public class MemoryAnalyzerToolActivity extends Activity{
	/**zk*/
	private Person zK;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawableResource(R.drawable.study);
		zK=createPerson("zk");
	}
	/***
	 * 创建一个人
	 * @param personName
	 * @return
	 */
	private Person createPerson(String personName) {
		Person pesron=new Person();
		pesron.setName(personName);
		pesron.setHeight("177");
		pesron.setWeight("152");
		pesron.setSex("man");
		pesron.setBirthday(getData(1986, 4, 30));
		pesron.setInterestList(createPersonInterest(personName));
		return pesron;
	}
	/***
	 * 创建人的兴趣
	 * @return
	 */
	private ArrayList<Interest> createPersonInterest(String personName) {
		ArrayList<Interest> list=new ArrayList<Interest>();
		for(int i=0;i<20000;i++){
			Interest interest=new Interest();
			interest.setBeginTime(getData(2015, 10, 23));
			interest.setInterestName(personName+": study");
			interest.setInterestSort("IT");
			list.add(interest);
		}
		return list;
	}
	/**
	 * 获取日期
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	private Date getData(int year,int month,int day) {
		Calendar calendar=Calendar.getInstance();
		calendar.set(year, month, day);
		return calendar.getTime();
	}
}
