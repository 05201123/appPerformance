package com.jh.memory.tools;

import com.jh.memory.DemoActivity;
import com.jh.memory.base.BaseActivity;
import com.jh.memory.tools.mat.MemoryAnalyzerToolActivity;
import com.jh.memory.utils.Utils;


/**
 * 内存分析工具主页面
 * @author 099
 *
 */
public class MemoryToolsMainActivity extends BaseActivity {

	@Override
	protected String[] getItemData() {
		 String[] appdata={"Memory Analyzer Tool","Demo"};
		return appdata;
	}
	@Override
	protected void itemCilck(int position) {
		String textview=appPerf[position];
		if("Memory Analyzer Tool".equals(textview)){
			Utils.intentToActivity(this, MemoryAnalyzerToolActivity.class);
		}else if("Demo".equals(textview)){
			Utils.intentToActivity(this, DemoActivity.class);
		}
		
	}
}
