package com.huodong.im.chat.mode;

import java.util.ArrayList;

public class FMode {

	public ArrayList<User> data_F;
	public ArrayList<User> data_AF;
	public ArrayList<User> data_BF;
	
	

	public ArrayList<User> getData_F() {
		return data_F;
	}

	public ArrayList<User> getData_AF() {
		return data_AF;
	}

	public ArrayList<User> getData_BF() {
		return data_BF;
	}

	public boolean flag = false;

	public boolean getFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}



}
