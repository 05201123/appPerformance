package com.jh.memory.leak;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.jh.memory.R;
import com.jh.memory.leak.adapter.LeakAdapter;
import com.jh.memory.utils.LeakListener;
import com.jh.memory.utils.LeakListenersManager;
/**
 * 静态变量引用Context
 * @author 099
 *
 */
public class StaticContextMemoryLeakActivity extends Activity implements LeakListener{
	private ListView listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listview = (ListView) findViewById(R.id.main_lv);
		listview.setAdapter(new LeakAdapter(this,getListData()));
		LeakListenersManager.getManager().addListener(this);
	}

	private List<String> getListData() {
		List<String> datalist=new ArrayList<String>();
		for(int i=0;i<15;i++){
			datalist.add("第"+i+"条");	
		}
		return datalist;
	}

	@Override
	public void happenLeak() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
//		LeakListenersManager.getManager().removeListener(this);
	}
	
}
    