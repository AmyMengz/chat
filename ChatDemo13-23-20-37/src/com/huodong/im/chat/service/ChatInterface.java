package com.huodong.im.chat.service;

public interface ChatInterface {
	void send(String str);

	void connect();

	void closeSession();

	void newMessage(int uid,String content);

	boolean isSessioinConnect();

	void reSetCount();
}
