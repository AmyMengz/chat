package com.huodong.im.chat.util;

import com.huodong.im.chat.ChatActivity;
import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.activity.MainActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NoticUtil {
	
	public static void show(Context context) {
		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = "您有新的消息！";
		long when = System.currentTimeMillis();
		// 第二个参数为弹出通知瞬间的提示
		Notification notification = new Notification(icon, tickerText, when);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		// notification.flags = Notification.DEFAULT_SOUND;
		notification.defaults = Notification.DEFAULT_SOUND;
		notification.audioStreamType = android.media.AudioManager.ADJUST_LOWER;
		// 内容
		CharSequence contentText = "点击查看";
		Intent intent = new Intent(context, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);
		// 第二个参数为标题
		notification.setLatestEventInfo(context, tickerText, contentText, contentIntent);
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(0, notification);
	}

}
