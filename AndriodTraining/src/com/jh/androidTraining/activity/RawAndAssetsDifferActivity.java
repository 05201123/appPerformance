package com.jh.androidTraining.activity;

import java.io.InputStream;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.jh.androidTraining.R;
/*
 * 
 * 系统希望我们用啥，我们就用啥
res/raw和assets的相同点：
两者目录下的文件在打包后会原封不动的保存在apk包中，不会被编译成二进制。
res/raw和assets的不同点：
1.res/raw中的文件会被映射到R.java文件中，访问的时候直接使用资源ID即R.id.filename；assets文件夹下的文件不会被映射到R.java中，访问的时候需要AssetManager类。
2.res/raw不可以有目录结构，而assets则可以有目录结构，也就是assets目录下可以再建立文件夹
3.raw需要编译。


注意1：Google的Android系统处理Assert有个bug，在AssertManager中不能处理单个超过1MB的文件，不然会报异常，raw没这个限制可以放个4MB的Mp3文件没问题。
注意2：assets 文件夹是存放不进行编译加工的原生文件，即该文件夹里面的文件不会像 xml， java 文件被预编译，可以存放一些图片，html，js, css 等文件。

声音，字体，xml，html

//自定义声音   声音文件放在raw目录下，没有此目录自己创建一个
notification.sound=Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.mm); 
*/

/**
 * R.raw.*** and assets differentActivity
 * 
 *  50k左右的html文件{@link #loadRawHtml}与{@link #loadAssetsHtml}效率相差无几
 * 100K左右html文件{@link #loadAssetsHtml}要明显快过{@link #loadRawHtml}
 * 
 * raw：Uri mUri = Uri.parse("android.resource://" + getPackageName() + "/"+ R.raw.a);
 * asstes:"file:///"+"android_asset/a.html"
 * @author 099
 *
 */
public class RawAndAssetsDifferActivity extends Activity implements OnClickListener{
	private WebView webview;
	private Button htmlFromAssets;
	private Button htmlFromRaw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_raw_assets_differ);
		htmlFromAssets = (Button) findViewById(R.id.htmlFromAssets);
		htmlFromAssets.setOnClickListener(this);
		htmlFromRaw = (Button) findViewById(R.id.htmlFromRaw);
		htmlFromRaw.setOnClickListener(this);
		findViewById(R.id.FontsFromAssets).setOnClickListener(this);
		findViewById(R.id.FontsFromRaw).setOnClickListener(this);
		webview = (WebView) findViewById(R.id.webview);
		initWebview();
	}

	private void initWebview() {
		webview.setWebChromeClient(new WebChromeClient(){
		});
		webview.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				Log.e("aaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "onPageFinished:"+System.currentTimeMillis());
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.htmlFromAssets:
			loadAssetsHtml();
			break;
		case R.id.htmlFromRaw:
			loadRawHtml();
			break;
		case R.id.FontsFromAssets:
			loadAssetsFonts();
			break;
		case R.id.FontsFromRaw:
			loadRawFonts();
			break;

		default:
			break;
		}
		
	}
	/**
	 * 加载Raw中fonts到文字
	 */
	private void loadRawFonts() {
//		Typeface typeFace =Typeface.createFromFile("android.resource://" + getPackageName() + "/"+ R.raw.fonts);
//		htmlFromRaw.setTypeface(typeFace);                    crach     不能放在raw中
	}

	/**
	 * 加载Assets中fonts到文字
	 */
	private void loadAssetsFonts() {
		Typeface typeFace =Typeface.createFromAsset(getAssets(), "fonts.otf");
//		Typeface typeFace=Typeface.createFromFile("file:///"+"android_asset/fonts.otf");//crash
		htmlFromAssets.setTypeface(typeFace);
	}

	/**
	 * 加载Raw中的html到webview
	 */
	private void loadRawHtml() {
		Log.e("aaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "loadRawHtml:"+System.currentTimeMillis());
		String ret="";
		try {
			InputStream is = getResources().openRawResource(R.raw.a);
			int len = is.available();
			byte []buffer = new byte[len];
			is.read(buffer);
			ret = EncodingUtils.getString(buffer, "utf-8");
			is.close();
			} catch (Exception e) {
			e.printStackTrace();
			}
		webview.loadData(ret, "text/html; charset=UTF-8", null);//解决乱码，方法不错
		
	}

	/**
	 * 加载Assets中的html到webview
	 * 
	 */
	private void loadAssetsHtml() {
		Log.e("aaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "loadAssetsHtml:"+System.currentTimeMillis());
		webview.loadUrl("file:///"+"android_asset/a.html");
	}
	
	
	
	
}
