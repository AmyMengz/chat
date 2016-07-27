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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apaches.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huodong.im.config.SharePreferenceHelper;
import com.huodong.im.config.UrlConstant;
import com.huodong.im.entity.BussinessDealEntity;
import com.huodong.im.entity.BussinessInfoEntity;
import com.huodong.im.utils.IMDateUserHelper;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GetDZDPData {
	private static GetDZDPData instance=null;
	String TAG="Amy";
	private static final int CODE = 1;
	private double myLongitude,myLatitude;//=SharePreferenceHelper
	public GetDZDPData(){
		myLongitude=SharePreferenceHelper.getInstance().getLocationLog();
		myLatitude=SharePreferenceHelper.getInstance().getLocationLat();
	}
	/*private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CODE:
//				receiveContent.setText(msg.obj.toString());
				parseJSONResult(msg.obj.toString());
				break;

			default:
				break;
			}
		}	
	};*/
	private List<BussinessInfoEntity> parseJSONResult(String string,boolean isDiscount) {
		List<BussinessInfoEntity> list=new ArrayList<BussinessInfoEntity>();
		try {
			
			JSONObject json=new JSONObject(string);
			String status=json.getString("status");
			if(status.equals("OK")){
				String counts=json.getString("count");
				int count=Integer.parseInt(counts);
				Log.i("Amy", "count  "+count);
				JSONArray bussinesArray=json.getJSONArray("businesses");
				for (int i = 0; i < count; i++) {
					JSONObject bussiness=bussinesArray.getJSONObject(i);
					BussinessInfoEntity bussinessInfo=new BussinessInfoEntity();
					bussinessInfo.setName(bussiness.getString("name"));
					bussinessInfo.setAddress(bussiness.getString("address"));
					bussinessInfo.setTeltphone(bussiness.getString("telephone"));
					bussinessInfo.setLatitude(bussiness.getDouble("latitude"));
					bussinessInfo.setLongtitude(bussiness.getDouble("longitude"));
					bussinessInfo.setPhotoUrl(bussiness.getString("photo_url"));
					bussinessInfo.setRatingImgUrl(bussiness.getString("rating_img_url"));
					bussinessInfo.setAvgPrice(bussiness.getString("avg_price"));
					JSONArray regions=bussiness.getJSONArray("regions");
					int len=regions.length();
					String region="";
					for (int j = 0; j <len; j++) {
						region+=" "+regions.getString(j);
					}
					bussinessInfo.setRegions(region);
					double distance=IMDateUserHelper.Distance(myLongitude, myLatitude, bussiness.getDouble("longitude"), bussiness.getDouble("latitude"));
					Log.i("Amy disssss", distance+" ");
					bussinessInfo.setDistance(IMDateUserHelper.formatDistance(distance));
					
					int dealCount=Integer.parseInt(bussiness.getString("deal_count"));
					bussinessInfo.setDeals(dealCount);
					if(dealCount>0){
						JSONArray dealArray=bussiness.getJSONArray("deals");
						for (int j = 0; j < dealCount; j++) {
							JSONObject dealObj=dealArray.getJSONObject(j);
							BussinessDealEntity deal=new BussinessDealEntity(dealObj.getString("description")
									, dealObj.getString("url"));
							bussinessInfo.getDealList().add(deal);
						}
					}
					list.add(bussinessInfo);
//					adapter.notifyDataSetChanged();
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
	
	public static GetDZDPData getInstance(){
		if(instance==null){
			instance=new GetDZDPData();
		}
		return instance;
	}
	Handler handler;
	/**
	 * 从大众点评获取相应数据
	 * 
	 * @param codes
	 */
	public void getDataFormNet(final URL url,final boolean isDiscount,final DataCallBack callBack) {
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case CODE:
//					receiveContent.setText(msg.obj.toString());
//					parseJSONResult(msg.obj.toString());
					List<BussinessInfoEntity> list=parseJSONResult(msg.obj.toString(),isDiscount);
					if(list!=null){
//						for (int i = 0; i < list.size(); i++) {
							 Collections.sort(list);
//						}
					}
					callBack.onDataCallBack(list);
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
					Log.i(TAG+"3-18", "url  "+url);
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
   	 void onDataCallBack(List<BussinessInfoEntity> list);
//		void onDataCallBack(String result,List<BussinessInfoEntity> list);
   }
	

}
