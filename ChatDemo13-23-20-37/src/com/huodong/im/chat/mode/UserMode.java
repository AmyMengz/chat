package com.huodong.im.chat.mode;

import java.util.ArrayList;

public class UserMode {
	public static final String NAME = "name";
	public static final String NAME2 = "name2";
	public static final String UID = "uid";
	public static final String UID2 = "uid2";
	public static final String PHONE = "phone";
	public static final String PHONE2 = "phone2";
	public static final String ID = "id";
	public static final String CODE = "code";
	public static final String FIND = "find";
	public static final String LIMIT = "limit";
	public static final int TYPE_CHECK_USER = 1;
	public static final int TYPE_ADD_USER = 2;
	public static final int TYPE_UPDATE_CODE = 3;
	public static final int TYPE_UPDATE_PHONE = 4;
	public static final int TYPE_CHECK_CODE = 5;
	public static final int TYPE_USER_INFO = 6;

	public ArrayList<User> getData() {
		return data;
	}

	public void setData(ArrayList<User> data) {
		this.data = data;
	}

	public ArrayList<User> data;

	public boolean flag = false;

	public boolean getFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}


}
