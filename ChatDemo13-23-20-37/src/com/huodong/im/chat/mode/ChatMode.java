package com.huodong.im.chat.mode;

import java.io.Serializable;

public class ChatMode implements Serializable{

	public Long time;
	public Long phone;
	public int type,id,uid,chatid,isRead;
	public String name;
	public int getId() {
		return id;
	}
	public int getUid() {
		return uid;
	}
	public int getChatid() {
		return chatid;
	}
	public int getIsRead() {
		return isRead;
	}
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
	public void setChatid(int chatid) {
		this.chatid = chatid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String content;
	public static final String UID = "uid";
	public static final String UID2 = "uid2";
	public static final String TIME = "time";
	public  static final String ID = "id";
	public  static final String IS_READ = "isRead";
	public  static final String PHONE = "phone";
	public static final  String TYPE = "type";
	public  static final String NAME = "name";
	public  static final String CHATID = "chatid";
	public  static final String CONTENT = "content";
	public  static final String TIME_TAG = "time_tag";
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public Long getPhone() {
		return phone;
	}
	public void setPhone(Long phone) {
		this.phone = phone;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	

}
