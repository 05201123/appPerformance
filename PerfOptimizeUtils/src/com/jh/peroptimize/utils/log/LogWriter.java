package com.jh.peroptimize.utils.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.util.Log;

public class LogWriter {
	private static final String TAG = "LogWriter";

    private static final Object SAVE_DELETE_LOCK = new Object();
    private static final SimpleDateFormat FILE_NAME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss.SSS");
    private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final long OBSOLETE_DURATION = 2 * 24 * 3600 * 1000;
    
    private long lastSaveTime=0;

    public static String saveLooperLog(Context context,String localPath, String str) {
        String path=null;
        synchronized (SAVE_DELETE_LOCK) {
        	saveLogToSDCard(context,localPath,"threadinfo",str);
        }
        return path;
    }


    private static String saveLogToSDCard(Context context,String localPath,String logFileName, String str) {
        String path = "";
        BufferedWriter writer = null;
        try {
            File file = detectedLeakDirectory(localPath);
            long time = System.currentTimeMillis();
            path = file.getAbsolutePath() + "/" + logFileName + "-" + FILE_NAME_FORMATTER.format(time) + ".txt";
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(path, true), "UTF-8");

            writer = new BufferedWriter(out);
            writer.write("\r\n**********************\r\n");
            writer.write(TIME_FORMATTER.format(time) + "(write log time)");
            writer.write("\r\n");
            writer.write("\r\n");
            writer.write(str);
            writer.write("\r\n");
            writer.flush();
            writer.close();
            writer = null;
        } catch (Throwable t) {
            Log.e(TAG, "saveLogToSDCard: ", t);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                    writer = null;
                }
            } catch (Exception e) {
                Log.e(TAG, "saveLogToSDCard: ", e);
            }
        }
        return path;
    }
    
    public static String getPath(String localPath) {
        String state = android.os.Environment.getExternalStorageState();
        if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {
            if (android.os.Environment.getExternalStorageDirectory().canWrite()) {
                return android.os.Environment.getExternalStorageDirectory().getPath()
                        + localPath;
            }
        }
        return android.os.Environment.getDataDirectory().getAbsolutePath() + localPath;
    }

    public static File detectedLeakDirectory(String localPath) {
        File directory = new File(getPath(localPath));
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory;
    }
    
}
