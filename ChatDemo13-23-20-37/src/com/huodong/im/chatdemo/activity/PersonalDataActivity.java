package com.huodong.im.chatdemo.activity;

import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.view.HeaderLayout.onRighTextClickListener;

import android.os.Bundle;

public class PersonalDataActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personaldata);
		initTopBarforRightText("个人资料", "编辑", new onRighTextClickListener() {
			
			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				startAnimActivity(SettingRedastActivity.class);
			}
		});
	}
}
