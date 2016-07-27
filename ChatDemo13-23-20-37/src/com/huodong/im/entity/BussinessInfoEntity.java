package com.huodong.im.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.widget.ImageView;
import android.widget.TextView;

public class BussinessInfoEntity implements Serializable ,Comparable<BussinessInfoEntity>{
	String name;
	String address;
	String photo_url;
	String regions="[\"思明区\",\"体育路\"]";
	double latitude;
	double longtitude;
	String rating_img_url;
	String rating_s_img_url="png";
	String avg_price;
	String distance;
	int deals;
	String teltphone;
	String city;
	String categories;	
	String avg_rating="5.0";
	String product_grade;
	String decoration_grade;
	String service_grade;
	String product_score="9.4";
	String decoration_score;
	String setvice_score;
	String review_count="1227";
	String review_list_url;
	List<BussinessDealEntity> dealList;
	public BussinessInfoEntity(){
		dealList=new ArrayList<BussinessDealEntity>();
	}
	public List<BussinessDealEntity> getDealList(){
		return this.dealList;
	}
	public void setTeltphone(String teltphone){
		this.teltphone=teltphone;
	}
	public String getTeltphone(){
		return this.teltphone;
	}
	public void setName(String name){
		this.name=name;
	}
	public String getName(){
		return this.name;
	}
	public void setAddress(String address){
		this.address=address;
	}
	public String getAddress(){
		return this.address;
	}
	public void setPhotoUrl(String photo_url){
		this.photo_url=photo_url;
	}
	public String getPhotoUrl(){
		return this.photo_url;
	}
	public void setRegions(String regions){
		this.regions=regions;
	}
	public String getRegions(){
		return this.regions;
	}
	public void setLatitude(double latitude){
		this.latitude=latitude;
	}
	public double getLatitude(){
		return this.latitude;
	}
	public void setLongtitude(double longtitude){
		this.longtitude=longtitude;
	}
	public double getLongtitude(){
		return this.longtitude;
		
	}
	public void setRatingImgUrl(String rating_img_url){
		this.rating_img_url=rating_img_url;
	}
	public String getRatingImgUrl(){
		return this.rating_img_url;
		
	}
	public void setAvgPrice(String avg_price){
		this.avg_price=avg_price;
	}
	public String getAvgPrice(){
		return this.avg_price;	
	}
	public void setDistance(String distance){
		this.distance=distance;
	}
	public String getDistance(){
		return this.distance;
	}
	public void setDeals(int deals){
		this.deals=deals;
	}
	public int getDeals(){
		return this.deals;	
	}
	@Override
	public int compareTo(BussinessInfoEntity another) {
		// TODO Auto-generated method stub
		return this.distance.compareTo(another.getDistance());
	}

	/**/

}
