package com.huodong.im.chat;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.huodong.im.chat.db.YCOpenHelperTest;
import com.huodong.im.chat.mode.ApplyFdsMode;
import com.huodong.im.chat.mode.UserMode;
import com.huodong.im.chat.service.ChatInterface;
import com.huodong.im.chat.service.ChatServiceConn;
import com.huodong.im.chat.util.HttpUtil;
import com.huodong.im.chat.util.HttpUtil.ResponseListner;
import com.huodong.im.chat.util.IMConstants;
import com.huodong.im.chat.util.MyRotateAnimation;
import com.huodong.im.chat.util.SendBroadUtil;
import com.huodong.im.chat.util.StringUtil;
import com.huodong.im.chat.util.ToastUtil;
import com.huodong.im.chatdemo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

public class ApplyFdsActivity extends Activity {

	private String name2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_al_fds);
		ImageView iv_init_progress = (ImageView) findViewById(R.id.iv_init_progress);
		MyRotateAnimation.rotateAnimation(iv_init_progress);
		Intent intent = getIntent();
		int uid = intent.getIntExtra(UserMode.UID, 0);
		int uid2 = intent.getIntExtra(UserMode.UID2, 0);
		name2 = intent.getStringExtra(UserMode.NAME2);
		type = intent.getIntExtra(IMConstants.TYPE, 0);
		initString();
		applyFds(uid, uid2);
		// 不能向自己申请
	}

	private String mFdsGetFail, re_get, fds_no, fds_fail, getting_fds, apply_fds_fail, connect_error, apply_fds,
			has_apply_fds;
	private int type;

	private void initString() {
		getting_fds = getResources().getString(R.string.getting_fds);
		fds_fail = getResources().getString(R.string.fds_fail);
		re_get = getResources().getString(R.string.re_get);
		fds_no = getResources().getString(R.string.fds_no);
		apply_fds_fail = getResources().getString(R.string.apply_fds_fail);
		connect_error = getResources().getString(R.string.connect_error);
		apply_fds = getResources().getString(R.string.apply_fds);
		has_apply_fds = getResources().getString(R.string.has_apply_fds);
	}

	private String strApplyTo(String name) {
		return has_apply_fds + " " + name + " " + apply_fds;
	}

	private YCOpenHelperTest yDB;

	private void applyFds(int uid, final int uid2) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(UserMode.UID, String.valueOf(uid)));
		params.add(new BasicNameValuePair(UserMode.UID2, String.valueOf(uid2)));
		HttpUtil httpUtil = new HttpUtil();
		httpUtil.post(params, IMConstants.URL_APPLY_FRIENDS, 500l);
		httpUtil.setResponseListner(new ResponseListner() {
			@Override
			public void result(String result) {
				Intent intent = new Intent();
				if (yDB == null)
					yDB = new YCOpenHelperTest(ApplyFdsActivity.this);
				if (!TextUtils.isEmpty(result)) {
					Gson gson = new Gson();
					ApplyFdsMode applyFdsMode = gson.fromJson(result, ApplyFdsMode.class);
					boolean flag = applyFdsMode.getFlag();
					if (flag) {
						int type2 = applyFdsMode.getType();
						Long time = applyFdsMode.getTime();
						if (type2==3) {
							// 成为好友 跳到聊天
							ToastUtil.show(ApplyFdsActivity.this, "成为好友");
							intent.putExtra("result", "成为好友");
							yDB.insert(time, name2, IMConstants.ChaType.AGREE, uid2);
//							switch (type) {
//							case IMConstants.ChaType.AGREE:
//								yDB.update(IMConstants.ChaType.AGREE, uid2);
//								break;
//							case IMConstants.ChaType.APPLY_FDS:
//								yDB.insert(time, name2, IMConstants.ChaType.AGREE, uid2);
//								break;
//
//							default:
//								break;
//							}
							SendBroadUtil.sendGetContact(ApplyFdsActivity.this);
							sendMs(uid2,IMConstants.SK_ACTION_BE_FDS);
							setResult(IMConstants.ChaType.AGREE, intent);
						} else if(type2==2){
							// 申请成功 跳到聊天
							String strApplyTo = strApplyTo(name2);
							ToastUtil.show(ApplyFdsActivity.this, strApplyTo);
							yDB.insert(time, name2, IMConstants.ChaType.APPLY_FDS, uid2);
							intent.putExtra("result", "申请成功");
							sendMs(uid2,IMConstants.SK_ACTION_APPLY_FDS);
							setResult(IMConstants.ChaType.APPLY_FDS, intent);
						} else if(type2==4){
							ToastUtil.show(ApplyFdsActivity.this, StringUtil.getStr(ApplyFdsActivity.this, R.string.no_need_apply));
						}
					} else {
						// 错误
						ToastUtil.show(ApplyFdsActivity.this, apply_fds_fail);
						intent.putExtra("result", "错误");
					}

				} else {
					// 连接失败
					ToastUtil.show(ApplyFdsActivity.this, connect_error);
					intent.putExtra("result", "连接失败");
				}
				finish();
			}
		});

	}

	private void sendMs(int uid2,String content) {
		ChatInterface chat = ChatServiceConn.getChat();
		if(chat!=null){
			chat.newMessage(uid2,content);
		}
	}

}
