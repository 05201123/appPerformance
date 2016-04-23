package com.jh.androidTraining.BuildingAppswithContentSharing;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jh.androidTraining.R;
/**
 * Receiving Simple Data from Other Apps
 * file:///D:/sdk/docs/training/sharing/receive.html
 * @author 099
 *
 */
public class ReceivingSimpleDataActivity extends Activity implements OnClickListener{
	private TextView receivedataTV;
	private ImageView receivedataIV;
	private LinearLayout receivedataLL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receivingsimpledata);
		receivedataTV = (TextView) findViewById(R.id.receivedata_tv);
		receivedataIV = (ImageView) findViewById(R.id.receivedata_iv);
		receivedataLL=(LinearLayout) findViewById(R.id.receivedata_ll);
		handleIntent();
		
		
	}

	private void handleIntent() {
		Intent intent=getIntent();
		if(Intent.ACTION_SEND.equals(intent.getAction())&&"text/plain".equals(intent.getType())){
			String receivedata=intent.getStringExtra(Intent.EXTRA_TEXT);
			if(!TextUtils.isEmpty(receivedata)){
				receivedataTV.setText(receivedata);
			}
		}else if(Intent.ACTION_SEND.equals(intent.getAction())&&"image/jpeg".equals(intent.getType())){
			Uri receivedata=intent.getParcelableExtra(Intent.EXTRA_STREAM);
			receivedataIV.setImageURI(receivedata);
		}else if(Intent.ACTION_SEND_MULTIPLE.equals(intent.getAction())&&"image/*".equals(intent.getType())){
			ArrayList<Uri> receivedatas=intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
			for(Uri uri:receivedatas){
				ImageView view=new ImageView(this);
				view.setImageURI(uri);
				receivedataLL.addView(view);
			}
		}
		
	}

	@Override
	public void onClick(View v) {
		
		
	}
}
