package com.jh.persistence.file;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 封装sharePreference的操作，所有对sharePreference的操作必须都继承该文件。
 * @author jhzhangnan1
 *
 */
public class PreferencesWrapper {
	
	private SharedPreferences preference;
	private Editor editor;
	protected PreferencesWrapper(String name,Context context)
	{
		preference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		editor = preference.edit();
	}
	/**
	 * 保存并持久化到硬盘
	 * @param key
	 * @param value
	 */
	public String getString(String key, String defValue)
	{
		return preference.getString(key, defValue);
	}
	/**
	 * 保存并持久化到硬盘
	 * @param key
	 * @param value
	 */
	public int getInt(String key, int defValue)
	{
		return preference.getInt(key, defValue);
	}
	/**
	 * 保存并持久化到硬盘
	 * @param key
	 * @param value
	 */
	public long getLong(String key, long defValue)
	{
		return preference.getLong(key, defValue);
	}
	/**
	 * 保存并持久化到硬盘
	 * @param key
	 * @param value
	 */
	public float getFloat(String key, float defValue)
	{
		return preference.getFloat(key, defValue);
	}
	/**
	 * 保存并持久化到硬盘
	 * @param key
	 * @param value
	 */
	public boolean getBoolean(String key, boolean defValue)
	{
		return preference.getBoolean(key, defValue);
	}
	/**
	 * 保存并持久化到硬盘
	 * @param key
	 * @param value
	 */
	public void saveInt(String key,int value)
	{
		editor.putInt(key, value).commit();
	}
	/**
	 * 保存并持久化到硬盘
	 * @param key
	 * @param value
	 */
	public void saveBoolean(String key,boolean value)
	{
		editor.putBoolean(key, value).commit();
	}
	/**
	 * 保存并持久化到硬盘
	 * @param key
	 * @param value
	 */
	public void saveString(String key,String value)
	{
		editor.putString(key, value).commit();
	}
	/**
	 * 保存并持久化到硬盘
	 * @param key
	 * @param value
	 */
	public void saveLong(String key,long value)
	{
		editor.putLong(key, value).commit();
	}
	
	public boolean contains(String key)
	{
		return preference.contains(key);
	}
	/**
	 * 更新内存，最后需要调用commit
	 * @param key
	 * @param value
	 */
	public void putInt(String key,int value)
	{
		editor.putInt(key, value);
	}
	/**
	 * 更新内存，最后需要调用commit
	 * @param key
	 * @param value
	 */
	public void putBoolean(String key,boolean value)
	{
		editor.putBoolean(key, value);
	}
	/**
	 * 更新内存，最后需要调用commit
	 * @param key
	 * @param value
	 */
	public void putString(String key,String value)
	{
		editor.putString(key, value);
	}
	/**
	 * 更新内存，最后需要调用commit
	 * @param key
	 * @param value
	 */
	public void remove(String key){
		editor.remove(key);
	}
	public void clear()
	{
		editor.clear().commit();
	}
	public void commit()	
	{
		editor.commit();
	}
}
