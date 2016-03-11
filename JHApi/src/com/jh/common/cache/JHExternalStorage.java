package com.jh.common.cache;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import android.R.integer;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import com.jh.exception.JHException;
import com.jh.persistence.file.ExternalStorageState;

public class JHExternalStorage {
	/*
	 *
	 * 外部存储器状态类.
	 *
	 * @class SdUtil
	 * @version  1.0
	 * @author  liaoyp
	 * @time  2012-5-30 上午1:38:45
	 */
	public static final String SD_CARD = "sdCard";
	public static List<String> getReadableStorage(){
		List<String> storages = getAvailableStorage();
		for(String storage:storages)
		{
			File file = new File(storage);
			if(!file.canRead()){
				storages.remove(storage);
			}
		}
		return storages;
	}
	public static class ExternalStorageFullException extends JHException{

		@Override
		public String getMessage() {
			// TODO Auto-generated method stub
			return "外置存储卡已满";
		}
		
	}
	public static class ExternalInvalidException extends JHException{

		@Override
		public String getMessage() {
			// TODO Auto-generated method stub
			return "外置存储卡不可用";
		}
		
	}
	public static List<String> getWriteableStorage(){
		List<String> storages = getAvailableStorage();
		for(String storage:storages)
		{
			File file = new File(storage);
			if(!file.canWrite()){
				storages.remove(storage);
			}
		}
		return storages;
	}
	public static List<String> getAvailableStorage(){
		List<String> mMounts = new ArrayList<String>(10);
       List<String> mVold = new ArrayList<String>(10);

		 try {
	            File mountFile = new File("/proc/mounts");
	            if(mountFile.exists()){
	                Scanner scanner = new Scanner(mountFile);
	                while (scanner.hasNext()) {
	                    String line = scanner.nextLine();
	                    if (!line.contains("/mnt/secure/asec")&&(line.startsWith("/dev/block/vold/")||line.startsWith("/dev/fuse")) ) {
	                    	
	                        String[] lineElements = line.split(" ");
	                        String element = lineElements[1];

	                        // don't add the default mount path
	                        // it's already in the list.
	                      //  if (!element.equals("/mnt/sdcard"))
	                            mMounts.add(element);
	                    }
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	/*	 try {
	            File voldFile = new File("/system/etc/vold.fstab");
	            if(voldFile.exists()){
	                Scanner scanner = new Scanner(voldFile);
	                while (scanner.hasNext()) {
	                    String line = scanner.nextLine();
	                    System.out.println("scanner===="+line);
	                    if (line.startsWith("dev_mount")) {
	                        String[] lineElements = line.split(" ");
	                        String element = lineElements[2];

	                        if (element.contains(":"))
	                            element = element.substring(0, element.indexOf(":"));
	                        if (!element.equals("/mnt/sdcard")&&!mMounts.contains(element))
	                            mVold.add(element);
	                    }
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }*/
	/*	 for (int i = 0; i < mMounts.size(); i++) {
	            String mount = mMounts.get(i);
	            if (!mVold.contains(mount))
	                mMounts.remove(i--);
	        }*/
		 mVold.addAll(mMounts);
		 return mVold;
/*	        mVold.clear();

	        List<String> mountHash = new ArrayList<String>(10);

	        for(String mount : mMounts){
	            File root = new File(mount);
	            if (root.exists() && root.isDirectory() && root.canWrite()) {
	                File[] list = root.listFiles();
	                String hash = "[";
	                if(list!=null){
	                    for(File f : list){
	                        hash += f.getName().hashCode()+":"+f.length()+", ";
	                    }
	                }
	                hash += "]";
	                if(!mountHash.contains(hash)){
	                    String key = SD_CARD + "_" + map.size();
	                    if (map.size() == 0) {
	                        key = SD_CARD;
	                    } else if (map.size() == 1) {
	                        key = EXTERNAL_SD_CARD;
	                    }
	                    mountHash.add(hash);
	                    map.put(key, root);
	                }
	            }
	        }

	        mMounts.clear();*/

	}
	/**
	 * 是否存在外部存储器
	 * @return
	 */
	public  static  boolean  isVisible(){
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// Something else is wrong. It may be one of many other states, but
			// all we need
			// to know is we can neither read nor write
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		return mExternalStorageWriteable ;
	}
	/**
	 * 是否可读
	 * @return
	 */
	public static boolean canRead()
	{
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			return true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			return true;
		} else
		{
			return false;
		}
	}
	/**
	 * 是否可写
	 * @return
	 */
	public static boolean canWrite()
	{
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			return true;
		}else
		{
			return false;
		}
	}
	/**
	 * 获取可用空间
	 * @return 如果返回值为-1，表示不存在sdcard。
	 */
	public static long getAvailableMemory(){
		if(canRead())
		{
			File path = Environment.getExternalStorageDirectory(); //取得sdcard文件路径
			StatFs stat = new StatFs(path.getPath()); 
			long blockSize = stat.getBlockSize(); 
			long availableBlocks = stat.getAvailableBlocks(); 
			return availableBlocks * blockSize; 
		}
		return -1;
	}
	private static final int MAXREMAIN = 5*1024*1024;
	public static boolean isFull(){
		return MAXREMAIN>getAvailableMemory();
	}
	public static boolean isFull(long size){
		return size>getAvailableMemory();
	}
}
