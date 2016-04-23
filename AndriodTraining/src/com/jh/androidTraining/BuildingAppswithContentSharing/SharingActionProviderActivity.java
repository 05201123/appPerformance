package com.jh.androidTraining.BuildingAppswithContentSharing;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ShareActionProvider;

import com.jh.androidTraining.R;
/**
 * Adding an Easy Share Action
 * file:///D:/sdk/docs/training/sharing/shareaction.html
 * @author 099
 *
 */
public class SharingActionProviderActivity extends Activity implements OnClickListener{
	private ShareActionProvider mShareActionProvider;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shareactionprovider);
	}

	@Override
	public void onClick(View v) {
			
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_shareactionprovider, menu);
		  // Locate MenuItem with ShareActionProvider
	    MenuItem item = menu.findItem(R.id.action_share);

	    mShareActionProvider = (ShareActionProvider) item.getActionProvider();
	    mShareActionProvider.setShareIntent(getDefaultIntent());
		return super.onCreateOptionsMenu(menu);
	}
	private Intent getDefaultIntent() {
	    Intent intent = new Intent(Intent.ACTION_SEND);
	    intent.setType("image/*");
	    return intent;
	}
}
