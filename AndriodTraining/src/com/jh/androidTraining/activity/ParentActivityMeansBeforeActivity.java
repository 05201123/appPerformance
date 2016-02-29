package com.jh.androidTraining.activity;

import com.jh.androidTraining.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
/**
 * 标签parentactivity的作用
 * @author 099
 *
 */
public class ParentActivityMeansBeforeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parent_activity_means_before);
		 getActionBar().setDisplayHomeAsUpEnabled(true);
		findViewById(R.id.button_parent).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ParentActivityMeansBeforeActivity.this, ParentActivityMeansActivity.class));
				
			}
		});
	}
	public void onClickTwo(View v){
		
		Toast.makeText(this, "onClickTwo", Toast.LENGTH_SHORT).show();
	}
	
	
}
