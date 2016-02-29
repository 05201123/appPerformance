package com.jh.memory.optimize;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.jh.memory.R;
import com.jh.memory.optimize.view.MyCustomTextView;
import com.jh.memory.utils.LeakListener;
import com.jh.memory.utils.LeakListenersManager;
/**
 *内存优化：慎用自定义view
 * @author 099
 *
 */
public class CautionCustomViewActivity extends Activity implements LeakListener{

	private MyCustomTextView mtv; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_caution_customview);
		Log.e("MemoryOptimize", "CautionCustomViewActivity:"+"onCreate()");
		final RelativeLayout rootRL=(RelativeLayout) findViewById(R.id.root);
		mtv = new MyCustomTextView(this);
		mtv.setText("custom view");
		rootRL.addView(mtv);
		mtv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(rootRL.getChildCount()>0){
					rootRL.removeViewAt(0);
				}
			}
		});
		SystemClock.sleep(20000);
		
	}
	@Override
	public void finish() {
		super.finish();
		Log.e("MemoryOptimize", "CautionCustomViewActivity:"+"finish()");
	}
	@Override
	protected void onStop() {
		super.onStop();
		Log.e("MemoryOptimize", "CautionCustomViewActivity:"+"onStop()");
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e("MemoryOptimize", "CautionCustomViewActivity:"+"onDestroy()");
//		LeakListenersManager.getManager().removeListener(this);
	}
	@Override
	public void happenLeak() {
		// TODO Auto-generated method stub
		
	}
	
}
    