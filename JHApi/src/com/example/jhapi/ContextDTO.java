package com.example.jhapi;

import java.io.UnsupportedEncodingException;

import com.jh.net.JHHttpClient;
import com.jh.util.Base64Util;
import com.jh.util.GsonUtil;

public class ContextDTO {
	 private static class LoginInfoDTO{
	        private String AccountType;
	        private String IuAccount;
	        private String IuPassword;
	        private String IweAccount;
	        private String IwuAccount;
	        private String IwuPassword;

	        public String getIuAccount() {
	            return IuAccount;
	        }
	        public void setIuAccount(String iuAccount) {
	            IuAccount = iuAccount;
	        }
	        public String getIuPassword() {
	            return IuPassword;
	        }
	        public void setIuPassword(String iuPassword) {
	            IuPassword = iuPassword;
	        }
	        public String getIweAccount() {
	            return IweAccount;
	        }
	        public void setIweAccount(String iweAccount) {
	            IweAccount = iweAccount;
	        }
	        public String getIwuAccount() {
	            return IwuAccount;
	        }
	        public void setIwuAccount(String iwuAccount) {
	            IwuAccount = iwuAccount;
	        }
	        public String getIwuPassword() {
	            return IwuPassword;
	        }
	        public void setIwuPassword(String iwuPassword) {
	            IwuPassword = iwuPassword;
	        }
	    }
	//实例化对象
	private static ContextDTO context;
	//登录原始请求穿
	private static String originString;
	//加密后串
	private static String encodedString;
	public static void setContext(String contextString) throws UnsupportedEncodingException{
		if(contextString!=null)
		{
			originString = contextString;
			encodedString = Base64Util.encode(contextString.getBytes("utf-8"));
			context = GsonUtil.parseMessage(originString, ContextDTO.class);
		}
	}
	private static String account = "15810695102";
	private static String password = "111111";
	private static void login(){
		 JHHttpClient client = new JHHttpClient();
	        client.setConnectTimeout(30*1000);
	        client.setReadTimeout(30*3000);
	        LoginInfoDTO loginInfoDTO = new LoginInfoDTO();
	        loginInfoDTO.setIuAccount(account);
	        loginInfoDTO.setIuPassword(password);
	        String   result=client.request("http://cbc.iuoooo.com/Jinher.AMP.CBC.SV.UserSV.svc/Login",
	        		"{\"loginInfoDTO\":"+GsonUtil.format(loginInfoDTO)+"}");
	        try {
				setContext(result);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
	}
	public static class UnInitiateException extends Exception{

		/**
		 * 
		 */
		private static final long serialVersionUID = -6903185682914859649L;

		@Override
		public String getLocalizedMessage() {
			// TODO Auto-generated method stub
			return "上下文没有初始化";
		}
		
	}
	public static JHHttpClient getWebClient() throws UnInitiateException{
		if (encodedString == null) {
			login();
		}
		JHHttpClient client = new JHHttpClient();
		client.setConnectTimeout(30000);
		client.setReadTimeout(30000);
		client.addHeader("ApplicationContext", encodedString);
		return client;
	}
	private String ID;
	 private String Latitude;
	 private String Longitude;
	 public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getLatitude() {
		return Latitude;
	}
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	public String getLongitude() {
		return Longitude;
	}
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	public int getLoginCurrentCulture() {
		return LoginCurrentCulture;
	}
	public void setLoginCurrentCulture(int loginCurrentCulture) {
		LoginCurrentCulture = loginCurrentCulture;
	}
	public String getLoginDepartment() {
		return LoginDepartment;
	}
	public void setLoginDepartment(String loginDepartment) {
		LoginDepartment = loginDepartment;
	}
	public String getLoginDepartmentName() {
		return LoginDepartmentName;
	}
	public void setLoginDepartmentName(String loginDepartmentName) {
		LoginDepartmentName = loginDepartmentName;
	}
	public String getLoginIP() {
		return LoginIP;
	}
	public void setLoginIP(String loginIP) {
		LoginIP = loginIP;
	}
	public String getLoginOrg() {
		return LoginOrg;
	}
	public void setLoginOrg(String loginOrg) {
		LoginOrg = loginOrg;
	}
	public String getLoginOrgCode() {
		return LoginOrgCode;
	}
	public void setLoginOrgCode(String loginOrgCode) {
		LoginOrgCode = loginOrgCode;
	}
	public String getLoginOrgName() {
		return LoginOrgName;
	}
	public void setLoginOrgName(String loginOrgName) {
		LoginOrgName = loginOrgName;
	}
	public String getLoginTenantId() {
		return LoginTenantId;
	}
	public void setLoginTenantId(String loginTenantId) {
		LoginTenantId = loginTenantId;
	}
	public String getLoginTenantName() {
		return LoginTenantName;
	}
	public void setLoginTenantName(String loginTenantName) {
		LoginTenantName = loginTenantName;
	}
	public String getLoginTime() {
		return LoginTime;
	}
	public void setLoginTime(String loginTime) {
		LoginTime = loginTime;
	}
	public String getLoginUserCode() {
		return LoginUserCode;
	}
	public void setLoginUserCode(String loginUserCode) {
		LoginUserCode = loginUserCode;
	}
	public String getLoginUserID() {
		return LoginUserID;
	}
	public void setLoginUserID(String loginUserID) {
		LoginUserID = loginUserID;
	}
	public String getLoginUserName() {
		return LoginUserName;
	}
	public void setLoginUserName(String loginUserName) {
		LoginUserName = loginUserName;
	}
	public String getSessionID() {
		return SessionID;
	}
	public void setSessionID(String sessionID) {
		SessionID = sessionID;
	}
	private int LoginCurrentCulture;
	 private String LoginDepartment;
	 private String LoginDepartmentName;
	 private String LoginIP;
	 private String LoginOrg;
	 private String LoginOrgCode;
	 private String LoginOrgName;
	 private String LoginTenantId;
	 private String LoginTenantName;
	 private String LoginTime;
	 private String LoginUserCode;
	 private String LoginUserID;
	 private String LoginUserName;
	 private String SessionID;
}
