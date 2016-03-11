package com.jh.net;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.http.HttpResponse;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.jh.app.util.RunnableExecutor;
import com.jh.common.app.application.AppSystem;
import com.jh.net.bean.RequestRecoder;
import com.jh.net.bean.ResultDTO;
import com.jh.net.bean.WebSiteCDTO;
import com.jh.net.db.SwitchIpDBHelper;

public class SwitchIP {
	Context context;
	static GetWebIP getIPservice;

	public static void setGetIPservice(GetWebIP getIPservice) {
		SwitchIP.getIPservice = getIPservice;
	}

	public SwitchIP(Context context) {
		this.context = context;
	}

	// private String domain = null;
	// private int responseCode;
	/*public Runnable getNeedFirstReqRunnable() {
		return new NeedFirstQuest();
	}*/
	public void firstReqRunnable(){
		executor.executeTask(new NeedFirstQuest());
	}
	
    public boolean hasSaved(String userId){
    	return AppSystem.getInstance().getContext().getSharedPreferences("switchip", 0).getBoolean(userId, false);
    }
    public void setSaved(String userId){
    	AppSystem.getInstance().getContext().getSharedPreferences("switchip", 0).edit().putBoolean(userId, true).commit();
    }
	// 内存维护的请求结果记录
	static HashMap<String, Queue<RequestRecoder>> sum;// = new HashMap<String,
														// Queue<RequestRecoder>>();;//
														// =
														// 这个是最终的
	static RunnableExecutor executor = new RunnableExecutor(1);
	protected void addRequestRecord(String url, HttpResponse response,
			IOException e) {
		if (getIPservice == null) {
			return;
		}
		Uri resource = Uri.parse(url);
		String domain = resource.getScheme() + "://" + resource.getAuthority()
				+ "/";
		int responseCode;
		if(response==null){
			responseCode = -100;
		}else{
			responseCode = response.getStatusLine().getStatusCode();
		}
		String lastUserId = getIPservice.getUserId();
		SwitchIpDBHelper dbHelper = SwitchIpDBHelper.getInstance(context);
		if (!"".equals(lastUserId)) {
			synchronized (SwitchIP.this) {
				if (!hasSaved(lastUserId)) {
					// new Thread(needFirstQuestTask).start();NeedFirstQuest
					executor.executeTask(
							new NeedFirstQuest());
				}
			}
		}

		// 初始化
		if (sum == null) {
			synchronized (SwitchIP.class) {
				if (sum == null) {
					sum = new HashMap<String, Queue<RequestRecoder>>();
				}
			}
		}
		// 更新状态信息
		
		
		synchronized (SwitchIP.class) {
			if (responseCode >= 500 || e != null) {

				RequestRecoder failRecode = new RequestRecoder();
				failRecode.setStatus(1);// 1 domain失效
				failRecode.setResponse(responseCode);

				Queue<RequestRecoder> temQueueDomainRecoder;
				LinkedList<RequestRecoder> paramsneedUpdateStatusToFail;
				if (sum.get(domain) != null&&sum.get(domain).size()>0) {
					temQueueDomainRecoder = sum.get(domain);

					// 具体的domain记录
					if (temQueueDomainRecoder.size() > 19) {
						// 出队列
						temQueueDomainRecoder.poll();
					}
					// 入队
					temQueueDomainRecoder.offer(failRecode);
				//	paramsneedUpdateStatusToFail = temQueueDomainRecoder;
					sum.put(domain, temQueueDomainRecoder);
					printSum(domain,"first");
				} else {
					temQueueDomainRecoder = new LinkedList<RequestRecoder>();
					// 入队
					temQueueDomainRecoder.offer(failRecode);
					sum.put(domain, temQueueDomainRecoder);
					printSum(domain,"more");
				}
				paramsneedUpdateStatusToFail =new LinkedList<RequestRecoder>();
				Iterator<RequestRecoder> iterator = temQueueDomainRecoder.iterator();
				while(iterator.hasNext()){
					paramsneedUpdateStatusToFail.add( iterator.next());
				}
				//paramsneedUpdateStatusToFail.addAll(temQueueDomainRecoder);
				// 是否需要更新数据库
				if (needUpdateStatusToFail(paramsneedUpdateStatusToFail,domain)) {

					dbHelper.updateStatusToFail(domain, lastUserId, responseCode
							+ "");
				}
				// 是否需要请求
					if (dbHelper.queryNeedQuestByDomain(domain, lastUserId)) {
						// 需要，那么发出请求
						if (getIPservice != null) {// !"".equals(lastUserId)
							// dbHelper.queryBizCodeByDomain(domain,getIPservice.getUserId());
							// new Thread(new SaveToDB()).start();

							executor.executeTask(
									new SaveToDBVersion2(domain, responseCode));
							//sum.remove(domain);
						}
					}
			} else {
				RequestRecoder sucRecode = new RequestRecoder();
				sucRecode.setStatus(0);// 1 domain失效
				sucRecode.setResponse(responseCode);

				Queue<RequestRecoder> temQueueDomainRecoder;
				if (sum.get(domain) != null) {
					temQueueDomainRecoder = sum.get(domain);

					// 具体的domain记录
					if (temQueueDomainRecoder.size() > 19) {
						// 出队列
						temQueueDomainRecoder.poll();
					}
					// 入队
					temQueueDomainRecoder.offer(sucRecode);
					sum.put(domain, temQueueDomainRecoder);
					//printSum(domain);
				} else {
					temQueueDomainRecoder = new LinkedList<RequestRecoder>();

					// 入队
					temQueueDomainRecoder.offer(sucRecode);
					sum.put(domain, temQueueDomainRecoder);
					//printSum(domain);
				}
			}
		}
		
	}

	/**
	 * 判断是否需要将domain失效
	 */
	private boolean needUpdateStatusToFail(
			Queue<RequestRecoder> temQueueDomainRecoder,String domain) {
		// 判断失败 1 连续三条失败或者记录有五条并且失败率多于20%
		// 前两次是否是同一个domain，并且都是失败的

		long failedNum = 0;

		int recoderNum = temQueueDomainRecoder.size();
		// 将记录放在数组中；
		RequestRecoder requestRecoders[] = new RequestRecoder[recoderNum];
		for (int ii = 0; ii < recoderNum; ii++) {
			requestRecoders[ii] = temQueueDomainRecoder.poll();
			if (requestRecoders[ii].getStatus() == 1) {
				failedNum++;
			}
		}
		if (recoderNum > 5 && failedNum / recoderNum > 0.2) {
			sum.remove(domain); 
			return true;
		}

		if (recoderNum >= 3) {
			if (requestRecoders[recoderNum-1].getStatus() == 1
					&& requestRecoders[recoderNum-2].getStatus() == 1) {
				sum.remove(domain);
				return true;
			}
		}

		return false;
	}

	private class SaveToDB implements Runnable {
		private String domain;
		private int responseCode;

		private SaveToDB(String domain, int responseCode) {
			super();
			this.domain = domain;
			this.responseCode = responseCode;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(getIPservice==null)
				return;
			List<ResultDTO> results;
			SwitchIpDBHelper dbHelper = SwitchIpDBHelper.getInstance(context);

			// 非第一次请求 else domain,responseCode,Bizcode
			int bizCode = dbHelper.queryBizCodeByDomain(domain,
					getIPservice.getUserId());
			
			results = getIPservice.getAddresses(domain, responseCode, bizCode);
			dbHelper.deleteTableByBizCode(bizCode + "",
					getIPservice.getUserId());
				for (int ii = 0; ii < results.get(0).getWebSiteCDTO().size(); ii++) {
					WebSiteCDTO webSiteDTO = results.get(0).getWebSiteCDTO()
							.get(ii);
					dbHelper.initOrInsertSwitchIPTable(results.get(0).getBizCode(),
							webSiteDTO.getId(), webSiteDTO.getName(),
							webSiteDTO.getDomain(), webSiteDTO.getIP(),
							webSiteDTO.getCode(), getIPservice.getUserId());
				}
			
		}

	}
	public void saveToByCode(int bizCode){
		if(bizCode!=0){
			SaveToDBByCode task = new SaveToDBByCode();
			task.setBizCode(bizCode);
			executor.executeTask(task);
		}
		
	}
	public class SaveToDBByCode implements Runnable{

		protected int bizCode;
		public void setBizCode(int bizCode) {
			this.bizCode = bizCode;
		}
		@Override
		public void run() {
			if(getIPservice==null)
				return;
			SwitchIpDBHelper dbHelper = SwitchIpDBHelper.getInstance(context);

			// results = getIPservice.getAddresses(domain, responseCode,
			// bizCode);
			List<ResultDTO> results;
			results = getIPservice.getAddresses(
					dbHelper.getListDomainInfo(bizCode + "",
							getIPservice.getUserId()), bizCode);
			
			if (results != null) {
				for(int index=0;index<results.size();index++){
					if(results.get(index).getBizCode()!=0){
						dbHelper.deleteTableByBizCode(bizCode + "",
								getIPservice.getUserId());
					}
					
					for (int ii = 0; ii < results.get(index).getWebSiteCDTO().size(); ii++) {
						WebSiteCDTO webSiteDTO = results.get(index).getWebSiteCDTO()
								.get(ii);
						dbHelper.initOrInsertSwitchIPTable(results.get(index)
								.getBizCode(), webSiteDTO.getId(), webSiteDTO
								.getName(), webSiteDTO.getDomain(), webSiteDTO
								.getIP(), webSiteDTO.getCode(), getIPservice
								.getUserId());
					}
				}
				
			}

		}
		
	}
	public class SaveToDBVersion2 extends SaveToDBByCode {
		private String domain;
		private int responseCode;

		private SaveToDBVersion2(String domain, int responseCode) {
			super();
			this.domain = domain;
			this.responseCode = responseCode;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(getIPservice==null)
				return;
				List<ResultDTO> results;
				SwitchIpDBHelper dbHelper = SwitchIpDBHelper
						.getInstance(context);

				// 非第一次请求 else domain,responseCode,Bizcode
				
				bizCode = dbHelper.queryBizCodeByDomain(domain,
						getIPservice.getUserId());
				super.run();
				/*// results = getIPservice.getAddresses(domain, responseCode,
				// bizCode);
				results = getIPservice.getAddresses(
						dbHelper.getListDomainInfo(bizCode + "",
								getIPservice.getUserId()), bizCode);
				dbHelper.deleteTableByBizCode(bizCode + "",
						getIPservice.getUserId());
				for (int ii = 0; ii < results.get(0).getWebSiteCDTO().size(); ii++) {
					WebSiteCDTO webSiteDTO = results.get(0).getWebSiteCDTO()
							.get(ii);
					dbHelper.initOrInsertSwitchIPTable(results.get(0)
							.getBizCode(), webSiteDTO.getId(), webSiteDTO
							.getName(), webSiteDTO.getDomain(), webSiteDTO
							.getIP(), webSiteDTO.getCode(), getIPservice
							.getUserId());
				}*/
		}

	}

	class NeedFirstQuest implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(getIPservice==null)
				return;
				SwitchIpDBHelper dbHelper = SwitchIpDBHelper
						.getInstance(context);
				Log.e("userId", getIPservice.getUserId());
				if (!dbHelper.queryExistRecoder(getIPservice.getUserId())) {
					Log.e("userId", "NeedFirstQuest ");
					List<ResultDTO> results;
					// 第一次请求 if
					results = getIPservice.getAddresses("", 0, 0);
					// 2 写入数据库中
					for (int ii = 0; ii < results.size(); ii++) {
						ResultDTO result = results.get(ii);
						for (int jj = 0; jj < result.getWebSiteCDTO().size(); jj++) {
							WebSiteCDTO webSiteDTO = result.getWebSiteCDTO()
									.get(jj);
							dbHelper.initOrInsertSwitchIPTable(
									result.getBizCode(), webSiteDTO.getId(),
									webSiteDTO.getName(),
									webSiteDTO.getDomain(), webSiteDTO.getIP(),
									webSiteDTO.getCode(),
									getIPservice.getUserId());
						}
					}
			}
		}

	}

	/*
	 * static Queue<HashMap<String, Queue<RequestRecoder>>> sum_; private void
	 * twentyDomainReqestRecoder(String domain, Queue<RequestRecoder>
	 * temQueueDomainRecoder) { if(sum==null){ sum = new
	 * LinkedList<HashMap<String, Queue<RequestRecoder>>>(); }
	 * 
	 * if (sum.size() > 19) { sum.poll(); } HashMap<String,
	 * Queue<RequestRecoder>> temMap = new HashMap<String,
	 * Queue<RequestRecoder>>(); temMap.put(domain, temQueueDomainRecoder);
	 * sum.offer(temMap); }
	 */
	
	private void printSum(String domain,String location) {
		Queue<RequestRecoder> queueItem = sum.get(domain);
	//	RequestRecoder[] RequestRecoderS = new RequestRecoder[queueItem.size()];
		/*queueItem、。
		for (int ii = 0; ii < queueItem.size(); ii++) {
			RequestRecoderS[ii] = queueItem.poll();
			android.util.Log.i("sum",RequestRecoderS[ii].getResponse()+":"+RequestRecoderS[ii].getStatus()+":"+domain+":"
			+SwitchIP.sum.size()+":"+RequestRecoderS.length+":"+location);
		}*/
		Iterator<RequestRecoder> iterator = queueItem.iterator();
		while(iterator.hasNext()){
			//paramsneedUpdateStatusToFail.add(0, iterator.next());
			RequestRecoder request = iterator.next();
			android.util.Log.i("sum",request.getResponse()+":"+request.getStatus()+":"+domain+":"
			+SwitchIP.sum.size()+":"+":"+location);
		}
		

	}
}
