package com.huodong.im.chatdemo.activity;

import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.view.HeaderLayout.onRighTextClickListener;

import android.os.Bundle;

public class SettingRedastActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settingredast);
		initTopBarforRightText("编辑个人信息", "保存", new onRighTextClickListener() {
			
			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				//保存
			}
		});
	}
}
