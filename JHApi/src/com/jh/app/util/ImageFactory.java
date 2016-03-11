package com.jh.app.util;

import java.io.BufferedInputStream;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jh.common.app.application.AppSystem;
import com.jh.common.cache.FileCache;
import com.jh.common.cache.FileCache.FileEnum;
import com.jh.exception.JHException;
import com.jh.net.JHFileNotFoundException;
import com.jh.net.JHIOException;
import com.jh.persistence.file.FileUtil;
import com.jh.persistence.file.FileUtil.ExternalStorageInValidException;
import com.jh.util.LogUtil;



import android.R.integer;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.widget.ImageView;

/**
 * <code>ImageFactory</code>
 * @description: TODO(与图像处理相关类) 
 * @version  1.0
 * @author  张楠
 * @since 2011-8-31
 */
public class ImageFactory 
{	
	//01-09 12:49:44.218: INFO/(21517): database:/sdcard//DYData//thumbImageFile//1643.jpg
	private static final Uri STORAGE_URI = Images.Media.EXTERNAL_CONTENT_URI;  
	public static Uri addImage(ContentResolver cr, String title, long dateTaken,  
			            Location location, String filename) {  
			        // We should store image data earlier than insert it to ContentProvider,  
			        // otherwise we may not be able to generate thumbnail in time.  
			        // Read back the compressed file size.  
					File file = new File(filename);
					if (!file.exists()) {
						try {
							file.createNewFile();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
			        long size = file.length();  
			        ContentValues values = new ContentValues(9);  
			        values.put(Images.Media.TITLE, title);  
			       // That filename is what will be handed to Gmail when a user shares a  
			        // photo. Gmail gets the name of the picture attachment from the  
			        // "DISPLAY_NAME" field.  
			        values.put(Images.Media.DISPLAY_NAME, file.getName());  
			        values.put(Images.Media.DATE_TAKEN, dateTaken);  
			        values.put(Images.Media.MIME_TYPE, "image/jpeg");  
			        values.put(Images.Media.ORIENTATION, 0);  
			        values.put(Images.Media.DATA, filename);  
			        values.put(Images.Media.SIZE, size);  
			        if (location != null) {  
		            values.put(Images.Media.LATITUDE, location.getLatitude());  
		            values.put(Images.Media.LONGITUDE, location.getLongitude());  
			       }  
			        return cr.insert(STORAGE_URI, values);  
	}  
	public static void saveToPhone(String fileName,Context context)
	{
		ContentResolver cr = context.getContentResolver();
		try
		{
			addImage(cr,"",new Date().getTime(),null,fileName);
		}
		catch(RuntimeException e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * 由文件路径获取bitmap
	 * @param bitmapPath 本地路径
	 * @return
	 * @throws JHException
	 */
	public static Bitmap getBitmapByPath(String bitmapPath) throws JHException{
		Bitmap bitmap = null;
		bitmap = BitmapFactory.decodeFile(bitmapPath);
		if(bitmap ==null){
			throw new JHException("bitmap is null");
		}
		return bitmap;
	}
	/**
	 * 由文件路径获取不大于指定宽和高的bitmap
	 * @param bitmapPath 本都文件路径
	 * @param w 最大宽度
	 * @param h 最大高度
	 * @return
	 */
	public static Bitmap getwhBitmap(String bitmapPath,int w,int h)
	{
			InputStream inputStream;
			byte[] len = null ;
			try {
				inputStream = new FileInputStream(bitmapPath);
				len = streamToBytes(inputStream);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			BitmapFactory.Options options = new BitmapFactory.Options();  
            options.inJustDecodeBounds = true;  
			Bitmap bm =  BitmapFactory.decodeByteArray(len, 0, len.length);
            options.inJustDecodeBounds = false;  
 
			if(bm ==null)
			{
				return null;
			}

			int  src_w = bm.getWidth();
			int  src_h = bm.getHeight();
			float scale_w = ((float)w)/src_w;
			float  scale_h = ((float)h)/src_h;
			Matrix  matrix = new Matrix();
			matrix.postScale(scale_w, scale_h);
			Bitmap bitmap = Bitmap.createBitmap(bm, 0, 0, src_w, src_h, matrix, true);
			
			bm.recycle();
			
		return bitmap;
		
	}
	/**
	 * <code>getFileBitmap</code>
	 * @description: TODO(获取指定大小图像,如果文件不存在，返回为空) 
	 * @param path 图像地址
	 * @param height 获取图像高度
	 * @param width 获取图像宽度
	 * @param context 上下文
	 * @return 获取的图像
	 * @throws JHException 
	 * @since   2011-8-31    张楠
	 */
	public static Bitmap getFileBitmapNoException(String path,int height,int width,Context context){
		try{
			Bitmap bmp = getFileBitmap(path,height,width,context);
			return bmp;
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	public static class BitmapInfo{
		private int inSampleSize;
		private Bitmap bitmap;
		public int getInSampleSize() {
			return inSampleSize;
		}
		public void setInSampleSize(int inSampleSize) {
			this.inSampleSize = inSampleSize;
		}
		public Bitmap getBitmap() {
			return bitmap;
		}
		public void setBitmap(Bitmap bitmap) {
			this.bitmap = bitmap;
		}
	}
	public static int[] getBitmapSize(Context context,String path){
		int widthandHeight[] = new int[]{-1,-1};
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.outHeight = 40;
		options.inJustDecodeBounds = true;
		options.outHeight = 40;
		if(path.startsWith("content"))
		{
			Uri uri=Uri.parse(path);
			ParcelFileDescriptor pfd=null;
			try {
				pfd = context.getContentResolver().openFileDescriptor(uri, "r");
				FileDescriptor fd = pfd.getFileDescriptor();
				BitmapFactory.decodeFileDescriptor(fd, null, options);
			}
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				throw new JHFileNotFoundException();
			}
		}else{
			BitmapFactory.decodeFile(path,options);
		}
		BitmapFactory.decodeFile(path,options);
		widthandHeight[0] = options.outWidth;
		widthandHeight[1] = options.outHeight;
		return widthandHeight;
	}
	/**
	 * 
	 * @param path
	 * @param height
	 * @param width
	 * @param context
	 * @return
	 * @throws JHException
	 */
	public static BitmapInfo getFileBitmapInfo(String path,int height,int width,Context context) throws JHException{
		System.gc();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.outHeight = 40;
		///options.outWidth = 60;
		Bitmap bmp = null;
		height = Math.max(80, height);
		width = Math.max(80, width);
		if(path==null)
		{
			throw new JHFileNotFoundException();
		}
		if(path.startsWith("content"))
		{
			Uri uri=Uri.parse(path);
			ParcelFileDescriptor pfd=null;
			try {
				pfd = context.getContentResolver().openFileDescriptor(uri, "r");
				FileDescriptor fd = pfd.getFileDescriptor();
				options.inJustDecodeBounds = true;
				options.outHeight = 40;
				BitmapFactory.decodeFileDescriptor(fd, null, options);
				options.inJustDecodeBounds = false;
				options.inSampleSize =getSample(options.outHeight,options.outWidth,height,width);
				System.gc();
				bmp=BitmapFactory.decodeFileDescriptor(fd, null, options);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				throw new JHFileNotFoundException();
			}
	//		return bmp;
		}
		else
		{
			BitmapFactory.decodeFile(path,options);
			options.inJustDecodeBounds = false;
			options.inSampleSize =getSample(options.outHeight,options.outWidth,height,width);
			bmp =  BitmapFactory.decodeFile(path,options);
		}
		if(bmp==null)
		{
			throw new JHFileNotFoundException();
		}
		BitmapInfo info = new BitmapInfo();
		info.setBitmap(bmp);
		info.setInSampleSize(options.inSampleSize);
		return info;
	}
	/**
	 * <code>getFileBitmap</code>
	 * @description: TODO(获取指定大小图像,如果文件不存在，抛出异常，注意捕捉) 
	 * @param path 图像地址
	 * @param height 获取图像高度
	 * @param width 获取图像宽度
	 * @param context 上下文
	 * @return 获取的图像
	 * @throws JHException 
	 * @since   2011-8-31    张楠
	 */
	public static Bitmap getFileBitmap(String path,int height,int width,Context context) throws JHException
	{
		BitmapInfo info = getFileBitmapInfo(path,height,width,context);
		return info.getBitmap();
	}
	/**
	 * 由文件路径获取图片
	 * @param path	文件本地路径
	 * @param context
	 * @return
	 * @throws JHException
	 */
	public static Bitmap getImageBitmap(String path,Context context)throws JHException{
		return 	BitmapFactory.decodeFile(GetImagePath(path,context));
	}
	
	/**
	 * 
	 * <code>getURLBitmap</code>
	 * @description: TODO(这里用一句话描述这个方法的作用) 
	 * @param uriPic
//	 * @param type 1 缩略图文件 thumbImageFile ,2 原图文件 formerImageFile 3 TempFile 临时文件
	 * @return
	 * @throws JHException
	 * @since   2011-12-14    yourname
	 */
	public static String getURLBitmap(String uriPic) throws JHException
	{
			URL imageUrl = null;
			int len = 0;
			byte img[] = new byte[2048];
			File fout;
			FileOutputStream output = null;
			String fileName = null;
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssSSS") ;
			try {
				if (uriPic!=null&&uriPic.trim().length()>0) {
					imageUrl = new URL(uriPic);
					HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
					conn.setRequestMethod("GET");
					conn.setDoInput(true);
					conn.setDoOutput(false);
					//		HttpClient client = new DefaultHttpClient();
					//		HttpGet request = new HttpGet(uriPic);
					//		HttpResponse response = client.execute(request);
					conn.setConnectTimeout(30000);
					conn.setReadTimeout(30000);
					//			InputStream is = response.getEntity().getContent();
					InputStream is = conn.getInputStream();
					// 做服务器图片地址的处理
					uriPic = uriPic.split("&")[0];
					//   String localPath =uriPic.substring(uriPic.lastIndexOf("/")+1, uriPic.length());
					String filePath ;
					String extendPath =null;
					//2016-1-22 lijie App里面看的图片不会在相册里面出现
					if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//						extendPath = Environment.getExternalStorageDirectory().getPath()+"//freeSmsData//";
						String localDirPath = AppSystem.getInstance().getAppDirPath() + File.separator + "." + FileEnum.COMPRESSIONIMAGE.getFilePath();
						extendPath = AppSystem.getInstance().creatDirIfNotExists(localDirPath);
					}else {
						extendPath = "//data//data//freeSmsData//";
					}
					/*if(type ==3){ // 大图
						 filePath = extendPath+"bigHeadpic//";  
					}else{
						 filePath = extendPath+"thumbHeadPic//";
					}*/
					filePath = extendPath;
					fileName = format.format(new Date())+".jpg";
					output = new FileOutputStream(CreateFile(filePath,fileName));
					fileName = filePath + fileName;
					//output = new FileOutputStream(CreateFileNew(filePath,localPath));
//					str1= filePath+localPath;
					len = is.read(img);
					while (len != -1) {
						output.write(img, 0, len);
						len = is.read(img);
					}
					output.close();
					is.close();
					//	conn.disconnect();
					//	conn = null;
					img = null;
				}
				
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
				throw new JHException("file not exist");
			}
			catch (IOException e)
			{
				e.printStackTrace();
				throw new JHIOException();
			}
			
			return fileName;
	}
	/**
	 * 由文件路径获取文件实际路径
	 * @param path 
	 * @param context
	 * @return
	 */
	public static String GetImagePath(String path,Context context)
	{
		String tmpname = path;
		if(path.startsWith("content"))
		{
			Uri uri = Uri.parse(path);
			ContentResolver cr = context.getContentResolver();
			Cursor cursor = cr.query(uri, null, null, null, null);
			cursor.moveToFirst();
			tmpname = cursor.getString(cursor.getColumnIndex("_display_name"));
//			byte [] b = cursor.getBlob(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
			String path1 = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
			LogUtil.println("path1="+path1);
			cursor.close();
			if(path1 != null){
				return path1;
			}
		}
		return path;
	}
	
	/**
	 * <code>getSample</code>
	 * @description: TODO(计算压缩比例) 
	 * @param outHeight 原图像高
	 * @param outWidth 原图像宽
	 * @param height 压缩后高
	 * @param width 压缩后宽
	 * @return 压缩比例
	 * @since   2011-8-31    张楠
	 */
	private static int getSample(int outHeight,int outWidth,int height,int width)
	{
		int scaleWidth=(int)Math.ceil(outHeight/(float)height);
		int scaleHeight = (int)Math.ceil(outWidth /(float) width);  
		int zoomtime=Math.max(scaleWidth, scaleHeight);
		if(zoomtime==0)
			zoomtime=1;
		return zoomtime;
	}
	/**
	 * <code>file2Byte</code>
	 * @description: 获取文件比特流
	 * @param filename 文件名
	 * @return 比特流
	 * @throws JHException
	 * @since   2011-6-27    张楠
	 */
	public  static byte[] file2Byte(String filename) throws JHException
	{  
		try
		{
			System.gc();
	        BufferedInputStream in = new BufferedInputStream(new FileInputStream(filename));      
	        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);      
	        byte[] temp = new byte[1024];      
	        int size = 0;      
	        while ((size = in.read(temp)) != -1) {      
	            out.write(temp, 0, size);      
	        }      
	        in.close();      
	        return out.toByteArray();
		}
		catch(FileNotFoundException e)
		{
			throw new JHException(e.getMessage());
		} catch (IOException e) {
			throw new JHException(e.getMessage());
		}
	}
	
	/**
	 * <code>hasExceed</code>
	 * @description: TODO(判断是否超过指定大小) 
	 * @param path 文件路径
	 * @param context 上下文
	 * @param maxsize 指定大小
	 * @return 是否超过
	 * @since   2011-9-2    张楠
	 */
	public static boolean hasExceed(String path,Context context,long maxsize)
	{
		return getFileSize(path,context)>maxsize;
	}
	
	private static void saveBitmap(Bitmap bitmap,String name) 
	{
		
	}
	/**
	 * 
	 * <code>getRoundedCornerBitmap</code>
	 * @description: TODO(获取圆角图片) 
	 * @param bitmap
	 * @param roundPx 弧度大小
	 * @return
	 * @since   2011-10-20     廖益平
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPx){  
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap  
        					  .getHeight(), Config.ARGB_8888);  
        Canvas canvas = new Canvas(output);  
        final int color = 0xff424242;  
        final Paint paint = new Paint();  
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  
        final RectF rectF = new RectF(rect);  
        paint.setAntiAlias(true);  
        canvas.drawARGB(0, 0, 0, 0);  
        paint.setColor(color);  
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);  
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
        canvas.drawBitmap(bitmap, rect, rect, paint);  
        return output;  

    }
	/**
	 * 
	 * <code>zoomBitmap</code>
	 * @description: TODO(扩大或者缩小图片) 
	 * @param bitmap
	 * @param w
	 * @param h
	 * @return
	 * @since   2011-11-1     廖益平
	 */
    public static Bitmap getZoomBitmap(Context context,String path, int w, int h) {
    	Bitmap bitmap =null;
    	try {
			bitmap=getFileBitmap(path, h, w, context);
		} catch (JHException e) {
			e.printStackTrace();
		}
		if(bitmap != null){
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			Matrix matrix = new Matrix();
			float scaleWidth = ((float) w / width);
			float scaleHeight = ((float) h / height);
			matrix.postScale(scaleWidth, scaleHeight);
			bitmap  = Bitmap.createBitmap(bitmap, 0, 0, width, height,
					matrix, true);
		}
		return bitmap; 
    }
    /**
     * 
     * <code>streamToBytes</code>
     * @description: TODO(这里用一句话描述这个方法的作用) 
     * @param is
     * @return
     * @since   2011-12-13    liaoyp
     */
    public static byte[] streamToBytes(InputStream is) {  
	    ByteArrayOutputStream os = new ByteArrayOutputStream(1024);  
	    byte[] buffer = new byte[1024];  
	    int len;  
	    try {  
	        while ((len = is.read(buffer)) >= 0) {  
	            os.write(buffer, 0, len);  
	        }  
	    } catch (java.io.IOException e) {  
	    }  
	    return os.toByteArray();  
	} 
    /**
     * 
     * <code>CreateText</code>
     * @description: TODO(创建文件夹及文件) 
     * @param filename
     * @param dir 
     * @throws IOException
     * @since   2011-12-14    liaoyp
     */
    public static  File CreateFile(String dir, String filename) throws IOException { 
        File directory = new File(dir); 
        if (!directory.exists()) { 
            try { 
                //按照指定的路径创建文件夹 
            	directory.mkdirs();
            	
            } catch (Exception e) { 
                // TODO: handle exception 
            } 
        } 
        String filePath = new String(dir+filename);
    	File file = new File(filePath.replace("\n", "")); 
    	if(!file.exists())
    		file.createNewFile();
    	return file;  
   //     File file = new File(dirr,filename); 
    } 
    /**
     * 创建新文件
     * @param dirr
     * @param filename
     * @return
     * @throws IOException
     */
    public static File CreateFileNew(String dirr, String filename) throws IOException
    {
    	File dir=new File(dirr);
    	File parentFile=dir.getParentFile();
    	if(!parentFile.exists())
    	{
    		parentFile.mkdirs();
    	}
    	File file=new File(dirr, filename);
    	return file;
    }

    
    /**
	 * <code>getFileSize</code>
	 * @description: TODO(获取文件大小) 
	 * @param path 文件路径
	 * @param context 上下纹
	 * @return 文件大小
	 * @since   2011-9-2    张楠
	 */
	public static long getFileSize(String path,Context context)
	{
		File file = new File(getFilePath(path,context));
		LogUtil.println("length: "+file.length());
		return file.length();
	}
	/**
	 * 获取文件大小
	 * @param path
	 * @param context
	 * @return
	 */
	public  static long getFileSize(Uri path,Context context)
	{
		File file = new File(getFilePath(path,context));
		return file.length();
	}
	
	/**
	 * <code>getFilePath</code>
	 * @description: TODO(获取图像路径) 
	 * @param path 传递如路径
	 * @param context 上下文
	 * @return 返回路径
	 * @since   2011-9-2    张楠
	 */
	public static String getFilePath(String path,Context context)
	{
		String tmpname = path;
		if(path.startsWith("content"))
		{
			Uri uri = Uri.parse(path);
			ContentResolver cr = context.getContentResolver();
			Cursor cursor = cr.query(uri, null, null, null, null);
			cursor.moveToFirst();
			tmpname = cursor.getString(cursor.getColumnIndex("_data"));
			cursor.close();
		}
		return tmpname;
	}
	/**
	 * 由url获取图片保存的本地路径
	 * @param uri
	 * @param context
	 * @return
	 */
	public static String getFilePath(Uri  uri,Context context)
	{	
		String tmpname;
			ContentResolver cr = context.getContentResolver();
			Cursor cursor = cr.query(uri, null, null, null, null);
			cursor.moveToFirst();
			tmpname = cursor.getString(cursor.getColumnIndex("_data"));
			cursor.close();
		return tmpname;
	}
	
	/**
	 * 
	 * <code>compressPic</code>
	 * @description: TODO(根据imageview 计算需要显示的图片大小) 
	 * @param mImageView
	 * @param mCurrentPhotoPath
	 * @return
	 * @since   2012-7-19    liaoyp
	 */
	public static Bitmap compressPic(ImageView mImageView,String mCurrentPhotoPath){
		
		LogUtil.println("path: "+mCurrentPhotoPath);
		// Get the dimensions of the View
		// mImageView = ((ImageView) findViewById(R.id.imageView1));
		 int targetW = mImageView.getWidth();
		 int targetH = mImageView.getHeight();
	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;
	  
	    // Determine how much to scale down the image
	    int scaleFactor = Math.max(Math.max(photoW/targetW, photoH/targetH), 1);
	  
	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;
	  
	    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    return bitmap;
	}
	/**
	 * 压缩图片，返回压缩后图片本地路径
	 * @param targetW 压缩后宽度
	 * @param targetH 压缩后高度
	 * @param mCurrentPhotoPath	待压缩图片路径
	 * @param context 上下文
	 * @return 压缩后图片路径
	 */
	public static String compressPicPath( int targetW , int targetH ,String mCurrentPhotoPath,Context context)
	{	File file = compressPic(targetW,targetH,mCurrentPhotoPath,context);
		if (file!=null) {
			return file.getAbsolutePath();
		}
		else {
			return null;
		}
	}
	/**
	 * <code>compressPic</code>
	 * @description: TODO(压缩图片) 
	 * @param targetW  压缩的比列宽度
	 * @param targetH  压缩的比列高度
	 * @param mCurrentPhotoPath   文件地址
	 * @return  返回压缩后文件的路劲
	 * @since   2012-7-19    liaoyp
	 */
	public static File compressPic( int targetW , int targetH ,String mCurrentPhotoPath,Context context){
		
		LogUtil.println("path: "+mCurrentPhotoPath);
		boolean isFromSelectPic=  false;
	  
	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;
	    int scaleFactor = Math.max(Math.max(photoW/targetW, photoH/targetH), 1);
	  
	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;	    
	    // 保存新文件 
	    File file= null;
	    try {
			Bitmap bitmap = getFileBitmap(mCurrentPhotoPath,targetW,targetH,context);
			file= FileUtil.createSdcardImageFile();
    		FileOutputStream out = new FileOutputStream(file);
    		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.close();
			
		}catch (ExternalStorageInValidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JHException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	   /* Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        try {  
        		file= FileUtil.createSdcardImageFile();  
        		FileOutputStream out = new FileOutputStream(file);
        		bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
				out.close();
	     } catch (Exception e) {   
	    	 LogUtil.println("获取文件失败！");
	    	  e.printStackTrace();
	    	  return null;
	     }finally{
	    	 
	     }  */
        return file;
	}
	/**
	 * 有bitmap获取对应的字节数组
	 * @param bm
	 * @return
	 */
	public static  byte[] Bitmap2Bytes(Bitmap bm){   
	   
	    return bmpToByteArray(bm,false);   
	   }
	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {


        int i;
        int j;
        if (bmp.getHeight() > bmp.getWidth()) {
            i = bmp.getWidth();
            j = bmp.getWidth();
        } else {
            i = bmp.getHeight();
            j = bmp.getHeight();
        }
        
        Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);
        
        while (true) {
            localCanvas.drawBitmap(bmp, new Rect(0, 0, i, j), new Rect(0, 0,i, j), null);
            if (needRecycle)
                bmp.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
                //F.out(e);
            }
            i = bmp.getHeight();
            j = bmp.getHeight();
        }
    }
}
