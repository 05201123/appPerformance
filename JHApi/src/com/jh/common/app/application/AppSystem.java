/*
 * @project freesms
 * @version 3.0
 * @author zhaopuqing
 * @time 2013-5-3 下午4:55:58 CopyRight:北京金和软件信息技术有限公司 2013-5-3
 */
package com.jh.common.app.application;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.util.EncodingUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.jh.common.cache.JHExternalStorage;
import com.jh.net.JHHttpClient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 系统类，包含了手机的品牌型号、操作系统版本等系统级信息
 * 
 * @version v3.0
 * @author zhaopuqing
 * @since v3.0
 * @createTime 2013-5-3
 */
public class AppSystem {
    private static AppSystem instance;

    /** SD卡根路径 */
    private String sdCardRootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;

    /** 应用 id **/
    private String appId;
    /**版本是否发布**/
    private String approvalFlag;
    /**当前应用布局版本号**/
    private String layoutVersion;
    
    /**
     * 一级栏目可见权限
     */
    private String auth4PartOne;

    /*** 应用 appPackageId **/
    private String appPackageId;

    /** 包名 */
    private String packageName;

    /** 验证app合法性的参数 **/
    private String accessToken;
    
    /** 应用 验证app合法性的参数 */
    private String refreshToken;

    /** 手机型号 */
    private PhoneEnum phoneType;

    private Context context;

    /** 操作系统版本 **/
    private AppSystem() {
    }

    /**
     * 获取当前实例
     * 
     */
    public static AppSystem getInstance() {
        if (instance == null) {
            instance = new AppSystem();
        }
        return instance;
    }



    public String getAppSource() {
        ApplicationInfo appInfo;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            String appKey=appInfo.metaData.get("app_key")+"";
            return appKey;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 
     * @description: 获取包名
     * @param context
     * @return
     * @since 2013-5-25 wangzhiqiang
     */
    public String getPackageName() {
        if (TextUtils.isEmpty(packageName)) {
            
               // info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
              //  packageName = info.packageName;
                packageName = context.getPackageName();
        }
        return packageName;
    }
    public String getAPPName(){
    	PackageManager packageManager = null; 
    	ApplicationInfo applicationInfo = null; 
    	String version = "";
    	try { 
    		packageManager = context.getApplicationContext().getPackageManager(); 
    		applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
    		version = 
    				(String) packageManager.getApplicationLabel(applicationInfo); 
    	} catch (PackageManager.NameNotFoundException e) { 
    		applicationInfo = null; 
    	}
        return version;
    }
    /**
     * 获取版本号
     * 
     * @return
     */
    public String getVersionCode() {
        PackageManager manager = context.getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode+"";
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 获取版本名称
     * 
     * @return
     */
    public String getVersionName() {
        PackageManager manager = context.getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName+"";
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 获取应用的APP id
     * 
     * @return
     * @since 2013-5-31 zhaopuqing
     * @throws 如果配置文件appId.txt不存在或者里面缺少appId，则抛出IllegalArgumentException( "App id does not exist in appid.txt error!")异常
     */
    public synchronized String getAppId() {
        if (appId != null) {
            return appId.toLowerCase();
        }
        appId = readXMLFromAssets("appId.xml","appId");
        if (TextUtils.isEmpty(appId)) {
            throw new IllegalArgumentException("App id does not exist in appid.txt error!");
        }
        return appId == null ? null : appId.toLowerCase();
    }
    /**
     * add by zhangjd
     * @return
     */
    public synchronized String getApprovalFlag() {
    	if (approvalFlag != null) {
    		return approvalFlag.toLowerCase();
    	}
    	approvalFlag = readXMLFromAssets("approvaladdress.xml","approvalFlag");
    	if (TextUtils.isEmpty(approvalFlag)) {
    		throw new IllegalArgumentException("approvalFlag does not exist in approvaladdress.txt error!");
    	}
    	return approvalFlag == null ? null : approvalFlag.toLowerCase();
    }
    /**
     * add by zhangjd
     * @return
     */
    public synchronized String getLayoutVersion() {
    	if (layoutVersion != null) {
    		return layoutVersion.toLowerCase();
    	}
    	layoutVersion = readXMLFromAssets("appId.xml","layoutVersion");
    	if (TextUtils.isEmpty(layoutVersion)) {
    		throw new IllegalArgumentException("layoutVersion does not exist in approvaladdress.txt error!");
    	}
    	return layoutVersion == null ? null : layoutVersion.toLowerCase();
    }
    /**
     * 获取一级栏目可见权限
     * @return
     */
    public synchronized String getAuth4PartOne() {
    	if (auth4PartOne != null) {
    		return auth4PartOne.toLowerCase();
    	}
    	auth4PartOne = readXMLFromAssets("appId.xml","auth4PartOne");
    	if (TextUtils.isEmpty(auth4PartOne)) {
    		throw new IllegalArgumentException("auth4PartOne does not exist in appid.txt error!");
    	}
    	return auth4PartOne == null ? null : auth4PartOne.toLowerCase();
    }

    /**
     * 获取应用的appPackageId
     * 
     * @return
     * @since 2013-5-31 zhaopuqing
     * @throws 如果配置文件appId.txt不存在或者里面缺少appId，则抛出IllegalArgumentException( "App id does not exist in appid.txt error!")异常
     */
    public synchronized String getAppPackageId() {
        if (appPackageId != null) {
            return appPackageId.toLowerCase();
        }
        appPackageId = readXMLFromAssets("appId.xml","appPackageId");
        return appPackageId;
    }

    /**
     * 获取手机型号信息
     * 
     * @return
     * @since 2013-5-3 下午5:00:04
     * @author zhaopuqing
     */
    public PhoneEnum getPhoneType() {
        String manufacturer = Build.MANUFACTURER;
        if (manufacturer.toLowerCase().contains("htc")) {
            phoneType = PhoneEnum.HTC;
        } else if (manufacturer.toLowerCase().contains("samsung")) {
            phoneType = PhoneEnum.SUMSING;
        } else if (manufacturer.toLowerCase().contains("huawei")) {
            phoneType = PhoneEnum.HUAWEI;
        } else {
            phoneType = PhoneEnum.UNKNOWN;
        }
        return phoneType;
    }
 //   private Toast toast;
    public void setContext(final Context context) {
        this.context = context;
        JHHttpClient.setContext(context);
       
    }
    public String getExternalPath(){
    	return getExternalPath(true);
    }
    public String getExternalPath(boolean visible){
    	return getAppDirPath(true,visible);
    }
    /**
     * 获取系统存放文件的根路径，
     * 
     * 即SD卡中com.jh.freesms.activity文件夹路径
     * 
     * 手机中/data/data/com.jh.freesms.activity/files路径
     * 
     * @since 2013-5-25 wangzhiqiang
     */
    public String getAppDirPath() {
       return getAppDirPath(false);
    }
    private String getAppDirPath(boolean onlyExternal){
    	 return getAppDirPath(onlyExternal, true);
    }
    /**
     * 获取应用缓存文件存放地址
     * @param onlyExternal 是否只放在外部存储
     * @param visible 是否文件夹可见
     * @return
     */
    private String getAppDirPath(boolean onlyExternal,boolean visible){
    	String extendPath = "";
         if (JHExternalStorage.canRead()) {
        	 String path = sdCardRootPath;
        	 if(!visible){
        		 path+=".";
        	 }
        	 path = path + getPackageName()+ File.separator;
             extendPath = creatDirIfNotExists(path);
             if(TextUtils.isEmpty(extendPath)){
            	 extendPath = context.getFilesDir().getAbsolutePath();
             }
         } 
         else if(!onlyExternal){
        	 extendPath = context.getFilesDir().getAbsolutePath();
         }
         return extendPath;
    }
    /**
     * 如果文件夹不存在就创建文件夹
     * 
     * @param dirPath 文件夹路径
     */
    public String creatDirIfNotExists(String dirPath) {
        File dir = new File(dirPath);
        boolean success = true;
        if (!dir.exists()) {
        	success = dir.mkdirs();
        }
        if(!success){
        	return "";
        }
        return dirPath;
    }

    /**
     * 如果文件不存在就创建文件
     * 
     * @return 文件路径
     */
    public String createFileIfNotExists(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            	return "";
            }
        }
        return filePath;
    }

    /**
     * 获取下载路径
     * 
     * 即SD卡中com.jh.freesms.activity/download问价夹路径
     * 
     * @since 2013-5-25 wangzhiqiang
     */
    public String getDownloadPath() {
        return creatDirIfNotExists(getAppDirPath() + File.separator + "download/");
    }

    /**
     * 获取录音文件路径
     * 
     */
    public String getAudioPath() {
        String audioPath = getAppDirPath() + File.separator + "localAudio/";
        File dir = new File(audioPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return audioPath;
    }

    /**
     * 判断ＳＩＭ卡是否可用
     * 
     * @param context
     * @return
     */
    public boolean isCanUseSim(Context context) {
        try {
            TelephonyManager mgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return TelephonyManager.SIM_STATE_READY == mgr.getSimState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取验证app安全的参数
     * 
     * @return
     * @since 2013-5-31 zhaopuqing
     * @throws 如果配置文件appId.txt不存在或者里面缺少appId，则抛出IllegalArgumentException( "App id does not exist in appid.txt error!")异常
     */
    public synchronized String getAccessToken() {
        if (accessToken != null) {
            return accessToken.toLowerCase();
        }
        accessToken = readXMLFromAssets("appId.xml","accessToken");
        return accessToken;
    }

    /**
     * 获取验证app安全的参数
     * 
     * @return
     * @since 2013-5-31 zhaopuqing
     * @throws 如果配置文件appId.txt不存在或者里面缺少appId，则抛出IllegalArgumentException( "App id does not exist in appid.txt error!")异常
     */
    public synchronized String getRefreshToken() {
        if (refreshToken != null) {
            return refreshToken.toLowerCase();
        }
        refreshToken = readXMLFromAssets("appId.xml","refreshToken");
        return refreshToken;
    }
    
    /**
     * @description:从资源文件中读取相应节点的值 返回键值对集合MAP
     * @param filename 文件名 
     * @param nodeName 节点名
     * 
     * */
    public LinkedHashMap<String,String> readXMLListFromAssets(String filename,String nodeName){
    	LinkedHashMap<String,String> maps = new LinkedHashMap<String,String>();
        DocumentBuilderFactory docBuilderFactory = null;
        DocumentBuilder docBuilder = null;
        Document doc = null;
        try {
            docBuilderFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docBuilderFactory.newDocumentBuilder();
            // xml file 放到 assets目录中的
            doc = docBuilder.parse(AppSystem.getInstance().getContext().getResources().getAssets().open(filename));
            Element root = doc.getDocumentElement();
            NodeList nodeList = root.getElementsByTagName(nodeName);
            if (nodeList.getLength()>0) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node nd = nodeList.item(i);
                    Element element = (Element) nd;
                    maps.put(element.getAttribute("id"), element.getAttribute("value"));
                }
            }
        } catch (IOException e) {
        } catch (SAXException e) {
        } catch (ParserConfigurationException e) {
        }catch (NoClassDefFoundError e) {
		} finally {
            doc = null;
            docBuilder = null;
            docBuilderFactory = null;
        }
        return maps;
    
    }
    /**
     * @description: 获取节点内容 返回String
     * @param: xmlName 要多去的xml文件的名称
     * @param: noteId  要读取的节点的Id
     * */
    public String readXMLFromAssets(String xmlName,String noteId) {
        DocumentBuilderFactory docBuilderFactory = null;
        DocumentBuilder docBuilder = null;
        Document doc = null;
        String values = "";
        try {
            docBuilderFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docBuilderFactory.newDocumentBuilder();
            // xml file 放到 assets目录中的
            doc = docBuilder.parse(AppSystem.getInstance().getContext().getResources().getAssets().open(xmlName));
            // root element
            Element root = doc.getDocumentElement();
            // Do something here
            // get a NodeList by tagname
            NodeList nodeList = root.getElementsByTagName("note");
            if (nodeList.getLength()>0) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node nd = nodeList.item(i);
                    Element element = (Element) nd;
                    if (element.getAttribute("id").equals(noteId)) {
                        values = element.getNodeValue();
                        values = element.getAttribute("value");
                        break;
                    }
                }
            }
        } catch (IOException e) {
        	e.printStackTrace();
        } catch (SAXException e) {
        	e.printStackTrace();
        } catch (ParserConfigurationException e) {
        	e.printStackTrace();
        } finally {
            doc = null;
            docBuilder = null;
            docBuilderFactory = null;
        }
        return values;
    }
    
    
    /**
	 * 
	 * 
	 * @description: 从assects中获取相关内容 目录结构和现有的asserts中一致（即是有en zh 等文件夹）
	 * @param fileName 文件名(带后缀名)
	 * @return Sting
	 * 
	 * */
	public String getContentFromAssects(Context context ,String fileName ) {
		String strContent = "";
		String filePath = "";
		String language = Locale.getDefault().getLanguage();
		String county = Locale.getDefault().getCountry();
		if ((language.equals("en") && county.equals("US"))
				|| (language.equals("zh") && county.equals("CN"))
				|| (language.equals("zh") && county.equals("TW"))) {
			filePath = Locale.getDefault().getLanguage() + "/"
					+ Locale.getDefault().getCountry()
					+ "/"+fileName;
		} else {
			filePath = "en/US/"+fileName;
		}
		try {
			InputStream inputStream = context.getResources().getAssets().open(filePath);
			int lenght = inputStream.available();
			byte[] buffer = new byte[lenght];
			inputStream.read(buffer);
			strContent = EncodingUtils.getString(buffer, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strContent;
	}
    
    
    
    
    

    public Context getContext() {
        return context;
    }

}
