package com.jh.androidTraining.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jh.androidTraining.application.AndroidTrainingApplication;

import android.os.Environment;

/**
 * 写日志的Utils
 * @author 099
 *
 */
public class WriteLogUtils {
	public static void writeLogSDCard(String content){
		//如果SD卡不存在或无法使用
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        	return;
        }
        File dir = new File(getFilePath());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //以当前时间创建log文件
        File file = new File(getFilePath() + time + ".log");
        try {
        	PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file,true)),true);
            pw.println(content);
            pw.close();
        } catch (Exception e) {
        }
	}
	private static String getFilePath(){
		return Environment.getExternalStorageDirectory().getPath() + "/" + getPackageName()+"/androidtraindinglog/";
	};
	private static String getPackageName() {
		if(AndroidTrainingApplication.getPublicContext()!=null){
			return AndroidTrainingApplication.getPublicContext().getPackageName();
		}
		return "ZPH";
	}
}
