package com.huodong.im.chatdemo.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.huodong.im.config.IntentConstant;
import com.huodong.im.config.UrlConstant;
import com.huodong.im.entity.UserEntity;
import com.huodong.im.utils.LoadDataFromServerNoLooper;
import com.huodong.im.utils.LoadDataFromServerNoLooper.DataCallBackNoLooper;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class NearbyUserDetailController {
	private static NearbyUserDetailController instance;
	private int uid;
//	private Map<String, String> map;
	private Context context;
	public static NearbyUserDetailController getInstance(Context context){
		if(instance==null){
			instance=new NearbyUserDetailController(context);
		}
		return instance;
	}
	public NearbyUserDetailController(Context context){
		this.context=context;
//		map=new HashMap<String, String>();
	}
	public UserEntity getUserBasicInfo(int uid,final NearbyUserDetailCallback callback){
		final Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
//				switch (msg.what) {
//				case IntentConstant.HANDLER_USER_INFO+IntentConstant.HANDLER_OK:
					if(callback!=null){
						callback.onNearbyUserDetailCallback(msg);
					}
//					break;
//				case IntentConstant.HANDLER_USER_INFO+IntentConstant.HANDLER_ERROR:
//					if(callback!=null){
//						callback.onNearbyUserDetailCallback(msg.obj);
//					}
//					break;

//				default:
//					break;
//				}
			}
		};
		Map<String, String> map;
		map=new HashMap<String, String>();
//		map.clear();
		map.put("id", String.valueOf(uid));
		LoadDataFromServerNoLooper loadTask=new LoadDataFromServerNoLooper(context, UrlConstant.NEARBY_USER_DETAIL_URL, map);
		loadTask.getData(new DataCallBackNoLooper() {
			
			@Override
			public void onDataCallBack(String result) {
				if(result!=null){
//					UserEntity entity=paraseUserInfoJson(result);
					UserEntity entity=new UserEntity();
					try {
						JSONObject info=new JSONObject(result);
						String flag=info.getString("flag");
						if(flag.equals("false")){
							String error=info.getString("error_infos");
							Message msg=handler.obtainMessage();
							msg.what=IntentConstant.HANDLER_USER_INFO+IntentConstant.HANDLER_ERROR;
							msg.obj=error;
							handler.sendMessage(msg);
						}
						else {
							entity.setNickName(info.getString("name"));
							entity.setBirthday(info.getString("birthday"));
							entity.setGender(info.getInt("sex"));
							entity.setJob(info.getString("job"));
							entity.setHight(info.getInt("hight"));
							entity.setRecentTime(info.getLong("recentTime"));
							entity.setIncome(info.getInt("income"));
							entity.setLongitude(info.getDouble("longitude"));
							entity.setLatitude(info.getDouble("latitude"));
							entity.setSignature(info.getString("PSignature"));
							Message msg=handler.obtainMessage();
							msg.what=IntentConstant.HANDLER_USER_INFO+IntentConstant.HANDLER_OK;
							msg.obj=entity;
							handler.sendMessage(msg);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Message msg=handler.obtainMessage();
						msg.what=IntentConstant.HANDLER_USER_INFO+IntentConstant.HANDLER_ERROR;
						msg.obj=e;
						handler.sendMessage(msg);
					}
					
				}
			}
		});
		return null;
		
	}
	
	public UserEntity getDateCountInfo(int uid, final NearbyUserDateCallback callback){
		Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				
				super.handleMessage(msg);
			}
		};
		Map<String, String> map;
		map=new HashMap<String, String>();
		map.put("uid", String.valueOf(uid));
		LoadDataFromServerNoLooper loadTask=new LoadDataFromServerNoLooper(context, UrlConstant.GET_DATE_COUNT_URL, map);
		loadTask.getData(new DataCallBackNoLooper() {
			
			@Override
			public void onDataCallBack(String result) {
				if(result!=null){
					try {
						JSONObject info=new JSONObject(result);
						String flag=info.getString("flag");
						if(flag.equals("true")){
							int datecount=info.getInt("date_count");
							int applycount=info.getInt("apply_count");
							callback.onNearbyUserDateCallback(datecount,applycount);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		});
		return null;
		
	}
	public interface NearbyUserDetailCallback{
		public void onNearbyUserDetailCallback(Object obj);
	}
	public interface NearbyUserDateCallback{
		public void onNearbyUserDateCallback(int date,int indate);
	}
}
