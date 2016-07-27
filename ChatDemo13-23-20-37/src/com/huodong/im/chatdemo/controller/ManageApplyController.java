package com.huodong.im.chatdemo.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huodong.im.config.IntentConstant;
import com.huodong.im.config.UrlConstant;
import com.huodong.im.entity.NearbyUserEntity;
import com.huodong.im.entity.UserEntity;
import com.huodong.im.utils.IMDateUserHelper;
import com.huodong.im.utils.IMUIHelper;
import com.huodong.im.utils.LoadDataFromServerNoLooper;
import com.huodong.im.utils.IMUIHelper.DialogCallBack;
import com.huodong.im.utils.LoadDataFromServerNoLooper.DataCallBackNoLooper;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ManageApplyController {
	private static ManageApplyController instance;
	private int uid;
//	private Map<String, String> map;
	private Context context;
	public static ManageApplyController getInstance(Context context){
		if(instance==null){
			instance=new ManageApplyController(context);
		}
		return instance;
	}
	public ManageApplyController(Context context){
		this.context=context;
//		map=new HashMap<String, String>();
	}
	
	public void getApplyedUser(int dateId,final ManageApplyCallback callback){
		final Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
					if(callback!=null){
						callback.onManageApplyCallback(msg);
					}
			}
		};
		Map<String, String> map;
		map=new HashMap<String, String>();
		map.put("dateId", String.valueOf(dateId));
		LoadDataFromServerNoLooper loadTask=new LoadDataFromServerNoLooper(context, UrlConstant.MANAGE_APPLY_URL, map);
		loadTask.getData(new DataCallBackNoLooper() {
			
			@Override
			public void onDataCallBack(String result) {
				if(result!=null){
					List<NearbyUserEntity> list=new ArrayList<NearbyUserEntity>();
					try {
						JSONObject info=new JSONObject(result);
						String flag=info.getString("flag");
						if(flag.equals("false")){
							String error=info.getString("error_infos");
							Message msg=handler.obtainMessage();
							msg.what=IntentConstant.HANDLER_APPLY_MANAGE+IntentConstant.HANDLER_ERROR;
							msg.obj=error;
							handler.sendMessage(msg);
						}
						else {
							JSONArray array=info.getJSONArray("data");
							int len=array.length();
							if(len==0){
								Message msg=handler.obtainMessage();
								msg.what=IntentConstant.HANDLER_APPLY_MANAGE_NO;
								handler.sendMessage(msg);
							}
							for (int i = 0; i < len; i++) {
								JSONObject infos=array.getJSONObject(i);
								NearbyUserEntity entity=new NearbyUserEntity();
								entity.setNickName(infos.getString("name"));
								entity.setBirthday(String.valueOf(IMDateUserHelper.getUserAge(infos.getString("birthday"), System.currentTimeMillis()/1000)));
								entity.setGender(infos.getInt("sex"));
								entity.setRecentTime(infos.getLong("recentTime"));
								entity.setDistance(IMDateUserHelper.formatDistance(infos.getDouble("distance")));
//								entity.setDistance(infos.getDouble("distance"));
								entity.setPhone(infos.getString("phone"));
								entity.setIsPassed(infos.getInt("isPass"));
								entity.setUid(infos.getInt("uid"));
								list.add(entity);
							}
							Collections.sort(list);
							Message msg=handler.obtainMessage();
							msg.what=IntentConstant.HANDLER_APPLY_MANAGE+IntentConstant.HANDLER_OK;
							msg.obj=list;
							handler.sendMessage(msg);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Message msg=handler.obtainMessage();
						msg.what=IntentConstant.HANDLER_APPLY_MANAGE+IntentConstant.HANDLER_ERROR;
						msg.obj=e;
						handler.sendMessage(msg);
					}
					
				}
			}
		});		
	}
	public interface ManageApplyCallback{
		public void onManageApplyCallback(Object obj);
	}
	/*public interface NearbyUserDateCallback{
		public void onNearbyUserDateCallback(int date,int indate);
	}*/
	public void makeAllowed(final int dateId,final int uid,final ManageApplyCallback callback,String dialogTitle) {
		final Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
					if(callback!=null){
						callback.onManageApplyCallback(msg);
					}
			}
		};
		
		IMUIHelper.showDialog(context, dialogTitle, 
				new DialogCallBack() {
			
			@Override
			public void onDialogCallBack(TextView ok,final AlertDialog dialog) {
				ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
							Map<String, String> map;
							map=new HashMap<String, String>();
							map.put("dateId", String.valueOf(dateId));
							map.put("uid", String.valueOf(uid));
							LoadDateFromNet(UrlConstant.ALLOW_APPLY_URL,map,handler,dialog);
					}

				});
			}
		});
	}
	private void LoadDateFromNet(String allowApplyUrl,
			Map<String, String> map, final Handler handler,
			final AlertDialog dialog) {
		LoadDataFromServerNoLooper load=new LoadDataFromServerNoLooper(context, UrlConstant.ALLOW_APPLY_URL, map);
		load.getData(new DataCallBackNoLooper() {
			
			@Override
			public void onDataCallBack(String result) {
				dialog.dismiss();
				if(result!=null){
					try {
						JSONObject info=new JSONObject(result);
						String flag=info.getString("flag");
						if(flag.equals("false")){
							String err=info.getString("errorinfos");
							Message msg=handler.obtainMessage();
							msg.what=IntentConstant.HANDLER_APPLY_MANAGE_ALLOW+IntentConstant.HANDLER_ERROR;
							msg.obj=err;
							handler.sendMessage(msg);
						}
						else {
							Message msg=handler.obtainMessage();
							msg.what=IntentConstant.HANDLER_APPLY_MANAGE_ALLOW+IntentConstant.HANDLER_OK;
							handler.sendMessage(msg);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}
}
