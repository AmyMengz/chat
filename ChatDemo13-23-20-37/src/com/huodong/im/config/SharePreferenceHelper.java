package com.huodong.im.config;

import android.text.TextUtils;

import com.huodong.im.config.LocationSp.SpLocationIdentity;
import com.huodong.im.config.LoginSp.SpLoginIdentity;

public class SharePreferenceHelper {
	
	private static SharePreferenceHelper instacne;
	public static  SharePreferenceHelper getInstance(){
		if(instacne==null){
			instacne=new SharePreferenceHelper();
		}
		return instacne;
	}
	public boolean isLogined(){
	
		SpLoginIdentity loginIdentity=LoginSp.getInstance().getLoginIdentity();
		if(loginIdentity!=null){
			int loginId=loginIdentity.getLoginId();
			String password =loginIdentity.getPwd();
			if(TextUtils.isEmpty(String.valueOf(loginId))||TextUtils.isEmpty(password)){
				return false;
			}
			else {
				return true;
			}
		}
		else {
			return false;
		}
		
	}
	public double getLocationLog(){
		
		SpLocationIdentity lcationIdentity=LocationSp.getInstance().getLocationIdentity();
		if (lcationIdentity.getLongitude()!=null) {
			return Double.valueOf(lcationIdentity.getLongitude());
		}
		else {
			return 0.0;
		}
		
	}
	public double getLocationLat(){
		
		SpLocationIdentity lcationIdentity=LocationSp.getInstance().getLocationIdentity();
		if (lcationIdentity.getLatitude()!=null) {
			return Double.valueOf(lcationIdentity.getLatitude());
		}
		return 0.0;
	}
	public String getCity(){
		
		SpLocationIdentity lcationIdentity=LocationSp.getInstance().getLocationIdentity();
		if (lcationIdentity.getCity()!=null) {
			return lcationIdentity.getCity();
		}
		return null;
	}
	

}
