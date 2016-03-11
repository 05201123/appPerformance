
package com.jh.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @description:网络工具类
 * @author chenjinghui
 * @since 2013-5-28
 */
public class NetworkUtils {
    /**
     * 数据流量
     */
    public static final int NETWORK_TYPE_MOBILE = ConnectivityManager.TYPE_MOBILE;
    /**
     * wifi
     */
    public static final int NETWORK_TYPE_WIFI = ConnectivityManager.TYPE_WIFI;

    /**
     * 判断是否有网络连接
     * 
     * @param ctx 上下文环境
     */
    public static boolean isNetworkAvaliable(Context ctx) {
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) ctx
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo net = connectivityManager.getActiveNetworkInfo();
			if (net != null && net.isAvailable() && net.isConnected()) {
				return true;
			} else {
				return false;
			}
		}
    	catch(Exception e){
        }
		return false;
    }

    /**
     * 测试连接
     * 
     * @param ctx 上下文环境
     * @param hostAddress 目标地址
     * @returns 是否能连接上
     */
    public static boolean isServiceReachable(Context ctx, int hostAddress) {
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) ctx
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			return connectivityManager.requestRouteToHost(connectivityManager
					.getActiveNetworkInfo().getType(), hostAddress);
		}
    	catch(Exception e){
        }
    	return false;
    }

    /**
     * 获取网络类型
     * 
     * @param con 上下文环境
     * @return 网络类型
     */
	public static int getNetworkType(Context con) {
		try {
			ConnectivityManager cm = (ConnectivityManager) con
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm == null)
				return NETWORK_TYPE_MOBILE;
			NetworkInfo netinfo = cm.getActiveNetworkInfo();
			if (netinfo != null && netinfo.isAvailable()) {
				if (netinfo.getType() == ConnectivityManager.TYPE_WIFI) {
					return NETWORK_TYPE_WIFI;
				} else {
					return NETWORK_TYPE_MOBILE;
				}
			}
		} catch (Exception e) {
		}
		return NETWORK_TYPE_MOBILE;
	}
    
    /**
     * 检查网络是否可用
     * 
     * @param context
     *            上下文
     * @return 网络可用
     */
	public static boolean isNetworkAvailable(Context context) {
		try {
			final ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity == null) {
				return false;
			} else {
				final NetworkInfo[] info = connectivity.getAllNetworkInfo();
				if (info != null) {
					for (int i = 0; i < info.length; i++) {
						if (info[i].getState() == NetworkInfo.State.CONNECTED) {
							return true;
						}
					}
				}
			}
		} catch (Exception e) {
		}
		return false;
	}
    
}
