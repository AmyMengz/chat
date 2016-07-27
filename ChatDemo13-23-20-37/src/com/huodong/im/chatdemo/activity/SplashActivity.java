package com.huodong.im.chatdemo.activity;

import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.R.layout;
import com.huodong.im.config.LoginSp;
import com.huodong.im.config.SharePreferenceHelper;
import com.huodong.im.utils.ImageLoaderUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SplashActivity extends Activity {
	private RelativeLayout rootLayout;
	private TextView versionText;
	private static final int sleepTime = 2000;
	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.activity_splash);
		super.onCreate(arg0);
		initUI();
		
	}
	private void initUI() {
		rootLayout=(RelativeLayout)findViewById(R.id.splash_root);
		versionText=(TextView)findViewById(R.id.tv_version);
		versionText.setText(getVersion());
		AlphaAnimation animation=new AlphaAnimation(0.3f, 1.0f);
		animation.setDuration(1500);
		rootLayout.setAnimation(animation);
		ImageLoaderUtil.initImageLoaderConfig(getApplicationContext());//imageloader初始化 暂时先放在这里
	}
	@Override
	public void onStart(){
		super.onStart();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
//				LoginSp.getInstance().init(getApplicationContext());
				if(SharePreferenceHelper.getInstance().isLogined()){
					//去登陆进入主界面
					long start = System.currentTimeMillis();
//					EMGroupManager.getInstance().loadAllGroups();
//					EMChatManager.getInstance().loadAllConversations();
					long costTime = System.currentTimeMillis() - start;
					//等待sleeptime时长
					if (sleepTime - costTime > 0) {
						try {
							Thread.sleep(sleepTime - costTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					//进入主页面
					startActivity(new Intent(SplashActivity.this,MainActivity.class));
					finish();
				}
				else {//不自动登录进入login
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//					startActivity(new Intent(SplashActivity.this, MainActivity.class));
					finish();
				}
			}
		}).start();
	}
	/**
	 * 获取当前应用程序的版本号
	 */
	private String getVersion() {
		String versionTips=getResources().getString(R.string.Version_number_is_wrong);
		PackageManager pm=getPackageManager();
		try {
			PackageInfo packageInfo=pm.getPackageInfo(getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return versionTips;
		}
	}

}
