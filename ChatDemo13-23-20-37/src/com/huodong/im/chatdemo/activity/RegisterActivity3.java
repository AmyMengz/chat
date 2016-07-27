package com.huodong.im.chatdemo.activity;

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

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.model.UserData;
import com.huodong.im.chatdemo.view.HeaderLayout.onRighTextClickListener;
import com.huodong.im.utils.Encapsulation;



public class RegisterActivity3 extends BaseActivity{
	private EditText raetnumber,raetpassword;
	String number,password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register3);
		init();
		
		initTopBarforRightTextno("个人资料(3/4)", "下一步");
	}
	public void init()
	{
		raetnumber=(EditText) findViewById(R.id.etrg_phonenumber);
		raetpassword=(EditText) findViewById(R.id.etrg_password);
		raetnumber.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				judgedoor();
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
		raetpassword.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				judgedoor();
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
	//判断下一步按钮是否恢复点击
	public void judgedoor()
	{
		if(!TextUtils.isEmpty(raetnumber.getText().toString())&&!TextUtils.isEmpty(raetpassword.getText().toString()))
		{
			initTopBarforRightText("个人资料(3/4)", "下一步", new onRighTextClickListener() {
				
				@Override
				public void onClick() {
					// TODO Auto-generated method stub
					if(Encapsulation.isMobileNum(raetnumber.getText().toString()))
					{
						JugdeNick3(raetnumber.getText().toString());
					}else{
						Toast.makeText(RegisterActivity3.this, "您输入的号码有误", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}else
		{
			initTopBarforRightTextno("个人资料(3/4)", "下一步");
		}
	}
	Handler handler3 = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			result=msg.getData().getString("key3");
			a=Encapsulation.analysis(result);
			
			if(a.equals("true"))
			{
				Toast.makeText(RegisterActivity3.this, "手机号码已被注册，请更换手机号码！", Toast.LENGTH_SHORT).show();
			}else if(a.equals("false")&&!TextUtils.isEmpty(a))
			{
				
				savephonenumber();
				startAnimActivity(RegisterActivity4.class);
			}
			
			
		}
		
	};
	String result=null;
	String a=null;
	private void JugdeNick3(final String nick)
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
					HttpPost httpPost=new HttpPost(RegisterActivity1.NICKURL);
					List<NameValuePair> params=new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("type", "1"));
					params.add(new BasicNameValuePair("phone", nick));
					UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"utf-8");
					httpPost.setEntity(entity);					
					HttpResponse httpResponse=httpClient.execute(httpPost);
						HttpEntity entity1=httpResponse.getEntity();
						result=EntityUtils.toString(entity1,"utf-8");
						Message msg=new Message();
						Bundle data=new Bundle();
						data.putString("key3", result);
						msg.setData(data);
						handler3.sendMessage(msg);
					
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
	
	public void savephonenumber()
	{
		 SharedPreferences sp = getSharedPreferences("nickname", MODE_PRIVATE);
	        Editor editor = sp.edit();
	        editor.putString("usernumber", raetnumber.getText().toString());
	        editor.putString("userpassword", raetpassword.getText().toString());
	        editor.commit();
	    
	}
}
