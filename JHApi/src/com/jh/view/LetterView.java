package com.jh.view;


import java.util.HashMap;




import java.util.List;

import com.jinher.commonlib.R;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

/**
 * 
 * <code>LetterView</code>
 * @description: 自定义VIEW 排列字母
 * @version  1.0
 * @author  yourname
 * @since 2012-2-10
 */
public class LetterView extends View {
	
	OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	String[] b = {"?","#","A","B","C","D","E","F","G","H","I","J","K","L"
			,"M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	int choose = -1;
	Paint paint = new Paint();
	boolean showBkg = false;
	int totalHeight = 0;
	private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
	public HashMap<String, Integer> getAlphaIndexer() {
		return alphaIndexer;
	}
	public boolean isSearchString(String letter)
	{
		return letter.equalsIgnoreCase("?");
	}
	private List<? extends IPY> items;
	private int offset;
	public interface IPY
	{
		public String getTitle();
	}

	public LetterView(Context context) {
		super(context);
		alphaIndexer = new HashMap<String, Integer>();
		init();
	}
	public LetterView(Context context, AttributeSet attrs) {
		super(context, attrs);

		alphaIndexer = new HashMap<String, Integer>();
		init();
	}
	public LetterView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		alphaIndexer = new HashMap<String, Integer>();
		init();
	}
	public void setItems(List<? extends IPY> items)
	{
		this.items = items;
		if(items!=null)
			refreshAlpha();
	}
	public boolean hasAlpha(String alpha)
	{
		return alphaIndexer.containsKey(alpha);
	}
	/**
	 * 获取字母对应位置
	 * @param alpha
	 * @return -1表示列表没有对应字母
	 */
	public int getAlphaPosition(String alpha)
	{
		if(hasAlpha(alpha))
		{
			return alphaIndexer.get(alpha);
		}
		else
			return -1;
	}
	public void refreshAlpha()
	{
		alphaIndexer.clear();
		alphaIndexer.put("?", 0);
//		alphaIndexer.put("#", offset);
		String current = null,last = null;
		int index = 0;
		current = last = "#";
		for (IPY item:items) {
			// 当前汉语拼音首字母
				if(index>=offset)
				{
					current = item.getTitle();
					if(alphaIndexer.containsKey(current))
					{
						
					}
					else
					{
						alphaIndexer.put(current, index);
					}
				}
				index++;
		}
	}
	private int height,width,singleLetterWidth,singleHeight;
	Rect src,destination;
	private Bitmap letter_bg;
	Bitmap search,search_click,search_select;
	private void init()
	{
		Resources res = this.getContext().getResources();
		letter_bg =  BitmapFactory.decodeResource(res, R.drawable.letter_bg);
 	    BitmapFactory.Options options = new BitmapFactory.Options();
 	    options.inScaled = false;
 	   search = BitmapFactory.decodeResource(this.getResources(), R.drawable.search,options);
 	  search_click = BitmapFactory.decodeResource(this.getResources(), R.drawable.search_click,options);
 	 search_select = BitmapFactory.decodeResource(this.getResources(), R.drawable.search_select,options);

	}
	private int padding;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(src==null)
		{
		 	  height = getHeight()-6;
			    width = getWidth();
			    singleLetterWidth = 0;
			    singleHeight = (height) /( b.length);
			    totalHeight = b.length*singleHeight;
			    padding = (height - totalHeight)/2+3;
				src = new Rect();
				destination = new Rect();
		}
		src.top = 0;
	    src.bottom =src.top+ letter_bg.getHeight();
	    src.left = 0;
	    src.right =  letter_bg.getWidth();
	    destination.left = (int) width/2-letter_bg.getWidth()/2;
 	    destination.top = 0 ;
 	    destination.bottom = (int)getHeight() ;
 	    destination.right = (int)( destination.left+letter_bg.getWidth());
		if(showBkg){
//		    canvas.drawColor(Color.parseColor("#40000000"));
//			canvas.drawBitmap(letter_bg, src, dst, paint);
			canvas.drawBitmap(letter_bg, src, destination, paint);
		       paint.setColor(Color.WHITE);
		}
		else
		{
			 canvas.drawColor(Color.parseColor("#00000000"));

		       paint.setColor(Color.GRAY);
		}
	    for(int i=0;i<b.length;i++){
	    	if(showBkg){
			       paint.setColor(Color.WHITE);
			}
			else
			{
			       paint.setColor(0xff999999);
			}
//	       paint.setTypeface(Typeface.DEFAULT_BOLD);
	       paint.setTextSize((float) singleHeight);
//	       paint.setTextScaleX((float) 1.5);
	       singleLetterWidth = (int) paint.measureText(b[5]);
	       paint.setAntiAlias(true);
	       if(i == choose){
	    	   paint.setColor(Color.parseColor("#3399ff"));
	    	   paint.setFakeBoldText(true);
	       }
	       float xPos = width/2  - paint.measureText(b[i])/2;
	       float yPos = padding+singleHeight * i + singleHeight;

    	   
	       if (i==0) {

		       
		       src.top = 0;
		       src.bottom =src.top+ search.getHeight();
		       src.left = 0;
		       src.right =  search.getWidth();
		       
		      
	    	   destination.left = (int) width/2-search.getWidth()/2;
	    	   destination.top = padding+Math.max((int) singleHeight/2-search.getHeight()/2 ,0);
	    	   destination.bottom = (int)( destination.top +singleHeight );
	    	   destination.right = (int)( destination.left+Math.max(search.getWidth(), singleLetterWidth));
	    	   if(showBkg)
	    	   {
		    	   if(choose==0)
		    	   {
		    		   canvas.drawBitmap(search_select, src, destination, paint);
		    	   }
		    	   else
		    	   {
		    		   canvas.drawBitmap(search, src, destination, paint);
		    	   }
	    	   }
	    	   else
	    		   canvas.drawBitmap(search_click, src, destination, paint);
		}else {
			canvas.drawText(b[i], xPos, yPos, paint);
		}
	       paint.reset();
	    }
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
	    final float y = event.getY()-padding;
	    final int oldChoose = choose;
	    final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
	    final int c = (int) ((y*b.length/totalHeight));
	    
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				showBkg = true;
				if(oldChoose != c && listener != null){
					if(c >= 0 && c< b.length){
						listener.onTouchingLetterChanged(b[c]);
						choose = c;
						invalidate();
					}
				}
				
				break;
			case MotionEvent.ACTION_MOVE:
				if(oldChoose != c && listener != null){
					if(c >=0 && c< b.length){
						listener.onTouchingLetterChanged(b[c]);
						choose = c;
						invalidate();
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				showBkg = false;
				choose = -1;
//				listener.onTouchingLetterChanged("");
				invalidate();
				break;
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	public interface OnTouchingLetterChangedListener{
		public void onTouchingLetterChanged(String s);
	}
	
	
	public void setTextSize(float density){
		paint.setTextSize(20f*density);
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int startPosition) {
		this.offset = startPosition;
	}
}
