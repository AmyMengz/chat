package com.huodong.im.chatdemo.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apaches.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huodong.im.config.UrlConstant;
import com.huodong.im.entity.BussinessInfoEntity;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GetDZDPRegion {
	private static GetDZDPRegion instance=null;
	String TAG="Amy";
	private static final int CODE = 1;
	private List<String> parseJSONResult(String string) {
		List<String> list=new ArrayList<String>();
		try {
			
			JSONObject json=new JSONObject(string);
			String status=json.getString("status");
			if(status.equals("OK")){
				JSONArray cities=json.getJSONArray("cities");
				JSONObject city=cities.getJSONObject(0);
				String city_name=city.getString("city_name");//城市名
				JSONArray districts=city.getJSONArray("districts");//区域
				int count=districts.length();//区域个数
				Log.i("Amy", "count  "+count);
				for (int i = 0; i < count; i++) {
					JSONObject regionobj=districts.getJSONObject(i);
					String region=regionobj.getString("district_name");
					list.add(region);
					Log.i("Amy", "region  "+region);
				}
				Log.i("Amy", "list  "+list.size());
//				adapter.notifyDataSetChanged();
				
			}
			return list;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return list;
	}
	
	public static GetDZDPRegion getInstance(){
		if(instance==null){
			instance=new GetDZDPRegion();
		}
		return instance;
	}
	Handler handler;
	/**
	 * 从大众点评获取相应数据
	 * 
	 * @param codes
	 */
	public void getDataFormNet(final URL url,final DataCallBack callBack) {
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case CODE:
//					receiveContent.setText(msg.obj.toString());
//					parseJSONResult(msg.obj.toString());
					callBack.onDataCallBack(parseJSONResult(msg.obj.toString()));
					break;

				default:
					break;
				}
			}	
		};
		
		new Thread() {
			@Override
			public void run() {
				HttpURLConnection urlConnection = null;
				InputStream inputStream = null;
				try {
//					URL url = codecParams(codes);
					System.out.println("the access network url is "
							+ url.toString());
					urlConnection = (HttpURLConnection) url.openConnection();
					urlConnection.setReadTimeout(10000 /* milliseconds */);
					urlConnection.setConnectTimeout(15000 /* milliseconds */);
					urlConnection.setRequestMethod("GET");
					// urlConnection.setDoInput(true);
					urlConnection.connect();
					int response = urlConnection.getResponseCode();
//					Log.d(TAG, "The response is: " + response);
					inputStream = urlConnection.getInputStream();

					readStream(inputStream);
				} catch (MalformedURLException e) {
					Log.i("Amy", "MalformedURLException   "+e+" ");
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
					Log.i("Amy", "IOException   "+e+" ");
				} finally {
					if(urlConnection!=null){
						urlConnection.disconnect();
						disableConnectionReuseIfNecessary();
					}
					try {
						if (inputStream != null) {
							inputStream.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				super.run();
			}
		}.start();
	}
	/**
	 * 返回结果
	 * 
	 * @param inputStream
	 */
	private void readStream(InputStream inputStream) {
		String value = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, "UTF-8"));
			StringBuffer buf = new StringBuffer();
			String str;

			while ((str = reader.readLine()) != null) {
				buf.append(str);
			}
			value = buf.toString();

			Message msg = new Message();
			msg.what = CODE;
			msg.obj = value;
			handler.sendMessage(msg);
			Log.d(TAG, "大众点评返回的数据是：" + value);

		} catch (Exception e) {
		}
	}
	private void disableConnectionReuseIfNecessary() {
		if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
			System.setProperty("http.keepAlive", "false");
		}
	}
	public interface DataCallBack {
   	 void onDataCallBack(List<String> list);
//		void onDataCallBack(String result,List<BussinessInfoEntity> list);
   }
	

}
