package com.jh.persistence.file;


import java.io.BufferedInputStream;



import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.RejectedExecutionException;

import org.apache.http.util.EncodingUtils;

import com.jh.common.app.application.AppSystem;
import com.jh.common.cache.FileCache;
import com.jh.common.cache.FileCache.FileEnum;
import com.jh.exception.ILegalException;
import com.jh.exception.JHException;
import com.jh.net.JHFileNotFoundException;
import com.jh.net.JHIOException;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;



public class FileUtil {
	
	
	
	
	private static final int BITMAP_SIZE = 100;
	
	/*
	 *
	 * 文件操作相关类.
	 *
	 * @class FileUtil
	 * @version  1.0
	 * @author  yourname
	 * @time  2012-2-1 下午01:11:42
	 */
	/**
	 * <code>getFileName</code>
	 * @description: TODO(获取文件�? 
	 * @param filepath
	 * @return
	 * @since   2012-2-1    yourname
	 */
	public static String getFileName(String filepath)
	{
		int lastIndex = filepath.lastIndexOf("/");
		if(lastIndex==-1)
		{
			return filepath;
		}
		else
		{
			return filepath.substring(lastIndex);
		}
	}
	/**
	 * <code>getFileType</code>
	 * @description: TODO(获取文件后缀名，即文件类�? 
	 * @param filepath
	 * @return
	 * @throws POAException
	 * @since   2012-2-3    yourname
	 */
	public static String getFileType(String filepath) throws ILegalException
	{
		int lastIndex = filepath.lastIndexOf(".");
		
		if(lastIndex+1<filepath.length())
		{
			return filepath.substring(lastIndex+1);
		}
		else
		{
			throw new ILegalException();
		}
	}
	/**
	 * <code>getFileSize</code>
	 * @description: TODO(获取文件大小) 
	 * @param filepath 文件路径
	 * @return 文件大小�?
	 * @since   2012-4-5    yourname
	 */
	public static String getFileSize(String filepath)
	{
		File file = new File(filepath);
		float size = (float)file.length();
		if(size<1024)
		{
			return file.length() + "字节";
		}
		else
		{
			size = size/1024f;
			if(size<1024)
			{
				return String.format("%.2f", size) + "kb";
			}
			else
			{
				size = size/1024f;
				return String.format("%.2f", size) + "mb";
			}
		}
		
	}
	/**
	 * 获取文件大小的表示
	 * @param size1
	 * @return
	 */
	public static String getFileSize(long size1)
	{
		float size = (float)size1;
		if(size<1024)
		{
			return size + "字节";
		}
		else
		{
			size = size/1024f;
			if(size<1024)
			{
				return String.format("%.2f", size) + "kb";
			}
			else
			{
				size = size/1024f;
				return String.format("%.2f", size) + "mb";
			}
		}
		
	}
	public static class ExternalStorageInValidException extends JHException
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 5269615745895702965L;

		@Override
		public String getMessage() {
			// TODO Auto-generated method stub
			return "sd卡不可用";
		}
		
	}


	/**
	 * 从输入流中读取文件并保存到给定文件中，该方法并不关闭输入流
	 * 
	 * @param fileName
	 *            待保存的目标文件
	 * @param in
	 *            输入流
	 * @throws IOException
	 * @since 2013-5-25 wangzhiqiang
	 */
	public static void saveToFile(String fileName, InputStream in)
			throws IOException {
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		int BUFFER_SIZE = 1024;
		byte[] buf = new byte[BUFFER_SIZE];
		int size = 0;
		try {
			// 获取网络输入流
			bis = new BufferedInputStream(in);
			// 建立文件
			fos = new FileOutputStream(fileName);
			// 保存文件
			while ((size = bis.read(buf)) != -1) {
				fos.write(buf, 0, size);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//
			fos.close();
			bis.close();
			fos = null;
			bis = null;
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @since 2013-5-25 wangzhiqiang
	 */
	public static void deleteFile(String filePath) {
		new File(filePath).deleteOnExit();
	}

	/**
	 * 删除指定目录下文件及目录
	 * 
	 * @param deleteThisPath
	 * @param filepath
	 * @return
	 */
	public static void deleteFolderFile(String filePath) throws IOException {
		if (!TextUtils.isEmpty(filePath)) {
			File file = new File(filePath);

			if (file.isDirectory()) {// 处理目录
				File files[] = file.listFiles();
				if(files!=null)
				{
					for (int i = 0; i < files.length; i++) {
						deleteFolderFile(files[i].getAbsolutePath());
					}
				}
				
			}

			if (!file.isDirectory()) {// 如果是文件，删除
				file.delete();
			} else {// 目录
				if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
					file.delete();
				}
			}

		}
	}
	/**
	 * 创建sdcard上的文件
	 * @param FileName 文件名
	 * @return
	 * @throws ExternalStorageInValidException
	 * @throws JHIOException
	 */
	public static File createSdcardFile(String FileName) throws ExternalStorageInValidException,JHIOException
	{
		if(ExternalStorageState.canWrite())
		{
			File file = new File(FileName);
			file.getParentFile().mkdirs();
			//file.mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new JHIOException(e);
			}
			return file;
		}
		else
		{
			throw new ExternalStorageInValidException();
		}
	}
	/**
	 * 创建临时文件
	 * @param context
	 * @return
	 * @throws ExternalStorageInValidException
	 */
	public static File createTmpFile(Context context) throws ExternalStorageInValidException
	{
		File file = null;
		try
		{
			file = createSdcardImageFile();
		}
		catch(ExternalStorageInValidException e)
		{
			e.printStackTrace();
		}
		if(file==null)
		{
			String currentTiembyformt = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
			String fileName =  currentTiembyformt+".jpg";
			file = new File(fileName);
		//	file.createNewFile();
			try {
				context.openFileOutput(fileName, Context.MODE_PRIVATE).close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new JHFileNotFoundException();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new JHIOException(e);
			}
		}
		return file;
	}
	/**
	 * 创建sdcard上的图片文件
	 * @return
	 * @throws ExternalStorageInValidException
	 * @throws JHIOException
	 */
	public static File createSdcardImageFile() throws ExternalStorageInValidException,JHIOException
	{
//		String extendPath = Environment.getExternalStorageDirectory().getPath();
		String currentTiembyformt = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
//		String fileName =  currentTiembyformt+".jpg";
		
//		String FileName =Environment.getExternalStorageDirectory().getPath()+"/"+ new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+".jpg";
		if(ExternalStorageState.canWrite())
		{
//			File file = new File(FileName);
			String filepath = FileCache.getInstance(AppSystem.getInstance().getContext()).getLocalFileAbsoluteName(currentTiembyformt, FileEnum.COMPRESSIONIMAGE);
			File file = new File(filepath);
			file.getParentFile().mkdirs(); 
			//file.mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new JHIOException(e);
			}
			return file;
		}
		else
		{
			throw new ExternalStorageInValidException();
		}
	}
	public static  byte[] readStream(InputStream inStream) {

		byte[] bytes1 = null;
		ByteArrayOutputStream outStream = null;
		try
		{
		    BufferedInputStream buffer = new BufferedInputStream(inStream);
			outStream = new ByteArrayOutputStream();
			
			byte[] bytes = new byte[1024];
			int len= -1;
			while((len = buffer.read(bytes))!=-1){
				outStream.write(bytes, 0, len);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
			
		}
		finally
		{
			try {
				outStream.flush();
				bytes1 = outStream.toByteArray();
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return bytes1;
		}
	}
	/**
	 * 根据路径获取字节数组
	 * @param path
	 * @return
	 */
	public static byte[] readstream(String path){
		try {
			FileInputStream fis = new FileInputStream(new File(path));
			byte[] buffer = new byte[1024];
			int len = -1;
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			while((len = fis.read(buffer))!= -1){
				bos.write(buffer, 0, len);
			}
			byte[] photo = bos.toByteArray();
			bos.close();
			fis.close();
			return photo;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	


	
	/**
     * 编码格式
     */
    public static final String ENCODEFORMAT = "UTF-8";
	
	
	/**
     * 文件转换成字符串
     * 
     * @param f
     *            文件
     * @return 文件的字符串
     */
    public static String file2String(File f) {
        try {
            final FileInputStream fin = new FileInputStream(f);
            // FileInputStream fin = openFileInput(fileName);
            // 用这个就不行了，必须用FileInputStream
            final int length = fin.available();
            final byte[] buffer = new byte[length];
            fin.read(buffer);
            final String result = EncodingUtils.getString(buffer, ENCODEFORMAT);
            fin.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
        
    }

    /**
     * 字符串转换成文件
     * 
     * @param str
     *            字符串
     * @param file
     *            文件
     * @return 是否转换成功
     */
    public static boolean string2File(String str, File file) {
        FileOutputStream fos = null;
        try {
            if (file == null) {
                return false;
            }
            fos = new FileOutputStream(file);
            fos.write(str.getBytes(ENCODEFORMAT));
            fos.flush();
            fos.close();
            // CLOSE_LOG_FOR_RELEASE_Log.d("file","write success:"+str+" to :"+des);
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {

        }
        return false;
    }

    /**
     * 复制单个文件
     * 
     * @param oldPath
     *            String 原文件路径 如：c:/fqf.txt
     * @param newPath
     *            String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static int copyFile(String oldPath, String newPath) {
        int result = 0;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            File newfile = new File(newPath);
            if (oldfile.exists() && oldfile.length() > 0) { // 文件存在时
                if (!newfile.getParentFile().exists()) {
                    newfile.getParentFile().mkdirs();
                }
                InputStream inStream = new FileInputStream(oldfile); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newfile);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
                result = 1;
            } else {
                result = 2;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

	
    /**
     * 将输入流的数据复制到输出流
     * @param is
     *            is
     * @param os
     *            os
     */
    public static void copyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        BufferedOutputStream bufferOutput = new BufferedOutputStream(os);
        try {
            final byte[] bytes = new byte[buffer_size];
            for (;;) {
                final int count = is.read(bytes, 0, buffer_size);
                if (count == -1) {
                    break;
                }
                bufferOutput.write(bytes, 0, count);
            }
            bufferOutput.flush();
            bufferOutput.close();
        }catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }catch (RejectedExecutionException e) {
            e.printStackTrace();
        }catch(JHIOException e){
            e.printStackTrace();
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Bitmap转换到Byte[]
     * 
     * @param bm
     *            bm
     * @return byte[]
     */
    public static byte[] bitmap2Bytes(Bitmap bm) {
        final ByteArrayOutputStream bas = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, BITMAP_SIZE, bas);
        return bas.toByteArray();
    }
    
    
    /****
     * 将文件大小转换成指定格式的String
     * add by zhou
     * @return
     */
    public static String getFileSizeToString(long fileSize,String pattern){
        DecimalFormat df = new DecimalFormat(pattern);
        long K = 1024l;
        long M = K * 1024;
        long G = M * 1024;
        String fileSizeString = "";
        if (fileSize < K) {
            fileSizeString = df.format((double) fileSize) + "B";
        } else if (fileSize < M) {
            fileSizeString = df.format((double) fileSize / K) + "K";
        } else if (fileSize < G) {
            fileSizeString = df.format((double) fileSize / M) + "M";
        } else {
            fileSizeString = df.format((double) fileSize / G) + "G";
        }
        return fileSizeString;
    }
}
