package com.jh.memory;

import com.jh.memory.base.BaseActivity;
import com.jh.memory.leak.MemoryLeakMainActivity;
import com.jh.memory.optimize.MemoryOptimizeMainActivity;
import com.jh.memory.tools.MemoryToolsMainActivity;
import com.jh.memory.utils.Utils;
import com.jh.performance.threadoptimize.activity.ThreadMemoryActivity;
import com.jh.performance.threadoptimize.activity.ThreadNumInfoActivity;
import com.jh.performance.threadoptimize.activity.ThreadPriorityActivity;
/**
 * 首页
 * @author 099
 *
 */
public class MainActivity extends BaseActivity {

	@Override
	protected String[] getItemData() {
		 String[] appdata={"内存分析工具","内存泄漏常见问题","内存优化常见问题","线程内存","线程数量"};
		return appdata;
	}
	@Override
	protected void itemCilck(int position) {
		String textview=appPerf[position];
		if("内存泄漏常见问题".equals(textview)){
			Utils.intentToActivity(this, MemoryLeakMainActivity.class);
		}else if("内存优化常见问题".equals(textview)){
			Utils.intentToActivity(this, MemoryOptimizeMainActivity.class);
		}else if("内存分析工具".equals(textview)){
			Utils.intentToActivity(this, MemoryToolsMainActivity.class);
		}else if("线程内存".equals(textview)){
			Utils.intentToActivity(this, ThreadMemoryActivity.class);
		}else if("线程数量".equals(textview)){
			Utils.intentToActivity(this, ThreadNumInfoActivity.class);
		}
		
	}

}
