package com.huodong.im.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import com.huodong.im.config.HandlerConstants;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class LoadDataFromServer {
	
	private String url;
    private Map<String, String> map = null;
    private Context context;
	
	public LoadDataFromServer(Context context, String url,
            Map<String, String> map){
		this.context=context;
		this.url=url;
		this.map=map;
		
	}
	
	 public void getData(final DataCallBack dataCallBack) {
////		 Handler ha=new 
		 Looper.prepare();
		 final Handler handler=new Handler(Looper.getMainLooper()){
				@Override
				public void handleMessage(Message msg){
					switch (msg.what) {
					case HandlerConstants.POST_OK:
						if(dataCallBack!=null){
							String result=(String) msg.obj;
							dataCallBack.onDataCallBack(result);
						}
						
						break;

					default:
						break;
					}
				}
			};
			
		 new Thread() {
	            @SuppressWarnings("rawtypes")
	            public void run() {
	            	Log.i("Amy", "looper22  "+"  "+Looper.myLooper());
	            	System.out.println("ssssss test ");
	                HttpClient client = new DefaultHttpClient();
	                List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>(); 
	                params = new LinkedList<BasicNameValuePair>(); 
	                Set keys = map.keySet();
	                if (keys != null) {
	                    Iterator iterator = keys.iterator();
	                    while (iterator.hasNext()) {
	                        String key = (String) iterator.next();
	                        String value=map.get(key);
//	                        params.add(new BasicNameValuePair(name, value));
	                        params.add(new BasicNameValuePair(key, value));  
	                    }
	                }
	              //设置链接超时
	                client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
	                // 设置读取超时(请求超时)
	                client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
	                HttpPost postMethod = new HttpPost(url); 
	                try {
						postMethod.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
						HttpResponse response = client.execute(postMethod); //执行POST方法
						Log.i("Amy", "resCode = " + response.getStatusLine().getStatusCode()); //获取响应码  
						 if (response.getStatusLine().getStatusCode() == 200) {
		                    	String result=EntityUtils.toString(response.getEntity());
		                    	Log.i("Amy", "result = " + result); //获取响应内   
		                          Message  msg= handler.obtainMessage();
		                          msg.what=HandlerConstants.POST_OK;
		                          msg.obj=result;
		                          handler.sendMessage(msg);
		                    }
						 else {
							
						}
//						   
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.i("Amy", "r = " + e); //获取响应内
						e.printStackTrace();
					}
	                
	            }
		 }.start();
		 Looper.loop();
	 }
	/**
     * 网路访问调接口
     *
     */
    public interface DataCallBack {
    	 void onDataCallBack(String result);
    }

}
