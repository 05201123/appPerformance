package com.jh.common.app.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.text.TextUtils;

/**
 * @author liudongsheng Create at 2012-10-10下午07:07:16
 */
public class Md5Util {
	/**
	 * MD5 加密
	 */
	public static String getMD5Str(String str) {
		MessageDigest messageDigest = null;
		try {
			if (!TextUtils.isEmpty(str)) {
				messageDigest = MessageDigest.getInstance("MD5");
				messageDigest.reset();
				messageDigest.update(str.getBytes("UTF-8"));

				byte[] byteArray = messageDigest.digest();
				StringBuffer md5StrBuff = new StringBuffer();

				for (int i = 0; i < byteArray.length; i++) {
					if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
						md5StrBuff.append("0").append(
								Integer.toHexString(0xFF & byteArray[i]));
					} else {
						md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
					}
				}
				return md5StrBuff.toString();
			}
		} catch (NoSuchAlgorithmException e) {
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getMD5String(Object... objects) {
		StringBuilder sb = new StringBuilder();
		for (Object obj : objects) {
			if (obj != null) {
				sb.append(obj.toString());
			}
		}

		String md5Str = getMD5Str(sb.toString());
		return md5Str;

	}
	
	
	/**
	 * 字符串加密  默认转小写返回值
	 * 
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	public static String toMD5(String pwd) throws Exception {
		return com.jh.util.BaseEncrypt.byteTohex(
				com.jh.util.BaseEncrypt.encryptMD5(pwd.getBytes("utf-8")))
				.toLowerCase();
	}
	

	public static void main(String[] args) {
		String md5str = getMD5String("123", "中国", "abc");
	}

}
