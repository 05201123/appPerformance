package com.jh.memory.leak;

import com.jh.memory.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
/**
 * Handler引起的内存泄漏
 * @author 099
 *
 */
public class HandlerMemoryLeakActivity extends Activity {
	/**延迟时间**/
	private static long delayTime=200000;
	private Handler mHandler=new Handler()/*{
		public void handleMessage(Message msg) {
			String textconent=tv_timer.getText().toString().trim();
			tv_timer.setText((Integer.valueOf(textconent)+1)+"");
			mHandler.sendEmptyMessageDelayed(0, 1000);
			
		};
	}*/;
	private TextView tv_timer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_handler_leak);
		oneLeakScene();
		twoLeakScene();
		threeLeakScene();
		fourLeakScene();
		fiveLeakScene();
		otherLeakScene();
		Log.e("eeeeeeeeeeeeeeee", "eeeeeeeeeeeeeeeeeee");
	}
	@Override
	protected void onStart() {
		Log.e("gggggggggggggggg", "gggggggggggggggggg");
		super.onStart();
	}

	/**
	 * 第一种使用场景，引发的泄露，短暂型内存泄露
	 * 一般不会出现
	 */
	private void oneLeakScene() {
//		mHandler.postDelayed(new Runnable() {
//		
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			
//		}
//	}, delayTime);
		
	}
	/**
	 * 第二种使用场景，引发的泄露，短暂型内存泄露
	 * 一般不会出现，但也要注意
	 */
	private  void twoLeakScene(){
//		mHandler.sendEmptyMessageDelayed(20000, delayTime);
	}
	
	
	
	/**
	 * 第三种种使用场景，引发的泄露，短暂型内存泄露
	 * 一般不会出现
	 */
	private void threeLeakScene() {
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				// dosomething()
//				SystemClock.sleep(delayTime);
//				mHandler.sendEmptyMessage(0);
//				
//				
//			}
//		}).start();
	}
	
	/**
	 * 第四种种使用场景，可能引发的主线程阻塞
	 * 这种方式比较危险，很容易造成无限迭代，轻则内存泄露，重则阻塞主线程卡顿
	 * 使用场景预加载
	 * 
	 */
	private void fourLeakScene(){
		mHandler.postDelayed(new FourLeakRunnable() , 500);
//		mHandler.post(new FourLeakRunnable());
		
	}
	/**
	 * 第五种使用场景，类似于读秒器，倒计时的功能
	 * 可能发生泄露
	 */
	private void fiveLeakScene(){
//		tv_timer = (TextView) findViewById(R.id.tv_timer);
//		tv_timer.setText("0");
//		mHandler.sendEmptyMessageDelayed(0, 1000);
		
	}
	/**
	 * 其他内存泄露
	 */
	private void otherLeakScene(){
		/**
		 * 生命周期
		 * 1.多看些java基础,内部类
		 * 2.源码 message handler looper messagequeue
		 * 3.Context 慎传
		 * **/
		
	}
	
	
	private class  FourLeakRunnable implements Runnable{

		@Override
		public void run() {
			Log.e("aaaaaaaaaaaa", "aaaaaaaaa="+System.currentTimeMillis()+" aaaaa="+this.hashCode());
			SystemClock.sleep(100);
			// TODO Auto-generated method stub
			if(FourLeakDataDao.getData()!=null){
				//dosomething
			}else{
				fourLeakScene();
			}
			
			
		}
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			Log.e("bbbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbbbbbbbbbbbbbb");
			finish();
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
		
	}
	
	@Override
	public void finish() {
		super.finish();
		Log.e("cccccccccccc", "ccccccccccccccccccccccccc");
	}
	@Override
	protected void onDestroy() {
		Log.e("ddddddddddddddddddd", "dddddddddddddddddddddddddd");
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.e("hhhhhhhhhhhhhhhhh", "hhhhhhhhhhhhhhhhhhhhhhh");
		super.onResume();
//		FourLeakDataDao.setData("11111111111111");
	}
	
	
}

class FourLeakDataDao{
	private static Object data=null;
	public static Object getData(){
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public static void setData(Object data) {
		FourLeakDataDao.data = data;
	};
}



