package com.huodong.im.entity;

public class UserEntity {
	String nickName;			//昵称
	String birthday;					//年龄
	String phone;
	int gender;
	String job;
	int hight;
	long recentTime;
	int income;
	String signature;
	double longitude,latitude;
	public void setNickName(String name){
		this.nickName=name;
	}
	public String getNickName(){
		return nickName;
	}
	public void setBirthday(String birthday){
		this.birthday=birthday;
	}
	public String getBirthday(){
		return this.birthday;
	}
	public void setPhone(String phone){
		this.phone=phone;
	}
	public String getPhone(){
		return phone;
	}
	public void setGender(int gender){
		this.gender=gender;
	}
	public int getGender(){
		return this.gender;
	}
	public void setJob(String job){
		this.job=job;
	}
	public String getJob(){
		return job;
	}
	public void setHight(int hight){
		this.hight=hight;
	}
	public int getHight(){
		return this.hight;
	}
	public void setIncome(int income){
		this.income=income;
	}
	public int getIncome(){
		return this.income;
	}
	public void setRecentTime(long recentTime){
		this.recentTime=recentTime;
	}
	public long getRecentTime(){
		return this.recentTime;
	}
	public void setSignature(String signature){
		this.signature=signature;
	}
	public String getSignature(){
		return this.signature;
	}
	public void setLongitude(double longitude){
		this.longitude=longitude;
	}
	public double getLongitude(){
		return this.longitude;
	}
	public void setLatitude(double latitude){
		this.latitude=latitude;
	}
	public double getLatitude(){
		return this.latitude;
	}
	
	
}
