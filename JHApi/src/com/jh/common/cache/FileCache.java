/*
 * @project PublicComponent
 * @package com.jh.common.cache
 * @version 3.0
 * @author wangzhiqiang
 * @time 2013-5-25 下午6:24:14 CopyRight:北京金和软件信息技术有限公司 2013-5-25
 */
package com.jh.common.cache;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.text.TextUtils;


import com.jh.common.app.application.AppSystem;
import com.jh.common.app.util.Md5Util;
import com.jh.common.cache.JHExternalStorage.ExternalInvalidException;
import com.jh.common.cache.JHExternalStorage.ExternalStorageFullException;
import com.jh.persistence.file.ExternalStorageState;
import com.jh.persistence.file.FileUtil;

/**
 * 文件緩存模型
 * 
 * @version v3.0
 * @author wangzhiqiang
 * @since v3.0
 * @createTime 2013-5-25
 */
public class FileCache {

    static final String IMAGE_PATH = "image/";

    static final String AUDIO_PATH = "audio/";

    static final String TEMP_PATH = "temp/";
    static final String VEDIO_PATH = "video/";
    static final String COMPRESSIONIMAGE_PATH = "freeSmsData/";

    private Set<String> fileDirPathSet = new HashSet<String>();
    /**
     * mInstance
     */
    private static FileCache mInstance;
    /**
     * cacheDir
     */
    private File cacheDir;
    public FileCache() {
        for (FileEnum fileEnum : FileEnum.values()) {
            fileDirPathSet.add(getAbsoluteFile(fileEnum));
        }
    }
    /**
     * 构造方法
     * 
     * @param context
     *            上下文
     */
    public FileCache(Context context) {
        // Find the dir to save cached images
    	this();
        if (ExternalStorageState.canWrite()) {
            cacheDir = new File(AppSystem.getInstance().getExternalPath(false));
        } else {
            cacheDir = context.getCacheDir();
        }
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
    }
    /**
     * 删除文件
     * 
     * @param id
     *            文件id
     * @return boolean 是否删除成功
     */
    public boolean delete(String id) {
        return this.getFile(id).delete();
    }

    /**
     * 获取缓存的文件大小
     * @return long
     */
    public long getFileCacheSize(){
        long l=0;
        final File[] files = cacheDir.listFiles();
        if(files!=null&&files.length>0){
        	 for(File f:files){
                 if(f.exists()){
                     long length=f.length();
                     l+=length;
                 }
             }
        }
        return l;
    }
    /**
     * 通过url路径获取文件对象
     * 
     * @param url
     *            ut路劲l
     * @return file
     */
    public File getFile(String url) {
        // I identify images by hashcode. Not a perfect solution, good for the
        // demo.
        if(!TextUtils.isEmpty(url))
        {
            final String filename = String.valueOf(Md5Util.getMD5Str(url));
            // Another possible solution (thanks to grantland)
            // String filename = URLEncoder.encode(url);
            final File f = new File(cacheDir, filename);
            return f;
        }
        return null;
    }
    /**
     * 单例，获取对象
     * 
     * @param context
     *            上下文
     * @return FileCache
     */
    public static FileCache getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new FileCache(context);
        }
        return mInstance;
    }
     String getAbsoluteFile(FileEnum fileEnum) {
    	 //2016-1-22 lijie App里面看的图片不会在相册里面出现
    	 String localDirPath = null;
    	 if(fileEnum.equals(FileEnum.IMAGE)||fileEnum.equals(FileEnum.COMPRESSIONIMAGE)){
    		 localDirPath = AppSystem.getInstance().getAppDirPath() + File.separator + "." + fileEnum.getFilePath();
    	 }else{
    		 localDirPath = AppSystem.getInstance().getAppDirPath() + File.separator + fileEnum.getFilePath();
    	 }
        String path = AppSystem.getInstance().creatDirIfNotExists(localDirPath);
        return path;
    }
    String getExternalFile(FileEnum fileEnum) {
        String localDirPath = AppSystem.getInstance().getExternalPath() + File.separator + fileEnum.getFilePath();
        String path = AppSystem.getInstance().creatDirIfNotExists(localDirPath);
        return path;
    }
    /**
     * 增加新的文件缓存目录
     * 
     * @param fileDir 文件缓存相对目录
     * @since 2013-5-25 wangzhiqiang
     */
    public void addFileDir(String fileDir) {
        String tempPath = AppSystem.getInstance().getAppDirPath() + File.separator + fileDir;
        fileDirPathSet.add(AppSystem.getInstance().creatDirIfNotExists(tempPath));
    }
    /**
     * 创建外部文件
     * @param fileDir
     * @param size
     */
    public File getExternalTmpFile(String fileName,long size)throws ExternalStorageFullException,ExternalInvalidException{
    	if(!JHExternalStorage.canWrite()){
    		throw new ExternalInvalidException();
    	}
    	if(JHExternalStorage.isFull(size))
    	{
    		throw new ExternalStorageFullException();
    	}
    	File file = new File(fileName);
    	File parentFile = file.getParentFile();
    	if(!parentFile.exists()){
    		parentFile.mkdirs();
    	}
    	if(!file.exists()){
    		try {
				if(!file.createNewFile())
					return null;
				else
					return file;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
    	}
    	return file;
    }
    /**
     * 删除文件缓存目录
     * 
     * @param fileDir 文件缓存相对目录
     * @since 2013-5-25 wangzhiqiang
     */
    public void removeFileDir(String fileDir) {
        fileDirPathSet.remove(fileDir);
        try {
            FileUtil.deleteFolderFile(fileDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<String> getFileDirPathSet() {
        return fileDirPathSet;
    }
    /**
     * 如果存在外部sdcard时，则在外部sdcard上创建，否则在内部存储中创建
     * @param fileName 创建文件名
     * @return 创建文件地址
     */
    public String createOtherFile(String fileName){
    	String filePath = AppSystem.getInstance().getAppDirPath();
    	filePath = filePath + File.separator + fileName;
    	return filePath;
    }
    /**
     * 清除文件缓存
     * 
     * @since 2013-5-25 wangzhiqiang
     */
    public void clear() {
        for (String dir : fileDirPathSet) {
            try {
                FileUtil.deleteFolderFile(dir);
                FileUtil.deleteFolderFile(cacheDir.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

 
    /**
     * 创建外部临时文件
     * @return
     * @throws ExternalStorageFullException
     * @throws ExternalInvalidException
     */
    public String createExternalImageFile()throws ExternalStorageFullException,ExternalInvalidException{
    	if(!JHExternalStorage.canWrite()){
    		throw new ExternalInvalidException();
    	}
    	if(JHExternalStorage.isFull(1024))
    	{
    		throw new ExternalStorageFullException();
    	}
    	String parentPath = getExternalFile(FileEnum.IMAGE);
    	SimpleDateFormat format = new SimpleDateFormat(
				"yyyyMMddhhmmssSSS");
		String fileName = format.format(new Date()) + ".jpg";
    	String filePath = parentPath+fileName;
    	return filePath;
    }


    /**
     * 根据文件类型，在缓存目录创建临时文件
     * 
     * @param fileEnum
     * @return
     */
    public String createTempFile(FileEnum fileEnum) {
        String result = getAbsoluteFile(fileEnum) + System.currentTimeMillis();
        if (fileEnum == FileEnum.IMAGE) {
            return result + ".jpg";
        }
        return AppSystem.getInstance().createFileIfNotExists(result);
    }




    public String getLocalFileAbsoluteName(String url, FileEnum fileEnum) {
    	String dirPath = getAbsoluteFile(fileEnum) + Md5Util.getMD5Str(url);
    	File file;
    	if(TextUtils.isEmpty(dirPath))
    	{
    		return "";
    	}
    	else if (fileEnum == FileEnum.IMAGE||fileEnum == FileEnum.COMPRESSIONIMAGE) {
    		dirPath = dirPath + ".jpg";
        } else if (fileEnum == FileEnum.AUDIO) {
        	if(url.length()>6){
        		int index = url.indexOf('.', url.length()-6);
        		if (index!=-1) {
        			String suffix = url.substring(index);
        			dirPath = dirPath + suffix;
				}
        	}
		}
        file = new File(dirPath);
    	if(!file.exists()){
    		try {
				if(!file.createNewFile())
					return "";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
    	}
        return dirPath;
    }

  

    public enum FileEnum {
        IMAGE(IMAGE_PATH), AUDIO(AUDIO_PATH), TEMP(TEMP_PATH),VEDIO(VEDIO_PATH),COMPRESSIONIMAGE(COMPRESSIONIMAGE_PATH);

        private String filePath;

        private FileEnum(String filePath) {
            this.filePath = filePath;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }

}
