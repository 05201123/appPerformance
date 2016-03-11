package com.jh.net.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jh.exception.JHException;
import com.jh.net.bean.DomainInfoCDTO;

/**
 * 
 * 类描述： 切换服务器ip辅助数据库操作类 创建人：017 创建时间：2014-9-10 上午 修改人：017 修改时间：2014-9-10 上午
 * 修改备注：
 * 
 * @version
 * 
 */

public class SwitchIpDBHelper extends SQLiteOpenHelper {
	public static final String TAG = "db";
	/**
	 * 数据库工具类实例
	 */
	private static SwitchIpDBHelper mInstance;
	/**
	 * 数据库名称
	 */
	private static final String DB_NAME = "switchip.db";
	/**
	 * 上下文用于constructor中
	 */
	private Context context;
	private static final int VersionCode = 1;
	public static QueryResult result;

	/**
	 * construct
	 * 
	 * @param c
	 *            上下文
	 */
	private SwitchIpDBHelper(Context c) {
		super(c, DB_NAME, null, VersionCode);
		this.context = c;
		database = this.getWritableDatabase();
	}

	public static SwitchIpDBHelper getInstance(Context c) {
		if (mInstance == null) {
			synchronized (SwitchIpDBHelper.class) {
				if (mInstance == null) {
					mInstance = new SwitchIpDBHelper(c);
				}
			}
		}
		return mInstance;
	}

	/**
	 * 创建表
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE  IF NOT EXISTS " + SwitchIPTable.TABLENAME
				+ "(_id Integer PRIMARY KEY autoincrement,"
				+ SwitchIPTable.BizCode + "  Integer," + SwitchIPTable.Id
				+ " varchar(100)," + SwitchIPTable.Name + "  varchar(100),"
				+ SwitchIPTable.Domain + " varchar(100)," + SwitchIPTable.IP
				+ "  varchar(20)," + SwitchIPTable.Status + "  Integer,"
				+ SwitchIPTable.ResponseCode + "  Integer,"
				+ SwitchIPTable.UserId + "  varchar(100)" + ")";
		android.util.Log.e("sql", "sql=" + sql);
		try {
			db.execSQL(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新表
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE " + SwitchIPTable.TABLENAME);
		onCreate(db);
	}

	// 数据库
	private static SQLiteDatabase database;

	public static class SwitchIPTable {
		public static final String TABLENAME = "SwitchIPTable";
		public static final String BizCode = "BizCode";
		// / 站点编号
		public static final String Id = "Id";
		// / 站点名称
		public static final String Name = "Name";
		// / 基域名带端口
		public static final String Domain = "Domain";
		public static final String IP = "IP";
		// /0 不可用 ，1 可用
		public static final String Status = "Status";

		// ---------------------------------------
		// / 网络请求返回码(导致url标示为失败)
		public static final String ResponseCode = "ResponseCode";

		public static final String UserId = "UserId";
	}

	// -------------------------------------------------------------
	/**
	 * 数据库的思路就是在回调函数中被引用，用来对数据库操作。包括 添加记录 删除记录 修改记录
	 */

	public static class QueryResult {
		// 标示
		public int BizCode;
		// url 0 不可用，1可用
		public int Status;
		// 可用吗
		public boolean flag;

	}

	/** 查询记录 */
	public synchronized QueryResult queryCache(String domain)
			throws JHException {
		Cursor cursor = null;
		QueryResult result = new QueryResult();

		cursor = database.query(SwitchIPTable.TABLENAME, null,
				SwitchIPTable.Domain + " =?", new String[] { domain }, null,
				null, null);
		if (cursor != null && cursor.moveToNext()) {
			result.Status = cursor.getInt(cursor
					.getColumnIndex(SwitchIPTable.Status));

			// result.failCount = cursor.getInt(cursor
			// .getColumnIndex(SwitchIPTable.FailCount));
			// result.sucCount = cursor.getInt(cursor
			// .getColumnIndex(SwitchIPTable.SuccCount));

		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return result;
	}

	/**
	 * 记录查询
	 * 
	 * @param domain
	 * @return
	 * @throws JHException
	 */
	public synchronized boolean queryNeedQuestByDomain(String domain,
			String userId) throws JHException {
		Cursor cursor = null;
		String bizCode = null;

		cursor = database.query(SwitchIPTable.TABLENAME, null,
				SwitchIPTable.Domain + " =? and " + SwitchIPTable.UserId
						+ " =?", new String[] { domain, userId }, null, null,
				null);
		if (cursor != null && cursor.moveToNext()) {
			bizCode = cursor.getString(cursor
					.getColumnIndex(SwitchIPTable.BizCode));
			cursor = database.query(SwitchIPTable.TABLENAME, null,
					SwitchIPTable.BizCode + " =?", new String[] { bizCode },
					null, null, null);
			while (cursor != null && cursor.moveToNext()) {
				// while(cursor.moveToNext()){
				if (0 == cursor.getInt(cursor
						.getColumnIndex(SwitchIPTable.Status))) {
					return false;
				}
				// }
			}
			// deleteTableByBizCode(bizCode,userId);
			return true;
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		// 找不到bizcode不做处理
		return false;

	}

	/**
	 * 记录查询
	 * 
	 * @param domain
	 * @return
	 * @throws JHException
	 */
	public synchronized int queryBizCodeByDomain(String domain, String userId)
			throws JHException {
		Cursor cursor = null;
		int bizCode = 0;

		cursor = database.query(SwitchIPTable.TABLENAME, null,
				SwitchIPTable.Domain + " =? and " + SwitchIPTable.UserId
						+ " = ?", new String[] { domain, userId }, null, null,
				null);
		if (cursor != null && cursor.moveToNext()) {
			bizCode = cursor.getInt(cursor
					.getColumnIndex(SwitchIPTable.BizCode));
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return bizCode;

	}

	/**
	 * 记录查询
	 * 
	 * @param domain
	 * @return
	 * @throws JHException
	 */
	public synchronized boolean queryExistRecoder(String userId)
			throws JHException {
		Cursor cursor = null;
		int num = 0;

		cursor = database.query(SwitchIPTable.TABLENAME, null,
				SwitchIPTable.UserId + " =?", new String[] { userId }, null,
				null, null);
		if (cursor != null && cursor.moveToNext()) {
			num = cursor.getCount();
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return num>0;

	}

	/**
	 * 查询可用的domain
	 * 
	 * @param domain
	 * @return
	 * @throws JHException
	 */
	public synchronized String queryValidDomainByBizCode(String bizcode,
			String userId) throws JHException {
		Cursor cursor = null;
		String domain = null;
		cursor = database.query(SwitchIPTable.TABLENAME, null,
				SwitchIPTable.BizCode + " =? and " + SwitchIPTable.UserId
						+ " =?", new String[] { bizcode, userId }, null, null,
				null);
		while (cursor != null && cursor.moveToNext()) {
			// while(cursor.moveToNext()){
			if (0 == cursor.getInt(cursor.getColumnIndex(SwitchIPTable.Status))) {
				domain = cursor.getString(cursor
						.getColumnIndex(SwitchIPTable.Domain));
				break;
			}
			// }
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return domain;
	}

	/**
	 * 接口请求返回的数据,插入数据库中
	 * 
	 * @param bizCode
	 * @param id
	 * @param name
	 * @param domain
	 * @param ip
	 * @param code
	 */
	public synchronized void initOrInsertSwitchIPTable(int bizCode, String id,
			String name, String domain, String ip, int code, String userId) {
		ContentValues values = new ContentValues();
		values.put(SwitchIPTable.BizCode, bizCode);
		values.put(SwitchIPTable.Id, id);
		values.put(SwitchIPTable.Name, name);
		values.put(SwitchIPTable.Domain, domain);
		values.put(SwitchIPTable.IP, ip);
		// 服务器返回的都是可用的
		values.put(SwitchIPTable.Status, 0);
		values.put(SwitchIPTable.UserId, userId);

		database.insert(SwitchIPTable.TABLENAME, null, values);
		// closeDB();
	}

	public synchronized void updateStatusToFail(String domain, String userId,
			String responseCode) {
		ContentValues values = new ContentValues();
		values.put(SwitchIPTable.Status, 1);
		values.put(SwitchIPTable.ResponseCode, responseCode);

		database.update(SwitchIPTable.TABLENAME, values, SwitchIPTable.Domain
				+ " =? and " + SwitchIPTable.UserId + " =?", new String[] {
				domain, userId });
	}

	/**
	 * 删除某条url记录
	 * 
	 * @param url
	 */
	public synchronized void deleteTable(String url) {
		database.delete(SwitchIPTable.TABLENAME, SwitchIPTable.Domain + " =?",
				new String[] { url });
	}

	public synchronized void deleteFailedUrl() {
		database.delete(SwitchIPTable.TABLENAME, SwitchIPTable.Status + " =?",
				new String[] { "1" });
	}

	/**
	 * 删除某条bizcode
	 * 
	 * @param url
	 */
	public synchronized void deleteTableByBizCode(String bizCode, String userId) {
		database.delete(SwitchIPTable.TABLENAME, SwitchIPTable.BizCode
				+ " =? and " + SwitchIPTable.UserId + " =?", new String[] {
				bizCode, userId });
	}

	/**
	 * 删除某条bizcode
	 * 
	 * @param url
	 */
	public synchronized List<DomainInfoCDTO> getListDomainInfo(String bizCode,
			String userId) {
		List<DomainInfoCDTO> domainInfoDTOs = new ArrayList<DomainInfoCDTO>();
		DomainInfoCDTO domainInfoDTO;
		Cursor cursor = null;
		cursor = database.query(SwitchIPTable.TABLENAME, null,
				SwitchIPTable.BizCode + " =? and " + SwitchIPTable.UserId
						+ " =? and "+ SwitchIPTable.Status+"=? ", new String[] { bizCode, userId ,"1"}, null, null,
				null);
		while (cursor != null && cursor.moveToNext()) {
			domainInfoDTO = new DomainInfoCDTO();
			domainInfoDTO.setDomain(cursor.getString(cursor
					.getColumnIndex(SwitchIPTable.Domain)));
			domainInfoDTO.setResponseCode(cursor.getInt(cursor
					.getColumnIndex(SwitchIPTable.ResponseCode)));
			domainInfoDTOs.add(domainInfoDTO);
		}
		if (cursor != null) {
			cursor.close();
		}
		return domainInfoDTOs;
	}

}
