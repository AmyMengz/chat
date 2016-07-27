package com.huodong.im.chat.service;


import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;

public class ChatServiceConn implements ServiceConnection {

	public static ChatInterface chat;

	private Context context;

	public ChatServiceConn(Context context) {
		super();
		this.context = context;
	}

	public static ChatInterface getChat() {
		return chat;
	}


	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		chat = (ChatInterface) service;
		chat.connect();
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
	}


}