package com.huodong.im.chatdemo.activity;

import com.baidu.mapapi.SDKInitializer;
import com.huodong.im.chatdemo.service.LocationService;
import com.huodong.im.config.LocationSp;
import com.huodong.im.config.LoginSp;
import com.huodong.im.utils.IMDateUserHelper;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
	public LocationService locationService;

	private boolean refresh=false;

	public static MyApplication mInstance;

	@Override
	public void onCreate(){
		super.onCreate();
		mInstance = this;
		/***
         * 初始化定位sdk，建议在Application中创建
         */
		locationService=new LocationService(getApplicationContext());
		SDKInitializer.initialize(getApplicationContext());
		//初始化
		LoginSp.getInstance().init(getApplicationContext());
		LocationSp.getInstance().init(getApplicationContext());

		IMDateUserHelper helper=new IMDateUserHelper(getApplicationContext());


	}
	public boolean getRefresh() {
        return refresh;
	}
	public void setRefresh(boolean refresh) {
		Log.i("Amy isRefush", "refresh   "+refresh);
        this.refresh = refresh;
	}
	public Long phone;
	
	public int uid = -1;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}


	public Long getPhone() {
		return phone;
	}


	public void setPhone(Long phone) {
		this.phone = phone;
	}


	public static MyApplication getInstance() {
		return mInstance;
	}

}
