package com.huodong.im.chatdemo.activity;

import java.io.DataOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.R.string;
import com.huodong.im.chatdemo.model.UserData;
import com.huodong.im.chatdemo.view.HeaderLayout.onRighTextClickListener;
import com.huodong.im.utils.Encapsulation;

public class RegisterActivity1 extends BaseActivity{
	TextView tvclase,tvtitle;
	Button btleft,buttonright;
	EditText etusername;
	static String  NICKURL="http://120.24.2.49:8787/yj2/servlet/UserServlet";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register1);
		initViews();
		
	}
	private void initViews() {
		// TODO Auto-generated method stub
		tvclase=(TextView) findViewById(R.id.tv_clase);
		etusername=(EditText) findViewById(R.id.id_username);
		initTopBarforRightTextno("个人资料(1/4)", "下一步");
		//点击了阅读条款
		tvclase.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "阅读条款", Toast.LENGTH_SHORT).show();
			}
		});
		etusername.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.length()>0)
				{
					//右边文字有点击效果
					initTopBarforRightText("个人资料(1/4)", "下一步", new onRighTextClickListener() {
						
						@Override
						public void onClick() {
							// TODO Auto-generated method stub
							
							JugdeNick1(etusername.getText().toString());
							
							
							
							
							
						}
					});
				}else{
					//右边文字没点击效果
					initTopBarforRightTextno("个人资料(1/4)", "下一步");
				}
			}
			
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			result=msg.getData().getString("key");
			a=Encapsulation.analysis(result);
			
			if(a.equals("true"))
			{
				Toast.makeText(RegisterActivity1.this, "用户名已被使用，请重新输入！", Toast.LENGTH_SHORT).show();
			}else if(a.equals("false")&&!TextUtils.isEmpty(a))
			{
				savenickname();
				startAnimActivity(RegisterActivity2.class);
			}
			
			
		}
		
	};
	
	String result=null;
	String a=null;
	private void JugdeNick1(final String nick)
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpClient httpClient = null;
				
				try {
					httpClient=new DefaultHttpClient();
					//设置超时时间
					httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,8000);
					httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,8000);
					HttpPost httpPost=new HttpPost(NICKURL);
					List<NameValuePair> params=new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("type", "1"));
					params.add(new BasicNameValuePair("name", nick));
					UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"utf-8");
					httpPost.setEntity(entity);					
					HttpResponse httpResponse=httpClient.execute(httpPost);
						HttpEntity entity1=httpResponse.getEntity();
						result=EntityUtils.toString(entity1,"utf-8");
						Message msg=new Message();
						Bundle data=new Bundle();
						data.putString("key", result);
						msg.setData(data);
						handler.sendMessage(msg);
						
						
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					
				}finally{
					if (httpClient != null) {
					
						httpClient.getConnectionManager().shutdown();
						httpClient = null;
					
						}
				}
			}
		}).start();
		
		
	}
	/*
	 * 储存一下用户姓名
	 */
	private void savenickname()
	{
		 SharedPreferences sp = this.getSharedPreferences("nickname", MODE_PRIVATE);
	        Editor editor = sp.edit();
	        editor.putString("nickname1", etusername.getText().toString());
	        editor.commit();
	       
	       
	}
}
