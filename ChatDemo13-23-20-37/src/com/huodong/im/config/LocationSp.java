package com.huodong.im.config;

import com.huodong.im.config.LoginSp.SpLoginIdentity;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class LocationSp {
	private final String fileName = "location.xml";
    private Context ctx;
    private final String LOGIN_ID = "login_id";
    private final String LONGITUDE = "longitude";
    private final String LATITUDE = "latitude";
    private final String CITY = "city";
    SharedPreferences sharedPreferences;
    private static LocationSp locationSp=null;
    public static LocationSp getInstance(){
    	if(locationSp==null){
    		 synchronized (LoginSp.class){
    			 locationSp=new LocationSp();
    		 }
    	}
    	return locationSp;
    }
    public void  init(Context ctx){
        this.ctx = ctx;
        sharedPreferences= ctx.getSharedPreferences
                (fileName,ctx.MODE_PRIVATE);
    }
    public void setLocationInfo(int logindId,String longitude,String latitude,String city){
    	SharedPreferences.Editor editor=sharedPreferences.edit();
    	editor.putInt(LOGIN_ID, logindId);
    	editor.putString(LONGITUDE, longitude);
    	editor.putString(LATITUDE, latitude);
    	editor.putString(CITY, city);
    	editor.commit();
    }
    public SpLocationIdentity getLocationIdentity(){
    	String longitude =  sharedPreferences.getString(LONGITUDE,null);
        String latitude = sharedPreferences.getString(LATITUDE,null);
        String city=sharedPreferences.getString(CITY, null);
        int loginId = sharedPreferences.getInt(LOGIN_ID,0);
        /**pwd不判空: loginOut的时候会将pwd清空*/
//        if(TextUtils.isEmpty(userName) || loginId == 0){
//            return null;
//        }
        return new SpLocationIdentity(longitude,latitude,loginId,city);
    }
    public class SpLocationIdentity{
    	private String longitude;
        private String latitude;
        private String city;
        private int loginId;
        public SpLocationIdentity(String longitude,String latitude,int loginId,String city){
        	this.longitude=longitude;
        	this.latitude=latitude;
        	this.loginId=loginId;
        	this.city=city;
        }
        public int getLoginId() {
            return loginId;
        }

        public void setLoginId(int loginId) {
            this.loginId = loginId;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getLatitude() {
            return latitude;
        }
        public String getCity() {
            return city;
        }
    }

}
