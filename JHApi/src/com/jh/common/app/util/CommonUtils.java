/*
 * @project aboutComponent
 * @package com.jh.app.util
 * @version 3.0
 * @author wangzhiqiang
 * @time 2013-5-9 上午2:48:51 CopyRight:北京金和软件信息技术有限公司 2013-5-9
 */

package com.jh.common.app.util;

import java.io.File;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.jh.common.app.application.AppSystem;

/**
 * 公共工具类
 * 
 * @author wangzhiqiang
 * @createTime 2013-5-9
 */
public class CommonUtils {

	 //样式默认值
    public static final int INIT_INDEX = -1;
    //未初始化状态
    private static final int UNINIT_INDEX = -2;
    private static int themeIndex = UNINIT_INDEX;
    //
    
	/**
     * 创建下载app保存路径 File
     */

    public static File getSaveFile(String url, boolean mkdirWhenNotExist) {
        return new File(getSaveFilePath(url, mkdirWhenNotExist));
    }

    /**
     * 获取文件路径
     * 
     * @param url 文件路徑
     * @param mkdirWhenNotExist 当文件不存在时是否创建该文件，ture：创建 false：不创建
     * @since 2013-5-25 wangzhiqiang
     */
    public static String getSaveFilePath(String url, boolean mkdirWhenNotExist) {
        if (TextUtils.isEmpty(url))
            return "";
        String savedUrl = url.substring(url.lastIndexOf("\\") + 1, url.length()).replace("?", "_").replace("=", "_");
        String[] urlStr = savedUrl.split("/");
        String fileName = urlStr[urlStr.length - 1];
        File tmpFile = new File(AppSystem.getInstance().getDownloadPath());
        if (!tmpFile.exists() && mkdirWhenNotExist) {
            tmpFile.mkdirs();
        }
        return AppSystem.getInstance().getDownloadPath() + fileName;
    }

    /**
     * 关闭EditText的软键盘
     * 
     * @param editText
     * @since 2013-6-6 gaobingbing
     */
    public static void hideSoftInputFromWindow(EditText editText) {
        InputMethodManager imm = (InputMethodManager) AppSystem.getInstance().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 启动一个应用
     * 
     * @param mContext Context
     * @param packageName 应用包名
     * @throws Exception 空指针异常
     * @since 2013-7-2
     */
    public static void startApplication(Context mContext, String packageName) throws Exception {
        PackageManager packageManager = mContext.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        mContext.startActivity(intent);
    }

    /**
     * 将传入字节转换成kb、mb
     * 
     * @param size 传入大小
     * @return
     */
    public static String getFileSize(Long size) {
        if (size == null) {
            return "0M";
        } else if (size > 1073741824) {
            return String.format("%.2f", size / 1073741824.0) + " GB";
        } else if (size > 1048576) {
            return String.format("%.2f", size / 1048576.0) + " MB";
        } else if (size > 1024) {
            return String.format("%.2f", size / 1024.0) + " KB";
        } else {
            return size + " B";
        }
    }

    public static String getDownlaodSize(Long size) {
        if (size == null) {
            return "0人下载";
        } else if (size > 100 * 10000) {
            return String.format("%.2f", size / (100 * 10000.0)) + "百万人下载";
        } else if (size >= 10000) {
            return String.format("%.2f", size / 10000.0) + "万人下载";
        } else {
            return size + "人下载";
        }
    }

    /**
     * 截取字符串
     * 
     * @param str 传入的串
     * @param position 截取的位置
     * @return
     */
    public static String getSubString(String str, int position) {
        if (str == null) {
            return "";
        }
        if (str.length() > position) {
            str = str.substring(0, position - 1) + "...";
        }
        return str;
    }

    /**
     * 图片变圆角方法.
     * 
     * @param bitmap lxl 20130707.
     * @param pixels
     * @return
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        if (bitmap != null) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            int color = 0xff424242;
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            RectF rectF = new RectF(rect);
            float roundPx = pixels;
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            return output;
        } else {
            return null;
        }
    }
	/**
	 * 释放Bitmap资源
	 * 
	 * @param bitmap
	 *            Bitmap
	 */
	public static void recycleBitmap(Bitmap bitmap) {
		if (bitmap != null && bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
	}
	/**
	 * 释放资源
	 * 
	 * @param view
	 *            View
	 */
	public static void releaseResources(View view) {
		if (view != null) {
			if (view instanceof ListView) {
				((ListView) view).setAdapter(null);
			}
			view.setVisibility(View.GONE);
			view.destroyDrawingCache();
			view = null;
		}
	}
	
	
	
	
	
	
	/**
     * @description: TODO(获取皮肤的Index)
     * @param: context  上下文
     * */
    public static int getThemeIndex(Context context)
    {
    	if(themeIndex<=UNINIT_INDEX){
    		SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("jhNews", Context.MODE_WORLD_WRITEABLE);
    		themeIndex=sharedPreferences.getInt("themeIndex", INIT_INDEX);
    		if(themeIndex<=INIT_INDEX){
	    		String th=AppSystem.getInstance().readXMLFromAssets("appId.xml", "defaultThemeId");
	        	if(!TextUtils.isEmpty(th))
	        	{
	        		try
	        		{
	        			themeIndex=Integer.parseInt(th);
	        		}catch(NumberFormatException ex)
	        		{
	        		}
	        	}
    		}
    		
    	}
    	
         return themeIndex;
    //	return themeIndex;
    }
    /**
     * 修改皮肤，只做修改，不做保存
     * @param index
     */
    public static void setThemeIndex(int index){
    	{
    		themeIndex = index;
    	}
    }
    
    /**
     * @description: TODO(保存皮肤的Index，不修改内存中的Index,需要实现PublicApplication的changeToResId方法，如果保存的直接是样式的resId，就不需要实现)
     * @param: context  上下文
     * @param: index  皮肤的Index
     * */
    public static void saveThemeIndex(Context context,int index)
    {
    	//if(0<index&&index<5)
        {
    		SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("jhNews", Context.MODE_WORLD_WRITEABLE);
            Editor editor = sharedPreferences.edit();//获取编辑器
            editor.putInt("themeIndex", index);
            editor.commit();//提交修改
          //  themeIndex = index;
        }
    }
    
    /**
     * 转换dp和像素点
     * 
     * @param context
     *            上下文
     * @param dp
     *            dp
     * @return 像素点
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5);
    }

    /**
     * 转换dp和像素点
     * 
     * @param context
     *            上下文
     * @param px
     *            像素点
     * @return dp
     */
    public static int px2dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5);
    }

    /**
     * 获取屏幕的大小
     * @param context
     * @return
     */
    public static Screen getScreenPix(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE); 
        windowManager.getDefaultDisplay().getMetrics(dm);
        return new Screen(dm.widthPixels, dm.heightPixels);
    }

    public static class Screen {
        public int widthPixels;
        public int heightPixels;

        public Screen() {

        }

        public Screen(int widthPixels, int heightPixels) {
            this.widthPixels = widthPixels;
            this.heightPixels = heightPixels;
        }

        @Override
        public String toString() {
            return "(" + widthPixels + "," + heightPixels + ")";
        }
    }
    
}
