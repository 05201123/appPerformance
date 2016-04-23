package com.jh.androidTraining.BuildingAppswithContentSharing;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;

import com.jh.androidTraining.R;
/**
 * Sharing Simple Data
 * file:///D:/sdk/docs/training/sharing/index.html
 * @author 099
 *
 */
public class SharingSimpleDataActivity extends Activity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sharingsimpledata);
		findViewById(R.id.sendsharetext).setOnClickListener(this);
		findViewById(R.id.sendsharechoosertext).setOnClickListener(this);
		findViewById(R.id.sendsharebinary).setOnClickListener(this);
		findViewById(R.id.sendsharemul).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.sendsharetext){
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
			sendIntent.setType("text/plain");//若无人接收则crash，所以用之前，需要先判断是否有人接收sendIntent.setType("sss")
			startActivity(sendIntent);
		}else if(v.getId()==R.id.sendsharechoosertext){
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
			sendIntent.setType("text/plain");///若无人接收,不会crash，sendIntent.setType("sss")
			startActivity(Intent.createChooser(sendIntent,"sendsharechoosertext"));
		}else if(v.getId()==R.id.sendsharebinary){
			Intent shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND);
//			Uri uri=Uri.parse("android.resource://" + getPackageName() + "/"+ R.drawable.ic_launcher);分享给本应用好使
			Uri uri = Uri.parse("file:///sdcard/icon.jpg");//分享给所有应用都好使
//			Uri uri = Uri.parse("file:///android_asset/icon.jpg");不好使
//			Uri uri = Uri.parse("android.resource://"+"com.jh.androidTraining/"+R.raw.icon); 分享给本应用好使
			shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
			shareIntent.setType("image/jpeg");
			startActivity(Intent.createChooser(shareIntent, "sendsharebinary"));
		}else if(v.getId()==R.id.sendsharemul){
			ArrayList<Uri> imageUris = new ArrayList<Uri>();
			Uri uri = Uri.parse("file:///sdcard/icon.jpg");
			Uri uri2 = Uri.parse("file:///sdcard/icon2.jpg");
			imageUris.add(uri); // Add your image URIs here
			imageUris.add(uri2);

			Intent shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
			shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
			shareIntent.setType("image/*");
			startActivity(Intent.createChooser(shareIntent, "Share images to.."));
			
			
			
		}
			
		
	}
}
