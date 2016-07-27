package com.huodong.im.entity;

import com.huodong.im.config.FeesType;
import com.huodong.im.config.PartnerType;

public class NearbyUserEntity implements Comparable<NearbyUserEntity>{
	String avatar="";
	int  uid;
	int gender;
	String birthday;
//	String age="AGE";
	String nickName="NICK";
	int dateTimes=0;
	int fans=0;
	String distance;
	long recentTime;		
	String signature="";
	String phone;
	int isPassed;
	public void setPhone(String phone){
		this.phone=phone;
	}
	public String getPhone(){
		return this.phone;
	}
	public void setIsPassed(int ispass){
		this.isPassed=ispass;
	}
	public int getPassed(){
		return this.isPassed;
	}
	public NearbyUserEntity(){}
//	public NearbyUserEntity(String avatar,String recentTime,String age,String nickName){
//		
//		this.avatar=avatar;
//		this.recentTime=recentTime;
//		this.age=age;
//		this.nickName=nickName;
//	}
	public void setAvatar(String avatar){
		this.avatar=avatar;
	}
	public String getAvatar(){
		return this.avatar;
	}
	public int getGender(){
		return this.gender;
	}
	public void setGender(int gender){
		this.gender=gender;
	}
	public int getUid(){
		return this.uid;
	}
	public void setUid(int uid){
		this.uid=uid;
	}
	public String getDistance(){
		return this.distance;
	}
	public void setDistance(String distance){
		this.distance=distance;
	}
	public void setRecentTime(long recentTime){
		this.recentTime=recentTime;
	}
	public long getRecentTime(){
		return this.recentTime;
	}
	public void setBirthday(String birthday){
		this.birthday=birthday;
	}
	public String getBirthday(){
		return this.birthday;
	}
	public void setNickName(String name){
		this.nickName=name;
	}
	public String getNickName(){
		return this.nickName;
	}
	public void setDateTimes(int dateTimes){
		this.dateTimes=dateTimes;
	}
	public int getDateTimes(){
		return this.dateTimes;
	}
	public void setFans(int fans){
		this.fans=fans;
	}
	public int getFans(){
		return this.fans;
	}
	public void setSignature(String signature){
		this.signature=signature;
	}
	public String getSignature(){
		return this.signature;
	}
	@Override
	public int compareTo(NearbyUserEntity another) {
		// TODO Auto-generated method stub
		return this.getDistance().compareTo(another.getDistance());
	}
	
	

}
