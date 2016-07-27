package com.huodong.im.chat.service;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.json.JSONObject;

import com.huodong.im.chat.db.YCOpenHelperTest;
import com.huodong.im.chat.mode.ChatMode;
import com.huodong.im.chat.mode.UserMode;
import com.huodong.im.chat.service.ChatService.Chat;
import com.huodong.im.chat.util.IMConstants;
import com.huodong.im.chat.util.JsonUtil;
import com.huodong.im.chat.util.SendBroadUtil;

import android.content.Context;
import android.util.Log;

public class ChatIoHandler implements IoHandler {

	private Context context;
	private YCOpenHelperTest yDB;
	private static long mNetCurrentTime = 0;

	public ChatIoHandler(Context context) {
		super();
		this.context = context;
	}

	@Override
	public void exceptionCaught(IoSession arg0, Throwable arg1) throws Exception {
		Log.v(IMConstants.TAG_YJ, "error--IoSession:" + arg0.toString());
		Log.v(IMConstants.TAG_YJ, "error--Throwable:" + arg1.toString());
	}

	public static boolean isRun = false;
	@Override
	public void messageReceived(IoSession arg0, Object arg1) throws Exception {
		String str = arg1.toString().trim();
		Log.v(IMConstants.TAG_YJ, "来自服务端的：" + str);
		
		JSONObject json = new JSONObject(str);
		if (yDB == null)
			yDB = new YCOpenHelperTest(context);
		String flag = json.getString(IMConstants.FLAG);
		if (IMConstants.AUTH.equals(flag)) {
			mNetCurrentTime = json.getLong(ChatMode.TIME);
			SendBroadUtil.sendGetNetChat(context);
			if (!isRun) {
				TimeRun timeRun = new TimeRun();
				new Thread(timeRun).start();
				isRun = true;
			}
		} else if (IMConstants.FLAG_MESSAGE.equals(flag)) {
			String content = json.getString(ChatMode.CONTENT);
			if (IMConstants.SK_ACTION_APPLY_FDS.equals(content)) {
				SendBroadUtil.sendGetNetChat(context);
				return;
			}
			if (IMConstants.SK_ACTION_BE_FDS.equals(content)) {
				SendBroadUtil.sendGetNetChat(context);
				SendBroadUtil.sendGetContact(context);
				return;
			}
				int uid = json.getInt(ChatMode.UID);
				long time = json.getLong(ChatMode.TIME);
				yDB.insertChat(time, content, IMConstants.ChaType.CHAT_FROM, uid);
				SendBroadUtil.sendNewMs(context, str);
				
				int uid2 = json.getInt(UserMode.UID2);
				String msKey = getMsKey(uid, uid2, time);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(IMConstants.FLAG, IMConstants.FLAG_MS_BACK);
				jsonObject.put(IMConstants.MS_KEY, msKey);
				ChatInterface chat = ChatServiceConn.getChat();
				if(chat!=null){
					chat.send(jsonObject.toString());
				}
		}

	}
	
	private String getMsKey(int uid1, int uid2, Long time_tag) {
		return uid1 + "/" + uid2 + "/" + time_tag;
	}

	@Override
	public void messageSent(IoSession arg0, Object arg1) throws Exception {
		String str = arg1.toString().trim();
		Log.v(IMConstants.TAG_YJ, "sendddd：" + str);
//		if (IMConstants.SK_ACTION_APPLY_FDS.equals(str) || IMConstants.SK_ACTION_BE_FDS.equals(str)) 
//			return;
		JSONObject json = new JSONObject(str);
		String flag = json.getString(IMConstants.FLAG);
		if (IMConstants.FLAG_MESSAGE.equals(flag)) {
			String content = json.getString(ChatMode.CONTENT);
			if (IMConstants.SK_ACTION_APPLY_FDS.equals(content)||IMConstants.SK_ACTION_BE_FDS.equals(content)) {
				SendBroadUtil.sendNewMs(context, str);
				return;
			}
			int uid2 = json.getInt(UserMode.UID2);
			long time = json.getLong(ChatMode.TIME);
			if (yDB == null)
				yDB = new YCOpenHelperTest(context);
			yDB.insertChat(time, content, IMConstants.ChaType.CHAT_TO, uid2);
			SendBroadUtil.sendNewMs(context, json.toString().trim());
		}
	}

	class TimeRun implements Runnable {
		public void run() {
			try {
				while (true) {
					Thread.sleep(1000);
					mNetCurrentTime += 1;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void sessionClosed(IoSession arg0) throws Exception {
	}

	@Override
	public void sessionCreated(IoSession arg0) throws Exception {
	}

	@Override
	public void sessionIdle(IoSession arg0, IdleStatus status) throws Exception {
	}

	@Override
	public void sessionOpened(IoSession arg0) throws Exception {
	}

	public static long getmNetCurrentTime() {
		return mNetCurrentTime;
	}


}
