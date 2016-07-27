package com.huodong.im.chat.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.huodong.im.chat.db.YCOpenHelperTest;
import com.huodong.im.chat.mode.ChatMode;
import com.huodong.im.chat.util.HttpUtil.ResponseListner;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class SetReadUtil {
	private YCOpenHelperTest yDB;

	public SetReadUtil(Context context) {
		super();
		yDB = new YCOpenHelperTest(context);
	}

	HashMap<Integer, Integer> mHasSetReadMap = new HashMap<Integer, Integer>();

	public void setOfLocal(int id, int isRead) {
		if (!mHasSetReadMap.containsKey(id)) {
			mHasSetReadMap.put(id, 1);
			yDB.setChatReadOfLocal(id);
		}
	}

	public static int setNetReadChatId;
	public static HashMap<Integer, Boolean> mSetNetReadMap = new HashMap<Integer, Boolean>();

	public static void setOfNet(int chatid, int isRead, Context context) {
		if (chatid == 0)
			return;
		if (isRead != 2) {
			if (chatid > setNetReadChatId) {
				setNetReadChatId = chatid;
				commitRead(setNetReadChatId, context);
			}
		}
	}

	public static boolean isCommitRead = false;

	public static void forceCommitRead(Context context) {
		if (mSetNetReadMap.containsKey(setNetReadChatId)) {
			Boolean boolean1 = mSetNetReadMap.get(setNetReadChatId);
			if(!boolean1){
				isCommitRead = false;
				commitRead(setNetReadChatId, context);
			}
		}
	}

	public static void commitRead(final int chatid, final Context context) {
		if (isCommitRead)
			return;
		isCommitRead = true;
		mSetNetReadMap.put(chatid, false);
		HttpUtil httpUtil = new HttpUtil();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(ChatMode.ID, String.valueOf(chatid)));
		httpUtil.post(params, IMConstants.URL_SET_READ);
		httpUtil.setResponseListner(new ResponseListner() {

			@Override
			public void result(String result) {
				if (!TextUtils.isEmpty(result)) {
					try {
						JSONObject json = new JSONObject(result);
						boolean flag = json.getBoolean(IMConstants.FLAG);
						if (flag) {
							mSetNetReadMap.put(chatid, true);
							YCOpenHelperTest yc = new YCOpenHelperTest(context);
							yc.setChatReadOfNet(chatid);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				isCommitRead = false;
				if (setNetReadChatId > chatid) {
					commitRead(setNetReadChatId, context);
				}
			}
		});
	}

}
