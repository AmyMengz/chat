package com.huodong.im.chat.mode;

public 	class User {
	public String name;
	public Long phone;
	public int id;
	public int uid;
	public String fenLei,alpha;

	public int getId() {
		return id;
	}


	public String getAlpha() {
		return alpha;
	}


	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}


	public String getFenLei() {
		return fenLei;
	}


	public void setFenLei(String fenLei) {
		this.fenLei = fenLei;
	}


	public void setId(int id) {
		this.id = id;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}
}
