package com.huodong.im.chatdemo.activity;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.huodong.im.chat.service.ChatService;
import com.huodong.im.chat.service.ChatServiceConn;
import com.huodong.im.chat.util.IMConstants;
import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.fragment.BaseFragment;
import com.huodong.im.chatdemo.fragment.ChatFragment;
import com.huodong.im.chatdemo.fragment.ContactsFragment;
import com.huodong.im.chatdemo.fragment.NearbyFragment;
import com.huodong.im.chatdemo.fragment.NewDateFragment;
import com.huodong.im.chatdemo.fragment.NewDateStartingFragmentTab;
import com.huodong.im.chatdemo.fragment.SettingFragment;
import com.huodong.im.chatdemo.service.IMLocationService;
import com.huodong.im.chatdemo.service.LocationService;
import com.huodong.im.config.FragmentsAvailable;
import com.huodong.im.config.IntentConstant;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends FragmentActivity {
	
	
//	private LocationService locationService;
	
	private NearbyFragment mNearbyFragment=null;
	private ChatFragment mChatFragment=null;
	private NewDateFragment mNewDateFragment=null;
	private ContactsFragment mContactsFragment=null;
	private SettingFragment mSettingFragment=null;
	
//	private Button BtnNearby,BtnCaht,BtnNewDate,BtnContacts,Btnsetting;
	private FragmentsAvailable currentFragment, nextFragment;
	
	private Fragment[] fragments;
	private Button[] mTabs;
	public int index;
	public int getIndex() {
		return index;
	}
	// 当前fragment的index
	private int currentTabIndex;
	public static Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		initHandler();
		

		startChatService();

		startService(new Intent(MainActivity.this, IMLocationService.class));
		// -----------location config ------------
		/*locationService=((MyApplication)getApplication()).locationService;
		locationService.registerListener(mListener);
		locationService.start();// 定位SDK
*/		
		mNearbyFragment = new NearbyFragment();
		mChatFragment = new ChatFragment(MainActivity.this);
		mNewDateFragment = new NewDateFragment();
		mContactsFragment = new ContactsFragment(MainActivity.this);
		mSettingFragment = new SettingFragment();
		fragments = new Fragment[] { mNearbyFragment, mChatFragment, mNewDateFragment,mContactsFragment, mSettingFragment};
	
		// 添加显示第一个fragment
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mNearbyFragment).commit();
		initView();
		
	}
//	private void initHandler() {
//		handler=new Handler(){
//			@Override
//			public void handleMessage(Message msg){
//				mNearbyFragment.;
//			}
//		};
//	}
	private void initView() {
		mTabs = new Button[5];
		mTabs[0] = (Button) findViewById(R.id.btn_nearby);
		mTabs[1] = (Button) findViewById(R.id.btn_conversation);
		mTabs[2] = (Button) findViewById(R.id.btn_satrt_date);
		mTabs[3] = (Button) findViewById(R.id.btn_contacts);
		mTabs[4] = (Button) findViewById(R.id.btn_setting);
		mTabs[0].setSelected(true);
	}
	/**
	 * button点击事件
	 * 
	 * @param view
	 */
	public void onTabClicked(View view) {
		switch (view.getId()) {
		case R.id.btn_nearby:
			index = 0;
			break;
		case R.id.btn_conversation:
			mChatFragment.judgeToUpdateData();
			index = 1;
			break;
		case R.id.btn_satrt_date:
			index = 2;
			break;
		case R.id.btn_contacts:
			index = 3;
			break;
		case R.id.btn_setting:
			index = 4;
			break;
		}
		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				trx.add(R.id.fragment_container, fragments[index]);
			}
			trx.show(fragments[index]).commit();
//			trx.replace(R.id.fragment_container, fragments[index]);
//			trx.commit();
		}
		mTabs[currentTabIndex].setSelected(false);
		// 把当前tab设为选中状态
		mTabs[index].setSelected(true);
		currentTabIndex = index;
			
//	}
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
//		locationService.unRegistenerListener(mListener); //注销掉监听
//		locationService.stop(); //停止定位服务
		super.onStop();
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		System.out.print("onStart()");
		// -----------location config ------------
//		locationService=((MyApplication)getApplication()).locationService;
//		locationService.registerListener(mListener);
//		locationService.start();// 定位SDK
//		locationService.set
	}
	private BDLocationListener mListener=new BDLocationListener() {
		
		@Override
		public void onReceiveLocation(BDLocation location) {
			String loc="";
			double lat=location.getLatitude();
			double log=location.getLongitude();
			loc=String.valueOf(lat)+"     "+log;
			Log.i("Amy Main 135", "loc   "+loc);
			System.out.println("loc  "+loc);
//			LocationResult.setText(loc);
		}
	};
	
	private void startChatService() {
		Intent intent = new Intent(MainActivity.this, ChatService.class);
		startService(intent);
		ChatServiceConn conn = new ChatServiceConn(MainActivity.this);
		bindService(intent, conn, Context.BIND_AUTO_CREATE);
	}
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch (requestCode) {
		case IntentConstant.REQUESTCODE_ADDRESS:
			mNewDateFragment.onActivityResult(requestCode, resultCode, data);
			break;
		case IntentConstant.REQUESTCODE_SELECTION:
			mNearbyFragment.onActivityResult(requestCode, resultCode, data);
			break;
		case IntentConstant.REQUESTCODE_DATE_DETAIL://需要更新约会列表 正在发起的约会也要更新
			boolean refush=data.getExtras().getBoolean(IntentConstant.RESULT_DATE_REFUSH); 
			if(refush){
				mNearbyFragment.onActivityResult(requestCode, resultCode, data);
				mNewDateFragment.onActivityResult(requestCode, resultCode, data);
			}
			break;
		}
	}
	
	
	@Override
		protected void onResume() {
			super.onResume();
		}

}
