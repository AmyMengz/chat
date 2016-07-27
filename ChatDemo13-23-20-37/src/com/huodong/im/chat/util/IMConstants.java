package com.huodong.im.chat.util;

public class IMConstants {

	public static final String TAG_YJ = "yjchat";
	public static final String INPUT_CONTACT_NAME = "请输入要查找的联系人";
	public static final String TYPE = "type";
	public static final String URL_BASE = "http://120.24.2.49:8787/yj2/servlet";
	public static final String URL_USERS = URL_BASE + "/UserServlet";
	public static final String URL_FIND_FRIENDS = URL_BASE + "/FindFriendsServlet";
	public static final String URL_FRIENDS = URL_BASE + "/FriendsServlet";
	public static final String URL_APPLY_FRIENDS = URL_BASE + "/ApplyFriendsServlet";
	public static final String URL_SET_READ = URL_BASE + "/SetReadServlet";
	public static final String URL_GETCHAT = URL_BASE + "/GetChatServlet";
	public static final String CHAT_HOST ="120.24.2.49";
//	public static final String CHAT_HOST ="192.168.56.1";
	public static final int CHAT_PORT = 5008;
	public static final String NET_STATE = "net_state";
	public static final String FLAG = "flag";
	public static final String TIME_TAG = "time_tag";
	public static final String AUTH = "auth";
	public static final String FLAG_MESSAGE = "ms";
	public static final String PROPERTY_DATA = "property_data";
	public static final String FLAG_NET_CHANGE = "net_change";
	public static final String FLAG_GET_NET_CHAT = "get_net_chat";
	public static final String FLAG_MS_BACK = "flag_ms_back";
	public static final String MS_KEY = "ms_key";
	public static final String FLAG_HAS_GET_NET_CHAT = "has_get_net_chat";
	public static final String MESSAGE_SEND_OK = "message_send_ok";
	public static final String NEW_FDS = "new_fds";
	public static final String PREF_CHAT = "pref_chat";
	
	
	public static class CodeTag{
		public static final int CHAT_CHANGE = 21;
	}

	public static class USERSERVLET {
		public static final String TYPE_USER_INFOS = "6";
	}
	
//	+ 1 聊天
//	+ 2 对方向你申请好友
//	+ 3 对方同意你的好友申请
//	+ 4 对方拒绝你的好友申请
	
	public static class ChaType {
		public static final int CHAT_TO = 6;
		public static final int APPLY_FDS = 7;
		public static final int CHAT_FROM = 1;
		public static final int BE_APPLY_FDS = 2;
		public static final int AGREE = 3;
		public static final int UN_AGREE = 4;
	}
	
	
	public static final String ACTION_MESSAGE = "yj.chat.message";
	public static final String ACTION_CONTACT = "yj.chat.contact";
	
	public static final String SK_ACTION_BE_FDS = "[sk:be_fds]";
	public static final String SK_ACTION_APPLY_FDS = "[sk:apply_fds]";
	
	public static final int COUNT_15 = 15;
	public static final int COUNT_20 = 20;
	public static final int COUNT_30 = 30;
	public static final int COUNT_50 = 50;
	public static final int TEXT_SIZE_38 = 38;
	public static final int TEXT_SIZE_30 = 30;
	public static final int FIVE_MIN = 5*60;
	


}
