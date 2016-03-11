package com.jh.persistence.db;


/*
 * @project Tingche

 * @package com.park.util
 * @file DBUtil.java
 * @version  1.0
 * @author  yourname
 * @time  2012-4-5 下午7:45:09
 * CopyRight:北京金和软件信息�?��有限公司 2012-4-5
 */



import com.jh.app.util.AllTaskFinish;
import com.jh.app.util.RunnableExecutor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * <code>DBExcutor</code>
 * @description: TODO(用于执行数据库命令的封装) ,请使用DBExecutorHelper。如果数据库一直不掉用close方法，没有问题，否则会出现多线程问题。
 * @version  1.0
 * @author  张楠
 * @since 2012-7-18
 */
@Deprecated()
public class DBExcutor{
	private SQLiteDatabase dbwriter;
	public SQLiteDatabase getDbwriter() {
		return dbwriter;
	}
//	private SerialTask excutor;
	private RunnableExecutor executor;
	public DBExcutor(SQLiteDatabase dbwriter)
	{
		this.dbwriter = dbwriter;
		executor = RunnableExecutor.newInstance(1);
	}
	public void setTaskFinishCallBack(AllTaskFinish allFinish){
		executor.setTaskFinishCallBack(allFinish);
	}
/*	public boolean isBusy()
	{
		if(excutor!=null&&excutor.)
	}*/
	/**
	 * <code>asynTranction</code>
	 * @description: TODO(执行数据库任务) 
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
		if(dbwriter.inTransaction())
		{
			executor.executeTask(new TranctionTask(){

				@Override
				public void excuteTranction(SQLiteDatabase dbwriter) {
					// TODO Auto-generated method stub
					dbwriter.execSQL(sql);
				}});
		}
		else
		{
			synchronized (dbwriter)
			{
				if(!dbwriter.inTransaction())
				{
					dbwriter.execSQL(sql);
				}
			}
		}
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
		if(dbwriter.inTransaction())
		{
			executor.executeTask(new TranctionTask(){

				@Override
				public void excuteTranction(SQLiteDatabase dbwriter) {
					// TODO Auto-generated method stub
					dbwriter.delete(table, whereClause, whereArgs);
				}});
		}
		else
		{
			synchronized (dbwriter)
			{
				if(!dbwriter.inTransaction())
				{
					dbwriter.delete(table, whereClause, whereArgs);
				}
			}
		}
	}
	/**
	 * <code>rawQuery</code>
	 * @description: TODO(执行查询操作) 
	 * @param sql
	 * @param selectionArgs
	 * @return
	 * @since   2012-7-18    yourname
	 */
	public Cursor rawQuery(final String sql,final String[] selectionArgs)
	{
		Cursor cursor = null;
//		synchronized (dbwriter) {
//			if (!dbwriter.inTransaction()) {
				cursor = dbwriter.rawQuery(sql, selectionArgs);
//			}
//		}
		return cursor;
	}
	
	public interface InsertCallBack {
		public void success(long id);
	}
	public void insert(final String table,final  String nullColumnHack,final  ContentValues values){
		insert(table,nullColumnHack,values,null);
	}
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
		if(dbwriter.inTransaction())
		{
			executor.executeTask(new TranctionTask(){

				@Override
				public void excuteTranction(SQLiteDatabase dbwriter) {
					// TODO Auto-generated method stub
					long id= dbwriter.insert(table, nullColumnHack, values);
					if(callBack!=null){
						callBack.success(id);
					}
				}});
		}
		else
		{
			synchronized (dbwriter)
			{
				if(!dbwriter.inTransaction())
				{
					long id= dbwriter.insert(table, nullColumnHack, values);
					if(callBack!=null){
						callBack.success(id);
					}
				}
			}
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
	 */
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
	{
		return query(table, columns, selection, selectionArgs, groupBy, having, orderBy,null);
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
	 * @param limit
	 * @return
	 * @since   2012-7-18    yourname
	 */
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy,String limit)
	{
		return dbwriter.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	}

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
		if(dbwriter.inTransaction())
		{
			executor.executeTask(new TranctionTask(){

				@Override
				public void excuteTranction(SQLiteDatabase dbwriter) {
					// TODO Auto-generated method stub
					dbwriter.update(table, values, whereClause, whereArgs);
				}});
		}
		else
		{
			synchronized (dbwriter)
			{
				if(!dbwriter.inTransaction())
				{
					dbwriter.update(table, values, whereClause, whereArgs);
				}
			}
		}
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
	public abstract class TranctionTask implements Runnable
	{
		private FinishDBTask finishTask; 
		public abstract void excuteTranction(SQLiteDatabase dbwriter) throws Exception;
		public void finish(){
			if(finishTask!=null)
			{
				finishTask.finish(null);
			}
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
	}
	public  void close()
	{
		executor.executeTask(new Runnable(){
	
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(dbwriter!=null)
					{
						if(dbwriter.isOpen())
						{
							dbwriter.close();
						}
						dbwriter = null;
					}
					executor.cancel();
				}});
		
	}
//	public void beginTransaction(){
//		dbwriter.beginTransaction();
//	}
//	
//	public void setTransactionSuccessful(){
//		dbwriter.setTransactionSuccessful();
//	}
//	public void endTransaction(){
//		dbwriter.endTransaction();
//	}
}

