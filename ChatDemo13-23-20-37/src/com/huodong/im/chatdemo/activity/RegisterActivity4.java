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
import org.json.JSONObject;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.view.HeaderLayout.onRighTextClickListener;
import com.huodong.im.utils.Encapsulation;

public class RegisterActivity4 extends BaseActivity {
	private TextView tvshowphonenumber;
	private EditText etreceivedcode;
	// 定义一些用户的参数
	String usernumber, userpassword, username, userphotopath, userbrithday;
	String usersex;
	// 填写从短信SDK应用后台注册得到的APPKEY
	private static String APPKEY = "ea096ef26e8e";
	// 填写从短信SDK应用后台注册得到的APPSECRET
	private static String APPSECRET = "9fa66c9059548efb55fb8bb358ec5e77";
	static String  NICKURL4="http://120.24.2.49:8787/yj2/servlet/AddUserServlet";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getnickname();
		setContentView(R.layout.activity_register4);
		init();

		initview();
	}

	// 初始化一些操作及控件
	private void init() {
		// TODO Auto-generated method stub
		tvshowphonenumber = (TextView) findViewById(R.id.tv_showphonenumber);
		tvshowphonenumber.setText(usernumber);
		etreceivedcode = (EditText) findViewById(R.id.et_receivedcode);
		initTopBarforRightTextno("个人资料(4/4)", "完成");
		SMSSDK.initSDK(this, APPKEY, APPSECRET, true);
		EventHandler eh = new EventHandler() {

			@Override
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				mHandler.sendMessage(msg);
			}

		};
		SMSSDK.registerEventHandler(eh);
		Log.i("info------------------------", usernumber);
//		SMSSDK.getVerificationCode("86",usernumber);
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {

			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			Log.e("event", "event=" + event);
			if (result == SMSSDK.RESULT_COMPLETE) {
				
				// 短信注册成功后，返回MainActivity,然后提示新好友
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
					Toast.makeText(RegisterActivity4.this, "验证成功",
							Toast.LENGTH_SHORT).show();
					// 注册
					JugdeNick3(username, userbrithday, usersex, usernumber,userpassword);

				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					// 已经验证
					Toast.makeText(RegisterActivity4.this, "验证码已经发送",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				((Throwable) data).printStackTrace();
				int status = 0;	
					try {
						((Throwable) data).printStackTrace();
						Throwable throwable = (Throwable) data;

						JSONObject object = new JSONObject(throwable.getMessage());
						String des = object.optString("detail");
						status = object.optInt("status");
						if (!TextUtils.isEmpty(des)) {
							Toast.makeText(RegisterActivity4.this, des, Toast.LENGTH_SHORT).show();
							return;
						}
					} catch (Exception e) {
						SMSLog.getInstance().w(e);
					}

			}

		};
	};

	private void initview() {
		// TODO Auto-generated method stub
		etreceivedcode.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (s.length() > 0) {
					initTopBarforRightText("个人资料(4/4)", "完成",
							new onRighTextClickListener() {

								@Override
								public void onClick() {
									// TODO Auto-generated method stub
									SMSSDK.submitVerificationCode("86",
											usernumber, etreceivedcode
													.getText().toString());
								
								}
							});
				} else {
					initTopBarforRightTextno("个人资料(4/4)", "完成");
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

	private void getnickname() {
		SharedPreferences sp = getSharedPreferences("nickname", MODE_PRIVATE);
		username = sp.getString("nickname1", null);
		userbrithday = sp.getString("birthday", null);
		usernumber = sp.getString("usernumber", null);
		userphotopath = sp.getString("photopath", null);
		userpassword = sp.getString("userpassword", null);
		usersex = sp.getString("sex", null);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		SMSSDK.unregisterAllEventHandler();
	}
	/*
	 * 添加用户部分代码
	 */
	String result=null;
	String a=null;
	private void JugdeNick3(final String nick,final String brithday,final String sex,final String phone,final String password)
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
					Log.i("info", nick+":"+brithday+":"+sex+":"+phone+":"+password);
					HttpPost httpPost=new HttpPost(NICKURL4);
					List<NameValuePair> params=new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("name", nick));
					params.add(new BasicNameValuePair("phone", phone));
					params.add(new BasicNameValuePair("code", password));
					params.add(new BasicNameValuePair("sex", sex));
					params.add(new BasicNameValuePair("birthday", brithday));
					UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"utf-8");
					httpPost.setEntity(entity);					
					HttpResponse httpResponse=httpClient.execute(httpPost);
						HttpEntity entity1=httpResponse.getEntity();
						result=EntityUtils.toString(entity1,"utf-8");
						Log.i("info", result);
						Message msg=new Message();
						Bundle data=new Bundle();
						data.putString("key4", result);
						msg.setData(data);
						handler4.sendMessage(msg);
						
						
					
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
	Handler handler4 = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			result=msg.getData().getString("key4");
			a=Encapsulation.analysisuser(result);
			
			if(a.equals("true"))
			{
				//注册用户成功
				Toast.makeText(RegisterActivity4.this, "注册用户成功", Toast.LENGTH_SHORT).show();
				startAnimActivity(LoginActivity.class);
			}else 
			{
				//注册用户失败
				//注册用户成功
				Toast.makeText(RegisterActivity4.this, "注册用户失败", Toast.LENGTH_SHORT).show();
			}
			
			
		}
		
	};
}
