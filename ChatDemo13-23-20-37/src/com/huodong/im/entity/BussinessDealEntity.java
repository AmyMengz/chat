package com.huodong.im.entity;

import java.io.Serializable;

public class BussinessDealEntity implements Serializable{
	String description;
	String url;
	public BussinessDealEntity(String description,String url){
		this.description=description;
		this.url=url;
	}
	public void setDescription(String description){
		this.description=description;
	}
	public String getDescription(){
		return this.description;
	}
	public void setUrl(String url){
		this.url=url;
	}
	public String getUrl(){
		return this.url;
	}

}
