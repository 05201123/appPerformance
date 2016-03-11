package com.jh.persistence.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jh.app.util.AllTaskFinish;
import com.jh.app.util.RunnableExecutor;
import com.jh.persistence.db.DBExcutor.InsertCallBack;
import com.jh.persistence.db.DBExcutor.TranctionTask;

public class DBExecutorHelper {
	private SQLiteOpenHelper dbHelper;
	private RunnableExecutor executor;
	private SQLiteDatabase dbwriter;
	public DBExecutorHelper(SQLiteOpenHelper dbHelper)
	{
		this.dbHelper = dbHelper;
		executor = RunnableExecutor.newInstance(1);
		executor.setTaskFinishCallBack(new AllTaskFinish() {
			
			@Override
			public void finish() {
				// TODO Auto-generated method stub
				if(dbwriter!=null)
				{
					close();
				}
				
			}
		});
	}
	public void close(){
		executor.executeTask(new Runnable(){
			
			@Override
			public void run() {
				Log.i("closeDb", "closeDb");
				closeDb();
			}});
	}
	private synchronized void closeDb(){
		if(dbwriter!=null)
		{
			if(dbwriter.isOpen())
			{
				dbwriter.close();
			}
			dbwriter = null;
		}
	}
	private SQLiteDatabase getDbWriter(){
		if(dbwriter!=null&&dbwriter.isOpen()){
			
		}
		else{
			synchronized (this) {
				if(dbwriter!=null&&dbwriter.isOpen()){
					
				}
				else{
					dbwriter = dbHelper.getWritableDatabase();
				}
			}
		}
		return dbwriter;
	}
	/**
	 * <code>delete</code>
	 * @description: TODO(执行删除操作) 
	 * @param table
	 * @param whereClause
	 * @param whereArgs
	 * @since   2012-7-18    yourname
	 */
	public void delete(final String table,final String whereClause,final String[] whereArgs)
	{
		executor.executeTask(new TranctionTask(){

			@Override
			public void excuteTranction(SQLiteDatabase dbwriter) {
				// TODO Auto-generated method stub
				
				dbwriter.delete(table, whereClause, whereArgs);
			}});
	}
	/**
	 * <code>TranctionTask</code>
	 * @description: TODO(事务执行任务�? 
	 * @version  1.0
	 * @author  yourname
	 * @since 2012-4-5
	 */
	public interface FinishDBTask
	{
		public void finish(Object result);
	}
	/**
	 * 用于返回结果
	 * @author jhzhangnan1
	 *
	 */
	public abstract class QueryCallBack  implements FinishDBTask{
	    
	    public Object object;

	}
	/**
	 * <code>asynTranction</code>
	 * @description: TODO(执行数据库任务,查询操作在该程序中执行)  
	 * @param task
	 * @since   2012-7-18    yourname
	 */
	public void excute(TranctionTask task)
	{
		executor.executeTask(task);
	}
	/**
	 * <code>excuteRawsql</code>
	 * @description: TODO(执行原生sql)
	 * @since   2012-7-18    yourname
	 */
	public void excuteRawsql(final String sql)
	{
		executor.executeTask(new TranctionTask() {

			@Override
			public void excuteTranction(SQLiteDatabase dbwriter) {
				// TODO Auto-generated method stub
				dbwriter.execSQL(sql);
			}
		});
	}
	public abstract class TranctionTask implements Runnable
	{
		private FinishDBTask finishTask; 
		private Object result;
		private boolean finished = false;
		public abstract void excuteTranction(SQLiteDatabase dbwriter) throws Exception;
		public void finish(){
			if(finishTask!=null)
			{
				finishTask.finish(result);
			}
			finished = true;
		};
		public TranctionTask()
		{
			
		}
		public TranctionTask(FinishDBTask finishTask)
		{
			this.finishTask = finishTask;
		}
		@Override
		public final void run() {
			SQLiteDatabase dbwriter = getDbWriter();
			dbwriter.beginTransaction();
			try {
				excuteTranction(dbwriter);
				if(dbwriter.inTransaction())
				{
					dbwriter.setTransactionSuccessful();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(dbwriter.inTransaction())
			{
				dbwriter.endTransaction();
			}
			finish();
		}
		public void setResult(Object result) {
			this.result = result;
		}
	}
	public interface InsertCallBack {
		public void success(long id);
	}
	public void insert(final String table,final  String nullColumnHack,final  ContentValues values){
		insert(table,nullColumnHack,values,null);
	}
	/**
	 * 阻塞线程来执行。
	 * @param task
	 * @return
	 */
	public Object executeBlock(final TranctionTask task){
		final Object object = new Object();
		synchronized (object) {
			try {
				executor.executeTask(new TranctionTask() {

					@Override
					public void excuteTranction(SQLiteDatabase dbwriter) {
						// TODO Auto-generated method stub
						synchronized (object) {
							try {
								task.excuteTranction(dbwriter);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							object.notify();
						}

					}
				});

				object.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return task.result;
		}
	}
	/**
	 * <code>query</code>
	 * @description: TODO(查询操作) 
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return
	 * @since   2012-7-18    yourname
	 
	public synchronized Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
	{
		return query(table, columns, selection, selectionArgs, groupBy, having, orderBy,null);
	}*/
	/**
	 * <code>query</code>
	 * @description: TODO(查询操作) 
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @param limit
	 * @return
	 * @since   2012-7-18    yourname
	 
	public synchronized Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)
	{
		return getDbWriter().query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	}*/
	/**
	 * <code>insert</code>
	 * @description: TODO(执行插入操作) 
	 * @param table
	 * @param nullColumnHack
	 * @param values
	 * @since   2012-7-18    yourname
	 */
	public void insert(final String table,final  String nullColumnHack,final  ContentValues values,final InsertCallBack callBack)
	{
		executor.executeTask(new TranctionTask() {

			@Override
			public void excuteTranction(SQLiteDatabase dbwriter) {
				// TODO Auto-generated method stub
				long id = dbwriter.insert(table, nullColumnHack, values);
				if (callBack != null) {
					callBack.success(id);
				}
			}
		});

	}
	/**
	 * <code>query</code>
	 * @description: TODO(查询操作) 
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return
	 * @since   2012-7-18    yourname
	
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
	{
		return query(table, columns, selection, selectionArgs, groupBy, having, orderBy,null);
	} */
	/**
	 * <code>query</code>
	 * @description: TODO(查询操作) 
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @param limit
	 * @return
	 * @since   2012-7-18    yourname
	
	public synchronized Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)
	{
		SQLiteDatabase dbwriter = getDbWriter();
		return dbwriter.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	} */

	/**
	 * <code>update</code>
	 * @description: TODO(执行更新操作) 
	 * @param table
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @since   2012-7-18    yourname
	 */
	public void update(final String table,final ContentValues values,final String whereClause,final String[] whereArgs)
	{
		executor.executeTask(new TranctionTask() {

			@Override
			public void excuteTranction(SQLiteDatabase dbwriter) {
				// TODO Auto-generated method stub
				dbwriter.update(table, values, whereClause, whereArgs);
			}
		});
	}
	/**
	 * <code>rawQuery</code>
	 * @description: TODO(执行查询操作) 
	 * @param sql
	 * @param selectionArgs
	 * @return
	 * @since   2012-7-18    yourname
	 
	public synchronized Cursor rawQuery(final String sql,final String[] selectionArgs)
	{
		SQLiteDatabase dbwriter = getDbWriter();
		Cursor cursor = null;
//		synchronized (dbwriter) {
//			if (!dbwriter.inTransaction()) {
				cursor = dbwriter.rawQuery(sql, selectionArgs);
//			}
//		}
		return cursor;
	}*/
}
