package com.huodong.im.chat.util;

import java.io.File;
import java.io.FileOutputStream;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {

	/**
	 * 判断是否有手机网络
	 * 
	 * @param context
	 *            上下文
	 * @return true：有手机网络
	 * 
	 *         false :没有手机网络
	 */
	public static boolean isMobileConnect(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		return mobileInfo.isConnected();
	}

	/**
	 * 判断是否有wifi网络
	 * 
	 * @param context
	 *            上下文
	 * @return true：有wifi网络
	 * 
	 *         false :没有wifi网络
	 */
	public static boolean isWifiConnect(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return wifiInfo.isConnected();
	}

	/**
	 * 检查网络是否可用
	 * 
	 * @param context
	 *            上下文
	 * @return true：有网络
	 * 
	 *         false :没有网络
	 */
	public static boolean iSHasNet(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnectedOrConnecting();
	}


}
