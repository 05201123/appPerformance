
package com.jh.view;

import java.io.File;

import com.jh.app.util.ImageFactory;
import com.jh.persistence.db.DBExcutor;
import com.jh.util.LogUtil;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * <code>CacheDB</code>
 * @description: TODO(缓存图片Db) 
 * @version  1.0
 * @author  liaoyp
 * @since 2012-3-10
 */
public class CacheDB {
	/**
	 * 
	 * <code>insertPic</code>
	 * @description: TODO(插入图片地址) 
	 * @param context
	 * @param httpPath
	 * @param localPath
	 * @since   2012-3-10    liaoyp
	 */
	
	public static void insertPic(Context context, String httpPath,String localPath){
//		DBHelper helper = new DBHelper(context);
//		SQLiteDatabase db = helper.getWriterHandler(context);
		ContentValues values = new ContentValues();
		values.put("HttpUrl", httpPath);
		values.put("LocalPath", localPath);
//		db.insert(DBHelper.PicDB, null, values);
		Cache.getExcutor(context).insert(Cache.CACHE, null, values);
//		db.close();
//		helper.release();
	}
	
	/**
	 * 
	 * <code>getLocalpicPath</code>
	 * @description: TODO(根据服务器地址获取本地图片地址) 
	 * @param context
	 * @param httpPath 服务器地址
	 * @return 返回null 代表本地无地址 否则存在
	 * @since   2012-3-10    liaoyp
	 */
	public static String getLocalpicPath(Context context, String httpPath){
		
//		if(httpPath == null){
//			return null;
//		}
		String localPath = null;
		DBExcutor dbUtil = Cache.getExcutor(context);
		Cursor cursor = dbUtil.query(Cache.CACHE,
				null, "HttpUrl = ?", new String[]{httpPath}, null, null, null);
		boolean b = cursor.moveToFirst();
		if(b){
			localPath =  cursor.getString(cursor.getColumnIndex("LocalPath"));
			if(localPath !=null ){
				localPath = ImageFactory.getFilePath(localPath, context);
				File file = new File(localPath);
				if( file.exists() && file.length()>0){
					
				}else{
					dbUtil.delete(Cache.CACHE, "HttpUrl = ?",  new String[]{httpPath});
					localPath = null;
				}
			}
		}else{
			LogUtil.println("com.jh.freesms.dbservice.CacheDB  getLocalpicPath  "+"无数据..............");
		}
		cursor.close();
		return localPath;
	} 
	
	public static void deletePic(Context context, String httpPath){
		
		Cache.getExcutor(context).delete(Cache.CACHE, 
				"HttpUrl = ?",  new String[]{httpPath});
	}
}
class Cache extends SQLiteOpenHelper{
	/** 
	 * DBHelper DATABASE_NAME : 鏁版嵁搴撳悕   
	 * @since  2012-02-04 Xiong Yangting
	 */
	private final static String DATABASE_NAME = "cache.db";
	/** 
	 * DBHelper DATABASE_VERSION : 鏁版嵁搴撶増鏈?  
	 * @since  2012-02-04 Xiong Yangting
	 */
	private final static int DATABASE_VERSION =1;  
	public final static String CACHE = "CACHE";
	private Cache(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
		
	}
	private static DBExcutor excutor;
	private static Context appContext;
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE  IF NOT EXISTS " +
				CACHE+
		  			"(_id Integer PRIMARY KEY autoincrement,"+
		  			"HttpUrl varchar(32),"+
		  			"LocalPath varchar(32))";
		    db.execSQL(sql);
		    sql = "CREATE index belong_picture on "+ 
		    		CACHE +"(HttpUrl)";
		    db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	public static DBExcutor getExcutor(Context context)
	{
		appContext = context.getApplicationContext();
		if(excutor!=null)
		{
			return excutor;
		}
		else
		{
			synchronized (appContext) {
				if(excutor!=null)
				{
					
				}
				else
				{
					excutor = new DBExcutor(new Cache(appContext).getWritableDatabase());
				}
				return excutor;
			}
		}
	}
}
