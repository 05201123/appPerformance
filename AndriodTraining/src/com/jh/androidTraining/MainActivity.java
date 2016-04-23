package com.jh.androidTraining;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.jh.androidTraining.BuildingAppswithContentSharing.SharingActionProviderActivity;
import com.jh.androidTraining.BuildingAppswithContentSharing.SharingSimpleDataActivity;
import com.jh.androidTraining.activity.ParentActivityMeansBeforeActivity;
/**
 * 主activity
 * @author 099
 *
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.hello_world).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				startActivity(new Intent(MainActivity.this, ParentActivityMeansBeforeActivity.class));
//				startActivity(new Intent(MainActivity.this, RawAndAssetsDifferActivity.class));
//				startActivity(new Intent(MainActivity.this, SharingSimpleDataActivity.class));
				startActivity(new Intent(MainActivity.this, SharingActionProviderActivity.class));
			}
		});
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

}
