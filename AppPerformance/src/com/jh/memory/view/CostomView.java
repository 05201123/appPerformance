package com.jh.memory.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

@SuppressLint({ "InlinedApi", "ResourceAsColor" })
public class CostomView extends View {

	private final  Paint paint;
	private final Context context;
	
	public CostomView(Context context) {
		
		// TODO Auto-generated constructor stub
		this(context, null);
	}

	public CostomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.paint = new Paint();
		this.paint.setAntiAlias(true); //消除锯齿
		this.paint.setColor(Color.RED);
		this.paint.setStyle(Style.FILL); 
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int center=getWidth()/2;
		canvas.drawCircle(center,center, center, this.paint);
		super.onDraw(canvas);
	}
	
	
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}

