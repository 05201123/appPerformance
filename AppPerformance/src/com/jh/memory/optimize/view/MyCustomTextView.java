package com.jh.memory.optimize.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
/**
 * 我的自定义TextView
 * @author 099
 *
 */
public class MyCustomTextView extends TextView {

public MyCustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public MyCustomTextView(Context context) {
		super(context);
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		Log.e("MemoryOptimize", "MyCustomTextView:"+"onAttachedToWindow()");
	}
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		Log.e("MemoryOptimize", "MyCustomTextView:"+"onDetachedFromWindow()");
	}
}
