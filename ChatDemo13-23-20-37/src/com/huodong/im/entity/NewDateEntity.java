package com.huodong.im.entity;

import com.huodong.im.config.FeesType;
import com.huodong.im.config.PartnerType;

public class NewDateEntity {
	private String dateTitle;			//标题
	private String dateTime;
	private String dateAddress;
	private String dateNotes;
	private int datePartnerNum;		//人数
	private PartnerType partnerType=PartnerType.NOGENDER;
	private FeesType fees=FeesType.IPAY;
	private double dateAddLongitude,dateAddLatitude;
	
	public NewDateEntity(){}
	public NewDateEntity(String dateTitle,String dateTime,String dateAddress,String dateNotes,PartnerType partnerType,FeesType fees,int datePartnerNum){
		this.dateTitle=dateTitle;
		this.dateTime=dateTime;
		this.dateAddress=dateAddress;
		this.dateNotes=dateNotes;
		this.datePartnerNum=datePartnerNum;
//		this.nickName=nickName;
	}
	public void setDateTitle(String dateTitle){
		this.dateTitle=dateTitle;
	}
	public String getDateTitle(){
		return this.dateTitle;
	}
	public void setDateAddress(String dateAddress){
		this.dateAddress=dateAddress;
	}
	public String getDateAddress(){
		return this.dateAddress;
	}
	public void setDatePartnerNum(int datePartnerNum){
		this.datePartnerNum=datePartnerNum;
	}
	public int getDatePartnerNum(){
		return this.datePartnerNum;
	}
	public void setDateNotes(String dateNotes){
		this.dateNotes=dateNotes;
	}
	public String getDateNotes(){
		return this.dateNotes;
	}
	public void setDateTime(String dateTime){
		this.dateTime=dateTime;
	}
	public String getDateTime(){
		return this.dateTime;
	}
	public void setPartnerType(PartnerType partnerType){
		this.partnerType=partnerType;
	}
	public PartnerType getPartnerType(){
		return this.partnerType;
	}
	public void setFeesType(FeesType fees){
		this.fees=fees;
	}
	public FeesType getFeesType(){
		return this.fees;
	}
	public void setDateAddLongitude(double dateAddLongitude){
		this.dateAddLongitude=dateAddLongitude;
	}
	public double getDateAddLongitude(){
		return this.dateAddLongitude;
	}
	public void setDateAddLatitude(double dateAddLatitude){
		this.dateAddLatitude=dateAddLatitude;
	}
	public double getDateAddLatitude(){
		return this.dateAddLatitude;
	}
}
