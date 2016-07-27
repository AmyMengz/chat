package com.huodong.im.chatdemo.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.huodong.im.chatdemo.R;
import com.huodong.im.utils.Encapsulation;
public class ResetPasswordActivity extends BaseActivity{
	private Button btgetcode,btverify;
	private EditText etphonenumber,etcode;
	private String number;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activty_resetpass);
			btgetcode=(Button) findViewById(R.id.bt_getcode);
			btverify=(Button) findViewById(R.id.bt_verify);
			etphonenumber=(EditText) findViewById(R.id.et_phonenumber);
			etcode=(EditText) findViewById(R.id.et_code);
			initTopBarForLeft("重置密码");
			//跳转到支持的国家列表
			findViewById(R.id.linearlayout).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "点击国家列表", Toast.LENGTH_SHORT).show();	
				}
			});
			btgetcode.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					number=etphonenumber.getText().toString();
					if(Encapsulation.isMobileNum(number))
					{
						getcode();
					}else{
						Toast.makeText(getApplicationContext(), "您输入的手机号码有误", Toast.LENGTH_SHORT).show();
					}
					
					
				}
			});
			btverify.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 String newnumber=etphonenumber.getText().toString();
					if(newnumber.equals(number))
					{
						
						Toast.makeText(getApplicationContext(), "tiaojin", Toast.LENGTH_SHORT).show();	
						if(judgecodeformat(etcode.getText().toString()))
						{
							judgecode(etcode.getText().toString());
						}
						number=newnumber;
					}else{
						Toast.makeText(getApplicationContext(), "手机号码已经被更改", Toast.LENGTH_SHORT).show();	
					}
				}
			});
			
		}
//		/*
//		 * 验证手机号码格式是否正确
//		 */
//		public  boolean isMobileNum(String mobiles) {
//			Pattern p = Pattern
//			.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//			Matcher m = p.matcher(mobiles);
//			System.out.println(m.matches() + "---");
//			return m.matches();
//
//			}
		/*
		 * 获取验证码
		 */
		public void getcode()
		{
			Toast.makeText(getApplicationContext(), "点击获取验证码", Toast.LENGTH_SHORT).show();
		}
		/*
		 * 判断验证码格式是否正确
		 */
		public boolean judgecodeformat(String code)
		{
			Pattern p = Pattern
					.compile("^d{6}$");
			Matcher m = p.matcher(code);
			return m.matches();
		}
		/*
		 * 判断验证码是否正确
		 */
		public void judgecode(String code)
		{
			Toast.makeText(getApplicationContext(), "正在验证", Toast.LENGTH_SHORT).show();
		}
		public void verify(String code)
		{
			Toast.makeText(getApplicationContext(), "点击验证按钮", Toast.LENGTH_SHORT).show();	
		}
		public void judge()
		{
			
		}
}
