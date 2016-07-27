package com.huodong.im.chat.service;

import org.json.JSONException;
import org.json.JSONObject;

import com.huodong.im.chat.util.IMConstants;
import com.huodong.im.chat.util.JsonUtil;
import com.huodong.im.chat.util.NetUtil;
import com.huodong.im.chat.util.SendBroadUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetStateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		boolean hasNet = NetUtil.iSHasNet(context);
		if (!isHasNet() && hasNet) {
			ChatInterface chat = ChatServiceConn.getChat();
			if (chat != null) {
				chat.connect();
			}
			setHasNet(hasNet);
		}
		SendBroadUtil.sendNetChange(context);
	}
	
	private boolean isHasNet = false;

	private boolean isHasNet() {
		return isHasNet;
	}

	private void setHasNet(boolean isHasNet) {
		this.isHasNet = isHasNet;
	}
	
	

}
