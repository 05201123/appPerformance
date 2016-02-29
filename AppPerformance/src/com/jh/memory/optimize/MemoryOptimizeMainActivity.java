package com.jh.memory.optimize;

import com.jh.memory.DemoActivity;
import com.jh.memory.base.BaseActivity;
import com.jh.memory.utils.Utils;


/**
 * 内存优化问题主页面
 * @author 099
 *
 */
public class MemoryOptimizeMainActivity extends BaseActivity {

	@Override
	protected String[] getItemData() {
		 String[] appdata={"慎用自定义view","Demo"};
		return appdata;
	}
	@Override
	protected void itemCilck(int position) {
		String textview=appPerf[position];
		if("慎用自定义view".equals(textview)){
			Utils.intentToActivity(this, CautionCustomViewActivity.class);
		}else if("Demo".equals(textview)){
			Utils.intentToActivity(this, DemoActivity.class);
		}
		
	}
}
