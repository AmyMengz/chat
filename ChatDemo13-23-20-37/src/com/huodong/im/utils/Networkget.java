package com.huodong.im.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

public class Networkget {
//	private Context context;
//	private GetNetIpListner listener;
//	private final String URL_NET_IP = "http://120.24.2.49:8787/yj2 /UserServlet";
//	private final String CHARSET_GBK = "utf-8";
//	private String nickname;
//	public Networkget(Context context) {
//		context = context;
//	}
//	private String getNetNick()
//	{
//		nickname = null;
//		URL infoUrl = null;
//		InputStream inStream = null;
//		try {
//			infoUrl = new URL(URL_NET_IP);
//			HttpURLConnection conn = (HttpURLConnection) infoUrl.openConnection();
//			conn.setRequestMethod("GET");
//			conn.setReadTimeout(5000);
//			conn.setConnectTimeout(5000);
//			int responseCode = conn.getResponseCode();
//			if (responseCode == 200) {
//				inStream = conn.getInputStream();
//				BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, CHARSET_GBK));
//				boolean isono=reader.readLine();
//				String line = null;
//				while ((line = reader.readLine()) != null)
//					strber.append(line + "\n");
//				inStream.close();
//				// 从反馈的结果中提取出IP地址
//				int start = strber.indexOf("[");
//				int end = strber.indexOf("]", start + 1);
//				String a = strber.substring(start + 1, end);
//				int i = 3 + strber.indexOf("来自：");
//				int j = strber.indexOf("</center>");
//				String b = strber.substring(i, j);
//				line=a+"("+b;
//				if (!TextUtils.isEmpty(line))
//					nickname = line;
//			}
//			conn.disconnect();
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//			return e.toString();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return e.toString();
//		}
//		return null;
//	}
}
