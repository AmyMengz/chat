package com.huodong.im.chatdemo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.huodong.im.chat.mode.ChatMode;
import com.huodong.im.chat.mode.UserMode;
import com.huodong.im.chat.service.ChatInterface;
import com.huodong.im.chat.service.ChatIoHandler;
import com.huodong.im.chat.service.ChatServiceConn;
import com.huodong.im.chat.util.IMConstants;
import com.huodong.im.chat.util.NetUtil;
import com.huodong.im.chat.util.ToastUtil;
import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.activity.DateDetailActivity;
import com.huodong.im.chatdemo.activity.MyApplication;
import com.huodong.im.config.IntentConstant;
import com.huodong.im.config.UrlConstant;
import com.huodong.im.utils.Base64Coder;
import com.huodong.im.utils.IMUIHelper;
import com.huodong.im.utils.ImageDownLoader;
import com.huodong.im.utils.LoadDataFromServerNoLooper;
import com.huodong.im.utils.MD5Encoder;
import com.huodong.im.utils.IMUIHelper.DialogCallBack;
import com.huodong.im.utils.LoadDataFromServerNoLooper.DataCallBackNoLooper;

public class DateDetailController {
	
	private DateDetailController instance;
	private Context context;
	private ImageDownLoader imageDownLoader;
	String send_fail,please_check_net;
	private MyApplication yAPP;
	
	public DateDetailController getInstance(Context context){
		if(instance==null){
			instance=new DateDetailController(context);
		}
		return instance;
	}
	public DateDetailController(Context context){
		this.context=context;
		imageDownLoader=new ImageDownLoader(context);
		send_fail = context.getResources().getString(R.string.send_fail);
		please_check_net = context.getResources().getString(R.string.please_check_net);
		yAPP = MyApplication.getInstance();
		
	}
	/**
	 * 获取申请信息
	 * @param map
	 * @param applyDateCallBack
	 */
	public void getApplyInfoFromNet(final Map<String, String> map,final DetailDateCallBack applyDateCallBack){
		final Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg){
				if(applyDateCallBack!=null){
					applyDateCallBack.onDetailDateCallBack(msg.what,msg.obj);
				}
			}
		};
		LoadDataFromServerNoLooper loadTask=new LoadDataFromServerNoLooper(context, UrlConstant.DATE_DETAIL_URL, map);
		loadTask.getData(new DataCallBackNoLooper() {
			
			@Override
			public void onDataCallBack(String result) {
				if(result!=null){
					JSONObject info;
					try {
						info = new JSONObject(result);
						String flag=info.getString("flag");
						if(flag.equals("true")){
							int applyState=info.getInt("apply_state");
							JSONArray allowed=info.getJSONArray("data");
							Message msg=handler.obtainMessage();
							msg.what=IntentConstant.HANDLER_GET_ALLOW_STATE+IntentConstant.HANDLER_OK;
							msg.obj=allowed;
							handler.sendMessage(msg);
						}else {
							String error=info.getString("error_infos");
							Message msg=handler.obtainMessage();
							msg.what=IntentConstant.HANDLER_GET_ALLOW_STATE+IntentConstant.HANDLER_ERROR;
							msg.obj=error;
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
	public void getDateSponsorPhone(final int uid,final DetailDateCallBack applyDateCallBack){
		Map<String, String> map=new HashMap<String, String>();
		map.put("id", String.valueOf(uid));
		LoadDataFromServerNoLooper load=new LoadDataFromServerNoLooper(context, UrlConstant.NEARBY_USER_DETAIL_URL, map);
		load.getData(new DataCallBackNoLooper() {
			
			@Override
			public void onDataCallBack(String result) {
				if(result!=null){
					JSONObject info;
					try {
						info = new JSONObject(result);
						String flag=info.getString("flag");
						if(flag.equals("true")){
							String phone=info.getString("phone");
							applyDateCallBack.onDetailDateCallBack(IntentConstant.HANDLER_GET_INFO+IntentConstant.HANDLER_OK, phone);
						}
						else {
							String err=info.getString("error_infos");
							applyDateCallBack.onDetailDateCallBack(IntentConstant.HANDLER_GET_INFO+IntentConstant.HANDLER_ERROR, err);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		});
	}
	/**
	 * 申请约会、撤销约会申请、删除自己的约会
	 * @param map
	 * @param applyDateCallBack
	 * @param dialogTitle
	 * @param url
	 * @param flag
	 */
	public void dateApplyManagerDelete(final Map<String, String> map,final DetailDateCallBack applyDateCallBack,String dialogTitle,final String url,final int flag){
		final Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg){
				if(applyDateCallBack!=null){
					applyDateCallBack.onDetailDateCallBack(msg.what,(String)msg.obj);
				}
			}
		};
		//先判断男女限制  人数限制
		
		IMUIHelper.showDialog(context, dialogTitle, 
				new DialogCallBack() {
			
			@Override
			public void onDialogCallBack(TextView ok,final AlertDialog dialog) {
				ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
//						if(flag==IntentConstant.HANDLER_DELETE_DATE){
							if(url==null){
								Message msg=handler.obtainMessage();
								msg.what=IntentConstant.HANDLER_DELETE_DATE_CAN_NOT;
								handler.sendMessage(msg);
								dialog.dismiss();
							}
//						}
						else {
							LoadDateFromNet(url,map,handler,dialog, flag);
						}
						/*LoadDateFromNet(UrlConstant.DATE_APPLY_URL,map,handler,dialog, IntentConstant.HANDLER_APPLY);*/
						
					}
				});
				
			}
		});
	}
	/**
	 * 处理网络请求公用接口
	 * @param url
	 * @param map
	 * @param handler
	 * @param dialog
	 * @param handleflag
	 */
	private void LoadDateFromNet(String url,Map<String, String> map, final Handler handler, final AlertDialog dialog,final int handleflag) {
		LoadDataFromServerNoLooper loadtask=new LoadDataFromServerNoLooper(context, url, map);
		loadtask.getData(new DataCallBackNoLooper() {
			
			@Override
			public void onDataCallBack(String result) {
				dialog.dismiss();
				if(result!=null){
					try {
						JSONObject json=new JSONObject(result);
						String flag=json.getString("flag");
						if(flag.equals("false")){//返回错误  通知ui错误
							String error=json.getString("error_infos");
							Message msg=handler.obtainMessage();
							msg.what=handleflag+IntentConstant.HANDLER_ERROR;//false
							msg.obj=error;
							handler.sendMessage(msg);
//							ShowToast(context.getResources().getString(R.string.apply_error_tip)+error);
						}
						else {//返回 true 通知ui正确
//							ShowToast(context.getResources().getString(R.string.apply_ok));
							Message msg=handler.obtainMessage();
							msg.what=handleflag+IntentConstant.HANDLER_OK;
							
							handler.sendMessage(msg);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					Message msg=handler.obtainMessage();
					msg.what=handleflag+IntentConstant.HANDLER_ERROR;
					handler.sendMessage(msg);
//					ShowToast(context.getResources().getString(R.string.apply_error_tip));
				}
			}
		});
	}
	public void getPhotoDate(final int id,final DetailDateCallBack callback) {
		Bitmap bit=imageDownLoader.getBitmapFormByid (id);
		callback.onDetailDateCallBack(1, bit);
		/*Map<String, String> map=new HashMap<String, String>();
//		map.put("id", String.valueOf(47));
		try {
			map.put("fileName",MD5Encoder.encode(phone+"photofilename1"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LoadDataFromServerNoLooper load=new LoadDataFromServerNoLooper(context, UrlConstant.IMAGE_LOAD_URL, map);
		load.getData(new DataCallBackNoLooper() {
			
			@Override
			public void onDataCallBack(String result) {
				if(!TextUtils.isEmpty(result)){
//					callback.onDetailDateCallBack(what, result)
					try {
						JSONObject info = new JSONObject(result);
						String data = info.getString("data");
						byte[] b2 = Base64Coder.decodeLines(data);
						Bitmap b=Bytes2Bimap(b2);
						File file = context.getCacheDir();

						if (!file.exists()) {
							file.mkdirs();
						}
						File file2 = new File(file, phone+"photofilename1");
						if (!file2.exists()) {
							file2.createNewFile();
						}
						FileOutputStream fos = new FileOutputStream(file2);
						fos.write(b2);
						fos.flush();
						fos.close();
						FileInputStream in = new FileInputStream(file2);
						final Bitmap bitmap = BitmapFactory.decodeStream(in);
//						avatar.setImageBitmap(bm)
//						avatar.setImageBitmap(bitmap);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});*/
	}
	public Bitmap Bytes2Bimap(byte[] b) {
		        if (b.length != 0) {
		             return BitmapFactory.decodeByteArray(b, 0, b.length);
		         } else {
		            return null;
		         }
		    }
	
	

	public interface DetailDateCallBack{
//		public void onDetailDateCallBack(int what,String result);
		public void onDetailDateCallBack(int what,Object result);
	}
	public void send(int mUid,String content){
//		String content ="request";
		if (!TextUtils.isEmpty(content)) {
			if (!NetUtil.iSHasNet(context)) {
//				 sendFail();
				ToastUtil.show(context, send_fail + "\n" + please_check_net);
				return;
			}
			ChatInterface chat = ChatServiceConn.getChat();
			if (chat != null) {
				JSONObject json = new JSONObject();
				try {
					if (yAPP == null)
						 yAPP = MyApplication.getInstance();
					json.put(IMConstants.FLAG, IMConstants.FLAG_MESSAGE);
					json.put(UserMode.UID, yAPP.getUid());
					json.put(UserMode.UID2, mUid);
					json.put(ChatMode.CONTENT, content);
					final Long time_tag =System.currentTimeMillis()/1000;
//					setCurrent_time_tag(time_tag);
					if (chat.isSessioinConnect()) {
						long getmNetCurrentTime = ChatIoHandler.getmNetCurrentTime();
						json.put(ChatMode.TIME, getmNetCurrentTime);
						chat.send(json.toString());
						new Thread(new Runnable() {
							public void run() {
								try {
									Thread.sleep(10000);
//									Long current_time_tag2 = getCurrent_time_tag();
									/*if(current_time_tag2==time_tag){
										runOnUiThread(new Runnable() {
											public void run() {
												ToastUtil.show(ChatActivity.this, send_fail);
												sendFail();
											}
										});
									}*/
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}).start();
//						sending();
					} else {
						ToastUtil.show(context, send_fail);
//						sendFail();
						chat.connect();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					ToastUtil.show(context, send_fail);
//					sendFail();
				}
			} else {
				ToastUtil.show(context, send_fail);
//				sendFail();
			}
		} else {
			// 不能为空
		}
	}

}
