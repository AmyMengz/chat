package com.huodong.im.chat.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;

public class SendBroadUtil {
	
	public static void sendNetChange(Context context){
		Intent intent2 = new Intent();
		intent2.setAction(IMConstants.ACTION_MESSAGE);
		JSONObject json = new JSONObject();
		try {
			json.put(IMConstants.FLAG, IMConstants.FLAG_NET_CHANGE);
			intent2.putExtra(IMConstants.PROPERTY_DATA, json.toString());
			context.sendBroadcast(intent2);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public static void sendNewMs(Context context,String str){
		Intent intent = new Intent();
		intent.setAction(IMConstants.ACTION_MESSAGE);
		intent.putExtra(IMConstants.PROPERTY_DATA, str);
		context.sendBroadcast(intent);
	}
	public static void sendGetNetChat(Context context){
		Intent intent = new Intent();
		intent.setAction(IMConstants.ACTION_MESSAGE);
		JSONObject json = new JSONObject();
		try {
			json.put(IMConstants.FLAG, IMConstants.FLAG_GET_NET_CHAT);
			intent.putExtra(IMConstants.PROPERTY_DATA, json.toString());
			context.sendBroadcast(intent);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public static void sendHasGetNetChat(Context context){
		Intent intent = new Intent();
		intent.setAction(IMConstants.ACTION_MESSAGE);
		JSONObject json = new JSONObject();
		try {
			json.put(IMConstants.FLAG, IMConstants.FLAG_HAS_GET_NET_CHAT);
			intent.putExtra(IMConstants.PROPERTY_DATA, json.toString());
			context.sendBroadcast(intent);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public static void sendGetContact(Context context){
		Intent intent2 = new Intent();
		intent2.setAction(IMConstants.ACTION_CONTACT);
		context.sendBroadcast(intent2);
	}

}
