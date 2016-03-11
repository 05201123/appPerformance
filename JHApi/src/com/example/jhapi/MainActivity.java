package com.example.jhapi;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.example.jhapi.ContextDTO.UnInitiateException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jh.app.util.BaseActivity;
import com.jh.app.util.BaseActivityTask;
import com.jh.app.util.BaseExcutor;
import com.jh.app.util.BaseTask;
import com.jh.app.util.ConcurrenceExcutor;
import com.jh.app.util.IResultCallBack;
import com.jh.app.util.SerialExcutor;
import com.jh.common.app.application.AppSystem;
import com.jh.common.cache.FileCache;
import com.jh.exception.JHException;
import com.jh.net.IClient;
import com.jh.net.JHHttpClient;
import com.jh.net.JHHttpClient.ErrorHandler;
import com.jh.net.JHIOException;
import com.jh.persistence.db.DBExecutorHelper;
import com.jh.persistence.file.ExternalStorageState;
import com.jh.util.Base64Util;
import com.jh.util.GsonUtil;
import com.jh.util.LogUtil;
import com.jinher.commonlib.R;

import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.R.integer;
import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends BaseActivity {
    private class jiemian{
        LoginInfoDTO loginInfoDTO;

        public LoginInfoDTO getLoginInfoDTO() {
            return loginInfoDTO;
        }

        public void setLoginInfoDTO(LoginInfoDTO loginInfoDTO) {
            this.loginInfoDTO = loginInfoDTO;
        }
    }
    private class PersonLogin{
        private String Name;
        private int AccountType;
        private String Password;
        public String getName() {
            return Name;
        }
        public void setName(String name) {
            Name = name;
        }
        public int getAccountType() {
            return AccountType;
        }
        public void setAccountType(int accountType) {
            AccountType = accountType;
        }
        public String getPassword() {
            return Password;
        }
        public void setPassword(String password) {
            Password = password;
        }
    }
    private class LoginInfoDTO{
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
    /**
     * 获取缓存的文件大小
     * @return long
     */
    public static long getFileSize(File file){
        long l=0;
        if(file.exists()){
        	if(file.isDirectory()){
        		final File[] files = file.listFiles();
                if(files!=null&&files.length>0){
                	 for(File f:files){
                         if(f.exists()){
                             long length=f.length();
                             l+=length;
                         }
                     }
                }
        	}
        	else{
        		l = file.length();
        	}
        }
        return l;
    }
    DBExecutorHelper helper;
    List<BaseTask> tasks;
    ListView list;
    private class TestAdapter extends BaseAdapter{
    	private List<String> lists;
    	public TestAdapter(List<String> lists){
    		this.lists = lists;
    	}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lists.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return lists.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView root;
			// TODO Auto-generated method stub
			if(convertView!=null){
				root = (TextView)convertView;
			}
			else{
				root = new TextView(MainActivity.this);
			}
			root.setText(lists.get(position));
			BaseTask task1 = new BaseActivityTask(MainActivity.this,"12312",false) {
				
				@Override
				public void doTask() throws JHException {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(10000);
						Log.i("onCreate", "onCreate");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			BaseTask tmpTask = (BaseTask)root.getTag();
			if(tmpTask!=null){
				Log.e("tmpTask", "tmpTask not null");
				MainActivity.this.removeTask(tmpTask);
			}
			root.setTag(task1);
		// 	MainActivity.this.executeTaskIfNotExist(task1);
			return root;
		}
    	
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    //    this.showLoading("123213", false);
        JHHttpClient client = new JHHttpClient();
        AppSystem.getInstance().setContext(this);
        client.setGlobalReadTimeout(50000);
        client.setGlobalConnectTimeout(50000);
        client.setGlobalRetryTimes(2);
 //       client.request("http://www.baidu12312.com", "1231");
  //      helper.excute(task)
/*        byte[] str1 = new String("5C3C98843016ECF267B41A0063A229D5").getBytes();
        String wwww = Base64Util.encode(str1);
        JHHttpClient client = new JHHttpClient();
        
        client.setContext(this);
        String result = client.getData("http://hanyh.iteye.com/blog/1725733", true);
      //  this.setMaxThreads(1);
      //filePath = "//sdcard//新建文件夹//Pod.m4v";
      		String filePath = "//sdcard//新建文件夹//";
      		File file = new File(filePath);
      		tasks = new ArrayList<BaseTask>();
      	//	list = (ListView)this.findViewById(R.id.list);
      		List<String> lists = new ArrayList<String>();
      		for(int index=0;index<25;index++)
      		{
      			lists.add("texst"+index);
      		}
      		list.setAdapter(new TestAdapter(lists));*/
      	//	this.showLoading("title", "message", true);
        /*     		BaseTask task1 = new BaseActivityTask(this,"12312",false) {
				
				@Override
				public void doTask() throws JHException {
					// TODO Auto-generated method stub
					throw new JHException();
				}

				@Override
				public void success() {
					// TODO Auto-generated method stub
					super.success();
				}

				@Override
				public void fail(String errorMessage) {
					// TODO Auto-generated method stub
					super.fail(errorMessage);
				}

				@Override
				public void fail(JHException e) {
					// TODO Auto-generated method stub
					super.fail(e);
				}
				
			};
			this.excuteTask(task1);
      		for(int index=0;index<100;index++)
      		{
      			BaseTask task1 = new BaseActivityTask(this,"12312",false) {
    				
    				@Override
    				public void doTask() throws JHException {
    					// TODO Auto-generated method stub
    					try {
    						Thread.sleep(10000);
    						Log.i("onCreate", "onCreate");
    						
    					} catch (InterruptedException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
    				}
    			};
    			tasks.add(task1);
    			//this.executeTaskIfNotExist(task1);
      		}
      		new Thread(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					for(int index=0;index<100;index++)
		      		{
						MainActivity.this.executeTaskIfNotExist(tasks.get(index));
		      		}
				}
      			
      		}.start();
      		new Thread(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					for(int index=0;index<100;index++)
		      		{
						MainActivity.this.removeTask(tasks.get(index));
		      		}
				}
      			
      		}.start();*/
    /*    AppSystem.getInstance().setContext(this);
        FileCache fileCache = FileCache.getInstance(this);
        fileCache.clear();
       // this.showLoading("12312");
        BaseActivityTask task1 = new BaseActivityTask(this,"123123"){

			@Override
			public void doTask() throws JHException {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(50000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}};
		this.excuteTask(task1);
        LoginInfoDTO dto1 = new LoginInfoDTO();
        dto1.setIuAccount("12312");
        JHHttpClient client = new JHHttpClient();
        client.setRetryTimes(2);
        client.setErrorHandler(new ErrorHandler() {
			
			@Override
			public void error(int responseCode, String responseMessage) {
				// TODO Auto-generated method stub
				int code = responseCode;
				code = 0;
			}
		});
        try {
        	client.request("http://192.168.10.15:8888/c6v3.2/JHSoft.WCF/POSTServiceForAndroid.svc/GetServerVison", "");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
        BaseTask task = new BaseTask(){

			@Override
			public void doTask() throws JHException {
				// TODO Auto-generated method stub
				Log.i("error", "doTask");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}};*/
       /* for(int ii=0;ii<10;ii++){
        	this.executeExclude(task);
        }*/
			helper = new DBExecutorHelper(new TestDBHelper(this)); 
			ContentValues values = new ContentValues();
			for(int ii=0;ii<1000;ii++){
				/*values.put("content", "123");
		      //  helper.insert(TestDBHelper.TABLE_NAME, null, values);
		        new Thread(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						super.run();
						 helper.close();
					}
		        	
		        }.start();
		       
		        values.put("content", "456");
		      //  helper.insert(TestDBHelper.TABLE_NAME, null, values);
		        helper.excute(helper.new TranctionTask() {
					
					@Override
					public void excuteTranction(SQLiteDatabase dbwriter) throws Exception {
						// TODO Auto-generated method stub
						 Cursor cursor = dbwriter.query(TestDBHelper.TABLE_NAME, null, null, null, null, null, null);
						 Log.i("count", cursor.getCount()+"");
						 cursor.close();
					}
				});*/
		        Object object = helper.executeBlock(helper.new TranctionTask() {
					
					@Override
					public void excuteTranction(SQLiteDatabase dbwriter) throws Exception {
						// TODO Auto-generated method stub
						 Cursor cursor = dbwriter.query(TestDBHelper.TABLE_NAME, null, null, null, null, null, null);
						 Log.i("count", cursor.getCount()+"");
						 cursor.close();
						 this.setResult("12312");
					}
				});
		        Log.i("test", object+"");
	        } 
   /*     List<LoginInfoDTO> list = new ArrayList<MainActivity.LoginInfoDTO>();
        list.add(dto1);
        List<LoginInfoDTO> list2 = GsonUtil.parseList(GsonUtil.format(list), LoginInfoDTO.class);
        list2 = null;
        JsonParser parser = new JsonParser();
        
            Type listType = new TypeToken<ArrayList>(){}.getType();
        
            Gson gson = new Gson();
        
            
        
            JsonElement element = parser.parse(GsonUtil.format(list));
            JsonArray array = element.getAsJsonArray();
            LinkedList list123 = new LinkedList();
            for(int ii=0;ii<array.size();ii++){
            	list123.add(gson.fromJson(array.get(ii), LoginInfoDTO.class));
            }
            array = null;
        /*       List<String> ttt = ExternalStorageState.getAvailableStorage();
        File file1 = new File(ttt.get(0)+"//news_log.txt");
        long len = file1.length();
        String str33 = Environment.getExternalStorageDirectory().toString();
        file1 = new File(str33+"//audio.raw");
        len = file1.length();
        List<String> str1 = new ArrayList<String>();
        str1.add(null);
        str1.add("212312");
        Log.i("index", str1.get(0)+"");
        SharedPreferences preferences = this.getSharedPreferences("name", MODE_PRIVATE );
        Editor editor = preferences.edit();
        editor.putString("name", "131231");
        Log.i("name", preferences.getString("name", ""));
        File file = new File("1.txt");
        JHHttpClient client = new JHHttpClient();
        client.uploadFile("1.txt", "http://www.baidu.com");
        this.excuteTask(new BaseActivityTask(this,"123123") {
			
			@Override
			public void doTask() throws JHException {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(10*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void cancel() {
				// TODO Auto-generated method stub
				super.cancel();
				//System.out.println("cancel");
				Log.i("task","cancel");
			}

			@Override
			public void cancelTask() {
				// TODO Auto-generated method stub
				super.cancelTask();
				Log.i("task","cancelTask");
			}
			
		});
        SerialExcutor excutor = SerialExcutor.getInstance();
		excutor.execute(new BaseTask() {
			
			@Override
			public void doTask() throws JHException {
				// TODO Auto-generated method stub
				//LogUtil.println("zhnn123");
				Log.i("12312", "zhnn123");
			}

			@Override
			public void success() {
				// TODO Auto-generated method stub
				super.success();
				Log.i("12312", "zhnn123");
			}

			@Override
			public void fail(String errorMessage) {
				// TODO Auto-generated method stub
				super.fail(errorMessage);
				Log.i("12312", "zhnn123");
			}
			
		});
		excutor.execute(new BaseTask() {
			
			@Override
			public void doTask() throws JHException {
				// TODO Auto-generated method stub
				Log.i("456", "456");
			}
			@Override
			public void success() {
				// TODO Auto-generated method stub
				super.success();
				Log.i("456", "456");
			}

			@Override
			public void fail(String errorMessage) {
				// TODO Auto-generated method stub
				super.fail(errorMessage);
				Log.i("456", "456");
			}
		});
		this.request("http://cbc.iuoooo.com/Jinher.AMP.CBC.SV.UserSV.svc/AnonymousRegister",
				"{\"anonymousUserDTO\":null" +
    			"}",new IResultCallBack<jiemian>(){

					@Override
					public void success(jiemian result) {
						// TODO Auto-generated method stub
						Log.i("tag", "erer");
					}

					@Override
					public void fail(String errorMes) {
						// TODO Auto-generated method stub
						Log.i("error", errorMes);
					}},jiemian.class);*/
   /* //    new BaseExcutor(null).execute(null);
        String[] accounts = new String[]{//"13956761215","13956761211",
        		//"13956761214","13956761213",
        		//"18670315127",
        		"13311111111"
        		
        		,"15810695102","13161628747",
        		"18911992978","15801223040","13811998201",
        		"13810374786","15210594006","13691589615",
        		"13011868125","15531975632","13488816277",
        		"15110221373","13070120350","18832610615"};
        JHHttpClient client = new JHHttpClient();
        client.setConnectTimeout(30*1000);
        client.setReadTimeout(30*1000);
        AnonymousUserDTO user = new AnonymousUserDTO();
        user.setPassword("111111");
        String www = GsonUtil.format(accounts);
        List ttt = GsonUtil.parseMessage(www, List.class);
        Class ff = List.class;
        try {

			ContextDTO.getWebClient();
            for(String account:accounts)
            {
            	try {
            	user.setAccount(account);
            	client.request("http://cbc.iuoooo.com/Jinher.AMP.CBC.SV.UserSV.svc/AnonymousRegister", 
            			"{\"anonymousUserDTO\":" +GsonUtil.format(user)+
            			"}");
            	
    				Thread.sleep(10*1000);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
            	catch(JHIOException e){
            		
            	}
            }
		} catch (UnInitiateException e) {
			e.printStackTrace();
		}
        
  //      PinYin.getFirst("李强～123");
  //      PinYin.close();
  //      PinYin.getFirst("李强~123");
 //       ContactsContract.CommonDataKinds.Email.
   ///     String serviceUrl = "http://118.194.166.149/Jinher.IWE.ADM.SV.CreateAccessTokenSV.svc/Do";
        String serviceUrl = "http://cbc.iuoooo.com/Jinher.AMP.CBC.SV.UserSV.svc/Login";
        JSONObject param = new JSONObject();
        
//      param.put("IuAccount", "123");
//      param.put("IuPassword", "123");
//      param.put("IweAccount", "123");
//      param.put("IwuAccount", "123");
//      param.put("IwuPassword", "123");
//      param.put("AccountType", "123");
//      param.put("BaseDto", "123");
        LoginInfoDTO loginInfoDTO = new LoginInfoDTO();
        loginInfoDTO.setIuAccount("liuchao1");
        loginInfoDTO.setIuPassword("111111");
        List<LoginInfoDTO> arrays = new LinkedList<LoginInfoDTO>();
        arrays.add(loginInfoDTO);
        www = GsonUtil.format(arrays);
        ttt = GsonUtil.parseMessage(www, List.class);
   //     loginInfoDTO.setIweAccount("fgf");
    //    loginInfoDTO.setIwuAccount("213123");
   //     loginInfoDTO.setIwuPassword("asdfasd");
        PersonLogin login = new PersonLogin();
        login.setAccountType(1);
        login.setName("liuchao");
        login.setPassword("111111");
       // JHHttpClient client = new JHHttpClient();
        client.setConnectTimeout(30*1000);
        client.setReadTimeout(30*3000);
        jiemian jj = new jiemian();
        jj.setLoginInfoDTO(loginInfoDTO);
        String   result=client.request(serviceUrl, GsonUtil.format(jj));
        JHHttpClient client1 = new JHHttpClient();
		try {
			client1.setConnectTimeout(30 * 1000);
			client1.setReadTimeout(30 * 3000);
			client1.addHeader("ApplicationContext",
					Base64Util.encode(result.getBytes("utf-8")));
			client1.request(
					"http://cbc.iuoooo.com/Jinher.AMP.App.BP.AppManagerBP.svc/GetRecommendAppsByHostType",
					"{\"hostType\":1,\"pageNumber\":1,\"pageSize\":6}");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    }
}
