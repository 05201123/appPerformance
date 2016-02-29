package com.jh.memory.utils;

import android.content.Context;
import android.content.Intent;
/**
 * 
 * @author 099
 *
 */
public class Utils {
	
	public static void intentToActivity(Context context, Class<?> cls){
		Intent intent =new Intent(context, cls);
		context.startActivity(intent);
	}
}
