package com.jh.util;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
/**
 * Encrypted in several ways
 * @author xiangBC
 * @since  2012-3-27 14:20:43
 * */
public class BaseEncrypt {
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8','9', 'A', 'B', 'C', 'D', 'E', 'F' };
	/**
	 * Encrypt by MD5
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptMD5(byte[] data) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(data);
		return md5.digest();
	}

	public static String encryptSHA1(String fileName) throws Exception {
		byte[] buffer = new byte[1024];

		int numRead = 0;

		MessageDigest sha;

		try {

			FileInputStream fis = new FileInputStream(fileName);

			sha = MessageDigest.getInstance("SHA-1");

			while ((numRead = fis.read(buffer)) > 0) {
				sha.update(buffer, 0, numRead);

			}

			fis.close();
			return toHexString(sha.digest());
		} catch (Exception e) {

			

			return null;

		}
	}
	public static byte[] encryptMD5ToByte(String fileName){
		byte[] buffer = new byte[1024];

		int numRead = 0;

		MessageDigest md5;

		try {

			FileInputStream fis = new FileInputStream(fileName);

			md5 = MessageDigest.getInstance("MD5");

			while ((numRead = fis.read(buffer)) > 0) {

				md5.update(buffer, 0, numRead);

			}

			fis.close();

			return md5.digest();

		} catch (Exception e) {

			return null;

		}
	}
	public static String toHexString(byte[] b) { // 转化成16进制

		StringBuilder sb = new StringBuilder(b.length * 2);

		for (int i = 0; i < b.length; i++) {

			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);

			sb.append(HEX_DIGITS[b[i] & 0x0f]);

		}

		return sb.toString();

	}

	public static String encryptMD5(String fileName) throws Exception {
		byte [] results = encryptMD5ToByte(fileName);
		if(results!=null)
		return byteTohex(results);
		return "";
	}
	/**
	 * Encrypt by SHA 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptSHA(byte[] data) throws NoSuchAlgorithmException {
		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		sha.update(data);
		return sha.digest();
	}
	
    /**
     * Convert byte[] to hex string
     * @author xiangBC
     * @param src byte[] data
     * @return hex string
    */  
    public static String byteTohex(byte[] b)
    { 
    	String hs=""; 
    	String stmp=""; 
    	for (int n=0;n<b.length;n++) 
    	{ 
    		stmp=(java.lang.Integer.toHexString(b[n] & 0XFF)); 
    		if (stmp.length()==1)
    			hs=hs+"0"+stmp; 
    		else 
    			hs=hs+stmp; 
    	} 
    	return hs.toUpperCase(); 
    } 
    public byte[] encryDes() throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
    	// 鐢熸垚涓�釜鍙俊浠荤殑闅忔満鏁版簮
    	SecureRandom sr = new SecureRandom();
    	// 涓烘垜浠�鎷╃殑DES绠楁硶鐢熸垚涓�釜KeyGenerator瀵硅薄
    	KeyGenerator kg = KeyGenerator.getInstance ("DES" );
    	kg.init (sr);
    	// 鐢熸垚瀵嗛挜
    	SecretKey key = kg.generateKey();
    	// 鐢ㄥ瘑閽ュ垵濮嬪寲Cipher瀵硅薄
    	Cipher cipher = Cipher.getInstance( "DES" );
    	cipher.init( Cipher.ENCRYPT_MODE, key, sr );
    	// 閫氳繃璇荤被鏂囦欢鑾峰彇闇�鍔犲瘑鐨勬暟鎹�
    	byte data [] = new String("128262001060464772").getBytes("utf-8");
    	// 鎵ц鍔犲瘑鎿嶄綔
    	byte encryptedClassData[] = cipher.doFinal(data);
    	return encryptedClassData;
    }
}
