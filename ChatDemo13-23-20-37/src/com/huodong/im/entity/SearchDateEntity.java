package com.huodong.im.entity;

import java.io.Serializable;

import com.huodong.im.config.FeesType;
import com.huodong.im.config.PartnerType;

public class SearchDateEntity implements Serializable{
	int sponsorID;//发起人ID				~
	String birthday;					//~
	int dateKey;						//~
	String avatar="";				//头像URL
	String dateTitle="title";			//标题 ~
	
	String nickName="NICK";			//~
	String dateAddress;			//地址
	String detailAddress;			//地址
	String notes;
	double distance;			//距离
	int partnerNum=1;		//人数
	int fees;
	
	int apply;			//~
	int gender;			//~
	long dateTime;
	int commentCount;	//~
	int partnerType;
	int applyState;
	double longitude,latitude;
	public SearchDateEntity(){};
	public void setSponsorID(int uid){
		this.sponsorID=uid;
	}
	public int getSponsorID(){
		return this.sponsorID;
	}
	public void setDateKey(int dateKey){
		this.dateKey=dateKey;
	}
	public int getDateKey(){
		return this.dateKey;
	}
	public void setBirthday(String bir){
		this.birthday=bir;
	}
	public String getBirthday(){
		return this.birthday;
	}
	public void setAvatar(String url){
		this.avatar=url;
	}
	public String getAvatar(){
		return this.avatar;
	}
	public void setDateTile(String title){
		this.dateTitle=title;
	}
	public String getDateTitle(){
		return this.dateTitle;
	}
	
	public void setNickName(String name){
		this.nickName=name;
	}
	public String getNickName(){
		return this.nickName;
	}
	public void setNotes(String notes){
		this.notes=notes;
	}
	public String getNotes(){
		return this.notes;
	}
	public void setAddress(String address){
		this.dateAddress=address;
	}
	public String getDateAddress(){
		return this.dateAddress;
	}
	public void setDetailAddress(String detailAddress){
		this.detailAddress=detailAddress;
	}
	public String getDetailAddress(){
		return this.detailAddress;
	}
	public void setDistance(double distance){
		this.distance=distance;
	}
	public double getDistance(){
		return this.distance;
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
	public void setPartnerNum(int num){
		this.partnerNum=num;
	}
	public int getPartnerNum(){
		return this.partnerNum;
	}
	public void setDateTime(long time){
		this.dateTime=time;
	}
	public long getDateTime(){
		return this.dateTime;
	}
	public void setFeesType(int fees){
		this.fees=fees;
	}
	public int getFeesType(){
		return fees;
	}
	public void setApply(int apply){
		this.apply=apply;
	}
	public int getApply(){
		return this.apply;
	}
	public void setGender(int sex){
		this.gender=sex;
	}
	public int getGender(){
		return gender;
	}
	public void setComment(int comment){
		this.commentCount=comment;
	}
	public int getComment(){
		return this.commentCount;
	}
	public void setPartnerType(int partner){
		this.partnerType=partner;
	}
	public int getPartnerType(){
		return this.partnerType;
	}
	public void setApplyState(int apply_state){
		this.applyState=apply_state;
	}
	public int getApplyState(){
		return applyState;
	}
	
}
