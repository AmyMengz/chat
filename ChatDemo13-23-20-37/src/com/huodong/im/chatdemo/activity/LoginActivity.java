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
import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.model.UserData;
import com.huodong.im.utils.Encapsulation;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
public class LoginActivity extends BaseActivity{
	private Button btlogin;
	private TextView tvnopassword,tvnouserID;
	private EditText etuserphonenumber,etuserpassword;
	static String  NICKURLLOGIN="http://120.24.2.49:8787/yj2/servlet/UserServlet";
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.acticity_login);
			initTopBarForOnlyTitle("用户登录");
			btlogin=(Button) findViewById(R.id.bt_login);
			tvnopassword=(TextView) findViewById(R.id.tv_nopassword);
			tvnouserID=(TextView) findViewById(R.id.tv_noid);
			etuserphonenumber=(EditText) findViewById(R.id.et_phonenumber);
			etuserpassword=(EditText) findViewById(R.id.et_password);
			btlogin.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(!TextUtils.isEmpty(etuserphonenumber.getText().toString())&&!TextUtils.isEmpty(etuserpassword.getText().toString()))
					{
						if(Encapsulation.isMobileNum(etuserphonenumber.getText().toString()))
						{
							JugdeNick3(etuserphonenumber.getText().toString(), etuserpassword.getText().toString());
						}else
						{
							Toast.makeText(LoginActivity.this, "您输入的手机号码有误", Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(LoginActivity.this, "请输入手机号码或密码", Toast.LENGTH_SHORT).show();
					}
				}
			});

			tvnouserID.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startAnimActivity(RegisterActivity1.class);
				}
			});
			tvnopassword.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//修改密码
					startAnimActivity(ResetPasswordActivity.class);
				}
			});
		}
		/*
		 * 登录
		 */
		String result=null;
		String a=null;
		private void JugdeNick3(final String number,final String password)
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
						
						HttpPost httpPost=new HttpPost(NICKURLLOGIN);
						List<NameValuePair> params=new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("type", "5"));
						params.add(new BasicNameValuePair("phone", number));
					
						params.add(new BasicNameValuePair("code", password));
					
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
				Log.i("infoinfoinfoinfo", result);
				a=Encapsulation.analysis(result);
				Log.i("infoinfoinfo", a+"");
				if(a.equals("false"))
				{
					//注册用户失败
					//注册用户成功
					Toast.makeText(LoginActivity.this, "手机或密码不正确！", Toast.LENGTH_SHORT).show();
				}else 
				{
					String userid=Encapsulation.analysis2(result);
					UserData.setPhonenumber("13686861522");
					UserData.setId(userid);
					Log.i("infoinfoinfo", userid);
					MyApplication yApp = MyApplication.getInstance();
					yApp.setUid(Integer.valueOf(userid));
					//进入主界面
					startAnimActivity(MainActivity.class);
				}
				
				
			}
			
		};
}
