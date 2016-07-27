package com.huodong.im.chatdemo.activity;

import android.os.Bundle;

import com.huodong.im.chatdemo.R;

public class NearbyUserDetailActivity extends BaseActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_date_detail);
		initTopBarForLeft("title");
	}
}
