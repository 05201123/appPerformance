package com.jh.memory.leak;

import com.jh.memory.DemoActivity;
import com.jh.memory.base.BaseActivity;
import com.jh.memory.utils.Utils;


/**
 * 内存泄漏主页面
 * @author 099
 *
 */
public class MemoryLeakMainActivity extends BaseActivity {

	@Override
	protected String[] getItemData() {
		 String[] appdata={"静态变量引用Context","Handler内存问题","Demo"};
		return appdata;
	}
	@Override
	protected void itemCilck(int position) {
		String textview=appPerf[position];
		if("静态变量引用Context".equals(textview)){
			Utils.intentToActivity(this, StaticContextMemoryLeakActivity.class);
		}else if("Handler内存问题".equals(textview)){
			Utils.intentToActivity(this, HandlerMemoryLeakActivity.class);
		}else if("Demo".equals(textview)){
			Utils.intentToActivity(this, DemoActivity.class);
		}
		
	}
}
