package com.jh.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetStatus {
	public static final int CMNET = 3,CMWAP=2,WIFI =1,NONET = -1;;
	public static boolean hasNet(Context context)
	{
		return isNetworkAvailable(context);
	}
	public static boolean isNetworkAvailable(Context context) {   
	  /*try{
		  ConnectivityManager cm = (ConnectivityManager) context   
	                .getSystemService(Context.CONNECTIVITY_SERVICE);   
	        if (cm == null) {   
	        	
	        } else {
		     //如果仅仅是用来判断网络连接
		     //则可以使用 cm.getActiveNetworkInfo().isAvailable();  
	            NetworkInfo[] info = cm.getAllNetworkInfo();   
	            if (info != null) {   
	                for (int i = 0; i < info.length; i++) {   
	                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {   
	                        return true;   
	                    }   
	                }   
	            }   
	        }   
	        return false;  
	  }  
	  catch(Exception e){
		  return false;
	  }*/
		return NetworkUtils.isNetworkAvailable(context);
    }
	/**
	 
     * @author sky
 
     * Email vipa1888@163.com
 
     * QQ:840950105
 
     * 获取当前的网络状态  -1：没有网络  1：WIFI网络2：wap网络3：net网络
 
     * @param context
 
     * @return
 
     */ 
 
    public static int getAPNType(Context context){ 
 
      /*  int netType = -1;  
        try{
        	 ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
        	 
             NetworkInfo networkInfo = connMgr.getActiveNetworkInfo(); 
      
              
      
             if(networkInfo==null){ 
      
                 return netType; 
      
             } 
      
             int nType = networkInfo.getType(); 
      
             if(nType==ConnectivityManager.TYPE_MOBILE){ 
      
      
                 if(networkInfo.getExtraInfo().toLowerCase().equals("cmnet")){ 
      
                     netType = CMNET; 
      
                 } 
      
                 else{ 
      
                     netType = CMWAP; 
      
                 } 
      
             } 
      
             else if(nType==ConnectivityManager.TYPE_WIFI){ 
      
                 netType = WIFI; 
      
             } 
        }
       
        catch(Exception e){
        }
        return netType; */
    	return NetworkUtils.getNetworkType(context) ;
 
    } 
}
