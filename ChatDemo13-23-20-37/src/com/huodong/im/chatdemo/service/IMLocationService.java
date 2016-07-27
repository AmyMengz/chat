package com.huodong.im.chatdemo.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.b.e;
import com.huodong.im.chatdemo.activity.MyApplication;
import com.huodong.im.config.LocationSp;
import com.huodong.im.config.SharePreferenceHelper;
import com.huodong.im.config.UrlConstant;
import com.huodong.im.config.LocationSp.SpLocationIdentity;
import com.huodong.im.utils.LoadDataFromServer;
import com.huodong.im.utils.LoadDataFromServer.DataCallBack;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

public class IMLocationService extends Service {
	private LocationService locationService;
	double log0,lat0;
	double log,lat;
	String city;
	private Context context;
	private  int loginId;
	Map<String, String> map=new HashMap<String, String>();
	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		context=this;
		MyApplication app = (MyApplication)getApplication();
		loginId=app.getUid();
		locationService=((MyApplication)getApplication()).locationService;
		locationService.registerListener(mListener);
		locationService.start();// 定位SDK
	}
	Timer timer=new Timer();
	TimerTask myTask;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
//		locationService=((MyApplication)getApplication()).locationService;
//		locationService.registerListener(mListener);
//		locationService.start();// 定位SDK
		System.out.println("log00  "+log0+" "+lat0);
		log0=SharePreferenceHelper.getInstance().getLocationLog();
		lat0=SharePreferenceHelper.getInstance().getLocationLat();
		//每一分钟确定一次自己的位置，如果和上一次位置对比>0.2公里则更新自己在服务器的位置
		/*Timer timer=new Timer();
		TimerTask*/ myTask=new TimerTask() {
			
			@Override
			public void run() {
				double distance=Distance(log0,lat0,log,lat);
//				 double distance2=GetDistance(log0,lat0,log,lat);
//				System.out.println("distance  "+distance+"  "+distance2);
				if(distance>0.2){
					log0=log;
					lat0=lat;
//					final int loginId=31;
					map.put("uid",String.valueOf(loginId));
					map.put("longitude", log+"");
					map.put("latitude", lat+"");
					System.out.println("post log lat "+log+" "+lat);
					//自己的位置上传到服务器
					LoadDataFromServer locationTask=new LoadDataFromServer(context, UrlConstant.UPDATE_POS_URL, map);
					locationTask.getData(new DataCallBack() {
						
						@Override
						public void onDataCallBack(String result) {
							if(result!=null){
								try {
									JSONObject jsonObject=new JSONObject(result);
									String flag=jsonObject.getString("flag");
									System.out.println("sssssflag  "+flag);
									if(flag.equals("false")){
										
									}
									else {
										LocationSp.getInstance().setLocationInfo(loginId, String.valueOf(log), String.valueOf(lat),city);
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
						}
					});
					locationService.start();
				}
				
			}
		};
//		timer.schedule(myTask, 0, 60000);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		locationService.unRegistenerListener(mListener); //注销掉监听
		locationService.stop(); //停止定位服务
	}
	private BDLocationListener mListener=new BDLocationListener() {
		
		@Override
		public void onReceiveLocation(BDLocation location) {
			String loc="";
			lat=location.getLatitude();
			log=location.getLongitude();
			city=location.getCity();
			loc=String.valueOf(lat)+"     "+log;
			timer.schedule(myTask, 0, 60000);
//			LocationResult.setText(loc);
		}
	};
	public static double Distance(double long1, double lat1, double long2,  
	        double lat2) {  
	    double a, b, R;  
	    R = 6378137; // 地球半径  
	    lat1 = lat1 * Math.PI / 180.0;  
	    lat2 = lat2 * Math.PI / 180.0;  
	    a = lat1 - lat2;  
	    b = (long1 - long2) * Math.PI / 180.0;  
	    double d;  
	    double sa2, sb2;  
	    sa2 = Math.sin(a / 2.0);  
	    sb2 = Math.sin(b / 2.0);  
	    d = 2  
	            * R  
	            * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)  
	                    * Math.cos(lat2) * sb2 * sb2));  
	    return d/1000;  
	}  
	 private static final double EARTH_RADIUS = 6378137;
	 private static double rad(double d)
	 {
	 return d * Math.PI / 180.0;
	 }
	public static double GetDistance(double lng1, double lat1, double lng2, double lat2)
  {
	double radLat1 = rad(lat1);
	double radLat2 = rad(lat2);
	double a = radLat1 - radLat2;
	double b = rad(lng1) - rad(lng2);
	double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + 
	Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
	s = s * EARTH_RADIUS;
	s = Math.round(s * 10000) / 10000;
	return s;
	}
	

}
