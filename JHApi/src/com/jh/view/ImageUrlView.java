package com.jh.view;
/*
 * @project Tingche
 * @package com.park.view
 * @file ImageUrlView.java
 * @version  1.0
 * @author  yourname
 * @time  2012-6-11 下午2:09:46
 * CopyRight:北京金和软件信息技术有限公司 2012-6-11
 */

import java.io.File;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jinher.commonlib.R;
import com.jh.app.util.ImageFactory;
import com.jh.exception.JHException;
import com.jh.util.LogUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


public class ImageUrlView extends RelativeLayout{
	/*
	 *
	 * Class Descripton goes here.
	 *
	 * @class ImageUrlView
	 * @version  1.0
	 * @author  yourname
	 * @time  2012-6-11 下午2:09:46
	 */
	private LayoutInflater inflater;
	private ImageView image;
	private ProgressBar progress;
	private String url;
//	private ImageCallback callback;
	private int defaultResId;
	private int failedResId;
	private boolean showLoading = false;
	public int getFailedResId() {
		return failedResId;
	}
	public void setFailedResId(int failedResId) {
		this.failedResId = failedResId;
	}
	private Bitmap imageBitmap;
	private static Context context;
	private static final int SUCCESS = 1,FAILED = 0;

	public ImageUrlView(Context context) {
		/**
		     * constructor 
		     * @param number
		     */
		super(context);
		initView(context);
		init();
	//	addView(view, new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//		
	}
	  public ImageUrlView(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        initView(context);
	        init();
	  }
	  
	  public ImageUrlView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		 initView(context);
	     init();
		// TODO Auto-generated constructor stub
	}
	public View initView(Context context)
	  {
		  	this.setGravity(Gravity.CENTER);
		  	inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.image_urlview, this,true);
			image = (ImageView)view.findViewById(R.id.image_src);
			image.setScaleType(ScaleType.CENTER);
//			image.setAdjustViewBounds(false);
			progress = (ProgressBar)view.findViewById(R.id.progress);
			if(showLoading)
				progress.setVisibility(View.VISIBLE);
			else
				progress.setVisibility(View.INVISIBLE);
			image.setVisibility(View.VISIBLE);
			return view;
	  }
	  public String getUrl() {
			return url;
	  }
	  @SuppressWarnings("javadoc")
	public void setUrl(String url) {
		  imageUrls.remove(this);
		  if(url==null||url.trim().length()==0)
		  {
			  setDefImage();
			  if(imageBitmap!=null)
				  imageBitmap = null;
		  }
		  else if(imageCaches.get(url)!=null&&imageCaches.get(url).get()!=null)
		  {
			  setImage(imageCaches.get(url).get());
		  }
		  else if(this.url==null)
		  {
			  setDefImage();
			  imageUrls.add(this);
		  }
		  else if(this.url.trim().length()>0
				  &&url.equalsIgnoreCase(this.url))
		  {
			  if(imageBitmap==null)
			  {
				  this.url = url;
				  setDefImage();
				  imageUrls.add(this);
			  }
			  else
				  setImage(imageBitmap);
		  }
		  else
		  {
			  if(imageBitmap!=null)
				  imageBitmap = null;
			  {
				  this.url = url;
				  setDefImage();
				  imageUrls.add(this);
			  }
		  }
		  this.url = url;
		  startLoading();
	  }
	  public void setC6Url(String url,Context context) {
		  imageUrls.remove(this);
		  this.context = context;
		  if(url==null||url.trim().length()==0)
		  {
			  setDefImage();
			  if(imageBitmap!=null)
				  imageBitmap = null;
		  }
		  else if(imageCaches.get(url)!=null&&imageCaches.get(url).get()!=null)
		  {
			  setImage(imageCaches.get(url).get());
		  }
		  else if(this.url==null)
		  {
			  setDefImage();
			  imageUrls.add(this);
		  }
		  else if(this.url.trim().length()>0
				  &&url.equalsIgnoreCase(this.url))
		  {
			  if(imageBitmap==null)
			  {
				  this.url = url;
				  setDefImage();
				  imageUrls.add(this);
			  }
			  else
				  setImage(imageBitmap);
		  }
		  else
		  {
			  if(imageBitmap!=null)
				  imageBitmap = null;
			  {
				  this.url = url;
				  setDefImage();
				  imageUrls.add(this);
			  }
		  }
		  this.url = url;
		  startLoadingForC6Portrait();
	  }
	  public void setDefImage()
	  {
		  if(defaultResId!=0)
			  image.setImageResource(defaultResId);
	  }
	  public void setFailedImage()
	  {
		  progress.setVisibility(View.INVISIBLE);
		  if(failedResId!=0)
			  image.setImageResource(failedResId);
	  }
	  public void setImage(Bitmap bmp) 
	  {
		  progress.setVisibility(View.INVISIBLE);
		  imageBitmap = bmp;
		  image.setImageBitmap(bmp);
	  }
	  public int getDefaultResId() {
		return defaultResId;
	}
	public void setDefaultResId(int defaultResId) {
		this.defaultResId = defaultResId;

	}
	 /**
     * 取消图片缩放比例
     * add by zhoukun 2013-12-13
     * 解决大屏手机适配的问题
     * @return
     */
  public void setImageScaleTypeDefault(){
      
      image.setScaleType(ScaleType.FIT_XY);
  }
	private static List<ImageUrlView> imageUrls;
	private static Handler handler = new Handler(){
		ImageInfo info;
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what == SUCCESS)
			{
				info = (ImageInfo)msg.obj;
				if(info.bitmap!=null&&info.url!=null&&info.view.getUrl()!=null&&info.url.equalsIgnoreCase(info.view.getUrl()))
				{
					info.view.setImage(info.bitmap);
					info.view.imageBitmap = info.bitmap;
				}
			}
			else
			{

//				BaseToast.getInstance(((ImageUrlView)msg.obj).getContext(), "下载失败").show();
				((ImageUrlView)msg.obj).setFailedImage();
			}
		}
	};
	public void init()
	{
		if(excutor==null)
		{
			excutor = Executors.newSingleThreadExecutor();
		}
		if(imageUrls==null)
		{
			imageUrls = new LinkedList<ImageUrlView>();
		}
	}
	private static ExecutorService excutor;
	private static boolean stopFlag = true;
	private static class DownThread implements Runnable
	{
		private String url;
		private Bitmap bmp;
		private Message msg;
		@Override
		public void run() {
			// TODO Auto-generated method stub
	//		super.run();
//			while(!stopFlag)
			{
				if(!imageUrls.isEmpty())
				{
					ImageUrlView view = imageUrls.remove(0);
					url = view.getUrl();
					bmp = null;
					if(url!=null)
					{
						if(imageCaches.containsKey(url))
						{
							bmp = imageCaches.get(url).get();
						}
						if(bmp==null)
						{
							String filePath = CacheDB.getLocalpicPath(view.getContext(),url);
							if(filePath==null||!new File(filePath).exists())
							{
								try {
									filePath = ImageFactory.getURLBitmap(url);
								} catch (JHException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									msg = Message.obtain();
									msg.what = FAILED;
									msg.obj = view;
									handler.sendMessage(msg);
								}
								if(filePath!=null)
								{
									CacheDB.deletePic(view.getContext(), url);//先删除之前的
									CacheDB.insertPic(view.getContext(), url, filePath);
								}
							}
							if(filePath!=null)
							{
								try {
									bmp = ImageFactory.getFileBitmap(filePath,view.getHeight(),view.getWidth(),null);
								} catch (JHException e) {
									// TODO Auto-generated catch block
								
								}
								if(!imageCaches.containsKey(url))
								{
									imageCaches.put(url, new SoftReference<Bitmap>(bmp));
								}
								else
								{
									imageCaches.remove(url);
									imageCaches.put(url, new SoftReference<Bitmap>(bmp));
								}
								if(imageCaches.size()>maxImage)
								{
									imageCaches.remove(imageCaches.keySet().iterator().next());
								}
							}
							
						}
						msg = Message.obtain();
						msg.obj = new ImageInfo(bmp,view,url);
						msg.what = SUCCESS;
						handler.sendMessage(msg);
					}
				}
			}
		}
	}
	private static class C6PortraitDownThread implements Runnable
	{
		private String url;
		private Bitmap bmp;
		private Message msg;
		private Uri uri;
		private String filePath;
		private Cursor cursor;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//		super.run();
//			while(!stopFlag)
			{
				if(!imageUrls.isEmpty())
				{
					ImageUrlView view = imageUrls.remove(0);
					url = view.getUrl();
					bmp = null;
					if(url!=null)
					{
						if(imageCaches.containsKey(url))
						{
							bmp = imageCaches.get(url).get();
						}
						if(bmp==null)
						{

							uri = Uri.parse("content://com.jh.yunoa.contentproviderforcontacts/headimage");
							cursor = context.getContentResolver().query(uri, new String[]{"localPath"}, "httpPath = ?", new String[]{url}, null);
//						String filePath = CacheDB.getLocalpicPath(view.getContext(),url);
							if (cursor!=null&&cursor.moveToFirst()) {
								filePath = cursor.getString(cursor.getColumnIndex("localPath"));
							}
							LogUtil.println("filePath_local:"+filePath);
							LogUtil.println("服务器地址url："+url);
//						filePath = "/storage/sdcard0//C6Data//thumbImageFile//2013-03-04-01_51_17_033.jpg";
						
							
							if(filePath==null||!new File(filePath).exists())
							{
								try {
									filePath = ImageFactory.getURLBitmap(url);
								} catch (JHException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									msg = Message.obtain();
									msg.what = FAILED;
									msg.obj = view;
									handler.sendMessage(msg);
								}
								if(filePath!=null)
								{
//									CacheDB.deletePic(view.getContext(), url);//先删除之前的
//									CacheDB.insertPic(view.getContext(), url, filePath);
									uri = Uri.parse("content://com.jh.yunoa.contentproviderforcontacts/headimage");
									context.getContentResolver().delete(uri, "httpPath = ?", new String[]{url});
									ContentValues values = new ContentValues();
									values.put("localPath", filePath);
									values.put("httpPath", url);
									context.getContentResolver().insert(uri, values);
								}
							}
							if(filePath!=null)
							{
								try {
									bmp = ImageFactory.getFileBitmap(filePath,view.getHeight(),view.getWidth(),null);
								} catch (JHException e) {
									// TODO Auto-generated catch block
									
								}
								if(!imageCaches.containsKey(url))
								{
									imageCaches.put(url, new SoftReference<Bitmap>(bmp));
								}
								else
								{
									imageCaches.remove(url);
									imageCaches.put(url, new SoftReference<Bitmap>(bmp));
								}
								if(imageCaches.size()>maxImage)
								{
									imageCaches.remove(imageCaches.keySet().iterator().next());
								}
							}
							
						}
						msg = Message.obtain();
						msg.obj = new ImageInfo(bmp,view,url);
						msg.what = SUCCESS;
						handler.sendMessage(msg);
					}
				}
			}
		}
	}
	private static class ImageInfo
	{
		public Bitmap bitmap;
		public ImageUrlView view;
		public String url;
		public ImageInfo(Bitmap bitmap,ImageUrlView view,String url)
		{
			this.bitmap = bitmap;
			this.view = view;
			this.url = url;
		}
	}
	private void startLoading()
	{
		excutor.execute(new DownThread());
	}
	private void startLoadingForC6Portrait()
	{
		excutor.execute(new C6PortraitDownThread());
	}
	public static void clearImages()
	{
		if(imageUrls!=null)
		{
			imageUrls.clear();
			imageUrls = null;
		}
		stopFlag = true;
		if(excutor!=null)
		{
			excutor.shutdownNow();
			excutor = null;
		}
	}

	  private final static int maxImage = 20;
	  private static Map<String, SoftReference<Bitmap>> imageCaches = new HashMap<String, SoftReference<Bitmap>>();

}
