package com.jh.memory.base;

import com.jh.memory.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public abstract class BaseActivity extends Activity {
	/**首页listview*/
	private ListView listview;
	protected String[] appPerf;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appPerf=getItemData();
		setContentView(R.layout.activity_main);
		listview = (ListView) findViewById(R.id.main_lv);
		listview.setAdapter(new ArrayAdapter<String>(this,R.layout.list_item, appPerf));
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				itemCilck(position);
			}

			
		});
	}
	
	protected abstract String[] getItemData();
	/**
	 * 点击每个条目
	 * @param position
	 */
	protected abstract void itemCilck(int position);
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
