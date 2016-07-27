package com.huodong.im.chatdemo.activity;

import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.view.HeaderLayout.onRighTextClickListener;
import com.huodong.im.config.IntentConstant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SelectionActivity extends BaseActivity implements OnClickListener{
	LinearLayout[] selectionView;//,userSelection;
	TextView[] genderLimit,feesLimit,dateTimeLimit,genderUser,activeUser;
	static int indexGl,indexFl,indexDl,indexGu,indexAu;
	int gllen,fllen,dllen,gulen,aulen;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selection);
		initData();
		initView();
		
	}

	private void initData() {
		//选择xml布局
		selectionView=new LinearLayout[2];
		int selectionIndex=getIntent().getIntExtra(IntentConstant.KEY_SELECTION_INDEX, 0);
		selectionView[0]=(LinearLayout)findViewById(R.id.date_select);
		selectionView[1]=(LinearLayout)findViewById(R.id.user_select);
		setSelectView(selectionIndex);
		//初始化textview
		genderLimit=new TextView[3];
		feesLimit=new TextView[5];
		dateTimeLimit=new TextView[4];
		genderUser=new TextView[3];
		activeUser=new TextView[4];
		
	}

	private void initView() {
		genderLimit[0]=(TextView)findViewById(R.id.gender_limit_no);
		genderLimit[1]=(TextView)findViewById(R.id.gender_limit_women);
		genderLimit[2]=(TextView)findViewById(R.id.gender_limit_men);
		
		feesLimit[0]=(TextView)findViewById(R.id.fees_limit_all);
		feesLimit[1]=(TextView)findViewById(R.id.fees_limit_sponsor);
		feesLimit[2]=(TextView)findViewById(R.id.fees_limit_men_a);
		feesLimit[3]=(TextView)findViewById(R.id.fees_limit_aa);
		feesLimit[4]=(TextView)findViewById(R.id.fees_limit_partner);
		
		dateTimeLimit[0]=(TextView)findViewById(R.id.time_limit_all);
		dateTimeLimit[1]=(TextView)findViewById(R.id.time_limit_today);
		dateTimeLimit[2]=(TextView)findViewById(R.id.time_limit_tommorrow);
		dateTimeLimit[3]=(TextView)findViewById(R.id.time_limit_week);
		
		genderUser[0]=(TextView)findViewById(R.id.gender_user_all);
		genderUser[1]=(TextView)findViewById(R.id.gender_user_women);
		genderUser[2]=(TextView)findViewById(R.id.gender_user_men);
		
		activeUser[0]=(TextView)findViewById(R.id.active_limit_no);
		activeUser[1]=(TextView)findViewById(R.id.active_limit_one);
		activeUser[2]=(TextView)findViewById(R.id.active_limit_three);
		activeUser[3]=(TextView)findViewById(R.id.active_limit_week);
		gllen=genderLimit.length;
		fllen=feesLimit.length;
		dllen=dateTimeLimit.length;
		gulen=genderLimit.length;
		aulen=activeUser.length;
		for (int i = 0; i < gllen; i++) {
			genderLimit[i].setOnClickListener(this);
		}
		for (int i = 0; i <fllen; i++) {
			feesLimit[i].setOnClickListener(this);
		}
		for (int i = 0; i <dllen; i++) {
			dateTimeLimit[i].setOnClickListener(this);
		}
		for (int i = 0; i <gulen; i++) {
			genderUser[i].setOnClickListener(this);
		}
		for (int i = 0; i <aulen; i++) {
			activeUser[i].setOnClickListener(this);
		}
		
		
		initTopBarforRightText(getString(R.string.selection), getString(R.string.ok), new onRighTextClickListener() {
			@Override
			public void onClick() {
				Intent intent=new Intent();
				intent.putExtra(IntentConstant.KEY_GENDER_USER, indexGu);//附近的用户性别
				intent.putExtra(IntentConstant.KEY_ACTIVE_USER, indexAu);//附近的人 活跃度
				
				intent.putExtra(IntentConstant.KEY_GENDER_LIMIT, indexGl);//附近的约会的性别限制
				intent.putExtra(IntentConstant.KEY_FEES_LIMIT, indexFl);
				intent.putExtra(IntentConstant.KEY_TIME_LIMIT, indexDl);
				SelectionActivity.this.setResult(RESULT_OK, intent);
				SelectionActivity.this.finish();
			}
		});
		setSelectGenderLimit(indexGl);
		setSelectFeesLimit(indexFl);
		setSelectTimeLimit();
		setSelectGenderUser();
		setSelectActiveUser();
	}

	private void setSelectView(int selectionIndex) {
		for (int i = 0; i < 2; i++) {
			selectionView[i].setVisibility(View.GONE);
		}
		selectionView[selectionIndex].setVisibility(View.VISIBLE);
		
	}
	/**
	 * 设置选中状态
	 * @param indexGl
	 */
	private void setSelectGenderLimit(int index) {
		for (int i = 0; i < gllen; i++) {
			genderLimit[i].setTextColor(getResources().getColor(R.color.black));		
		}
		genderLimit[indexGl].setTextColor(getResources().getColor(R.color.common_botton_bar_blue));		
	}
	private void setSelectFeesLimit(int index) {
		for (int i = 0; i < fllen; i++) {
			feesLimit[i].setTextColor(getResources().getColor(R.color.black));		
		}
		feesLimit[index].setTextColor(getResources().getColor(R.color.common_botton_bar_blue));		
	}
	private void setSelectTimeLimit() {
		for (int i = 0; i < dllen; i++) {
			dateTimeLimit[i].setTextColor(getResources().getColor(R.color.black));		
		}
		dateTimeLimit[indexDl].setTextColor(getResources().getColor(R.color.common_botton_bar_blue));		
	}
	private void setSelectGenderUser() {
		for (int i = 0; i < gulen; i++) {
			genderUser[i].setTextColor(getResources().getColor(R.color.black));		
		}
		genderUser[indexGu].setTextColor(getResources().getColor(R.color.common_botton_bar_blue));		
	}
	private void setSelectActiveUser() {
		for (int i = 0; i < aulen; i++) {
			activeUser[i].setTextColor(getResources().getColor(R.color.black));		
		}
		activeUser[indexAu].setTextColor(getResources().getColor(R.color.common_botton_bar_blue));		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gender_limit_no:
			indexGl=0;
			setSelectGenderLimit(indexGu);
			break;
		case R.id.gender_limit_women:
			indexGl=1;
			setSelectGenderLimit(indexGu);
			break;
		case R.id.gender_limit_men:
			indexGl=2;
			setSelectGenderLimit(indexGu);
			break;
		case R.id.fees_limit_all:
			indexFl=0;
			setSelectFeesLimit(indexFl);
			break;
		case R.id.fees_limit_sponsor:
			indexFl=1;
			setSelectFeesLimit(indexFl);
			break;
		case R.id.fees_limit_men_a:
			indexFl=2;
			setSelectFeesLimit(indexFl);
			break;
		case R.id.fees_limit_aa:
			indexFl=3;
			setSelectFeesLimit(indexFl);
			break;
		case R.id.fees_limit_partner:
			indexFl=4;
			setSelectFeesLimit(indexFl);
			break;
		case R.id.time_limit_all:
			indexDl=0;
			setSelectTimeLimit();
			break;
		case R.id.time_limit_today:
			indexDl=1;
			setSelectTimeLimit();
			break;
		case R.id.time_limit_tommorrow:
			indexDl=2;
			setSelectTimeLimit();
			break;
		case R.id.time_limit_week:
			indexDl=3;
			setSelectTimeLimit();
			break;
		case R.id.gender_user_all:
			indexGu=0;
			setSelectGenderUser();
			break;
		case R.id.gender_user_women:
			indexGu=1;
			setSelectGenderUser();
			break;
		case R.id.gender_user_men:
			indexGu=2;
			setSelectGenderUser();
			break;
		case R.id.active_limit_no:
			indexAu=0;
			setSelectActiveUser();
			break;
		case R.id.active_limit_one:
			indexAu=1;
			setSelectActiveUser();
			break;
		case R.id.active_limit_three:
			indexAu=2;
			setSelectActiveUser();
			break;
		case R.id.active_limit_week:
			indexAu=3;
			setSelectActiveUser();
			break;

		
		}
		
	}
	

}
