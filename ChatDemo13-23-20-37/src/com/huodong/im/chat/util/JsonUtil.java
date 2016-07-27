package com.huodong.im.chat.util;

import org.json.JSONException;
import org.json.JSONObject;

import com.huodong.im.chat.mode.ChatMode;
import com.huodong.im.chat.mode.UserMode;

public class JsonUtil {
	
	public static String PackgNetStateChange( ){
		JSONObject json = new JSONObject();
		try {
			json.put(IMConstants.FLAG, IMConstants.FLAG_NET_CHANGE);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}
	public static String PackgNewMs(int uid,int uid2,String content){
		JSONObject json = new JSONObject();
		try {
			json.put(IMConstants.FLAG, IMConstants.FLAG_MESSAGE);
			json.put(UserMode.UID, uid);
			json.put(UserMode.UID2, uid2);
			json.put(ChatMode.CONTENT, content);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}

}
