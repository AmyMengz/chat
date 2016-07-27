package com.huodong.im.chat.util;

import com.huodong.im.chat.ApplyFdsActivity;
import com.huodong.im.chat.ChatActivity;
import com.huodong.im.chat.db.YCOpenHelperTest;
import com.huodong.im.chat.mode.ChatMode;
import com.huodong.im.chat.mode.UserMode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public class ToActivityUtil {
	
	public static void applyFdsForResult(int uid,int uid2,String name2,Context context,int type){
		Intent intent = new Intent(context, ApplyFdsActivity.class);
		intent.putExtra(UserMode.UID, uid);
		intent.putExtra(UserMode.UID2, uid2);
		intent.putExtra(UserMode.NAME2, name2);
		intent.putExtra(IMConstants.TYPE, type);
		// context.startActivity(intent);
		((Activity) context).startActivityForResult(intent, IMConstants.ChaType.APPLY_FDS);
	}
	public static void chat(int uid,Context context, String name){
		Intent intent = new Intent(context, ChatActivity.class);
		intent.putExtra(ChatMode.UID, uid);
		if(TextUtils.isEmpty(name)){
			YCOpenHelperTest yDB = new YCOpenHelperTest(context);
			name = yDB.getName(uid);
			if(TextUtils.isEmpty(name))
				return;
		}
		intent.putExtra(ChatMode.NAME, name);
		context.startActivity(intent);
	}

}
