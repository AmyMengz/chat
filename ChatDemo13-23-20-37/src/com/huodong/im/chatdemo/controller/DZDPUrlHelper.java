package com.huodong.im.chatdemo.controller;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;

import org.apaches.commons.codec.digest.DigestUtils;

import android.util.Log;

import com.huodong.im.config.UrlConstant;

public class DZDPUrlHelper {
	public static final String TAG="tag";
	private Map<String, String> paramMap;
	public DZDPUrlHelper(Map<String, String> paramMap){
		this.paramMap=paramMap;
	}
	
	/**
	 * 对数据key进行字典排序
	 * 
	 * @return 拼接排序完成后的字符串
	 */
	public String sortForParamKey() {
		String[] keyArray = paramMap.keySet().toArray(new String[0]);
		Arrays.sort(keyArray);

		// 拼接有序的参数名-值串
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(UrlConstant.appkey);

		for (String key : keyArray) {
			Log.d(TAG, "排序后的key是这样的" + key+":"+paramMap.get(key));
			stringBuilder.append(key).append(paramMap.get(key));
		}

		stringBuilder.append(UrlConstant.secret);
		String codes = stringBuilder.toString();
		// 字符串连接后生成的字符串
		Log.d(TAG, "拼接排序完成后的字符串" + codes);
		return codes;
	}
	/**
	 * 对排序并拼接完成的字符串进行SHA-1编码
	 * @throws MalformedURLException
	 * @throws UnsupportedEncodingException
	 * @return 访问大众点评的正确URL地址
	 */
	public URL codecParams(String codes,String basicurl) throws MalformedURLException,
			UnsupportedEncodingException {
		// 生成sign SHA-1编码， 这里使用的是Apache codec，
		// 即可获得签名(shaHex()会首先将中文转换为UTF8编码然后进行sha1计算，使用其他的工具包请注意UTF8编码转换)
		String sign = DigestUtils.shaHex(codes).toUpperCase();
		Log.i("Amy 3-18", "sign  "+sign);

		// 以下sha1签名代码效果等同
		// byte[] sha = DigestUtils.sha(StringUtils.getBytesUtf8(codes));
		// String sign1 = Hex.encodeHexString(sha).toUpperCase();

		// 使用签名生成访问
		StringBuilder sb = new StringBuilder();
		sb.append("appkey=").append(UrlConstant.appkey).append("&sign=").append(sign);

		for (java.util.Map.Entry<String, String> entry : paramMap.entrySet()) {
			sb.append('&').append(entry.getKey()).append('=')
					.append(URLEncoder.encode(entry.getValue(), "utf-8"));// entry.getValue());
		}

//		String requestUrl = UrlConstant.DZDP_API_URL + "?" + sb.toString(); // +
															// URLEncoder.encode((
															// sb.toString()),
															// "UTF-8");
		String requestUrl = basicurl + "?" + sb.toString(); 
		Log.d(TAG, "after sign the url is " + requestUrl);

		return new URL(requestUrl);// requestUrl);
	}

}
