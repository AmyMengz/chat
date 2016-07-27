package com.huodong.im.chatdemo.controller;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.huodong.im.chatdemo.controller.GetDZDPRegion.DataCallBack;
import com.huodong.im.config.IntentConstant;
import com.huodong.im.config.UrlConstant;
import com.huodong.im.entity.UserEntity;
import com.huodong.im.utils.LoadDataFromServerNoLooper;
import com.huodong.im.utils.LoadDataFromServerNoLooper.DataCallBackNoLooper;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

public class SeleteAddressController {
	private static SeleteAddressController instance;
	private int uid;
//	private Map<String, String> map;
	private Context context;
	public static SeleteAddressController getInstance(Context context){
		if(instance==null){
			instance=new SeleteAddressController(context);
		}
		return instance;
	}
	public SeleteAddressController(Context context){
		this.context=context;
//		map=new HashMap<String, String>();
	}
	public void getRegion(String city,final SeleteAddressCallback callback){
		URL url=createRegionRequestUrl(city);
        
        GetDZDPRegion.getInstance().getDataFormNet(url, new DataCallBack() {
			
			@Override
			public void onDataCallBack(List<String> list) {
				callback.onSeleteAddressCallback(list);				
			}
		});
	}
	Map<String, String> paramMap;
	private URL createRegionRequestUrl(String result) {
		URL url=null;
		String codes;
		
		createParamTable(result);
		DZDPUrlHelper dzdpUrlHelper=new DZDPUrlHelper(paramMap);
		codes=dzdpUrlHelper.sortForParamKey();
//		codes = sortForParamKey();
		try {
			url=dzdpUrlHelper.codecParams(codes,UrlConstant.DZDP_API_REGION_URL);
//			Log.i(TAG+"3-18", "url  "+url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}
	/**
	 * 对需要参数进行设置
	 * @param city 
	 */
	private void createParamTable(String city) {
		paramMap = new HashMap<String, String>();

		paramMap.put("format", "json");
		if(!TextUtils.isEmpty(city)){
			paramMap.put("city", city);
		}
	}
	public interface SeleteAddressCallback{
		public void onSeleteAddressCallback(Object obj);
	}
	/*public interface NearbyUserDateCallback{
		public void onNearbyUserDateCallback(int date,int indate);
	}*/
}
