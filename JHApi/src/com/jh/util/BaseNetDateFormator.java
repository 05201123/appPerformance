package com.jh.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseNetDateFormator {
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy/m/d hh:MM:ss");
	public static String getCurrentTime()
	{
		return format.format(new Date());
	}
}
