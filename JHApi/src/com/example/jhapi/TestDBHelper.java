package com.example.jhapi;

import java.util.concurrent.RejectedExecutionException;

import com.jh.net.JHIOException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TestDBHelper extends SQLiteOpenHelper {
    /**
     * ��ݿ����
     */
    private static final String DB_NAME = "newsmessage.db";
	 /**
     * ������
     */
    private Context context;
    /**
     * ����
     */
    public static final String TABLE_NAME = "newsmessage";
    /**
     * construct
     * 
     * @param c
     *            ������
     */
    public TestDBHelper(Context c) {
        super(c, DB_NAME, null, 2);
        this.context = c;
    }
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		 final String sqlString = "CREATE TABLE  IF NOT EXISTS " + TABLE_NAME
	                + " (id varchar(50),content varchar(512),time bigint );";
	        try {
	            db.execSQL(sqlString);

	        } catch (NullPointerException e) {
	            e.printStackTrace();
	        } catch (IndexOutOfBoundsException e) {
	            e.printStackTrace();
	        } catch (RejectedExecutionException e) {
	            e.printStackTrace();
	        } catch (JHIOException e) {
	            e.printStackTrace();
	        }
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
}
