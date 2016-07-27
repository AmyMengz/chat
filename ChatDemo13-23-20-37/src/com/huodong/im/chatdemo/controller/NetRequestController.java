package com.huodong.im.chatdemo.controller;

import org.json.JSONException;
import org.json.JSONObject;

import com.huodong.im.chat.ChatActivity;
import com.huodong.im.chat.mode.ChatMode;
import com.huodong.im.chat.mode.UserMode;
import com.huodong.im.chat.service.ChatInterface;
import com.huodong.im.chat.service.ChatIoHandler;
import com.huodong.im.chat.service.ChatServiceConn;
import com.huodong.im.chat.util.IMConstants;
import com.huodong.im.chat.util.NetUtil;
import com.huodong.im.chat.util.ToastUtil;
import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.activity.MyApplication;

import android.content.Context;
import android.text.TextUtils;

public class NetRequestController {
	
	private Context context;
	String send_fail,please_check_net;
	private MyApplication yAPP;
	int mUid;
	private NetRequestController(Context contex){
		this.context=contex;
		send_fail = context.getResources().getString(R.string.send_fail);
		please_check_net = context.getResources().getString(R.string.please_check_net);
		yAPP = MyApplication.getInstance();
		mUid=yAPP.getUid();
	}
	private NetRequestController instance=null;
	public NetRequestController getInstance(Context context){
		if(instance==null){
			instance=new NetRequestController(context);
		}
		return instance;
	}
	public void send(){
		String content ="request";
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
