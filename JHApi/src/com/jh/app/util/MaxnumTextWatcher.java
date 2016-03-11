/*
 * @project POA_Android
 * @package com.jh.iyou.contact.util
 * @file OwnTextWatcher.java
 * @version  1.0
 * @author  张楠
 * @time  2011-5-24 下午01:17:08
 * CopyRight:北京金和软件信息技术有限公司 2011-5-24
 */package com.jh.app.util;


import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;
/**
 * 监控输入框最大字数输入
 * @author jhzhangnan1
 *
 */

public class MaxnumTextWatcher implements TextWatcher{
	
	private int maxnum;
	private Context context;
	public int start,end;
	public MaxnumTextWatcher(int maxnum,Context context)
	{
		this.maxnum = maxnum;
		this.context = context;
	}
	public void afterTextChanged(Editable s) {
		boolean nOverMaxLength = false;
		   
		nOverMaxLength = (s.length() > maxnum) ? true : false; 
		   if(nOverMaxLength){
			  BaseToast.getInstance(context, "最大只能输入"+maxnum+"字").show();
			//   s.delete(start, Math.min(start+end, s.length()));
			   s.delete(start, Math.min(start+end, s.length()));
		   }
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		this.start = Math.max(Selection.getSelectionStart(s), 0);
//		this.start = edit.getSelectionStart();
		this.end = after;
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
	}
	/*
	 *
	 * Class Descripton goes here.
	 *
	 * @class OwnTextWatcher
	 * @version  1.0
	 * @author  张楠
	 * @time  2011-5-24 下午01:17:08
	 */
	public int getMaxnum() {
		return maxnum;
	}
	public void setMaxnum(int maxnum) {
		this.maxnum = maxnum;
	}
	
}
