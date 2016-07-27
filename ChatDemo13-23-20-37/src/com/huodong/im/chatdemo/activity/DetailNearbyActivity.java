package com.huodong.im.chatdemo.activity;

import com.huodong.im.chat.util.IMConstants;
import com.huodong.im.chat.util.ToActivityUtil;
import com.huodong.im.chat.util.pic.ImageLoader;
import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.controller.NearbyUserDetailController;
import com.huodong.im.chatdemo.controller.NearbyUserDetailController.NearbyUserDateCallback;
import com.huodong.im.chatdemo.controller.NearbyUserDetailController.NearbyUserDetailCallback;
import com.huodong.im.config.IntentConstant;
import com.huodong.im.config.SharePreferenceHelper;
import com.huodong.im.entity.UserEntity;
import com.huodong.im.utils.IMDateUserHelper;
import com.huodong.im.utils.IMUIHelper;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DetailNearbyActivity extends BaseActivity implements OnClickListener{
	int hisuid;
	ImageView avatar;
	TextView age, star,fans,attention,distance,recentTime,signature,high,emotion,location,job,income,heDateCount,historyDateCount;
	TextView btnAddFriend,btnChat;
	RelativeLayout hisDate;
	double mLongtitud,mLatitude;
	int fansNum=0,attentionNum=0;
	String name;
	ImageLoader imageLoader;
	SharePreferenceHelper helper=SharePreferenceHelper.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearby_detail);
		mLongtitud=helper.getLocationLog();
		mLatitude=helper.getLocationLat();
		fansNum=getIntent().getIntExtra(IntentConstant.KEY_FANS, 0);
		name=getIntent().getStringExtra(IntentConstant.KEY_NICK_NAME);
		initTopBarForLeft(name);
		hisuid=getIntent().getIntExtra(IntentConstant.KEY_UID, 0);
		initView();
		initDate();
		imageLoader = new ImageLoader(DetailNearbyActivity.this, true);
		imageLoader.DisplayImage(String.valueOf(hisuid), avatar);
		
	}

	private void initDate() {
		NearbyUserDetailController controller=NearbyUserDetailController.getInstance(DetailNearbyActivity.this);
		controller.getUserBasicInfo(hisuid,new NearbyUserDetailCallback() {
			
			@Override
			public void onNearbyUserDetailCallback(Object obj) {
				Message msg=(Message)obj;
				switch (msg.what) {
				case IntentConstant.HANDLER_USER_INFO+IntentConstant.HANDLER_OK:
					if(msg.obj!=null){
						UserEntity entity=(UserEntity) msg.obj;
						switch (entity.getGender()) {
						case 1:
							age.setBackgroundResource(R.drawable.gender_boy);
							break;
						case 2:
							age.setBackgroundResource(R.drawable.gender_gril);
							break;
						}
						name=entity.getNickName();
						age.setText(String.valueOf(IMDateUserHelper.getUserAge(entity.getBirthday(), System.currentTimeMillis()/1000)));
						recentTime.setText(IMDateUserHelper.getRecentTime(entity.getRecentTime()));
						signature.setText(entity.getSignature());
						double dis=IMDateUserHelper.Distance(mLongtitud, mLatitude, entity.getLongitude(), entity.getLatitude()/1000);
						
						distance.setText(String.format(getString(R.string.distance_mail), 
								IMDateUserHelper.formatDistance(dis)));
						high.setText(entity.getHight()+"cm");
//						entity.getBirthday()
						star.setText(IMDateUserHelper.getConstellation(entity.getBirthday()));
						initTopBarForLeft(name);
					}
					break;

				case IntentConstant.HANDLER_USER_INFO+IntentConstant.HANDLER_ERROR:
					if(msg.obj!=null){
						ShowToast(getString(R.string.get_user_error_tip)+"  "+msg.obj);
					}
					else {
					ShowToast(getString(R.string.get_user_error_tip));
					}
					
					break;
				}
			}
		});
		controller.getDateCountInfo(hisuid,new NearbyUserDateCallback() {

			@Override
			public void onNearbyUserDateCallback(int date, int indate) {
				heDateCount.setText(String.valueOf(date));
				historyDateCount.setText(String.valueOf(indate));
			}
			
			
		});
	}

	private void initView() {
//		String name=getIntent().getStringExtra(IntentConstant.KEY_NICK_NAME);
//		initTopBarForLeft(name);
		btnAddFriend=(TextView)findViewById(R.id.add_attention);
		btnChat=(TextView)findViewById(R.id.chat);
		avatar=(ImageView)findViewById(R.id.avatar);
		age=(TextView)findViewById(R.id.gender_age);
		star=(TextView)findViewById(R.id.star);
		fans=(TextView)findViewById(R.id.fans);
		attention=(TextView)findViewById(R.id.attention);
		distance=(TextView)findViewById(R.id.distance);
		recentTime=(TextView)findViewById(R.id.recent_time);
		signature=(TextView)findViewById(R.id.signature);
		high=(TextView)findViewById(R.id.hight);
		emotion=(TextView)findViewById(R.id.emotion);
		location=(TextView)findViewById(R.id.location);
		job=(TextView)findViewById(R.id.job);
		income=(TextView)findViewById(R.id.income);
		heDateCount=(TextView)findViewById(R.id.date_count);
		historyDateCount=(TextView)findViewById(R.id.history_date_count);
		hisDate=(RelativeLayout)findViewById(R.id.his_date);
		hisDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				IMUIHelper.openHisDateActivity(DetailNearbyActivity.this, hisuid);
			}
		});
		fans.setText(String.format(getString(R.string.fans), fansNum));
		attention.setText(String.format(getString(R.string.attention), attentionNum));
		btnAddFriend.setOnClickListener(this);
		btnChat.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_attention:
			MyApplication yApp = MyApplication.getInstance();
			int uid = yApp.getUid();
			ToActivityUtil.applyFdsForResult(uid, hisuid, name, DetailNearbyActivity.this,IMConstants.ChaType.APPLY_FDS);
			break;
		case R.id.chat:
			ToActivityUtil.chat(hisuid, DetailNearbyActivity.this,name);
			break;

		}
	}

}
