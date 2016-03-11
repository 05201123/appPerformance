package com.jh.persistence.db;

import android.database.sqlite.SQLiteOpenHelper;

public class DBWrapper {

	private SQLiteOpenHelper dbHelper;
	public DBWrapper(SQLiteOpenHelper dbHelper) {
		super();
		this.dbHelper = dbHelper;
	}
	public static DBWrapper getSession(SQLiteOpenHelper dbHelper){
		return new DBWrapper(dbHelper);
	}
	
}
